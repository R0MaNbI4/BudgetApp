package budget.ui.statistics;

import budget.domain.statistics.piechart.Dataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class BudgetPieChart {
    JFreeChart pieChart;
    Dataset dataset;

    public BudgetPieChart(Dataset dataset) {
        this.dataset = dataset;

        pieChart = ChartFactory.createPieChart(
                "",
                dataset,
                false,
                true,
                Locale.getDefault()
        );
        PiePlot plot = (PiePlot) pieChart.getPlot();

        Paint background = UIManager.getColor("Panel.background");

        pieChart.setTextAntiAlias(true);
        pieChart.setBackgroundPaint(background);
        plot.setBackgroundPaint(background);
        plot.setIgnoreZeroValues(true);

        // Внешний вид секций
        plot.setInteriorGap(0.1);
        plot.setOutlineVisible(false);
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(1.5f));

        // Внешний вид названий секций
        plot.setLabelFont(new Font("Courier New", Font.BOLD, 24));
        plot.setLabelLinkPaint(Color.WHITE);
        plot.setLabelLinkStroke(new BasicStroke(1.5f));
        plot.setLabelOutlineStroke(null);
        plot.setLabelPaint(Color.BLACK);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelLinkStyle(PieLabelLinkStyle.QUAD_CURVE);

        plot.setNoDataMessage("Нет данных");
    }

    public JFreeChart getPieChart() {
        return pieChart;
    }
}