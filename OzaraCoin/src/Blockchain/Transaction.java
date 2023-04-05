package Blockchain;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

//need to implement a way to sign a transaction with a digital transaction to 
//ensure its authenticity
public class Transaction {
    private String sender;
    private String receiver;
    private double amount;
    private double fee;
    private String transactionId;
    private String transactionSignature;

    public Transaction(String sender, String receiver, double amount,double fee, String transactionId,String transactionSignature){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;

        this.fee = fee;
        this.transactionId = transactionId;
        this.transactionSignature = transactionSignature;
    }

    public String getSender(){
        return this.sender;
    }

    public String getReceiver(){
        return this.receiver;
    }

    public double getAmount(){
        return this.amount;
    }

    public double getFee(){
        return this.fee;
    }

    public String getTrasactionId(){
        return this.transactionId;
    }

    public String getTransactionSignature(){
        return this.transactionSignature;
    }

    public String getData(){
        return getSender() + " " + getReceiver() + " " + getAmount() +
        getFee() + " " + getTrasactionId() + " " + getTransactionSignature(); 
    }

    public boolean isValidSignature(){
        //TODO: implement code to validate signature

        return true;
    }

    /**
     * This method first converts each of the fields of a transaction into a byte array.
     * The ByteOutputStream is then used to concatenate the byte arrays. This will be used in the calculation of fees
     * @return A byte array representing the fields of a transaction
     */
    public int getTransactionBytesSize(){
        byte[] senderBytes = sender.getBytes(StandardCharsets.UTF_8);
        byte[] receiverBytes = sender.getBytes(StandardCharsets.UTF_8);
        byte[] amountBytes = String.valueOf(amount).getBytes(StandardCharsets.UTF_8);
        byte[] transactionIdBytes = transactionId.getBytes(StandardCharsets.UTF_8);
        byte[] transactionSignatureBytes = transactionSignature.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            outputStream.write(senderBytes);
            outputStream.write(receiverBytes);
            outputStream.write(amountBytes);
            outputStream.write(transactionIdBytes);
            outputStream.write(transactionSignatureBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray().length;
    }
}
