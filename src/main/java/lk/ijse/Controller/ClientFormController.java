package lk.ijse.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientFormController {
    public TextField txtClientMsg;
    public Label lblClientName;
    public VBox vBoxLeft;
    //public VBox ImojiVBox;
    public HBox emojiHBox;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    String username;
    Socket socket;
    String msg;
    String imgPath;
   // private final byte[] emoji1 = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x98, (byte)0x8C};

    //private final String Imoji1 = new String(emoji1, StandardCharsets.UTF_8);

    public void initialize(){
        getConnection();
        lblClientName.setText(LoginFormController.name);
        username = lblClientName.getText();

        setImoji("\uD83D\uDE00");
        //setImoji("\uD83C\uDF1E");
        setImoji("\u2764\ufe0f");
        //setImoji("\uD83D\uDC4D");
        setImoji("\uD83D\uDE02");
        //setImoji("\uD83C\uDF3B");
        //setImoji("\uD83D\uDC4D");
        setImoji("\uD83C\uDFB5");
        setImoji("\uD83D\uDC4E");
        setImoji("\uD83D\uDD25");
    }

    private void setImoji(String imoji) {
        String jaString =imoji;
        writeOutput(jaString);
        String inputString = readInput();

        Label label = new Label();
        label.setText(inputString);
        label.setPadding(new Insets(10,10,10,10));
        emojiHBox.getChildren().add(label);

        label.setOnMouseClicked(event ->{
            txtClientMsg.setText(inputString);
        });
    }

    static void writeOutput(String str) {
        try {
            FileOutputStream fos = new FileOutputStream("test.txt");
            Writer out = new OutputStreamWriter(fos, "UTF8");
            out.write(str);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String readInput() {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream("test.txt");
            InputStreamReader isr = new InputStreamReader(fis, "UTF8");
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char)ch);
            }
            in.close();
            return buffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
                try {
                    msg = dataInputStream.readUTF();
                    System.out.println(msg);

                    if(msg.startsWith("img")){
                        String[] splitUsername = msg.split("img ");

                        String clientNameWithFilePath = splitUsername[1];
                        String[] splitFilePath = clientNameWithFilePath.split(" ");
                        String clientName = splitFilePath[0];
                        System.out.println(clientName);
                        imgPath = splitFilePath[1];
                        //System.out.println(imgPath);

                        if(!(clientName.equals(username))){
                            Label label = new Label();
                            label.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                            label.setStyle("-fx-font-size: 14");
                            label.setText(clientName +"\n");

                            File file = new File(imgPath);
                            Image image = new Image(file.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(150);
                            imageView.setFitWidth(150);

                            HBox hBox1 = new HBox(12);
                            hBox1.setAlignment(Pos.BOTTOM_RIGHT);
                            vBoxLeft.setAlignment(Pos.BOTTOM_LEFT);
                            hBox1.setAlignment(Pos.CENTER_LEFT);
                            hBox1.getChildren().add(imageView);
                            hBox1.getChildren().add(label);
                            Platform.runLater(()->{
                                vBoxLeft.getChildren().addAll(hBox1);
                            });
                        }
                    }else if (!(msg.startsWith(username))) {
                        Text text = new Text(msg);
                    //System.out.println(text);
                        text.setStyle("-fx-font-size: 14");

                        TextFlow textFlow = new TextFlow(text);
                        textFlow.setStyle("-fx-font-weight: bold; -fx-text-fill: purple; -fx-background-radius: 0 10 10 10");
                        textFlow.setPadding(new Insets(10, 8, 10,10));

                        HBox hBox = new HBox();
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        hBox.getChildren().add(textFlow);

                        Platform.runLater(()->{
                            vBoxLeft.getChildren().add(hBox);
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void btnSendOnAction1(ActionEvent actionEvent) {
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

    public void btnImageOnAction(ActionEvent actionEvent) {
        Label label = new Label();
        label.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        label.setStyle("-fx-font-size: 14");
        label.setText("Me\n");

        try {

            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);

            if(selectedFile != null){
                File file = new File(selectedFile.getPath());
                Image image = new Image(file.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(200);
                imageView.setFitWidth(200);

                HBox hBox = new HBox(12);
                hBox.setAlignment(Pos.BOTTOM_RIGHT);
                vBoxLeft.setAlignment(Pos.BOTTOM_LEFT);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.getChildren().add(imageView);
                hBox.getChildren().add(label);
                Platform.runLater(()->{
                    vBoxLeft.getChildren().addAll(hBox);
                });


                dataOutputStream.writeUTF( "img "+ username + " " + selectedFile.getPath());
                dataOutputStream.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
