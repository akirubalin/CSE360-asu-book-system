package application;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.Group;
import javafx.scene.text.Text;
import java.io.FileWriter;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;

public class BookApprovalPage extends Page{

    public static final Label adminMessage = new Label("Welcome, Admin!");
    public static final String informationCardLayout = "-fx-border-color: black;\n" +
            "-fx-border-insets: 3;\n" +
            "-fx-border-width: 3;\n";
    Button approveButton;
    Button rejectButton;
    Button returnButton;
    Button userManagementButton;
    Button adminDashboardButton;
    Button salesDataButton;
    VBox buttonHolder;
    JSONArray pendingBooks;
    JSONArray availableBooks;
    private Text title;
    private Text author;
    private Text publishedYear;
    private Text category;
    private Text condition;
    private Text price;
    private Text sellerID;


    private ListView<String> bookListView;
    private ObservableList<String> bookObsList;
    private Label pendingBooksLabel;
    private VBox bookInfoCard;
    public BookApprovalPage(Stage stage) {
        super(stage);
        setUpBookApprovalPage();
    }

    public void setUpBookApprovalPage() {
        initializePendingBooks();
        initializeAvailableBooks();
        displayPendingBooks();
        setUpDecisionButtons();
        initializeText();
        setUpRedirectButtons();

        adminMessage.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        adminMessage.setTextFill(Color.DARKBLUE);

        VBox contentLayout = new VBox();
        contentLayout.getChildren().addAll(adminMessage, pendingBooksLabel, bookListView);
        contentLayout.setAlignment(Pos.TOP_LEFT);
        setUpInfoCard();

        VBox infoCardLayout = new VBox();
        Label cardLabel = new Label("\n\n");
        infoCardLayout.getChildren().addAll(cardLabel, bookInfoCard, rejectButton, approveButton);
        infoCardLayout.setLayoutX(325);
        infoCardLayout.setLayoutY(55);
        infoCardLayout.setAlignment(Pos.TOP_RIGHT);

        HBox tempGroup = new HBox();
        tempGroup.setSpacing(60);
        tempGroup.getChildren().addAll(contentLayout, infoCardLayout, buttonHolder);

        bookListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                fillInInfoCard(newValue);
            }
        });

        //event handler for approveButton
        approveButton.setOnAction(actionEvent -> {
            String bookSelectedTitle = bookListView.getSelectionModel().getSelectedItem();
            if (bookSelectedTitle == null) {
                return;
            }
            JSONObject bookSelected = returnBook(bookSelectedTitle);
            if (bookSelected == null) {
                return;
            }
            availableBooks.put(bookSelected);
            saveBookToAvailable(availableBooks);

            // calls savePendingBooks
            int index = indexOfPendingBook(bookSelectedTitle);

            if (index != -1) { //ensure that the book was found
                pendingBooks.remove(index);
                savePendingBooks();
                showAlert("Success", "You have successfully approved this book");

                // update the UI and clear the information
                bookListView.getItems().remove(index);
                bookInfoCard.getChildren().clear();
            }
        });

        //event handler for rejectButton
        rejectButton.setOnAction(actionEvent -> {
            String bookSelectedTitle = bookListView.getSelectionModel().getSelectedItem();
            // remove the book from pendingBooks and update pendingBooks.json
            if (bookSelectedTitle == null) {
                return;
            }
            int index = indexOfPendingBook(bookSelectedTitle);

            if (index != -1) { //ensures that the book is found
                pendingBooks.remove(index);
                savePendingBooks();
                showAlert("Success", "You have successfully rejected this book");

                // update the ListView
                bookListView.getItems().remove(index);

                // clear the info card
                bookInfoCard.getChildren().clear();
            }
        });

        Scene scene = new Scene(tempGroup, 800, 600);
        stage.setScene(scene);

    }
    public void setUpRedirectButtons() {
        returnButton = new Button("Return");
        userManagementButton = new Button("View Users");
        salesDataButton = new Button("View Sales");
        adminDashboardButton = new Button("Admin Dashboard");

        returnButton.setMinWidth(125);
        userManagementButton.setMinWidth(125);
        salesDataButton.setMinWidth(125);
        adminDashboardButton.setMinWidth(125);

        returnButton.setOnAction(actionEvent -> {
            new LoginPage(stage).display();
        });
        salesDataButton.setOnAction(actionEvent -> {
            new SalesDataPage(stage).display();
        });
        userManagementButton.setOnAction(actionEvent -> {
            new UserManagementPage(stage).display();
        });
        adminDashboardButton.setOnAction(actionEvent -> {
            new AdminPage(stage).display();
        });

        buttonHolder = new VBox();
        buttonHolder.getChildren().setAll(returnButton, userManagementButton, salesDataButton, adminDashboardButton);
        buttonHolder.setAlignment(Pos.TOP_RIGHT);
    }
    public int indexOfPendingBook(String title) {
        for (int i = 0; i < pendingBooks.length(); ++i) {
            JSONObject book = pendingBooks.getJSONObject(i);
            if (title.equals(book.getString("title"))) {
                return i;
            }
        }
        return -1;
    }

    public JSONObject returnBook(String title) {
        for (int i = 0; i < pendingBooks.length(); ++i) {
            JSONObject book = pendingBooks.getJSONObject(i);
            if (title.equals(book.getString("title"))) {
                return book;
            }
        }
        return null;
    }


    public void saveBookToAvailable(JSONArray bookApproved) {
        try (FileWriter file = new FileWriter("src/application/books.json")) {
            file.write(bookApproved.toString(4));
            file.flush();
            System.out.println("Successfully updated books.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void fillInInfoCard(String title) {
        bookInfoCard.getChildren().clear(); // clear pre-existing info card just in case before showing new book info
        if(title == null) {
            bookInfoCard.getChildren().removeAll();
            return;
        }
        bookInfoCard.getChildren().removeAll();
        setDefaultText();
        JSONObject book = new JSONObject();
        for(int i = 0; i < pendingBooks.length(); ++i) {
            book = pendingBooks.getJSONObject(i);
            if(book.getString("title").equals(title)) {
                break;
            }
        }
        this.title.setText(title);
        author.setText(author.getText() + book.getString("author"));
        publishedYear.setText(publishedYear.getText() + book.getInt("published year"));
        category.setText(category.getText() + book.getString("category"));
        condition.setText(condition.getText() + book.getString("condition"));
        price.setText(price.getText() + book.getDouble("price"));

        bookInfoCard.getChildren().setAll(this.title, author, publishedYear, category, condition, price, sellerID);
    }
    public void setUpInfoCard() {
        bookInfoCard = new VBox(20);
        bookInfoCard.setPadding(new Insets(10));
        bookInfoCard.setMaxSize(350, 200);
        bookInfoCard.setMinSize(350, 200);
        bookInfoCard.setLayoutX(300);
        bookInfoCard.setLayoutY(40);
        bookInfoCard.setSpacing(3);
        bookInfoCard.setStyle(informationCardLayout);
    }
    public void initializeBookList() {
        for(int i = 0; i < pendingBooks.length(); ++i) {
            JSONObject book = pendingBooks.getJSONObject(i);
            bookObsList.add(book.getString("title"));
        }
        bookListView.setItems(bookObsList);
    }
    public void displayPendingBooks() {
        bookObsList = FXCollections.observableArrayList();
        bookListView = new ListView<>();
        bookListView.setMaxSize(200,300);
        pendingBooksLabel = new Label("Pending Books:");

        initializeBookList();
    }

    public void initializePendingBooks() {
        //CHANGE FILE PATHS AS NECESSARY
        try {
            BufferedReader pendingReader = new BufferedReader(new FileReader("src/application/pendingBooks.json"));
            StringBuilder jsonListedBuilder = new StringBuilder();
            String line;
            while ((line = pendingReader.readLine()) != null) {
                jsonListedBuilder.append(line);
            }
            pendingReader.close();
            pendingBooks = new JSONArray(jsonListedBuilder.toString());
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeAvailableBooks() {
        try {
            BufferedReader pendingReader = new BufferedReader(new FileReader("src/application/pendingBooks.json"));
            StringBuilder jsonListedBuilder = new StringBuilder();
            String line;
            while ((line = pendingReader.readLine()) != null) {
                jsonListedBuilder.append(line);
            }
            pendingReader.close();
            availableBooks = new JSONArray(jsonListedBuilder.toString());
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeText() {
        title = new Text();
        author = new Text();
        publishedYear = new Text();
        category = new Text();
        condition = new Text();
        price = new Text();
        sellerID = new Text();
    }

    public void setDefaultText() {
        title.setText("Title: ");
        author.setText("Author: ");
        publishedYear.setText("Published Year: ");
        category.setText("Category: ");
        condition.setText("Condition: ");
        price.setText("Price: ");
        sellerID.setText("Seller ID: ");
    }

    public void setUpDecisionButtons() {
        approveButton = new Button("Approve Book");
        rejectButton = new Button("Reject Book");

    }

    // after removing a book from pendingBooks, save the updated array back
    public void savePendingBooks() {
        try (FileWriter file = new FileWriter("src/application/pendingBooks.json")) {
            file.write(pendingBooks.toString(4));
            file.flush();
            System.out.println("Successfully updated pendingBooks.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        // Show an alert with a title and message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
