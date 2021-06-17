package budget.ui.add;

import budget.dao.TransactionDAO;
import budget.domain.Account;
import budget.domain.Category;
import budget.ui.ComboBoxRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.*;

public class AddTransactionDialog extends JDialog implements AddItemDialog {
    private static final Logger logger = LogManager.getLogger(AddTransactionDialog.class);
    private final boolean isIncome;
    private final int paddingLeft = 120;
    private JFormattedTextField valueTextField;
    private JComboBox<Account> accountComboBox;
    private JComboBox<Category> categoryComboBox;
    private JDatePickerImpl datePicker;
    private JTextArea noteTextArea;

    public AddTransactionDialog(JFrame frame, boolean isIncome) {
        setModal(true);
        setTitle("Добавить транзакцию");
        setSize(frame.getWidth(), frame.getHeight());
        setLocationRelativeTo(frame);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        this.isIncome = isIncome;
        createValueField();
        createAccountField();
        createCategoryField(isIncome);
        createDateField();
        createNoteField();
        createAddTransactionButton(isIncome);

        setVisible(true);
    }

    private void createValueField() {
        NumberFormat valueFormat = NumberFormat.getNumberInstance();
        valueFormat.setMaximumIntegerDigits(TransactionDAO.valueMaxIntDigits);
        valueFormat.setMaximumFractionDigits(TransactionDAO.valueMaxFractionDigits);
        valueTextField = new JFormattedTextField(valueFormat);

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Сумма", valueTextField);

        add(panel, setConstraints(1, 0, paddingLeft));
    }

    private void createAccountField() {
        accountComboBox = new JComboBox<>();
        accountComboBox.setRenderer(new ComboBoxRenderer());

        updateAccountComboBox();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Счёт", accountComboBox);
        add(panel, setConstraints(1, 1, paddingLeft));

        JButton addAccountButton = new JButton("Добавить счёт");
        addAccountButton.addActionListener(e -> new AddAccountDialog(this));
        add(addAccountButton, setConstraints(2, 1, new Insets(14, 10, 0, 0)));
    }

    private void createCategoryField(boolean isIncome) {
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setRenderer(new ComboBoxRenderer());

        updateCategoryComboBox();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Категория", categoryComboBox);
        add(panel, setConstraints(1, 2, paddingLeft));

        JButton addCategoryButton = new JButton("Добавить категорию");
        addCategoryButton.addActionListener(e -> new AddCategoryDialog(this, isIncome));
        add(addCategoryButton, setConstraints(2, 2, new Insets(14, 10, 0, 0)));
    }

    private void createDateField() {
        DateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        datePicker = new JDatePickerImpl(datePanel, new JFormattedTextField.AbstractFormatter() {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

            @Override
            public Object stringToValue(String text) throws ParseException {
                try {
                    return formatter.parse(text);
                } catch (ParseException e) {
                    logger.error(String.format("Error during parse String \"%s\" to Date", text), e);
                    throw new ParseException(String.format("Error during parse String \"%s\" to Date", text), 0);
                }
            }

            @Override
            public String valueToString(Object value) {
                if (value != null) {
                    GregorianCalendar date = (GregorianCalendar) value;
                    return formatter.format(Date.from(date.toZonedDateTime().toInstant()));
                }
                return "";
            }
        });

        model.setSelected(true);

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Дата", datePicker);
        add(panel, setConstraints(1, 3, paddingLeft));
    }

    private void createNoteField() {
        noteTextArea = new JTextArea();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Заметка", noteTextArea);
        add(panel, setConstraints(1, 4, 2, GridBagConstraints.BOTH, paddingLeft));
    }

    private void createAddTransactionButton(boolean isIncome) {
        JButton addTransactionButton = new JButton();
        if (isIncome) {
            addTransactionButton.setText("Добавить доход");
        } else {
            addTransactionButton.setText("Добавить расход");
        }

        addTransactionButton.addActionListener(new AddTransactionListener(
            this,
            valueTextField,
            accountComboBox,
            categoryComboBox,
            datePicker,
            noteTextArea
        ));

        add(addTransactionButton, setConstraints(1, 5, new Insets(10, paddingLeft, 10, 0)));
    }

    void updateAccountComboBox() {
        accountComboBox.removeAllItems();
        for (Account account : Account.getAllAccounts()) {
            accountComboBox.addItem(account);
        }
    }

    void updateCategoryComboBox() {
        categoryComboBox.removeAllItems();
        for (Category category : Category.getAllCategories()) {
            if (isIncome && category.isIncome()) {
                categoryComboBox.addItem(category);
            } else if (!isIncome && !category.isIncome()) {
                categoryComboBox.addItem(category);
            }
        }
    }
}