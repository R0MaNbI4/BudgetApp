package budget.ui.add;

import javax.swing.*;
import java.awt.*;

public class AddCategoryDialog extends JDialog implements AddItemDialog {
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
        setResizable(false);

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        createNameField();
        createAddCategoryButton();

        setVisible(true);
    }

    private void createNameField() {
        categoryNameField = new JTextField();

        JPanel panel = new JPanel();
        addLabelToComponent(panel, "Название", categoryNameField);
        add(panel, setConstraints(1, 0));
    }

    private void createAddCategoryButton() {
        JButton addCategoryButton = new JButton("Добавить категорию");
        addCategoryButton.addActionListener(new AddCategoryListener(
                addTransactionDialog,
                this,
                isIncome,
                categoryNameField
        ));

        add(addCategoryButton, setConstraints(1, 2));
    }
}