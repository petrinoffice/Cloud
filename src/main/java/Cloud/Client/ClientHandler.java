package Cloud.Client;

import Cloud.Common.MessageType.AuthMessage;
import Cloud.Common.MessageType.CommonMessage;
import Cloud.Common.MessageType.FileDataMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    /**
     * Creates a client-side handler.
     */
private Controller controller;
private FileDataMessage fileDataMessage = null;

    public ClientHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        ctx.writeAndFlush(new AuthMessage(controller.loginField.getText(),controller.passField.getText()));
        //ctx.writeAndFlush(new AuthMessage("LoginTest","taram pam pam"));

        if (fileDataMessage !=null){
            ctx.writeAndFlush(fileDataMessage);
            fileDataMessage=null;
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (msg == null) return;
        if (msg instanceof CommonMessage){
            if (((CommonMessage) msg).getCmd() == 1){
                System.out.println(controller);
                controller.authComplete();
                System.out.println("ok");
            } else if (((CommonMessage) msg).getCmd() == 3){
                controller.loginField.setStyle("-fx-text-inner-color: red;");;
            }
        } else if (true){} //
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    protected void sendFile(FileDataMessage fileDataMessage){
        this.fileDataMessage = fileDataMessage;
        System.out.println("222");
    }
}
