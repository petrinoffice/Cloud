package Cloud.Server;

import Cloud.Common.Color;
import Cloud.Common.MessageType.AuthMessage;
import Cloud.Common.MessageType.CommonMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    private String clientLogin = null;
    private boolean isNotAuth = true;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(Color.ANSI_GREEN.getColor()+"Client connected from: "+ InetAddress.getLoopbackAddress()+Color.ANSI_RESET.getColor());
        //System.out.println("Client connected..."+ InetAddress.getLoopbackAddress());
        // Send greeting for a new connection.
        // ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        // ctx.write("It is " + new Date() + " now.\r\n");
        // ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            if (msg == null) return;
            logger.info(Color.ANSI_CYAN.getColor()+"Incoming message " + msg.getClass()+Color.ANSI_RESET.getColor());

            if(isNotAuth){
                if (msg instanceof AuthMessage){
                    if(SQLConnect.checkAvtorisation(((AuthMessage) msg).getLogin(), ((AuthMessage) msg).getPass())){
                        clientLogin = ((AuthMessage) msg).getLogin();
                        isNotAuth = false;

                        logger.info("Сlient authorized by name: " + Color.ANSI_GREEN.getColor() + clientLogin + " " + Color.ANSI_RESET.getColor());
                        ctx.write(new CommonMessage(1));
                        ctx.flush();

                    }else {
                        logger.error(Color.ANSI_RED.getColor()+"Client cant be authorized by login: "+ ((AuthMessage) msg).getLogin()+ Color.ANSI_RESET.getColor());
                        ctx.writeAndFlush(new CommonMessage(3));
                        isNotAuth = true;
                    }
                }
            }else if (true) { // добавить отбработку сообщений

            } else {
                logger.error(Color.ANSI_RED.getColor() + "Server received wrong object!"+ Color.ANSI_RESET.getColor());
                return;
            }

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Exception when work with message");
        cause.printStackTrace();
        ctx.close();
    }

}
