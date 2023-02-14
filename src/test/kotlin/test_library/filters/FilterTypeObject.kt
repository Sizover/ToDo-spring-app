package test_library.filters


data class FilterTypeObject(
    // Конструктор для енума типа фильтра КИАП (FilterTypeEnum)

    // Условный локатор элемента клик по которому очистит фильтр
    val cleanLocator: ((String) -> String)?,

    // Условный локатор коллекции элементов содержащих все возможные значения фильтра (если значения ограничены классификаторами и их не более 20 (хардкод на стороне фронта))
    val valueLocator: ((String) -> String)? = null,

    // Условный локатор элемента с конкретным значением фильтра
    val clickLocator: ((String, String) -> String)
)