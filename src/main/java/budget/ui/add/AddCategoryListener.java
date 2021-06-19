package budget.ui.add;

import budget.domain.Category;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCategoryListener implements ActionListener {
    private final AddCategoryDialog addCategoryDialog;
    private final boolean isIncome;
    private final JTextField categoryNameField;

    AddCategoryListener(AddCategoryDialog addCategoryDialog) {
        this.addCategoryDialog = addCategoryDialog;
        this.isIncome = addCategoryDialog.isIncome();
        this.categoryNameField = addCategoryDialog.getCategoryNameField();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = getName();

        if (name.isEmpty() || name.isBlank()) {
            JOptionPane.showMessageDialog(categoryNameField, "Введите название категории");
        } else {
            Category.addCategory(new Category(name, isIncome));
            addCategoryDialog.getParent().updateCategoryComboBox();
            addCategoryDialog.dispose();
        }
    }

    private String getName() {
        return categoryNameField.getText();
    }
}