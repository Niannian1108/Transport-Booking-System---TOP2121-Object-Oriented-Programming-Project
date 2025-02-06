import java.io.Serializable;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transport;
    private String destination;
    private String date;
    private String time;
    private double amount;
    private boolean isPaid;

    public Booking(String transport, String destination, String date, String time, double amount) {
        this.transport = transport;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.isPaid = false;
    }

    // Getters
    public String getTransport() { return transport; }
    public String getDestination() { return destination; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public double getAmount() { return amount; }
    public boolean isPaid() { return isPaid; }

    // Setters
    public void setTransport(String transport) { this.transport = transport; }
    public void setDestination(String destination) { this.destination = destination; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setPaid(boolean paid) { isPaid = paid; }

    @Override
    public String toString() {
        return String.format(
            "%-15s %s\n%-15s %s\n%-15s %s\n%-15s %s\n%-15s $%.2f\n%-15s %s\n------------------------",
            "Transport:", transport,
            "Destination:", destination,
            "Date:", date,
            "Time:", time,
            "Amount:", amount,
            "Status:", (isPaid ? "Paid" : "Pending")
        );
    }
}
