import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TransportBookingSystem extends Applet implements ActionListener {
    private CardLayout cardLayout;
    private Panel mainPanel;

    private TextField usernameField, passwordField;
    private Button loginButton, registerButton;
    private Label loginErrorLabel;

    private Choice transportChoice;
    private TextField dateField, timeField;
    private Button confirmDetailsButton, confirmBookingButton, viewBookingsButton;

    private Label confirmationLabel;
    private TextArea bookingsArea;

    private ArrayList<String> bookings;
    private UserManager userManager;

    public void init() {
        System.out.println("Initializing applet...");
        setSize(600, 400);

        // Initialize components
        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);
        bookings = new ArrayList<String>();
        userManager = new UserManager();

        // Login Page
        Panel loginPanel = new Panel(new GridLayout(4, 2));
        usernameField = new TextField();
        passwordField = new TextField();
        passwordField.setEchoChar('*');
        loginButton = new Button("Login");
        registerButton = new Button("Register");
        loginErrorLabel = new Label("");
        loginErrorLabel.setForeground(Color.RED);
        
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        
        loginPanel.add(new Label("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new Label("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        loginPanel.add(loginErrorLabel);
        
        // Booking Page
        Panel bookingPanel = new Panel(new GridLayout(5, 2));
        transportChoice = new Choice();
        transportChoice.add("Bus");
        transportChoice.add("Train");
        transportChoice.add("Ferry");
        dateField = new TextField("YYYY-MM-DD");
        timeField = new TextField("HH:MM");
        confirmDetailsButton = new Button("Confirm Details");
        confirmDetailsButton.addActionListener(this);
        bookingPanel.add(new Label("Transport Mode:"));
        bookingPanel.add(transportChoice);
        bookingPanel.add(new Label("Date:"));
        bookingPanel.add(dateField);
        bookingPanel.add(new Label("Time:"));
        bookingPanel.add(timeField);
        bookingPanel.add(confirmDetailsButton);

        // Confirmation Page
        Panel confirmationPanel = new Panel(new GridLayout(3, 1));
        confirmationLabel = new Label("Please confirm your details.");
        confirmBookingButton = new Button("Confirm Booking");
        confirmBookingButton.addActionListener(this);
        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(confirmBookingButton);

        // View Bookings Page
        Panel viewBookingsPanel = new Panel(new BorderLayout());
        bookingsArea = new TextArea();
        bookingsArea.setEditable(false);
        viewBookingsButton = new Button("Back to Booking Page");
        viewBookingsButton.addActionListener(this);
        viewBookingsPanel.add(bookingsArea, BorderLayout.CENTER);
        viewBookingsPanel.add(viewBookingsButton, BorderLayout.SOUTH);

        // Adding panels to mainPanel
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(bookingPanel, "Booking");
        mainPanel.add(confirmationPanel, "Confirmation");
        mainPanel.add(viewBookingsPanel, "ViewBookings");

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "Login");
        repaint();    
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        // Login button handling
        if (source == loginButton) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (userManager.loginUser(username, password)) {
                // Clear any previous error messages
                loginErrorLabel.setText("");
                cardLayout.show(mainPanel, "Booking");
            } else {
                // Show error message on the login page
                loginErrorLabel.setText(userManager.getLastErrorMessage());
            }
        }
                
        // Register button handling
        if (source == registerButton) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (!userManager.userExists(username)) {
                userManager.registerUser(username, password);
                System.out.println("User registered successfully");
            } else {
                System.out.println("Username already exists");
            }
        }
        
        // Other button handlers can be added similarly
        if (source == confirmDetailsButton) {
            cardLayout.show(mainPanel, "Confirmation");
        }
        
        if (source == confirmBookingButton) {
            // Booking confirmation logic
            String booking = transportChoice.getSelectedItem() + " - " + 
                             dateField.getText() + " " + 
                             timeField.getText();
            bookings.add(booking);
            cardLayout.show(mainPanel, "Booking");
        }
        
        if (source == viewBookingsButton) {
            bookingsArea.setText("");
            for (String booking : bookings) {
                bookingsArea.append(booking + "\n");
            }
            cardLayout.show(mainPanel, "ViewBookings");
        }
    }
}
