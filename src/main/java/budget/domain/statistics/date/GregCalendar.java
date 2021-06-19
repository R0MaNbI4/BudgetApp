package budget.domain.statistics.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class GregCalendar extends GregorianCalendar {
    GregCalendar() {
        super();
    }

    void addDay(int dayCount) {
        this.add(Calendar.DATE, dayCount);
    }

    void addWeek(int weekCount) {
        weekCount *= 7;
        this.add(Calendar.DATE, weekCount);
    }

    void addMonth(int monthCount) {
        this.add(Calendar.MONTH, monthCount);
    }

    void addYear(int yearCount) {
        this.add(Calendar.YEAR, yearCount);
    }

    void setDate(GregCalendar gregCalendar) {
        this.setTime(gregCalendar.getTime());
    }

    GregCalendar setDate(Date date) {
        this.setTime(date);
        return this;
    }

    void setStartOfCurrentYear() {
        this.set(Calendar.YEAR, new GregorianCalendar().get(Calendar.YEAR));
        this.set(Calendar.MONTH, Calendar.JANUARY);
        this.set(Calendar.DAY_OF_MONTH, 1);
    }
}
