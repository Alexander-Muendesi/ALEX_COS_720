import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * This class simulates how the Peers in the P2P network will discover each other. Each peer will be added to a known network and then
 * connected to all other peers. So this P2P network is fully connected.
 */
public class PeerNetwork{
    private List<Peer> peers;

    public PeerNetwork(){
        this.peers = new ArrayList<Peer>();
    }

    /**
     * Method to add a peer to the network and connect the new Peer to the other peers
     * @param peer
     */
    public void addPeer(Peer peer){
        peers.add(peer);

        if(peers.size() > 1){
            for(Peer peerr : peers){
                if(peerr != peer){
                    try{
                        peer.connectToPeer(peerr.getAddress(),peer.getPort());
                        peerr.connectToPeer(peer.getAddress(),peer.getPort());
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Method to remove a peer that wants to disconnet from the network
     * @param peer
     */
    public void removePeer(Peer peer){
        peers.remove(peer);
    }

    /**
     * This method broadcasts a newly mined block to all Peers in the blockchain
     * @param block Newly mined block
     */
    public void broadcast(Block block){

    }
    
    /**
     * This method broadcasts a new transaction to other Peers on the network
     * @param transaction A newly created transaction
     */
    public void broadcast(Transaction transaction){

    }

    /**
     * This methods starts all the server sockets in the network.
     * @throws IOException
     */
    public void start() throws IOException{
        for(Peer peer: peers)
            peer.startServer();
    }

    /**
     * This method stops all the server sockets in the network. This method is mainly to free up ports
     * as without it sometimes the PORTS seem to be in use even if application is not running
     * @throws IOException
     */
    public void stop() throws IOException{
        for(Peer peer: peers)
            peer.stopServer();
    }
}