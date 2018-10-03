package com.geekstomach.cloud.server;

import com.geekstomach.cloud.common.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.sql.SQLOutput;
import java.util.List;

public class ProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        if (in.readByte()== Command.AUTH){
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
        }

    }
}
