package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RolePage {

    private VBox layout;

    public RolePage(String role, Runnable backAction) { // Accept a Runnable for the back action
        layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome to the " + role + " page!");
        Button backButton = new Button("Back to Login");

        // Handle the back button action
        backButton.setOnAction(e -> backAction.run()); // Call the back action when clicked

        // Add specific content for each role
        if (role.equals("Admin")) {
            layout.getChildren().addAll(welcomeLabel, new Label("Admin specific content"), backButton);
        } else if (role.equals("Buyer")) {
            layout.getChildren().addAll(welcomeLabel, new Label("Buyer specific content"), backButton);
        } else if (role.equals("Seller")) {
            layout.getChildren().addAll(welcomeLabel, new Label("Seller specific content"), backButton);
        }
    }

    // Provide the layout to be used by other classes
    public VBox getLayout() {
        return layout;
    }
}
