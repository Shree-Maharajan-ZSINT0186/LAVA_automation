package Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;
import com.azure.storage.blob.*;
public class StorageBlob {
	WebActions actions = new WebActions();
	
	public void uploadfileInContainer() throws IllegalArgumentException, NullPointerException, InterruptedException, IOException {
		
	BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(actions.getPropertyValue("storageConnection")).buildClient();
    BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(actions.getPropertyValue("StorageContainerName"));
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy_HHmm");
    Date now = new Date();
    String formattedDateTime = dateFormat.format(now);
    String tagString1 = String.join(",", GlobalData.runnerTags);
    tagString1 = tagString1.replace("@", "");
    BlobClient blobClient = containerClient.getBlobClient(formattedDateTime+"_"+tagString1+".zip");
    blobClient.uploadFromFile("src\\reports.zip");
	}
}
