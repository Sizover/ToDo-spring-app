package test_library.menu

import BaseTest


class MyMenu: BaseTest() {
    // "Словарь" енумов хранящий множество подменю для каждого родительского раздела меню КИАП.
    // Реализованно именно в таком виде для удобства использовани в IDE
    enum class MainPage(override val menu: MenuEnum, override val subMenu: String, override val table: Boolean, override val desc: String?) : SubmenuInterface {
        Home(MenuEnum.Главная, "Главная", false, null),
    }

    enum class Incidents(override val menu: MenuEnum, override val subMenu: String, override val table: Boolean, override val desc: String?) : SubmenuInterface {
        CreateIncident(MenuEnum.Происшествия, "Создать карточку", false, null),
        IncidentsList(MenuEnum.Происшествия, "Список происшествий", true, null),
        IncidentsArchive(MenuEnum.Происшествия, "Архив происшествий", true, null)
    }

    enum class Map(override val menu: MenuEnum, override val subMenu: String, override val table: Boolean, override val desc: String?) : SubmenuInterface {
        SubMenuMap(MenuEnum.Карта, "Карта", false, null)
    }

    enum class Reports(override val menu: MenuEnum, override val subMenu: String, override val table: Boolean, override val desc: String?) : SubmenuInterface {
        IncidentsReport(MenuEnum.Отчеты, "По происшествиям", true, null),
        CallsReport(MenuEnum.Отчеты, "По обращениям", true, null),
        UsersReport(MenuEnum.Отчеты, "По сотрудникам", true, null),
        SummaryReport(MenuEnum.Отчеты, "По форме", true, null)

    }

    enum class Dictionaries(override val menu: MenuEnum, override val subMenu: String, override val table: Boolean, override val desc: String?) : SubmenuInterface {
        CumulativePlans(MenuEnum.Справочники, "Алгоритмы реагирования", true, null),
        VideoCameras(MenuEnum.Справочники, "Видеокамеры", true, null),
        Sensors(MenuEnum.Справочники, "Датчики", true, null),
        Hotlines(MenuEnum.Справочники, "Дежурные службы", true, null),
        Positions(MenuEnum.Справочники, "Должности", true, null),
        Officials(MenuEnum.Справочники, "Должностные лица", true, null),
        Labels(MenuEnum.Справочники, "Метки", true, null),
        Municipalities(MenuEnum.Справочники, "Муниципальные образования", true, null),
        Companies(MenuEnum.Справочники, "Организации", true, null),
        HotlineAssets(MenuEnum.Справочники, "Силы и средства", true, null),
        IncidentTypes(MenuEnum.Справочники, "Типы происшествий", true, null)
    }
    enum class KB (override val menu: MenuEnum, override val subMenu: String, override val table: Boolean, override val desc: String?) : SubmenuInterface {
        Explorer(MenuEnum.`База знаний`, "Проводник", false, null),
        Categories(MenuEnum.`База знаний`, "Разделы", true, null),
        Articles(MenuEnum.`База знаний`, "Статьи", true, null)
    }

    enum class System(override val menu: MenuEnum, override val subMenu: String, override val table: Boolean, override val desc: String?) : SubmenuInterface {
        About(MenuEnum.Настройки, "О системе", false, null),
        Audit(MenuEnum.Настройки, "Аудит", false, null)
    }

    fun returnAllClasses(): Array<Array<SubmenuInterface>> =
        arrayOf<SubmenuInterface>(*MyMenu.Incidents.values(), *MyMenu.Reports.values(), *MyMenu.Dictionaries.values(), *MyMenu.KB.values()).filter { it.table }.map { arrayOf ( it) }
            .toTypedArray()



}