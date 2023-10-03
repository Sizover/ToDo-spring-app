package otus_api_tests


data class Task(
    var id: Int? = null,
    val name: String,
    var priority: Priority,
    var completed: Boolean = false)
