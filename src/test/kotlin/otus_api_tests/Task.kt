package otus_api_tests

import com.fasterxml.jackson.annotation.JsonProperty

data class Task(
    var id: Int? = null,
    val name: String,
    var priority: Priority,
    var completed: Boolean = false)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}