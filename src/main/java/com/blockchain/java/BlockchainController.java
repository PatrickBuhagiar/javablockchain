package com.blockchain.java;

import com.blockchain.java.domain.Block;
import com.blockchain.java.domain.Blockchain;
import com.blockchain.java.domain.Node;
import com.blockchain.java.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.blockchain.java.domain.Block.proofOfWork;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class BlockchainController {

    @Autowired
    private Blockchain blockchain;

    @RequestMapping(value = "chain", method = GET)
    public @ResponseBody
    List<Block> getChain() {
        return blockchain.getChain();
    }

    @RequestMapping(value = "mine", method = GET)
    public @ResponseBody
    Block mineBlock(@RequestHeader(value = "identifier") final String identifier) {
        //find new proof
        final long newProof = proofOfWork(blockchain.lastBlock().getProof());
        //reward miner
        blockchain.createNewTransaction(new Transaction("0000", identifier, 1L));
        //create new block
        return blockchain.createNewBlock(blockchain.lastBlock().hash(), newProof);
    }

    @RequestMapping(value = "transaction", method = POST)
    public @ResponseBody
    int createTransaction(@RequestBody final Transaction transaction) {
        return blockchain.createNewTransaction(transaction);
    }

    @RequestMapping(value = "nodes/{address}", method = POST)
    public @ResponseBody
    Set<Node> addNode(@RequestParam(value = "address") final String address) {
        //Let the other nodes in the blockchain know about the new node
        //At this point, I'm assuming that existing nodes are already synchronised
        blockchain.getNodes().forEach(node -> {
            final Blockchain nodeBlockchain = node.fetchChain();
            if (nodeBlockchain != null) {
                nodeBlockchain.addNode(address);
            }
        });
        //Add new node to current node's list
        this.blockchain.addNode(address);

        //return the whole list of nodes
        return this.blockchain.getNodes();
    }

    @RequestMapping(value = "nodes/consensus", method = GET)
    public @ResponseBody
    Blockchain consensus() {
        return this.blockchain.consensus();
    }
}
