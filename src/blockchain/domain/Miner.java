package blockchain.domain;

import blockchain.Constants;
import blockchain.domain.transactions.Transaction;

import java.util.List;

public class Miner {

    private static final String leadingZeros = "0".repeat(Constants.Difficulty);

    private double reward;


    public double getReward(){
        return this.reward;
    }

    public Block mine(Block block){

        while (!isGoldenHash(block)){
            block.incrementNonce();
            block.generateHash();
        }

        return block;
    }

    private boolean isGoldenHash(Block block){
        return block.getHash().startsWith(leadingZeros);
    }

    public void processTransactions(List<Transaction> newTransactions, BlockChain blockChain) {

        var lastBlock = blockChain.getBlockChains().getLast();
        var newBlock = new Block(BlockChain.getNextId(), newTransactions, lastBlock.getHash());

        this.mine(newBlock);

        reward += Constants.REWARD;
        blockChain.addBlock(newBlock);
    }
}
