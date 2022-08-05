package ii_tests_pim

import Tools
import java.time.LocalDateTime

//import java.time.ZonedDateTime
//import java.io.File
//import java.time.Duration
//import org.junit.jupiter.api.Order
//import com.codeborne.selenide.Configuration
//import com.codeborne.selenide.Browsers.*
//import com.codeborne.selenide.CollectionCondition

class Bufer {
    val tools = Tools()
    val waitTime: Long = 5

    var date = ""
    var dateTime = LocalDateTime.now().toString()

//        println(date)
//        var date2 = date.split("-")
//        println("Год ${date2[0]}")
//        println(date2[1])
//        println(date2[2])
//        Thread.sleep(50000)
//
//
//
//    //Время ожидания элементов при выполнении теста
//    var waitTime: Long = 5
//    val checkboxTrue = "M19 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2V5c0-1.1-.89-2-2-2zm-9 14l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"
//    val checkboxFalse = "M19 5v14H5V5h14m0-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2z"

//    @org.testng.annotations.Test
    fun `N 0250`(){
////        val actions = Action()
//        open("https://crossbrowsertesting.github.io/drag-and-drop.html")
//        element(byXpath("//body/h1"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//        Thread.sleep(2000)
//        val element1 = element(byCssSelector("div#draggable"))
//        val element2 = element(byCssSelector("div#droppable"))
//        println("start")
////        actions().dragAndDrop(element(byCssSelector("div#draggable")),element(byCssSelector("div#droppable")))
//        element1.dragAndDropTo(element2)
//        element(byCssSelector("#draggable")).dragAndDropTo("#droppable")
//        println("stop")
//        Thread.sleep(10000)

    }
    @org.testng.annotations.Test
    fun `N 02500`(){
        tools.logonTool()
        tools.menuNavigation("Происшествия","Список происшествий", waitTime)
        tools.stringsOnPage(100, waitTime)
        Thread.sleep(10000)
    }
}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//https://crossbrowsertesting.github.io/drag-and-drop.html