package Blockchain;


import java.util.ArrayList;
import java.util.List;

public class Mempool {
    private List<Transaction> transactions;
    private List<Block> blockchain;

    public Mempool(List<Block> blockchain){
        this.transactions = new ArrayList<Transaction>();
        this.blockchain = blockchain;
    }

    public void addTransaction(Transaction transaction){
        if(isValidTransaction(transaction))
            transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction){
        transactions.remove(transaction);
    }

    public List<Transaction> geTransactions(){
        return this.transactions;
    }

    /**
     * This method checks whether a transaction is valid before it is entered into a mempool.
     * It first checks whether the digital signature is valid. Then it checks whether the sender has enough funds to carry out the transaction
     * @param transaction
     * @return True if the transation is valid else false.
     */
    private Boolean isValidTransaction(Transaction transaction){
        //check all required fields are present
        if(transaction.getSender() == null || transaction.getReceiver() == null || transaction.getAmount() <=0 || transaction.getFee() <= 0 || 
            transaction.getTrasactionId() == null){

            return false;
        }

        if(!transaction.isValidSignature())
            return false;

        //next we have to check whether the sender has enough funds to carry out the transaction. Need to implement tha UTXO class first though
        double senderMoney = 0.0;
        double senderSpentMoney = 0.0;

        String senderAddress = transaction.getSender();
        for(Block block: blockchain){
            for(Transaction transactionTwo : block.getTransactions()){
                if(senderAddress.equals(transactionTwo.getReceiver()))//find out how much money the sender has received
                    senderMoney += transactionTwo.getAmount();
                if(senderAddress.equals(transactionTwo.getSender()))//find out how much the sender has spent
                    senderSpentMoney += transactionTwo.getAmount();
            }
        }

        if(senderMoney - senderSpentMoney < transaction.getAmount())//check if the sender can afford to complete the transaction
            return false;

        //check for double spending
        String transactionId = transaction.getTrasactionId();
        for(Block block: blockchain)
            for(Transaction transactionTwo: block.getTransactions())
                if(transactionId.equals(transactionTwo.getTrasactionId()))
                    return false;

        return true;
    }
}
