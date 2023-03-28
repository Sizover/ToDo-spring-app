package test_library.menu


class MyMenu {
    // "Словарь" енумов хранящий множество подменю для каждого родительского раздела меню КИАП.
    // Реализованно именно в таком виде для удобства использовани в IDE
    enum class MainPage(override val menu: MenuEnum, override val subMenu: String, override val desc: String?) : SubmenuInterface {
        Home(MenuEnum.Главная, "Главная", null)
    }

    enum class Incidents(override val menu: MenuEnum, override val subMenu: String, override val desc: String?) : SubmenuInterface {
        CreateIncident(MenuEnum.Происшествия, "Создать карточку", null),
        IncidentsList(MenuEnum.Происшествия, "Список происшествий", null),
        IncidentsArchive(MenuEnum.Происшествия, "Архив происшествий", null)
    }

    enum class Map(override val menu: MenuEnum, override val subMenu: String, override val desc: String?) : SubmenuInterface {
        SubMenuMap(MenuEnum.Карта, "Карта", null)
    }

    enum class Reports(override val menu: MenuEnum, override val subMenu: String, override val desc: String?) : SubmenuInterface {
        IncidentsReport(MenuEnum.Отчеты, "По происшествиям", null),
        CallsReport(MenuEnum.Отчеты, "По обращениям", null),
        UsersReport(MenuEnum.Отчеты, "По сотрудникам", null),
        SummaryReport(MenuEnum.Отчеты, "По форме", null)

    }

    enum class Dictionaries(override val menu: MenuEnum, override val subMenu: String, override val desc: String?) : SubmenuInterface {
        CumulativePlans(MenuEnum.Справочники, "Алгоритмы реагирования", null),
        VideoCameras(MenuEnum.Справочники, "Видеокамеры", null),
        Sensors(MenuEnum.Справочники, "Датчики", null),
        Hotlines(MenuEnum.Справочники, "Дежурные службы", null),
        Positions(MenuEnum.Справочники, "Должности", null),
        Officials(MenuEnum.Справочники, "Должностные лица", null),
        Labels(MenuEnum.Справочники, "Метки", null),
        Municipalities(MenuEnum.Справочники, "Муниципальные образования", null),
        Companies(MenuEnum.Справочники, "Организации", null),
        HotlineAssets(MenuEnum.Справочники, "Силы и средства", null),
        IncidentTypes(MenuEnum.Справочники, "Типы происшествий", null)
    }
    enum class KB(override val menu: MenuEnum, override val subMenu: String, override val desc: String?) : SubmenuInterface {
        Explorer(MenuEnum.`База знаний`, "Проводник", null),
        Categories(MenuEnum.`База знаний`, "Разделы", null),
        Articles(MenuEnum.`База знаний`, "Статьи", null)
    }

    enum class System(override val menu: MenuEnum, override val subMenu: String, override val desc: String?) : SubmenuInterface {
        About(MenuEnum.Настройки, "О системе", null),
        Audit(MenuEnum.Настройки, "Аудит", null)
    }

}