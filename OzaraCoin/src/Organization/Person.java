package Organization;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;

import Blockchain.Sha256;
import Blockchain.Transaction;

public class Person {
    private String name;
    private KeyPairGenerator keygen;
    private KeyPair pair;
    private Random random;
    private List<String> peers;
    private List<PublicKey> peersKey;

    private String address = UUID.randomUUID().toString();
    private double money = 0.0;
    public double lastTransactionAmount = 0.0;
    private CertificateAuthority ca;
    private String digitalCertificate = "";
    public  static double accountNonce = 1.0;//a nonce used to prevent double spending

    /**
     * 
     * @return true if the maximum value of the double has not been reached else false
     */
    public static void incrementAccountNonce(){
        if(accountNonce == Double.MAX_VALUE){
            accountNonce = 1.0;
        }
        accountNonce++;
    }


    public Person(String name,SecureRandom secureRandom, Random random, CertificateAuthority ca){
        this.name = name;
        this.random = random;
        this.ca = ca;

        peers = new ArrayList<String>();
        peersKey = new ArrayList<PublicKey>();

        try {
            keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(2048);
            pair = keygen.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public double getMoney(){
        return this.money;
    }

    public void addMoney(double amount){
        this.money += amount;
    }

    public PublicKey getPublicKey(){
        return pair.getPublic();
    }

    public String getAddress(){
        return this.address;
    }

    public Transaction createTransaction(){
        double amount = random.nextInt(10);
        String transactionId = UUID.randomUUID().toString();
        PublicKey senderPublicKey = pair.getPublic();

        int index = random.nextInt(peers.size());
        String receiverAddress = peers.get(index);
        PublicKey receiverKey = peersKey.get(index);

        String transactionSignature = generateTransactionSignature(Double.toString(amount), transactionId, 
                this.address, receiverAddress);

        Transaction t = new Transaction(amount, transactionId, transactionSignature, senderPublicKey, receiverKey, this.address,receiverAddress,accountNonce);
        return t;

    }

    /**
     * This method creates the initial coinbase transactions of the genesis block
     * @param senderPublicKey Public key of the founder of the network
     * @return
     */
    public Transaction getCoinbaseTransaction(PublicKey senderPublicKey, String senderAddress){
        double amount = random.nextInt(1000);
        lastTransactionAmount = amount;
        String transactionId = UUID.randomUUID().toString();
        String receiverAddress = this.address;
        String transactionSignature = generateTransactionSignature(Double.toString(amount), transactionId, 
                senderAddress, receiverAddress);

        Transaction t = new Transaction(amount, transactionId, transactionSignature, senderPublicKey, this.getPublicKey(),senderAddress,receiverAddress,0.0);
        return t;
    }

    public void addPeer(PublicKey key,String address){
        peers.add(address);
        peersKey.add(key);
    }

    /**
     * This method returns a digital signature of the Transaction using the RSA algorithm
     * @param amount
     * @param transactionId
     * @param sender
     * @param receiver
     * @return
     */
    public String generateTransactionSignature(String amount, String transactionId, String sender, String receiver){
        String data = sender + " " + receiver + " " + amount + " " + transactionId;
        Sha256 hasher = new Sha256();

        data = hasher.hash(data);
        String result = "";

        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pair.getPrivate());
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            result = Base64.getEncoder().encodeToString(encryptedData);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method verifies whether an individuals public key is from the person they claim to be by comparing the hash of the public key
     * and the decrypted version of the digital certificate from the CA
     * @param key The public key from the individual under consideration.
     * @return True if the public key is authentic else false.
     */
    public Boolean verifyIndividualPublicKey(PublicKey key, String individualAddress){
        byte[] keyBytes = key.getEncoded();
        String data = Base64.getEncoder().encodeToString(keyBytes);
        Sha256 hasher = new Sha256();


        data = hasher.hash(data);
        String result = "";

        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, ca.getPublicKey());
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(ca.getIndividualCertificate(individualAddress)));
            result = new String(decryptedData);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return (result.equals(data)) ? true : false;

    }

    public void applyForDigitalCertificate(String certificate){
        this.digitalCertificate = certificate;
    }
}
