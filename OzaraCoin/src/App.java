import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Date;

import Blockchainn.Transaction;


public class App {
    public static void main(String[] args) throws Exception {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");//pass this a parameter to Peers

        Transaction one = new Transaction("Alex", "John", 20, 10, "tttt", "dfdf");
        Transaction two = new Transaction("Alex2", "John2", 202, 120, "ttt222t", "d22f22df");

        

    }
}
