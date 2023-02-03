package test_library


data class FilterTypeObject(
    val cleanLocator: ((String) -> String)?,
    val valueLocator: ((String) -> String)? = null,
    val clickLocator: ((String, String) -> String))