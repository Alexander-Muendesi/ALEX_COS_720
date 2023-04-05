package Blockchain;

public class MerkleLeaf extends MerkleNode{
    private Transaction transaction;

    public MerkleLeaf(Transaction transaction){
        super();
        this.transaction = transaction;
    }

    public Transaction getTransaction(){
        return this.transaction;
    }

    public void setTransaction(Transaction transaction){
        this.transaction = transaction;
    }

    //need to verify this method is being called and acting correctly for polymorphism
    public String getHash(){
        return transaction.getData();
    }
}
