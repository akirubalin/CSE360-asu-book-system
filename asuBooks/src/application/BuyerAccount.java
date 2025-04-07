package application;
import java.util.ArrayList;
public class BuyerAccount extends Account {
    ArrayList<Book> ShoppingCart;
    ArrayList<Book> booksBought;
    double balance;
    double amountSpent;

    public BuyerAccount(String username, String password) {
        super(username, password);
        ShoppingCart = new ArrayList<>();
        booksBought = new ArrayList<>();
        balance = 0.0;
        amountSpent = 0.0;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public ArrayList<Book> getBooksBought() {
        return booksBought;
    }

    public ArrayList<Book> getShoppingCart() {
        return ShoppingCart;
    }

    public void addToShoppingCart(Book book) {
        ShoppingCart.add(book);
    }

    public void addToBooksBought(int bookID) {
        for(int i = 0; i < ShoppingCart.size(); ++i) {
            if(ShoppingCart.get(i).getBookID() == bookID) {
                booksBought.add(ShoppingCart.get(i));
                ShoppingCart.remove(i);
            }
        }
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
