package app.wilmo

import app.wilmo.plugins.configureKoin
import app.wilmo.plugins.configureRouting
import app.wilmo.plugins.configureSecurity
import app.wilmo.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    embeddedServer(
        Jetty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
