package park.sangeun.studyspringbatch.concurrency.reader

import lombok.extern.slf4j.Slf4j
import org.springframework.batch.item.ItemReader
import org.springframework.stereotype.Component
import park.sangeun.studyspringbatch.common.redis.RedisConfig
import park.sangeun.studyspringbatch.common.redis.RedisUtils
import park.sangeun.studyspringbatch.concurrency.model.TransactionReq

@Component
@Slf4j
open class ConcurrencyReader(
    private val redisUtils: RedisUtils,
    private val redisConfig: RedisConfig
): ItemReader<TransactionReq> {

    override fun read(): TransactionReq? {
        redisUtils.factory = redisConfig.redisConnectionFactory()
        redisUtils.template = redisConfig.redisTemplate(redisUtils.factory)
        return redisUtils.getWaitingData()
    }

}