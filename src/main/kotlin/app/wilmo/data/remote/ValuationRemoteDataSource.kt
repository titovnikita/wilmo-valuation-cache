package app.wilmo.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class ValuationRemoteDataSource(
    private val client: HttpClient
) {
    suspend fun getValuation(token: String, body: String): String {
        return client.post(VALUATION_API_URL) {
            headers {
                append(HttpHeaders.Authorization, token)
                append(HttpHeaders.ContentType, "application/json")
            }
            setBody(body)
        }.body()
    }
}

private const val VALUATION_API_URL = "https://api.pricehubble.com/api/v1/valuation/property_value"
