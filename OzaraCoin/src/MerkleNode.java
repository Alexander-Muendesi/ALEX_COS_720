public class MerkleNode {
    private String value;//value of the Hash
    private MerkleNode left;
    private MerkleNode right;

    /**
     * Constructor to initialize the value attribute only whilst setting left and right to null
     * @param value The value of the computed hash
     */
    public MerkleNode(String value){
        this.value = value;
        left = null;
        right = null;
    }

    /**
     * Constructor to initialize all the attributes of the class
     * @param value Value of the hash
     * @param left left child of current node
     * @param right Right child of current node
     */
    public MerkleNode(String value, MerkleNode left, MerkleNode right){
        this.value = value;
        this.left = left;
        this.right = right;
    }

    /**
     * Getter method for value
     * @return value of hash
     */
    public String getValue(){
        return this.value;
    }

    /**
     * Setter method for value
     * @param value new value of hash
     */
    public void setValue(String value){
        this.value = value;
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
