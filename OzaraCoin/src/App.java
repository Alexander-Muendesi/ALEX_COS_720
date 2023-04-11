import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import Blockchain.Block;
import Blockchain.Blockchain;
import Blockchain.Mempool;
import Blockchain.Transaction;
import Organization.Person;
import PeerToPeer.Peer;
import PeerToPeer.PeerNetwork;


public class App {
    public static void main(String[] args) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");//pass this a parameter to Peers
        Random random2 = new Random(6079);
        KeyPair pair = null;

        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("DSA");
            keygen.initialize(1024,random);
            pair = keygen.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String founderAddress = UUID.randomUUID().toString();

        // Mempool mem = new Mempool();
        // PeerNetwork pn = new PeerNetwork();

        // Peer peer1 = new Peer("localhost", 9000, random, mem, random2);
        // peer1.startServer();
        // Peer peer2 = new Peer("localhost", 9001, random, mem, random2);
        // peer2.startServer();
        // Peer peer3 = new Peer("localhost", 9002, random, mem, random2);
        // peer3.startServer();
        // Peer peer4 = new Peer("localhost", 9003, random, mem, random2);
        // peer4.startServer();
        
        // pn.addPeer(peer1);
        // pn.addPeer(peer2);
        // pn.addPeer(peer3);
        // pn.addPeer(peer4);

        Block genesisBlock = new Block("Genesis");
        Blockchain blockchain = new Blockchain();//create a central blockchain
        blockchain.addBlock(genesisBlock);

        Mempool mempool = new Mempool(blockchain.getBlockchain());//central mempool

        Person alice = new Person("Alice", random, random2);
        Person bob = new Person("Bob", random, random2);
        Person peter = new Person("Peter", random, random2);

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
    }
}
