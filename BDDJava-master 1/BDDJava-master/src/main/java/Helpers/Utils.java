package Helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * The Utils class provides utility methods for common operations.
 */
public class Utils {
	
	private Utils() {
        // Private constructor to hide the implicit public one
    }
	
	static WebActions actions = new WebActions();
	private static final Logger logger = Logger.getLogger(Utils.class.getName());
	
    public static void clearFolder() {    	
    	String directoryPath = "src/reports"; // Use forward slashes for paths
        Path directory = Paths.get(directoryPath);

        if (Files.exists(directory) && Files.isDirectory(directory)) {
            try {
                Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                            logger.info("Deleted file: " + file.getFileName());
                        } catch (IOException e) {
                            logger.severe("Failed to delete file: " + file.getFileName() + ". Error: " + e.getMessage());
                        }
                    });

                logger.info("All files in the directory have been cleared.");
            } catch (IOException e) {
                logger.severe("Failed to clear the directory. Error: " + e.getMessage());
            }
        } else {
            logger.severe("The specified directory does not exist or is not a directory.");
        }
    }
    
    public static void getAddZip(String path) throws IOException {
		 String sourceFolderPath = "src//"+path;
	        String zipFilePath = "src//reports.zip";
	        try(FileOutputStream fos = new FileOutputStream(zipFilePath)) {
	            // Create a ZipOutputStream to write to the FileOutputStream
	            ZipOutputStream zipOut = new ZipOutputStream(fos);
	            // Get a list of files and directories in the source folder
	            File sourceFolder = new File(sourceFolderPath);
	            File[] files = sourceFolder.listFiles();
	            // Loop through each file and directory and add to the ZIP file
	            for (File file : files) {
	                if (file.isFile()) {
	                    addToZip(zipOut, file, sourceFolder.getAbsolutePath());
	                }
	            }
	            // Close the ZipOutputStream and FileOutputStream
	            zipOut.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
    public static void addToZip(ZipOutputStream zipOut, File file, String sourceFolder) throws NullPointerException, IOException {
    	 try(FileInputStream fis = new FileInputStream(file)){
	    	String entryName = file.getAbsolutePath().substring(sourceFolder.length() + 1);
	
	    	ZipEntry zipEntry = new ZipEntry(entryName);
	    	zipOut.putNextEntry(zipEntry);
	
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = fis.read(buffer)) > 0) {
	    		zipOut.write(buffer, 0, length);
	    	}
	    	
	    }catch(IOException e) {
	    	e.printStackTrace();
	    }
	}
	 
    
	 public static void failedTestLog(String Log) {
		 String failedMessage = "Failed in step named #"+ WebActions.stepName+ "# --"+ " "+Log; 
		 String styledFailedMessage = "<span style='color: red; font-weight: bold; font-style: italic;'>" + failedMessage + "</span>";
		 ExtentReportSetup.test.fail(styledFailedMessage);
	 }
	 
	 public static void WarningTestLog(String Log) {
		 String warningMessage = "Warning in step named #"+ WebActions.stepName+ "# --"+ " "+Log; 
		 String styledFailedMessage = "<span style='color: orange; font-weight: bold; font-style: italic;'>" + warningMessage + "</span>";
		 ExtentReportSetup.test.warning(styledFailedMessage);
	 }
	 
	 public static void failedSummaryTestLog(String Log) {
		 String failedMessage = "Scenario Failed: "+Log; 
		 String styledFailedMessage = "<span style='color: red; font-weight: bold; font-style: italic; font-size:18px;'>" + failedMessage + "</span>";
		 ExtentReportSetup.test.fail(styledFailedMessage);
	 }
	 
	 public static void passedTestLog(String Log) {
		 String styledPassedMessage = "<span style='color: green; font-weight: bold; font-style: italic;'>" + Log + "</span>";
		 ExtentReportSetup.test.pass(styledPassedMessage);
	 }
	 
	 public static void passedSummaryTestLog(String Log) {
		 String styledPassedMessage = "<span style='color: green; font-weight: bold; font-style: italic; font-size:18px;'>" + Log + "</span>";
		 ExtentReportSetup.test.pass(styledPassedMessage);
	 }
	 
	 public static void reportHeader(String scenarioName) {
		 String headerMessage = "<span style='color: white; font-weight: bold; font-style: italic; font-size: 17px;'>" + scenarioName + "</span>";
		 ExtentReportSetup.test.info(headerMessage);
	 }
	 
	 public static void failedStepInfoLog(String Log) {
		 String styledInfoMessage = "<span style='color: red; font-weight: bold; font-style: italic;'>" +"Error Occured : "+ Log +"</span>";
		 ExtentReportSetup.test.info(styledInfoMessage);
	 }
	 
	 public static void stepInfoLog(String Log) {
		 String styledInfoMessage = "<span style='color: white; font-weight: bold; font-style: italic;'>" + Log +"</span>";
		 ExtentReportSetup.test.info(styledInfoMessage);
	 }
	 
	 public static void getBrowserFailedLog(String sceName) throws IOException {
		 FileWriter writer = null;
		 try {
			 LogEntries logEntries = WebActions.driver.manage().logs().get(LogType.BROWSER);
			 // Create the folder if it doesn't exist
			 File folder = new File("src/reports/log");
			 if (!folder.exists()) {
				 if (folder.mkdirs()) {
					 logger.info("Folder created: " + folder);
				 } else {
					 logger.severe("Failed to create folder: " + folder);
					 return;
				 }
			 }
			 String logFileName = String.format("src/reports/log/Browser_Console_Log_%s_%s.txt",sceName,WebActions.getCurrentDate());
			 writer= new FileWriter(new File(logFileName));
			 for (LogEntry logEntry : logEntries) {
				 if (logEntry.getLevel() == Level.SEVERE || logEntry.getLevel() == Level.WARNING) {
					 // Write the log message to the file.
					 writer.write(logEntry.getMessage() + "\n");
				 }
			 }
		 }catch(Exception e) {
			 e.printStackTrace();
		 }finally{
			 writer.close();
		 }
	 }
}