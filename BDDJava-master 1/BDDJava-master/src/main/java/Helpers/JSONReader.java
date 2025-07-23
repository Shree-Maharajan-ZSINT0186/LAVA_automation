package Helpers;

import static Helpers.GlobalData.*;
//import static genericFunctions.GlobalData.jsonBlockObj;
//import static genericFunctions.GlobalData.moduleName;

import java.io.FileReader;
import java.util.Map;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.openqa.selenium.By;

//import genericFunctions.JSONReader;
//import genericFunctions.ScreenShotCapture;
//import genericFunctions.Utils;


/**
 * Class Description: This class is to Retrieve data from JSON
 *
 */
public class JSONReader {
	

	static String extension=".json";
	static String userDirectory = System.getProperty("user.dir");

	static JSONParser parser = new JSONParser();
	
	/**
	 * Method Description: This method is used to Get JSON data based on Json object name
	 * @param jsonObjName
	 * @return
	 * @throws Throwable 
	 */
	public static String getJSONDataBlock(String jsonObjName) throws Throwable {
		String dataBlock=null;

		try {
			Map<String, Object> testDataValues=YamlLoader.loadYamlFile("config");
			String envVal =String.valueOf(testDataValues.get("Execution Instance"));
			String appVal =String.valueOf(testDataValues.get("ApplicationType"));
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(userDirectory+"\\src\\test\\resources\\testDataJSON\\"+ appVal+"\\"+envVal+extension);
			Object obj = jsonParser.parse(reader);
			JSONObject jsonData = (JSONObject) obj;
			jsonBlockObj = (JSONObject) jsonData.get(jsonObjName);
			dataBlock = jsonBlockObj.toString();
			

		}catch(Exception e) {
			Utils.failedTestLog("can't able to get json dataBlock "+jsonObjName+" from the Json file");
			ScreenShotCapture.importScreenToReports("dataBlock_fail");
			e.printStackTrace();
		}
		return dataBlock;
	}
	
	/**
	 * Method Description: This method is used to Get PlayLoad from JSON
	 * @param jsonObjName
	 * @return
	 * @throws Throwable 
	 */
	public static JSONObject getJSONPlayLoad(String jsonObjName) throws Throwable {
		JSONObject jsonBlockObj=null;
		try {

			Map<String, Object> testDataValues=YamlLoader.loadYamlFile("config");
			String envVal =String.valueOf(testDataValues.get("Execution Instance"));
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(userDirectory+"\\src\\test\\resources\\testDataJSON\\API\\"+envVal+extension);
			Object obj = jsonParser.parse(reader);
			JSONObject jsonData = (JSONObject) obj;
			jsonBlockObj = (JSONObject) jsonData.get(jsonObjName);
			//dataBlock = jsonBlockObj.toString();

		}catch(Exception e) {
			Utils.failedTestLog("can't able to get json dataBlock "+jsonObjName+" from the Json file");
			ScreenShotCapture.importScreenToReports("dataBlock_fail");
			e.printStackTrace();
		}
		return jsonBlockObj;
	}
	
	
	
	/**
	 * Method Description: This method is used to Get locators from JSON
	 * @param fileName
	 * @param elementName
	 * @return
	 * @throws Throwable 
	 */
	public static By getLocatorsFromJSONFile(String fileName, String elementName) throws Throwable{
    	String elementPath = "";
		String type = "";
		String value = "";
		try {

			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(userDirectory+"\\src\\test\\resources\\LocatorOREndPoints\\Locators\\" + fileName+extension);
			Object obj = jsonParser.parse(reader);
			GlobalData.jsonObj = (JSONObject) obj;
			elementPath = GlobalData.jsonObj.get(elementName).toString();
			type = elementPath.split("::")[0];
			value = elementPath.split("::")[1];
			switch (type.toLowerCase()) {
			case "id":
				return By.id(value);

			case "xpath":
				return By.xpath(value);
			case "name":
				return By.name(value);

			case "classname":
				return By.className(value);

			case "cssselector":
				return By.cssSelector(value);

			case "linktext":
				return By.linkText(value);

			case "tagname":
				return By.tagName(value);

			default:
				throw new Exception("Unknown locator " + type + " : " + value);
			}
		}catch(Exception e) {
			Utils.failedTestLog("can't able to get locator "+elementName+" from the Json file");
			ScreenShotCapture.importScreenToReports("UnknownLocator_fail");
			e.printStackTrace();
			throw new AssertionError("Error loading JSON file - " + fileName);
		}
	}
	
	/**
	 * Method Description: This method is used to Get End Points from JSON
	 * @param fileName
	 * @param elementName
	 * @return
	 * @throws Throwable 
	 */
	public static String getEndPointsFromJSONFile(String fileName, String elementName) throws Throwable{
    	String elementPath = "";
		try {
			System.out.println("user directory"+JSONReader.userDirectory);
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(userDirectory+"\\src\\test\\resources\\LocatorOREndPoints\\EndPoints\\" + fileName+extension);
			Object obj = jsonParser.parse(reader);
			GlobalData.jsonObj = (JSONObject) obj;
			elementPath = GlobalData.jsonObj.get(elementName).toString();
		}catch(Exception e) {
			Utils.failedTestLog("can't able to get locator "+elementName+" from the Json file");
			ScreenShotCapture.importScreenToReports("UnknownEndPoint_fail");
			e.printStackTrace();
			throw new AssertionError("Error loading JSON file - " + fileName);
			
		}
		return elementPath;
	}
    
	public static String getJSONDataBlockKey(String fileName,String jsonObjName,String key) throws Throwable {
		String dataBlock=null;
		try {
			JSONParser jsonParser = new JSONParser();
			Map<String, Object> testDataValues=YamlLoader.loadYamlFile("config");
			String appVal =String.valueOf(testDataValues.get("ApplicationType"));
			FileReader reader = new FileReader(userDirectory+"\\src\\test\\resources\\testDataJSON\\"+appVal +"\\"+ fileName+extension);
			Object obj = jsonParser.parse(reader);
			JSONObject jsonData = (JSONObject) obj;
			jsonBlockObj = (JSONObject) jsonData.get(jsonObjName);
			dataBlock = jsonBlockObj.toString();
			JSONObject jsonObject = (JSONObject) parser.parse(dataBlock.toString());
			dataBlock = jsonObject.get(key).toString();
 
		}catch(Exception e) {
			Utils.failedTestLog("can't able to get json dataBlock "+jsonObjName+" from the Json file");
			ScreenShotCapture.importScreenToReports("dataBlock_fail");
			e.printStackTrace();
		}
		return dataBlock;
	}

}
