package Cloud.Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public final class NettyClient {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8189"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
    private static final int MAX_OBJ_SIZE = 1024 * 1024 * 100; // 10 mb
    private static ClientHandler clientHandler;

    public static ClientHandler getClientHandler(){
        return clientHandler;
    }

    public static ClientHandler ClientRun(Controller controller) throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        clientHandler = new ClientHandler(controller);

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();

                            //p.addLast(new LoggingHandler(LogLevel.INFO));
                            p.addLast(
                                    new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    clientHandler);
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
        return clientHandler;
    }
}
