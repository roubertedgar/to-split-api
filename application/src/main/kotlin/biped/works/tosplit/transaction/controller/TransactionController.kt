package biped.works.tosplit.transaction.controller

import biped.works.tosplit.transaction.CreateTransactionUseCase
import biped.works.tosplit.transaction.ListTransactionsUseCase
import biped.works.tosplit.transaction.data.Transaction
import biped.works.tosplit.transaction.data.remote.TransactionRequest
import biped.works.tosplit.transaction.data.toDomain
import java.time.LocalDate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transaction")
class TransactionController(
    private val createTransaction: CreateTransactionUseCase,
    private val listOperations: ListTransactionsUseCase
) {
    @GetMapping("/{entry}/{conclusion}")
    fun getTransactions(@PathVariable entry: String, @PathVariable conclusion: String): ResponseEntity<List<Transaction>> {
        val operations = listOperations(entry.toLocalDate(), conclusion.toLocalDate())
        return ResponseEntity.ok(operations)
    }

    @PostMapping
    fun createTransaction(@RequestBody transactionRequest: TransactionRequest) {
        createTransaction(transactionRequest.toDomain())
    }

    private fun String.toLocalDate() = LocalDate.parse(this)
}