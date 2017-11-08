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
        //reward miner 1 coin. In this case we will set sender as ....
        blockchain.createNewTransaction(new Transaction("....", identifier, 1L));
        //create new block
        return blockchain.createNewBlock(blockchain.lastBlock().hash(), newProof, System.currentTimeMillis());
    }

    @RequestMapping(value = "transaction", method = POST)
    public @ResponseBody
    int createTransaction(@RequestBody final Transaction transaction) {
        return blockchain.createNewTransaction(transaction);
    }

    @RequestMapping(value = "miners/register", method = POST)
    public @ResponseBody
    Set<Miner> registerSelf(@RequestBody final MinerAddress minerAddress) {
        System.out.println("Current port is " + PortListener.getPort());
        //Assumption: we'll be working on localhost only
        final String currentMinerAddress = "127.0.0.1:" + PortListener.getPort();
        final Miner alphaMiner = new Miner(minerAddress.getAddress());

        //get the target's blockchain. For simplicity, we're going to assume that the chosen
        //miner contains the right list of miners.
        final Blockchain existingBlockChain = alphaMiner.fetchMinerBlockChain();

        //next we're going to iterate all existing miners and add this new miner's minerAddress to their miner lists
        alphaMiner.registerMiner(currentMinerAddress);
        existingBlockChain.getNeighbouringMiners().forEach(miner -> miner.registerMiner(currentMinerAddress));

        //finally, update this miner's blockchain
        this.blockchain.addMiner(alphaMiner.getAddress());
        this.blockchain.updateChain(existingBlockChain.getChain());
        //Add new miner address to this miner
        existingBlockChain.getNeighbouringMiners().forEach(miner -> this.blockchain.addMiner(miner.getAddress()));
        return this.blockchain.getNeighbouringMiners();
    }

    @RequestMapping(value = "miners/{address}/add", method = GET)
    public @ResponseBody
    Set<Miner> addMiner(@PathVariable final String address) {
        //Adds this minerAddress to the list of neighbouring miners for this Miner.
        System.out.println("Adding Miner " + address);
        this.blockchain.addMiner(address);
        return this.blockchain.getNeighbouringMiners();
    }

    @RequestMapping(value = "miners/consensus", method = GET)
    public @ResponseBody
    Blockchain consensus() {
        return this.blockchain.consensus();
    }
}
