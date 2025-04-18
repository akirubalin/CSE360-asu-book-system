package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        loginPage.display();  // Display the login page
    }

    public static void main(String[] args) {
        launch(args);
    }
}

