import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage;

public class Identifier {
    public final String USERNAME;
    public final String PASSWORD;


    public Identifier(String username, String password) {
        USERNAME = username;
        PASSWORD = password;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public Authenticator getAuthenticator(){
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        };
    }
}
