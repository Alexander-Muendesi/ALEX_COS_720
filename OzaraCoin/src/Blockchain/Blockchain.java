package Blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> blockchain;
    private Sha256 sha256;

    public Blockchain(){
        this.sha256 = new Sha256();
        blockchain = new ArrayList<Block>();
    }

    /**
     * This method checks if the blockchain is valid. It goes through 5 steps
     * step 1: Verify the integrity of the blocks: checks that each block has not been tempered with by verifying the hash of the previous block
     *          and the current block
     * step 2: Check the PoW. verifies that the PoW algorithm has been executed correctly for each block
     * step 3: Validate Transactions: by verifying the digital signatures and that the sender has enough funds to complete
     *          the transaction.
     * Step4: Check for double spending. Ensure that there are no double spending attempts within the blockchain. If a transaction has
     *          been included in a block it cannot be included in another block
     * @return True if the blockchain is valid and false if blockchain is not valid
     */
    public boolean isChainValid(){
        int counter = 0;
        Block prev = null;

        //Step 1
        for(Block b: blockchain){
            if(counter != 0){//skip the genesis block as it cannot be verified
                String data = prev.getHash() + b.getTree().getRootHash() + b.getTimeStamp() + b.getNonce();
                String result = sha256.hash(data);

                if(!result.equals(b.getHash()))
                    return false;  

                prev = b;
            }
            else{
                counter++;
                prev = b;
            }
        }
        counter = 0;
        //step 2
        for(Block block: blockchain){
            if(counter !=0){
                // String target = new String(new char[block.getDifficulty()]).replace('\n','0');
                String target = "";
                for(int i=0;i<block.getDifficulty();i++)
                    target += "0";

                int nonce = 0;
                String data = block.getPrevHash() + block.getTree().getRootHash() + block.getTimeStamp() + nonce;
                String hash = sha256.hash(data);

                while(!hash.substring(0,block.getDifficulty()).equals(target)){
                    hash = sha256.hash(data);
                    nonce++;
                    data = block.getPrevHash() + block.getTree().getRootHash() + block.getTimeStamp() + nonce;
                }

                if(!hash.equals(block.getHash())){
                    return false;
                }
            }
            else
                counter++;
        }
        //step 3: Done when transactions are being added to the block
        
        //step 4: Done already in mempool
        return true;
    }

    /**
     * Method used to add a block to the blockchain
     * @param block Block to be added to the blockchain
     */
    public void addBlock(Block block){
        blockchain.add(block);
    }

    /**
     * Getter for all the blocks in the blockchain
     * @return A list representing all the blocks in the blockchain
     */
    public List<Block> getBlockchain(){
        return this.blockchain;
    }
}
