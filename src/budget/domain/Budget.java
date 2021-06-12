package budget.domain;

import budget.dao.TransactionDAO;

import java.util.ArrayList;
import java.util.Date;

public class Budget {
    public static void addTransaction(Transaction transaction) {
        TransactionDAO.addTransaction(transaction);
    }

    void updateTransaction(int id, Transaction modified) {
        TransactionDAO.updateTransaction(id, modified);
    }

    void updateTransaction(Transaction original, Transaction modified) {
        int id = original.getId();
        if (id != -1) {
            TransactionDAO.updateTransaction(id, modified);
        } else {
            System.out.println("Transaction hasn't id"); //Добавить логи
        }
    }

    void deleteTransaction(Transaction transaction) {

    }

    Transaction getTransactionById(int id) {
        return TransactionDAO.getTransactionById(id);
    }

    ArrayList<Transaction> getTransactionByDate(Date date) { //or Calendar?
        return new ArrayList<>();
    }
}
