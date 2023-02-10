package test_library.filters

enum class FilterEnum (val filter: FilterObject) {
    `Дата_принятия`(FilterObject("Дата принятия", "Дата принятия в обработку", FilterTypeEnum.DATE,)),

    `Дата_регистрации`(FilterObject("Дата регистрации", "Дата регистрации", FilterTypeEnum.DATE,)),

    `Дата-время`(FilterObject("Дата/время", "Дата/время", FilterTypeEnum.DATE,)),

    `Охват`(FilterObject("Охват", "Территориальный охват", FilterTypeEnum.POOLBUTTONS)),

    `Статусы`(FilterObject("Статусы", "Статусы карточки", FilterTypeEnum.POOLBUTTONS)),

    `Уровни`(FilterObject("Уровни", "Уровень происшествия", FilterTypeEnum.POOLBUTTONS)),

    `Источники`(FilterObject("Источники", "Источники событий", FilterTypeEnum.POOLBUTTONS)),

    `Угрозы`(FilterObject("Угрозы людям", "Угроза людям", FilterTypeEnum.RADIOBUTTON)),

    `Файлы`(FilterObject("Файлы", "Есть файлы", FilterTypeEnum.RADIOBUTTON)),

    `Пользователь`(FilterObject("Пользователь", "Пользователь системы", FilterTypeEnum.RADIOBUTTON)),

    `Типы_происшествий`(FilterObject("Типы происшествий", "Типы происшествий", FilterTypeEnum.HIERARCHICATALOG)),

    `МО`(FilterObject("МО", "Муниципальные образования", FilterTypeEnum.HIERARCHICATALOG)),

    `Метки`(FilterObject("Метки", "Метки", FilterTypeEnum.HIERARCHICATALOG)),

    `Адрес`(FilterObject("Адрес", "Адрес происшествия", FilterTypeEnum.FLATCATALOG)),

    `Службы`(FilterObject("Службы", "Службы", FilterTypeEnum.FLATCATALOG)),

    `Оператор`(FilterObject("Оператор", "Оператор", FilterTypeEnum.FLATCATALOG)),

    `Идентификаторы`(FilterObject("Идентификаторы", "Идентификаторы", FilterTypeEnum.FLATCATALOG))
}