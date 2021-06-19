package budget.ui.add;

import budget.domain.Account;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAccountListener implements ActionListener {
    private final int wrongValue = -1;
    private final AddAccountDialog addAccountDialog;
    private final JTextField accountNameField;
    private final JFormattedTextField accountBalanceField;

    AddAccountListener(AddAccountDialog addAccountDialog) {
        this.addAccountDialog = addAccountDialog;
        this.accountNameField = addAccountDialog.getAccountNameField();
        this.accountBalanceField = addAccountDialog.getAccountBalanceField();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int balance = getBalance();
        String name = getName();

        if (balance == wrongValue) {
            JOptionPane.showMessageDialog(accountBalanceField, "Введите начальный баланс");
        } else if (name.isEmpty() || name.isBlank()) {
            JOptionPane.showMessageDialog(accountNameField, "Введите название счёта");
        } else {
            Account.addAccount(new Account(name, balance));
            addAccountDialog.getParent().updateAccountComboBox();
            addAccountDialog.getParent().getParent().updateAccountComboBox();
            addAccountDialog.dispose();
        }
    }

    private int getBalance() {
        int balance;
        if (accountBalanceField.getValue() instanceof Long) {
            balance = (int) ((Long) accountBalanceField.getValue()).longValue() * 100;
        } else if (accountBalanceField.getValue() instanceof Double) {
            balance = (int) ((Double) accountBalanceField.getValue()).doubleValue() * 100;
        } else if (accountBalanceField.getValue() instanceof Integer) {
            balance = (int) ((Integer) accountBalanceField.getValue()).doubleValue() * 100;
        } else {
            balance = wrongValue;
        }
        return balance;
    }

    private String getName() {
        return accountNameField.getText();
    }
}