package com.blockchain.java.domain;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

import static com.blockchain.java.domain.Block.validProof;

@Component
public class Blockchain implements Serializable {

    private LinkedList<Block> chain;
    private ArrayList<Transaction> nextBlockTransactions;
    private Set<Node> nodes;

    public Blockchain(final LinkedList<Block> chain,
                      final ArrayList<Transaction> nextBlockTransactions,
                      final Set<Node> nodes) {
        this.chain = chain;
        this.nextBlockTransactions = nextBlockTransactions;
        this.nodes = nodes;
    }

    public Blockchain() {
        this.chain = new LinkedList<>();
        this.nextBlockTransactions = new ArrayList<>();
        this.nodes = new HashSet<>();
        //Initialize the blockchain with a block
        this.createNewBlock("0001".getBytes(), 50, 0L);
    }

    public Blockchain updateChain(LinkedList<Block> chain) {
        this.chain = chain;
        return this;
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
     * Get the latest block in the chain.
     *
     * @return the last block in the chain.
     */
    public Block lastBlock() {
        return this.chain.getLast();
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public LinkedList<Block> getChain() {
        return chain;
    }

    public ArrayList<Transaction> getNextBlockTransactions() {
        return nextBlockTransactions;
    }

    /**
     * Register a new {@link Node}
     *
     * @param address the node's address
     */
    public void addNode(final String address) {
        this.nodes.add(new Node(address.replace("%3A", ":")));
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
     * This is the consensus algorithm. The node with the longest chain is considered
     * as the source of truth. The current node will iterate through all other nodes
     * and check their chains. This node will update its chain if it finds a node with a
     * longer chain.
     *
     * @return the blockchain
     */
    public Blockchain consensus() {
        List<Block> newChain = null;
        int max_length = this.chain.size();
        for (Node node : this.nodes) {
            final Blockchain otherNodeBlockchain = node.fetchNodeBlockChain();
            if (otherNodeBlockchain != null) {
                final LinkedList<Block> otherNodeChain = otherNodeBlockchain.chain;
                //if other node's blockchain is longer and valid, update newChain and max_length variables
                if (otherNodeChain.size() > max_length && isValid(otherNodeChain)) {
                    max_length = otherNodeChain.size();
                    newChain = otherNodeChain;
                }
            }
        }

        if (newChain != null) {
            this.chain = new LinkedList<>(newChain);
        }
        return this;
    }
}
