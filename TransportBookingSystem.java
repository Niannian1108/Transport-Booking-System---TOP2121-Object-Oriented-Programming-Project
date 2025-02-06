import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.JOptionPane;

public class TransportBookingSystem extends Applet implements ActionListener {
    private CardLayout cardLayout;
    private Panel mainPanel;
    private UserManager userManager;
    
    // UI Components for Login
    private TextField usernameField, passwordField;
    private Button loginButton, registerButton;
    private Label loginErrorLabel;
    private final Color MAIN_BLUE = new Color(51, 122, 183);
    private final Color DARK_BLUE = new Color(40, 96, 144);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);

    // UI Components for Booking
    private Choice transportChoice, destinationChoice;
    private TextField dateField, timeField, paymentField;
    private TextArea bookingsArea;
    private ArrayList<Booking> bookings;
    private HashMap<String, Double> prices;

    public void init() {
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

    private void setupLoginPanel() {
        Panel loginPanel = new Panel(new BorderLayout(0, 20));
        loginPanel.setBackground(BACKGROUND_COLOR);

        // Title Panel
        Panel titlePanel = new Panel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(MAIN_BLUE);
        Label titleLabel = new Label("Transport Booking System", Label.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        // Input Panel
        Panel inputPanel = new Panel(new GridLayout(4, 1, 0, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);

        // Username
        Panel usernamePanel = new Panel(new BorderLayout(5, 0));
        usernamePanel.add(new Label("Username:"), BorderLayout.WEST);
        usernameField = new TextField(20);
        usernamePanel.add(usernameField, BorderLayout.CENTER);

        // Password
        Panel passwordPanel = new Panel(new BorderLayout(5, 0));
        passwordPanel.add(new Label("Password:"), BorderLayout.WEST);
        passwordField = new TextField(20);
        passwordField.setEchoChar('*');
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        // Buttons
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new CustomButton("Login", MAIN_BLUE);
        registerButton = new CustomButton("Register", DARK_BLUE);
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Error Label
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

    private void setupBookingPanel() {
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

        bookingButton.addActionListener(this);
        viewBookingsButton.addActionListener(this);

        bookingPanel.add(new Label("Transport Mode:"));
        bookingPanel.add(transportChoice);
        bookingPanel.add(new Label("Destination:"));
        bookingPanel.add(destinationChoice);
        bookingPanel.add(new Label("Date:"));
        bookingPanel.add(dateField);
        bookingPanel.add(new Label("Time:"));
        bookingPanel.add(timeField);
        bookingPanel.add(bookingButton);
        bookingPanel.add(viewBookingsButton);

        mainPanel.add(bookingPanel, "Booking");
    }

    private void setupConfirmationPanel() {
        Panel confirmationPanel = new Panel(new GridLayout(5, 1, 5, 5));

        Label confirmationLabel = new Label("Booking Details:", Label.CENTER);
        confirmationLabel.setFont(new Font("Arial", Font.BOLD, 12));

        TextArea confirmationText = new TextArea(5, 40);
        confirmationText.setEditable(false);

        paymentField = new TextField("Enter payment details");
        Button confirmBookingButton = new CustomButton("Confirm and Pay", MAIN_BLUE);
        Button cancelButton = new CustomButton("Cancel", DARK_BLUE);

        confirmBookingButton.addActionListener(this);
        cancelButton.addActionListener(this);

        confirmationPanel.add(confirmationLabel);
        confirmationPanel.add(confirmationText);
        confirmationPanel.add(paymentField);
        confirmationPanel.add(confirmBookingButton);
        confirmationPanel.add(cancelButton);

        mainPanel.add(confirmationPanel, "Confirmation");
    }

    private void setupViewBookingsPanel() {
        Panel viewBookingsPanel = new Panel(new BorderLayout(5, 5));

        Label headerLabel = new Label("Your Bookings:", Label.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 12));

        bookingsArea = new TextArea(10, 40);
        bookingsArea.setEditable(false);

        Button backButton = new CustomButton("Back to Booking", MAIN_BLUE);
        backButton.addActionListener(this);

        viewBookingsPanel.add(headerLabel, BorderLayout.NORTH);
        viewBookingsPanel.add(bookingsArea, BorderLayout.CENTER);
        viewBookingsPanel.add(backButton, BorderLayout.SOUTH);

        mainPanel.add(viewBookingsPanel, "ViewBookings");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (userManager.loginUser(username, password)) {
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
        else if (e.getSource() instanceof Button) {
            Button source = (Button) e.getSource();
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
                                    "%-15s %s\n%-15s %s\n%-15s %s\n%-15s %s\n%-15s $%.2f",
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
                        prices.get(destinationChoice.getSelectedItem())
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
        }
    }

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
            bookings = new ArrayList<Booking>(); // File not found, start with an empty list
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
