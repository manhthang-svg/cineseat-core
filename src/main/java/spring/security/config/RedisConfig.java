package spring.security.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 1. Tạo cấu hình mặc định với Serializer
        RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(GenericJacksonJsonRedisSerializer.builder().build()))
                .disableCachingNullValues();

        // 2. Tạo Map để cấu hình TTL riêng biệt cho từng Cache Name
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // Kế thừa cấu hình mặc định (để giữ lại Serializer) và chỉ ghi đè lại thời gian TTL
        cacheConfigurations.put("movie_list", defaultCacheConfiguration.entryTtl(Duration.ofMinutes(5)));

        // Bạn có thể thêm các vùng cache khác vào đây
        // cacheConfigurations.put("movie_details", defaultCacheConfiguration.entryTtl(Duration.ofDays(1)));

        // 3. Build CacheManager
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations) // Add cấu hình riêng
                .build();
    }
}