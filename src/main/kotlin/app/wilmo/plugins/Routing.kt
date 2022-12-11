package app.wilmo.plugins

import app.wilmo.config.ITokenProvider
import app.wilmo.domain.IValuationRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val logger = getLogger()

    routing {
        val tokenProvider by inject<ITokenProvider>()
        val repository by inject<IValuationRepository>()

        post("/valuation") {
            val headers = call.request.headers
            val tokenHeader = headers["Token"]
            val authorizationHeader = headers["Authorization"]
            val body = call.receiveText()

            if (tokenProvider.token.isBlank()) {
                logger.error("Environmental token variable is empty!")
            }

            if (authorizationHeader.isNullOrBlank()) {
                logger.error("Authorization header is empty!")

                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = "Unauthorized access to Wilmo valuation endpoint."
                )

                return@post
            }

            if (tokenHeader.equals(tokenProvider.token).not()) {
                logger.error("Unauthorized call to caching server.")

                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = "Unauthorized access to Wilmo valuation endpoint."
                )

                return@post
            }

            call.respond(repository.getValuation(authorizationHeader, body))
        }
    }
}
