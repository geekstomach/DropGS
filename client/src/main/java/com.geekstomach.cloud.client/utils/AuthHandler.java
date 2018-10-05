package com.geekstomach.cloud.client.utils;

import com.geekstomach.cloud.common.protocol.Command;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AuthHandler {


    Socket socket;
    DataOutputStream out;

    public AuthHandler(Socket socket,DataOutputStream out) {
        this.socket = socket;
        this.out = out;
    }

    public void tryAuth() throws IOException {
        out.writeByte(Command.START_MSG);
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
        //out.close();
        System.out.println("Закрываем");
    }
}
