package budget.ui.util;

import budget.domain.Account;
import budget.domain.Category;
import budget.ui.add.AddTransactionDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class ComboBoxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Category) {
            Category category = (Category) value;
            setText(category.getName());
        } else if (value instanceof Account) {
            Account account = (Account) value;
            setText(account.getName());
        }
        return this;
    }
}
