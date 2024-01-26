package blockchain.domain;

import blockchain.domain.transactions.Transaction;
import blockchain.domain.transactions.TransactionOutput;
import blockchain.services.CryptographyService;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Block {

    private final int id;

    private int nonce = -1;

    private final long timeStamp;

    private String merkelRoot;

    private String hash;

    private final String previousHash;

    private final List<Transaction> transactions;

    public Block(int id, List<Transaction> transactions, String previousHash){
        this.id = id;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.merkelRoot = calculateMerkelRoot();
        updateHash();
    }

    public void incrementNonce(){
        this.nonce++;
    }

    public List<TransactionOutput> getUTXO(PublicKey publicKey) {
        return transactions.stream().flatMap(t -> t.getOutputs(publicKey).stream()).collect(Collectors.toList());
    }

    public void updateHash(){
        var dataToHash = this.id + this.previousHash + this.timeStamp + this.nonce + this.merkelRoot;
        this.hash = CryptographyService.generateHash(dataToHash);
    }

    public String getHash() {
        return hash;
    }

    public String getHashDigest(String space) {
        var newSpace = space.isEmpty()? "" : space + "    ";
        var transactionsInfo =  this.transactions.stream().map(t -> t.getHashDigest(newSpace)).collect(Collectors.joining(",\n"));
        return  newSpace + "{\n" +
                newSpace +  "   id: "             + this.id + "," + '\n' +
                newSpace + "   merkelRoot: '"     + this.merkelRoot + "'," + '\n' +
                newSpace + "   hash: '"           + this.hash + "'," + '\n' +
                newSpace + "   previousHash: '"   + this.previousHash + "'," + '\n' +
                newSpace + "   transactions: [\n" + transactionsInfo + "]\n" + newSpace + "}";
    }


    private String calculateMerkelRoot() {
        var merkelRoot = new MerkleTree(this.transactions);
        return merkelRoot.getMerkelRoot();
    }

}
