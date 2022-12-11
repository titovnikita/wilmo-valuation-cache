package app.wilmo.config

interface ITokenProvider {
    val token: String
}

class TokenProvider : ITokenProvider {
    override val token: String by lazy { System.getenv(ENV_VARIABLE_VALUATION_TOKEN) }
}

private const val ENV_VARIABLE_VALUATION_TOKEN = "VALUATION_TOKEN"
