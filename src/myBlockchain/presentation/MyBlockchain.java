package myBlockchain.presentation;

import myBlockchain.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyBlockchain {

    public static void main(String[] args) {

        List<String> transactions = new ArrayList<>();
        transactions.add("aa");
        transactions.add("bb");
        transactions.add("dd");
        transactions.add("ee");
        transactions.add("11");
        transactions.add("22");
        transactions.add("33");
        transactions.add("44");
        transactions.add("55");

        var merkleTree = new MerkleTree(transactions);
        System.out.println(merkleTree.getMerkelRoot());
    }

    private void minerTest(){
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
