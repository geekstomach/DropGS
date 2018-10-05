package com.geekstomach.cloud.server.thehat;

import com.geekstomach.cloud.common.protocol.Command;
import com.geekstomach.cloud.server.AuthService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.List;

public class ProtocolDecoder extends ByteToMessageDecoder {
    private AuthService authService;
    private AuthService getAuthService(){
        return authService;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        if (in.readByte() == Command.START_MSG) {
            System.out.println("Получаем команду START_MSG " + Command.START_MSG);
            byte command = in.readByte();
            if (command == Command.AUTH) {

                authService = new AuthService();
                authService.connect();

                System.out.println("Проверяем что-то в базе данных и авторизируется и возможо отправляем статус AUTH_OK");
                //ByteBuf loginLength = in.readBytes(4);
                int loginLength = in.readInt();//получаем длину логина в байтах
                System.out.println("Получаем длину логина в байтах");
                byte[] loginBytes = new byte[loginLength];
                in.readBytes(loginBytes);
                System.out.println("Получаем логин  в байтах");
                String login = new String(loginBytes);
                System.out.println("Получаем логин: " + login);
                int passwordLength = in.readInt();//получаем длину логина в байтах
                System.out.println("Получаем длину пароля в байтах");
                byte[] passwordBytes = new byte[passwordLength];
                in.readBytes(passwordBytes);
                System.out.println("Получаем пароль  в байтах");
                String password = new String(passwordBytes);
                System.out.println("Получаем пароль : " + password);
                System.out.println("Login: " + login + " Password :" + password);
                System.out.println("Nickname :" + getAuthService().getNickByLoginAndPass(login, password));
                authService.disconnect();
                in.clear();
            }
            if (command == Command.FILE_UPLOAD) {
                System.out.println("Получаем команду FILE_UPLOAD " + Command.FILE_UPLOAD);
                        saveFile(in);
            }
        } else {
            //System.out.println("Не сработало(");
        }
}


private void saveFile (ByteBuf in) throws IOException {

    String path = "server\\repository\\WTF.txt";
    FileOutputStream fileOut = new FileOutputStream(path);
    System.out.println("Открываем FileOutputStream");
    byte[] buffer = new byte[1024];
    int n = 0;
    int dataLength = in.readInt();//15
    System.out.println(dataLength);
    int read = 0;
    int totalRead = 0;
    int remaining = dataLength; //15
    System.out.println(in.readerIndex());//6
    System.out.println(in.capacity());//1024
    System.out.println(in.readableBytes());//1018
    System.out.println(in.getByte(6));
    System.out.println("dataLength: (" + dataLength + "), read: (" + read + "), totalRead: (" + totalRead + "), remaining: (" + remaining + ")");
    // dataLength: (15), read: (0), totalRead: (0), remaining: (15)
    System.out.println("Позиция в буфере в начале " + in.readerIndex());
    while (remaining > 0) {
        read = Math.min(buffer.length, remaining);
        System.out.println(read);////15

        System.out.println("dataLength: (" + dataLength + "), read: (" + read + "), totalRead: (" + totalRead + "), remaining: (" + remaining + ")");

        //in.readBytes(buffer, 0, Math.min(in.readableBytes(), remaining));
        System.out.println("Позиция в буфере до чтения " + in.readerIndex());
        in.readBytes(buffer, 0, Math.min(in.readableBytes(), remaining));
        System.out.println("Позиция в буфере после чтения " + in.readerIndex());
        totalRead += read;
        remaining -= read;

        System.out.println("read " + totalRead + " bytes.");
        System.out.println("dataLength: (" + dataLength + "), read: (" + read + "), totalRead: (" + totalRead + "), remaining: (" + remaining + ")");

        showBuffer(buffer);
        fileOut.write(buffer, 0, read);
        in.resetReaderIndex();
        n++;
        System.out.println("Получено пакет " + n);
    }
    //if (in.readByte() == Command.FINISH_MSG){
    System.out.println("Файл записан(Yf cfvjv ltkt pfgbcfyf rfrfznj [htym)");
    //}

    fileOut.close();
}

    private void showBuffer(byte[] buffer){
        for (int i = 0; i <buffer.length ; i++) {
            System.out.print(buffer[i] + " ");
        }
        System.out.println();
    }}
