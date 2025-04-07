package application;

import javafx.scene.Scene;  
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Page {
    protected Text applicationTitle;
    protected ImageView applicationLogo;
    protected Text welcomeMessage;
    protected ComboBox<String> redirectMenu;
    protected Stage stage;

    public Page(Stage stage) {
        this.stage = stage;
        setupPage();
    }

    public void setupPage() {
        // Title
        applicationTitle = new Text("ASU BOOKS");

        // Set up application logo
        /**
        Image logoImage = new Image(getClass().getResourceAsStream("/asubooks logo.png"));
        applicationLogo = new ImageView(logoImage);
        applicationLogo.setFitHeight(100);
        applicationLogo.setFitWidth(100);
        **/

        // Set up personal welcome message
        welcomeMessage = new Text("Welcome to the SunDevil System!");

        // Set up redirection menu
        redirectMenu = new ComboBox<>();
        redirectMenu.getItems().addAll("Login", "Signup", "Browse Books", "List a Book", "Admin Page");
        redirectMenu.setOnAction(e -> redirectPage(redirectMenu.getValue()));

        // Add all components to layout (e.g., VBox)
        VBox layout = new VBox(10);
        layout.getChildren().addAll(applicationTitle, welcomeMessage, redirectMenu);
    }

    // Method to redirect to a new page
    public void redirectPage(String page) {
        if (page.equals("Login")) {
            new LoginPage(stage).display();
        } else if (page.equals("Signup")) {
            new CreateAccountPage(stage).display();
        }
        // Handle other redirections...
    }

    // Redirect to login page 
    public void logOut() {
        new LoginPage(stage).display();
    }

    public void display() {
        stage.show();
    }
}
