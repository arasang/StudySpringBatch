package park.sangeun.studyspringbatch.common.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.*
import park.sangeun.studyspringbatch.concurrency.model.TransactionReq

@Configuration
open class RedisConfig(
    @Value("\${spring.data.redis.host}") val REDIS_HOST: String,
    @Value("\${spring.data.redis.port}") val REDIS_PORT: Int,
    @Value("\${spring.data.redis.password}") val REDIS_PW: String
) {
    @Bean
    open fun redisConnectionFactory(): RedisConnectionFactory {
        val config = RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT)
        config.setPassword(REDIS_PW)

        return LettuceConnectionFactory(config)
    }

    @Bean
    open fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any>{
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = factory
        redisTemplate.keySerializer = StringRedisSerializer();
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(TransactionReq::class.java)

        return redisTemplate
    }

    @Bean
    open fun redisCacheManager(factory: RedisConnectionFactory): RedisCacheManager {
        val redisCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))

        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(factory)
            .cacheDefaults(redisCacheConfig)
            .build()
    }
}