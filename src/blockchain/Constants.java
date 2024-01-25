package blockchain;

import blockchain.domain.Wallet;

public class Constants {

    private Constants(){
    }

    public static  final int Difficulty = 5;

    public static final double REWARD = 6.25;

    public static final String GENESIS_PREVIOUS_HASH = "0000000000000000000000000000000000000000000000000000000000000000";

    public static final String GENESIS_PREVIOUS_Transaction_Id = "0000000000000000000000000000000000000000000000000000000000000000";

    public static final double GENESIS_Transaction_Amount = 1000000000;

    public static final Wallet GENESIS_Wallet = new Wallet("Genesis Wallet");
}
