package Cloud.Client;

import Cloud.Common.MessageType.FileDataMessage;
import Cloud.Common.WorkWithFiles;
import io.netty.channel.ChannelHandlerContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.io.IOException;

public class Controller {

    public TextField loginField;
    public TextField passField;
    public HBox authPanel;
    public HBox actionPanel1;
    public HBox actionPanel2;
    public ListView localList;
    public ListView cloudList;

    private WorkWithFiles workWithFiles = new WorkWithFiles();
    private ClientHandler clientHandler;
    private String pathToSearch = "File/ClientFile";


    public void actionAuth(ActionEvent actionEvent) throws IOException {

        refreshClientFile(new ActionEvent());

        new Thread(()->{
        try {
            NettyClient.ClientRun(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientHandler = NettyClient.getClientHandler();
        System.out.println("1111 "+clientHandler);
    }



    public void authComplete(){
        authPanel.setDisable(true);
        authPanel.setVisible(false);
        authPanel.setManaged(false);

        actionPanel1.setDisable(false);
        actionPanel1.setVisible(true);
        actionPanel1.setManaged(true);

        actionPanel2.setDisable(false);
        actionPanel2.setVisible(true);
        actionPanel2.setManaged(true);
    }

    public void sendClientFile(ActionEvent actionEvent) {
       // clientHandler.channelRead(, new FileDataMessage(pathToSearch+"/"+localList.getSelectionModel().getSelectedItem()));
        clientHandler.sendFile(new FileDataMessage(pathToSearch+"/"+localList.getSelectionModel().getSelectedItem()));

    }

    public void refreshClientFile(ActionEvent actionEvent) {
        ObservableList<String> items = FXCollections.observableArrayList(workWithFiles.startSearchFilenameHashDate(pathToSearch));
        localList.setItems(items);
    }

    public void deleteClientFile(ActionEvent actionEvent) {
    }
}
