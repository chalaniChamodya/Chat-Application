package lk.ijse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    ArrayList<Socket> sockets = new ArrayList<>();
    DataOutputStream outputStream ;

    public void initialize(){
        new Thread(()->{
            Socket clientSocket;

            try (ServerSocket serverSocket = new ServerSocket(3023)){
                System.out.println("Server Started");
                while (true){
                    clientSocket = serverSocket.accept();
                    System.out.println("\nuser connected");

                    Socket finalClientSocket = clientSocket;

                    Thread clientThread = new Thread(()->{
                        try {
                            DataInputStream inputStream = new DataInputStream(finalClientSocket.getInputStream());
                            int port = finalClientSocket.getPort();
                            sockets.add(finalClientSocket);

                            String receivingMsg = "";
                            while (!(receivingMsg = inputStream.readUTF()).equals("Finish")){
                                for (Socket socket : sockets){
                                    outputStream = new DataOutputStream(socket.getOutputStream());
                                    outputStream.writeUTF(receivingMsg);
                                    outputStream.flush();
                                }
                            }

                            sockets.remove(finalClientSocket);
                            finalClientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.exit(0);
                        }
                    });

                    clientThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
