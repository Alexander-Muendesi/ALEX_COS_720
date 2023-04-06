package Blockchain;

import java.security.PublicKey;

/**
 * This class represents the Unspent Transaction Output in the blockchain.
 */
public class UTXO {
    private String transactionHash;
    private double value;
    private PublicKey recipient;
    private int outputIndex;

    public UTXO(String transactionHash,double value,PublicKey recipient, int outputIndex){
        this.transactionHash = transactionHash;
        this.value = value;
        this.recipient = recipient;
        this.outputIndex = outputIndex;
    }

    public int getOutputIndex(){
        return this.outputIndex;
    }

    public String getTransactionHash(){
        return this.transactionHash;
    }

    public double getValue(){
        return this.value;
    }

    public PublicKey getRecipient(){
        return this.recipient;
    }
}
