package com.geekstomach.cloud.server;


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
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

public class Server {
    private static final Logger Logger = LogManager.getLogger(Server.class.getName());

    /*private int propPort = 8189;//должны ли они быть static?
    private int propMaxObjSize = 1024 * 1024 * 100;//10mb зачем мы инициализируем эти переменные если они читаются из файла свойств?
    */
    private static int propPort;
    private static int propMaxObjSize;//пока не используется

    public Server(){
    }

    public void run() throws Exception{ //метод запускающий сервер
/*
        NioEventLoopGroup-многопоточный цикл обработки событий, обрабатывающий операции ввода-вывода.
        Netty предоставляет различные реализации EventLoopGroup для различных видов транспорта.
        Мы реализуем серверное приложение в этом примере, и поэтому будут использоваться два NioEventLoopGroup.
        Первый, Часто называемый "boss", принимает входящее соединение.
        Второй, часто называемый "работник", обрабатывает трафик принятого соединения,
        как только босс принимает соединение и регистрирует принятое соединение с работником.
        Сколько потоков используется и как они сопоставляются с созданными каналами зависит от реализации
        EventLoopGroup и может быть даже настраивается с помощью конструктора.
*/
        EventLoopGroup bossGroup = new NioEventLoopGroup();//принимает входящее соединение
        EventLoopGroup workerGroup = new NioEventLoopGroup();//обрабатывает трафик принятого соединения
        // как только босс принимает соединение и регистрирует принятое соединение с работником

        getPropertiesValues(); //загружаем конфигурацию

        //Куча непонятного кода, которая нужна для запуска сервера
        try {
/*
            ServerBootstrap это вспомогательный класс, который создает сервер.
            Вы можете настроить сервер, используя канал напрямую.
            Тем не менее, обратите внимание, что это утомительный процесс,
            и вам не нужно делать это в большинстве случаев.
*/
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)//наши подключения будут обрабатывать два
                    //настраиваем каналы клиентов
                    .channel(NioServerSocketChannel.class)//Здесь мы указываем использовать класс NioServerSocketChannel,
                    // который используется для создания экземпляра нового канала для приема входящих соединений.
                    //.handler(new LoggingHandler(org.apache.log4j.Logger)) //Надо чтото делать с логгером
                    .childHandler(new ChannelInitializer<SocketChannel>() { //
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                   // new ServerHandler());
                                    new ProtocolDecoder());
                                    //Из набора байтов преобразуем в объект (оба класса нужны для сериализации)
                                    //new ObjectDecoder(propMaxObjSize, ClassResolvers.cacheDisabled(null)),
                                    //new ObjectEncoder());
                                   // new AuthGatewayHandler());
                        }
                    })
/*
            Указанный здесь обработчик всегда будет вычисляться вновь принятым каналом.
            ChannelInitializer - это специальный обработчик, предназначенный для помощи пользователю
            в настройке нового канала. Наиболее вероятно, что вы хотите настроить ChannelPipeline
            Нового канала путем добавления некоторых обработчиков, таких как DiscardServerHandler для
            реализации вашего сетевого приложения. Поскольку приложение усложняется, вполне вероятно,
            что вы добавите больше обработчиков в конвейер и в конечном итоге извлеките этот анонимный класс
            В класс верхнего уровня.
*/
                  //  .option(ChannelOption.SO_BACKLOG, 128)
/*
            Можно также установить параметры, которые являются определенными для реализации канала.
            Мы пишем сервер TCP / IP, поэтому нам разрешено устанавливать параметры сокетов, такие
            как tcpNoDelay, и поддерживать их. Пожалуйста, обратитесь к опции apidocs of Channel
            и конкретным реализациям настройки канала, чтобы получить обзор о поддерживаемых ChannelOptions
*/
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
/*
            Ты option() и childOption()? option() для NioServerSocketChannel, что принимает входящие соединения.
             childOption() для каналов, принимаемых материнской ServerChannel,
             который NioServerSocketChannel в этом случае.
*/
            Logger.info("Server started at port " + propPort);
            Logger.trace("trace");
            // Bind and start to accept incoming connections
            ChannelFuture future = b.bind(propPort).sync();
/*           Мы готовы идти. Осталось выполнить привязку к порту и запустить сервер.
            Здесь мы привязываем к порту все сетевые адаптеры (сетевые интерфейсные карты) в машине.
            Теперь вы можете вызывать метод bind() столько раз, сколько хотите (с разными адресами привязки.)
*/
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
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
            propMaxObjSize = Integer.parseInt(pr.getProperty("max_obj_size"));
        } catch (IOException e) {
            System.err.println("Error: Property file does not exist");
        }
    }

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        new Server().run();
    }
}
