package Cloud.Server;

import Cloud.Common.Color;
import Cloud.Common.MessageType.*;
import Cloud.Common.WorkWithFiles;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * Класс обработки сообщений ServerHandler, выполняет основной функционал облочного хранилища.
     * Переменная rootPath отвечает за корневой каталог придожения.
     *
     */
    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    private String clientLogin = null;
    private String rootPath = "File/ServerFile/";
    private String clientPath;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            if (msg == null) return;
            logger.info(Color.ANSI_CYAN.getColor() + "Incoming message " + msg.getClass() + Color.ANSI_RESET.getColor());

            if (clientLogin == null & msg instanceof AuthMessage){
                clientLogin = ((AuthMessage) msg).getLogin();
                clientPath = rootPath+clientLogin+"/";
                return;
            }

            if (msg instanceof FileDataMessage) {
                FileDataMessage fdm = (FileDataMessage) msg;
                WorkWithFiles.checkDir(clientPath);
                Files.write(Paths.get(clientPath+fdm.getFileName()),fdm.getData(), StandardOpenOption.CREATE);
                ctx.writeAndFlush(new FileListMessage(WorkWithFiles.startSearchFilenameHashDate(clientPath)));
                return;

            } if(msg instanceof CommonMessage){
                if (((CommonMessage) msg).getCmd() == 4) {
                    ctx.writeAndFlush(new FileListMessage(WorkWithFiles.startSearchFilenameHashDate(clientPath)));
                    return;
                } else if (((CommonMessage) msg).getCmd() == 5) {
                    if (WorkWithFiles.deleteFile(clientPath+((CommonMessage) msg).getAttachment()[0])){
                        logger.info("File " + ((CommonMessage) msg).getAttachment()[0] + " deleted");
                        ctx.writeAndFlush(new FileListMessage(WorkWithFiles.startSearchFilenameHashDate(clientPath)));
                        return;
                    } else {
                        logger.error("Can`t delete " + ((CommonMessage) msg).getAttachment()[0] + " file");
                        return;
                    }
                } else if (((CommonMessage) msg).getCmd() == 6){
                    ctx.writeAndFlush(new FileDataMessage(clientPath+((CommonMessage) msg).getAttachment()[0]));
                    return;
                }

            } else {
                logger.error(Color.ANSI_RED.getColor() + "ServerHandler received wrong object: " + msg.getClass() + Color.ANSI_RESET.getColor());
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
