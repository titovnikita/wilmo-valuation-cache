package app.wilmo.domain

import app.wilmo.data.local.ValuationLocalDataSource
import app.wilmo.data.remote.ValuationRemoteDataSource

interface IValuationRepository {
    suspend fun getValuation(token: String, params: String): String
}

class ValuationRepository(
    private val localDataSource: ValuationLocalDataSource,
    private val remoteDataSource: ValuationRemoteDataSource
) : IValuationRepository {
    override suspend fun getValuation(token: String, params: String): String {
        return remoteDataSource.getValuation(token, params)
    }
}