package com.geekstomach.cloud.server;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    String path = "server\\repository\\WTF.txt";
    FileOutputStream fileOut = new FileOutputStream(path);
    System.out.println("Открываем FileOutputStream");
    fileOut.write(in);
    System.out.println("Файл записан)");
    fileOut.close();
}

}
