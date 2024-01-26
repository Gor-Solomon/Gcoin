package gCoin.domain;

import gCoin.Constants;
import gCoin.domain.transactions.Transaction;

import java.util.LinkedList;
import java.util.List;

public class Miner {

    private static final String leadingZeros = "0".repeat(Constants.Difficulty);
    private final BlockChainRegistrar blockChainRegistrar;

    private double reward;

    private final List<Transaction> memPool;

    public Miner(BlockChainRegistrar blockChainRegistrar) {
        this.blockChainRegistrar = blockChainRegistrar;
        this.memPool = new LinkedList<>();
    }

    public double getReward(){
        return this.reward;
    }

    public Block mine(Block block){

        while (!isGoldenHash(block)){
            block.incrementNonce();
            block.updateHash();
        }

        return block;
    }

    public void addTransaction(Transaction transaction){

        if (transaction != null){

            this.memPool.add(transaction);

            if (memPool.size() > Constants.Mempool_Threshold){
                this.createBlock(memPool, blockChainRegistrar);
            }
        }
    }

    private void createBlock(List<Transaction> newTransactions, BlockChainRegistrar blockChainRegistrar) {

        var lastBlock = blockChainRegistrar.getBlockChains().getLast();
        var newBlock = new Block(blockChainRegistrar.getNextId(), new LinkedList<>(newTransactions), lastBlock.getHash());

        this.mine(newBlock);

        reward += Constants.REWARD;
        blockChainRegistrar.addBlock(newBlock);
        newTransactions.clear();
    }

    private boolean isGoldenHash(Block block){
        return block.getHash().startsWith(leadingZeros);
    }

    @Override
    public String toString() {
        return "Miner{" +
                "\nreward=" + reward +
                "\n}";
    }
}
