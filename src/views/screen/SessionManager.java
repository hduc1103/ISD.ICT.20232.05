package views.screen;

// This class is used to manage the login state of the user
public class SessionManager {
    // Variable used to store the login state of the user. false means not logged in and vice versa
    private static boolean isLoggedIn = false;

    // Store user-specific session data
    private static int userId = -1;

    // Method to check if the user is logged in
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Method to change the login state
    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    // Method to set the user ID
    public static void setUserId(int id) {
        userId = id;
    }

    // Method to get the user ID
    public static int getUserId() {
        return userId;
    }

    // Method to clear the session data
    public static void clearSession() {
        isLoggedIn = false;
        userId = -1;
    }
}
