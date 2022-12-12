package app.wilmo

import app.wilmo.plugins.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import java.io.File

fun main() {
    val sslKeyAlias = System.getenv(ENV_VARIABLE_SSL_ALIAS)
    val sslKeyPassword = System.getenv(ENV_VARIABLE_SSL_PASSWORD)

    val keyStoreFile = File("keystore/keystore.jks")
    val keystore = generateCertificate(
        file = keyStoreFile,
        keyAlias = sslKeyAlias,
        keyPassword = sslKeyPassword,
        jksPassword = sslKeyPassword
    )

    val environment = applicationEngineEnvironment {
        sslConnector(
            keyStore = keystore,
            keyAlias = sslKeyAlias,
            keyStorePassword = { sslKeyPassword.toCharArray() },
            privateKeyPassword = { sslKeyPassword.toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }
    }

    embeddedServer(Jetty, environment).start(wait = true)
}

fun Application.module() {
    // leave it in the first place, as some dependencies are injected in the next calls
    configureKoin()

    configureSerialization()
    configureSecurity()
    configureRouting()
    configureDatabase()
}

private const val ENV_VARIABLE_SSL_ALIAS = "SSL_ALIAS"
private const val ENV_VARIABLE_SSL_PASSWORD = "SSL_PASSWORD"
