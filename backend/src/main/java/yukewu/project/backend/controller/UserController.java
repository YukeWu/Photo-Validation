package yukewu.project.backend.controller;

import java.util.Base64;
// import java.util.UUID;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.azure.core.annotation.Get;

import yukewu.project.backend.dao.Image;
import yukewu.project.backend.dao.User;
import yukewu.project.backend.service.DataService;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")

public class UserController {

    @Autowired
    private DataService dataService;

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@RequestBody String data){
        JSONObject jsonObject = new JSONObject(data);
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String base64 = jsonObject.getString("image").replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64);
        UUID uuid = UUID.randomUUID();
        String userId = uuid.toString();
        User user = new User(userId, name, email);
        Image image = new Image(userId, rawBytes);
        dataService.saveUserAndImage(user, image);
        return new ResponseEntity<>("Successfully created user", HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable String userId) throws Exception{
        User user = dataService.getUser(userId);
        if(user == null){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
