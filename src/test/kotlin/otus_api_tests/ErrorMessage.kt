package otus_api_tests

data class ErrorMessage(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val path: String)