package com.blockchain.java.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class Blockchain {

    private LinkedList<Block> chain;
    private ArrayList<Transaction> nextBlockTransactions;

    public Blockchain() {
        this.chain = new LinkedList<>();
        this.nextBlockTransactions = new ArrayList<>();

        //Initialize the blockchain with a block
        this.createNewBlock("0001".getBytes(), 50);
    }

    /**
     * Create a new com.blockchain.java.domain.Block and add it to the chain.*
     *
     * @param previousHash the previous hash
     * @param proof        the proof
     * @return the newly created block
     */
    public Block createNewBlock(final byte[] previousHash,
                                final long proof) {
        final Block block = new Block(this.chain.size() + 1,
                System.currentTimeMillis(),
                new ArrayList<>(this.nextBlockTransactions),
                proof,
                previousHash);

        this.nextBlockTransactions.clear();
        this.chain.add(block);

        return block;
    }

    /**
     * Create a new transaction and return the index of the previous block.
     * A transaction is added to a queue so that it can be processed into a block later on.
     *
     * @param transaction the new Transaction
     * @return an increment of the last block's index
     */
    public int createNewTransaction(final Transaction transaction) {
        this.nextBlockTransactions.add(transaction);

        return this.lastBlock().getIndex() + 1;
    }

    /**
     * Get the latest block in the chain.
     *
     * @return the last block in the chain.
     */
    public Block lastBlock() {
        return this.chain.getLast();
    }

    public List<Block> getChain() {
        return this.chain;
    }

}
