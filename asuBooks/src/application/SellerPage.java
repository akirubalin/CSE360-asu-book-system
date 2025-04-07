package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SellerPage extends Page {
    private TableView<Book> bookTable;

    public SellerPage(Stage stage) {
        super(stage);
        setupSellerPage();
    }

    private void setupSellerPage() {
        // Return button
        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> navigateToLoginPage());
        
        HBox returnButtonContainer = new HBox();
        returnButtonContainer.setAlignment(Pos.TOP_RIGHT);
        returnButtonContainer.setPadding(new Insets(10));
        returnButtonContainer.getChildren().add(returnButton);

        // Seller page content
        Label pageTitle = new Label("Seller Dashboard");
        pageTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label sellerMessage = new Label("Welcome"
        		+ ", Seller! Here you can list books for sale and manage your inventory.");
        sellerMessage.setWrapText(true);

        // Buttons
        Button listBookButton = new Button("List a New Book");
        listBookButton.setOnAction(e -> listBook());
        Button viewBookStatusButton = new Button("View Book Status");
        viewBookStatusButton.setOnAction(e -> showBookStatus());

        // Button container
        HBox buttonContainer = new HBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(listBookButton, viewBookStatusButton);

        // Content layout
        VBox contentLayout = new VBox(20);
        contentLayout.setPadding(new Insets(20));
        contentLayout.setAlignment(Pos.CENTER);
        contentLayout.getChildren().addAll(pageTitle, sellerMessage, buttonContainer);

        // Initialize book table
        setupBookTable();
        contentLayout.getChildren().add(bookTable);

        // Layout the page with the return button at the top
        BorderPane layout = new BorderPane();
        layout.setTop(returnButtonContainer);
        layout.setCenter(contentLayout);
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
    }
    
    private void setupBookTable() {
        // Create table
        bookTable = new TableView<>();
        bookTable.setMaxHeight(300);
        
        // Create columns and add it to the table
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookTable.getColumns().add(0, titleColumn);
        
        TableColumn<Book, String> bookIDColumn = new TableColumn<>("BookID");
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookTable.getColumns().add(1, bookIDColumn);
        
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookTable.getColumns().add(2, authorColumn);
        
        TableColumn<Book, String> publishyearColumn = new TableColumn<>("Published Year");
        publishyearColumn.setCellValueFactory(new PropertyValueFactory<>("published year"));
        bookTable.getColumns().add(3, publishyearColumn);
        
        TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        bookTable.getColumns().add(4, categoryColumn);
        
        TableColumn<Book, String> conditionColumn = new TableColumn<>("Condition");
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("condition"));
        bookTable.getColumns().add(5, conditionColumn);
        
        TableColumn<Book, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        bookTable.getColumns().add(6, statusColumn);    
                
//        // Set column widths
        titleColumn.setPrefWidth(115);
        bookIDColumn.setPrefWidth(115);
        authorColumn.setPrefWidth(115);
        publishyearColumn.setPrefWidth(115);
        categoryColumn.setPrefWidth(115);
        conditionColumn.setPrefWidth(115);
        statusColumn.setPrefWidth(115);
        
    }

    private void showBookStatus() {
        // Refresh the table data
        bookTable.setVisible(true);
        System.out.println("Showing book status...");
        
        
    }

    private void listBook() {
        System.out.println("Listing a new book...");
    }

    private void navigateToLoginPage() {
        new LoginPage(stage).display(); // Navigate back to the login page
    }

    @Override
    public void display() {
        stage.show();
    }
}
