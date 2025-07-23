package Helpers;

import static Helpers.GlobalData.env;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.LinkedList;

import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.MessageCollectionPage;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.Attachment;
import com.microsoft.graph.models.EmailAddress;
import com.microsoft.graph.models.ItemBody;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.models.Recipient;
import com.microsoft.graph.requests.AttachmentCollectionPage;
import com.microsoft.graph.requests.AttachmentCollectionResponse;
import com.microsoft.graph.requests.GraphServiceClient;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.io.File;


public class MSGraphMailTrigger {
	static WebActions actions = new WebActions();
	
	private MSGraphMailTrigger() {
        // Private constructor to hide the implicit public one
    }
	
	public static String mailID = "";
	private static final Logger logger = Logger.getLogger(MSGraphMailTrigger.class.getName());

 	public static String RetrieveOtpFromMSO() throws InterruptedException, IOException {
		String OTPMailSubject ="OTP from Croma";
        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(actions.getPropertyValue("MailClientId"))
                .clientSecret(actions.getPropertyValue("MailClientSecret"))
                .tenantId(actions.getPropertyValue("TenantKey").trim())
                .build();
        
        List<String> scopes = new ArrayList<String>();
        scopes.add("https://graph.microsoft.com/.default");

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
        
        @SuppressWarnings("rawtypes")
		final GraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(tokenCredentialAuthProvider)
                        .buildClient();
          
        //Get OTP from mail   
        String yesterdayDate = LocalDate.now().minusDays(1).toString(); 
        WebActions.setWaitTime(5000);
        MessageCollectionPage messagePage = graphClient.users(actions.getPropertyValue("MailId").trim())
        		.mailFolders("inbox")
        		.messages().buildRequest()
        		.select("receivedDateTime,subject,body")
        		.filter("receivedDateTime ge " + yesterdayDate + " and subject eq '" + OTPMailSubject + "'")
        		.orderBy("receivedDateTime DESC")
        		.get();
        List<Message> messageList = messagePage.getCurrentPage();
        
        String otpString = "";
        String msgBodyContent = "";
        msgBodyContent = messageList.get(0).body.content;
    	otpString = msgBodyContent.split("Use OTP <strong>")[1].split("</strong>")[0];
       // ExtentReportSetup.test.info("OTP from Croma is -> "+ otpString);
		return otpString;
    }	
 	
