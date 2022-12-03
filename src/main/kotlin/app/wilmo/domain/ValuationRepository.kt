package app.wilmo.domain

import app.wilmo.data.IDataSource

interface IValuationRepository {
}

class ValuationRepository(
    private val localDataSource: IDataSource,
    private val remoteDataSource: IDataSource
) : IValuationRepository {
}