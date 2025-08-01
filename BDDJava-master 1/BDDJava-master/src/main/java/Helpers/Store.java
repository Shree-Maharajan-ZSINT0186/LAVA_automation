package Helpers;

import java.util.ArrayList;
import java.util.List;

public class Store {
    public static List<String> titleIdList = new ArrayList<>();

    public static void addTitleId(String id) {
        titleIdList.add(id);
    }

    public static List<String> getTitleIds() {
        return titleIdList;
    }
}
