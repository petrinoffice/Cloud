package Cloud.Common;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.ArrayList;
import java.util.List;

public class WorkWithFiles {


    public List<String> startSearchFilenameHashDate(String directory) {
        List<String> fileNames = new ArrayList<>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory));
            for (Path path : directoryStream) {
                fileNames.add(path.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

}
