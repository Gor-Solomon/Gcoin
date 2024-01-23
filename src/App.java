import blockchain.domain.Block;
import blockchain.domain.BlockChain;
import blockchain.domain.Miner;
import blockchain.Constants;
import blockchain.domain.transactions.Transaction;
import blockchain.services.CryptographyService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.Base64;
import java.util.List;

public class App {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        var kvp = CryptographyService.ellipticCurveCrypto();

        System.out.println(Base64.getEncoder().encodeToString(kvp.getPrivate().getEncoded()));
        System.out.println(Base64.getEncoder().encodeToString(kvp.getPublic().getEncoded()));
    }

    private void minerTest(){
        Miner miner = new Miner();

//        var genesisBlock = new Block(BlockChain.getNextId(), (List<Transaction>) new Transaction(null, null, 0), Constants.GENESIS_PREVIOUS_HASH);
//        miner.mine(genesisBlock);
//
//        BlockChain blockChain = new BlockChain(genesisBlock);

//
//        miner.processTransactions("Sendding 1000 Bitcoin from Jack To Jon", blockChain);
//        miner.processTransactions("Sendding 500 Bitcoin from Mery To Peeter", blockChain);
//        miner.processTransactions("Sendding 33.5 Bitcoin from Jon To Kale", blockChain);
//
//        System.out.println(blockChain);
    }

}
