package budget.ui;

import budget.dao.TransactionDAO;
import budget.domain.Account;
import budget.domain.Category;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.*;
import java.util.*;

public class AddTransactionDialog extends JDialog {
    private static final Logger logger = LogManager.getLogger(AddTransactionDialog.class);
    private final boolean isIncome;
    private final int paddingLeft = 120;
    private JFormattedTextField valueTextField;
    private JComboBox<Account> accountComboBox;
    private JComboBox<Category> categoryComboBox;
    private JDatePickerImpl datePicker;
    private JTextArea noteTextArea;

    AddTransactionDialog(JFrame frame, boolean isIncome) {
        setModal(true);
        setTitle("Добавить транзакцию");
        setSize(frame.getWidth(), frame.getHeight());
        setLocationRelativeTo(frame);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        this.isIncome = isIncome;
        setValue();
        setAccount();
        setCategory(isIncome);
        setDate();
        setNote();
        setAddTransactionButton(isIncome);

        setVisible(true);
    }

    private void setValue() {
        NumberFormat valueFormat = NumberFormat.getNumberInstance();
        valueFormat.setMaximumIntegerDigits(TransactionDAO.valueMaxIntDigits);
        valueFormat.setMaximumFractionDigits(TransactionDAO.valueMaxFractionDigits);
        valueTextField = new JFormattedTextField(valueFormat);

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Сумма", valueTextField);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        c.insets = new Insets(0, paddingLeft, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setAccount() {
        accountComboBox = new JComboBox<>();
        accountComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account) {
                    Account account = (Account) value;
                    setText(account.getName());
                }
                return this;
            }
        });

       updateAccountComboBox();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Счёт", accountComboBox);

        GridBagConstraints accountComboBoxConstraints = new GridBagConstraints();
        accountComboBoxConstraints.gridx = 1;
        accountComboBoxConstraints.gridy = 1;
        accountComboBoxConstraints.weighty = 1;
        accountComboBoxConstraints.insets = new Insets(0, paddingLeft, 0, 0);
        accountComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(panel, accountComboBoxConstraints);

        JButton addAccountButton = new JButton("Добавить счёт");
        addAccountButton.addActionListener(e -> new AddAccountDialog(this));

        GridBagConstraints addAccountButtonConstraints = new GridBagConstraints();
        addAccountButtonConstraints.gridx = 2;
        addAccountButtonConstraints.gridy = 1;
        addAccountButtonConstraints.weighty = 1;
        addAccountButtonConstraints.insets = new Insets(14, 10, 0, 0);
        addAccountButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(addAccountButton, addAccountButtonConstraints);
    }

    private void setCategory(boolean isIncome) {
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Category) {
                    Category category = (Category) value;
                    setText(category.getName());
                }
                return this;
            }
        });

        updateCategoryComboBox();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Категория", categoryComboBox);

        GridBagConstraints categoryComboBoxConstraints = new GridBagConstraints();
        categoryComboBoxConstraints.gridx = 1;
        categoryComboBoxConstraints.gridy = 2;
        categoryComboBoxConstraints.weighty = 1;
        categoryComboBoxConstraints.insets = new Insets(0, paddingLeft, 0, 0);
        categoryComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(panel, categoryComboBoxConstraints);

        JButton addCategoryButton = new JButton("Добавить категорию");
        addCategoryButton.addActionListener(e -> new AddCategoryDialog(this, isIncome));

        GridBagConstraints addCategoryButtonConstraints = new GridBagConstraints();
        addCategoryButtonConstraints.gridx = 2;
        addCategoryButtonConstraints.gridy = 2;
        addCategoryButtonConstraints.weighty = 1;
        addCategoryButtonConstraints.insets = new Insets(14, 10, 0, 0);
        addCategoryButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        add(addCategoryButton, addCategoryButtonConstraints);
    }

    private void setDate() {
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

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 1;
        c.insets = new Insets(0, paddingLeft, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setNote() {
        noteTextArea = new JTextArea();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Заметка", noteTextArea);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, paddingLeft, 0, 0);
        add(panel, c);
    }

    private void setAddTransactionButton(boolean isIncome) {
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

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, paddingLeft, 10, 0);
        add(addTransactionButton, c);
    }

    private void addLabelToComponent(JPanel panel, String labelText, Component comp) {
        JLabel label = new JLabel(labelText);
        panel.setLayout(new BorderLayout());
        panel.add(comp, BorderLayout.CENTER);
        panel.add(label, BorderLayout.NORTH);
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