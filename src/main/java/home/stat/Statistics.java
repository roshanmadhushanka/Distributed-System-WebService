package home.stat;

import java.util.ArrayList;

public class Statistics {
    private static int sentMessages = 0;
    private static int receivedMessages = 0;
    private static ArrayList<SearchQueryStat> searchQueryStats = new ArrayList<>();
    private static ArrayList<JoinQueryStat> joinQueryStats = new ArrayList<>();
    private static ArrayList<LeaveQueryStat> leaveQueryStats = new ArrayList<>();

    public static void incrementSentMessages() {
        sentMessages++;
    }

    public static void incrementReceivedMessages() {
        receivedMessages++;
    }

    public static void appendSearchStat(SearchQueryStat searchQueryStat) {
        searchQueryStats.add(searchQueryStat);
    }

    public static void appendJoinStat(JoinQueryStat joinQueryStat) {
        joinQueryStats.add(joinQueryStat);
    }

    public static void appendLeaveStat(LeaveQueryStat leaveQueryStat) {
        leaveQueryStats.add(leaveQueryStat);
    }

    public void displayStatistics() {
        System.out.println("Sent Messages");
        System.out.println("=============");
        System.out.println(sentMessages);
        System.out.println();

        System.out.println("Received Messages");
        System.out.println("=================");
        System.out.println(receivedMessages);
        System.out.println();

        System.out.println("Search Query Sats");
        System.out.println("=================");
        System.out.println("Sent Time\tReceived Time\tHops Count\tElapsed");
        for(SearchQueryStat searchQueryStat: searchQueryStats) {
            searchQueryStat.display();
        }
        System.out.println();

        System.out.println("Join Query Stats");
        System.out.println("================");
        System.out.println("Sent Time\tReceived Time\tElapsed");
        for(JoinQueryStat joinQueryStat: joinQueryStats) {
            joinQueryStat.display();
        }
        System.out.println();

        System.out.println("Leave Query Stats");
        System.out.println("=================");
        for(LeaveQueryStat leaveQueryStat: leaveQueryStats) {
            leaveQueryStat.display();
        }

    }

}
