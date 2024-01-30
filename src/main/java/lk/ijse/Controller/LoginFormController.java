package lk.ijse.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.Server;

import java.io.IOException;

public class LoginFormController {
    public TextField txtClientName;
    static String name = "";
    public Button btnLogin;

    Server server = new Server();

    public void initialize(){
        // start server
        server.initialize();
        btnLogin.setVisible(false);
        if(!txtClientName.getText().equals(" ")){
            btnLogin.setVisible(true);
        }
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        name = txtClientName.getText();

        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/View/ClientForm.fxml"));
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.show();
            txtClientName.setText("");
        } catch (IOException e) {
            Throwable cause = e.getCause();
            cause.printStackTrace();
        }
    }
}
