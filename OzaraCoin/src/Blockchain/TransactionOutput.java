package Blockchain;

import java.security.PublicKey;

public class TransactionOutput {
    private double value;
    private PublicKey recipient;

    public TransactionOutput(double value, PublicKey recipient){
        this.value = value;
        this.recipient = recipient;
    }

    public double getValue(){
        return this.value;
    }

    public PublicKey getRecipient(){
        return this.recipient;
    }
}
