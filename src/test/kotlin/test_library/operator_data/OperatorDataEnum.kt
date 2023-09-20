package test_library.operator_data

enum class OperatorDataEnum(val locator: String) {
    `Аккаунт пользователя`("//p[text()='Аккаунт пользователя']/following-sibling::p//text()/.."),
    `Должностное лицо`("//*[@name='userProfile']/ancestor::div[2]//h2"),
    `Муниципальное образование`("//p[text()='Муниципальное образование']/following-sibling::p//text()/.."),
    `Служба по умолчанию`("//p[text()='Служба по умолчанию']/following-sibling::p//text()/.."),
    //`Службы`
    //`Внутренний телефон`
}