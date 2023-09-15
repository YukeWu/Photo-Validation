package yukewu.project.backend.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
// import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

import yukewu.project.backend.service.CustomVision;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")

public class ImageController {
    
    private static final String CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=cloudshell000406;AccountKey=3HJAPhlejAaCqGZK8ApzTKEyGcZDtG30yAO0hNWO8XPDnsrjp0L6K/3Oj1XDG1giGL+92dXqws8Q+AStuxP5TQ==;EndpointSuffix=core.windows.net";

    @GetMapping("/greeting") // http://localhost:8080/greeting?name=yukewu
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }

    @PostMapping("/images")
    public ResponseEntity<Object> uploadImage(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64); // string decoded to byte[]
        // String imageName = UUID.randomUUID() + ".png"; // random name for image
        // saveImageToFile(imageName, rawBytes);
        // saveToCloud(imageName, rawBytes);
        CustomVision.uploadYukeImage(rawBytes); // upload to Custom Vision
        return new ResponseEntity<>("Successfully uploaded image", HttpStatus.OK);
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateImage(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64); // string decoded to byte[]
        ResponseEntity<String> ret_val = CustomVision.validate(rawBytes);
        return ret_val;
    }

    @SuppressWarnings("unused")
    private void saveImageToFile(String imageName, byte[] image) throws IOException {
        File path = new File("./images/");
        if (!path.exists()) {
            path.mkdir();
        }
        Files.write(new File("./images/" + imageName).toPath(), image);
    }

    @SuppressWarnings("unused")
    private void saveToCloud(String imageName, byte[] image) {
        // Create a BlobServiceClient object using a connection string
        BlobServiceClient client = new BlobServiceClientBuilder().connectionString(CONNECTION_STRING).buildClient();

        // Create a unique name for the container (here it's a folder)
        String containerName = "images";

        // Create the container (if doesn't exist) and return a container client object
        BlobContainerClient blobContainerClient = client.createBlobContainerIfNotExists(containerName);

        // Get a reference to a blob (aka a file)
        BlobClient blobClient = blobContainerClient.getBlobClient(imageName);

        // Upload the blob
        // blobClient.uploadFromFile(localPath + fileName); // this is upload from local
        InputStream targetStream = new ByteArrayInputStream(image);
        blobClient.upload(targetStream);
    }
}
