package com.geekstomach.cloud.client.utils;

import com.geekstomach.cloud.client.UI.LoginController;
import com.geekstomach.cloud.common.FileInfo;
import com.geekstomach.cloud.common.protocol.Command;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class Client {
    private static Client ourInstance = new Client();

    public static Client getInstance() {
        return ourInstance;
    }

    private Client() {
        try {
            this.socket = new Socket(SERVER_IP, SERVER_PORT);
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String login;
    private String password;

    public Socket getSocket() {
        return socket;
    }

    private Socket socket;

/*    public Client(String login, String password) {
        this.login = login;
        this.password = password;
    }*/

    public DataOutputStream getOut() {
        return out;
    }

    private DataOutputStream out;
    private DataInputStream in;

    final String SERVER_IP = "localhost";
    final int SERVER_PORT = 8326;

    public boolean isAuthorized() {
        return authorized;
    }

    private boolean authorized;

    LoginController loginController;

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;}

public void sendAuthMsg(DataOutputStream out) throws IOException {
    System.out.println("Открываем оутпутстрим");
    System.out.println("Записываем его в поток");
    out.writeByte(Command.START_MSG);
    out.writeByte(Command.AUTH);
    System.out.println("Отправляем команду Command.AUTH " + Command.AUTH);
    byte[] loginBytes = login.getBytes();//конвертируем строку в байтовый массив
    byte[] passwordBytes = password.getBytes();
    int msgLength = 4+loginBytes.length+4+passwordBytes.length;//int 4 byte
    out.writeInt(msgLength);
    System.out.println("Отправляем длину сообщения"+msgLength);
    out.writeInt(loginBytes.length);
    out.write(loginBytes);
    System.out.println("Отправляем логин");
    out.writeInt(password.length());
    System.out.println("Отправляем длину пароля");

    out.write(passwordBytes);
    System.out.println("Отправляем пароль");
    out.flush();
}

    public void initialize() {
        try {
/*            socket = new Socket(SERVER_IP, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());*/

            setAuthorized(false); //при первичном подключениии устанавливаем значание false
            sendAuthMsg(out);//отправляем запрос авторизации
            System.out.println("отправляем запрос авторизации");
            byte auth = in.readByte();
            System.out.println("Читаем байт "+ auth);
            if (auth == Command.AUTH_OK){
                System.out.println("считаем авторизацию успешой");
                setAuthorized(true);
/*                                loginController = new LoginController();
                                loginController.createMainWindow();*/

            } else loginController.showAlertWithoutHeaderText("Вы ввелели неверные данные, попробуйте еще раз");

            Thread t = new Thread(new Runnable() {//запускаем нить ввода вывода
                @Override
                public void run() {
                    System.out.println("запускаем нить ввода вывода");
                    try {
                        while (true) {
                            System.out.println("Входим в бесконечный цикл");

                            if (auth == Command.AUTH_OK){
                                System.out.println("считаем авторизацию успешой");
                                setAuthorized(true);
/*                                loginController = new LoginController();
                                loginController.createMainWindow();*/
                                break;
                            } else loginController.showAlertWithoutHeaderText("Вы ввелели неверные данные, попробуйте еще раз");
                        }

                    } finally {
                        setAuthorized(false);
                        /*try {
                            //socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                    }
                }
            });
            t.setDaemon(true);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(Path path, DataOutputStream out) throws IOException {

        System.out.println("Записываем его в поток");
        //FileInputStream fileInput = new FileInputStream(path);

        //Path file = Paths.get(path);
        byte[] data = Files.readAllBytes(path);

        byte[] fileNameBytes = String.valueOf(path.getFileName()).getBytes();
        byte[] fileNameLengthBytes = ByteBuffer.allocate(4).putInt(fileNameBytes.length).array();

        //ByteBuffer Buffin = ByteBuffer.wrap(data);
        int dataLength = fileNameLengthBytes.length + fileNameBytes.length + data.length;
        out.writeByte(Command.START_MSG);
        System.out.println("Отправляем команду START_MSG " + Command.START_MSG);
        out.writeByte(Command.FILE_UPLOAD);
        System.out.println("Отправляем команду FILE_UPLOAD " + Command.FILE_UPLOAD);
        out.writeInt(dataLength); //как узнать длину файла?
        System.out.println("Отправляем длину файла " + dataLength);
        //необходимо отправить длину имени файла и его имя.
        out.write(fileNameLengthBytes);
        out.write(fileNameBytes);
        out.write(data);
        out.flush();
        //fileInput.close();
    }

    public  ObservableList<FileInfo> getCloudFileList() {
        try {
            ObservableList<FileInfo> cloudFilesList = null;
            ArrayList<FileInfo> cloudFilesListArray = new ArrayList<>();
            out.writeByte(Command.START_MSG);
            out.writeByte(Command.FILE_GET_LIST);
            out.writeInt(1);//издержки логики работы сервера, костыль так сказать
            out.writeByte(-1);
            System.out.println("Отправляем запрос серверу на получение файл листа");
            in.skipBytes(1);
            int i = in.readInt();

            System.out.println("Читаем длину сообщения "+ i);

            byte[] arr = new byte[i];

            in.read(arr);
            System.out.println("Создаем массив байт "+ i);
 /*           for (int j = 0; j < 425 ; j++) {
                arr[j] = in.readByte();
            }*/

            System.out.println("Заполняем массив байт");
            ByteArrayInputStream bis = new ByteArrayInputStream(arr);
            ObjectInputStream ois = new ObjectInputStream(bis);
            //cloudFilesList = (ObservableList<FileInfo>) ois.readObject();
            cloudFilesListArray = (ArrayList<FileInfo>) ois.readObject();
            System.out.println("Преобразуем массив байт");
            System.out.println(cloudFilesListArray.toString());
            cloudFilesList = FXCollections.observableArrayList(cloudFilesListArray);
            return cloudFilesList;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadFile(Path path) {
        try {
            out.writeByte(Command.START_MSG);
            out.writeByte(Command.FILE_DOWNLOAD);
            String FileName = path.getFileName().toString();
            byte [] FileNameByteArray = FileName.getBytes();
            out.writeInt(FileNameByteArray.length);
            out.writeUTF(FileName);

            in.skipBytes(1);

            int i = in.readInt();

            System.out.println("Читаем длину сообщения "+ i);

            byte[] arr = new byte[i];

            in.read(arr);
            System.out.println("Создаем массив байт "+ i);
 /*           for (int j = 0; j < 425 ; j++) {
                arr[j] = in.readByte();
            }*/


            System.out.println("Заполняем массив байт");
/*
            byte[] fileNameLengthBytes = Arrays.copyOfRange(in, 0, 3);
            ByteBuffer buffer = ByteBuffer.wrap(in , 0,4);
            int fileNameLength = buffer.getInt();
            byte[] fileNameBytes =Arrays.copyOfRange(in, 4, fileNameLength+4);*/
            String pathClient = "client\\local_storage\\"+FileName;
            FileOutputStream fileOut = new FileOutputStream(pathClient);
            System.out.println("Открываем FileOutputStream");
            fileOut.write(arr,FileName.length()+3,arr.length-(FileName.length()+4));
            System.out.println("Файл записан)");
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
