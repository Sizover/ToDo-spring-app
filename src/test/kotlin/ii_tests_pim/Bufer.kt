package ii_tests_pim

//import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.*
//import com.codeborne.selenide.Selenide.$
//import com.codeborne.selenide.Selenide.elements
import Tools
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.`$$`
import com.codeborne.selenide.Selenide.`$x`
import java.lang.String.*
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.Selenide.open
import java.time.Duration.ofSeconds
import java.time.LocalDateTime
import org.openqa.selenium.interactions.Locatable
import org.openqa.selenium.SearchContext

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
        tools.menuNavigation("Происшествия","Список происшествий",waitTime)
        //кликаем по "создать обращение"
        for (i in 1..20) {
            element(byXpath("//span[text()='Создать обращение']/.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//form"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byCssSelector("input[id='fio.lastname']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byCssSelector("input[id='fio.lastname']")).sendKeys("asd")
            element(byCssSelector("input[id='fio.firstname']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byCssSelector("input[id='fio.firstname']")).sendKeys("asd")
            element(byCssSelector("input[id='phone']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byCssSelector("input[id='phone']")).sendKeys("asd")
            element(byXpath("//*[text()='Консультация']/parent::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
    }
    @org.testng.annotations.Test
    fun `N 02501`(){

        tools.logonTool()
        tools.menuNavigation("Происшествия","Список происшествий",waitTime)
        elements(byXpath("//table/tbody/tr")).shouldHave(
            CollectionCondition.sizeGreaterThanOrEqual(1))
        //открываем фильтр "Типы происшествий"
        element(byXpath("//*[text()='Типы происшествий']/ancestor::button")).click()
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()

        Thread.sleep(5000)


    }
    @org.testng.annotations.Test
    fun `N 02502`(){
        tools.logonTool()
        tools.menuNavigation("Происшествия", "Создать карточку", waitTime)
        Thread.sleep(10000)
//        tools.logonTool()
//        tools.menuNavigation("Происшествия","Создать карточку",waitTime)
//        element(byXpath("//*[text()='Создать карточку']/ancestor::button"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        element(byXpath("//input[@name='incidentTypeId-textfield']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        Thread.sleep(500)
//        element(byXpath("//div[@role='presentation']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//        val test = elements(byXpath("//div[@role='presentation']"))
//        test.forEach {
//            println(it)
//        }
////        println(elements(byXpath("//body//div[role='presentation']//li[1]//*")) )
//
////        Thread.sleep(20000)

    }
}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//https://crossbrowsertesting.github.io/drag-and-drop.html