import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Random;
import java.util.UUID;


import Blockchain.Block;
import Blockchain.Blockchain;
import Blockchain.Mempool;
import Organization.CertificateAuthority;
import Organization.Person;


public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Registeration of 3 users, Alice, Bob and Peter initiated.");
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
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

        Block genesisBlock = new Block("Genesis",1);
        Blockchain blockchain = new Blockchain();//create a central blockchain
        blockchain.addBlock(genesisBlock);

        Mempool mempool = new Mempool(blockchain.getBlockchain());//central mempool
        CertificateAuthority authority = new CertificateAuthority(mempool);


        Person alice = new Person("Alice", random, random2, authority);
        System.out.println("Alice registered? : "+ authority.registerUser(alice));
        Person bob = new Person("Bob", random, random2, authority);
        System.out.println("Bob registered? : " + authority.registerUser(bob));
        Person peter = new Person("Peter", random, random2, authority);
        System.out.println("Peter registered? : " + authority.registerUser(peter));

        alice.applyForDigitalCertificate(authority.createCertificate(alice.getPublicKey(),alice.getAddress()));
        bob.applyForDigitalCertificate(authority.createCertificate(bob.getPublicKey(),bob.getAddress()));
        peter.applyForDigitalCertificate(authority.createCertificate(peter.getPublicKey(),peter.getAddress()));

        System.out.println(alice.verifyIndividualPublicKey(bob.getPublicKey(),bob.getAddress()));
        
        

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

        //update users money based on the bootstrapped block
        alice.addMoney(genesisBlock.getUserMoney(alice.getAddress()));
        bob.addMoney(genesisBlock.getUserMoney(bob.getAddress()));
        peter.addMoney(genesisBlock.getUserMoney(bob.getAddress()));

        int counter = 1;
        Block block = new Block(genesisBlock.getHash(), blockchain.getBlockchainLength());

        //simulate some transactions between the users and adding of blocks to the blockchain
        while(counter <= 10000){
            if(counter % 500 == 0){
                //mine a block
                System.out.println("Number of transactions in block: " + mempool.geTransactions().size());
                block.addTransactions(mempool.geTransactions());
                block.calculateHash();

                double reward = block.mineBlock();
                int temp = random2.nextInt(3);
                if(temp == 0){
                    peter.addMoney(reward);
                    System.out.println("Reward: " + reward);
                    System.out.println("Peter money: " + peter.getMoney());
                }
                else if(temp == 1){
                    alice.addMoney(reward);
                    System.out.println("Reward: " + reward);
                    System.out.println("Alice money: " + alice.getMoney());
                }
                else
                {
                    bob.addMoney(reward);
                    System.out.println("Reward: " + reward);
                    System.out.println("bob money: " + bob.getMoney());
                }

                mempool.removeTransactions();
                blockchain.addBlock(block);
                block = new Block(block.getHash(), blockchain.getBlockchainLength());
                System.out.println();
            }
            else{//simulate creation of transactions
                int tempIndex = random.nextInt(3);

                if(tempIndex == 0){
                    boolean r = mempool.addTransaction(peter.createTransaction());
                }
                else if(tempIndex == 1){
                    boolean r = mempool.addTransaction(alice.createTransaction());
                }
                else if(tempIndex == 2){
                    boolean r = mempool.addTransaction(bob.createTransaction());
                }
            }
            counter++;
        }
        System.out.println("Is blockchain valid: " + blockchain.isChainValid());
        System.out.println("Blockchain length: " + blockchain.getBlockchain().size());

    }  
   //email: burnerdevelopment8@gmail.com
   
    
}
