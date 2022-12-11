import app.wilmo.config.DatabaseConfigProvider
import app.wilmo.config.IDatabaseConfigProvider
import app.wilmo.config.ITokenProvider
import app.wilmo.config.TokenProvider
import app.wilmo.data.dao.IValuationDao
import app.wilmo.data.dao.ValuationDao
import app.wilmo.data.local.ValuationLocalDataSource
import app.wilmo.data.remote.ValuationRemoteDataSource
import app.wilmo.database.DatabaseFactory
import app.wilmo.database.IDatabaseFactory
import app.wilmo.domain.IValuationRepository
import app.wilmo.domain.ValuationRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val configModule = module {
    single<ITokenProvider> { TokenProvider() }
    single<IDatabaseConfigProvider> { DatabaseConfigProvider() }
}

val networkModule = module {
    single {
        HttpClient(CIO) {
            expectSuccess = true
        }
    }
}

val databaseModule = module {
    single<IDatabaseFactory> { DatabaseFactory(get()) }
    single<IValuationDao> { ValuationDao() }
}

val domainModules = module {
    single(named(NAMED_VALUATION_LOCAL_DATA_SOURCE)) { ValuationLocalDataSource(get()) }
    single(named(NAMED_VALUATION_REMOTE_DATA_SOURCE)) { ValuationRemoteDataSource(get()) }
    single<IValuationRepository> {
        ValuationRepository(
            localDataSource = get(named(NAMED_VALUATION_LOCAL_DATA_SOURCE)),
            remoteDataSource = get(named(NAMED_VALUATION_REMOTE_DATA_SOURCE))
        )
    }
}

val koinModules = listOf(
    configModule,
    networkModule,
    databaseModule,
    domainModules
)

private const val NAMED_VALUATION_LOCAL_DATA_SOURCE = "valuation_local_source"
private const val NAMED_VALUATION_REMOTE_DATA_SOURCE = "valuation_remote_source"
