package Organization;

import java.util.Date;
import java.util.Properties;

import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TwoFactorAuthentication {
   private static final int EXPIRATION_TIME = 300;
   
   public static TwoFactorAuthentication generateCode(){
        int code = (int) (Math.random() * 1000000);//generate a random 6 digit code
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

   public void sendCode(String emailAddress){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // replace with your SMTP server
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", "burnerdevelopment8@gmail.com");

        Session session = Session.getDefaultInstance(props);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("burnerdevelopment8@gmail.com")); //your email address
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress)); //recipient email address
            message.setSubject("Blockchain authentication code");
            message.setText(String.valueOf(code));

            // Send the email
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "burnerdevelopment8@gmail.com", "ytdpcqxpvkqxfsgy"); // replace with your email address and password
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("Email sent successfully to " + emailAddress + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
}
