package budget.ui.statistics;

import budget.ui.DateFormatter;
import budget.ui.MainFrame;
import org.jdatepicker.DateModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

public class DatePeriodSpecificDialog extends JDialog {
    private final int WIDTH = 250;
    private final int HEIGHT = 150;
    private final MainFrame frame;
    private JDatePickerImpl startDatePicker;
    private JDatePickerImpl endDatePicker;
    private GridBagConstraints c;
    private JDialog datePeriodSpecificDialog;

    public DatePeriodSpecificDialog(MainFrame frame) {
        setModal(true);
        setTitle("Добавить категорию");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(frame);
        setResizable(false);

        this.frame = frame;
        datePeriodSpecificDialog = this;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        startDatePicker = createDateField(frame.getDateChooser().getStartDate());
        endDatePicker = createDateField(frame.getDateChooser().getEndDate());
        createSubmitButton();

        setVisible(true);
    }

    private JDatePickerImpl createDateField(Date date) {
        DateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateFormatter("dd.MM.yyyy"));
        model.setSelected(true);
        model.setValue(date);
        add(datePicker, c);
        return datePicker;
    }

    private void createSubmitButton() {
        JButton sumbitButton = new JButton("ОК");

        sumbitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.getDateChooser().setStartDate((Date) startDatePicker.getModel().getValue());
                frame.getDateChooser().setEndDate((Date) endDatePicker.getModel().getValue());
                frame.updatePeriodLabel();
                datePeriodSpecificDialog.dispose();
            }
        });

        add(sumbitButton, c);
    }
}
