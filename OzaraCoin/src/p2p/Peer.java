import java.io.IOError;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//acts as a Peer Node in the cryptocurrency network
//This will be an unstructured P2P network
public class Peer {
    private String address;
    private int port;
    private ServerSocket serverSocket;
    private List<Socket> clientSockets;//this represents the connections to other peers

    public Peer(String address, int port){
        this.address = address;
        this.port = port;
        this.clientSockets = new ArrayList<Socket>();
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
                    System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
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
        Socket socket = new Socket(peerAddress, peerPort);
        this.clientSockets.add(socket);
        System.out.println("Connected to peer " + peerAddress + ":" + peerPort);
    }
}
