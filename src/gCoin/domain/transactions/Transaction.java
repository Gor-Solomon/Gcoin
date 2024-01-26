package gCoin.domain.transactions;

import gCoin.Constants;
import gCoin.services.CryptographyService;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.stream.Collectors;

public class Transaction {

    // SHA-256
    private final String id;

    // We use public keys to reference the sender & receivers.

    private PublicKey sender;

    private PublicKey receiver;

    // the amount of the coin that the sender is sending to the receiver.
    private double amount;

    // make sure the transaction is signed to prevent anyone else from spending the coins.
    private byte[] signature;

    private List<TransactionInput> inputs;

    private List<TransactionOutput> outputs;

    public Transaction(PublicKey sender, PublicKey receiver, double amount, List<TransactionInput> inputs, List<TransactionOutput> outputs) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        this.outputs = outputs;
        this.id = generateId();
    }

    public String getId() {
        return id;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public PublicKey getReceiver() {
        return receiver;
    }

    public void setReceiver(PublicKey receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public byte[] getSignature() {
        return signature;
    }


    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutput> getOutputs(PublicKey receiver) {
        return receiver == null ? outputs : outputs.stream().filter(t -> t.getReceiver() == receiver).collect(Collectors.toList());
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }

    public void generateSignature(PrivateKey privateKey){
        String signatureDigest = getHashDigest("");
        this.signature = CryptographyService.sign(privateKey, signatureDigest);
    }

    public boolean verifySignature(){
        String signatureDigest = getHashDigest("");
        return CryptographyService.verify(this.sender, signatureDigest, signature);
    }

    public boolean commitTransaction(){

        if (!verifySignature()){
            System.out.println("Invalid transaction because of invalid signature for id" + this.id);
            return false;
        }

        if (getInputAmount() != getOutputAmount()){
            System.out.println("Input and output amount does not add up to each other, please balance the transaction" + this.id);
            return false;
        }

        return true;
    }

    public String getHash() {
        var digest = getHashDigest("");
        return CryptographyService.generateHash(digest);
    }
    private String generateId() {
        var hashInput = this.sender.toString() + this.receiver.toString() + this.amount;
        return CryptographyService.generateHash(hashInput);
    }

    public String getHashDigest(String space) {
        var newspaper = space.isEmpty() ? "" : space + "    ";
        return newspaper + "{\n" + newspaper + "   id: '" + this.id + "',\n" +
                newspaper + "   Sender: '" + Constants.Wallet_Names.get(this.sender) + "',\n"+
                newspaper + "   Receiver: '" + Constants.Wallet_Names.get(this.receiver) + "',\n"+
                newspaper + "   Amount: " +  this.amount + ",\n"+
                newspaper + "   Inputs: [\n" + String.join(",\n", this.inputs.stream().sorted(Comparator.comparing(TransactionInput::getId)).map(t -> t.getHashDigest(newspaper)).collect(Collectors.toList())) + "],\n"+
                newspaper + "   Outputs: [\n" +String.join(",\n", this.outputs.stream().sorted(Comparator.comparing(TransactionOutput::getId)).map(t -> t.getHashDigest(newspaper)).collect(Collectors.toList()))+ "]\n" + newspaper + "}";
    }


    // This is how we calculate that how much money the sender has, we have to consider all transactions in the past.
    private double getInputAmount(){
        return this.inputs.stream().mapToDouble(TransactionInput::getAmount).sum();
    }

    private double getOutputAmount(){
        return this.outputs.stream().mapToDouble(TransactionOutput::getAmount).sum();
    }
}
