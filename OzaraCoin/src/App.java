import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



import Blockchain.Block;
import Blockchain.Blockchain;
import Blockchain.Mempool;
import Organization.CertificateAuthority;
import Organization.Person;


public class App {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        CertificateAuthority authority = new CertificateAuthority();
        //Initialization of blockchain and other small stuff

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");//pass this a parameter to Peers
        Random random2 = new Random(6079);
        KeyPair pair = null;

        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            // keygen.initialize(1024,random);
            keygen.initialize(2048);
            pair = keygen.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String founderAddress = UUID.randomUUID().toString();

        Block genesisBlock = new Block("Genesis");
        Blockchain blockchain = new Blockchain();//create a central blockchain
        blockchain.addBlock(genesisBlock);

        Mempool mempool = new Mempool(blockchain.getBlockchain());//central mempool

        Person alice = new Person("Alice", random, random2, authority);
        System.out.println("User registered? : "+ authority.registerUser(alice.getAddress()));
        /*Person bob = new Person("Bob", random, random2, authority);
        Person peter = new Person("Peter", random, random2, authority);

        alice.applyForDigitalCertificate(authority.createCertificate(alice.getPublicKey(),alice.getAddress()));
        bob.applyForDigitalCertificate(authority.createCertificate(bob.getPublicKey(),bob.getAddress()));
        peter.applyForDigitalCertificate(authority.createCertificate(peter.getPublicKey(),peter.getAddress()));

        System.out.println(alice.verifyIndividualPublicKey(bob.getPublicKey(),bob.getAddress()));*/
        
        /* 

        alice.addPeer(bob.getPublicKey(),bob.getAddress());
        alice.addPeer(peter.getPublicKey(), peter.getAddress());

        bob.addPeer(alice.getPublicKey(),alice.getAddress());
        bob.addPeer(peter.getPublicKey(), alice.getAddress());

        peter.addPeer(alice.getPublicKey(), alice.getAddress());
        peter.addPeer(bob.getPublicKey(), bob.getAddress());

        //bootstrap the cryptocurrency by creating some coinbase transactions and allocating the first few coins to certain addresses.
        genesisBlock.addTransaction(alice.getCoinbaseTransaction(pair.getPublic(), founderAddress));
        genesisBlock.addTransaction(bob.getCoinbaseTransaction(pair.getPublic(), founderAddress));
        genesisBlock.addTransaction(peter.getCoinbaseTransaction(pair.getPublic(), founderAddress));

        genesisBlock.calculateHash();
        genesisBlock.mineBlock();

        int counter = 1;
        Block block = new Block(genesisBlock.getHash());

        //simulate some transactions between the users and adding of blocks to the blockchain
        while(counter <= 10000){
            if(counter % 2000 == 0){
                //mine a block
                System.out.println("Block size: " + mempool.geTransactions().size());
                block.addTransactions(mempool.geTransactions());
                block.calculateHash();

                double reward = block.mineBlock();
                int temp = random2.nextInt(3);
                if(temp == 0)
                    peter.addMoney(reward);
                else if(temp == 1)
                    alice.addMoney(reward);
                else
                    bob.addMoney(reward);

                mempool.removeTransactions();
                blockchain.addBlock(block);
                block = new Block(block.getHash());
            }
            else{//simulate creation of transactions
                int tempIndex = random.nextInt(3);

                if(tempIndex == 0){
                    boolean r = mempool.addTransaction(peter.createTransaction());
                    if(r)peter.addMoney(peter.lastTransactionAmount * -1);
                }
                else if(tempIndex == 1){
                    boolean r = mempool.addTransaction(alice.createTransaction());
                    if(r)alice.addMoney(-1 * alice.lastTransactionAmount);
                }
                else if(tempIndex == 2){
                    boolean r = mempool.addTransaction(bob.createTransaction());
                    if(r)bob.addMoney(bob.lastTransactionAmount * -1);  
                }
            }
            counter++;
        }
        System.out.println("Is blockchain valid: " + blockchain.isChainValid());
        System.out.println("Blockchain length: " + blockchain.getBlockchain().size());*/

    }

    public static void sendMail(){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // replace with your SMTP server
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", "burnerdevelopment8@gmail.com");
        // props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        // props.put("mail.smtp.ssl.ciphersuites", "TLS_AES_128_GCM_SHA256");
              
        

        Session session = Session.getDefaultInstance(props);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("burnerdevelopment8@gmail.com")); // replace with your email address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("burnerdevelopment8@gmail.com")); // replace with recipient email address
            message.setSubject("Java Email Test 2");
            message.setText("Hello from Java!");

            // Send the email
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "burnerdevelopment8@gmail.com", "ytdpcqxpvkqxfsgy"); // replace with your email address and password
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//     private static void sendMail(){
//         Properties props = new Properties();
//         props.put("mail.smtp.host", "smtp.gmail.com"); // replace with your SMTP server
//         props.put("mail.smtp.port", "587");
//         props.put("mail.smtp.user", "burnerdevelopment8@gmail.com");

//         props.put("mail.smtp.auth", "true");

//         props.put("mail.smtp.starttls.enable", "true");
         
//         props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//         props.put("mail.smtp.ssl.protocols", "TLSv1.2");
//         props.put("mail.smtp.ssl.ciphersuites", "TLS_AES_128_GCM_SHA256");
      

//         Session session = Session.getDefaultInstance(props);


  
//       try {
//         Message message = new MimeMessage(session);
//         message.setFrom(new InternetAddress("burnerdevelopment8@gmail.com")); // replace with your email address
//         message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("burnerdevelopment8@gmail.com")); // replace with recipient email address
//         message.setSubject("Java Email Test");
//         message.setText("Hello from Java!");

//         // Send the email
//         Transport transport = session.getTransport("smtp");
//         transport.connect("smtp.gmail.com", "burnerdevelopment8@gmail.com", "ytdpcqxpvkqxfsgy"); // replace with your email address and password
//         transport.sendMessage(message, message.getAllRecipients());
//         transport.close();

//         System.out.println("Email sent successfully.");
//       } catch (Exception e) {
//         e.printStackTrace();
//       }  
//    }  
   //email: burnerdevelopment8@gmail.com
   
    
}
