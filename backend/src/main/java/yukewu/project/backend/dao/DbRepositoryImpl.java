package yukewu.project.backend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DbRepositoryImpl implements DbRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int saveUser(User user) {
        return jdbcTemplate.update(
                "insert into users (user_id, user_name, email) values(?,?,?)",
                user.getUserId(), user.getUserName(), user.getEmail());
    }

    @Override
    public int saveImage(Image image) {
        return jdbcTemplate.update(
                "insert into images (user_id, photo) values(?,?)",
                image.getUserId(), image.getPhoto());
    }

    @Override
    public User getUser(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User target = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
                rs.getString("user_id"),
                rs.getString("user_name"),
                rs.getString("email")), new Object[] { userId });
        return target;
    }

    @Override
    public Image getImage(String userId) {
        String sql = "SELECT * FROM images WHERE user_id = ?";
        Image target = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Image(
                rs.getString("user_id"),
                rs.getBytes("photo")), new Object[] { userId });
        return target;
    }

}
