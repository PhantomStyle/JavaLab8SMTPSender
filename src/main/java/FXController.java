import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

public class FXController extends Application {

    private static Logger log = LoggerFactory.getLogger(FXController.class);

    @FXML
    private TextField recieverField;

    @FXML
    private TextArea messageField;

    @FXML
    private TextField themeField;

    @FXML
    private Button loginButton;

    @FXML
    private Button authButton;

    @FXML
    private PasswordField passwordEnterField;

    @FXML
    private Text authText;

    @FXML
    private TextField emailEnterField;

    @FXML
    private TextField authEnterField;

    @FXML
    private Button sendButton;

    private Identifier identifier;
    private PropertiesSetter propertiesSetter = new PropertiesSetter();
    private Transport transport;
    private Session mailSession;

    @FXML
    void onLoginButton(ActionEvent event) throws MessagingException {
        String email = emailEnterField.getText();
        String password = passwordEnterField.getText();
        identifier = new Identifier(email, password);

        try {
            mailSession = Session.getDefaultInstance(propertiesSetter.getProperties(getHost(identifier.getUSERNAME())), identifier.getAuthenticator());
            transport = mailSession.getTransport("smtp");
            transport.connect(getHost(identifier.getUSERNAME()), identifier.getUSERNAME(), identifier.getPASSWORD());
            setSendScene();
        } catch (Exception ex) {
            log.info("Error: ", ex);
            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Wrong credentials"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            st.setScene(dialogScene);
            st.show();
            emailEnterField.clear();
            passwordEnterField.clear();
        }
    }

    @FXML
    void onAuthButton(ActionEvent event) {
        String authCode = authEnterField.getText();
        identifier.getAuthenticator();
        setSendScene();
    }

    @FXML
    void onSendButton(ActionEvent event) throws MessagingException {
        try {
            String receiver = recieverField.getText();
            String theme = themeField.getText();
            String message = messageField.getText();
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            String host = getHost(identifier.getUSERNAME());

            Session mailSession = Session.getDefaultInstance(propertiesSetter.getProperties(host), identifier.getAuthenticator());
//        mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(identifier.getUSERNAME()));
            InternetAddress[] address = {new InternetAddress(receiver)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(theme);
            msg.setSentDate(new Date());
            msg.setText(message);


//                transport.connect(host, identifier.getUSERNAME(), identifier.getPASSWORD());
            transport.sendMessage(msg, msg.getAllRecipients());
            themeField.clear();
            messageField.clear();
            recieverField.clear();
            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Message was send successfully"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            st.setScene(dialogScene);
            st.show();
//            transport.close();
        } catch (Exception ex) {

            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Something was wrong"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            st.setScene(dialogScene);
            st.show();
        }
    }

    private String getHost(String user) {
        String mailProvider = user.split("@")[1];
        return "smtp." + mailProvider;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    public void initialize() throws NoSuchProviderException {
        setLoginScene();
        messageField.setWrapText(true);
    }

    private void setLoginScene() {
        recieverField.setVisible(false);
        messageField.setVisible(false);
        themeField.setVisible(false);
        loginButton.setVisible(true);
        authButton.setVisible(false);
        passwordEnterField.setVisible(true);
        authText.setVisible(false);
        emailEnterField.setVisible(true);
        authEnterField.setVisible(false);
        sendButton.setVisible(false);
    }

    private void setAuthScene() {
        recieverField.setVisible(false);
        messageField.setVisible(false);
        themeField.setVisible(false);
        loginButton.setVisible(false);
        authButton.setVisible(true);
        passwordEnterField.setVisible(false);
        authText.setVisible(true);
        emailEnterField.setVisible(false);
        authEnterField.setVisible(true);
        sendButton.setVisible(false);
    }

    private void setSendScene() {
        recieverField.setVisible(true);
        messageField.setVisible(true);
        themeField.setVisible(true);
        loginButton.setVisible(false);
        authButton.setVisible(false);
        passwordEnterField.setVisible(false);
        authText.setVisible(false);
        emailEnterField.setVisible(false);
        authEnterField.setVisible(false);
        sendButton.setVisible(true);
    }

}