package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.json.JSONObject;
import java.io.*;
import javafx.scene.Group;

public class AdminPage extends Page {
    public static final Label pageTitle = new Label("Admin Dashboard: View the application's books here");
    public static final Button returnButton = new Button("Return");
    public static final Button userManagementButton = new Button("Manage Users");

    public static final Button salesStatisticsButton = new Button("View Sales");
    public static final String[] bookConditions = {"Used Like New", "Moderately Used", "Heavily Used", "No Preference"};
    public static final String[] bookCategories = {"Science", "Art", "Mathematics", "English", "History"};
    public static final String informationCardLayout = "-fx-border-color: black;\n" +
            "-fx-border-insets: 3;\n" +
            "-fx-border-width: 3;\n";

    private Label soldBookLabel;
    private Label listedBookLabel;
    private Label soldBookTitle;
    private Label soldBookPrice;
    private Label soldBookCategory;
    private Label soldBookCondition;
    private Label soldBookAuthor;
    private Label soldBookYear;

    private Label listedBookTitle;
    private Label listedBookPrice;
    private Label listedBookCategory;
    private Label listedBookCondition;
    private Label listedBookAuthor;
    private Label listedBookYear;
    private JSONArray allSoldBooks;
    private JSONArray allListedBooks;

    private ComboBox<String> bookCategoryMenu;
    private ComboBox<String> bookConditionMenu;
    private ListView<String> availableBooksListView;
    private ObservableList<String> availableBooks;
    private ListView<String> soldBooksListView;
    private ObservableList<String> soldBooks;
    private VBox soldBookInformationCard;
    private VBox listedBooksInformationCard;


    public AdminPage(Stage stage) {
        super(stage);
        setupAdminPage();
    }

