package blockchain.domain.transactions;

import blockchain.services.CryptographyService;

import java.security.PublicKey;

public class TransactionOutput {

    // SHA-256
    private final String id;

    // transaction id of the parent where it was created in, for example input transaction X3W has 1 bitcoin to adam, then, this will be  X3W.
    private String parentTransactionId;

    // the receiver of the bitcoin.
    private PublicKey receiver;

    // The amount of the coin sent from the sender to the receiver.
    private double amount;

    public TransactionOutput(String parentTransactionId, PublicKey receiver, double amount) {
        this.parentTransactionId = parentTransactionId;
        this.receiver = receiver;
        this.amount = amount;
        this.id = generateId();
    }

    private String generateId() {
        var hashInput =  getHashDigest();
        return CryptographyService.generateHash(hashInput);
    }

    public boolean isOwner(PublicKey publicKey){
        return publicKey == this.receiver;
    }

    public String getId() {
        return id;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public PublicKey getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }


    public String getHashDigest(){
        return "{\n" +
                "'id': " + this.id + ",\n" +
                "'Receiver': " + this.receiver.toString() + ",\n" +
                "'Amount': " + this.amount + ",\n" +
                "'ParentTransactionId': " + this.parentTransactionId + "}\n";
    }
}
