package Helpers;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Store {
    public static List<String> titleIdList = new ArrayList<>();
    private static String applicationId;

    // Setter method
    public static void addApplicationId(String id) {
        applicationId = id;
    }

    // Getter method
    public static String getApplicationId() {
        return applicationId;
    }
    public static void addTitleId(String id) {
        titleIdList.add(id);
    }

    public static List<String> getTitleIds() {
        return titleIdList;
    }
}
