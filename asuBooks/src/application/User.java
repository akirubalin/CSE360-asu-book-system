package application;

public class User {
    // Private attributes
    private String username;
    private String password;

    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Placeholder for switching account functionality
    public boolean switchAccount() {
        // Implement logic for switching accounts if needed
        System.out.println("Switching account for: " + username);
        return true;
    }

    // Placeholder for logging out
    public boolean logOut() {
        System.out.println("Logging out: " + username);
        return true;
    }

    // Retrieve account information
    public String getAccountInfo() {
        return "Username: " + username + "\nPassword: " + password;
    }

    // Placeholder for logging in
    public boolean logIn() {
        System.out.println("Logging in: " + username);
        return true;
    }
}

