package com.geekstomach.cloud.client;

import java.io.DataOutputStream;
import java.net.Socket;

//этот сработал
public class ProtocolClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 8326);
            System.out.println("Соединение установленно "+ socket.isConnected());
            FileHandler  fileHandler = new FileHandler(socket);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
           // AuthHandler authHandler = new AuthHandler(socket,out);

            System.out.println("Открываем OutputStream");
            fileHandler.sendFile("client\\local_storage\\WTF1.txt",out);
            //authHandler.tryAuth();
            socket.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
           /* */