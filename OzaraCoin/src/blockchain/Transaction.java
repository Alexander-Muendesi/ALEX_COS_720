//need to implement a way to sign a transaction with a digital transaction to 
//ensure its authenticity
public class Transaction {
    private String sender;
    private String receiver;
    private double amount;
    private double fee;
    private String transactionId;
    private String transactionSignature;

    public Transaction(String sender, String receiver, double amount,double fee, String transactionId,String transactionSignature){
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;

        this.fee = fee;
        this.transactionId = transactionId;
        this.transactionSignature = transactionSignature;
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

    public double getFee(){
        return this.fee;
    }

    public String getTrasactionId(){
        return this.transactionId;
    }

    public String getTransactionSignature(){
        return this.transactionSignature;
    }

    public String getData(){
        return getSender() + " " + getReceiver() + " " + getAmount() +
        getFee() + " " + getTrasactionId() + " " + getTransactionSignature(); 
    }

    public boolean isValidSignature(){
        //TODO: implement code to validate signature

        return true;
    }
}
