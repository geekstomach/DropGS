package com.geekstomach.cloud.client.nettyClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.BasicConfigurator;

public class Client {

    static final String HOST = "localhost";//завести все в проперти
    static final int PORT = 8323;

    public void start() throws Exception {
        BasicConfigurator.configure();
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    //.handler(new ClientInitializer());
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ProtocolEncoder());
                            p.addLast(new ClientHandler());
                        }
                    });
            // Make a new connection.
            ChannelFuture future = b.connect(HOST, PORT).sync();
// Wait until the connection is closed.
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }



    public static void main(String[] args) throws Exception {
new Client().start();
    }
}
