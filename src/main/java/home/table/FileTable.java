package home.table;

import home.system.Evaluator;
import java.util.ArrayList;
import java.util.List;

public class FileTable {
    private static List<String> fileList = new ArrayList<String>(5);

    public static void add(String fileName) {
        fileList.add(fileName);
    }

    public static void add(List<String> fileNames) {
        fileList = fileNames;
    }

    public static List<String> search(String query) {
        List<String> resultSet = new ArrayList<String>();
        Evaluator evaluator = new Evaluator();
        for(String file: fileList) {
            if(evaluator.cosineSimilarity(query, file) > 0.5) {
                resultSet.add(file);
            }
        }
        return resultSet;
    }

    public static List<String> getFileList() {
        return fileList;
    }

    public static void display() {
        System.out.println("> File Table");
        System.out.println("------------");
        for(String file: fileList) {
            System.out.println(file);
        }
        System.out.println();
    }

}
