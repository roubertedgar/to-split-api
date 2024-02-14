package biped.works.tosplit.transaction.data

import java.math.BigDecimal
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate
import java.util.*

data class TransactionMetadata(
    val id: String,
    val name: String,
    val description: String,
    val value: BigDecimal,
    val recurrence: Recurrence
) {
    val conclusion = recurrence.conclusion

    fun createTransactions(timeSpan: TimeSpan = TimeSpan()) = recurrence.generateDupDates(timeSpan)
        .map { dueDate ->
            transaction(
                id = UUID.randomUUID().toString(),
                metaId = id,
                name = name,
                description = description,
                due = dueDate,
                value = value
            )
        }
}

fun LocalDate.withAdjustableDayOfMonth(dayOfMonth: Int): LocalDate {
    val lastDayOfMonth = month.length(isLeapYear)
    return if (dayOfMonth < lastDayOfMonth) withDayOfMonth(dayOfMonth) else withDayOfMonth(lastDayOfMonth)
}

data class TimeSpan(
    val start: LocalDate = LocalDate.MIN,
    val end: LocalDate = LocalDate.MAX
) {
    val startDay = start.dayOfMonth
}


object DateTools {
    fun min(first: LocalDate, second: LocalDate) = if (first.isBefore(second)) first else second
    fun max(first: LocalDate, second: LocalDate) = if (first.isAfter(second)) first else second
}

fun LocalDate.isBeforeOrEquals(other: ChronoLocalDate): Boolean {
    return isBefore(other) || this == other
}