package Organization;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.crypto.Cipher;

import Blockchain.Mempool;
import Blockchain.Sha256;

public class CertificateAuthority {
    private Sha256 sha256; 
    private KeyPair pair;
    private Map<String, String> subscribers;
    private Mempool mempool;
    
    public CertificateAuthority(Mempool mempool){
        sha256 = new Sha256();
        pair = null;
        subscribers = new HashMap<String,String>();
        this.mempool = mempool;

        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            keygen.initialize(2048);
            pair = keygen.genKeyPair();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method creates a digital certificate of a users public key by hashing it with sha256 then creating a digital signature using the
     * CA's private key
     * @param key PublicKey of the individual
     * @param address The address associated with an individual
     * @return
     */
    public String createCertificate(PublicKey key, String address){
        byte[] keyBytes = key.getEncoded();
        String data = Base64.getEncoder().encodeToString(keyBytes);

        data = sha256.hash(data);
        String result = "Error";

        try{
            // Cipher cipher = Cipher.getInstance("RSA");
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pair.getPrivate());
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            // result = new String(encryptedData);
            result = Base64.getEncoder().encodeToString(encryptedData);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        subscribers.put(address, result);
        return result;
    }

    /**
     * This method returns the public key of the CA
     * @return PublicKey of the Certificate Authority
     */
    public PublicKey getPublicKey(){
        return pair.getPublic();
    }
    
    /**
     * This method returns the digital certificate of the individual whose address is provided. If the address is not found, null is returned.
     * @param address The address of the individual whose certificate is required
     * @return The digital certificate of the individual
     */
    public String getIndividualCertificate(String address){
        if(subscribers.containsKey(address)){
            return subscribers.get(address);
        }
        else
            return null;
    }

    public boolean registerUser(Person person){
        /*TwoFactorAuthentication twoFA = TwoFactorAuthentication.generateCode();
        System.out.println("Enter email address: ");
        Scanner input = new Scanner(System.in);

        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_.+-]+@gmail\\.com$");
        String email= "";
        for(int i=0;i<5;i++){//give a maximum of chances for user to enter correct email address according to regex
            email = input.nextLine();

            if(emailPattern.matcher(email).matches())
                break;

            else if(emailPattern.matcher(email).matches() == false && i == 4)
                return false;
        }

        twoFA.sendCode(email);
        Pattern codePattern = Pattern.compile("^\\d{6}$");
        System.out.println("Enter code: ");
        String code = input.nextLine();

        boolean result = false;
        if(codePattern.matcher(code).matches() && twoFA.isValid() && code.equals(String.valueOf(twoFA.getCode()))){
            result = true;
            mempool.addUser(person);
        }*/
        mempool.addUser(person);
        
        // return result;
        return true;
    }
}
