package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CreateAccountPage extends Page {
    private ComboBox<String> roleComboBox;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField asuIdField;

    public CreateAccountPage(Stage stage) {
        super(stage);
        display();
    }

    @Override
    public void display() {
        BorderPane layout = new BorderPane();

        // Return to login on the top right
        Button returnHomeButton = new Button("Return to Login");
        returnHomeButton.setOnAction(event -> redirectToLoginPage());
        HBox topRightBox = new HBox(returnHomeButton);
        topRightBox.setAlignment(Pos.TOP_RIGHT);
        layout.setTop(topRightBox);

        // layout
        VBox formLayout = new VBox(10);
        Label roleLabel = new Label("Select Role:");
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Buyer", "Seller", "Admin");
        roleComboBox.setPromptText("Select Role");

        Label usernameLabel = new Label("Enter Username:");
        usernameField = new TextField();
        Label passwordLabel = new Label("Enter Password:");
        passwordField = new PasswordField();
        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordField = new PasswordField();
        Label asuIdLabel = new Label("Enter Last 4 Digits of ASU ID:");
        asuIdField = new TextField();
        asuIdField.setPromptText("e.g., 1234");

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(event -> createAccount());
        formLayout.getChildren().addAll(
            roleLabel, roleComboBox, 
            usernameLabel, usernameField, 
            passwordLabel, passwordField, 
            confirmPasswordLabel, confirmPasswordField, 
            asuIdLabel, asuIdField, 
            createAccountButton
        );

        layout.setCenter(formLayout);

        // Set scene
        Scene scene = new Scene(layout, 400, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void createAccount() {
        String role = roleComboBox.getValue();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String asuId = asuIdField.getText();

        if (role == null || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || asuId.isEmpty()) {
            showAlert("Error", "Please fill out all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match. Please try again.");
            return;
        }

        if (!asuId.matches("\\d{4}")) {
            showAlert("Error", "ASU ID must be exactly 4 digits.");
            return;
        }

        JSONArray usersArray = loadUsers();

        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject existingUser = usersArray.getJSONObject(i);
            if (existingUser.getString("username").equals(username) && existingUser.getString("role").equals(role)) {
                showAlert("Error", "An account with this username and role already exists.");
                return;
            }
        }

        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("role", role);
        newUser.put("asuId", asuId);

        usersArray.put(newUser);
        saveUsers(usersArray);

        showConfirmationAlert();
    }

    private JSONArray loadUsers() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/application/users.json")));
            return new JSONArray(content);
        } catch (NoSuchFileException e) {
            System.out.println("File not found. A new users array will be created.");
            return new JSONArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private void saveUsers(JSONArray usersArray) {
        try (FileWriter file = new FileWriter("src/application/users.json")) {
            file.write(usersArray.toString(4));
            file.flush();
            System.out.println("Successfully updated users.json");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not save account. Please try again.");
        }
    }

    private void showConfirmationAlert() {
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle("Account Created");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Account created successfully!");

        ButtonType returnToLoginButton = new ButtonType("Return to Login");
        confirmationAlert.getButtonTypes().setAll(returnToLoginButton);

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == returnToLoginButton) {
                redirectToLoginPage();
            }
        });
    }

    private void redirectToLoginPage() {
        LoginPage loginPage = new LoginPage(stage);
        loginPage.setupLoginPage();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
