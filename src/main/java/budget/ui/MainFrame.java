package budget.ui;

import budget.domain.Account;
import budget.domain.statistics.date.DateChooser;
import budget.domain.statistics.date.DatePeriod;
import budget.domain.statistics.piechart.Dataset;
import budget.ui.add.AddTransactionDialog;
import budget.ui.statistics.BudgetPieChart;
import budget.ui.statistics.CategoryType;
import budget.ui.statistics.DatePeriodSpecificDialog;
import budget.ui.util.ComboBoxRenderer;
import budget.ui.util.DateFormatter;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.synth.SynthButtonUI;
import java.awt.*;
import java.util.HashMap;

public class MainFrame extends JFrame {
    private final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private final int WIDTH = 1000;
    private final int HEIGHT = 600;
    private final int X_CENTER = (int) (dimension.getWidth() / 2) - (WIDTH / 2);
    private final int Y_CENTER = (int) (dimension.getHeight() / 2) - (HEIGHT / 2);
    private final String pattern = "dd.MM.yyyy";
    private final DateChooser dateChooser;
    private JFreeChart pieChart;
    private Dataset dataset;

    private JComboBox<Account> accountComboBox;
    private JCheckBox incomeCategoryTypeCheckBox, expenseCategoryTypeCheckBox;
    /*
        Перенести на sqlLite
        Выровнять интерфейс

        Добавить настройки:
        отсчет дат
        изменение и удаление категорий, счетов, транзакций
     */
    public MainFrame() {
        setTitle("BudgetKeeper");
        setBounds(X_CENTER, Y_CENTER, WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        dateChooser = new DateChooser();
        dataset = new Dataset();
        pieChart = new BudgetPieChart(dataset).getPieChart();

        setLayout(new BorderLayout());

        createAddTransactionButtons();
        createFilters();
        createDisplayStatistics();

        setVisible(true);
    }

    public DateChooser getDateChooser() {
        return dateChooser;
    }

    public Account getAccount() {
        return (Account) accountComboBox.getSelectedItem();
    }

    public CategoryType getCategoryType() {
        boolean income = incomeCategoryTypeCheckBox.isSelected();
        boolean expense = expenseCategoryTypeCheckBox.isSelected();
        if (income && !expense) {
            return CategoryType.INCOME;
        } else if (!income && expense) {
            return CategoryType.EXPENSE;
        } else if (income && expense) {
            return CategoryType.INCOME_AND_EXPENSE;
        } else {
            return CategoryType.NONE;
        }
    }

    public void updatePieChart() {
        System.out.println("updated");;
        pieChart.setTitle(new TextTitle(
                getPeriodText(),
                new Font("Arial", Font.BOLD, 18)
        ));

        dataset.update(
                dateChooser.getStartDate(),
                dateChooser.getEndDate(),
                getCategoryType(),
                getAccount()
        );

        pieChart.fireChartChanged();
    }

    public void updateAccountComboBox() {
        accountComboBox.removeAllItems();
        Account superAccount = new Account(-1, "Все", -1);
        accountComboBox.addItem(superAccount);
        for (Account account : Account.getAllAccounts()) {
            accountComboBox.addItem(account);
        }
    }

    private void createAddTransactionButtons() {
        JPanel bottomTransactionButtons = new JPanel();
        add(bottomTransactionButtons, BorderLayout.SOUTH);
        bottomTransactionButtons.setLayout(new FlowLayout());

        JButton addIncomeButton = new JButton("Добавить доход");
        addIncomeButton.setBackground(Color.GREEN);
        addIncomeButton.setOpaque(true);
        addIncomeButton.setBorder(new BorderUIResource.EmptyBorderUIResource(20, 25, 20, 25));

        JButton addExpenseButton = new JButton("Добавить расход");
        addExpenseButton.setBackground(Color.RED);
        addExpenseButton.setOpaque(true);
        addExpenseButton.setBorder(new BorderUIResource.EmptyBorderUIResource(20, 25, 20, 25));

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
        //filtersPanel.setBorder(new BorderUIResource.LineBorderUIResource(Color.BLACK));
        filtersPanel.setLayout(new GridBagLayout());
        add(filtersPanel, BorderLayout.WEST);

        createAccountChooser(filtersPanel);
        createDateChooser(filtersPanel);
        createCategoryChooser(filtersPanel);
    }
    private void createDisplayStatistics() {
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new BorderLayout());
        add(statisticsPanel, BorderLayout.CENTER);

        createDateSwitch(statisticsPanel);
        createPieChart(statisticsPanel);
    }

    private void createAccountChooser(JPanel filtersPanel) {
        accountComboBox = new JComboBox<>();
        accountComboBox.setRenderer(new ComboBoxRenderer());

        updateAccountComboBox();

        accountComboBox.addActionListener(e -> updatePieChart());

        GridBagConstraints c = new GridBagConstraints();
        c.gridy  = 0;
        filtersPanel.add(accountComboBox, c);
    }
    private void createDateChooser(JPanel filtersPanel) {
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

        for (DatePeriod datePeriod : DatePeriod.values()) {
            if (datePeriod != DatePeriod.SPECIFIC) {
                dateChooserGroup.add(dateButtons.get(datePeriod));
                dateButtons.get(datePeriod).addActionListener(e -> {
                    dateChooser.setBorderDates(datePeriod);
                    updatePieChart();
                });
                filtersPanel.add(dateButtons.get(datePeriod), c);
            }
        }

        JButton datePeriodSpecificButton = new JButton("Другой период");
        datePeriodSpecificButton.addActionListener(e -> {
                dateChooserGroup.clearSelection();
                new DatePeriodSpecificDialog(this);
        });
        filtersPanel.add(datePeriodSpecificButton, c);

        add(filtersPanel, BorderLayout.WEST);
    }
    private void createCategoryChooser(JPanel filtersPanel) {
        incomeCategoryTypeCheckBox = new JCheckBox("Доходы");
        expenseCategoryTypeCheckBox = new JCheckBox("Расходы");

        expenseCategoryTypeCheckBox.setSelected(true);

        incomeCategoryTypeCheckBox.addActionListener(e -> updatePieChart());
        expenseCategoryTypeCheckBox.addActionListener(e -> updatePieChart());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        filtersPanel.add(incomeCategoryTypeCheckBox, c);
        filtersPanel.add(expenseCategoryTypeCheckBox, c);
    }

    private void createDateSwitch(JPanel statisticsPanel) {
        JPanel dateNavigatePanel = new JPanel();
        statisticsPanel.add(dateNavigatePanel, BorderLayout.SOUTH);

        JButton nextDateButton = new JButton("След");
        JButton prevDateButton = new JButton("Пред");

        nextDateButton.addActionListener(e -> {
            dateChooser.setNextPeriod();
            updatePieChart();
        });
        prevDateButton.addActionListener(e -> {
            dateChooser.setPrevPeriod();
            updatePieChart();
        });

        dateNavigatePanel.add(prevDateButton);
        dateNavigatePanel.add(nextDateButton);
    }
    private void createPieChart(JPanel statisticsPanel) {
        updatePieChart();

        ChartPanel chartPanel = new ChartPanel(pieChart);

        statisticsPanel.add(chartPanel, BorderLayout.CENTER);
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