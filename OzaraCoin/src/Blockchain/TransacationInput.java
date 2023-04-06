package Blockchain;

public class TransacationInput {
    private String outputTransactionHash;
    private int outputIndex;
    private String signature;
    
    public TransacationInput(String outputTransactionHash, int outputIndex, String signature){
        this.outputTransactionHash = outputTransactionHash;
        this.outputIndex = outputIndex;
        this.signature = signature;
    }

    public String getOutputTransactionHash(){
        return this.outputTransactionHash;
    }

    public int getOutputIndex(){
        return this.outputIndex;
    }

    public String getSignature(){
        return this.signature;
    }
}
