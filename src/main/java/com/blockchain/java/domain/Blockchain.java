package com.blockchain.java.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class Blockchain {

    private final LinkedList<Block> chain;
    private final ArrayList<Transaction> nextBlockTransactions;

    public Blockchain() {
        this.chain = new LinkedList<>();
        this.nextBlockTransactions = new ArrayList<>();

        //Initialize the blockchain with a block
        this.createNewBlock("0001", 50);
    }

    /**
     * Create a new com.blockchain.java.domain.Block and add it to the chain.*
     *
     * @param previousHash the previous hash
     * @param proof        the proof
     * @return the newly created block
     */
    public Block createNewBlock(final String previousHash,
                                final long proof) {
        final Block block = new Block(this.chain.size() + 1,
                System.currentTimeMillis(),
                this.nextBlockTransactions,
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
     * @param sender    The sender
     * @param recipient the person receiving funds
     * @param amount    the transaction amount
     * @return an incremet of the last block's index
     */
    public int createNewTransaction(final String sender,
                                    final String recipient,
                                    final long amount) {
        nextBlockTransactions.add(new Transaction(sender, recipient, amount));

        return this.getLastBlock().getIndex() + 1;
    }

    /**
     * Get the latest block in the chain.
     *
     * @return the last block in the chain.
     */
    public Block getLastBlock() {
        return this.chain.getLast();
    }

    public List<Block> getChain() {
        return this.chain;
    }

}