package yukewu.project.backend.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    private final String hostname;

    private final int port;

    private final String password;

    public RedisConfig(
            @Value("${redis.host}") String hostname,
            @Value("${redis.port}") int port,
            @Value("${redis.password}") String password) {
        this.hostname = hostname;
        this.port = port;
        this.password = password;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(hostname);
        config.setPort(port);
        config.setPassword(password);
        JedisClientConfiguration jedisConfig = JedisClientConfiguration
                .builder()
                .useSsl()
                .build();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config, jedisConfig);
        return jedisConnectionFactory;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
