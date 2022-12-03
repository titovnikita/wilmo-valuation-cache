package app.wilmo.plugins

import io.ktor.server.application.*
import koinModules
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()

        modules(koinModules)
    }
}

inline fun <reified T> T.getLogger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
