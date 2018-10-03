package com.geekstomach.cloud.server;

import com.geekstomach.cloud.common.protocol.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class ServerHandler extends ChannelInboundHandlerAdapter {
    private int state = -1;
    private int reqLenOne = -1;
    private int reqLenTwo = -1;
    private String type;
    private String action;
    private String status;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = ((ByteBuf) msg);
        if (state == -1) {//Здесь мы обработали команду
            byte firstByte = buf.readByte();

                type = "AUTH Fusking";
                action = "Login";
                state = 0;//меняем состояние на чтоние информации
                reqLenOne = 5;
                reqLenTwo = 8;
                System.out.println(type);
            System.out.println(firstByte);

           if (state == 0) {
/*                String login = String.valueOf(buf.readBytes(reqLenOne));
                String password = String.valueOf(buf.readBytes(reqLenTwo));*/
byte[] loginByte = new byte[reqLenOne];
               byte[] PasswordByte = new byte[reqLenTwo];
               buf.readBytes(loginByte);
               buf.readBytes(PasswordByte);
               String login = new String(loginByte);
               String password = new String(PasswordByte);
                state = 1;
                System.out.println("login: " + login);
                System.out.println("password: " + password);
            }
        }
    }
}