    private void setupAdminPage() {
        gatherBooksSoldAndListed();

        displayRedirectButtons();

        VBox returnButtonContainer = new VBox();
        returnButtonContainer.setAlignment(Pos.TOP_RIGHT);
        returnButtonContainer.setPadding(new Insets(10));
        returnButtonContainer.getChildren().addAll(returnButton, userManagementButton, salesStatisticsButton);

        // Admin page content
        pageTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        pageTitle.setWrapText(true);
        pageTitle.setLayoutX(125);
        pageTitle.setLayoutY(0);
        pageTitle.setTextFill(Color.DARKBLUE);

        displayListsView();
        displayLabels();
        VBox contentLayout = new VBox(20);
        contentLayout.setPadding(new Insets(20));
        contentLayout.setAlignment(Pos.CENTER_LEFT);
        contentLayout.setSpacing(10);
        contentLayout.getChildren().addAll(listedBookLabel, availableBooksListView, soldBookLabel, soldBooksListView);

        displayInformationCard();
        Group tempContainer = new Group();
        tempContainer.getChildren().addAll(contentLayout, listedBooksInformationCard, soldBookInformationCard, pageTitle);


        // Layout the page with the return button at the top
        BorderPane layout = new BorderPane();
        layout.setTop(returnButtonContainer);
        layout.setCenter(tempContainer);

        // Combo boxes for category and condition
        displayBookCategoryMenu();
        displayBookConditionMenu();

        // Add the combo boxes to the layout
        VBox filters = new VBox(10);

        filters.getChildren().addAll(bookCategoryMenu, bookConditionMenu);
        layout.getChildren().addAll(filters);

        bookCategoryMenu.setOnAction((event) -> {
            displayBooksByCategory();
        });

        bookConditionMenu.setOnAction((event) -> {
            displayBooksByCategoryAndCondition();
        });

        availableBooksListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                fillInListedInformationCard(newValue);
            }
        });

        soldBooksListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                fillInSoldInformationCard(newValue);
            }
        });
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
    }
    private void displayLabels() {
        soldBookLabel = new Label("Sold Books: ");
        listedBookLabel = new Label("Listed Books: ");

        soldBookTitle = new Label("Title: ");
        soldBookAuthor = new Label("Author: ");
        soldBookYear = new Label("Published Year: ");
        soldBookCategory = new Label("Category: ");
        soldBookCondition = new Label("Condition: ");
        soldBookPrice = new Label("Price: ");


        listedBookTitle = new Label("Title: ");
        listedBookAuthor = new Label("Author: ");
        listedBookYear = new Label("Published Year: ");
        listedBookCategory = new Label("Category: ");
        listedBookCondition = new Label("Condition: ");
        listedBookPrice = new Label("Price: ");
    }

    private void clearSoldLabels() {
        soldBookTitle.setText("Title: ");
        soldBookAuthor.setText("Author: ");
        soldBookYear.setText("Published Year: ");
        soldBookCategory.setText("Category: ");
        soldBookCondition.setText("Condition: ");
        soldBookPrice.setText("Price: ");
    }

    private void clearListedLabels() {
        listedBookTitle.setText("Title: ");
        listedBookAuthor.setText("Author: ");
        listedBookYear.setText("Published Year: ");
        listedBookCategory.setText("Category: ");
        listedBookCondition.setText("Condition: ");
        listedBookPrice.setText("Price: ");
    }
    private void fillInListedInformationCard(String title) {
        if(title == null) {
            listedBooksInformationCard.getChildren().removeAll();
            return;
        }

        listedBooksInformationCard.getChildren().removeAll();
        clearListedLabels();
        JSONObject book = new JSONObject();

        for(int i = 0; i < allListedBooks.length(); ++i) {
            book = allListedBooks.getJSONObject(i);
            if(book.getString("title").equals(title)) {
                break;
            }
        }

        listedBookTitle.setText(listedBookTitle.getText() + book.getString("title"));
        listedBookAuthor.setText(listedBookAuthor.getText() + book.getString("author"));
        listedBookYear.setText(listedBookYear.getText() + String.valueOf(book.getInt("published year")));
        listedBookCategory.setText(listedBookCategory.getText() + book.getString("category"));
        listedBookCondition.setText(listedBookCondition.getText() + book.getString("condition"));
        listedBookPrice.setText(listedBookPrice.getText() + Double.toString(book.getDouble("price")));

        listedBooksInformationCard.getChildren().setAll(listedBookTitle, listedBookAuthor, listedBookYear, listedBookCategory, listedBookCondition, listedBookPrice);

    }

    private void fillInSoldInformationCard(String title) {
        if(title == null) {
            soldBookInformationCard.getChildren().removeAll();
            return;
        }

        soldBookInformationCard.getChildren().removeAll();
        clearSoldLabels();
        JSONObject book = new JSONObject();

        for(int i = 0; i < allSoldBooks.length(); ++i) {
            book = allSoldBooks.getJSONObject(i);
            if(book.getString("title").equals(title)) {
                break;
            }
        }

        soldBookTitle.setText(soldBookTitle.getText() + book.getString("title"));
        soldBookAuthor.setText(soldBookAuthor.getText() + book.getString("author"));
        soldBookYear.setText(soldBookYear.getText() + String.valueOf(book.getInt("published year")));
        soldBookCategory.setText(soldBookCategory.getText() + book.getString("category"));
        soldBookCondition.setText(soldBookCondition.getText() + book.getString("condition"));
        soldBookPrice.setText(soldBookPrice.getText() + Double.toString(book.getDouble("price")));

        soldBookInformationCard.getChildren().setAll(soldBookTitle, soldBookAuthor, soldBookYear, soldBookCategory, soldBookCondition, soldBookPrice);
    }
    private void displayRedirectButtons() {
        returnButton.setOnAction(e -> navigateToLoginPage());
        userManagementButton.setOnAction(e -> navigateToUserManagementPage());
        salesStatisticsButton.setOnAction(e->navigateToSalesStatisticsPage());

        returnButton.setMinWidth(100);
        userManagementButton.setMinWidth(100);
        salesStatisticsButton.setMinWidth(100);

    }
    public void displayInformationCard() {
        listedBooksInformationCard = new VBox(20);
        listedBooksInformationCard.setPadding(new Insets(10));
        listedBooksInformationCard.setMaxSize(350, 160);
        listedBooksInformationCard.setMinSize(350, 160);
        listedBooksInformationCard.setLayoutX(300);
        listedBooksInformationCard.setLayoutY(40);
        listedBooksInformationCard.setSpacing(3);
        listedBooksInformationCard.setStyle(informationCardLayout);


        soldBookInformationCard = new VBox(20);
        soldBookInformationCard.setPadding(new Insets(10));
        soldBookInformationCard.setMaxSize(350, 160);
        soldBookInformationCard.setMinSize(350, 160);
        soldBookInformationCard.setLayoutX(300);
        soldBookInformationCard.setLayoutY(240);
        soldBookInformationCard.setSpacing(3);
        soldBookInformationCard.setStyle(informationCardLayout);
    }
    public void displayListsView() {
        availableBooks = FXCollections.observableArrayList();
        availableBooksListView = new ListView<>();
        availableBooksListView.setMaxSize(200,160);

        soldBooks = FXCollections.observableArrayList();
        soldBooksListView = new ListView<>();
        soldBooksListView.setMaxSize(200, 160);
    }

    // Method to display the book category menu
    public void displayBookCategoryMenu() {
        bookCategoryMenu = new ComboBox<>();
        bookCategoryMenu.getItems().addAll(bookCategories);
        bookCategoryMenu.setPromptText("Select Category");
        bookCategoryMenu.setMinWidth(150);

    }
    // Method to display the book condition menu
    public void displayBookConditionMenu() {
        bookConditionMenu = new ComboBox<>();
        bookConditionMenu.getItems().addAll(bookConditions);
        bookConditionMenu.setPromptText("Select Condition");
        bookConditionMenu.setMinWidth(150);
    }
    private void navigateToLoginPage() {
        new LoginPage(stage).display(); // Navigate back to the login page
    }

    private void navigateToUserManagementPage() {
        new UserManagementPage(stage).display();
    }

    private void navigateToSalesStatisticsPage() {new SalesDataPage(stage).display();}

    public void gatherBooksSoldAndListed() {
        try {
            BufferedReader listedreader = new BufferedReader(new FileReader("/Users/sophia/IdeaProjects/CSEBooks/src/main/java/com/example/csebooks/availableBooks.json"));
            StringBuilder jsonListedBuilder = new StringBuilder();
            String line;
            while ((line = listedreader.readLine()) != null) {
                jsonListedBuilder.append(line);
            }
            listedreader.close();
            allListedBooks = new JSONArray(jsonListedBuilder.toString());

            BufferedReader soldreader = new BufferedReader(new FileReader("/Users/sophia/IdeaProjects/CSEBooks/src/main/java/com/example/csebooks/salesRecord.json"));
            StringBuilder jsonSoldBuilder = new StringBuilder();
            while ((line = soldreader.readLine()) != null) {
                jsonSoldBuilder.append(line);
            }
            soldreader.close();
            allSoldBooks = new JSONArray(jsonSoldBuilder.toString());
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void curateAvailableBookshelfByCategory(String category) {
        availableBooks.clear();

            for(int i = 0; i < allListedBooks.length(); ++i) {
                JSONObject book = allListedBooks.getJSONObject(i);

                if(book.getString("category").equals(category)) {
                    availableBooks.add(book.getString("title"));
                }
            }

    }

    public void curateAvailableBookshelfByCategoryAndCondition(String category, String condition) {
        availableBooks.clear();

            for (int i = 0; i < allListedBooks.length(); ++i) {
                JSONObject book = allListedBooks.getJSONObject(i);

                if (book.getString("category").equals(category) && book.getString("condition").equals(condition)) {
                    availableBooks.add(book.getString("title"));
                }
            }
    }

    public void curateSoldBooksByCategory(String category) {
        soldBooks.clear();


        for(int i = 0; i < allSoldBooks.length(); ++i) {
            JSONObject book = allSoldBooks.getJSONObject(i);

            if(book.getString("category").equals(category)) {
                soldBooks.add(book.getString("title"));
            }
        }

    }

    public void curateSoldBooksByCategoryAndCondition(String category, String condition) {
        soldBooks.clear();

        for(int i = 0; i < allSoldBooks.length(); ++i) {
            JSONObject book = allSoldBooks.getJSONObject(i);

            if(book.getString("category").equals(category) && book.getString("condition").equals(condition)) {
                soldBooks.add(book.getString("title"));
                System.out.println(book.getString("title"));
            }
        }

    }

    public void displayBooksByCategoryAndCondition() {
        String conditionSelected = bookConditionMenu.getValue();
        if ((!(bookCategoryMenu.getSelectionModel().isEmpty())) && !(conditionSelected.equals("No Preference"))) {
            String categorySelected = bookCategoryMenu.getValue();
            curateAvailableBookshelfByCategoryAndCondition(categorySelected, conditionSelected);
            curateSoldBooksByCategoryAndCondition(categorySelected, conditionSelected);
            soldBooksListView.setItems(soldBooks);
            availableBooksListView.setItems(availableBooks);
        } else  {
            displayBooksByCategory();
        }
    }

    public void displayBooksByCategory() {
        String categorySelected = bookCategoryMenu.getValue();
        if(bookConditionMenu.getSelectionModel().isEmpty() || bookConditionMenu.getValue().equals("No Preference")) {
            curateAvailableBookshelfByCategory(categorySelected);
            curateSoldBooksByCategory(categorySelected);
            soldBooksListView.setItems(soldBooks);
            availableBooksListView.setItems(availableBooks);
        }
        else {
            displayBooksByCategoryAndCondition();
        }
    }


    @Override
    public void display() {
        stage.show();
    }
}
