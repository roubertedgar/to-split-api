package biped.works.tosplit.transaction

import biped.works.tosplit.core.toLocalDate
import biped.works.tosplit.transaction.data.TransactionLocator
import biped.works.tosplit.transaction.data.TransactionResponse
import biped.works.tosplit.transaction.data.domain.Transaction
import biped.works.tosplit.transaction.data.remote.TransactionRequest
import biped.works.tosplit.transaction.data.remote.TransactionUpdateRequest
import biped.works.tosplit.transaction.data.toDomain
import biped.works.tosplit.transaction.data.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

data class RemoteStatement(
    val timeSpan: RemoteTimeSpan,
    val balance: String,
    val transactions: List<Transaction>
)

data class RemoteTimeSpan(
    val entry: String,
    val conclusion: String
)

@RestController
@RequestMapping("/transaction")
class TransactionController(
    private val listTransactionsUseCase: ListTransactionsUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
    private val saveTransactionUseCase: CreateTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase
) {
    @GetMapping("/statement/{entry}/{conclusion}")
    fun getStatement(
        @PathVariable entry: String,
        @PathVariable conclusion: String
    ): ResponseEntity<RemoteStatement> {
        val transactions = listTransactionsUseCase(LocalDate.parse(entry), LocalDate.parse(conclusion))
        val balance = transactions.sumOf { it.value.amount }
        val timeSpan = RemoteTimeSpan(entry, conclusion)

        return ResponseEntity.ok(
            RemoteStatement(
                timeSpan = timeSpan,
                balance = balance.toString(),
                transactions = transactions
            )
        )
    }

    @GetMapping("/{entry}/{conclusion}")
    fun getTransactions(
        @PathVariable entry: String,
        @PathVariable conclusion: String
    ): ResponseEntity<List<Transaction>> {
        val transactions = listTransactionsUseCase(entry.toLocalDate(), conclusion.toLocalDate())
        return ResponseEntity.ok(transactions)
    }

    @GetMapping("/{id}")
    fun getTransaction(
        @PathVariable id: String,
    ): ResponseEntity<TransactionResponse> {
        val result = getTransactionUseCase(TransactionLocator(id))
        return ResponseEntity.ok(result.getOrNull()?.toResponse())
    }

    @PostMapping
    fun createTransaction(@RequestBody transactionRequest: TransactionRequest): ResponseEntity<Transaction> {
        val transaction = saveTransactionUseCase(transactionRequest.toDomain())
        return ResponseEntity.ok(transaction)
    }
}
