package com.geekstomach.cloud.server;

import io.netty.buffer.ByteBuf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class FileHandler {

    public FileHandler(byte [] data) {
        System.out.println("Создается объект FileHandler и он получает массив байт");
        try {
            saveFile(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
