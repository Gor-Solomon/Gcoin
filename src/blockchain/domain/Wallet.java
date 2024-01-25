package blockchain.domain;

import blockchain.domain.transactions.Transaction;
import blockchain.domain.transactions.TransactionInput;
import blockchain.domain.transactions.TransactionOutput;
import blockchain.services.CryptographyService;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

public class Wallet {

    // this is just for testing purpose to know who transferred what by name.
    private final String name;
    // user of the network.

    // Used for signature
    private final PrivateKey privateKey;

    // Used for verification
    // address: RIPMD (hash representation) public key, we use to generate the (160) bit point on curve.
    private final PublicKey publicKey;

    public Wallet(String name) {
        var kvp = CryptographyService.ellipticCurveCrypto();
        this.privateKey = kvp.getPrivate();
        this.publicKey = kvp.getPublic();
        this.name = name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    // We are able to transfer money!
    // Miners of blockchain will put this transaction into the blockchain.
    public Transaction transferMoney(PublicKey transferTo, double amount, BlockChain blockChain){
        var balance = calculateBalance(blockChain);

        if (balance < amount){
            throw new RuntimeException("You don't have such amount to transfer!\n top-up your balance");
        }

        double topUp = amount;
        var newTransactionInputs = new ArrayList<TransactionInput>();
        var newTransactionOutputs = new ArrayList<TransactionOutput>();
        var availableUto =blockChain.getUTXO(publicKey);
        availableUto.sort(Comparator.comparingDouble(TransactionOutput::getAmount));

        while (topUp != 0){
            var transactionInput = availableUto.removeFirst();
            var localAmount = transactionInput.getAmount() - topUp;

            if (localAmount < 0){
                TransactionOutput txo = new TransactionOutput(transactionInput.getId(), transferTo, transactionInput.getAmount());
                newTransactionOutputs.add(txo);
                topUp = Math.abs(localAmount);
            }

            if (localAmount > 0){
                TransactionOutput txo = new TransactionOutput(transactionInput.getId(), transferTo, transactionInput.getAmount());
                newTransactionOutputs.add(txo);
                TransactionOutput myTxo = new TransactionOutput(transactionInput.getId(), publicKey, localAmount);
                newTransactionOutputs.add(myTxo);
                topUp = Math.abs(localAmount);
            }

            newTransactionInputs.add(new TransactionInput(newTransactionOutputs.getLast().getId()));
        }

        var transaction = new Transaction(this.publicKey, transferTo, amount, newTransactionInputs, newTransactionOutputs);
        transaction.generateSignature(privateKey);

        var miner = new Miner(blockChain);
        miner.addTransaction(transaction);

        return transaction;
    }

    // ITXOs and consider all the transaction in the past
    public double calculateBalance(BlockChain blockChain){

        var walletTransactions = blockChain.getUTXO(publicKey);

        if (walletTransactions.stream().anyMatch(t -> !t.isOwner(this.publicKey))) {
            throw  new RuntimeException("User " + publicKey.toString() + " Has transaction that doesn't belongs to him");
        }

        return  walletTransactions.stream().mapToDouble(TransactionOutput::getAmount).sum();
    }

    @Override
    public String toString() {
        return "Wallet {" +
                "name='" + name + '\'' +
                ", privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                '}';
    }
}
