package blockchain.domain;

import blockchain.domain.transactions.Transaction;
import blockchain.domain.transactions.TransactionOutput;
import blockchain.services.CryptographyService;

import java.security.PublicKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Block {

    private int id;

    private int nonce = -1;

    private long timeStamp;

    private String merkelRoot;

    private String hash;

    private String previousHash;

    private List<Transaction> transactions;

    public Block(int id, List<Transaction> transactions, String previousHash){
        this.id = id;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.merkelRoot = calculateMerkelRoot();
        this.hash = generateHash();
    }

    public void incrementNonce(){
        this.nonce++;
    }

    public List<TransactionOutput> getUTXO(PublicKey publicKey) {
        return transactions.stream().flatMap(t -> t.getOutputs(publicKey).stream()).collect(Collectors.toList());
    }

    public String generateHash(){
        var dataToHash = this.id + this.previousHash + this.timeStamp + this.nonce + this.merkelRoot;
        return CryptographyService.generateHash(dataToHash);
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        var transactionsInfo = String.join(",\n", this.transactions.stream().map(t -> t.getHashDigest()).collect(Collectors.toList()));
        return "Block {\n" +
                "   id= " + id + "," + '\n' +
                "   hash= '" + hash + "," + '\n' +
                "   previousHash= '" + previousHash + "," + '\n' +
                "   transactions = {\n" + transactionsInfo + "\n}";
    }


    private String calculateMerkelRoot() {
        var merkelRoot = new MerkleTree(this.transactions);
        return merkelRoot.getMerkelRoot();
    }

}
