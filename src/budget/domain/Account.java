package budget.domain;

public class Account {
    private int id;
    private String name;
    private int balance;

    public Account(int id, String name, int balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    void transferTo (Account account, int amount) {
        this.balance -= amount;
        account.balance += amount;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }
}
