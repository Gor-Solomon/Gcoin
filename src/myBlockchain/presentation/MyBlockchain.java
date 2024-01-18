package myBlockchain.presentation;

import myBlockchain.domain.*;

public class MyBlockchain {

    public static void main(String[] args) {

        Miner miner = new Miner();

        var genesisBlock = new Block(BlockChain.getNextId(), "Genesis Block", Constants.GENESIS_PREVIOUS_HASH);
        miner.mine(genesisBlock);

        BlockChain blockChain = new BlockChain(genesisBlock);

        miner.processTransaction("Sendding 1000 Bitcoin from Jack To Jon", blockChain);
        miner.processTransaction("Sendding 500 Bitcoin from Mery To Peeter", blockChain);
        miner.processTransaction("Sendding 33.5 Bitcoin from Jon To Kale", blockChain);

        System.out.println(blockChain);

    }

}
