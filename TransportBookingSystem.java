import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

public class TransportBookingSystem extends Applet implements ActionListener { 
    //instance variables
    
    private CardLayout cardLayout;
    private Panel mainPanel;
    private UserManager userManager;

    //Login
    private TextField usernameField, passwordField;
    private Button loginButton, registerButton, profileButton;
    private Label loginErrorLabel;
    private final Color MAIN_BLUE = new Color(51, 122, 183);
    private final Color DARK_BLUE = new Color(40, 96, 144);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    //booking
    private Choice transportChoice, destinationChoice, paymentMethodChoice;
    private TextField dateField, timeField, paymentField;
    private TextArea bookingsArea;
    private ArrayList<Booking> bookings;
    private HashMap<String, Double> prices;

    //user
    private Label usernameDisplayLabel;
    private Label phoneNumberLabel;
    private Label statusLabel;
    private String currentLoggedInUser;
    private boolean hasChangedUsername = false;
    
    public void init() { //init is initialise applet, 
        //sets up user manager
        //load booking
        /prices
        userManager = new UserManager();
        bookings = new ArrayList<Booking>();
        prices = new HashMap<String, Double>();
        initializePrices();
        loadBookings();

        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);

        setupLoginPanel();
        setupBookingPanel();
        setupConfirmationPanel();
        setupViewBookingsPanel();
        setupUserProfilePanel();

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "Login");
    }

    private void initializePrices() {
        prices.put("Johor Baharu", 55.0);
        prices.put("Kuala Lumpur", 60.0);
        prices.put("Penang", 50.0);
        prices.put("Melaka", 45.0);
        prices.put("Kluang", 40.0);
    }

    private void setupLoginPanel() {    //sets up login panel with username and pw fields buttons and labels
        Panel loginPanel = new Panel(new BorderLayout(0, 20));
        loginPanel.setBackground(BACKGROUND_COLOR);

        Panel titlePanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(MAIN_BLUE);
        Label titleLabel = new Label("Transport Booking System", Label.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        Panel inputPanel = new Panel(new GridLayout(4, 1, 0, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);

        Panel usernamePanel = new Panel(new BorderLayout(5, 0));
        usernamePanel.add(new Label("Username:"), BorderLayout.WEST);
        usernameField = new TextField(20);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        Panel passwordPanel = new Panel(new BorderLayout(5, 0));
        passwordPanel.add(new Label("Password:"), BorderLayout.WEST);
        passwordField = new TextField(20);
        passwordField.setEchoChar('*');
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new CustomButton("Login", MAIN_BLUE);
        registerButton = new CustomButton("Register", DARK_BLUE);
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        loginErrorLabel = new Label("", Label.CENTER);
        loginErrorLabel.setForeground(Color.RED);

        inputPanel.add(usernamePanel);
        inputPanel.add(passwordPanel);
        inputPanel.add(buttonPanel);
        inputPanel.add(loginErrorLabel);

        loginPanel.add(titlePanel, BorderLayout.NORTH);
        loginPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(loginPanel, "Login");
    }

    private void setupBookingPanel() {  //stes up booking panel with transport mode and detaisl + view booking + confirmation
        Panel bookingPanel = new Panel(new GridLayout(7, 2, 5, 5));

        transportChoice = new Choice();
        transportChoice.add("Bus");
        transportChoice.add("Train");
        transportChoice.add("Ferry");

        destinationChoice = new Choice();
        for (String dest : prices.keySet()) destinationChoice.add(dest);

        dateField = new TextField("YYYY-MM-DD");
        timeField = new TextField("HH:MM");
        Button bookingButton = new CustomButton("Proceed", MAIN_BLUE);
        Button viewBookingsButton = new CustomButton("View Bookings", DARK_BLUE);
        profileButton = new CustomButton("User Profile", MAIN_BLUE);

        bookingButton.addActionListener(this);
        viewBookingsButton.addActionListener(this);
        profileButton.addActionListener(this);

        Label transportLabel = new Label("Transport Mode:", Label.CENTER);
        transportLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        Label destinationLabel = new Label("Destination:", Label.CENTER);
        destinationLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        Label dateLabel = new Label("Date:", Label.CENTER);
        dateLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        Label timeLabel = new Label("Time:", Label.CENTER);
        timeLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));

        bookingPanel.add(transportLabel);
        bookingPanel.add(transportChoice);
        bookingPanel.add(destinationLabel);
        bookingPanel.add(destinationChoice);
        bookingPanel.add(dateLabel);
        bookingPanel.add(dateField);
        bookingPanel.add(timeLabel);
        bookingPanel.add(timeField);
        bookingPanel.add(bookingButton);
        bookingPanel.add(viewBookingsButton);
        bookingPanel.add(profileButton);

        mainPanel.add(bookingPanel, "Booking");
    }

    private void setupConfirmationPanel() { //sets up confirmation panel
        Panel confirmationPanel = new Panel(new GridLayout(6, 1, 5, 5));
        confirmationPanel.setName("Confirmation");

        Label confirmationLabel = new Label("Booking Details:", Label.CENTER);
        confirmationLabel.setFont(new Font("Arial", Font.BOLD, 12));

        TextArea confirmationText = new TextArea(5, 40);
        confirmationText.setEditable(false);
        confirmationText.setFont(new Font("Monospaced", Font.PLAIN, 12));

        //payment method choice
        Panel paymentPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        Label paymentMethodLabel = new Label("Payment Method:");
        paymentMethodChoice = new Choice();
        paymentMethodChoice.add("Debit/Credit Card");
        paymentMethodChoice.add("Bank Transfer");
        paymentPanel.add(paymentMethodLabel);
        paymentPanel.add(paymentMethodChoice);

        paymentField = new TextField("Enter payment details");
        Button confirmBookingButton = new CustomButton("Confirm and Pay", MAIN_BLUE);
        Button cancelButton = new CustomButton("Cancel", DARK_BLUE);

        confirmBookingButton.addActionListener(this);
        cancelButton.addActionListener(this);

        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(confirmationText);
        confirmationPanel.add(paymentPanel);
        confirmationPanel.add(paymentField);
        confirmationPanel.add(confirmBookingButton);
        confirmationPanel.add(cancelButton);

        mainPanel.add(confirmationPanel, "Confirmation");
    }

    private void setupViewBookingsPanel() { //display booking 
        Panel viewBookingsPanel = new Panel(new BorderLayout(5, 5));

        Label headerLabel = new Label("Your Bookings:", Label.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 12));

        bookingsArea = new TextArea(10, 40);
        bookingsArea.setEditable(false);
        bookingsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        Button backButton = new CustomButton("Back to Booking", MAIN_BLUE);
        backButton.addActionListener(this);

        viewBookingsPanel.add(headerLabel, BorderLayout.NORTH);
        viewBookingsPanel.add(bookingsArea, BorderLayout.CENTER);
        viewBookingsPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(viewBookingsPanel, "ViewBookings");
    }

    private void setupUserProfilePanel() {  //user profile to change username pw and phone num
        Panel userProfilePanel = new Panel(new GridLayout(8, 1, 5, 5));
        userProfilePanel.setBackground(BACKGROUND_COLOR);
    
        usernameDisplayLabel = new Label("", Label.CENTER);
        usernameDisplayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        phoneNumberLabel = new Label("Phone Numbers: None", Label.CENTER);
        phoneNumberLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        statusLabel = new Label("", Label.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(0, 100, 0));
    
        Button changeUsernameButton = new CustomButton("Change Username", MAIN_BLUE);
        Button changePasswordButton = new CustomButton("Change Password", MAIN_BLUE);
        Button addPhoneNumberButton = new CustomButton("Add Phone Number", MAIN_BLUE);
        Button deletePhoneNumberButton = new CustomButton("Delete Phone Number", MAIN_BLUE);
        Button backButton = new CustomButton("Back to Booking", DARK_BLUE);
    
        changeUsernameButton.addActionListener(this);
        changePasswordButton.addActionListener(this);
        addPhoneNumberButton.addActionListener(this);
        deletePhoneNumberButton.addActionListener(this);
        backButton.addActionListener(this);
    
        userProfilePanel.add(usernameDisplayLabel);
        userProfilePanel.add(phoneNumberLabel);
        userProfilePanel.add(changeUsernameButton);
        userProfilePanel.add(changePasswordButton);
        userProfilePanel.add(addPhoneNumberButton);
        userProfilePanel.add(deletePhoneNumberButton);
        userProfilePanel.add(statusLabel);
        userProfilePanel.add(backButton);
    
        mainPanel.add(userProfilePanel, "UserProfile");
    }

    private void updateProfileDisplay() {   //update user profile
        if (currentLoggedInUser != null) {
            usernameDisplayLabel.setText("Current Username: " + currentLoggedInUser);
            
            UserProfileManager profileManager = new UserProfileManager(currentLoggedInUser);
            String[] phoneNumbers = profileManager.getPhoneNumbers();
            
            if (phoneNumbers != null && phoneNumbers.length > 0) {
                StringBuilder displayText = new StringBuilder("Phone Numbers: ");
                for (int i = 0; i < phoneNumbers.length; i++) {
                    if (i > 0) displayText.append(", ");
                    displayText.append(phoneNumbers[i]);
                }
                phoneNumberLabel.setText(displayText.toString());
            } else {
                phoneNumberLabel.setText("Phone Numbers: None");
            }
        }
    }
        
    private void updatePhoneNumbersDisplay() {
        UserProfileManager profileManager = new UserProfileManager(usernameField.getText());
        String[] phoneNumbers = profileManager.getPhoneNumbers();
        if (phoneNumbers != null && phoneNumbers.length > 0) {
            StringBuilder displayText = new StringBuilder("Phone Numbers: ");
            for (int i = 0; i < phoneNumbers.length; i++) {
                if (i > 0) displayText.append(", ");
                displayText.append(phoneNumbers[i]);
            }
            phoneNumberLabel.setText(displayText.toString());
        } else {
            phoneNumberLabel.setText("Phone Numbers: None");
        }
    }

    public void actionPerformed(ActionEvent e) {    //buttons and the action after click
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = passwordField.getText();
        
            if (userManager.loginUser(username, password)) {
                currentLoggedInUser = username;
                updateProfileDisplay();
                cardLayout.show(mainPanel, "Booking");
            } else {
                loginErrorLabel.setText(userManager.getLastErrorMessage());
            }
        }
        else if (e.getSource() == registerButton) {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (!userManager.userExists(username)) {
                userManager.registerUser(username, password);
                loginErrorLabel.setText("Registration successful!");
            } else {
                loginErrorLabel.setText("Username already exists!");
            }
        }
        else if (e.getSource() == profileButton) {
            cardLayout.show(mainPanel, "UserProfile");
        }
        else if (e.getSource() instanceof Button) {
            Button source = (Button) e.getSource();
            UserProfileManager profileManager = new UserProfileManager(usernameField.getText());

            if (source.getLabel().equals("Proceed")) {
                String date = dateField.getText();
                String time = timeField.getText();

                if (!isValidDate(date)) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid date! Please use YYYY-MM-DD format",
                        "Invalid Date",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!isValidTime(time)) {
                    JOptionPane.showMessageDialog(this,
                        "Invalid time! Please use HH:MM format",
                        "Invalid Time",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double amount = prices.get(destinationChoice.getSelectedItem());
                Component[] components = mainPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof Panel && ((Panel) comp).getName() != null && ((Panel) comp).getName().equals("Confirmation")) {
                        Panel confirmationPanel = (Panel) comp;
                        for (Component c : confirmationPanel.getComponents()) {
                            if (c instanceof TextArea) {
                                TextArea confirmationText = (TextArea) c;
                                String confirmationDetails = String.format(
                                    "%-15s %s\n%-15s %s\n%-15s %s\n%-15s %s\n%-15s RM %.2f",
                                    "Transport:", transportChoice.getSelectedItem(),
                                    "Destination:", destinationChoice.getSelectedItem(),
                                    "Date:", date,
                                    "Time:", time,
                                    "Amount:", amount
                                );
                                confirmationText.setText(confirmationDetails);
                                break;
                            }
                        }
                        break;
                    }
                }
                cardLayout.show(mainPanel, "Confirmation");
            }
            else if (source.getLabel().equals("Confirm and Pay")) {
                if (paymentField.getText().length() > 0) {
                    Booking newBooking = new Booking(
                        transportChoice.getSelectedItem(),
                        destinationChoice.getSelectedItem(),
                        dateField.getText(),
                        timeField.getText(),
                        prices.get(destinationChoice.getSelectedItem()),
                        paymentMethodChoice.getSelectedItem()
                    );
                    newBooking.setPaid(true);
                    bookings.add(newBooking);
                    saveBookings();
                    JOptionPane.showMessageDialog(this, "Booking Confirmed!");
                    cardLayout.show(mainPanel, "Booking");
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter payment details!");
                }
            }
            else if (source.getLabel().equals("View Bookings")) {
                updateBookingsDisplay();
                cardLayout.show(mainPanel, "ViewBookings");
            }
            else if (source.getLabel().equals("Back to Booking") || source.getLabel().equals("Cancel")) {
                cardLayout.show(mainPanel, "Booking");
            }
            else if (source.getLabel().equals("Change Username")) {
                if (hasChangedUsername) {
                    statusLabel.setText("Changing usernames multiple times in a session is not allowed");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
                
                String newUsername = JOptionPane.showInputDialog(this, "Enter new username:");
                if (newUsername != null && !newUsername.isEmpty()) {
                    String result = profileManager.changeUsername(newUsername);
                    statusLabel.setText(result);
                    if (result.equals("Username changed successfully!")) {
                        currentLoggedInUser = newUsername;
                        hasChangedUsername = true;
                        updateProfileDisplay();
                        statusLabel.setForeground(new Color(0, 100, 0));
                    } else {
                        statusLabel.setForeground(Color.RED);
                    }
                }
            }
            else if (source.getLabel().equals("Change Password")) {
                String oldPassword = JOptionPane.showInputDialog(this, "Enter old password:");
                if (oldPassword != null && !oldPassword.isEmpty()) {
                    String newPassword = JOptionPane.showInputDialog(this, "Enter new password:");
                    if (newPassword != null && !newPassword.isEmpty()) {
                        String result = profileManager.changePassword(oldPassword, newPassword);
                        statusLabel.setText(result);
                        statusLabel.setForeground(result.equals("Password changed successfully!") ? 
                            new Color(0, 100, 0) : Color.RED);
                    }
                }
            }
            else if (source.getLabel().equals("Add Phone Number")) {
                String phoneNumber = JOptionPane.showInputDialog(this, "Enter phone number:");
                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    String result = profileManager.addPhoneNumber(phoneNumber);
                    statusLabel.setText(result);
                    if (result.equals("Phone number added successfully!")) {
                        updateProfileDisplay();
                        statusLabel.setForeground(new Color(0, 100, 0));
                    } else {
                        statusLabel.setForeground(Color.RED);
                    }
                }
            }
            else if (source.getLabel().equals("Delete Phone Number")) {
                String[] phoneNumbers = profileManager.getPhoneNumbers();
                if (phoneNumbers == null || phoneNumbers.length == 0) {
                    statusLabel.setText("No phone numbers to delete!");
                    statusLabel.setForeground(Color.RED);
                    return;
                }
                
                Object[] possibilities = phoneNumbers;
                String selectedNumber = (String)JOptionPane.showInputDialog(
                    this,
                    "Choose phone number to delete:",
                    "Delete Phone Number",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    possibilities[0]);
                
                if (selectedNumber != null) {
                    String result = profileManager.deletePhoneNumber(selectedNumber);
                    statusLabel.setText(result);
                    if (result.equals("Phone number deleted successfully!")) {
                        updateProfileDisplay();
                        statusLabel.setForeground(new Color(0, 100, 0));
                    } else {
                        statusLabel.setForeground(Color.RED);
                    }
                }
            }
            else if (source.getLabel().equals("User Profile")) {
                if (currentLoggedInUser != null) {
                    updateProfileDisplay();
                }
                cardLayout.show(mainPanel, "UserProfile");
            }
        }
    }

    //Validation for error
    private boolean isValidDate(String date) {
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) return false;
        try {
            String[] parts = date.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return year >= 2024 && year <= 2050 && month >= 1 && month <= 12 && day >= 1 && day <= 31;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        if (!time.matches("\\d{2}:\\d{2}")) return false;
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
        } catch (Exception e) {
            return false;
        }
    }

    private void saveBookings() {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("bookings.dat"));
            oos.writeObject(bookings);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving bookings: " + e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error closing stream: " + e.getMessage());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBookings() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("bookings.dat"));
            bookings = (ArrayList<Booking>) ois.readObject();
        } catch (FileNotFoundException e) {
            bookings = new ArrayList<Booking>();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
            bookings = new ArrayList<Booking>();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
            bookings = new ArrayList<Booking>();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error closing stream: " + e.getMessage());
                }
            }
        }
    }

    private void updateBookingsDisplay() {
        bookingsArea.setText("");
        if (bookings.isEmpty()) {
            bookingsArea.append("No bookings found.\n");
        } else {
            for (Booking booking : bookings) {
                bookingsArea.append(booking.toString() + "\n");
            }
        }
    }

    private class CustomButton extends Button {
        public CustomButton(String label, Color bgColor) {
            super(label);
            setBackground(bgColor);
            setForeground(Color.WHITE);
        }
    }
}