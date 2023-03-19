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

    public String receiver(){
        return this.receiver;
    }

    public double amount(){
        return this.amount;
    }
}
