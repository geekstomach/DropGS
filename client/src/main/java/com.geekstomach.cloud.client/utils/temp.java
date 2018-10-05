package com.geekstomach.cloud.client.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class temp {
    public static void main(String[] args) {
        try {
            Files.lines(Paths.get("C:\\Users\\XE\\Google Диск\\GeekBrains\\DropGS\\client\\src\\main\\java\\com.geekstomach.cloud.client\\utils\\text.txt"))
                    .map(line -> line.split("\\s"))
                    .flatMap(Arrays::stream)
                    .distinct()
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }}
