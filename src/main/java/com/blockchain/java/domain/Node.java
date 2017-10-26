package com.blockchain.java.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Node implements Serializable {

    private String address;

    public Node() {

    }

    public Node(final String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    /**
     * This will fetch the full blockchain for a specific node.
     *
     * @return The other node's blockchain.
     */
    public Blockchain fetchNodeBlockChain() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://" + this.address + "/chain");
        getRequest.addHeader("accept", "application/json");
        getRequest.addHeader("Content-Type", "application/json");


        HttpResponse response;
        try {
            System.out.println("Fetching Blockchain " + "http://" + this.address + "/chain");
            response = httpClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Failed: HTTP " + response.getStatusLine().getStatusCode() + " error:" + response.getStatusLine().getReasonPhrase());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String jsonString;
            Blockchain blockchain = null;
            while ((jsonString = br.readLine()) != null) {
                //Parse JSON String into Blockchain POJO
                final ObjectMapper mapper = new ObjectMapper();
                blockchain = mapper.readValue(jsonString, Blockchain.class);
            }
            return blockchain;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * Registers a new node address to thid node through a HTTP GET call.
     * @param nodeAddress Node address that will be registered in this node
     * @return new set of nodes for this node
     */
    @SuppressWarnings("unchecked")
    public Set<Node> registerNode(final String nodeAddress) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpPost = new HttpGet("http://" + this.address + "/nodes/" + nodeAddress + "/add");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("accept", "application/json");
        HttpResponse response;
        try {
            System.out.println("Registering node to " + "http://" + this.address + "/nodes/" + nodeAddress + "/add");
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Failed: HTTP " + response.getStatusLine().getStatusCode() + " error:" + response.getStatusLine().getReasonPhrase());
                return new HashSet<>();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String jsonString;
            Set nodes = null;
            while ((jsonString = br.readLine()) != null) {
                final ObjectMapper mapper = new ObjectMapper();
                nodes = mapper.readValue(jsonString, Set.class);
            }
            return (Set<Node>) nodes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return  new HashSet<>();
    }
}
