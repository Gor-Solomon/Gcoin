package blockchain;

import blockchain.domain.Wallet;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class Constants {

    private Constants(){
    }

    public static  final int Difficulty = 3;

    public static final double REWARD = 50;

    public static final String GENESIS_PREVIOUS_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    public static final String GENESIS_PREVIOUS_Transaction_Id = "0000000000000000000000000000000000000000000000000000000000000000";

    public static final double GENESIS_Transaction_Amount = 50;

    public static final int Mempool_Threshold = 2;

    public static final Map<PublicKey, String> Wallet_Names = new HashMap<>();

    public static final Wallet GENESIS_Wallet = new Wallet("Genesis Wallet");
}
