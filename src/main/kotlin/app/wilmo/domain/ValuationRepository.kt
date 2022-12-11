package app.wilmo.domain

import app.wilmo.data.local.ValuationLocalDataSource
import app.wilmo.data.remote.ValuationRemoteDataSource
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

interface IValuationRepository {
    suspend fun getValuation(token: String, params: String): String
}

class ValuationRepository(
    private val localDataSource: ValuationLocalDataSource,
    private val remoteDataSource: ValuationRemoteDataSource
) : IValuationRepository {

    override suspend fun getValuation(token: String, params: String): String {
        val query = Json.decodeFromString<JsonObject>(params).toString() // to format json properly
        val cache = localDataSource.getResponseCache(query)

        if (cache == null || cache.isExpired) {
            val valuation = remoteDataSource.getValuation(token, query)

            if (cache != null) {
                val updatedSuccessfully = localDataSource.updateResponseCache(cache.id, query, valuation)
                if (updatedSuccessfully) {
                    return valuation
                }
            }

            return localDataSource.addResponseCache(query, valuation)?.response ?: valuation
        } else {
            return cache.response
        }
    }

}