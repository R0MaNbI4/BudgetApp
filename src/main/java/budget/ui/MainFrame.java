package budget.ui;

import budget.domain.Account;
import budget.domain.statistics.DateChooser;
import budget.domain.statistics.DatePeriod;
import budget.ui.add.AddTransactionDialog;
import budget.ui.statistics.DatePeriodSpecificDialog;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MainFrame extends JFrame {
    private final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private final int WIDTH = 600;
    private final int HEIGHT = 600;
    private final int X_CENTER = (int) (dimension.getWidth() / 2) - (WIDTH / 2);
    private final int Y_CENTER = (int) (dimension.getHeight() / 2) - (HEIGHT / 2);
    private final String pattern = "dd.MM.yyyy";
    private final DateChooser dateChooser;
    private final JLabel periodLabel;

    private JComboBox<Account> accountComboBox;

    public MainFrame() throws HeadlessException {
        setTitle("BudgetKeeper");
        setBounds(X_CENTER, Y_CENTER, WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        dateChooser = new DateChooser();
        periodLabel = new JLabel();

        setLayout(new BorderLayout());

        createAddTransactionButtons();
        createFilters();
        createDisplayStatistics();

        setVisible(true);
    }

    public DateChooser getDateChooser() {
        return dateChooser;
    }

    public void updatePeriodLabel() {
        periodLabel.setText(getPeriodText());
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
        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new GridBagLayout());
        add(filtersPanel, BorderLayout.WEST);

        createAccountChooser(filtersPanel);
        createDateChooser(filtersPanel);
        createCategoryChooser(filtersPanel);
    }

    private void createAccountChooser(JPanel filtersPanel) {
        accountComboBox = new JComboBox<>();
        accountComboBox.setRenderer(new ComboBoxRenderer());

        updateAccountComboBox();

        GridBagConstraints c = new GridBagConstraints();
        c.gridy  = 0;
        filtersPanel.add(accountComboBox, c);
    }

    private void createDateChooser(JPanel filtersPanel) {
        int paddingLeft = 0;
        ButtonGroup dateChooserGroup = new ButtonGroup();

        HashMap<DatePeriod, JRadioButton> dateButtons = new HashMap<>();
        dateButtons.put(DatePeriod.DAY, new JRadioButton("День"));
        dateButtons.put(DatePeriod.WEEK, new JRadioButton("Неделя"));
        dateButtons.put(DatePeriod.MONTH, new JRadioButton("Месяц"));
        dateButtons.put(DatePeriod.YEAR, new JRadioButton("Год"));
        dateButtons.put(DatePeriod.ALL, new JRadioButton("Все"));

        dateButtons.get(DatePeriod.DAY).setSelected(true);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, paddingLeft, 0, 0);

        for (DatePeriod datePeriod : DatePeriod.values()) {
            if (datePeriod != DatePeriod.SPECIFIC) {
                dateChooserGroup.add(dateButtons.get(datePeriod));
                dateButtons.get(datePeriod).addActionListener(e -> {
                    dateChooser.setBorderDates(datePeriod);
                    updatePeriodLabel();
                });
                filtersPanel.add(dateButtons.get(datePeriod), c);
            }
        }

        JButton datePeriodSpecificButton = new JButton("Другой период");
        datePeriodSpecificButton.addActionListener(e -> new DatePeriodSpecificDialog(this));
        filtersPanel.add(datePeriodSpecificButton, c);

        add(filtersPanel, BorderLayout.WEST);
    }

    private void createCategoryChooser(JPanel filtersPanel) {

    }

    private void createDisplayStatistics() {
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new GridBagLayout());
        add(statisticsPanel, BorderLayout.CENTER);

        createPeriodLabel(statisticsPanel);
        createDateSwitch(statisticsPanel);
        createPieChart(statisticsPanel);
    }

    private void createPeriodLabel(JPanel statisticsPanel) {
        periodLabel.setText(getPeriodText());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        statisticsPanel.add(periodLabel);
    }

    private void createDateSwitch(JPanel statisticsPanel) {
        JButton nextDateButton = new JButton("След");
        JButton prevDateButton = new JButton("Пред");

        nextDateButton.addActionListener(e -> {
            dateChooser.setNextPeriod();
            updatePeriodLabel();
        });
        prevDateButton.addActionListener(e -> {
            dateChooser.setPrevPeriod();
            updatePeriodLabel();
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        statisticsPanel.add(prevDateButton, c);
        c.gridx = 1;
        statisticsPanel.add(nextDateButton, c);
    }

    private void createPieChart(JPanel statisticsPanel) {

    }

    void updateAccountComboBox() {
        accountComboBox.removeAllItems();
        for (Account account : Account.getAllAccounts()) {
            accountComboBox.addItem(account);
        }
    }

    private String getPeriodText() {
        DateFormatter formatter = new DateFormatter(pattern);
        StringBuilder sb = new StringBuilder();
        sb.append(formatter.valueToString(dateChooser.getStartDate()));
        sb.append(" - ");
        sb.append(formatter.valueToString(dateChooser.getEndDate()));
        return sb.toString();
    }



}
