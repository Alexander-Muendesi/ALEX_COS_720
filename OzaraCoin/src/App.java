import java.security.SecureRandom;
import java.util.Random;

import Blockchain.Mempool;
import Blockchain.Transaction;
import PeerToPeer.Peer;
import PeerToPeer.PeerNetwork;


public class App {
    public static void main(String[] args) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");//pass this a parameter to Peers
        Random random2 = new Random(6079);

        Mempool mem = new Mempool();
        PeerNetwork pn = new PeerNetwork();

        Peer peer1 = new Peer("localhost", 9000, random, mem, random2);
        peer1.startServer();
        Peer peer2 = new Peer("localhost", 9001, random, mem, random2);
        peer2.startServer();
        Peer peer3 = new Peer("localhost", 9002, random, mem, random2);
        peer3.startServer();
        
        pn.addPeer(peer3);
        pn.addPeer(peer2);
        pn.addPeer(peer1);

    }
}
