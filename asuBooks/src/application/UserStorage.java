package application;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class UserStorage {
    private static final String FILE_NAME = "users.json";

    // Save a new user to the JSON file
    public static void saveUser(User user) {
        JSONArray users = readUsers();

        // Create a new JSON object for the user
        JSONObject newUser = new JSONObject();
        newUser.put("username", user.getUsername());
        newUser.put("password", user.getPassword()); // Store hashed password

        users.put(newUser);

        // Write back to the file
        try (FileWriter fileWriter = new FileWriter(FILE_NAME)) {
            fileWriter.write(users.toString(2)); // Pretty print with 2-space indentation
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read all users from the JSON file
    public static JSONArray readUsers() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new JSONArray(); // Return empty array if file does not exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            return new JSONArray(jsonBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray(); // Return empty array on error
        }
    }

    // Check if user exists and validate password
    public static boolean validateUser(String username, String password) {
        JSONArray users = readUsers();

        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("username").equals(username) && user.getString("password").equals(password)) {
                return true; // User found and password matches
            }
        }
        return false; // User not found or password does not match
    }
}
