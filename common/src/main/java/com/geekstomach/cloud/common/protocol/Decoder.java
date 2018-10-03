package com.geekstomach.cloud.common.protocol;

import io.netty.buffer.ByteBufUtil;

import java.sql.SQLOutput;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;

public class Decoder {


Object msg;

public static byte[] Auth (String login,String password){
    byte AuthCommand = Command.AUTH;
    byte[] loginToByte = login.getBytes();
    byte[] passwordToByte = password.getBytes();
    System.out.println("Переводим в байты сообщение"+AuthCommand + loginToByte.toString() +passwordToByte.toString());
    return crateByteMassage(AuthCommand,loginToByte,passwordToByte); }




    private static byte[] crateByteMassage(byte cmd,byte[] partOne,byte[] partTwo){
        byte[] cmdArr = new byte[1];
        cmdArr[0]= cmd;//добавляем команду
        System.out.println(cmdArr[0]);
        byte[] massage = ArrayUtils.addAll(cmdArr,partOne);
        massage = ArrayUtils.addAll(massage,partTwo);
        System.out.println(massage[0]);
        System.out.println("Создаем строку сообщения"+massage.toString());
        return massage;

    }
}
