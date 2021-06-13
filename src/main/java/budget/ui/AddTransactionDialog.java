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
import javax.swing.border.Border;
import java.awt.*;
import java.text.*;
import java.util.*;

public class AddTransactionDialog extends JDialog {
    private static final Logger logger = LogManager.getLogger(TransactionDAO.class);

    AddTransactionDialog(JFrame frame, boolean isIncome) {
        setModal(true);
        setTitle("Добавить транзакцию");
        setSize(frame.getWidth(), frame.getHeight());
        setLocationRelativeTo(frame);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

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
        valueFormat.setMaximumIntegerDigits(9);
        valueFormat.setMaximumFractionDigits(2);
        JFormattedTextField valueTextField = new JFormattedTextField(valueFormat);

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Сумма", valueTextField);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setAccount() {
        JComboBox<Account> accountComboBox = new JComboBox<>();
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

        for (Account account : Account.getAllAccounts()) {
            accountComboBox.addItem(account);
        }

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Счёт", accountComboBox);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setCategory(boolean isIncome) {
        JComboBox<Category> categoryComboBox = new JComboBox<>();
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

        for (Category category : Category.getAllCategories()) {
            if (isIncome && category.isIncome()) {
                categoryComboBox.addItem(category);
            } else if (!isIncome && !category.isIncome()) {
                categoryComboBox.addItem(category);
            }
        }

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Категория", categoryComboBox);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setDate() {
        DateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new JFormattedTextField.AbstractFormatter() {
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            @Override
            public Object stringToValue(String text) {
                try {
                    return formatter.parse(text);
                } catch (ParseException e) {
                    logger.error(String.format("Error during parse String %s to Date", text), e);
                    throw new RuntimeException(String.format("Error during parse String %s to Date", text), e);
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

        datePicker.setTextEditable(true);

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Дата", datePicker);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setNote() {
        JTextArea note = new JTextArea();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Заметка", note);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 10, 0);
        add(panel, c);
    }

    private void setAddTransactionButton(boolean isIncome) {
        JButton addTransactionButton = new JButton();
        if (isIncome) {
            addTransactionButton.setText("Добавить доход");
        } else {
            addTransactionButton.setText("Добавить расход");
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 10, 0);
        add(addTransactionButton, c);
    }

    private void addLabelToComponent(JPanel panel, String labelText, Component comp) {
        JLabel label = new JLabel(labelText);
        panel.setLayout(new BorderLayout());
        panel.add(comp, BorderLayout.CENTER);
        panel.add(label, BorderLayout.NORTH);
    }
}