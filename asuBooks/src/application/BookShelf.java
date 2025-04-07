package application;
import java.util.ArrayList;
public class BookShelf {
    private ArrayList<Book> booksMatchingSpecifications;

    public BookShelf() {
        booksMatchingSpecifications = new ArrayList<>();
    }

    public void fetchSoldBooksByCategory(BookStore bookstore, String category) {
        clearBooksMatchingSpecifications();
        ArrayList<Book> soldBooks = bookstore.getBooksSold();
        for(int i = 0; i < soldBooks.size(); ++i) {
            if(soldBooks.get(i).getCategory().equals(category)) {
                booksMatchingSpecifications.add(soldBooks.get(i));
            }
        }
    }

    public void fetchListedBooksByCategory(BookStore bookstore, String category) {
        clearBooksMatchingSpecifications();
        ArrayList<Book> listedBooks = bookstore.getBooksListed();
        for(int i = 0; i < listedBooks.size(); ++i) {
            if(listedBooks.get(i).getCategory().equals(category)) {
                booksMatchingSpecifications.add(listedBooks.get(i));
            }
        }
    }

    public void fetchListedBooksByCategoryAndCondition(BookStore bookstore, String category, String condition) {
        clearBooksMatchingSpecifications();
        ArrayList<Book> listedBooks = bookstore.getBooksListed();
        for(int i = 0; i < listedBooks.size(); ++i) {
            if(listedBooks.get(i).getCategory().equals(category) && listedBooks.get(i).getCondition().equals(condition)) {
                booksMatchingSpecifications.add(listedBooks.get(i));
            }
        }
    }

    public void fetchSoldBooksByCategoryAndCondition(BookStore bookstore, String category, String condition) {
        clearBooksMatchingSpecifications();
        ArrayList<Book> soldBooks = bookstore.getBooksSold();
        for(int i = 0; i < soldBooks.size(); ++i) {
            if(soldBooks.get(i).getCategory().equals(category) && soldBooks.get(i).getCondition().equals(condition)) {
                booksMatchingSpecifications.add(soldBooks.get(i));
            }
        }
    }

    public void clearBooksMatchingSpecifications() {
        booksMatchingSpecifications.clear();
    }

    public void testFilteringCapabilities() {
        for(int i = 0; i < booksMatchingSpecifications.size(); ++i) {
            booksMatchingSpecifications.get(i).printBookInformation();
        }
    }

}
