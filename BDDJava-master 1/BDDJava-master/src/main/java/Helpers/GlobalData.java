package Helpers;

import java.util.*;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Class Description: This class is to declare and initialize the variables as global scope
 *
 */
public class GlobalData {
    public static Map<String, Object> env;
    public static By setOfLocators;
    public static Map<String, List<String>> envValues;
    public static JSONObject jsonObj = null;
    public static JSONObject jsonBlockObj = null;
    public static String timestamp= null;
    public static List<String> runnerTags = new ArrayList<>();
    public static List<String> scenarioStatus = new ArrayList<>();
    
    // Private constructor to hide the implicit public one
    private GlobalData() {
        // Private constructor to prevent instantiation
    }
}
