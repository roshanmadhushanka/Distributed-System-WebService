package home.table;

import home.message.request.SearchRequest;
import java.util.ArrayList;
import java.util.List;

public class SearchResponseTable {
    private final static int maxPool = 100;
    private static List<String> requests = new ArrayList<>();

    public static void add(SearchRequest searchRequest) {
        if(requests.size() > maxPool) {
            requests.remove(maxPool-1);
        }
        requests.add(searchRequest.toString());
    }

    public static boolean isResponded(SearchRequest searchRequest) {
        if(requests.contains(searchRequest.toString())) {
            return true;
        } else {
            add(searchRequest);
            return false;
        }
    }

}
