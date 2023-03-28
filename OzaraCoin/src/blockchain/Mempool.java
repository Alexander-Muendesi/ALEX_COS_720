import java.util.ArrayList;
import java.util.List;

public class Mempool {
    private List<Transaction> transactions;

    public Mempool(){
        this.transactions = new ArrayList<Transaction>();
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

    private Boolean isValidTransaction(Transaction transaction){
        //check all required fields are present
        if(transaction.getSender() == null || transaction.getReceiver() == null || transaction.getAmount() <=0 || transaction.getFee() <= 0 || 
            transaction.getTrasactionId() == null){

            return false;
        }

        if(!transaction.isValidSignature())
            return false;

        //check for double spending
        //TODO: implement double spending logic

        return true;
    }
}
