package Helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;

import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

import java.io.File;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SharePointReportUpload {
	 static WebActions actions = new WebActions();
	private SharePointReportUpload() {
        // Private constructor to hide the implicit public one
    }
	
	static String graphUrl = "https://graph.microsoft.com/v1.0/Drives/";
    //static String folderPath = "/"+actions.getPropertyValue("SharePointFolderName");
    static String fileName = "reports.zip";
    static String filePath = System.getProperty("user.dir")+"/src/reports.zip";
	private static final Logger logger = Logger.getLogger(SharePointReportUpload.class.getName());

 	private static String getAccessToken() throws InterruptedException, ExecutionException, IOException { 		
 		
         Set<String> scope = Collections.singleton("https://graph.microsoft.com/.default");
        ConfidentialClientApplication app = ConfidentialClientApplication.builder(
        		actions.getPropertyValue("SharePointClientId"),
                ClientCredentialFactory.createFromSecret(actions.getPropertyValue("SharePointClientSecret")))
                .authority("https://login.microsoftonline.com/" + actions.getPropertyValue("TenantKey").trim())
                .build();

        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters
                .builder(scope)
                .build();

        CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
        String accessToken;
                try {
            accessToken = future.get().accessToken();
            return accessToken;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
 	}
 	 	
 	public static String uploadFileOnSharepoint() throws IOException, InterruptedException, ExecutionException, URISyntaxException {
        String accessToken = getAccessToken();
        String webUrl = null;
        HttpClient httpclient = HttpClients.createDefault();

        try {       	
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmm");
            Date now = new Date();    
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // IST timezone
            String formattedDateTime = sdf.format(now);
            String tagString1 = String.join(",", GlobalData.runnerTags);
            tagString1 = tagString1.replace("@", "");
            String fileName = formattedDateTime+"_"+tagString1+".zip";
        	
            HttpPut request = new HttpPut(graphUrl + actions.getPropertyValue("SharePointdriverId").trim() + "/root:/" +"/" + actions.getPropertyValue("SharePointFolderName").trim() + "/" + fileName + ":/content");
            request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

            File file = new File(filePath);
            InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201) {
            	logger.info("File uploaded successfully.");
            	webUrl = getWebUrl(fileName, accessToken);
                if (webUrl != null) {
                    logger.info("Web URL of the uploaded file: " + webUrl);
                } else {
                    logger.info("Failed to retrieve web URL for the uploaded file.");
                }
            } else {
            	logger.info("Failed to upload file. Status code: " + statusCode);
            }
        }catch(Exception e) {
        	e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
		return webUrl;
    }
 	
 	private static String getWebUrl(String fileName, String accessToken) throws IOException {
 	    HttpClient httpclient = HttpClients.createDefault();
 	    try {
 	        URIBuilder builder = new URIBuilder(graphUrl + actions.getPropertyValue("SharePointdriverId").trim() + "/root:/" +"/" + actions.getPropertyValue("SharePointFolderName").trim()+ "/" + fileName);
 	        builder.setParameter("$select", "webUrl");
 	        HttpGet request = new HttpGet(builder.build());
 	        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
 	        HttpResponse response = httpclient.execute(request);
 	        if (response.getStatusLine().getStatusCode() == 200) {
 	        	String responseBody = EntityUtils.toString(response.getEntity());
 	            JSONParser parser = new JSONParser();
 	            JSONObject json = (JSONObject) parser.parse(responseBody);
 	            return (String) json.get("webUrl");
 	        } else {
 	            logger.info("Failed to get web URL. Status code: " + response.getStatusLine().getStatusCode());
 	            return null;
 	        }
 	    } catch (Exception e) {
 	        e.printStackTrace();
 	        return null;
 	    } finally {
 	        httpclient.getConnectionManager().shutdown();
 	    }
 	}
	public static String readJsonFromSharePoint(String headername,String key) {
        HttpClient httpclient = HttpClients.createDefault();
        String value = null;
        try {
        	String accessToken = getAccessToken();
            HttpGet request = new HttpGet(graphUrl + actions.getPropertyValue("SharePointdriverId").trim() + "/root:/" +"/" + actions.getPropertyValue("SharePointJsonFolderName").trim()+ "/" + actions.getPropertyValue("SharePointJsonFileName").trim() + ":/content");
             request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
 
             // Execute the request and get the response
             HttpResponse response = httpclient.execute(request);
 
             // Check if the response is successful (status code 2xx)
             if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                 // Parse the response body as JSON
                 String fileContent = EntityUtils.toString(response.getEntity());
 
                 // Parse JSON content using Jackson
                 ObjectMapper objectMapper = new ObjectMapper();
                 JsonNode rootNode = objectMapper.readTree(fileContent);
 
                 // Extract value of "@SC-SearchProduct"
                 if (rootNode.has(headername)) {
                     JsonNode searchProductNode = rootNode.get(headername);
                     // Print the products array
                     JsonNode productsArray = searchProductNode.get(key);
                     if(productsArray!=null) {
                         value = productsArray.toString();
                         }else {
                        	 value = "DataNotPresent";
                         }
                     //value = productsArray.toString();
                 } else {
                	 Utils.WarningTestLog("Key "+headername+" not found in the JSON file");
                 }
             } else {
            	 Utils.WarningTestLog("Json file is not available in the path");
             }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
		return value;
    }
}
