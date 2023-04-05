package Blockchainn;


public class MerkleNode {
    private String hash;//value of the Hash
    private MerkleNode left;
    private MerkleNode right;

    public MerkleNode(){}

    /**
     * Constructor to initialize the value attribute only whilst setting left and right to null
     * @param hash The value of the computed hash
     */
    public MerkleNode(String hash){
        this.hash = hash;
        left = null;
        right = null;
    }

    /**
     * Constructor to initialize all the attributes of the class
     * @param hash Value of the hash
     * @param left left child of current node
     * @param right Right child of current node
     */
    public MerkleNode(String hash, MerkleNode left, MerkleNode right){
        this.hash = hash;
        this.left = left;
        this.right = right;
    }

    /**
     * Getter method for value
     * @return value of hash
     */
    public String getHash(){
        return this.hash;
    }

    /**
     * Setter method for value
     * @param hash new value of hash
     */
    public void setHash(String hash){
        this.hash = hash;
    }

    /**
     * Getter method for left attribute
     * @return The left child of current node
     */
    public MerkleNode getLeft(){
        return this.left;
    }

    /**
     * Setter method for left attribute
     * @param left new value of left Node
     */
    public void setLeft(MerkleNode left){
        this.left = left;
    }

    /**
     * Getter method for right attribute
     * @return The right attribute of the class
     */
    public MerkleNode getRight(){
        return this.right;
    }

    public void setRight(MerkleNode right){
        this.right = right;
    }
}
