package home.stat;

import org.bson.Document;

public class Statistics {
    private static int sentMessages = 0;
    private static int receivedMessages = 0;

    public static void incrementSentMessages() {
        sentMessages++;
    }

    public static void incrementReceivedMessages() {
        receivedMessages++;
    }

    public static Document getStats() {
        Document doc = SearchQueryStat.getStats();
        doc.put("Sent", sentMessages);
        doc.put("Received", receivedMessages);

        return doc;
    }

    public static void clear() {
        sentMessages = 0;
        receivedMessages = 0;
    }
}
