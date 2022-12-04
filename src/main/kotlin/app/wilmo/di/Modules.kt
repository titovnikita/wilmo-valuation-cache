import app.wilmo.auth.ITokenProvider
import app.wilmo.auth.TokenProvider
import app.wilmo.data.local.ValuationLocalDataSource
import app.wilmo.data.remote.ValuationRemoteDataSource
import app.wilmo.domain.IValuationRepository
import app.wilmo.domain.ValuationRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {
    single<ITokenProvider> { TokenProvider() }
}

val networkModule = module {
    single {
        HttpClient(CIO) {
            expectSuccess = true
        }
    }
}

val domainModules = module {
    single(named(NAMED_VALUATION_LOCAL_DATA_SOURCE)) { ValuationLocalDataSource() }
    single(named(NAMED_VALUATION_REMOTE_DATA_SOURCE)) { ValuationRemoteDataSource(get()) }
    single<IValuationRepository> {
        ValuationRepository(
            localDataSource = get(named(NAMED_VALUATION_LOCAL_DATA_SOURCE)),
            remoteDataSource = get(named(NAMED_VALUATION_REMOTE_DATA_SOURCE))
        )
    }
}

val koinModules = listOf(
    authModule,
    networkModule,
    domainModules
)

private const val NAMED_VALUATION_LOCAL_DATA_SOURCE = "valuation_local_source"
private const val NAMED_VALUATION_REMOTE_DATA_SOURCE = "valuation_remote_source"
