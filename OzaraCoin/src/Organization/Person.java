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


    public Person(String name,SecureRandom secureRandom, Random random){
        this.name = name;
        this.random = random;

        peers = new ArrayList<String>();
        peersKey = new ArrayList<PublicKey>();

        try {
            keygen = KeyPairGenerator.getInstance("RSA");
            // keygen.initialize(1024,secureRandom);
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

        Transaction t = new Transaction(amount, transactionId, transactionSignature, senderPublicKey, receiverKey, this.address,receiverAddress);
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

        Transaction t = new Transaction(amount, transactionId, transactionSignature, senderPublicKey, this.getPublicKey(),senderAddress,receiverAddress);
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
            // Cipher cipher = Cipher.getInstance("RSA");
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pair.getPrivate());
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            // result = new String(encryptedData);
            result = Base64.getEncoder().encodeToString(encryptedData);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
