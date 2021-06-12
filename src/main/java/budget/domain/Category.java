package budget.domain;

public class Category {
    private int id;
    private String name;
    private boolean isIncome;

    public Category(int id, String name, boolean isIncome) {
        this.id = id;
        this.name = name;
        this.isIncome = isIncome;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isIncome=" + isIncome +
                '}';
    }

    public Category(String name, boolean isIncome) {
        this(-1, name, isIncome);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isIncome() {
        return isIncome;
    }
}
