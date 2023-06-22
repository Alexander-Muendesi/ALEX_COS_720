package Blockchain;


import java.util.List;

public class MerkleTree {
    private MerkleNode root;
    private List<Transaction> transactions;
    private Sha256 hasher;

    public MerkleTree(List<Transaction> transactions){
        this.transactions = transactions;

        //if the number of transactions is odd, add the last element as a duplicate
        //we need a perfect binary tree for hashing purposes 
        if(this.transactions.size() % 2 != 0)
          this.transactions.add(this.transactions.get(this.transactions.size()-1));  

        hasher = new Sha256();

        //build the Merkle tree
        root = buildTree(transactions);
    }

    public String getRootHash(){
        return root.getHash();
    }

    /**
     * This method takes a list of transactions from the block and builds a Merkle Tree
     * from the transaction data. It uses double hashing to create the intermediate hash values as the tree is being built
     * @param transactions List of transactions from a block
     * @return The root of the tree??
     */
    public MerkleNode buildTree(List<Transaction> transactions){
        // System.out.println("transaction size: " + transactions.size());
        if(transactions.size() == 1)//there is only one transaction so create a leaf node
            return new MerkleLeaf(transactions.get(0));

        int middle = transactions.size() / 2;
        List<Transaction> leftList = transactions.subList(0,middle);
        List<Transaction> rightList = transactions.subList(middle, transactions.size());

        //use recursion to build left and right subtrees
        MerkleNode left = buildTree(leftList);
        MerkleNode right = buildTree(rightList);

        //compute intermediate hash value of parent node
        String hashValue = hasher.hash(hasher.hash(left.getHash() + right.getHash()));

        //create the parent node
        return new MerkleNode(hashValue,left,right);
    }

 }
