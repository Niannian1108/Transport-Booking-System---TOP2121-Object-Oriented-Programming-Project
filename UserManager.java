import java.io.*;

public class UserManager {
    private static final String FILE_NAME = "users.txt";
    //give file name
    private String lastErrorMessage = "";

    public UserManager() {
        //check if user exists, create unless
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) { //exception handling
            System.out.println("Error initializing user file: " + e.getMessage());
        }
    }

    public void registerUser(String username, String password) {    //register new user
        BufferedWriter writer = null;
        try {
            //open and write new user information
            writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            //write username and pw with comma as divider
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {   //error
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

    //check user during login
    public boolean loginUser(String username, String password) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            boolean userFound = false;
            //read lines in the file and split into username and pw and check if matches
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(username)) {
                    userFound = true;
                    if (userDetails[1].equals(password)) {
                        return true;    //login successful
                    }
                }
            }
            
            //error message
            if (!userFound) {
                lastErrorMessage = "User not found, please register.";
            } else {
                lastErrorMessage = "Password incorrect, please try again.";
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
        return false;   //login fail
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
        return false;   //user doesn't exists
    }

    protected String getUserFileName() {
        return FILE_NAME; 
    }
}