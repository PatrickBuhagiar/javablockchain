package com.blockchain.java.domain;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

import static com.blockchain.java.domain.Block.validProof;

@Component
public class Blockchain implements Serializable {

    private LinkedList<Block> chain;
    private ArrayList<Transaction> nextBlockTransactions;
    private Set<Miner> neighbouringMiners;

    public Blockchain(final LinkedList<Block> chain,
                      final ArrayList<Transaction> nextBlockTransactions,
                      final Set<Miner> neighbouringMiners) {
        this.chain = chain;
        this.nextBlockTransactions = nextBlockTransactions;
        this.neighbouringMiners = neighbouringMiners;
    }

    public Blockchain() {
        this.chain = new LinkedList<>();
        this.nextBlockTransactions = new ArrayList<>();
        this.neighbouringMiners = new HashSet<>();
        //Initialize the blockchain with a block
        this.createNewBlock("0001".getBytes(), 50, 0L);
    }

    public void updateChain(LinkedList<Block> chain) {
        this.chain = chain;
    }

    /**
     * Create a new {@link Block} and add it to the chain.
     *
     * @param previousHash the previous hash
     * @param proof        the proof
     * @param timestamp    timestamp
     * @return the newly created block
     */
    public Block createNewBlock(final byte[] previousHash,
                                final long proof,
                                final long timestamp) {
        final Block block = new Block(this.chain.size() + 1,
                timestamp,
                new ArrayList<>(this.nextBlockTransactions),
                proof,
                previousHash);

        this.nextBlockTransactions.clear();
        this.chain.add(block);

        return block;
    }

    /**
     * Create a new {@link Transaction} and return the index of the previous block.
     * A {@link Transaction} is added to a list so that it can be processed into a block later on.
     *
     * @param transaction the new Transaction
     * @return an increment of the last block's index
     */
    public int createNewTransaction(final Transaction transaction) {
        this.nextBlockTransactions.add(transaction);

        return this.lastBlock().getIndex() + 1;
    }

    /**
     * Get the last block in the chain.
     *
     * @return the last block in the chain.
     */
    public Block lastBlock() {
        return this.chain.getLast();
    }

    public Set<Miner> getNeighbouringMiners() {
        return neighbouringMiners;
    }

    public LinkedList<Block> getChain() {
        return chain;
    }

    /**
     * Register a new {@link Miner}
     *
     * @param address the miner's address
     */
    public void addMiner(final String address) {
        this.neighbouringMiners.add(new Miner(address.replace("%3A", ":")));
    }

    /**
     * Determines whether given blockchain is valid
     *
     * @param chain the block chain
     * @return true is valid, false otherwise
     */
    private static boolean isValid(final List<Block> chain) {
        Block previousBlock = chain.get(0);
        for (int i = 1; i < chain.size(); i++) {
            final Block currentBlock = chain.get(i);

            //assert that current block has correct previous block hash
            if (!Arrays.equals(currentBlock.getPreviousHash(), previousBlock.hash())) {
                return false;
            }

            //check that the proof is correct
            if (!validProof(previousBlock.getProof(), currentBlock.getProof())) {
                return false;
            }

            previousBlock = currentBlock;
        }
        return true;
    }

    /**
     * This is the consensus algorithm. The miner with the longest chain is considered
     * as the source of truth. The current miner will iterate through all other neighbouringMiners
     * and check their chains. This miner will update its chain if it finds a miner with a
     * longer chain.
     *
     * @return the blockchain
     */
    public Blockchain consensus() {
        List<Block> newChain = null;
        int max_length = this.chain.size();
        for (Miner miner : this.neighbouringMiners) {
            final Blockchain otherMinerBlockchain = miner.fetchMinerBlockChain();
            if (otherMinerBlockchain != null) {
                final LinkedList<Block> otherMinerChain = otherMinerBlockchain.chain;
                //if other miner's blockchain is longer and valid, update newChain and max_length variables
                if (otherMinerChain.size() > max_length && isValid(otherMinerChain)) {
                    max_length = otherMinerChain.size();
                    newChain = otherMinerChain;
                }
            }
        }

        if (newChain != null) {
            this.chain = new LinkedList<>(newChain);
        }
        return this;
    }
}
