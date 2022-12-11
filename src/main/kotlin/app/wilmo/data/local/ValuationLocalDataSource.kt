package app.wilmo.data.local

import app.wilmo.data.dao.IValuationDao
import app.wilmo.data.entity.ResponseCacheEntity
import app.wilmo.data.model.ResponseCache
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.Period

class ValuationLocalDataSource(
    private val dao: IValuationDao
) {

    suspend fun addResponseCache(query: String, response: String): ResponseCache? =
        dao.add(query, response)?.let(::map)

    suspend fun updateResponseCache(id: Long, query: String, response: String): Boolean =
        dao.update(id, query, response)

    suspend fun getResponseCache(query: String): ResponseCache? =
        dao.get(query)?.let(::map)

    suspend fun deleteResponseCache(query: String) {
        dao.delete(query)
    }

    private fun map(from: ResponseCacheEntity): ResponseCache {
        val creationDate = from.created.date
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        return ResponseCache(
            id = from.id,
            isExpired = Period.between(
                creationDate.toJavaLocalDate(),
                currentDate.toJavaLocalDate()
            ).days > CACHE_EXPIRATION_DAYS,
            query = from.query,
            response = from.response
        )
    }


}

private const val CACHE_EXPIRATION_DAYS = 14
