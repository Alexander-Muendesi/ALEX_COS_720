package PeerToPeer;

import java.io.IOError;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import Blockchain.Block;
import Blockchain.Mempool;
import Blockchain.Transaction;

//acts as a Peer Node in the cryptocurrency network
//This will be an unstructured P2P network
public class Peer {
    private String address;
    private int port;
    private ServerSocket serverSocket;

    private List<Socket> clientSockets;//this represents the connections to other peers
    private KeyPairGenerator keygen;
    private final SecureRandom random;

    private final KeyPair pair;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private Mempool mempool;
    private final int BLOCK_SIZE_LIMIT = 5000;
    private final Random random2;

    //https://www.javatpoint.com/java-digital-signature
    public Peer(String address, int port, SecureRandom random, Mempool mempool, Random random2){
        this.address = address;
        this.port = port;
        this.clientSockets = new ArrayList<Socket>();

        this.random = random;
        try {
            this.keygen = KeyPairGenerator.getInstance("DSA");
            keygen.initialize(1024,random);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        pair = keygen.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();

        this.mempool = mempool;
        this.random2 = random2;
    }

    /**
     * Getter to return the address of the Peer
     * @return The address of the Peer
     */
    public String getAddress(){
        return this.address;
    }

    /**
     * Getter method
     * @return The port of the peer
     */
    public int getPort(){
        return this.port;
    }

    /**
     * This method starts the server socket of the peer and accepts new connections which are then added to its list of clients
     * @throws IOException
     */
    public void startServer() throws IOException{
        this.serverSocket = new ServerSocket(port);

        new Thread(()->{
            while(true){
                try{
                    Socket clientSocket = this.serverSocket.accept();
                    this.clientSockets.add(clientSocket);
                    System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "\n");
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();;
    }

    /**
     * Stops the server from listening to new connections and shuts it down.
     * @throws IOException
     */
    public void stopServer() throws IOException{
        this.serverSocket.close();
    }

    /**
     * Getter method for clientSockets
     * @return List of clientSockets
     */
    public List<Socket> getClients(){
        return this.clientSockets;
    }

    /**
     * This function connects two peers together
     * @param peerAddress Address of peer to connect to
     * @param peerPort Port of peer to connect to
     * @throws IOException
     */
    public void connectToPeer(String peerAddress, int peerPort) throws IOException{
        // System.out.println("Port: " + port + " peerPort: " + peerPort);
        Socket socket = new Socket(peerAddress, peerPort);
        this.clientSockets.add(socket);
        System.out.println(port + " Connected to peer " + peerAddress + ":" + peerPort + "\n");
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
     * Getter method for the public key of a Peer
     * @return
     */
    public PublicKey getPublicKey(){
        return this.publicKey;
    }

    /**
     * This method will create a transaction, generate the signature for it and give it a unique transaction ID;
     * @return
     */
    public Transaction createTransaction(){
        String transactionId = UUID.randomUUID().toString();
        String sender = this.address;
        //TODO: Fix the below fee stuff as well as making it return the proper thing
        // double fee = calculateTransactionFees();//this needs fixing
        return null;
    }

    double calculateTransactionFees(Transaction transaction){
        int transactionSize = transaction.getTransactionBytesSize();
        double congestion = getCurrentNetworkCongestion(mempool.geTransactions().size(), BLOCK_SIZE_LIMIT);

        double baseFee = transactionSize * congestion;

        //10 means highest priority, whilst 0 means lowest priority
        double priorityFee = getPriorityFee(transactionSize,random2.nextInt(10));

        return baseFee + priorityFee;
    }

    /**
     * This method simulates getting the network congestion of the blockchain
     * @param numPendingTransactions The number of transaction in the mempool
     * @param blockSize The max number of transactions eaach block
     * @return a double representing the network congestion
     */
    double getCurrentNetworkCongestion(int numPendingTransactions, int blockSize){
        double ratio = (double) numPendingTransactions / blockSize;

        //cap the congestion ratio to 1 so that greater values are prevented. This is too prevent very high transaction fees
        if(ratio > 1){
            ratio = 1.0;
        }

        double congestion = ratio * ratio;

        return congestion;
    }

    /**
     * This method calculates the priority fee for a transaction by multiplying the transaction size and the
     * priority as a decimal.
     * @param transactionSize
     * @param priority Value ranges from 0 - 9 with 0 being low priority and 9 being highest priority
     * @return
     */
    double getPriorityFee(int transactionSize, int priority){
        double ratio = (double)transactionSize * ((double)priority / 10.0);
        return ratio;
    }
}
