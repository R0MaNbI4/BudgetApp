package budget.domain.statistics.piechart;

import budget.domain.Account;
import budget.domain.Category;
import budget.domain.Transaction;
import budget.ui.statistics.CategoryType;
import org.jfree.data.general.DefaultPieDataset;

import java.util.ArrayList;
import java.util.Date;

public class Dataset extends DefaultPieDataset {
    public void update(Date startDate, Date endDate, CategoryType categoryType, Account account) {
        this.clear();
        ArrayList<Category> categories = Category.getCategoriesByType(categoryType);
        for (Category category : categories) {
            ArrayList<Transaction> transactions;
            double sumTotal = 0;

            if (account.isSuperAccount()) {
                transactions = Transaction.getTransactionsByPeriodAndCategory(startDate, endDate, category);
            } else {
                transactions = Transaction.getTransactionsByPeriodAndCategoryAndAccount(startDate, endDate, category, account);
            }

            for (Transaction transaction : transactions) {
                sumTotal += transaction.getValue();
            }

            sumTotal /= 100;
            this.setValue(category.getName(), sumTotal);
        }
    }
}