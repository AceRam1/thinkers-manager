package ThinkersUserManagement;

public class payment {
    private int payment_id;
    private int tuteeId;
    private String subject;
    private String start;
    private String end;
    private int balance;
    private String status;

    // Existing constructor
    public payment(int tuteeId, String subject, String start, String end, int balance, String status) {
        this.tuteeId = tuteeId;
        this.subject = subject;
        this.start = start;
        this.end = end;
        this.balance = balance;
        this.status = status;
    }

    // Modified constructor with paymentId
    public payment(int payment_id, int tuteeId, String subject, String start, String end, int balance, String status) {
        this.payment_id = payment_id;
        this.tuteeId = tuteeId;
        this.subject = subject;
        this.start = start;
        this.end = end;
        this.balance = balance;
        this.status = status;
    }

    // Getters and setters
    public int getPaymentId() {
        return payment_id;
    }

    public void setPaymentId(int paymentId) {
        this.payment_id = paymentId;
    }

    public int getTuteeId() {
        return tuteeId;
    }

    public void setTuteeId(int tuteeId) {
        this.tuteeId = tuteeId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}