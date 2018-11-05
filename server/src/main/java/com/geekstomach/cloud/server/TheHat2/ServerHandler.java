package com.geekstomach.cloud.server.TheHat2;


import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
User user;
    public ServerHandler(User user) {
        this.user = user;
    }
}
