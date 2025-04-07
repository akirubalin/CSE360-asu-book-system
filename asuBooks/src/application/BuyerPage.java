package application;

import javafx.geometry.Insets; 
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class BuyerPage extends Page {

	//comment
	private List<JSONObject> books;
    private VBox bookListContainer;
    private ComboBox<String> categoryFilter;
    private int currentPage = 0;
    private static final int BOOKS_PER_PAGE = 3; //num of books
    private Label pageLabel;
    private CheckBox likeNewCheckBox;
    private CheckBox heavilyUsedCheckBox;
    private CheckBox moderatelyUsedCheckBox;
    private VBox cartContainer;
    private BorderPane layout; 
    private List<JSONObject> cartItems;
    private HashMap<String, Integer> bookCounters;
    private Label cartCountLabel;

    public BuyerPage(Stage stage) {
        super(stage);
        cartItems = new ArrayList<>();
        bookCounters = new HashMap<>();
        loadBooks();
        setupBuyerPage();
    }

    private void setupBuyerPage() {
        layout = new BorderPane();
        
        pageLabel = new Label("Page 1");

        // Image
        ImageView logo = new ImageView(new Image("file:Image/asubooks logo.png"));
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);

        // header
        Label title = new Label("ASU Books");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // search filter
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for a book...");
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue, categoryFilter.getValue()));

        categoryFilter = new ComboBox<>();
        categoryFilter.getItems().addAll("All", "Science", "Computer", "English", "History", "Art", "Mathematics");
        categoryFilter.setValue("All");
        categoryFilter.setOnAction(e -> filterBooks(searchBar.getText(), categoryFilter.getValue()));

        // logout
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> navigateToLoginPage());

        HBox header = new HBox(10, logo, title, searchBar, categoryFilter, logoutButton);
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #f2f2f2;");
        
        // Condition filters in VBox
        likeNewCheckBox = new CheckBox("Used like new");
        heavilyUsedCheckBox = new CheckBox("Heavily Used");
        moderatelyUsedCheckBox = new CheckBox("Moderately Used");

        likeNewCheckBox.setOnAction(e -> filterBooks(searchBar.getText(), categoryFilter.getValue()));
        heavilyUsedCheckBox.setOnAction(e -> filterBooks(searchBar.getText(), categoryFilter.getValue()));
        moderatelyUsedCheckBox.setOnAction(e -> filterBooks(searchBar.getText(), categoryFilter.getValue()));

        VBox conditionFilters = new VBox(10, likeNewCheckBox, heavilyUsedCheckBox, moderatelyUsedCheckBox);
        conditionFilters.setAlignment(Pos.CENTER);
        conditionFilters.setPadding(new Insets(10));

        // display the books
        bookListContainer = new VBox(10);
        bookListContainer.setPadding(new Insets(20));
        updateBookList("", "All");

        // page controls
        Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");
        prevButton.setOnAction(e -> navigatePage(-1, searchBar.getText(), categoryFilter.getValue()));
        nextButton.setOnAction(e -> navigatePage(1, searchBar.getText(), categoryFilter.getValue()));

        HBox paginationControls = new HBox(10, prevButton, pageLabel, nextButton);
        paginationControls.setAlignment(Pos.CENTER);
        paginationControls.setPadding(new Insets(10));
        
        // CART STUFFS
        cartContainer = new VBox(10);
        cartContainer.setPadding(new Insets(10));
        cartContainer.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-background-color: #f2f2f2;");

        // Main content layout (HBox: left, middle, right)
        HBox mainContent = new HBox(10);

        // LEFT CONDITION FILTER
        VBox leftSection = new VBox(10, conditionFilters);
        leftSection.setPrefWidth(200);

        // MIDDLE BOOK LIBRARY
        VBox centerSection = new VBox(10, bookListContainer, paginationControls);
        centerSection.setPrefWidth(450);
        VBox.setVgrow(bookListContainer, Priority.ALWAYS);

        // RIGHT CARTTTT
        VBox rightSection = new VBox(10);
        rightSection.setAlignment(Pos.CENTER);

        // Label for cart count
        cartCountLabel = new Label("Books in Cart: 0");
        cartCountLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        cartCountLabel.setPadding(new Insets(5));

        // Button to view the cart
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setOnAction(e -> showCartContents());

        rightSection.getChildren().addAll(cartCountLabel, viewCartButton);

        mainContent.getChildren().addAll(leftSection, centerSection, rightSection);

        layout.setTop(new VBox(header));
        layout.setCenter(mainContent);
        layout.setBottom(paginationControls);

        Scene scene = new Scene(layout, 800, 550);
        stage.setScene(scene);
    }

    private void loadBooks() {
        books = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/application/books.json")));
            JSONArray bookArray = new JSONArray(content);
            for (int i = 0; i < bookArray.length(); i++) {
                books.add(bookArray.getJSONObject(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not load book data.");
        }
    }

    private void updateBookList(String filter, String category) {
        bookListContainer.getChildren().clear();

        // filter
        List<JSONObject> filteredBooks = books.stream()
                .filter(book -> book.getString("title").toLowerCase().contains(filter.toLowerCase()) &&
                        (category.equals("All") || book.getString("category").equalsIgnoreCase(category)) &&
                        isConditionSelected(book.getString("condition")))
                .toList();

        // get books for page
        int start = currentPage * BOOKS_PER_PAGE;
        int end = Math.min(start + BOOKS_PER_PAGE, filteredBooks.size());
        List<JSONObject> booksToDisplay = filteredBooks.subList(start, end);

        // display everything
        booksToDisplay.forEach(book -> {
            VBox bookBox = createBookBox(book);
            bookListContainer.getChildren().add(bookBox);
        });

        int totalPages = (int) Math.ceil((double) filteredBooks.size() / BOOKS_PER_PAGE);
        pageLabel.setText("Page " + (currentPage + 1) + " of " + totalPages);
    }

    private boolean isConditionSelected(String condition) {
        if (!likeNewCheckBox.isSelected() && !heavilyUsedCheckBox.isSelected() && !moderatelyUsedCheckBox.isSelected()) {
            return true;
        }
        return (likeNewCheckBox.isSelected() && condition.equalsIgnoreCase("Used like new")) ||
               (heavilyUsedCheckBox.isSelected() && condition.equalsIgnoreCase("Heavily Used")) ||
               (moderatelyUsedCheckBox.isSelected() && condition.equalsIgnoreCase("Moderately Used"));
    }

    private VBox createBookBox(JSONObject book) {
        VBox bookBox = new VBox(5);
        bookBox.setPadding(new Insets(10));
        bookBox.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-background-color: #f9f9f9;");

        Label titleLabel = new Label(book.getString("title"));
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        HBox categoryConditionBox = new HBox(30);
        Label categoryLabel = new Label(book.getString("category"));
        Label conditionLabel = new Label(book.getString("condition"));
        categoryConditionBox.getChildren().addAll(categoryLabel, conditionLabel);
        
        Label authorLabel = new Label(book.getString("author"));

        // category and condition; one line
        HBox priceQuantityBox = new HBox(30);
        Label priceLabel = new Label("$" + book.getDouble("price"));
        Label quantityLabel = new Label("Quantity: " + book.getInt("quantity"));
        priceQuantityBox.getChildren().addAll(priceLabel, quantityLabel);
        
        // Counter with "+" and "-" buttons
        Button minusButton = new Button("-");
        Button plusButton = new Button("+");
        Label counterLabel = new Label(String.valueOf(bookCounters.getOrDefault(book.getString("title"), 0)));

        // event handler
        plusButton.setOnAction(e -> {
            int currentCount = bookCounters.getOrDefault(book.getString("title"), 0);
            int availableQuantity = book.getInt("quantity");

            if (currentCount < availableQuantity) {
                currentCount++;
                bookCounters.put(book.getString("title"), currentCount);
                counterLabel.setText(String.valueOf(currentCount));
                addBookToCart(book);
            }
        });

        minusButton.setOnAction(e -> {
            int currentCount = bookCounters.getOrDefault(book.getString("title"), 0);

            if (currentCount > 0) {
                currentCount--;
                bookCounters.put(book.getString("title"), currentCount);
                counterLabel.setText(String.valueOf(currentCount));
                removeBookFromCart(book);
            }
        });

        // price and quantity; one line
        HBox authorAndButtonsBox = new HBox(20, authorLabel, minusButton, counterLabel, plusButton);
        authorAndButtonsBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(minusButton, Priority.ALWAYS);
        HBox.setHgrow(counterLabel, Priority.ALWAYS);
        HBox.setHgrow(plusButton, Priority.ALWAYS);

        //add up all the components
        bookBox.getChildren().addAll(categoryConditionBox, titleLabel, authorAndButtonsBox, priceQuantityBox);

        return bookBox;
    }
    
    private void addBookToCart(JSONObject book) {
        cartItems.add(book);
        updateCartDisplay();
        updateCartLabel();
    }
    
    private void removeBookFromCart(JSONObject book) {
        cartItems.remove(book);
        updateCartDisplay();
        updateCartLabel();
    }

    private void updateCartDisplay() {
        cartContainer.getChildren().clear();

        for (JSONObject book : cartItems) {
            Label cartItem = new Label(book.getString("title") + " - $" + book.getDouble("price"));
            cartContainer.getChildren().add(cartItem);
        }
    }
    
    private void showCartContents() {
        VBox cartDisplay = new VBox(10);
        cartDisplay.setPadding(new Insets(10));
        cartDisplay.setStyle("-fx-background-color: #f9f9f9;");

        // Display each book in the cart
        for (JSONObject book : cartItems) {
            Label bookLabel = new Label(book.getString("title") + " - $" + book.getDouble("price"));
            cartDisplay.getChildren().add(bookLabel);
        }

        Separator separator = new Separator();
        cartDisplay.getChildren().add(separator);

        double totalPrice = cartItems.stream()
                .mapToDouble(book -> book.getDouble("price"))
                .sum();
        Label totalLabel = new Label("Total: $" + String.format("%.2f", totalPrice));
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        cartDisplay.getChildren().add(totalLabel);

        // buy
        Button purchaseButton = new Button("Purchase Books");
        purchaseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        purchaseButton.setOnAction(e -> {
            // update book quantities
            for (JSONObject cartItem : cartItems) {
                String title = cartItem.getString("title");
                int currentCount = bookCounters.getOrDefault(title, 0);

                // find and change number
                for (JSONObject book : books) {
                    if (book.getString("title").equals(title)) {
                        int newQuantity = book.getInt("quantity") - currentCount;
                        book.put("quantity", Math.max(newQuantity, 0));
                        break;
                    }
                }
            }

            //update
            saveBooksToFile();

            cartItems.clear();
            bookCounters.clear();
            updateCartDisplay();
            updateCartLabel();

            //message
            showAlert("Purchase Successful", "Thank you for your purchase!");
        });

        HBox purchaseButtonContainer = new HBox();
        purchaseButtonContainer.setAlignment(Pos.BOTTOM_RIGHT);
        purchaseButtonContainer.getChildren().add(purchaseButton);
        cartDisplay.getChildren().add(purchaseButtonContainer);

        Scene cartScene = new Scene(cartDisplay, 400, 300);
        Stage cartStage = new Stage();
        cartStage.setTitle("Cart Details");
        cartStage.setScene(cartScene);
        cartStage.show();
    }

    private void updateCartLabel() {
        cartCountLabel.setText("Books in cart: " + cartItems.size());
    }
    
    private void navigatePage(int direction, String filter, String category) {
        int totalPages = (int) Math.ceil((double) books.size() / BOOKS_PER_PAGE);
        
        // range for page index
        if ((direction < 0 && currentPage > 0) || (direction > 0 && currentPage < totalPages - 1)) {
            currentPage += direction;
        }
        
        updateBookList(filter, category);
    }

    private void filterBooks(String filter, String category) {
        updateBookList(filter, category);
    }

    private void navigateToLoginPage() {
        new LoginPage(stage).display();
    }
    
    private void saveBooksToFile() {
        try {
            JSONArray updatedBooksArray = new JSONArray(books); 
            Files.write(Paths.get("src/application/books.json"), updatedBooksArray.toString(4).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not update book data.");
        }
    }

    @Override
    public void display() {
        stage.show();
    }

    // Helper method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}