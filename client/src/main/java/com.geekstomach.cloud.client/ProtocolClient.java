package com.geekstomach.cloud.client;

import java.io.DataOutputStream;
import java.net.Socket;


import com.geekstomach.cloud.common.protocol.Command;

//этот сработал
public class ProtocolClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8324);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Открываем оутпутстрим");
            System.out.println("Записываем его в поток");
            out.writeByte(Command.AUTH);
            System.out.println("Отправляем команду Command.AUTH " + Command.AUTH);
            String login = "admin";
            String password = "admin";
            out.writeInt(login.length());
            System.out.println("Отправляем длину логина");
            byte[] loginBytes = login.getBytes();//конвертируем строку в байтовый массив
            out.write(loginBytes);
            System.out.println("Отправляем логин");
            out.writeInt(password.length());
            System.out.println("Отправляем длину пароля");
            byte[] passwordBytes = password.getBytes();
            out.write(passwordBytes);
            System.out.println("Отправляем пароль");
            out.flush();
            System.out.println("Флашим");
            out.close();
            System.out.println("Закрываем");

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
