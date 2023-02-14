package test_library.filters


enum class FilterTypeEnum(val filterType: FilterTypeObject) {
// Енум типов фильтров КИАП.
// В зависимости от типа, хранит условные локаторы и возвращает конкретные локаторы,
// по которым можно осуществить (если тип фильтра (реализация) и логика КИАП это допускают)
// очистку фильтра/выборку возможных значений/клик на конкретное значение


    //календарный тип фильтра
    DATE(FilterTypeObject(
        //cleanLocator
        null,
        //valueLocator
        null,
        //clickLocator
        fun(fullName: String, from_until: String): String {
            return "html/body/div[@role='presentation']//*[contains(text(),'$fullName')]/following-sibling::*//input[@placeholder='$from_until']"
        }
    )),
    // Фильтр с множественным выбором из фиксированного хардкодного (Классификаторы) пула значений.
    POOLBUTTONS(
        FilterTypeObject(
        //cleanLocator
        fun(fullName: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/following-sibling::*//*[@name='close']"
        },
        //valueLocator
        fun(fullName: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/following-sibling::*//*[text()]/text()/ancestor::button"
        },
        //clickLocator
        fun(fullName: String, value: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/following-sibling::*//*[text()='$value']/text()/ancestor::button"
        })
    ),
    // Фильтр с множественным выбором содержащий в качестве значений тот или иной не иерархический справочник КИАП
    FLATCATALOG(
        FilterTypeObject(
        //cleanLocator
        fun(fullName: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/following-sibling::*//*[@title='Clear']"
        },
        //valueLocator
        null,
        //clickLocator
        fun(fullName: String, value: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/following-sibling::*//input"
        })
    ),
    // Фильтр с множественным выбором содержащий в качестве значений тот или иной иерархический справочник КИАП
    HIERARCHICATALOG(
        FilterTypeObject(
        //cleanLocator
        fun(fullName: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/following-sibling::*//*[@title='Очистить']"
        },
        //valueLocator
        fun(fullName: String): String {
            return "//*[text()='$fullName']/ancestor::div[@role='presentation']/following-sibling::div[@role='presentation']//li[.//*[@name='checkboxNormal'] and not(.//*[contains(@name,'arrow')])]//*[text()]"
        },
        //clickLocator
        fun(fullName: String, value: String): String {
            return "//*[text()='$fullName']/ancestor::div[@role='presentation']/following-sibling::div[@role='presentation']//*[text()='$value']/ancestor::li//*[@name='checkboxNormal']"
        })
    ),
    // Фильтр реализованный радиокнопкой. Реализация не ограничена значениями "да" или "нет", но очистка ограничена установкой значения "Все"
    RADIOBUTTON(
        FilterTypeObject(
        //cleanLocator
        fun(fullName: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/ancestor::fieldset//*[text()='Все']/ancestor::label//input/.."
        },
        //valueLocator
        fun(fullName: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/ancestor::fieldset//label//*[text() and not (contains(text(),'Все'))]"
        },
        //clickLocator
        fun(fullName: String, value: String): String {
            return "html/body/div[@role='presentation']//*[text()='$fullName']/ancestor::fieldset//*[text()='$value']/ancestor::label//input/.."
        })
    ),
}
