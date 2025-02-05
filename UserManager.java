import java.io.*;

public class UserManager {
    private static final String FILE_NAME = "users.txt";
    private String lastErrorMessage = "";

    public UserManager() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error initializing user file: " + e.getMessage());
        }
    }

    public void registerUser(String username, String password) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Error closing writer: " + e.getMessage());
                }
            }
        }
    }

    public boolean loginUser(String username, String password) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            boolean userFound = false;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(username)) {
                    userFound = true;
                    if (userDetails[1].equals(password)) {
                        return true;
                    }
                }
            }
            
            // Set appropriate error message
            if (!userFound) {
                lastErrorMessage = "User not found, please register.";
            } else {
                lastErrorMessage = "Username/password incorrect, please try again.";
            }
        } catch (IOException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing reader: " + e.getMessage());
                }
            }
        }
        return false;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public boolean userExists(String username) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user data: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error closing reader: " + e.getMessage());
                }
            }
        }
        return false;
    }
}
