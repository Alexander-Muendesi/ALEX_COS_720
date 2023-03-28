//need to implement a way to sign a transaction with a digital transaction to 
//ensure its authenticity
public class Transaction {
    private String sender;
    private String receiver;
    private double amount;

    public Transaction(String sender, String receiver, double amount){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getSender(){
        return this.sender;
    }

    public String getReceiver(){
        return this.receiver;
    }

    public double getAmount(){
        return this.amount;
    }

    public String getData(){
        return getSender() + " " + getReceiver() + " " + getAmount(); 
    }
}
