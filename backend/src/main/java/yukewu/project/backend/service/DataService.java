package yukewu.project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import yukewu.project.backend.dao.DbRepository;
import yukewu.project.backend.dao.Image;
import yukewu.project.backend.dao.User;

@Service
public class DataService {
    @Autowired
    private DbRepository dbRepositoryImpl;

    @Transactional
    public void saveUserAndImage(User user, Image image){
        dbRepositoryImpl.saveUser(user);
        dbRepositoryImpl.saveImage(image);
    }
}
