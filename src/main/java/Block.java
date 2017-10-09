import java.util.List;

public class Block {

    private int index;
    private long timestamp;
    private List<Transaction> transactions;
    private long proof;
    private String previousHash;

    public Block(final int index,
                 final long timestamp,
                 final List<Transaction> transactions,
                 final long proof,
                 final String previousHash) {
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public long getProof() {
        return proof;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}
