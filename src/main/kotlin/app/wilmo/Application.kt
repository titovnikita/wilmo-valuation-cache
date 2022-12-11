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

    val keyStoreFile = File("keystore.jks")
    val keystore = generateCertificate(
        file = keyStoreFile,
        keyAlias = sslKeyAlias,
        keyPassword = sslKeyPassword,
        jksPassword = sslKeyPassword
    )

    val environment = applicationEngineEnvironment {
        connector {
            port = 8080
            host = "138.2.176.179"
            rootPath = "/"
        }

        sslConnector(
            keyStore = keystore,
            keyAlias = sslKeyAlias,
            keyStorePassword = { sslKeyPassword.toCharArray() },
            privateKeyPassword = { sslKeyPassword.toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }

        module(Application::module)
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
