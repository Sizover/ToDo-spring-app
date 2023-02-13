package test_library.filters


enum class FilterTypeEnum(val filterType: FilterTypeObject) {
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
