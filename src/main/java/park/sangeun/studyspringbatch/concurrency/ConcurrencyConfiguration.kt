package park.sangeun.studyspringbatch.concurrency

import lombok.extern.slf4j.Slf4j
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import park.sangeun.studyspringbatch.common.redis.RedisConfig
import park.sangeun.studyspringbatch.common.redis.RedisUtils
import park.sangeun.studyspringbatch.concurrency.model.TransactionReq
import park.sangeun.studyspringbatch.concurrency.processor.ConcurrencyProcessor
import park.sangeun.studyspringbatch.concurrency.reader.ConcurrencyReader

@Slf4j
@Configuration
open class ConcurrencyConfiguration(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val redisUtils: RedisUtils,
    private val redisConfig: RedisConfig
) {
    companion object {
        const val CHUNK_SIZE: Int = 1;
    }

    val JOB_NAME = "CONCURRENCY_JOB"
    val STEP_NAME = "CONCURRENCY_STEP"

    @Bean
    open fun concurrencyJob(
        @Value("\${backend.ip}") BACKEND_IP: String,
        @Value("\${backend.port}") BACKEND_PORT: Int
    ): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .start(concurrencyStep(BACKEND_IP, BACKEND_PORT))
            .build()
    }

    @Bean
    open fun concurrencyStep(
        @Value("\${backend.ip}") BACKEND_IP: String,
        @Value("\${backend.port}") BACKEND_PORT: Int
    ): Step {
        return StepBuilder(STEP_NAME, jobRepository)
            .chunk<TransactionReq,TransactionReq>(CHUNK_SIZE, transactionManager)
            .reader(ConcurrencyReader(redisUtils, redisConfig))
            .processor (ConcurrencyProcessor(BACKEND_IP, BACKEND_PORT))
            .writer(ItemWriter {  })
            .build()
    }
}