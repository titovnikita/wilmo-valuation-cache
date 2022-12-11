package app.wilmo.data.dao

import app.wilmo.data.entity.ResponseCacheEntity
import app.wilmo.data.entity.ResponseCachesTable
import app.wilmo.database.DatabaseFactory.Companion.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

interface IValuationDao {
    suspend fun get(query: String): ResponseCacheEntity?

    suspend fun add(query: String, response: String): ResponseCacheEntity?

    suspend fun update(id: Long, query: String, response: String): Boolean

    suspend fun delete(query: String): Boolean
}

class ValuationDao : IValuationDao {

    override suspend fun get(query: String): ResponseCacheEntity? = dbQuery {
        ResponseCachesTable
            .select { ResponseCachesTable.query eq query }
            .map(::map)
            .singleOrNull()
    }

    override suspend fun add(query: String, response: String): ResponseCacheEntity? = dbQuery {
        ResponseCachesTable.insert {
            it[ResponseCachesTable.query] = query
            it[ResponseCachesTable.response] = response
        }

        get(query)
    }

    override suspend fun update(id: Long, query: String, response: String): Boolean = dbQuery {
        ResponseCachesTable.update({ ResponseCachesTable.id eq id }) {
            it[ResponseCachesTable.query] = query
            it[ResponseCachesTable.response] = response
        } > 0
    }

    override suspend fun delete(query: String): Boolean = dbQuery {
        ResponseCachesTable.deleteWhere { ResponseCachesTable.query eq query } > 0
    }

    private fun map(row: ResultRow) = ResponseCacheEntity(
        id = row[ResponseCachesTable.id],
        created = row[ResponseCachesTable.created],
        query = row[ResponseCachesTable.query],
        response = row[ResponseCachesTable.response]
    )

}