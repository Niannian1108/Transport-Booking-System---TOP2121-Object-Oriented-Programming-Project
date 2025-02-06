import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TransportBookingSystem extends Applet implements ActionListener {
    private CardLayout cardLayout;
    private Panel mainPanel;
    
    // UI Components
    private TextField usernameField, passwordField;
    private Button loginButton, registerButton;
    private Label loginErrorLabel, titleLabel;
    private Panel loginPanel, inputPanel, buttonPanel;
    
    // Other components remain the same
    private Choice transportChoice;
    private TextField dateField, timeField;
    private Button confirmDetailsButton, confirmBookingButton, viewBookingsButton;
    private Label confirmationLabel;
    private TextArea bookingsArea;
    private ArrayList<String> bookings;
    private UserManager userManager;

    // Custom colors
    private final Color MAIN_BLUE = new Color(51, 122, 183);
    private final Color LIGHT_BLUE = new Color(217, 237, 247);
    private final Color DARK_BLUE = new Color(40, 96, 144);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    public void init() {
        System.out.println("Initializing applet...");
        setSize(600, 400);
        setBackground(BACKGROUND_COLOR);

        // Initialize components
        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);
        bookings = new ArrayList<String>();
        userManager = new UserManager();

        // Enhanced Login Page
        loginPanel = new Panel(new BorderLayout(0, 20));
        loginPanel.setBackground(BACKGROUND_COLOR);
        
        // Title Panel
        Panel titlePanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(MAIN_BLUE);
        titleLabel = new Label("Transport Booking System", Label.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Input Panel
        inputPanel = new Panel(new GridLayout(4, 1, 0, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);
        
        // Username field with label
        Panel usernamePanel = new Panel(new BorderLayout(5, 0));
        usernamePanel.setBackground(BACKGROUND_COLOR);
        Label usernameLabel = new Label("Username:");
        usernameLabel.setForeground(DARK_BLUE);
        usernameField = new TextField(20);
        usernamePanel.add(usernameLabel, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        
        // Password field with label
        Panel passwordPanel = new Panel(new BorderLayout(5, 0));
        passwordPanel.setBackground(BACKGROUND_COLOR);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setForeground(DARK_BLUE);
        passwordField = new TextField(20);
        passwordField.setEchoChar('*');
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        // Button Panel
        buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        loginButton = new CustomButton("Login", MAIN_BLUE);
        registerButton = new CustomButton("Register", DARK_BLUE);
        
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        // Error Label
        loginErrorLabel = new Label("", Label.CENTER);
        loginErrorLabel.setForeground(Color.RED);
        
        // Add components to input panel
        inputPanel.add(usernamePanel);
        inputPanel.add(passwordPanel);
        inputPanel.add(buttonPanel);
        inputPanel.add(loginErrorLabel);
        
        // Add panels to login panel
        loginPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Center panel to contain input panel
        Panel centerPanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(inputPanel);
        loginPanel.add(centerPanel, BorderLayout.CENTER);

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

        // Custom Button class for better aesthetics
        private class CustomButton extends Button {
            public CustomButton(String label, Color bgColor) {
                super(label);
                setBackground(bgColor);
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 12));
            }
            
            public void paint(Graphics g) {
                Color oldColor = g.getColor();
                g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g.setColor(getForeground());
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getLabel())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g.drawString(getLabel(), x, y);
                g.setColor(oldColor);
            }
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            
            // Login button handling
            if (source == loginButton) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                
                if (userManager.loginUser(username, password)) {
                    cardLayout.show(mainPanel, "Booking");
                } else {
                    // Display the error message on the login page
                    loginErrorLabel.setText(userManager.getLastErrorMessage());
                    cardLayout.show(mainPanel, "Login"); // Stay on the login page
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
