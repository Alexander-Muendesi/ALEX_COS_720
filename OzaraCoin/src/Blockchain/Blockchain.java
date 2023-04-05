package Blockchain;

import java.util.List;

//need to set upper limit on number of OzaraCoins than can exist. 
//something to do with how many coins a miner gets rewarded with etc
public class Blockchain {
    private List<Block> blockchain;
    private Sha256 sha256;

    public Blockchain(){
        this.sha256 = new Sha256();
    }

    /**
     * This method checks if the blockchain is valid. It goes through 5 steps
     * step 1: Verify the integrity of the blocks: checks that each block has not been tempered with by verifying the hash of the previous block
     *          and the current block
     * step 2: Check the PoW. verifies that the PoW algorithm has been executed correctly for each block
     * step 3: Validate Transactions: by verifying the digital signatures and that the sender has enough funds to complete
     *          the transaction.
     * step 4: Check the consensus: verify that the blockchain is consenus by with other peers in the network by comparing
     *          the blockchain of the peer with the other peers in the network and ensuring that they all agree on the longest chain
     * Step5: Check for double spending. Ensure that there are no double spending attempts within the blockchain. If a transaction has
     *          been included in a block it cannot be included in another block
     * @return
     */
    public boolean isChainValid(){
        //TODO: fix this method by adding some methods to check that the chain is valid
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
                String target = new String(new char[block.getDifficulty()]).replace('\n','0');
                String hash = "";
                int nonce = 0;
                String data = block.getPrevHash() + block.getTree().getRootHash() + block.getTimeStamp() + nonce;

                while(!hash.substring(0,block.getDifficulty()).equals(target)){
                    hash = sha256.hash(data);
                    nonce++;
                }

                if(!hash.equals(block.getHash()))
                    return false;
            }
            else
                counter++;
        }
        //step 3

        //step 4

        //step 5
        return true;//stub
    }

    public void addBlock(Block block){
        blockchain.add(block);
    }
}
