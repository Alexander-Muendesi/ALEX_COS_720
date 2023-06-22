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

    public String getHash(){
        return transaction.getData();
    }
}
