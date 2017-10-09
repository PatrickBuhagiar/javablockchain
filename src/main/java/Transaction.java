public class Transaction {

    private String sender;
    private String recipient;
    private long amount;

    public Transaction(String sender, String recipient, long amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public long getAmount() {
        return amount;
    }
}


