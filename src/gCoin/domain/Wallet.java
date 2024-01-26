package gCoin.domain;

import gCoin.Constants;
import gCoin.domain.transactions.Transaction;
import gCoin.domain.transactions.TransactionInput;
import gCoin.domain.transactions.TransactionOutput;
import gCoin.services.CryptographyService;

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
        Constants.Wallet_Names.put(this.publicKey, this.name);
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    // We are able to transfer money!
    // Miners of blockchain will put this transaction into the blockchain.
    public Transaction createTransaction(PublicKey transferTo, double amount, BlockChainRegistrar blockChainRegistrar){
        var balance = calculateBalance(blockChainRegistrar);

        if (balance < amount){
            throw new RuntimeException("You don't have such amount to transfer!\n top-up your balance");
        }

        double topUp = amount;
        var newTransactionInputs = new ArrayList<TransactionInput>();
        var newTransactionOutputs = new ArrayList<TransactionOutput>();
        var availableUto = blockChainRegistrar.getUTXO(publicKey);
        availableUto.sort(Comparator.comparingDouble(TransactionOutput::getAmount));

        while (topUp != 0){
            var transactionInput = availableUto.removeFirst();
            var localAmount = transactionInput.getAmount() - topUp;

            // Use oldest transactions first.
            if (localAmount <= 0){
                TransactionOutput txo = new TransactionOutput(transactionInput.getId(), transferTo, transactionInput.getAmount());
                newTransactionOutputs.add(txo);
                topUp = Math.abs(localAmount);
            }

            // Return the change to the sender.
            if (localAmount > 0){
                TransactionOutput myTxo = new TransactionOutput(transactionInput.getId(), publicKey, localAmount);
                newTransactionOutputs.add(myTxo);

                TransactionOutput txo = new TransactionOutput(transactionInput.getId(), transferTo, topUp);
                newTransactionOutputs.add(txo);
                topUp = 0;
            }

            newTransactionInputs.add(new TransactionInput(transactionInput.getId(), newTransactionOutputs.getLast().getId()));
        }

        var transaction = new Transaction(this.publicKey, transferTo, amount, newTransactionInputs, newTransactionOutputs);
        transaction.generateSignature(privateKey);

        return transaction;
    }

    // ITXOs and consider all the transaction in the past
    public double calculateBalance(BlockChainRegistrar blockChainRegistrar){

        var walletTransactions = blockChainRegistrar.getUTXO(publicKey);

        if (walletTransactions.stream().anyMatch(t -> !t.isOwner(this.publicKey))) {
            throw  new RuntimeException("User " + publicKey.toString() + " Has transaction that doesn't belongs to him");
        }

        return  walletTransactions.stream().mapToDouble(TransactionOutput::getAmount).sum();
    }

    public String getInfo(BlockChainRegistrar blockChainRegistrar) {
        return "Wallet{" + "\n" +
                "name='" + name + "\n" +
                "Balance='" + this.calculateBalance(blockChainRegistrar)+ "\n" +
                '}';
    }
}
