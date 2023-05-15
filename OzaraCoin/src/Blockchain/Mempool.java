package Blockchain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Organization.Person;

public class Mempool {
    private List<Transaction> transactions;
    private List<Block> blockchain;
    public static Map<String, Person> registeredUsers;//string= address

    public Mempool(List<Block> blockchain){
        this.transactions = new ArrayList<Transaction>();
        this.blockchain = blockchain;
        registeredUsers = new HashMap<String,Person>();
    }

    public void addUser(Person person){
        registeredUsers.put(person.getAddress(), person);
    }

    public boolean addTransaction(Transaction transaction){
        if(isValidTransaction(transaction)){
            transactions.add(transaction);
            return true;
        }
        else{
            return false;
        }
    }

    public void removeTransaction(Transaction transaction){
        transactions.remove(transaction);
    }

    public List<Transaction> geTransactions(){
        return this.transactions;
    }

    public void removeTransactions(){
        transactions = new ArrayList<Transaction>();
    }

    /**
     * This method checks whether a transaction is valid before it is entered into a mempool.
     * It first checks whether the digital signature is valid. Then it checks whether the sender has enough funds to carry out the transaction
     * @param transaction
     * @return True if the transation is valid else false.
     */
    private Boolean isValidTransaction(Transaction transaction){
        //check all required fields are present
        if(transaction.getSender() == null || transaction.getReceiver() == null || transaction.getAmount() <=0 || 
            transaction.getTrasactionId() == null){
            return false;
        }

        //check if the sender is registered with CA
        if(!registeredUsers.containsKey(transaction.getSender()))
            return false;

        //check if the transaction signature is valid
        if(!transaction.isValidSignature())
            return false;

        //next we have to check whether the sender has enough funds to carry out the transaction. Need to implement tha UTXO class first though
        Person sender = registeredUsers.get(transaction.getSender());
        if(sender.getMoney() >= transaction.getAmount())
            sender.addMoney(-1 * transaction.getAmount());
        else
            return false;

        //check for double spending
        if(transaction.getTransactionNonce() != Person.accountNonce)
            return false;
        Person.incrementAccountNonce();

        return true;
    }
}
