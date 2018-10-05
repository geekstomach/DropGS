package com.geekstomach.cloud.server;

import com.geekstomach.cloud.common.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileOutputStream;
import java.io.IOException;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;

        if (in.readByte() == Command.START_MSG) {
            System.out.println("Получаем команду START_MSG " + Command.START_MSG);
            byte command = in.readByte();
            if (command == Command.FILE_UPLOAD) {
                System.out.println("Получаем команду FILE_UPLOAD " + Command.FILE_UPLOAD);
                saveFile(in,ctx);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void saveFile(ByteBuf in, ChannelHandlerContext ctx) throws IOException {

        String path = "server\\repository\\WTF.txt";
        FileOutputStream fileOut = new FileOutputStream(path);
        System.out.println("Открываем FileOutputStream");
        byte[] buffer = new byte[1024];
        int n = 0;
        int dataLength = in.readInt();//15

        System.out.println("Длина файла в байтах "+dataLength);
        int read = 0;
        int totalRead = 0;
        int remaining = dataLength; //15
        System.out.println("Позиция в буфере в начале " + in.readerIndex());//6
        System.out.println("Вместимость буфера" + in.capacity());//1024
        System.out.println("Количество байт которые можно прочитать" + in.readableBytes());//1018
        System.out.println("Первый байт файла" + in.getByte(6));
        System.out.println("dataLength: (" + dataLength + "), read: (" + read + "), totalRead: (" + totalRead + "), remaining: (" + remaining + ")");
        // dataLength: (15), read: (0), totalRead: (0), remaining: (15)
        //System.out.println("Позиция в буфере в начале " + in.readerIndex());

        while (remaining > 0) {
            read = Math.min(buffer.length, remaining);
            System.out.println(read);////15

            System.out.println("dataLength: (" + dataLength + "), read: (" + read + "), totalRead: (" + totalRead + "), remaining: (" + remaining + ")");

            //in.readBytes(buffer, 0, Math.min(in.readableBytes(), remaining));
            System.out.println("Позиция в буфере до чтения " + in.readerIndex());
            in.readBytes(buffer, 0, Math.min(in.readableBytes(), remaining));
            System.out.println("Позиция в буфере после чтения " + in.readerIndex());
            totalRead += read;
            remaining -= read;

            System.out.println("read " + totalRead + " bytes.");
            System.out.println("dataLength: (" + dataLength + "), read: (" + read + "), totalRead: (" + totalRead + "), remaining: (" + remaining + ")");

            showBuffer(buffer);
            fileOut.write(buffer, 0, read);
            in.resetReaderIndex();
            n++;
            if (n>10)break;//для проверки, чтобы не улетало в бесконечный цикл
            System.out.println("Получено пакет " + n);
        }
        //if (in.readByte() == Command.FINISH_MSG){
        System.out.println("Файл записан(Yf cfvjv ltkt pfgbcfyf rfrfznj [htym)");
        //}

        fileOut.close();
    }

    private void showBuffer(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            System.out.print(buffer[i] + " ");
        }
        System.out.println();
    }
}