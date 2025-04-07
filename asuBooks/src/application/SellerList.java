package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.Group;
public class SellerList extends Page {
    private Button returnButton;
    private ComboBox<String> conditionComboBox; // Dropdown for condition
    private ComboBox<String> categoryComboBox; // Dropdown for category
    private TextField bookNameField; // Input field for book name
    private TextField priceField; // Input field for price
    private TextField publishedYearField;
    private TextField authorNameField;
    private Button addBookButton; // Button to add the book
    private VBox bookListContainer; // Container to display listed books
    private static final String FILE_PATH = "src/application/pendingBooks.json"; // File path for saving books
    private static final String PERS_FILE_PATH = "/Users/sophia/IdeaProjects/CSEBooks/src/main/java/com/example/csebooks/pendingBooks.json";
    private static final String[] CONDITIONS = {"Moderately Used", "Used Like New", "Heavily Used"}; // Available conditions
    private static final String[] CATEGORIES = {"Science", "Mathematics", "History", "English", "Art"}; // Available categories
    public SellerList(Stage stage) {
        super(stage);
        setupSellerListPage(); // Set up the page
    }
    private void setupSellerListPage() {
        returnButton = new Button("Return");
        returnButton.setOnAction(actionEvent ->  {
            new LoginPage(stage).display();
        });

        returnButton.setAlignment(Pos.CENTER_RIGHT);

        Label titleLabel = new Label("List a Book for Sale"); // Page title
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        // Input fields
        bookNameField = new TextField();
        bookNameField.setPromptText("Enter Book Name");
        authorNameField = new TextField();
        authorNameField.setPromptText("Enter Author Name");
        publishedYearField = new TextField();
        publishedYearField.setPromptText("Enter Published Year");
        conditionComboBox = new ComboBox<>();
        conditionComboBox.getItems().addAll(CONDITIONS); // Add options to condition dropdown
        conditionComboBox.setPromptText("Select Condition");
        categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(CATEGORIES); // Add options to category dropdown
        categoryComboBox.setPromptText("Select Category");
        priceField = new TextField();
        priceField.setPromptText("Enter Price");
        addBookButton = new Button("Add Book");
        addBookButton.setOnAction(e -> addBook()); // Trigger book addition on button click
        // Layout for input fields
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10); // Horizontal spacing
        inputGrid.setVgap(10); // Vertical spacing
        inputGrid.setPadding(new Insets(20)); // Padding around the grid
        inputGrid.add(new Label("Book Name:"), 0, 0);
        inputGrid.add(bookNameField, 1, 0);
        inputGrid.add(new Label("Author Name: "), 0, 1);
        inputGrid.add(authorNameField, 1, 1);
        inputGrid.add(new Label("Published Year: "), 0, 2);
        inputGrid.add(publishedYearField, 1, 2);
        inputGrid.add(new Label("Condition:"), 0, 3);
        inputGrid.add(conditionComboBox, 1, 3);
        inputGrid.add(new Label("Category:"), 0, 4);
        inputGrid.add(categoryComboBox, 1, 4);
        inputGrid.add(new Label("Price:"), 0, 5);
        inputGrid.add(priceField, 1, 5);
        inputGrid.add(addBookButton, 1, 6);
        // Container for displaying listed books
        bookListContainer = new VBox(10);
        bookListContainer.setPadding(new Insets(20));
        Label bookListTitle = new Label("My Items");
        bookListTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        bookListContainer.getChildren().add(bookListTitle);
        // Main layout combining title, input grid, and book list
        VBox layout = new VBox(20, titleLabel, inputGrid, bookListContainer);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Group entireLayout = new Group(layout, returnButton);
        Scene scene = new Scene(entireLayout, 600, 500); // Set the scene size
        stage.setScene(scene);
        loadBooks(); // Load existing books when the page is displayed
    }
    private void addBook() {
        // Get input values
        String bookName = bookNameField.getText();
        String condition = conditionComboBox.getValue();
        String category = categoryComboBox.getValue();
        String priceText = priceField.getText();
        String publishedYearText = publishedYearField.getText();
        String author = authorNameField.getText();

        // Check if any field is empty
        if (bookName.isEmpty() || condition == null || category == null || priceText.isEmpty()) {
            showAlert("Error", "Please fill out all fields."); // Show error for missing fields
            return;
        }
        try {
            double price = Double.parseDouble(priceText); // Parse price to ensure it's a number
            int publishedYear = Integer.parseInt(publishedYearText);
            JSONArray booksArray = loadBooksFromFile(); // Load existing books
            // Check for duplicate book names
            for (int i = 0; i < booksArray.length(); i++) {
                if (booksArray.getJSONObject(i).getString("title").equalsIgnoreCase(bookName)) {
                    showAlert("Error", "This book is already listed."); // Show error if duplicate
                    return;
                }
            }

            // Create a new book object
            JSONObject newBook = new JSONObject();
            newBook.put("title", bookName);
            newBook.put("condition", condition);
            newBook.put("category", category);
            newBook.put("price", price);
            newBook.put("published year", publishedYear);
            newBook.put("author", author);

            booksArray.put(newBook); // Add the new book to the array
            saveBooksToFile(booksArray); // Save updated book list to file
            // Add the new book to the display
            Label bookLabel = new Label(bookName + " (" + condition + ", " + category + ") - $" + price);
            bookListContainer.getChildren().add(bookLabel);
            clearForm(); // Clear the input fields after adding the book
            showAlert("Success", "Book added successfully!"); // Show success message
        } catch (NumberFormatException e) {
            showAlert("Error", "Price must be a valid number."); // Error if price is not a number
        }
    }
    private void loadBooks() {
        try {
            JSONArray booksArray = loadBooksFromFile(); // Load books from file
            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject book = booksArray.getJSONObject(i);
                // Format book details for display
                String bookDetails = book.getString("title") + " (" +
                        book.getString("condition") + ", " +
                        book.getString("category") + ") - $" +
                        book.getDouble("price");
                bookListContainer.getChildren().add(new Label(bookDetails)); // Add book to display
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load existing books."); // Show error if loading fails
        }
    }
    private JSONArray loadBooksFromFile() {
        try {
            if (Files.exists(Paths.get(FILE_PATH))) { // Check if file exists
                String content = new String(Files.readAllBytes(Paths.get(FILE_PATH))); // Read file content
                return new JSONArray(content); // Parse content into a JSON array
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray(); // Return empty array if file does not exist or an error occurs
    }
    private void saveBooksToFile(JSONArray booksArray) {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(booksArray.toString(4)); // Write JSON array to file with indentation
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save the book."); // Show error if saving fails
        }
    }
    private void clearForm() {
        bookNameField.clear(); // Clear book name input
        conditionComboBox.setValue(null); // Reset condition dropdown
        categoryComboBox.setValue(null); // Reset category dropdown
        priceField.clear(); // Clear price input
    }
    private void showAlert(String title, String message) {
        // Show an alert with a title and message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override
    public void display() {
        stage.show(); // Show the stage
    }
}
