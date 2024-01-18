package myBlockchain.domain;

import java.util.Date;

public class Block {

    private int id;

    private int nonce = -1;

    private long timeStamp;

    private String hash;

    private String previousHash;

    private String transaction;

    public Block(int id, String transaction, String previousHash){
        this.id = id;
        this.transaction = transaction;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.generateHash();
    }

    public void incrementNonce(){
        this.nonce++;
    }

    public void generateHash(){
        var dataToHash = this.id + this.previousHash + this.timeStamp + this.nonce + this.transaction;
        this.hash = SHA256Helper.generateHash(dataToHash);
    }

    public void updatePreviousHash(String previousHash){
        this.previousHash = previousHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash){
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "Block {\n" +
                "   id= " + id + "," + '\n' +
                "   hash= '" + hash + "," + '\n' +
                "   previousHash= '" + previousHash + "," + '\n' +
                "   transaction= '" + transaction + " }";
    }
}
