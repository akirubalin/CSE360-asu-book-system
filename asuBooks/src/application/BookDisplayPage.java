package application;

import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookDisplayPage extends Page {

    private String[] bookConditions = {"Used Like New", "Moderately Used", "Heavily Used"};
    private String[] bookCategories = {"Science", "Computer", "Math", "English Language", "History"};

    private ComboBox<String> bookCategoryMenu;
    private ComboBox<String> bookConditionMenu;
    private ListView<String> bookListView;
    
    public BookDisplayPage(Stage stage) {
        super(stage);
        display();
    }

    @Override
    public void display() {
        // Create the layout
        HBox layout = new HBox(10); // Use HBox to align elements horizontally

        // Combo boxes for category and condition
        displayBookCategoryMenu();
        displayBookConditionMenu();
        
        // Add the combo boxes to the layout
        VBox filters = new VBox(10);
        Label categoryLabel = new Label("Select Book Category:");
        Label conditionLabel = new Label("Select Book Condition:");

        filters.getChildren().addAll(categoryLabel, bookCategoryMenu, conditionLabel, bookConditionMenu);
        layout.getChildren().addAll(filters);

        // Set up the ListView
        bookListView = new ListView<>();
        layout.getChildren().add(bookListView); // Add the book list view

        // Create and set up the scene
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    // Method to display the book category menu
    public void displayBookCategoryMenu() {
        bookCategoryMenu = new ComboBox<>();
        bookCategoryMenu.getItems().addAll(bookCategories);
        bookCategoryMenu.setPromptText("Select Category");
    }

    // Method to display the book condition menu
    public void displayBookConditionMenu() {
        bookConditionMenu = new ComboBox<>();
        bookConditionMenu.getItems().addAll(bookConditions);
        bookConditionMenu.setPromptText("Select Condition");
    }

    // Method to get the selected book category
    public String selectBookCategory() {
        return bookCategoryMenu.getValue();
    }

    // Method to get the selected book condition
    public String selectBookCondition() {
        return bookConditionMenu.getValue();
    }
}
