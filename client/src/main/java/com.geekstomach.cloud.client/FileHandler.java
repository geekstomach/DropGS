package com.geekstomach.cloud.client;

import com.geekstomach.cloud.common.protocol.Command;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {


    Socket socket;
    FileHandler(Socket socket){
        this.socket = socket;
    }

    public void sendFile(String path,DataOutputStream out ) throws IOException {
        //DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        //System.out.println("Открываем оутпутстрим");
        System.out.println("Записываем его в поток");
        FileInputStream fileInput = new FileInputStream(path);

        Path filelocation = Paths.get(path);
        byte[] data = Files.readAllBytes(filelocation);
        ByteBuffer in = ByteBuffer.wrap(data);
        int dataLength = data.length;
        out.writeByte(Command.START_MSG);
        System.out.println("Отправляем команду START_MSG " + Command.START_MSG);
        out.writeByte(Command.FILE_UPLOAD);
        System.out.println("Отправляем команду FILE_UPLOAD " + Command.FILE_UPLOAD);
        out.writeInt(dataLength); //как узнать длину файла?
        System.out.println("Отправляем длину файла " + dataLength);
        out.write(data);
/*
        byte [] buffer = new byte[1018];
        int n = 0;
        int write = 0;
        int totalWrite = 0;
        int remaining = dataLength; //15

        System.out.println("dataLength: (" + dataLength + "), write: (" + write+ "), totalWrite: (" + totalWrite + "), remaining: (" + remaining +")");
        System.out.println("Позиция в буфере в начале" + in.position());
        in.position(0);
        while ( remaining > 0){
            write = Math.min(buffer.length, remaining);
            System.out.println(write);////15

            System.out.println("Позиция в буфере до чтения" + in.position());
            in.get(buffer,0, Math.min(buffer.length,remaining));
            System.out.println("Позиция в буфере после чтения" + in.position());

            totalWrite += write;
            remaining -= write;

            System.out.println("write " + totalWrite + " bytes.");
            System.out.println("dataLength: (" + dataLength + "), write: (" + write+ "), totalWrite: (" + totalWrite + "), remaining: (" + remaining +")");

            showBuffer(buffer);
            out.write(buffer,0,write);

                    n++;
                    System.out.println("Отправляем пакет " + n);
        }
       // out.writeByte(Command.FINISH_MSG);
       */
        out.close();
        fileInput.close();
    }
    private void showBuffer(byte[] buffer){
        for (int i = 0; i <buffer.length ; i++) {
            System.out.print(buffer[i] + " ");
        }
        System.out.println();
    }
}
