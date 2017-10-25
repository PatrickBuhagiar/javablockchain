package com.blockchain.java;

import com.blockchain.java.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    Blockchain getChain() {
        return blockchain;
    }

    @RequestMapping(value = "mine", method = GET)
    public @ResponseBody
    Block mineBlock(@RequestHeader(value = "identifier") final String identifier) {
        //find new proof
        final long newProof = proofOfWork(blockchain.lastBlock().getProof());
        //reward miner. In this case we will set sender as 0000
        blockchain.createNewTransaction(new Transaction("0000", identifier, 1L));
        //create new block
        return blockchain.createNewBlock(blockchain.lastBlock().hash(), newProof, System.currentTimeMillis());
    }

    @RequestMapping(value = "transaction", method = POST)
    public @ResponseBody
    int createTransaction(@RequestBody final Transaction transaction) {
        return blockchain.createNewTransaction(transaction);
    }

    @RequestMapping(value = "nodes/register", method = POST)
    public @ResponseBody
    Set<Node> registerSelf(@RequestBody final NodeAddress nodeAddress) {
        System.out.println("Current port is " + PortListener.getPort());
        //Assumption: we'll be working on localhost only
        final String thisNodeAddress = "127.0.0.1:" + PortListener.getPort();
        final Node alphaNode = new Node(nodeAddress.getAddress());

        //get the target's blockchain. For simplicity, we're going to assume that the chosen
        //node contains the right list of nodes.
        final Blockchain existingBlockChain = alphaNode.fetchNodeBlockChain();

        //next we're going to iterate all existing nodes and add this new node's nodeAddress to their node lists
        alphaNode.registerNode(thisNodeAddress);
        existingBlockChain.getNodes().forEach(node -> node.registerNode(thisNodeAddress));

        //finally, update this node's node list
        this.blockchain.addNode(alphaNode.getAddress());
        this.blockchain.updateChain(existingBlockChain.getChain());
        existingBlockChain.getNodes().forEach(node -> this.blockchain.addNode(node.getAddress()));
        return this.blockchain.getNodes();
    }

    @RequestMapping(value = "nodes/{address}/add", method = GET)
    public @ResponseBody
    Set<Node> addNode(@PathVariable final String address) {
        //Adds this nodeAddress to the list of neighbouring nodes for this Node.
        System.out.println("Adding Node " + address);
        this.blockchain.addNode(address);
        return this.blockchain.getNodes();
    }

    @RequestMapping(value = "nodes/consensus", method = GET)
    public @ResponseBody
    Blockchain consensus() {
        return this.blockchain.consensus();
    }
}
