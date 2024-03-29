package gcoin.domain;

import gcoin.Constants;
import gcoin.domain.transactions.Transaction;
import gcoin.domain.transactions.TransactionInput;
import gcoin.domain.transactions.TransactionOutput;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlockChainRegistrar {

    private int idSequence = 0;
    private final List<Block> blockChains;

    public BlockChainRegistrar() {
        this.blockChains = new LinkedList<>();
        var genesisBlock = generateGenesisBlock();
        this.blockChains.add(genesisBlock);
    }

    public  void addBlock(Block block){
        this.blockChains.add(block);
    }

    public List<Block> getBlockChains(){
        return this.blockChains;
    }

    public int getNextId(){
        return idSequence++;
    }

    public int getSize(){
        return this.blockChains.size();
    }

    public String generateHashDigest(String space) {
        var chain = new StringBuilder();

        chain.append(space + "[").append('\n');
        for (Block block : this.blockChains){
            chain.append(block.getHashDigest(new String(space))).append(",\n");
        }
        chain.deleteCharAt(chain.length() - 2);
        chain.append(space).append("]");

        return chain.toString();
    }

    public List<TransactionOutput> getUTXO(PublicKey publicKey) {
        return blockChains.stream().flatMap(b -> b.getUTXO(publicKey).stream()).collect(Collectors.toList());
    }

    private Block generateGenesisBlock() {
        var id = this.getNextId();
        List<Transaction> genesisTransaction = generateGenesisTransaction();
        var newBlock = new Block(id, genesisTransaction, Constants.GENESIS_PREVIOUS_HASH);
        var miner = new Miner(this);

        return miner.mine(newBlock);
    }

    private List<Transaction> generateGenesisTransaction() {

        List<Transaction> transactions = new ArrayList<>();
        var txnOutput = new TransactionOutput(Constants.GENESIS_PREVIOUS_Transaction_Id, Constants.GENESIS_Wallet.getPublicKey(), Constants.GENESIS_Transaction_Amount);
        var txnIn = new TransactionInput(null, txnOutput.getId());
        txnIn.setUTXO(txnOutput);

        List<TransactionOutput> outputs = new ArrayList<>(List.of(txnOutput));
        List<TransactionInput> inputs = new ArrayList<>(List.of(txnIn));

        transactions.add(new Transaction(Constants.GENESIS_Wallet.getPublicKey(), Constants.GENESIS_Wallet.getPublicKey(), Constants.GENESIS_Transaction_Amount, inputs, outputs));
        return transactions;
    }

}
