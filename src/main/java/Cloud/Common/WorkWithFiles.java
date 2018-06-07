package Cloud.Common;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WorkWithFiles {
    /**
     * Класс обработки сообщений WorkWithFiles отвечает за единообразный подход
     * в работе с файлами.
     *
     */

    public static List<String> startSearchFilenameHashDate(String directory) {
        /**
         * Метод startSearchFilenameHashDate возвращает список всех вайлов в директории
         */
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

    public static void checkDir(String string){
        /**
         * Метод checkDir проверяет наличие необходимой дирректории
         * и в случае необходимости создает ее
         */
        if (!Files.isDirectory(Paths.get(string))) {
            try {
                Files.createDirectories(Paths.get(string));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteFile(String string){
        /**
         * Метод deleteFile удаляет файл
         * Возвращая результат удаления
         */
        Path path = Paths.get(string);
        try {
            Files.deleteIfExists(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
