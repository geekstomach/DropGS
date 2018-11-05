package com.geekstomach.cloud.server;

import com.geekstomach.cloud.common.FileInfo;
import com.geekstomach.cloud.common.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FileHandler {

    public FileHandler(byte [] data) {
        System.out.println("Создается объект FileHandler и он получает массив байт");
        try {

            saveFile(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileHandler(byte command, byte [] data) {
        System.out.println("Создается объект FileHandler и он получает массив байт");
        try {
            switch (command) {
//                case Command.REG: new DBService();
                case Command.FILE_UPLOAD:saveFile(data);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileHandler(byte command, byte[] data, ChannelHandlerContext ctx) {
        System.out.println("Создаем в FileHandler");
        switch (command) {

            case Command.FILE_DOWNLOAD:
                downloadFile(data,ctx);
                break;

            case Command.FILE_GET_LIST:
                System.out.println("Заходим в FILE_GET_LIST");
                try {
                    getFileList(data,ctx);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    private void downloadFile(byte[] data,ChannelHandlerContext ctx) {
        System.out.println("Отправляем файл клиенту");
        byte[] FileNameByteArraylength= new byte[4];
        for (int i = 0; i < 4; i++) {
            FileNameByteArraylength[i]= data[i];
        }
//не успел...
    }
    private void getFileList(byte[] data,ChannelHandlerContext ctx) throws IOException {
        System.out.println("Заходим в метод отправки файллиста клиенту");
        //ObservableList<FileInfo> cloudFilesList;
        ArrayList<FileInfo> cloudFilesListArray = new ArrayList<>();
        //cloudFilesList = FXCollections.observableArrayList();
        try {
           // cloudFilesList.addAll(Files.list(Paths.get("server\\repository")).map(Path::toFile).map(FileInfo::new).collect(Collectors.toList()));
            cloudFilesListArray.addAll(Files.list(Paths.get("server\\repository")).map(Path::toFile).map(FileInfo::new).collect(Collectors.toList()));
            System.out.println("Заполняем лист");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        //oos.writeObject(cloudFilesList);
        oos.writeObject(cloudFilesListArray);

        System.out.println("Преобразуем лист в массив байт");
        byte[] arr = bos.toByteArray();
        ByteBufAllocator allocator = new PooledByteBufAllocator();
        ByteBuf buf = allocator.buffer(4);
        System.out.println(buf.toString());
        buf.writeInt((int) arr.length);
        System.out.println(arr.length);
        System.out.println(buf.toString());
        ctx.writeAndFlush(buf);
        System.out.println("Отправляем длину массива " + arr.length);
        buf = allocator.buffer(arr.length);
        buf.writeBytes(arr);
        System.out.println("Отправляем файллист клиенту");
        ctx.writeAndFlush(buf);
    }
    private void saveFile(byte [] in) throws IOException {

    byte[] fileNameLengthBytes = Arrays.copyOfRange(in, 0, 3);
    ByteBuffer buffer = ByteBuffer.wrap(in , 0,4);
    int fileNameLength = buffer.getInt();
    byte[] fileNameBytes =Arrays.copyOfRange(in, 4, fileNameLength+4);
    String path = "server\\repository\\"+(new String(fileNameBytes));
    FileOutputStream fileOut = new FileOutputStream(path);
    System.out.println("Открываем FileOutputStream");
    fileOut.write(in,fileNameLength+3,in.length-(fileNameLength+4));
    System.out.println("Файл записан)");
    fileOut.close();
}

}

