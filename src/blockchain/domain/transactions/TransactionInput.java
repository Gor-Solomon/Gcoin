package blockchain.domain.transactions;

import java.util.UUID;

public class TransactionInput {

    private final UUID id;

    // Every input has an output, this is the id of output
    public String transactionOutputId;

    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
        this.id = UUID.randomUUID();
    }

    public UUID getId() { return id; }

    public double getAmount() { return this.UTXO.getAmount(); }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public void setTransactionOutputId(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public TransactionOutput getUTXO() {
        return this.UTXO;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }

    public String getHashDigest(){
        return "{\n" +
                "'id': " + this.id + ",\n" +
                "'TransactionOutputId': " + this.transactionOutputId + "}\n";
    }
}
