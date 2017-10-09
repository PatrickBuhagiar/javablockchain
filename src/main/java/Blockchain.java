import java.util.ArrayList;
import java.util.LinkedList;

public class Blockchain {

    private LinkedList<Block> chain;
    private ArrayList<Transaction> currentTransaction;

    public Blockchain() {
        this.chain = new LinkedList<Block>();
        this.currentTransaction = new ArrayList<Transaction>();

        this.createNewBlock("1", 100);
    }

    /**
     * Create a new Block
     */
    public void createNewBlock(final String previousHash,
                               final long proof) {
        final Block block = new Block(this.chain.size() + 1,
                System.currentTimeMillis(),
                this.currentTransaction,
                proof,
                previousHash);

        this.currentTransaction.clear();
        this.chain.add(block);
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
        currentTransaction.add(new Transaction(sender, recipient, amount));

        return this.getLastBlock().getIndex() + 1;
    }

    public static void hash(final Block block) {
    }

    /**
     * Get the latest block in the chain.
     *
     * @return the last block in the chain.
     */
    public Block getLastBlock() {
        return this.chain.getLast();
    }

}
