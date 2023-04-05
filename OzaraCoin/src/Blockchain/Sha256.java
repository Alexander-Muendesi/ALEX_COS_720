package Blockchain;

import java.security.MessageDigest;

/**
 * This class is used to carry out the SHA-256 hashing algorithm
 */
public class Sha256 {
    public Sha256(){}

    /**
     * Method used to hash a string with SHA-256. 
     * @param data The transaction data in the form of a string to be hashed
     * @return The hash value as a string
     */
    public String hash(String data){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();//mutable string class

            for(int i = 0;i < hash.length; i++){
                //below we are converting a signed byte value to an unsigned integer value.
                //0xff is used to mask off the sign bit of the byte and covert it to an unsigned integer value
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');//ensures that each byte in the hash is represented as a 2 digit hex string
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            System.out.println("Error in sha256 function");
            e.printStackTrace();
            return null;
        }
    }
}
