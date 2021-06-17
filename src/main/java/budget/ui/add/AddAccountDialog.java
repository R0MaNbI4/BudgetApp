package budget.ui.add;

import budget.dao.TransactionDAO;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class AddAccountDialog extends JDialog implements AddItemDialog {
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

        createNameField();
        createBalanceField();
        createAddAccountButton();

        setVisible(true);
    }

    private void createNameField() {
        accountNameField = new JTextField();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Название", accountNameField);
        add(panel, setConstraints(1, 0));
    }

    private void createBalanceField() {
        NumberFormat valueFormat = NumberFormat.getNumberInstance();
        valueFormat.setMaximumIntegerDigits(TransactionDAO.valueMaxIntDigits);
        valueFormat.setMaximumFractionDigits(TransactionDAO.valueMaxFractionDigits);
        accountBalanceField = new JFormattedTextField(valueFormat);
        accountBalanceField.setValue(0);

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Начальный баланс", accountBalanceField);
        add(panel, setConstraints(1, 1));
    }

    private void createAddAccountButton() {
        JButton addAccountButton = new JButton("Добавить счёт");
        addAccountButton.addActionListener(new AddAccountListener(
                addTransactionDialog,
                this,
                accountNameField,
                accountBalanceField
        ));

        add(addAccountButton, setConstraints(1, 2));
    }
}