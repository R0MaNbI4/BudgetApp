package budget.ui;

import budget.domain.Account;
import budget.ui.add.AddTransactionDialog;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int X_CENTER = (int) (dimension.getWidth() / 2) - (WIDTH / 2);
    private final int Y_CENTER = (int) (dimension.getHeight() / 2) - (HEIGHT / 2);

    private JComboBox<Account> accountComboBox;

    public MainFrame() throws HeadlessException {
        setTitle("BudgetKeeper");
        setBounds(X_CENTER, Y_CENTER, WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        createAddTransactionButtons();
        createFilters();

        setVisible(true);
    }

    private void createAddTransactionButtons() {
        JPanel bottomTransactionButtons = new JPanel();
        add(bottomTransactionButtons, BorderLayout.SOUTH);
        bottomTransactionButtons.setLayout(new FlowLayout());

        Button addIncomeButton = new Button();
        addIncomeButton.setLabel("Добавить доход");

        Button addExpenseButton = new Button();
        addExpenseButton.setLabel("Добавить расход");

        bottomTransactionButtons.add(addIncomeButton);
        bottomTransactionButtons.add(addExpenseButton);

        addIncomeButton.addActionListener(e -> {
            new AddTransactionDialog(this, true);
        });
        addExpenseButton.addActionListener(e -> {
            new AddTransactionDialog(this, false);
        });
    }

    private void createFilters() {
        createAccountChooser();
        createDateChooser();
        createCategoryChooser();
    }

    private void createAccountChooser() {

    }

    private void createDateChooser() {

    }

    private void createCategoryChooser() {

    }
}
