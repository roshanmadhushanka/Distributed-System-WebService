package home.buffers;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class MessageBuffer {
    /*
        MessageBuffer
        =============
        Store message results until gui requests
     */

    private static List<Document> messages = new ArrayList<>();

    public static void put(String type, String message) {
        Document document = new Document();
        document.put("type", type);
        document.put("message", message);
        messages.add(document);
    }

    public static List<Document> get() {
        return messages;
    }
}
