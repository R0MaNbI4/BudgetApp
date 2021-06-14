package budget.ui;

import budget.dao.TransactionDAO;
import budget.domain.Account;
import budget.domain.Category;
import budget.domain.Transaction;
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
import java.time.LocalDate;
import java.util.*;

public class AddTransactionDialog extends JDialog {
    private static final Logger logger = LogManager.getLogger(TransactionDAO.class);
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

        addTransactionButton.addActionListener(e -> {
            final int wrongValue = -1;
            int value;
            Account account;
            Category category;
            Date date;
            String note;

            if (valueTextField.getValue() instanceof Long) {
                value = (int) ((Long) valueTextField.getValue()).longValue() * 100;
            } else if (valueTextField.getValue() instanceof Double) {
                value = (int) ((Double) valueTextField.getValue()).doubleValue() * 100;
            } else {
                value = wrongValue;
            }

            account = (Account) accountComboBox.getSelectedItem();

            category = (Category) categoryComboBox.getSelectedItem();

            String textDate = datePicker.getJFormattedTextField().getText();
            try {
                date = (Date) datePicker.getJFormattedTextField().getFormatter().stringToValue(textDate);
            } catch (ParseException parseException) {
                date = new Date(0);
            }

            note = noteTextArea.getText();

            if (value == wrongValue) {
                JOptionPane.showMessageDialog(valueTextField, "Введите сумму");
            } else if (account == null) {
                JOptionPane.showMessageDialog(accountComboBox, "Выберите или создайте счёт");
            } else if (category == null) {
                JOptionPane.showMessageDialog(categoryComboBox, "Выберите или создайте категорию");
            } else if (date.getTime() == 0) {
                JOptionPane.showMessageDialog(datePicker, "Выберите дату");
            } else {
                Transaction.addTransaction(new Transaction(value, account, category, date, note));
                dispose();
            }
        });

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