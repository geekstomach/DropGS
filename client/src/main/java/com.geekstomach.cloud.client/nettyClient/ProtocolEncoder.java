package com.geekstomach.cloud.client.nettyClient;

import com.geekstomach.cloud.common.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtocolEncoder extends MessageToByteEncoder {

    public ProtocolEncoder() {
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        out.writeByte(Command.AUTH);
        System.out.println("Отправляем команду Command.AUTH " + Command.AUTH);
        String login = "admin";
        String password = "admin";
        out.writeInt(login.length());
        System.out.println("Отправляем длину логина");
        byte[] loginBytes = login.getBytes();//конвертируем строку в байтовый массив
        out.writeBytes(loginBytes);
        System.out.println("Отправляем логин");
        out.writeInt(password.length());
        System.out.println("Отправляем длину пароля");
        byte[] passwordBytes = password.getBytes();
        out.writeBytes(passwordBytes);
        System.out.println("Отправляем пароль");
        ctx.writeAndFlush(out);


    }
}
