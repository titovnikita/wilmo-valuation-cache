package app.wilmo.database

import app.wilmo.config.IDatabaseConfigProvider
import app.wilmo.data.entity.ResponseCachesTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

interface IDatabaseFactory {
    fun init()
}

class DatabaseFactory(
    private val databaseConfigProvider: IDatabaseConfigProvider
) : IDatabaseFactory {

    override fun init() {
        val database = Database.connect(
            url = databaseConfigProvider.url,
            driver = databaseConfigProvider.driver,
            user = databaseConfigProvider.user,
            password = databaseConfigProvider.password
        )

        transaction(database) {
            SchemaUtils.create(ResponseCachesTable)
        }
    }

    companion object {
        suspend fun <T> dbQuery(block: suspend () -> T): T =
            newSuspendedTransaction(Dispatchers.IO) { block() }
    }

}
