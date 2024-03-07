package park.sangeun.studyspringbatch.concurrency.processor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import park.sangeun.studyspringbatch.concurrency.model.TransactionReq

@Component
open class ConcurrencyProcessor(
    @Value("\${backend.ip}") val BACKEND_IP: String,
    @Value("\${backend.port}") val BACKEND_PORT: Int,
) : ItemProcessor<TransactionReq, TransactionReq> {
    val log: Logger = LoggerFactory.getLogger(ConcurrencyProcessor::class.java)

    override fun process(item: TransactionReq): TransactionReq? {
        log.info("transactionReq: {}", item)

        WebClient.builder()
            .baseUrl("http://$BACKEND_IP:$BACKEND_PORT")
            .build()
            .post()
            .uri("/transaction/request")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(item)
            .retrieve()

        return item
    }
}