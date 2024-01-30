package lk.ijse.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientFormController {
    public TextField txtClientMsg;
    public Label lblClientName;
    public VBox vBoxLeft;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    String username;
    Socket socket;
    String msg;

    public void initialize(){
        getConnection();
        lblClientName.setText(LoginFormController.name);
        username = lblClientName.getText();

    }

    private void getConnection() {
        new Thread(()->{
            try {
                socket = new Socket("localhost", 3023);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                getServerMsg();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("connection done");
    }

    private void getServerMsg() {
        new Thread(()->{
            while (true){
                /*try {
                    msg = dataInputStream.readUTF();
                    System.out.println(msg);
                    //if (!(msg.startsWith(LoginFormController.name))) {
                        Text text = new Text(msg);
                    //System.out.println(text);
                        text.setStyle("-fx-font-size: 14");

                        TextFlow textFlow = new TextFlow(text);
                        textFlow.setStyle("-fx-font-weight: bold; -fx-text-fill: purple; -fx-background-radius: 0 10 10 10");
                        textFlow.setPadding(new Insets(10, 8, 10,10));

                        HBox hBox = new HBox();
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        if(msg.charAt(0) == '#'){

                        }else {
                            hBox.getChildren().add(textFlow);
                        }

                        Platform.runLater(()->{
                            vBoxLeft.getChildren().add(hBox);
                        });
                   // }

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }).start();
    }

    public void btnSendOnAction(ActionEvent actionEvent) {
        try {
            dataOutputStream.writeUTF(username + "\n" +txtClientMsg.getText());
            dataOutputStream.flush();

            Text text = new Text("Me\n" + txtClientMsg.getText());
            text.setStyle("-fx-font-size: 14");

            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-background-radius: 0 10 10 10");
            textFlow.setPadding(new Insets(10, 8, 10,10));

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().add(textFlow);

            vBoxLeft.getChildren().add(hBox);
            txtClientMsg.setText("");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
