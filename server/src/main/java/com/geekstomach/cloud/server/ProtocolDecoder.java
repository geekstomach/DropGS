package com.geekstomach.cloud.server;

import com.geekstomach.cloud.common.protocol.Command;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ProtocolDecoder extends ByteToMessageDecoder {
    boolean waitingNewMsg = true;
    byte command;
    int msgLength;
    byte[] data;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> outToNextHandler) throws Exception {

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
                    System.out.println("Получаем команду на авторизацию");
                    new AuthHandler(data);//тут какаято херня
                    //будем считать что авторизация прошла успешно
/*                  outToNextHandler.add(Command.AUTH_OK);*/
                    System.out.println(Command.AUTH_OK);
                    //ctx.writeAndFlush(Command.AUTH_OK);//тут какаято херня
//какаято хрень, но она работает
                    byte[] arr = new byte[]{Command.AUTH_OK};
                    ByteBufAllocator allocator = new PooledByteBufAllocator();
                    ByteBuf buf = allocator.buffer(arr.length);
                    buf.writeBytes(arr);
                    buf.writeByte(Command.AUTH_OK);
                    ctx.writeAndFlush(buf);



                    System.out.println("Отправляем сообщение об успешной авторизации");
                    //ReferenceCountUtil.release(ctx);
                    waitingNewMsg = true;
                    msgLength = 0;
                    break;
                case Command.FILE_UPLOAD: {
                    System.out.println("Получаем команду "+Command.FILE_UPLOAD);
                    System.out.println("Отправляем получившийся массив байт в FileHandler");
                    new FileHandler(data);//или объязательно в лист отправлять
                    waitingNewMsg = true;
                    msgLength = 0;
                    break;
                }
                case Command.FILE_DOWNLOAD: {
                    System.out.println("Получаем команду "+Command.FILE_DOWNLOAD);
                    System.out.println("Отправляем получившийся массив байт в FileHandler");
                    new FileHandler(Command.FILE_DOWNLOAD, data);//или объязательно в лист отправлять
                    waitingNewMsg = true;
                    msgLength = 0;
                    break;
                }
                case Command.FILE_GET_LIST: {
                    System.out.println("Получаем команду "+Command.FILE_DOWNLOAD);
                    System.out.println("Отправляем получившийся массив байт в FileHandler");
                    new FileHandler(Command.FILE_GET_LIST, data, ctx);//или объязательно в лист отправлять
                    waitingNewMsg = true;
                    msgLength = 0;
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
                System.out.println("Получаем длину сообщения в байтах  " + msgLength);
                data = new byte[msgLength];
                System.out.println("Создаем массив байт размером " + msgLength);
            }

        } else return;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

