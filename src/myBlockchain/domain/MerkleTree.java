package myBlockchain.domain;

import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

    private final List<String> transactions;

    public MerkleTree(List<String> transactions) {
        this.transactions = transactions;
    }

    public String getMerkelRoot(){

        var copyOfTransactions = new ArrayList<String>(this.transactions);

        if (copyOfTransactions.size() % 2 == 1){
            String tailReplica = copyOfTransactions.getLast();
            copyOfTransactions.addLast(tailReplica);
        }

        var result = construct(copyOfTransactions);

        return result.getFirst();
    }

    private List<String> construct(List<String> transactionChains){

        if (transactionChains.size() > 1){
            var leftTransaction = transactionChains.removeFirst();
            var rightTransaction = transactionChains.removeFirst();
            var newTransaction =  mergeHash(leftTransaction, rightTransaction);
            transactionChains.addLast(newTransaction);
            this.construct(transactionChains);
        }

        return transactionChains;
    }

    private String mergeHash(String leftHash, String rightHash){
        final String concatenatedHash = leftHash + rightHash;
        return SHA256Helper.generateHash(concatenatedHash);
    }
}
