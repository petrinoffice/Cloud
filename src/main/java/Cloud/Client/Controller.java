package Cloud.Client;

import Cloud.Common.MessageType.CommonMessage;
import Cloud.Common.MessageType.FileDataMessage;
import Cloud.Common.WorkWithFiles;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import java.io.IOException;
import java.util.List;

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
    protected String pathToSearch = "File/ClientFile";


    public void actionAuth(ActionEvent actionEvent) throws IOException {

        refreshClientFile(new ActionEvent());

       Thread t = new Thread(()->{
        try {
            NettyClient.ClientRun(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        });
        t.setDaemon(true);
        t.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        Почему при закрыитии окна поток демон не завершается?
         */
        clientHandler = NettyClient.getClientHandler();
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
        clientHandler.sendMsg(new FileDataMessage(pathToSearch+"/"+localList.getSelectionModel().getSelectedItem()));
    }

    public void refreshClientFile(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            ObservableList<String> items = FXCollections.observableArrayList(workWithFiles.startSearchFilenameHashDate(pathToSearch));
            localList.setItems(items);
        });
    }

    public void refreshServerFile(List<String> files) {
        Platform.runLater(() -> {
            ObservableList<String> items = FXCollections.observableArrayList(files);
            cloudList.setItems(items);
        });
    }

    public void deleteClientFile(ActionEvent actionEvent) {
        WorkWithFiles.deleteFile(pathToSearch+"/"+localList.getSelectionModel().getSelectedItem());
        ObservableList<String> items = FXCollections.observableArrayList(workWithFiles.startSearchFilenameHashDate(pathToSearch));
        localList.setItems(items);
    }

    public void cloudDownloadFile(ActionEvent actionEvent) {
        clientHandler.sendMsg(new CommonMessage(6, cloudList.getSelectionModel().getSelectedItems().get(0).toString()));
    }

    public void cloudDeleteFile(ActionEvent actionEvent) {
        clientHandler.sendMsg(new CommonMessage(5,cloudList.getSelectionModel().getSelectedItems().get(0).toString()));
    }

    public void cloudRefreshList(ActionEvent actionEvent) {
        clientHandler.sendMsg(new CommonMessage(4));
    }
}
