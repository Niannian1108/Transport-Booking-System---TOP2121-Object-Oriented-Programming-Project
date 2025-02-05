import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TransportBookingSystem extends Applet implements ActionListener {
    private CardLayout cardLayout;
    private Panel mainPanel;

    private TextField usernameField, passwordField;
    private Button loginButton, registerButton;

    private Choice transportChoice;
    private TextField dateField, timeField;
    private Button confirmDetailsButton, confirmBookingButton, viewBookingsButton;

    private Label confirmationLabel;
    private TextArea bookingsArea;

    private ArrayList<String> bookings;

    public void init() {
        cardLayout = new CardLayout();
        mainPanel = new Panel(cardLayout);
        bookings = new ArrayList<String>();

        // Login Page
        Panel loginPanel = new Panel(new GridLayout(3, 2));
        usernameField = new TextField();
        passwordField = new TextField();
        passwordField.setEchoChar('*');
        loginButton = new Button("Login");
        registerButton = new Button("Register");
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);
        loginPanel.add(new Label("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new Label("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

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
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton || e.getSource() == registerButton) {
            cardLayout.show(mainPanel, "Booking");
        } else if (e.getSource() == confirmDetailsButton) {
            String transport = transportChoice.getSelectedItem();
            String date = dateField.getText();
            String time = timeField.getText();
            confirmationLabel.setText("Transport: " + transport + ", Date: " + date + ", Time: " + time);
            cardLayout.show(mainPanel, "Confirmation");
        } else if (e.getSource() == confirmBookingButton) {
            String transport = transportChoice.getSelectedItem();
            String date = dateField.getText();
            String time = timeField.getText();
            bookings.add("Transport: " + transport + ", Date: " + date + ", Time: " + time);
            showStatus("Booking Confirmed!");
            cardLayout.show(mainPanel, "Booking");
        } else if (e.getSource() == viewBookingsButton) {
            bookingsArea.setText("");
            for (String booking : bookings) {
                bookingsArea.append(booking + "\n");
            }
            cardLayout.show(mainPanel, "ViewBookings");
        }
    }
}