package biped.works.tosplit.transaction.data

import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val id: String,
    val metaId: String,
    val owner: String,
    val name: String,
    val description: String,
    val due: LocalDate,
    val value: BigDecimal,
    val recurrence: Recurrence,
)