package app.wilmo.config

interface IDatabaseConfigProvider {
    val url: String
    val driver: String
    val user: String
    val password: String
}

class DatabaseConfigProvider : IDatabaseConfigProvider {
    override val url: String = DATABASE_URL
    override val driver: String = DATABASE_DRIVER_CLASS
    override val user: String by lazy { System.getenv(ENV_VARIABLE_DATABASE_USER) }
    override val password: String by lazy { System.getenv(ENV_VARIABLE_DATABASE_PASSWORD) }
}

private const val ENV_VARIABLE_DATABASE_USER = "DATABASE_USER"
private const val ENV_VARIABLE_DATABASE_PASSWORD = "DATABASE_PASSWORD"

private const val DATABASE_URL = "jdbc:h2:file:./build/db"
private const val DATABASE_DRIVER_CLASS = "org.h2.Driver"
