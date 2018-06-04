package Cloud.Common.MessageType;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDataMessage implements Serializable {

    private String fileName;
    private  long size;
    private byte[] data;

    public FileDataMessage(String filename){
        try {
            this.fileName = Paths.get(filename).getFileName().toString();
            this.size = Files.size(Paths.get(filename));
            this.data = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public byte[] getData() {
        return data;
    }
}
