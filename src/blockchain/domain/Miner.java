package blockchain.domain;

import blockchain.Constants;
import blockchain.domain.transactions.Transaction;

import java.util.LinkedList;
import java.util.List;

public class Miner {

    private static final String leadingZeros = "0".repeat(Constants.Difficulty);
    private final BlockChain blockChain;

    private double reward;

    private final List<Transaction> memPool;

    public Miner(BlockChain blockChain) {
        this.blockChain = blockChain;
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
                this.createBlock(memPool, blockChain);
            }
        }
    }

    private void createBlock(List<Transaction> newTransactions, BlockChain blockChain) {

        var lastBlock = blockChain.getBlockChains().getLast();
        var newBlock = new Block(blockChain.getNextId(), new LinkedList<>(newTransactions), lastBlock.getHash());

        this.mine(newBlock);

        reward += Constants.REWARD;
        blockChain.addBlock(newBlock);
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
