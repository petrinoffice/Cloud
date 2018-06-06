package Cloud.Server;

import Cloud.Common.Color;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerMain {
    /**
     * Основной класс серверной части облачного хранилища.
     * Метод run отвечает за создание netty сервера.
     * В конвеер pipeline подгружаются: ObjectDecoder и ObjectEncoder отвечающие за прием и отправку сообщений
     * Основная работа с входящими объектами происходит в ServerAuthHandler и ServerHandler.
     * ServerAuthHandler отвечает за проверку авторизации, любые сообщания без авторизации не попадут на обработку.
     * ServerHandler проводит основную работу с сообщениями.
     *
     * SQLConnect.connect() отвечает за создание подключения к базе данных.
     *
     */
    private static final int PORT = 8189;
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // 10 mb
    private static Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) throws Exception {
        try {
            SQLConnect.connect();
            logger.info(Color.ANSI_GREEN.getColor() + "Connection to data base established: "+ Color.ANSI_RESET.getColor());

        } catch (Exception e) {
            e.printStackTrace();
        }
        new ServerMain().run();
    }

    public void run() throws Exception {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(mainGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new ServerAuthHandler(),
                                    new ServerHandler()
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = b.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
