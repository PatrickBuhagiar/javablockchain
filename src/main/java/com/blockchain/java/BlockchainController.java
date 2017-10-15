package com.blockchain.java;

import com.blockchain.java.domain.Block;
import com.blockchain.java.domain.Blockchain;
import com.blockchain.java.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping(value="mine", method = GET)
    public @ResponseBody
    Block mineBlocking() {
        return null;
    }

    @RequestMapping(value="transaction", method = POST)
    public @ResponseBody
    Transaction createTransaction(@RequestBody final Transaction transaction) {
        return null;
    }
}
