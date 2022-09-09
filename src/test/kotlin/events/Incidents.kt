package events

import Retry
import BaseTest
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byName
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.*
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.io.File
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

class Incidents :BaseTest() {

    var date = LocalDate.now()
    var dateTime = LocalDateTime.now()
    //Время ожидания элементов при выполнении теста
    val waitTime: Long = 5
    val longWait: Long = 10


    @Test (retryAnalyzer = Retry::class, groups = ["ALL"])
    fun `Event INC 5010 Проверка наличия информирования о ложном вызове`() {
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool()
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        //ждем
        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //если вдруг источник обращения не телефон - исправляем это
        //не будем исправлять источник, т.к. это заодно проверит дефолтный источник карточки
//        if (element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype' and text()]")).ownText != "Телефон (ССОП)"){
//            element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype' and text()]"))
//                .click()
//            element(byXpath("//div[@role='presentation']//ul/li[text()='Телефон (ССОП)']"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
//        }
        element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype' and text()='Телефон (ССОП)']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //телефон
        val tel = (89000000000..89999999999).random()
        element(byCssSelector("#phone")).sendKeys(tel.toString())
        //ФИО
        element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname inc")
        element(byCssSelector("input[id='fio.firstname']")).sendKeys("INC 5010 Firstname")
        //адрес
        element(byXpath("//input[@id='callAddress']"))
            .click()
        element(byXpath("//input[@id='callAddress']"))
            .sendKeys("INC 5010 adr $dateTime")
        Thread.sleep(500)
        //заполняем дополнительную информацию
        element(byCssSelector("div[role='textbox']>p")).click()
        element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest INC 5010 inc $dateTime")
        //Создаем ложное обращение
        element(byXpath("//*[text()='Ложное обращение']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        logoffTool()
//        Thread.sleep(60000)
//        element(byXpath("//*[text()='Создать обращение']/ancestor::button"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
        logonTool()
        menuNavigation("Происшествия", "Создать карточку", waitTime)
//        element(byXpath("//form[@novalidate]"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype' and text()='Телефон (ССОП)']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("#phone")).sendKeys(tel.toString())
        element(byXpath("//*[@name='noteError']/ancestor::div[@role='alert']//*[text()='Номер данного абонента был зафиксирован ранее как ложный']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))









        logoffTool()
    }


}