package app.wilmo.data.model

data class ResponseCache(val id: Long, val isExpired: Boolean, val query: String, val response: String)