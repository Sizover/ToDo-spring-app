package test_library.filters


data class FilterObject(
// Конструктор объекта FilterObject, используемый в class FilterEnum.
// Набор атрибутов необходимый для работы с любым фильтром, на момент реализации.

    val shortName: String,
    val fullName: String,
    val type: FilterTypeEnum
)
