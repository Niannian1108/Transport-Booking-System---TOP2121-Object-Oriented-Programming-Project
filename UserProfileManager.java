import java.io.*;
import java.util.*;

public class UserProfileManager extends UserManager {
    private String currentUsername;
    private String userFileName;

    public UserProfileManager(String username) {
        this.currentUsername = username;
        this.userFileName = getUserFileName();  //get filename frm user manager
    }

    public String changeUsername(String newUsername) {
        if (userExists(newUsername)) {
            return "Username already exists!";  //check if already exists
        }
        
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            List<String> userLines = new ArrayList<String>();   //store user data temp
            boolean userFound = false;
    
            reader = new BufferedReader(new FileReader(userFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(currentUsername)) {
                    userFound = true;
                    //replace username with new username
                    StringBuilder newLine = new StringBuilder(newUsername);
                    for (int i = 1; i < userDetails.length; i++) {
                        newLine.append(",").append(userDetails[i]);
                    }
                    userLines.add(newLine.toString());
                } else {
                    userLines.add(line);    //make sure other user data not changed
                }
            }
    
            if (!userFound) {
                return "User not found!";
            }
    
            //write updated information back into the file
            writer = new BufferedWriter(new FileWriter(userFileName));
            for (String userLine : userLines) {
                writer.write(userLine);
                writer.newLine();
            }
            currentUsername = newUsername; //update current username in display window
            return "Username changed successfully!";
        } catch (IOException e) {
            return "Error processing user data: " + e.getMessage();
        } finally {
            closeQuietly(reader);
            closeQuietly(writer);
        }
    }

    public String changePassword(String oldPassword, String newPassword) {
        if (oldPassword.equals(newPassword)) {
            return "Password should not be same as the old one. Try again.";
        }   //make sure new pw is different
        
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            List<String> userLines = new ArrayList<String>();   //store user data temp
            boolean userFound = false;
            boolean passwordChanged = false;
    
            reader = new BufferedReader(new FileReader(userFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(currentUsername)) {
                    userFound = true;
                    if (userDetails[1].equals(oldPassword)) {
                        StringBuilder newLine = new StringBuilder(userDetails[0] + "," + newPassword);
                        for (int i = 2; i < userDetails.length; i++) {
                            newLine.append(",").append(userDetails[i]);
                        }
                        userLines.add(newLine.toString());
                        passwordChanged = true;
                    } else {
                        userLines.add(line);    //keep line unchanged if pw is incorrect
                        return "Wrong password, please try again.";
                    }
                } else {
                    userLines.add(line);    
                }
            }
    
            if (!userFound) {
                return "User not found!";
            }
    
            if (passwordChanged) {
                //write updated user data back to the file
                writer = new BufferedWriter(new FileWriter(userFileName));
                for (String userLine : userLines) {
                    writer.write(userLine);
                    writer.newLine();
                }
                return "Password changed successfully!";
            }
        } catch (IOException e) {
            return "Error processing user data: " + e.getMessage();
            //file I/o errors
        } finally {
            closeQuietly(reader);
            closeQuietly(writer);
        }
        return "An error occurred while changing password.";
    }


    public String addPhoneNumber(String phoneNumber) {
        if (!isValidPhoneNumber(phoneNumber)) { //validates phone number
            return "Invalid phone number format! Please enter up to 11 digits only.";
        }
        
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            List<String> userLines = new ArrayList<String>();
            boolean userFound = false;
    
            reader = new BufferedReader(new FileReader(userFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(currentUsername)) {
                    userFound = true;
                    //check if phone number already exists
                    for (int i = 2; i < userDetails.length; i++) {
                        if (userDetails[i].equals(phoneNumber)) {
                            return "Phone number already exists!";
                        }
                    }
                    //add the new phone number
                    StringBuilder newLine = new StringBuilder();
                    newLine.append(userDetails[0]).append(",").append(userDetails[1]); // Username and password
                    
                    //add existing phone numbers
                    for (int i = 2; i < userDetails.length; i++) {
                        newLine.append(",").append(userDetails[i]);
                    }
                    
                    //add the new phone number
                    newLine.append(",").append(phoneNumber);
                    userLines.add(newLine.toString());
                } else {
                    userLines.add(line);
                }
            }
    
            if (!userFound) {
                return "User not found!";
            }
    
            writer = new BufferedWriter(new FileWriter(userFileName));
            for (String userLine : userLines) {
                writer.write(userLine);
                writer.newLine();
            }
            return "Phone number added successfully!";
        } catch (IOException e) {
            return "Error processing user data: " + e.getMessage();
        } finally {
            closeQuietly(reader);
            closeQuietly(writer);
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // validates if phone num is only digits
        if (!phoneNumber.matches("\\d+")) {
            return false;
        }
        
        //check length <=11
        if (phoneNumber.length() > 11) {
            return false;
        }
        
        return true;
    }

public String deletePhoneNumber(String phoneNumberToDelete) {
    BufferedReader reader = null;
    BufferedWriter writer = null;
    try {
        List<String> userLines = new ArrayList<String>();
        boolean userFound = false;

        reader = new BufferedReader(new FileReader(userFileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");
            if (userDetails[0].equals(currentUsername)) {
                userFound = true;
                StringBuilder newLine = new StringBuilder(userDetails[0] + "," + userDetails[1]);
                for (int i = 2; i < userDetails.length; i++) {
                    if (!userDetails[i].equals(phoneNumberToDelete)) {
                        newLine.append(",").append(userDetails[i]);
                    }
                }
                userLines.add(newLine.toString());
            } else {
                userLines.add(line);
            }
        }

        if (!userFound) {
            return "User not found!";
        }

        writer = new BufferedWriter(new FileWriter(userFileName));
        for (String userLine : userLines) {
            writer.write(userLine);
            writer.newLine();
        }
        return "Phone number deleted successfully!";
    } catch (IOException e) {
        return "Error processing user data: " + e.getMessage();
    } finally {
        closeQuietly(reader);
        closeQuietly(writer);
    }
}

    public String getCurrentUsername() {
        return currentUsername;
    }

public String[] getPhoneNumbers() {
    BufferedReader reader = null;
    try {
        reader = new BufferedReader(new FileReader(userFileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userDetails = line.split(",");
            if (userDetails[0].equals(currentUsername)) {
                if (userDetails.length <= 2) {
                    return new String[0]; 
                }
                String[] phoneNumbers = new String[userDetails.length - 2];
                System.arraycopy(userDetails, 2, phoneNumbers, 0, phoneNumbers.length);
                return phoneNumbers;
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading user data: " + e.getMessage());
    } finally {
        closeQuietly(reader);
    }
    return new String[0];
}    

    private void closeQuietly(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
            }
        }
    }
}