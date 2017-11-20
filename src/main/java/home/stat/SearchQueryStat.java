package home.stat;

import dnl.utils.text.table.TextTable;
import home.system.Configuration;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchQueryStat {
    private String fileName;
    private String query;
    private long sentTime;
    private long receivedTime;
    private int hopsCount;
    private static ArrayList<SearchQueryStat> searchQueryStats = new ArrayList<SearchQueryStat>();

    public SearchQueryStat() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public int getHopsCount() {
        return hopsCount;
    }

    public void setHopsCount(int hopsCount) {
        this.hopsCount = hopsCount;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void display() {
        long elapsed = receivedTime - sentTime;
        System.out.println(fileName + "\t\t\t" + sentTime + "\t" + receivedTime + "\t" + hopsCount + "\t" + elapsed);
    }

    @Override
    public String toString() {
        long elapsed = receivedTime - sentTime;
        return fileName + "\t\t\t" +sentTime + "\t" + receivedTime + "\t" + hopsCount + "\t" + elapsed + "\n";
    }

    public static void append(SearchQueryStat searchQueryStat) {
        searchQueryStats.add(searchQueryStat);
    }

    public static String[] getHeaders() {
        return new String[] { "Query", "File Name", "Sent Time", "Received Time", "Hop Count", "Elapsed Time" };
    }

    public static Object[][] getData() {
        Object[][] data = new Object[searchQueryStats.size()][getHeaders().length];
        for(int row=0; row<searchQueryStats.size(); row++) {
            SearchQueryStat searchQueryStat = searchQueryStats.get(row);
            for(int col=0; col<getHeaders().length; col++) {
                data[row][0] = searchQueryStat.query;
                data[row][1] = searchQueryStat.fileName;
                data[row][2] = searchQueryStat.sentTime;
                data[row][3] = searchQueryStat.receivedTime;
                data[row][4] = searchQueryStat.hopsCount;
                data[row][5] = searchQueryStat.receivedTime - searchQueryStat.sentTime;
            }
        }
        return data;
    }

    public static void clear() {
        searchQueryStats = new ArrayList<SearchQueryStat>();
    }

    public static Document getStats() {
        double meanElapsedSqrd = 0;
        double meanElapsed = 0;
        double stdElapsed = 0;
        int count = searchQueryStats.size();
        long maxElapsed = Long.MIN_VALUE;
        long minElapsed = Long.MAX_VALUE;

        int maxHopCount = 0;
        int minHopCount = Integer.MAX_VALUE;
        double meanHopCount = 0;
        double meanHopCountSqrd = 0;
        double stdHopCount = 0;

        List<String> successQueries = new ArrayList<String>();
        List<String> allQueries = Arrays.asList(new String[]{ "Twilight",
                "Jack",
                "American Idol",
                "Happy Feet",
                "Twilight saga",
                "Happy Feet",
                "Happy Feet",
                "Feet",
                "Happy Feet",
                "Twilight",
                "Windows",
                "Happy Feet",
                "Mission Impossible",
                "Twilight",
                "Windows 8",
                "The",
                "Happy",
                "Windows 8",
                "Happy Feet",
                "Super Mario",
                "Jack and_Jill",
                "Happy Feet",
                "Impossible",
                "Happy Feet",
                "Turn Up The Music",
                "Adventures of Tintin",
                "Twilight saga",
                "Happy Feet",
                "Super Mario",
                "American Pickers",
                "Microsoft Office 2010",
                "Twilight",
                "Modern Family",
                "Jack and Jill",
                "Jill",
                "Glee",
                "The Vampire Diarie",
                "King Arthur",
                "Jack and Jill",
                "King Arthur",
                "Windows XP",
                "Harry Potter",
                "Feet",
                "Kung Fu Panda",
                "Lady Gaga",
                "Gaga",
                "Happy Feet",
                "Twilight",
                "Hacking",
                "King" });

        for(SearchQueryStat queryStat: searchQueryStats) {
            if(queryStat == null) {
                continue;
            }

            long elapsed = queryStat.receivedTime - queryStat.sentTime;
            if(elapsed > maxElapsed) {
                maxElapsed = elapsed;
            }

            if(elapsed < minElapsed) {
                minElapsed = elapsed;
            }

            int hopCount = Configuration.getMaxHopCount() - queryStat.hopsCount;
            if (hopCount < minHopCount && hopCount > 0) {
                minHopCount = hopCount;
            }

            if (hopCount > maxHopCount) {
                maxHopCount = hopCount;
            }

            meanElapsed += elapsed;
            meanElapsedSqrd += elapsed * elapsed;

            meanHopCount += hopCount;
            meanHopCountSqrd += hopCount * hopCount;

            if(!successQueries.contains(queryStat.query)) {
                successQueries.add(queryStat.query);
            }
        }

        meanElapsed = meanElapsed / count;
        meanElapsedSqrd = meanElapsedSqrd / count;
        stdElapsed = (meanElapsedSqrd - (meanElapsed * meanElapsed)) / count;

        meanHopCount = meanHopCount / count;
        meanHopCountSqrd = meanHopCountSqrd / count;
        stdHopCount = (meanHopCountSqrd - (meanHopCount*meanHopCount)) / count;

        double successCount = 0;
        for(String s: allQueries) {
            if(successQueries.contains(s)) {
                successCount++;
            }
        }

        double successRatio = 100.0 * successCount / 50.0;

        Document doc = new Document();
        doc.put("Success", successRatio);
        doc.put("Min Elapsed", minElapsed);
        doc.put("Max Elapsed", maxElapsed);
        doc.put("Mean Elapsed", meanElapsed);
        doc.put("Std. Elpased", stdElapsed);
        doc.put("Min Hops", minHopCount);
        doc.put("Max Hops", maxHopCount);
        doc.put("Mean Hops", meanHopCount);
        doc.put("Std. Hops", stdHopCount);

        return doc;
    }
}
