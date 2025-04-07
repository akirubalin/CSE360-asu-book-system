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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginPage extends Page {
    private VBox layout;
    private ToggleGroup roleGroup;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button signUpButton;
    private Button forgotPasswordButton;

    public LoginPage(Stage stage) {
        super(stage);
        setupLoginPage();
    }

    public void setupLoginPage() {
        layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        
        File file = new File("Image/asubooks logo.png");
        if (file.exists()) {
            System.out.println("Logo found: " + file.getAbsolutePath());
        } else {
            System.out.println("Logo not found!");
        }

        //logo
        Image logoImage = new Image("File:Image/asubooks logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(100);
        logoView.setPreserveRatio(true);
		
        Label appName = new Label("ASU BOOKS");
        appName.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #8B0000;");

        //role buttons
        roleGroup = new ToggleGroup();
        RadioButton adminButton = new RadioButton("Admin");
        RadioButton buyerButton = new RadioButton("Buyer");
        RadioButton sellerButton = new RadioButton("Seller");

        adminButton.setToggleGroup(roleGroup);
        buyerButton.setToggleGroup(roleGroup);
        sellerButton.setToggleGroup(roleGroup);
        adminButton.setSelected(true);

        // Highlight selected user box in yellow
        roleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            adminButton.setStyle("-fx-background-color: transparent;");
            buyerButton.setStyle("-fx-background-color: transparent;");
            sellerButton.setStyle("-fx-background-color: transparent;");

            if (newValue == adminButton) {
                adminButton.setStyle("-fx-background-color: yellow; -fx-padding: 5 15 5 15;");
            } else if (newValue == buyerButton) {
                buyerButton.setStyle("-fx-background-color: yellow; -fx-padding: 5 15 5 15;");
            } else if (newValue == sellerButton) {
                sellerButton.setStyle("-fx-background-color: yellow; -fx-padding: 5 15 5 15;");
            }
        });

        HBox roleButtons = new HBox(10, adminButton, buyerButton, sellerButton);
        roleButtons.setAlignment(Pos.CENTER);

        VBox memberBox = new VBox(10);
        memberBox.setAlignment(Pos.CENTER);
        memberBox.setPadding(new Insets(15));
        memberBox.setStyle("-fx-border-color: gray; -fx-border-width: 2; -fx-border-radius: 10; -fx-padding: 10;");

        Label alreadyMembersLabel = new Label("Already members");
        alreadyMembersLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        usernameField = new TextField();
        usernameField.setPromptText("Username or email");
        usernameField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

        //login button
        loginButton = new Button("SIGN IN");
        loginButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setOnAction(e -> loginUser());

        memberBox.getChildren().addAll(alreadyMembersLabel, usernameField, passwordField, loginButton);

        signUpButton = new Button("Create an account");
        signUpButton.setStyle("-fx-font-size: 12px; -fx-text-fill: #8B0000;");
        signUpButton.setOnAction(e -> redirectToSignup());

        forgotPasswordButton = new Button("Forgot password");
        forgotPasswordButton.setStyle("-fx-font-size: 12px; -fx-text-fill: #8B0000;");
        forgotPasswordButton.setOnAction(e -> redirectToForgotPassword());

        HBox accountButtons = new HBox(10, signUpButton, forgotPasswordButton);
        accountButtons.setAlignment(Pos.CENTER);

        //all the layout components together
        layout.getChildren().addAll(
                logoView, 
                appName,
                roleButtons,
                memberBox,
                accountButtons
        );

        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
    }

    public void loginUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String selectedRole = ((RadioButton) roleGroup.getSelectedToggle()).getText();

        if (validateLogin(username, password, selectedRole)) {
            redirectPageBasedOnRole(selectedRole);
        } else {
            showAlert("Error", "Invalid username or password. Please try again.");
        }
    }

    private boolean validateLogin(String username, String password, String selectedRole) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/application/users.json"))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            JSONArray usersArray = new JSONArray(jsonBuilder.toString());
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("username").equals(username) &&
                        user.getString("password").equals(password) &&
                        user.getString("role").equalsIgnoreCase(selectedRole)) {
                    return true;
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Could not read user data.");
            e.printStackTrace();
        }
        return false;
    }

    public void redirectToSignup() {
        CreateAccountPage createAccount = new CreateAccountPage(stage);
        createAccount.display();
    }

    public void redirectToForgotPassword() {
        ForgetPasswordPage forgetPasswordPage = new ForgetPasswordPage(stage);
        forgetPasswordPage.display();
    }

    private void redirectPageBasedOnRole(String role) {
        switch (role.toLowerCase()) {
            case "buyer":
                BuyerPage buyerPage = new BuyerPage(stage);
                buyerPage.display();
                break;
            case "seller":
                SellerPage sellerPage = new SellerPage(stage);
                sellerPage.display();
                break;
            case "admin":
                AdminPage adminPage = new AdminPage(stage);
                adminPage.display();
                break;
            default:
                showAlert("Error", "Unknown role: " + role);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
