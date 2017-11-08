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

public class Miner implements Serializable {

    private String address;

    public Miner() {

    }

    public Miner(final String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    /**
     * This will fetch the full blockchain for a specific miner.
     *
     * @return The other miner's blockchain.
     */
    public Blockchain fetchMinerBlockChain() {
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
     * Registers a new miner to this miner through a HTTP GET call.
     * @param minerAddress Miner address that will be registered in this miner
     * @return new set of miners for this miner
     */
    @SuppressWarnings("unchecked")
    public Set<Miner> registerMiner(final String minerAddress) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        final HttpGet httpPost = new HttpGet("http://" + this.address + "/miners/" + minerAddress + "/add");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("accept", "application/json");
        HttpResponse response;
        try {
            System.out.println("Registering miner to " + "http://" + this.address + "/miners/" + minerAddress + "/add");
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                System.out.println("Failed: HTTP " + response.getStatusLine().getStatusCode() + " error:" + response.getStatusLine().getReasonPhrase());
                return new HashSet<>();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String jsonString;
            Set miners = null;
            while ((jsonString = br.readLine()) != null) {
                final ObjectMapper mapper = new ObjectMapper();
                miners = mapper.readValue(jsonString, Set.class);
            }
            return (Set<Miner>) miners;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return  new HashSet<>();
    }
}
