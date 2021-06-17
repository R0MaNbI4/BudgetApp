package budget.ui;

import budget.dao.TransactionDAO;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class AddAccountDialog extends JDialog {
    private final int WIDTH = 250;
    private final int HEIGHT = 250;
    private final AddTransactionDialog addTransactionDialog;
    private JTextField accountNameField;
    private JFormattedTextField accountBalanceField;

    AddAccountDialog(AddTransactionDialog addTransactionDialog) {
        this.addTransactionDialog = addTransactionDialog;

        setModal(true);
        setTitle("Добавить счёт");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(addTransactionDialog);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        setName();
        setBalance();
        setAddAccountButton();

        setVisible(true);
    }

    private void setName() {
        accountNameField = new JTextField();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Название", accountNameField);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setBalance() {
        NumberFormat valueFormat = NumberFormat.getNumberInstance();
        valueFormat.setMaximumIntegerDigits(TransactionDAO.valueMaxIntDigits);
        valueFormat.setMaximumFractionDigits(TransactionDAO.valueMaxFractionDigits);
        accountBalanceField = new JFormattedTextField(valueFormat);
        accountBalanceField.setValue(0);

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Начальный баланс", accountBalanceField);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setAddAccountButton() {
        JButton addAccountButton = new JButton("Добавить счёт");
        addAccountButton.addActionListener(new AddAccountListener(
                addTransactionDialog,
                this,
                accountNameField,
                accountBalanceField
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(addAccountButton, c);
    }

    private void addLabelToComponent(JPanel panel, String labelText, Component comp) {
        JLabel label = new JLabel(labelText);
        panel.setLayout(new BorderLayout());
        panel.add(comp, BorderLayout.CENTER);
        panel.add(label, BorderLayout.NORTH);
    }
}