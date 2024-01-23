package blockchain.domain;

import blockchain.domain.transactions.Transaction;
import blockchain.services.CryptographyService;

import java.util.List;
import java.util.stream.Collectors;

public class MerkleTree {

    private final List<Transaction> transactions;

    public MerkleTree(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getMerkelRoot(){

        var inputTransactions = this.transactions.stream().map(t -> t.getHash()).collect(Collectors.toList());

        if (inputTransactions.size() % 2 == 1){
            var tailReplica = inputTransactions.getLast();
            inputTransactions.addLast(tailReplica);
        }

        var result = construct(inputTransactions);

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
        return CryptographyService.generateHash(concatenatedHash);
    }
}
