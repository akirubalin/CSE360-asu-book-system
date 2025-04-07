package application;

import java.util.ArrayList;

public class Order {
    ArrayList<Book> orderedBooks;
    double totalPrice;
    
    public Order() {
        orderedBooks = new ArrayList<>();
        totalPrice = 0.0;
    }
    
    public void calculateTotalPrice() {
        totalPrice = 0;
        for(int i = 0; i < orderedBooks.size(); ++i) {
            //totalPrice += price
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Book> getOrderedBooks() {
        return orderedBooks;
    }

    public void setOrderedBooks(ArrayList<Book> orderedBooks) {
        this.orderedBooks = orderedBooks;
    }
}
