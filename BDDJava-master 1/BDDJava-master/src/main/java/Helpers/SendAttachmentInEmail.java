package Helpers;

import java.util.Properties;
import javax.mail.internet.*;

import java.io.File;

import javax.mail.*;
import javax.activation.*;

import static Helpers.GlobalData.env;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SendAttachmentInEmail {

	static WebActions actions = new WebActions();

	public static <DataSource> void sendAttachmentInEmail(String status, String tags)
			throws MessagingException {
		try {
			// String hostName = "smtp.gmail.com";
			final String username = actions.getPropertyValue("MailClientId");
			final String password = actions.getPropertyValue("MailKey");
			
			// Recipient's email address
			String[] recipientEmails = {

					actions.getPropertyValue("MailClientId")

			};
			// Setup properties for the SMTP server
			Properties properties = new Properties();
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.office365.com"); // Replace with your SMTP server
			properties.put("mail.smtp.port", "587"); // Replace with your SMTP server's port (e.g., 587 for TLS)

			// Create a Session with the properties
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			try {
				// Create a MimeMessage object
				Message message = new MimeMessage(session);

				// Set the sender's and recipient's email addresses
				message.setFrom(new InternetAddress(username));
				InternetAddress[] recipientAddresses = new InternetAddress[recipientEmails.length];
				for (int i = 0; i < recipientEmails.length; i++) {
					recipientAddresses[i] = new InternetAddress(recipientEmails[i]);
				}
				message.setRecipients(Message.RecipientType.TO, recipientAddresses);
				// message.setRecipients(Message.RecipientType.TO,
				// InternetAddress.parse(recipientEmail));

				Date timestamp = new Date();
				// Create the text part of the email
				BodyPart textPart = new MimeBodyPart();
//				String value = null;
//				for (int i = 0; i < tags.size(); i++) {
//
//					if (value != null) {
//						value = value + "," + tags.get(i);
//					} else {
//						value = tags.get(i);
//					}
//				}
				// Set the email subject and text
				// message.setText("This is the email content.");
				env = YamlLoader.loadYamlFile("config");
				String environment = String.valueOf(env.get("Environment"));
				Multipart multipart = new MimeMultipart();
				
				if (status.equalsIgnoreCase("Summary")) {

					WebActions.deletFileInDirectory("src\\reports\\Production_Summary.html");

					Utils.getAddZip("reports");

					message.setSubject("Production Sanity Summary report for " + tags + " execution");

					textPart.setText("Hi All," + '\n' + '\n'
							+ "Production sanity test execution for user journeys with frequency - " + tags
							+ " triggered at " + WebActions.executionStartDate + " has been successfully completed.\r\n"

							+ "Please refer the attached report for user journeys executed and its execution status."

							+ "\r\n"

							+ "PS: In case of any failure, separate alert mail would have been triggered."

							+ "\r\n" + "\r\n" + "Thanks,\r\n"

							+ "Production Sanity Test Automation Team");

				} else if (status.equalsIgnoreCase("Failed")) {

					Utils.getAddZip("reports//reports");

					message.setSubject("Alert: Issue with <" + WebActions.scenarioName + ">");

					textPart.setText("Hi All," + '\n' + '\n' + "An issue has been identified with the user journey – "
							+ WebActions.scenarioName + ", that requires immediate attention."

							+ '\n' + "\r\n" + "User Journey - " + WebActions.scenarioName + '\n'
							+ "Execution environment -" + environment + '\n' +

							"Execution Date & Time - " + WebActions.executionStartDate +

							'\n' + "\r\n"

							+ "Thanks,\r\n"

							+ "Production Sanity Test Automation Team");

				}
				multipart.addBodyPart(textPart);
				// Create the attachment part
				BodyPart attachmentPart = new MimeBodyPart();

				// Set the file path for the attachment
				String attachmentFilePath = "src\\reports.zip";
				// String attachmentFilePath = System.get
				DataSource source = (DataSource) new FileDataSource(attachmentFilePath);
				attachmentPart.setDataHandler(new DataHandler((javax.activation.DataSource) source));

				// ZipUtil.pack(new File("D:\\santhiya\\croma\\src\\reports\\"), new
				// File("D:\\santhiya\\croma\\src\\reports.zip"));

				// Set the attachment filename
				attachmentPart.setFileName(
						"reports.zip");
				multipart.addBodyPart(attachmentPart);

				// Set the multipart content of the message
				message.setContent(multipart);

				// Send the email
				Transport.send(message);
				String directoryPath = "src\\reports\\reports"; // Replace with the path to the directory you want to delete
				File directory = new File(directoryPath);
				if (directory.exists()) {
					if (WebActions.deleteDirectory(directory)) {
						System.out.println("Directory deleted successfully.");
					} else {
						System.err.println("Failed to delete directory.");
					}
				} else {
					System.err.println("Directory does not exist.");
		        }
				System.out.println("Email sent successfully!");

			} catch (MessagingException e) {
				e.printStackTrace();
				System.err.println("Error sending email: " + e.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
