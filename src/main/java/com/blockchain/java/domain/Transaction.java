package com.blockchain.java.domain;

import java.io.Serializable;

public class Transaction implements Serializable {

    private String sender;
    private String recipient;
    private long amount;
    
    protected Transaction() {
    }

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


