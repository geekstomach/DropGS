package com.geekstomach.cloud.server;

public class AuthHandler {
   /* В конструторе передаем Ctx чтобы можно было отправить команду успешой авторизации*/
    public AuthHandler(byte[] data) {
        System.out.println("зашли в сервис авторизации");

    }

}
