package com.geekstomach.cloud.server.TheHat2;



import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class Server {

    private static int propPort;
    private int propMaxObjSize;


    public Server(){
    }

    public void run() throws Exception{ //метод запускающий сервер

        EventLoopGroup bossGroup = new NioEventLoopGroup();//принимает входящее соединение
        EventLoopGroup workerGroup = new NioEventLoopGroup();//обрабатывает трафик принятого соединения
        // как только босс принимает соединение и регистрирует принятое соединение с работником

        getPropertiesValues(); //загружаем конфигурацию

        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)//наши подключения будут обрабатывать два
                    .channel(NioServerSocketChannel.class)//Здесь мы указываем использовать класс NioServerSocketChannel,
                    .childHandler(new ChannelInitializer<SocketChannel>() { //
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ObjectDecoder(propMaxObjSize, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new AuthHandler()
                                    //new ServerHandler()
                            );

                        }
                    })

                   .option(ChannelOption.SO_BACKLOG, 128)
                   .option(ChannelOption.TCP_NODELAY, true)
                   .childOption(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture future = b.bind(propPort).sync();
            future.channel().closeFuture().sync();

        } finally {
            //вызываем shutdown чтобы потоки аккуратно закрылись
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void getPropertiesValues() {
        //Блок отвечающий за свойства
        //обращаемся к файлу и получаем данные
        //для четния из файла настроек испльзуем Try with resources
        try (Reader in = new InputStreamReader(this.getClass().getResourceAsStream("/server.properties"))) {
            //инициализируем специальный объект Properties
            //типа Hashtable для удобной работы с данными
            Properties pr = new Properties();
            pr.load(in);
            //присваиваем переменным параметры из файла
            propPort = Integer.parseInt(pr.getProperty("port"));
            propMaxObjSize = Integer.parseInt(pr.getProperty("MAX_OBJ_SIZE"));
        } catch (IOException e) {
            System.err.println("Error: Property file does not exist");
        }
    }

    public static void main(String[] args) throws Exception {
        new Server().run();
    }
}
