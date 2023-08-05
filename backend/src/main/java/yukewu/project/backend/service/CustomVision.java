package yukewu.project.backend.service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class CustomVision {
    final static String PROJECT_ID = "08bdcb56-cafd-49e7-a5fd-7db657a73db8";
    final static String TRAINING_ENDPOINT = "https://eastus.api.cognitive.microsoft.com/";
    final static String PREDICTION_ENDPOINT = "https://customvisionyukewu-prediction.cognitiveservices.azure.com/customvision/v3.0/Prediction/08bdcb56-cafd-49e7-a5fd-7db657a73db8/classify/iterations/Model-1/image";
    final static String API_KEY = "6c781c6d5a8b4a3fa29c4252bc897656";

    static RestTemplate restTemplate = new RestTemplate();

    // Maps each tag to its Custom Vision tag ID
    final static public Map<String, String> tags = new HashMap<>() {
        {
            put("Yuke", "6846fbc3-8c35-4312-97cd-a46796159e63");
            put("Others", "15aadd77-a25d-429d-aeae-9395810a0626");
        }
    };

    /** Creates a new tag to categorize data in Custom Vision */
    public static void createTag(String tagName) throws JSONException {
        // Add params to URI
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", TRAINING_ENDPOINT);
        params.put("projectId", PROJECT_ID);
        params.put("name", tagName);

        String url = "{endpoint}/customvision/v3.3/Training/projects/{projectId}/tags?name={name}";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();

        // Set http request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", API_KEY);

        // Make http request and print result of success
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);
        JSONObject jsonObject = new JSONObject(response.getBody());

        System.out.println(jsonObject.getString("id") + ": " + tagName);
    }

    public static void uploadYukeImage(byte[] fileData) throws JSONException {
        uploadImage(tags.get("Yuke"), fileData);
    }

    /** Uploads a new image to the training set and tags it accordingly */
    public static void uploadImage(String tagId, byte[] fileData) throws JSONException {
        // Add params to URI
        Map<String, String> params = new HashMap<>();
        params.put("endpoint", TRAINING_ENDPOINT);
        params.put("projectId", PROJECT_ID);
        params.put("tagIds", tagId);

        String url = "{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.buildAndExpand(params).toUri();

        // Set request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 这里是2进制图像，也可以是其他类型，office/pdf/.mp4都可以
        headers.set("Training-key", API_KEY);

        // Make request and print result of success
        HttpEntity<byte[]> request = new HttpEntity<>(fileData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

        System.out.println(response.getBody());
    }

    /**
     * Sends a prediction request to Custom Vision endpoint and returns the result
     * of validation
     */
    public static ResponseEntity<String> validate(byte[] data) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/octet-stream");
        headers.add("Prediction-Key", "368431aa6958438894af7c8dc0a896e3");

        HttpEntity<byte[]> entity = new HttpEntity<>(data, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(PREDICTION_ENDPOINT, entity, String.class);
        return result;
    }

    public static void main(String args[]) throws JSONException {
        // createTag("Yuke");
        // createTag("Others");
        // uploadYukeImage(null);
    }
}
