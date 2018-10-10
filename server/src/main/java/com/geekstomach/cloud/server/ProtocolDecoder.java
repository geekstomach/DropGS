package com.geekstomach.cloud.server;

import com.geekstomach.cloud.common.protocol.Command;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

public class ProtocolDecoder extends ByteToMessageDecoder {
    boolean waitingNewMsg = true;
    byte command;
    int msgLength;
    byte[] data;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> outToNextHandler) throws Exception {

        System.out.println("Входим в метод Decode");

        if (msgLength > 0) {

            if (in.readableBytes() < msgLength) {
                System.out.println("Ждем пока буфер наполнится до размера файла");
                return;
            }
            in.readBytes(data);
            //ReferenceCountUtil.release(in);//вроде этим очищаем буфер
            //in.release();
            System.out.println("Заполняем массив байтами из буфера");

            switch (command) {
//                case Command.REG: new DBService();
                case Command.AUTH:
                    outToNextHandler.add(new AuthHandler(data));
                    break;
                case Command.FILE_UPLOAD: {
                    System.out.println("Отправляем получившийся массив байт в FileHandler");
                    new FileHandler(data);//или объязательно в лист отправлять
                    waitingNewMsg = true;
                    break;
                }

            }


        }
        //может надо включить еще один служебный байт на всякий случай.
        if (in.readableBytes() != 0) {
            if ((in.readByte() == Command.START_MSG) && waitingNewMsg) {
                System.out.println("Cмотрим на первый байт буфера " + waitingNewMsg + " " + (in.getByte(0) == Command.START_MSG));
                waitingNewMsg = false;
                command = in.readByte();
                System.out.println("Получаем команду " + command);
                msgLength = in.readInt();//пока ограничемся 2гб
                System.out.println("Получаем длину файла в байтах " + msgLength);
                data = new byte[msgLength];
                System.out.println("Создаем массив байт размером " + msgLength);
            }

        } else return;
    }
}

