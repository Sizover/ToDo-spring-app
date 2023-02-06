package test_library.filters


data class FilterTypeObject(
    val cleanLocator: ((String) -> String)?,
    val valueLocator: ((String) -> String)? = null,
    val clickLocator: ((String, String) -> String)
)