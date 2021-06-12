package budget.domain;

import budget.dao.TransactionDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Transaction {
    private static final Logger logger = LogManager.getLogger(Transaction.class);

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

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", value=" + value +
                ", account=" + account +
                ", category=" + category +
                ", date=" + date +
                ", note='" + note + '\'' +
                '}';
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

    public static void addTransaction(Transaction transaction) {
        TransactionDAO.addTransaction(transaction);
    }

    public static void  updateTransaction(int id, Transaction modified) {
        TransactionDAO.updateTransaction(id, modified);
    }

    public static void updateTransaction(Transaction original, Transaction modified) {
        int id = original.getId();
        if (id != -1) {
            TransactionDAO.updateTransaction(id, modified);
        } else {
            logger.debug("Transaction hasn't id\n" + original.toString());
        }
    }

    public static void deleteTransaction(int id) {
        TransactionDAO.deleteTransaction(id);
    }

    public static void deleteTransaction(Transaction transaction) {
        int id = transaction.getId();
        if (id != -1) {
            TransactionDAO.deleteTransaction(id);
        } else {
            logger.debug("Transaction hasn't id\n" + transaction.toString());
        }
    }

    public static Transaction getTransactionById(int id) {
        return TransactionDAO.getTransactionById(id);
    }

    public static ArrayList<Transaction> getTransactionByDate(Date startDate, Date endDate) {
        return TransactionDAO.getTransactionByDate(startDate, endDate);
    }
}