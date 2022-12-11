package app.wilmo.data.entity

import kotlinx.datetime.LocalDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

data class ResponseCacheEntity(val id: Long, val created: LocalDateTime, val query: String, val response: String)

object ResponseCachesTable : Table() {
    val id = long("id").autoIncrement()
    val created = datetime("date_created").defaultExpression(CurrentDateTime)
    val query = text("query")
    val response = text("response")

    override val primaryKey = PrimaryKey(id)
}