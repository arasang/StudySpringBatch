package park.sangeun.studyspringbatch.concurrency.model

import java.io.Serializable

data class TransactionReq(
    var requestDh: String? = "",
    var cardId: String? = "",
    var amount: Int = 0,
    var transactionType: String? = "",
) : Serializable {
}