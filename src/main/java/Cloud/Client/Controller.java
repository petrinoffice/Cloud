package Cloud.Client;

import Cloud.Common.MessageType.AuthMessage;
import Cloud.Common.MessageType.CommonMessage;
import Cloud.Common.MyMessage;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.Socket;

public class Controller {
    public TextField loginField;
    public TextField passField;
    public HBox authPanel;
    public HBox actionPanel1;

    public void actionAuth(ActionEvent actionEvent) {
        ObjectEncoderOutputStream oeos = null;
        try (Socket socket = new Socket("localhost", 8189)) {
            oeos = new ObjectEncoderOutputStream(socket.getOutputStream());
            MyMessage textMessage = new MyMessage("Hello Object");
            oeos.writeObject(textMessage);
            oeos.flush();

            oeos.writeObject(new AuthMessage(loginField.getText(),passField.getText()));
            oeos.flush();

            ObjectDecoderInputStream odis = new ObjectDecoderInputStream(socket.getInputStream());
            Object o = odis.readObject();
            if (o instanceof CommonMessage){
                if (((CommonMessage) o).getCmd() ==  1){
authPanel.setVisible(false);
actionPanel1.setVisible(true);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                oeos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
