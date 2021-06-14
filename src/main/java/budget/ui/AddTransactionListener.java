package budget.ui;

import budget.domain.Account;
import budget.domain.Category;
import budget.domain.Transaction;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

public class AddTransactionListener implements ActionListener {
    private final int wrongValue = -1;
    private final AddTransactionDialog addTransactionDialog;
    private final JFormattedTextField valueTextField;
    private final JComboBox<Account> accountComboBox;
    private final JComboBox<Category> categoryComboBox;
    private final JDatePickerImpl datePicker;
    private final JTextArea noteTextArea;

    AddTransactionListener(AddTransactionDialog addTransactionDialog, JFormattedTextField valueTextField, JComboBox<Account> accountComboBox, JComboBox<Category> categoryComboBox, JDatePickerImpl datePicker, JTextArea noteTextArea) {
        this.addTransactionDialog = addTransactionDialog;
        this.valueTextField = valueTextField;
        this.accountComboBox = accountComboBox;
        this.categoryComboBox = categoryComboBox;
        this.datePicker = datePicker;
        this.noteTextArea = noteTextArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int value = getValue();
        Account account = getAccount();
        Category category = getCategory();
        Date date = getDate();
        String note = getNote();

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
            addTransactionDialog.dispose();
        }
    }

    private int getValue() {
        int value;
        if (valueTextField.getValue() instanceof Long) {
            value = (int) ((Long) valueTextField.getValue()).longValue() * 100;
        } else if (valueTextField.getValue() instanceof Double) {
            value = (int) ((Double) valueTextField.getValue()).doubleValue() * 100;
        } else {
            value = wrongValue;
        }
        return value;
    }

    private Account getAccount() {
        return (Account) accountComboBox.getSelectedItem();
    }

    private Category getCategory() {
        return (Category) categoryComboBox.getSelectedItem();
    }

    private Date getDate() {
        Date date;
        String textDate = datePicker.getJFormattedTextField().getText();
        try {
            date = (Date) datePicker.getJFormattedTextField().getFormatter().stringToValue(textDate);
        } catch (
                ParseException parseException) {
            date = new Date(0);
        }
        return date;
    }

    private String getNote() {
        return noteTextArea.getText();
    }
}