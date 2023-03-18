public class Block {
    private String hash;
    private String prevHash;
    private MerkleTree tree;//this will have access to merkle root
    private int difficulty;//for mining purposes
    private Long timestamp;
    private int nonce;
    private final int maxTransactions = 5000;

    public Block(String prevHash){
        this.prevHash = prevHash;
        timestamp = System.currentTimeMillis();
        this.nonce = 0;
    }

    public void calculateHash(){

    }

    public String getHash(){

    }

    public void setHash(String hash){

    }

    public String getPrevHash(){

    }

    public void setPrevHash(String prevHash){

    }

    public void mineBlock(){

    }

    public void incrementNonce(){

    }

    public void addTransaction(Transaction transaction){
        
    }
}
