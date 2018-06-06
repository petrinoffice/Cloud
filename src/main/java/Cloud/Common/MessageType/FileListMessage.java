package Cloud.Common.MessageType;

import java.io.Serializable;
import java.util.List;

public class FileListMessage implements Serializable {
    private List<String> files;

    public FileListMessage(List<String> files){
        this.files = files;
    }

    public List<String> getFiles() {
        return files;
    }
}
