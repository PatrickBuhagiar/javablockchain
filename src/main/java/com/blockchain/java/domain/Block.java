package com.blockchain.java.domain;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

public class Block implements Serializable {

    private int index;
    private long timestamp;
    private ArrayList<Transaction> transactions;
    private long proof;
    private byte[] previousHash;

    public Block(final int index,
                 final long timestamp,
                 final ArrayList<Transaction> transactions,
                 final long proof,
                 final byte[] previousHash) {
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.proof = proof;
        this.previousHash = previousHash;
    }

    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public long getProof() {
        return proof;
    }

    public byte[] getPreviousHash() {
        return previousHash;
    }

    /**
     * Hash a block using SHA-256.
     *
     * @return the hash
     */
    public byte[] hash() {
        final byte[] bytes = SerializationUtils.serialize(this);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "".getBytes();
    }

    /**
     * The proof of work algorithm.
     * The algorithm with iterate through different proofs
     * until one satisfies the valid Proof condition.
     *
      * @param previousProof the previous block's proof
     * @return the new proof
     */
    public static long proofOfWork(final long previousProof) {
        long proof = 0;
        while (!validProof(previousProof, proof))  {
            proof++;
        }
        return proof;
    }

    /**
     * Validates the proof.
     *
     * The proof is deemed valid when hashing the String
     * concatenation of the previous proof and current proof
     * end with 4 zeros.
     *
     * @param previousProof the previous proof
     * @param currentProof the current proof
     * @return true if valid
     */
    public static boolean validProof(final long previousProof, final long currentProof) {

        try {
            final byte[] bytes = MessageDigest.getInstance("SHA-256").digest(("" + previousProof + currentProof).getBytes());
            final String endCharacters = new StringBuffer(new String(bytes)).reverse().substring(0,2);
            return Objects.equals(endCharacters, "00");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }
}
