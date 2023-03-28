import java.util.List;

//acts as a Peer Node in the cryptocurrency network
//This will be an unstructured P2P network
public class Peer {
    private String address;
    private int port;
    private List<String> peers;//addresses of other peers discovered or connected to curr peer
}
