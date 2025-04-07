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

import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class ForgetPasswordPage extends Page {
    private TextField usernameField;
    private TextField asuIdField;

    public ForgetPasswordPage(Stage stage) {
        super(stage);
        display();
    }

    @Override
    public void display() {
        BorderPane layout = new BorderPane();

        // Top right "Return to Home Page" button
        Button returnHomeButton = new Button("Return to Home Page");
        returnHomeButton.setOnAction(event -> redirectToLoginPage());
        HBox topRightBox = new HBox(returnHomeButton);
        topRightBox.setAlignment(Pos.TOP_RIGHT);
        layout.setTop(topRightBox);

        // Main form layout (center)
        VBox formLayout = new VBox(10);
        Label usernameLabel = new Label("Enter Username:");
        usernameField = new TextField();
        Label asuIdLabel = new Label("Enter Last 4 Digits of ASU ID:");
        asuIdField = new TextField();

        Button retrievePasswordButton = new Button("Retrieve Password");
        retrievePasswordButton.setOnAction(event -> retrievePassword());
        formLayout.getChildren().addAll(usernameLabel, usernameField, asuIdLabel, asuIdField, retrievePasswordButton);

        layout.setCenter(formLayout);

        // Set scene
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void retrievePassword() {
        String username = usernameField.getText();
        String asuId = asuIdField.getText();

        if (username.isEmpty() || asuId.isEmpty()) {
            showAlert("Error", "Please fill out all fields.");
            return;
        }

        JSONArray usersArray = loadUsers();
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("username").equals(username) && user.getString("asuId").equals(asuId)) {
                String password = user.getString("password");
                showConfirmationAlert(password);
                return;
            }
        }

        // If no match found, show an error
        showAlert("Error", "Username or ASU ID does not match record.");
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

    private void showConfirmationAlert(String password) {
        Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
        confirmationAlert.setTitle("Password Retrieved");
        confirmationAlert.setHeaderText("Your password is: " + password);

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
