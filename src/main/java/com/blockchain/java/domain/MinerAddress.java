package com.blockchain.java.domain;

import java.io.Serializable;

public class MinerAddress implements Serializable {
    private String address;

    protected MinerAddress() {
    }

    public MinerAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
