package Helpers;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class Description: This class is to Get data from YAML file
 *
 */
public class YamlLoader {
	
	public YamlLoader() {
        // Private constructor to hide the implicit public one
    }

	/**
	 * Method Description: This method is used to Load and Get data from YAML and Stored in Map
	 * @param fileName
	 * @return
	 * @throws NullPointerException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadYamlFile(String fileName) throws NullPointerException, IOException {
		try(FileInputStream finput= new FileInputStream(new File("src/test/resources/yaml/" + fileName))) {
	        Yaml yaml = new Yaml();
	        Map<String, Object> yamlKeys = new HashMap<>();
	        Map<String, Map<String, Object>> values = (Map<String, Map<String, Object>>) yaml.load(finput);

	        for (Map.Entry<String, Map<String, Object>> entry : values.entrySet()) {
	            yamlKeys.putAll(entry.getValue());
	        }
	        
	        return yamlKeys;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new AssertionError("Error loading yaml file - " + fileName);
	    } 
	}

    
    /**
     * Method Description: This method is used to Get Host name from YAML file based on ENV
     * @param fileName
     * @param key
     * @return
     * @throws FileNotFoundException
     */
    public static String getHostFromYamlBasedOnEnv(String fileName, String key) throws FileNotFoundException{
    	Map<String, Object> testDataValues;
    	String envVal = "";
    	String value = null;
        try{
        	testDataValues=loadYamlFile(fileName);
        	envVal = String.valueOf(testDataValues.get("Execution Instance"));
        	try(InputStream input = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\yaml\\" + fileName)) {
	        	Yaml yaml = new Yaml();
	            Map<String, Map<String, String>> config = yaml.load(input);
	            Map<String, String> environmentData = config.get(envVal);
	            Object retrievedValue = environmentData.get(key);          
	            if (retrievedValue instanceof Integer) {
	                value = Integer.toString((Integer) retrievedValue);
	            } else if (retrievedValue != null) {
	                value = retrievedValue.toString();
	            }
	            } 
        }catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException("YAML file not found - " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Error loading yaml file - " + fileName);
        }
        return value;
    }
    
    public static String getUserNameAndPasswordFromYamlBasedOnURL(String fileName, String key) throws FileNotFoundException{
    	Map<String, Object> testDataValues;
    	String envVal = "";
    	String value = null;
        try{
        	testDataValues=loadYamlFile(fileName);
        	envVal = String.valueOf(testDataValues.get("Users"));
        	try(InputStream input = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\yaml\\" + fileName)) {
	        	Yaml yaml = new Yaml();
	            Map<String, Map<String, String>> config = yaml.load(input);
	            Map<String, String> environmentData = config.get(envVal);
	            Object retrivedValue = environmentData.get(key);       
	            if (retrivedValue instanceof Integer) {
	                value = Integer.toString((Integer) retrivedValue);
	            } else if (retrivedValue != null) {
	                value = retrivedValue.toString();
	            }
	            } 
        }catch (IOException e) {
            e.printStackTrace();
            throw new FileNotFoundException("YAML file not found - " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Error loading yaml file - " + fileName);
        }
        return value;
    }
}
