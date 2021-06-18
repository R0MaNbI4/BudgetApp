package budget.domain.statistics;

import budget.dao.TransactionDAO;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateChooser {
    private StartDate startReferenceDate;
    private DatePeriod datePeriod;
    private GregCalendar startDate, endDate;
    private final GregCalendar currentDate;
    private final int currentYear;
    private final int currentWeekOfYear;
    private final int currentMonth;

    public DateChooser() {
        startReferenceDate = StartDate.FROM_BEGINNING_OF_THE_YEAR;

        startDate = new GregCalendar();
        endDate = new GregCalendar();

        currentDate = new GregCalendar();
        currentWeekOfYear = currentDate.get(Calendar.WEEK_OF_YEAR);
        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);

        this.datePeriod = DatePeriod.DAY;
        setBorderDates();
    }

    public void setDatePeriod(DatePeriod datePeriod) {
        this.datePeriod = datePeriod;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = (GregCalendar) startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = new GregCalendar().setDate(startDate);
    }

    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = (GregCalendar) endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = new GregCalendar().setDate(endDate);
    }

    public Date getStartDate() {
        return new Date(startDate.getTime().getTime());
    }

    public Date getEndDate() {
        return new Date(endDate.getTime().getTime());
    }

    public void setNextPeriod() {
        switch (datePeriod) {
            case DAY:
                startDate.addDay(1);
                endDate.addDay(1);
                break;
            case WEEK:
                startDate.addWeek(1);
                endDate.addWeek(1);
                break;
            case MONTH:
                startDate.addMonth(1);
                endDate.addMonth(1);
                break;
            case YEAR:
                startDate.addYear(1);
                endDate.addYear(1);
                break;
        }
    }

    public void setPrevPeriod() {
        switch (datePeriod) {
            case DAY:
                startDate.addDay(-1);
                endDate.addDay(-1);
                break;
            case WEEK:
                startDate.addWeek(-1);
                endDate.addWeek(-1);
                break;
            case MONTH:
                startDate.addMonth(-1);
                endDate.addMonth(-1);
                break;
            case YEAR:
                startDate.addYear(-1);
                endDate.addYear(-1);
                break;
        }
    }

    public void setBorderDates(DatePeriod datePeriod) {
        this.datePeriod = datePeriod;
        setBorderDates();
    }

    private void setBorderDates() {
        switch (datePeriod) {
            case ALL:
                startDate.setDate(TransactionDAO.getDateOfFirstTransaction());
                endDate.setDate(TransactionDAO.getDateOfLastTransaction());
                break;
            case SPECIFIC:
                break;

            default:
                switch (startReferenceDate) {
                    case FROM_BEGINNING_OF_THE_YEAR:
                       setBorderDatesFromBeginningOfTheYear(datePeriod);
                       break;

                    case FROM_TODAY:
                       setBorderDatesFromToday(datePeriod);
                       break;
                }
        }
    }

    private void setBorderDatesFromBeginningOfTheYear(DatePeriod datePeriod) {
        switch (datePeriod) {
            case DAY:
                startDate.setDate(currentDate);
                endDate.setDate(currentDate);
                break;
            case WEEK:
                startDate.setWeekDate(currentYear, currentWeekOfYear, Calendar.MONDAY);
                endDate.setDate(startDate);
                endDate.addWeek(1);
                break;
            case MONTH:
                startDate.setStartOfCurrentYear();
                startDate.set(Calendar.MONTH, currentMonth);
                endDate.setDate(startDate);
                endDate.addMonth(1);
                break;
            case YEAR:
                startDate.setStartOfCurrentYear();
                endDate.setDate(startDate);
                endDate.addYear(1);
                break;
        }
    }

    private void setBorderDatesFromToday(DatePeriod datePeriod) {
        switch (datePeriod) {
            case DAY:
                endDate.setDate(currentDate);
                startDate.setDate(currentDate);
                break;
            case WEEK:
                endDate.setDate(currentDate);
                startDate.setDate(endDate);
                startDate.addWeek(-1);
                break;
            case MONTH:
                endDate.setDate(currentDate);
                startDate.setDate(endDate);
                startDate.addMonth(-1);
                break;
            case YEAR:
                endDate.setDate(currentDate);
                startDate.setDate(endDate);
                startDate.addYear(-1);
                break;
        }
    }
}