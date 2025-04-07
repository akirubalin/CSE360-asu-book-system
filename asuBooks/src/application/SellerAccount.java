package application;
import java.util.ArrayList;

public class SellerAccount extends Account {
    ArrayList<Book> booksListed;
    ArrayList<Book> booksSold;
    ArrayList<Book> booksPending;

    double revenue;

    public SellerAccount(String username, String password) {
        super(username, password);
        booksListed = new ArrayList<>();
        booksSold = new ArrayList<>();
        booksPending = new ArrayList<>();
        revenue = 0.0;
    }

    public ArrayList<Book> getBooksSold() {
        return booksSold;
    }

    public ArrayList<Book> getBooksPending() {
        return booksPending;
    }

    public ArrayList<Book> getBooksListed() {
        return booksListed;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void addToBooksPending(Book book) {
        booksPending.add(book);
    }

    public void moveBookToListed(int bookID) {
        for(int i = 0; i < booksPending.size(); ++i) {
            if(booksPending.get(i).getBookID() == bookID) {
                booksListed.add(booksPending.get(i));
                booksPending.remove(i);
                break;
            }
        }
    }

    public void moveBookToSold(int bookID) {
        for(int i = 0; i < booksListed.size(); ++i) {
            if(booksListed.get(i).getBookID() == bookID) {
                booksSold.add(booksListed.get(i));
                booksListed.remove(i);
                break;
            }
        }

    }


}
