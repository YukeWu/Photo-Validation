package yukewu.project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import yukewu.project.backend.dao.DbRepository;
import yukewu.project.backend.dao.Image;
import yukewu.project.backend.dao.User;

@Service
public class DataService {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private DbRepository dbRepositoryImpl;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    public void saveUserAndImage(User user, Image image){
        dbRepositoryImpl.saveUser(user);
        dbRepositoryImpl.saveImage(image);
    }

    public User getUser(String userId) throws Exception{
        return readFromCache(userId);
    }

    private User readFromCache(String key) throws Exception{
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        User user = null;
        if(!stringRedisTemplate.hasKey(key)){
            user = dbRepositoryImpl.getUser(key);
            System.out.println("cache miss!");
            saveToCache(user);
        } else {
            String json = ops.get(key);
            user = mapper.readValue(json, User.class);
            System.out.println("cache hit!");
        }
        return user;
    }

    private void saveToCache(User user) throws JsonProcessingException {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String json = mapper.writeValueAsString(user);
        String key = user.getUserId();
        ops.set(key, json);
    }
}
