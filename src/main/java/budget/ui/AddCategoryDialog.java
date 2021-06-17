package budget.ui;

import javax.swing.*;
import java.awt.*;

public class AddCategoryDialog extends JDialog {
    private final int WIDTH = 250;
    private final int HEIGHT = 150;
    private final AddTransactionDialog addTransactionDialog;
    private final boolean isIncome;
    private JTextField categoryNameField;

    AddCategoryDialog(AddTransactionDialog addTransactionDialog, boolean isIncome) {
        this.addTransactionDialog = addTransactionDialog;
        this.isIncome = isIncome;

        setModal(true);
        setTitle("Добавить категорию");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(addTransactionDialog);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        setName();
        setAddCategoryButton();

        setVisible(true);
    }

    private void setName() {
        categoryNameField = new JTextField();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Название", categoryNameField);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(panel, c);
    }

    private void setAddCategoryButton() {
        JButton addCategoryButton = new JButton("Добавить категорию");
        addCategoryButton.addActionListener(new AddCategoryListener(
                addTransactionDialog,
                this,
                isIncome,
                categoryNameField
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(addCategoryButton, c);
    }

    private void addLabelToComponent(JPanel panel, String labelText, Component comp) {
        JLabel label = new JLabel(labelText);
        panel.setLayout(new BorderLayout());
        panel.add(comp, BorderLayout.CENTER);
        panel.add(label, BorderLayout.NORTH);
    }
}