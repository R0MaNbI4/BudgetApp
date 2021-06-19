package budget.ui.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormatter extends JFormattedTextField.AbstractFormatter {
    private static final Logger logger = LogManager.getLogger(DateFormatter.class);
    private final SimpleDateFormat formatter;
    private final String pattern;

    public DateFormatter(String pattern) {
        this.pattern = pattern;
        this.formatter = new SimpleDateFormat(pattern);
    }

    @Override
    public Object stringToValue(String text) {
        try {
            return formatter.parse(text);
        } catch (ParseException e) {
            logger.error(String.format("Error during parse String \"%s\" to Date", text), e);
            throw new RuntimeException(String.format("Error during parse String \"%s\" to Date", text), e);
        }
    }

    @Override
    public String valueToString(Object value) {
        if (value != null) {
            GregorianCalendar date;
            if (value instanceof GregorianCalendar) {
                date = (GregorianCalendar) value;
            } else if (value instanceof Date) {
                date = dateToGregorianCalendar((Date) value);
            } else {
                logger.error(("Wrong input date" + value));
                throw new RuntimeException(("Wrong input date" + value));
            }
            return formatter.format(Date.from(date.toZonedDateTime().toInstant()));
        }

        return "";
    }

    private GregorianCalendar dateToGregorianCalendar(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return gc;
    }
}
