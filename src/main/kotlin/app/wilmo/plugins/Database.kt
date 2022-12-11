package app.wilmo.plugins

import app.wilmo.database.IDatabaseFactory
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureDatabase() {
    val databaseFactory by inject<IDatabaseFactory>()
    databaseFactory.init()
}
