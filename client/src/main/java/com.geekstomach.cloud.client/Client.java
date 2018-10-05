package com.geekstomach.cloud.client;

import com.geekstomach.cloud.client.UI.LoginController;
import com.geekstomach.cloud.common.protocol.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {


    private Socket socket;

    public DataOutputStream getOut() {
        return out;
    }

    private DataOutputStream out;
    private DataInputStream in;

    final String SERVER_IP = "localhost";
    final int SERVER_PORT = 8195;

    private boolean authhorized;

    LoginController loginController;

    public void setAuthhorized(boolean authhorized) {
        this.authhorized = authhorized;}

public void sendAuthMsg(DataOutputStream out) throws IOException {
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
}

    public void initialize() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            setAuthhorized(false); //при первичном подключениии устанавливаем значание false

            Thread t = new Thread(new Runnable() {//запускаем нить ввода вывода
                @Override
                public void run() {
                    try {
                        while (true) {
                            byte auth = in.readByte();
                            if (auth == Command.AUTH_OK){
                                setAuthhorized(true);
                                loginController.createMainWindow();
                                break;
                            } else loginController.showAlertWithoutHeaderText("Вы ввелели неверные данные, попробуйте еще раз");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        setAuthhorized(false);
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.setDaemon(true);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
