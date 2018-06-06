package Cloud.Server;

import Cloud.Common.Color;
import Cloud.Common.MessageType.AuthMessage;
import Cloud.Common.MessageType.CommonMessage;
import Cloud.Common.MessageType.FileListMessage;
import Cloud.Common.WorkWithFiles;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

public class ServerAuthHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    private boolean autorized;
    private String clientLogin = null;
    private String clientPath;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(Color.ANSI_GREEN.getColor()+"Client connected from: "+ InetAddress.getLoopbackAddress()+Color.ANSI_RESET.getColor());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) return;

        if (!autorized) {
            if (msg instanceof AuthMessage) {
                if (SQLConnect.checkAvtorisation(((AuthMessage) msg).getLogin(), ((AuthMessage) msg).getPass())) {
                    clientLogin = ((AuthMessage) msg).getLogin();
                    autorized = true;

                    logger.info("Сlient authorized by name: " + Color.ANSI_GREEN.getColor() + clientLogin + " " + Color.ANSI_RESET.getColor());
                    ctx.writeAndFlush(new CommonMessage(1));
                    clientPath = "File/ServerFile/"+clientLogin;

                    WorkWithFiles.checkDir(clientPath);
                    ctx.writeAndFlush(new FileListMessage(WorkWithFiles.startSearchFilenameHashDate(clientPath)));
                    ctx.fireChannelRead(msg);

                } else {
                    logger.error(Color.ANSI_RED.getColor() + "Client cant be authorized by login: " + ((AuthMessage) msg).getLogin() + Color.ANSI_RESET.getColor());
                    ctx.writeAndFlush(new CommonMessage(3));
                    autorized = false;
                }
            } else {
                logger.error(Color.ANSI_RED.getColor() + "ServerAuthHandler received wrong object: " + msg.getClass() + Color.ANSI_RESET.getColor());
                return;
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
