package com.blockchain.java.domain;

public class Transaction {

    private final String sender;
    private final String recipient;
    private final long amount;

    public Transaction(final String sender,
                       final String recipient,
                       final long amount) {
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


