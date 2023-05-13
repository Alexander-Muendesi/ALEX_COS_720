package Organization;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TwoFactorAuthentication {
   private static final int EXPIRATION_TIME = 300;
   
   public static TwoFactorAuthentication generateCode(){
        int code = (int) (Math.random() * 1000000);
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(EXPIRATION_TIME));
        return new TwoFactorAuthentication(code, expirationDate);
   }

   private final int code;
   private final Date expirationDate;

   public TwoFactorAuthentication(int code, Date expirationDate){
        this.code = code;
        this.expirationDate = expirationDate;
   }

   public int getCode(){
    return code;
   }

   public Date getExpirationDate(){
        return expirationDate;
   }

   public Boolean isValid(){
        return !expirationDate.before(new Date()); 
   }
}
