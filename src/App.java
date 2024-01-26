import blockchain.domain.BlockChain;
import blockchain.domain.Miner;
import blockchain.Constants;
import blockchain.domain.Wallet;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());

        BlockChain blockChain = new BlockChain();
        var miner = new Miner(blockChain);
        List<Wallet> luckyWallets = new ArrayList<>(List.of(new Wallet("Gorun"),  new Wallet("Vasil"),  new Wallet("Ahmed")));

        for (var luckyWallet : luckyWallets)
        {
            var txn = Constants.GENESIS_Wallet.createTransaction(luckyWallet.getPublicKey(), 10, blockChain);
            miner.addTransaction(txn);
        }

        var coins = 1;
        var birthdayBoy = new Wallet("Birthday Boy");
        for (var luckyWallet : luckyWallets)
        {
            var txn = luckyWallet.createTransaction(birthdayBoy.getPublicKey(), coins++, blockChain);
            miner.addTransaction(txn);
        }

        luckyWallets.add(birthdayBoy);

        System.out.println(blockChain.generateHashDigest(" "));

        for (var luckyWallet : luckyWallets)
        {
            System.out.println();
            System.out.println(luckyWallet.getInfo(blockChain));
        }

        System.out.println();
        System.out.println(miner);

    }

}
