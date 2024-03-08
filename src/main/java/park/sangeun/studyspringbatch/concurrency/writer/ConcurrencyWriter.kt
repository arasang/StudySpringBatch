package park.sangeun.studyspringbatch.concurrency.writer

import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component
import park.sangeun.studyspringbatch.common.redis.RedisUtils
import park.sangeun.studyspringbatch.concurrency.model.TransactionReq

@Component
class ConcurrencyWriter(
    private val redisUtils: RedisUtils,
) : ItemWriter<TransactionReq> {
    override fun write(chunk: Chunk<out TransactionReq>) {
        val items = chunk.items
        if (items.isNullOrEmpty()) {
            items.forEach { item ->
                if (!redisUtils.deleteWaitingData(item)) {
                    throw Exception("Delete 실패로 인한 배치 중단.")
                }
            }
        }
    }
}