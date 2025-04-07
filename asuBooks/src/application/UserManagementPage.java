package application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.Group;
import javafx.scene.control.Button;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.scene.text.Text;

public class UserManagementPage extends Page{
    public static final Label adminMessage = new Label("Welcome, Admin! Here you can manage the users of the application");
    public static final String[] letters = {"a/A", "b/B", "c/C", "d/D", "e/E", "f/F", "g/G", "h/H", "i/I", "j/J", "k/K", "l/L", "m/M", "n/N", "o/O", "p/P", "q/Q", "r/R", "s/S", "t/T", "u/U", "v/V", "x/X", "y/Y", "z/Z"};

    public static final String informationCardLayout = "-fx-border-color: black;\n" +
            "-fx-border-insets: 3;\n" +
            "-fx-border-width: 3;\n";
    Button returnButton;
    Button salesStatisticsButton;
    Button adminDashboardButton;
    Button bookApprovalButton;


    private ComboBox<String> selectUserMenu;
    private ListView<String> sellersListView;
    private ObservableList<String> sellers;
    private ListView<String> buyersListView;
    private ObservableList<String> buyers;

    private Label buyerListLabel;
    private Label sellerListLabel;
    private VBox userInformationCard;
    private Text infoCardUsername;
    private Text infoCardPassword;
    private Text infoCardASUID;
    private Text infoCardRole;
    private JSONArray allUsers;
    public UserManagementPage(Stage stage) {
        super(stage);
        setupUserManagementPage();
    }

    public void setupUserManagementPage() {
        initializeUsers();
        setUpInformationCard();

        displayRedirectButtons();

        VBox buttonHolder = new VBox();
        buttonHolder.getChildren().addAll(returnButton, salesStatisticsButton, adminDashboardButton, bookApprovalButton);
        buttonHolder.setAlignment(Pos.TOP_RIGHT);
        buttonHolder.setPadding(new Insets(10));
        displayRedirectButtons();

        adminMessage.setWrapText(true);
        adminMessage.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        adminMessage.setTextFill(Color.DARKBLUE);
        adminMessage.setLayoutX(300);

        displayUserLists();

        VBox contentLayout = new VBox(20);
        contentLayout.setPadding(new Insets(20));
        contentLayout.setSpacing(10);
        contentLayout.setAlignment(Pos.CENTER);
        contentLayout.getChildren().addAll(adminMessage, sellerListLabel, sellersListView, buyerListLabel, buyersListView);

        setUpInformationCard();
        Group tempGroup = new Group();
        tempGroup.getChildren().addAll(contentLayout, userInformationCard);

        BorderPane layout = new BorderPane();
        layout.setLeft(tempGroup);
        layout.setTop(buttonHolder);
        //layout.setRight(userInformationCard);


        displaySelectUserMenu();

        VBox filters = new VBox(10);
        filters.getChildren().addAll(selectUserMenu);
        layout.getChildren().addAll(filters);

        selectUserMenu.setOnAction((event) -> {
            displaySelectedUsers();
        });


        sellersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                fillInInformationCard(newValue);

            }
        });

        buyersListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Your action here
                fillInInformationCard(newValue);

            }
        });

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
    }

    public void displayUserLists() {
        sellerListLabel = new Label("Sellers:");
        sellers = FXCollections.observableArrayList();
        sellersListView = new ListView<>();
        sellersListView.setMaxSize(200,160);

        buyerListLabel = new Label("Buyers:");
        buyers = FXCollections.observableArrayList();
        buyersListView = new ListView<>();
        buyersListView.setMaxSize(200, 160);
    }

    private void displayRedirectButtons() {
        returnButton = new Button("Return");
        salesStatisticsButton = new Button("Sales statistics");
        adminDashboardButton = new Button("Admin Dashboard");
        bookApprovalButton = new Button("Approve Books");

        returnButton.setOnAction(e -> new LoginPage(stage).display());
        adminDashboardButton.setOnAction(e -> new AdminPage(stage).display());
        salesStatisticsButton.setOnAction(e-> new SalesDataPage(stage).display());
        bookApprovalButton.setOnAction(e->new BookApprovalPage(stage).display());

        returnButton.setMinWidth(125);
        adminDashboardButton.setMinWidth(125);
        salesStatisticsButton.setMinWidth(125);
        bookApprovalButton.setMinWidth(125);

    }

    public void initializeUsers() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader( "src/application/users.json"));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            allUsers = new JSONArray(jsonBuilder.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displaySelectUserMenu() {
        selectUserMenu = new ComboBox<>();
        selectUserMenu.getItems().addAll(letters);
        selectUserMenu.setPromptText("Select Letter");
        selectUserMenu.setMinSize(150, 25);
        selectUserMenu.setMaxSize(150, 25);
        selectUserMenu.setLayoutY(210);
    }

    public void selectUser(String chosenLetter) {
        char lowerCaseLetter = chosenLetter.charAt(0);
        char upperCaseLetter = chosenLetter.charAt(2);

        sellers.clear();
        buyers.clear();


        for (int i = 0; i < allUsers.length(); ++i) {
            JSONObject user = allUsers.getJSONObject(i);
            char firstLetter = user.getString("username").charAt(0);

            if (user.getString("role").equals("Seller")) {
                if (firstLetter == upperCaseLetter || firstLetter == lowerCaseLetter) {
                    sellers.add(user.getString("username"));
                }
            } else if (user.getString("role").equals("Buyer")) {
                if (firstLetter == upperCaseLetter || firstLetter == lowerCaseLetter) {
                    buyers.add(user.getString("username"));
                }
            }
        }
    }

    public void setUpInformationCard() {
        userInformationCard = new VBox(20);
        userInformationCard.setPadding(new Insets(10));
        userInformationCard.setMaxSize(250, 200);
        userInformationCard.setMinSize(250, 200);
        userInformationCard.setSpacing(3);
        userInformationCard.setLayoutY(95);
        userInformationCard.setLayoutX(450);
        userInformationCard.setStyle(informationCardLayout);
    }

    public void initializeText() {
        infoCardUsername = new Text("Username: ");
        infoCardPassword = new Text("Password: ");
        infoCardASUID = new Text("ASU ID: ");
        infoCardRole = new Text("Role: ");
    }

    public void fillInInformationCard(String username) {
        if(username == null) {
            userInformationCard.getChildren().removeAll();
            return;
        }
        userInformationCard.getChildren().removeAll();

        initializeText();

        JSONObject user = new JSONObject();
        for(int i = 0; i < allUsers.length(); ++i) {
            user = allUsers.getJSONObject(i);
            if(user.getString("username").equals(username)) {
                break;
            }
        }

        infoCardUsername.setText(infoCardUsername.getText() + user.getString("username"));
        infoCardPassword.setText(infoCardPassword.getText() + user.getString("password"));
        infoCardRole.setText(infoCardRole.getText() + user.getString("role"));
        infoCardASUID.setText(infoCardASUID.getText() + user.getInt("asuId"));

        userInformationCard.getChildren().setAll(infoCardUsername, infoCardPassword, infoCardRole, infoCardASUID);

    }

    public void displaySelectedUsers() {
        String letterSelected = selectUserMenu.getValue();
        selectUser(letterSelected);
        sellersListView.setItems(sellers);
        buyersListView.setItems(buyers);
    }

}
