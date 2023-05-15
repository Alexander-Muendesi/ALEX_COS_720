package Blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Organization.Person;

import java.util.Map;

public class Block {
    private String hash;
    private String prevHash;

    private MerkleTree tree;//this will have access to merkle root
    private int difficulty=1;//for mining purposes

    private Long timestamp;
    private int nonce;

    private final int maxTransactions = 2000;
    private List<Transaction> transactions;

    private final Sha256 hasher;
    private final int MAX_DIFFICULTY = 10;
    private Map<String,Double> userMoney;

    /**
     * Constructor which takes as a parameter the previous hash of a block.
     * @param prevHash
     */
    public Block(String prevHash, int blockchainLength){
        this.prevHash = prevHash;
        timestamp = System.currentTimeMillis();
        this.nonce = 0;
        transactions = new ArrayList<Transaction>();
        hasher = new Sha256();
        userMoney = new HashMap<String,Double>();

        int val = ((int)Math.floor(blockchainLength/10000));
        this.difficulty = Math.min(10, val);
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

    /**
     * This method is similar to the calculateHash() method above but is used when mining only.
     */
    public void mininingCalculateHash(){
        String data = prevHash + tree.getRootHash() + Long.toString(timestamp) + Integer.toString(nonce);
        this.hash = hasher.hash(data);
    }

    /**
     * Getter to return the timestamp of a block
     * @return String representation of the timestamp value of a block
     */
    public String getTimeStamp(){
        return Long.toString(timestamp);
    }

    /**
     * Getter for the current nonce of the block
     * @return String representation of the nonce value.
     */
    public String getNonce(){
        return Integer.toString(nonce);
    }

    /**
     * Getter to return the hash of a block
     * @return String representation of the hash of a block
     */
    public String getHash(){
        return this.hash;
    }

    /**
     * Setter method used to update the hash of a block
     * @param hash String representation of a hash that was calculated.
     */
    public void setHash(String hash){
        this.hash = hash;
    }

    /**
     * Getter to get the previous hash of the current block
     * @return String representation of the previous hash of a block
     */
    public String getPrevHash(){
        return this.prevHash;
    }

    /**
     * Setter method to update the value of the prevHash attribute
     * @param prevHash String value used to replace the prevHash
     */
    public void setPrevHash(String prevHash){
        this.prevHash = prevHash;
    }

    /**
     * Getter method
     * @return The root of the merkle tree
     */
    public MerkleTree getTree(){
        return this.tree;
    }

    /**
     * Getter for the mining difficulty of a block
     * @return Integer representing the mining difficulty of a block.
     */
    public int getDifficulty(){
        return this.difficulty;
    }

    /**
     * Setter method to update the mining difficulty
     * @param difficulty
     */
    public void setDifficulty(int difficulty){
        this.difficulty = (difficulty <= MAX_DIFFICULTY) ? difficulty : MAX_DIFFICULTY;
    }

    /**
     * This method mines a new block. The goal is to find a hash that starts "difficulty" many 0's.
     * Basically proof of work concept.
     */
    public int mineBlock(){
        //the process of creating a new block of transactions through solving a cryptographic puzzle.
        String target = "";
        for(int i=0;i<difficulty;i++)
            target += "0";

        while(!hash.substring(0,difficulty).equals(target)){
            nonce++;
            mininingCalculateHash();
        }
        calculateUserMoney();
        System.out.println("Block mined!");

        Random random = new Random();
        return random.nextInt(1, 2000);//reward a miner with some cryptocurrency in the range 1-15
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

    /**
     * Setter to update the list of transactions in the current block
     * @param transactions
     */
    public void addTransactions(List<Transaction> transactions){
        this.transactions = transactions;
    }

    /**
     * Getter method for the list of transaction in the block
     * @return List representing all the transactions of a block
     */
    public List<Transaction> getTransactions(){
        return this.transactions;
    }

    public void calculateUserMoney(){
        for(Transaction transaction: transactions){//calculate how much each person has received
            if(userMoney.containsKey(transaction.getReceiver())){
                userMoney.put(transaction.getReceiver(), userMoney.get(transaction.getReceiver()) + transaction.getAmount());
            }
            else{
                userMoney.put(transaction.getReceiver(), transaction.getAmount());
            }
        }

        /*for(Map.Entry<String,Person> entry: Mempool.registeredUsers.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue().getMoney());
        }*/

        for(Transaction transaction: transactions){//deduct what they have sent to other people to remain with actual balance
            if(userMoney.containsKey(transaction.getSender())){
                userMoney.put(transaction.getSender(), userMoney.get(transaction.getSender()) - transaction.getAmount());
            }
            else{//add a sender if they don't exist yet
                if(Mempool.registeredUsers.containsKey(transaction.getSender()))
                    userMoney.put(transaction.getSender(), Mempool.registeredUsers.get(transaction.getSender()).getMoney() - transaction.getAmount());
            }
        }

        // for(Map.Entry<String,Double> entry: userMoney.entrySet()){
        //     System.out.println(entry.getKey() + " " + entry.getValue());
        // }
    }

    public double getUserMoney(String address){
        return userMoney.get(address);
    }
}
