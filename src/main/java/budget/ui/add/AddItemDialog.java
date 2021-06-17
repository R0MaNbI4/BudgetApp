package budget.ui.add;

import javax.swing.*;
import java.awt.*;

public interface AddItemDialog {
    default void addLabelToComponent(JPanel panel, String labelText, Component comp) {
        JLabel label = new JLabel(labelText);
        panel.setLayout(new BorderLayout());
        panel.add(comp, BorderLayout.CENTER);
        panel.add(label, BorderLayout.NORTH);
    }

    default GridBagConstraints setConstraints(int gridx, int gridy, int weighty, int fill, int paddingLeft) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.weighty = weighty;
        c.fill = fill;
        c.insets = new Insets(0, paddingLeft, 0, 0);
        return c;
    }

    default GridBagConstraints setConstraints(int gridx, int gridy, Insets insets) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = insets;
        return c;
    }

    default GridBagConstraints setConstraints(int gridx, int gridy, int paddingLeft) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, paddingLeft, 0, 0);
        return c;
    }

    default GridBagConstraints setConstraints(int gridx, int gridy) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = gridx;
        c.gridy = gridy;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        return c;
    }
}
