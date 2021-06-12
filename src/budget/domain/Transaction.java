package budget.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private int id;
    private int value;
    private Account account;
    private Category category;
    private Date date;
    private String note;

    public Transaction(int id, int value, Account account, Category category, Date date, String note) {
        this.id = id;
        this.value = value;
        this.account = account;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    public Transaction(int value, Account account, Category category, Date date, String note) {
        this(-1, value, account, category, date, note);
    }

    Transaction setValue(int value) {
        this.value = value;
        return this;
    }

    Transaction setCount(Account account) {
        this.account = account;
        return this;
    }

    Transaction setCategory(Category category) {
        this.category = category;
        return this;
    }

    Transaction setDate(Date date) {
        this.date = date;
        return this;
    }

    Transaction setDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date = format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException("Error during parse String to Date", e);
        }
        return this;
    }

    Transaction setNote(String note) {
        this.note = note;
        return this;
    }

    Transaction build() {
        return build();
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public Account getAccount() {
        return account;
    }

    public Category getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public String getDateAsString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public String getNote() {
        return note;
    }
}