package Blockchain;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private String hash;
    private String prevHash;

    private MerkleTree tree;//this will have access to merkle root
    private int difficulty;//for mining purposes

    private Long timestamp;
    private int nonce;

    private final int maxTransactions = 2000;
    private List<Transaction> transactions;

    private final Sha256 hasher;

    /**
     * Constructor which takes as a parameter the previous hash of a block.
     * @param prevHash
     */
    public Block(String prevHash){
        this.prevHash = prevHash;
        timestamp = System.currentTimeMillis();
        this.nonce = 0;
        transactions = new ArrayList<Transaction>();
        hasher = new Sha256();
    }

    /**
     * This method calculates the hash of the block by concatentating the following values in order using sha256
     * prevHash + merkle tree root hash + timestamp + nonce
     */
    public void calculateHash(){
        tree = new MerkleTree(transactions);
        String data = prevHash + tree.getRootHash() + Long.toString(timestamp) + Integer.toString(nonce);
        
        this.hash = hasher.hash(data);
    }

    public void mininingCalculateHash(){
        String data = prevHash + tree.getRootHash() + Long.toString(timestamp) + Integer.toString(nonce);
        this.hash = hasher.hash(data);
    }

    public String getTimeStamp(){
        return Long.toString(timestamp);
    }

    public String getNonce(){
        return Integer.toString(nonce);
    }

    public String getHash(){
        return this.hash;
    }

    public void setHash(String hash){
        this.hash = hash;
    }

    public String getPrevHash(){
        return this.prevHash;
    }

    public void setPrevHash(String prevHash){
        this.prevHash = prevHash;
    }

    public MerkleTree getTree(){
        return this.tree;
    }

    public int getDifficulty(){
        return this.difficulty;
    }

    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }
    /**
     * This method mines a new block. The goal is to find a hash that starts "difficulty" many 0's.
     * Takes inspiration from Bitcoin.
     */
    public void mineBlock(){
        //the process of creating a new block of transactions through solving a cryptographic puzzle.
        String target = new String(new char[difficulty]).replace('\n','0');
        while(!hash.substring(0,difficulty).equals(target)){
            mininingCalculateHash();
            nonce++;
        }
        System.out.println("Block mined!");
    }

    /**
     * This method adds transactions to the the blocks list of transactions
     * @param transaction
     * @return Returns true if the transaction was added else false if the block has the maximum number of transactions
     */
    public Boolean addTransaction(Transaction transaction){
        if(transactions.size() < maxTransactions){
            transactions.add(transaction);
            return true;
        }
        return false;
    }

    public void addTransactions(List<Transaction> transactions){
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions(){
        return this.transactions;
    }
}
