package test_library.menu

interface SubmenuInterface {
    // Интерфейс связи разных классов в общем вызове class MyMenu
    val menu: MenuEnum
    val subMenu: String
    val table: Boolean
    val desc: String?


    fun listOf() {
        TODO()
    }
}