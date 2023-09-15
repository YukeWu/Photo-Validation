package yukewu.project.backend.dao;

public interface DbRepository {
    int saveUser(User user);

    int saveImage(Image image);

    User getUser(String userId);

    Image getImage(String userId);
}