 	@SuppressWarnings("resource")
	public static void sendMailWithAttachment(String status, String tags, String startDate) throws Exception {
 		 // Variable initialization
 		 String summaryMailBody = null;
         String summaryMailSubject = null;
         
 		final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
 				 .clientId(actions.getPropertyValue("MailClientId"))
                 .clientSecret(actions.getPropertyValue("MailClientSecret"))
                 .tenantId(actions.getPropertyValue("TenantKey").trim())
                .build();
        
        List<String> scopes = new ArrayList<String>();
        scopes.add("https://graph.microsoft.com/.default");

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
        
        @SuppressWarnings("rawtypes")
		final GraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(tokenCredentialAuthProvider)
                        .buildClient();
        
		env = YamlLoader.loadYamlFile("config");
		String environment = String.valueOf(env.get("Environment"));
		@SuppressWarnings("unchecked")
		List<String> toReceipient = (List<String>) env.get("ToRecipients");
		@SuppressWarnings("unchecked")
		List<String> ccRecipient = (List<String>) env.get("CCRecipients");
		  //Create New Message
        Message message = new Message();
		if (status.equalsIgnoreCase("Summary")) {
			WebActions.deleteFile("src\\reports\\Croma_Production_Summary.html");
			Utils.getAddZip("reports");
	        //Upload report file in Sharepoint
	        String sharePointLink = SharePointReportUpload.uploadFileOnSharepoint();
//	        StorageBlob storageBlob = new StorageBlob();
//	        storageBlob.uploadfileInContainer();
			summaryMailBody  = "Hi All,<br/><br/>" +
	                  "Production sanity test execution for user journeys with frequency - " + tags +" triggered at " + startDate + " has been successfully completed.<br/><br/>" +
	                  "Please refer to the attached SharePoint report at the following link: <a href=\"" + sharePointLink + "\">SharePoint Report Link</a>"+" for user journeys executed and its execution status.<br/><br/>" +
	                  "PS: In case of any failure, a separate alert email would have been triggered.<br/><br/>" +
	                  "Thanks,<br/>" +
	                  "Production Sanity Test Automation Team";
			summaryMailSubject = "Production Sanity Summary report for "+tags+" execution";  
			message.subject = summaryMailSubject;    
	        message.body = new ItemBody();
	        message.body.content = summaryMailBody;
		}else if (status.equalsIgnoreCase("Failed")) {
			Utils.getAddZip("reports//reports");
			summaryMailBody = "Hi All,<br/><br/>" +
	                  "An issue has been identified with the user journey – "+WebActions.scenarioName+", that requires immediate attention.<br/><br/>" +
	                  "<strong>User Journey</strong> - "+WebActions.scenarioName+"<br/>" +
	                  "<strong>Execution environment</strong> - "+ environment +"<br/>" +
	                  "<strong>Execution Date & Time</strong> - " + startDate + "<br/><br/>" +
	                  "Thanks,<br/>" +
	                  "Production Sanity Test Automation Team";
			summaryMailSubject = "Alert: Issue with <" + WebActions.scenarioName + ">";  
			message.subject = summaryMailSubject;    
	        message.body = new ItemBody();
	        message.body.content = summaryMailBody;
	        
	      //Add Attachment
	        message.hasAttachments = true;
	        AttachmentCollectionResponse response = new AttachmentCollectionResponse();
	        
	        FileAttachment fileAttachment = new FileAttachment();
			fileAttachment.name = "reports.zip";
			InputStream fileStream = new FileInputStream("src\\reports.zip");
			
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = fileStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			
			fileAttachment.contentBytes = buffer.toByteArray();
			fileAttachment.oDataType = "#microsoft.graph.fileAttachment";
			fileAttachment.id="54321";
				
			LinkedList<Attachment> attachmentsList = new LinkedList<Attachment>();
			attachmentsList.add(fileAttachment);
	        response.value = attachmentsList;
	        message.attachments = new AttachmentCollectionPage(response,null);
		}
	    message.body.contentType = BodyType.HTML;
        
	  //Add To Recipient
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        for(int i=0;i<toReceipient.size();i++) {
	        Recipient toRecipients = new Recipient();
	        EmailAddress emailAddress = new EmailAddress();
	        emailAddress.address = toReceipient.get(i);
	        toRecipients.emailAddress = emailAddress;
	        toRecipientsList.add(toRecipients);
	        message.toRecipients = toRecipientsList;
        }
        
         //Add CC Recipient
        LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
        for(int i=0;i<ccRecipient.size();i++) {
            Recipient ccRecipients = new Recipient();
            EmailAddress emailAddress1 = new EmailAddress();
        	emailAddress1.address = ccRecipient.get(i);
	        ccRecipients.emailAddress = emailAddress1;
	        ccRecipientsList.add(ccRecipients);
	        message.ccRecipients = ccRecipientsList;
        	}
      
        //Send Email
        graphClient.users(mailID)
                .sendMail(UserSendMailParameterSet
                        .newBuilder()
                        .withMessage(message)
                        .withSaveToSentItems(true)
                        .build())
                .buildRequest()
                .post();
        
        String directoryPath = "src\\reports\\reports";
		File directory = new File(directoryPath);
		if (directory.exists()) {
			if (WebActions.deleteDirectory(directory)) {
				logger.info("Directory deleted successfully.");
			} else {
				logger.severe("Failed to delete directory.");
			}
		} else {
			logger.severe("Directory does not exist.");
        }
 	}
 	
 	
 	public static int getInboxCount() {
		int totalItemCount =0;
	    try {
	        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
	                .clientId(actions.getPropertyValue("MailClientId"))
	                .clientSecret(actions.getPropertyValue("MailClientSecret"))
	                .tenantId(actions.getPropertyValue("TenantKey").trim())
	                .build();
 
	        List<String> scopes = new ArrayList<String>();
	        scopes.add("https://graph.microsoft.com/.default");
 
	        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, clientSecretCredential);
 
	        @SuppressWarnings("rawtypes")
	        final GraphServiceClient graphClient =
	                GraphServiceClient
	                        .builder()
	                        .authenticationProvider(tokenCredentialAuthProvider)
	                        .buildClient();
	        WebActions.setWaitTime(20000);
	        // Get inbox
	        MailFolder inbox = graphClient.users(actions.getPropertyValue("MailId").trim())  // Replace "me" with the user's email or user ID
	                .mailFolders("inbox")
	                .buildRequest()
	                .select("totalItemCount")
	                .get();
 
	        totalItemCount = inbox.totalItemCount;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return totalItemCount;
	}
 	
}

