package budget.domain;

import java.util.ArrayList;
import java.util.Date;

public class Budget {
    void addTransaction(Transaction transaction) {
    }

    Transaction getTransactionById(int id) {
        return new Transaction();
    }

    ArrayList<Transaction> getTransactionByDate(Date date) { //or Calendar?
        return new ArrayList<>();
    }

    void updateTransaction(Transaction original, Transaction modified) {

    }

    void deleteTransaction(Transaction transaction) {

    }
}
