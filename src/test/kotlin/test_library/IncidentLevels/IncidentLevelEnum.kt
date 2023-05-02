package test_library.IncidentLevels

enum class IncidentLevelEnum (val Level: IncidentLevelObject) {
    `Консультация`(IncidentLevelObject("Консультации", "К", true)),
    `Ложное`(IncidentLevelObject("Ложные", "Л", true)),
    `Происшествие`(IncidentLevelObject("Повседневные", "П", true)),
    `Угроза ЧС`(IncidentLevelObject("Чрезвычайные ситуации", "ЧС", false)),
    `Черновик`(IncidentLevelObject("Черновик", "Ч", true)),
    `ЧС`(IncidentLevelObject("Чрезвычайные ситуации", "ЧС", true)),
}