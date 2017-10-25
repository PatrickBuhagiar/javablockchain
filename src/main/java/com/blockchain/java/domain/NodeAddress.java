package com.blockchain.java.domain;

import java.io.Serializable;

public class NodeAddress implements Serializable {
    private String address;

    public NodeAddress() {
    }

    public NodeAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
