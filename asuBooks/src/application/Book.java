package application;

import org.json.JSONObject;

public class Book {
    private int publishedYear;
    private int bookID;
    private String title;
    private String author;
    private String condition;
    private String category;
    private String status;

    //constructor
    public Book() {
        this.bookID = -1;
        this.publishedYear = 0;
        this.title = "";
        this.author = "";
        this.condition = "";
        this.category = "";
        this.status = "";
    }

    //constructor
    public Book(int publishedYear, int bookID, String title, String author, String condition, String category, String status) {
        this.publishedYear = publishedYear;
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.condition = condition;
        this.category = category;
        this.status = status;
    }
    
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    //getter setter
    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Method to convert a JSON object to a Book object
    public void convertJSONObjToBook(JSONObject unmoddedBook) {
        this.title = unmoddedBook.getString("title");
        this.author = unmoddedBook.getString("author");
        this.publishedYear = unmoddedBook.getInt("published year");
        this.category = unmoddedBook.getString("category");
        this.condition = unmoddedBook.getString("condition");
        this.status = unmoddedBook.optString("status", "available"); // Default status if not present
    }
}
