package com.blockchain.java.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private String address;

    public Node(final String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public Blockchain fetchChain() {
        final Client client = ClientBuilder.newClient();
        final WebTarget resource = client.target("http://" + this.address + "/chain");
        final Invocation.Builder request = resource.request();

        request.accept(MediaType.APPLICATION_JSON_TYPE);
        final Response response = request.get();

        Blockchain blockchain = null;
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            final String text = response.getEntity().toString();
            final ObjectMapper mapper = new ObjectMapper();
            try {
                blockchain = mapper.readValue(text, Blockchain.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return blockchain;
    }
}
