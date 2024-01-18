package myBlockchain.domain;

import java.util.LinkedList;
import java.util.List;

public class BlockChain {

    private static int idSequance = 0;
    private List<Block> blockChains;

    public BlockChain(Block genesisBlock) {
        this.blockChains = new LinkedList<Block>();
        this.blockChains.add(genesisBlock);
    }

    public  void addBlock(Block block){
        this.blockChains.add(block);
    }

    public List<Block> getBlockChains(){
        return this.blockChains;
    }

    public static int getNextId(){
        return idSequance++;
    }

    public int getSize(){
        return this.blockChains.size();
    }

    @Override
    public String toString() {
        var chain = new StringBuilder();

        for (Block block : this.blockChains){
            chain.append(block + "\n");
        }

        return chain.toString();
    }
}
