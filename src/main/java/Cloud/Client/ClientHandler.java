package Cloud.Client;

import Cloud.Common.Color;
import Cloud.Common.MessageType.AuthMessage;
import Cloud.Common.MessageType.CommonMessage;
import Cloud.Common.MessageType.FileDataMessage;
import Cloud.Common.MessageType.FileListMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.event.ActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * Класс обработки сообщений ClientHandler, выполняет обработку сообщений клиентской части облочного хранилища.
     * В методе channelRead происходит обработка входящих сообщений.
     * В методе sendMsg происходит обработка исходящих сообщений.
     */
    private static Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private Controller controller;
    private ChannelHandlerContext ctx;
    //private FileDataMessage fileDataMessage = null;


    public ClientHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
        ctx.writeAndFlush(new AuthMessage(controller.loginField.getText(),controller.passField.getText()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg == null) return;
        logger.info(Color.ANSI_CYAN.getColor() + "Incoming message " + msg.getClass() + Color.ANSI_RESET.getColor());

        if (msg instanceof CommonMessage){
            if (((CommonMessage) msg).getCmd() == 1){
                controller.authComplete();
                logger.info("Клиент авторизовался на сервере");
            } else if (((CommonMessage) msg).getCmd() == 3){
                controller.loginField.setStyle("-fx-text-inner-color: red;");
                logger.error(Color.ANSI_RED.getColor()+ "Клиент не смог авторизоваться на сервере, полученно сообщение с кодом 3" + Color.ANSI_RESET.getColor());
            }
        } else if (msg instanceof FileListMessage){
            controller.refreshServerFile(((FileListMessage) msg).getFiles());
            return;

        } else if (msg instanceof FileDataMessage) {
            FileDataMessage fdm = (FileDataMessage) msg;
            try {
                Files.write(Paths.get(controller.pathToSearch+"/"+fdm.getFileName()),fdm.getData(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.refreshClientFile(new ActionEvent());
            return;

        } else {
            logger.error(Color.ANSI_RED.getColor() + "Server received wrong object!" + Color.ANSI_RESET.getColor());
            return;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        logger.error("При обоаботке сообщения возникло исключение.");
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    /*
    Метод sendMsg дичайший костыль, но избавится от него незнаю как
     */
    protected void sendMsg(Object o){
        ctx.writeAndFlush(o);
        logger.info(Color.ANSI_PURPLE.getColor() + "Сообщение " + o.getClass() + " отправленно" + Color.ANSI_RESET.getColor());
    }
}
