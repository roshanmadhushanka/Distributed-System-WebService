package home.buffers;

import home.message.response.SearchOKResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchBuffer {
    /*
        SearchBuffer
        ============
        Store search results until gui requests
     */

    private static List<SearchOKResponse> searchOKResponseList = new ArrayList<>();

    public static void put(SearchOKResponse searchOKResponse) {
        searchOKResponseList.add(searchOKResponse);
    }

    public static List<SearchOKResponse> get() {
        return searchOKResponseList;
    }


}
