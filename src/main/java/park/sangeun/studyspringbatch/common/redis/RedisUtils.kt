package park.sangeun.studyspringbatch.common.redis

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import park.sangeun.studyspringbatch.concurrency.model.TransactionReq
import java.text.ParseException

@Component
open class RedisUtils(
    @Value("\${backend.port}") val test: String,
) {
    private val logger = LoggerFactory.getLogger(RedisUtils::class.java)

    @Value("\${redis.key.transaction}")
    lateinit var REDIS_KEY: String

    @Autowired
    lateinit var factory: RedisConnectionFactory

    @Autowired
    lateinit var template: RedisTemplate<String, Any>

    fun getWaitingData(): TransactionReq? {
        val data = template.opsForZSet().range(REDIS_KEY, 0, 0)?.iterator()?:return null

        var target:TransactionReq? = null

        if (data.hasNext()) {
            target = data.next() as TransactionReq
        }

        return target
    }

    fun deleteWaitingData(transactionReq: TransactionReq?): Boolean {
        logger.info("[deleteWaitingData] !!! run")
        val removed = template.opsForZSet().remove(REDIS_KEY, transactionReq)
        if (removed != null && removed > 0) {
            // 삭제 완료
            return true
        }
        return false
    }
}