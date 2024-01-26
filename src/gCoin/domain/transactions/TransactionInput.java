package gCoin.domain.transactions;

import java.util.UUID;

public class TransactionInput {

    private final UUID id;

    private final String referenceTransactionInputId;

    // Every input has an output, this is the id of output
    public String transactionOutputId;

    private TransactionOutput UTXO;

    public TransactionInput(String referenceTransactionInputId, String transactionOutputId) {
        this.id = UUID.randomUUID();
        this.referenceTransactionInputId = referenceTransactionInputId;
        this.transactionOutputId = transactionOutputId;
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

    public String getHashDigest(String space){
        space += space.isEmpty() ? "" : "   ";
        return space  + "{\n" +
                " " + space + "   id: '" + this.id + "',\n" +
                " " + space + "   TransactionInputId: '" + this.referenceTransactionInputId + "',\n" +
                " " + space + "   TransactionOutputId: '" + this.transactionOutputId + "'\n" + space + "}";
    }
}
