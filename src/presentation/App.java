package presentation;

import gcoin.domain.BlockChainRegistrar;
import gcoin.domain.Miner;
import gcoin.Constants;
import gcoin.domain.Wallet;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;


public class App {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        BlockChainRegistrar blockChainRegistrar = new BlockChainRegistrar();
        var miner = new Miner(blockChainRegistrar);
        List<Wallet> luckyWallets = new ArrayList<>(List.of(new Wallet("Gorun"),  new Wallet("Vasil"),  new Wallet("Ahmed")));

        for (var luckyWallet : luckyWallets)
        {
            var txn = Constants.GENESIS_Wallet.createTransaction(luckyWallet.getPublicKey(), 10, blockChainRegistrar);
            miner.addTransaction(txn);
        }

        var coins = 1;
        var birthdayBoy = new Wallet("Birthday Boy");
        for (var luckyWallet : luckyWallets)
        {
            var txn = luckyWallet.createTransaction(birthdayBoy.getPublicKey(), coins++, blockChainRegistrar);
            miner.addTransaction(txn);
        }

        luckyWallets.add(birthdayBoy);

        System.out.println(blockChainRegistrar.generateHashDigest(" "));

        for (var luckyWallet : luckyWallets)
        {
            System.out.println();
            System.out.println(luckyWallet.getInfo(blockChainRegistrar));
        }

        System.out.println();
        System.out.println(miner);

    }

}
