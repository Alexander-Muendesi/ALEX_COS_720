package Blockchain;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

//need to implement a way to sign a transaction with a digital transaction to 
//ensure its authenticity
public class Transaction {
    private final String sender;
    private final String receiver;
    private final double amount;
    private final String transactionId;
    private final String transactionSignature;
    private final PublicKey senderPublicKey;
    private final PublicKey receiverPublicKey;

    /**
     * 
     * @param amount
     * @param transactionId
     * @param transactionSignature
     * @param senderPublicKey
     * @param receiverPublicKey
     * @param senderAddress
     * @param receiverAddress
     */
    public Transaction(double amount, String transactionId,String transactionSignature, 
                        PublicKey senderPublicKey, PublicKey receiverPublicKey, String senderAddress, String receiverAddress){
        // this.sender = sender;
        this.sender = senderAddress;
        // this.receiver = receiver;
        this.receiver = receiverAddress;
        this.amount = amount;

        this.transactionId = transactionId;
        this.transactionSignature = transactionSignature;

        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;

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

    public String getTrasactionId(){
        return this.transactionId;
    }

    public String getTransactionSignature(){
        return this.transactionSignature;
    }

    public String getData(){
        return getSender() + " " + getReceiver() + " " + getAmount() + " " + getTrasactionId(); 
    }

    /**
     * This method checks whether the Digital signature for the transaction is valid using the following steps
     * Step 1: Get the senders public Key
     * Step 2: Compute hash of transaction data excluding the digital signature with sha256
     * Step 3: Use public key to verify signature by decrypting the signature using the public key and comparing the result with the hash of the transaction data
     *          If the decryped signature matches the hash, then the digital signature is considered valid
     * @return
     */
    public boolean isValidSignature(){
        Sha256 sha256 = new Sha256();
        String hash = sha256.hash(getData());

        byte[] encryptedData = transactionSignature.getBytes();

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, senderPublicKey);

            byte[] decryptedData = cipher.doFinal(encryptedData);
            String result = new String(decryptedData);

            if(!hash.equals(result))
                return false;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return false;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return false;
        }
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
