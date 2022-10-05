package dicts

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

    @DataProvider(name = "Языки")
    open fun Statuses(): Any {
        return arrayOf<Array<Any>>(
            arrayOf("Русский", "Английский"),
            arrayOf("Английский", "Русский")
        )
    }

    @Test (retryAnalyzer = Retry::class, dataProvider = "Языки", groups = ["ALL"])
    fun `Dicts INC 0010 Проверка наличия и работоспособности языка обращения в карточке обращения`(language1 : String, language2 : String) {
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        //кликаем по "создать обращение"
        element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
        //заполняем карточку
        //Источник события - выбираем случайно
        element(byCssSelector("div#calltype"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        var iI = (1..10).random()
        element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($iI)")).click()
        Thread.sleep(200)
        //язык обращения
        element(byXpath("//label[@id='language-label' and text()='Язык']/following-sibling::div/div[@id='language']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем язык
        element(byXpath("//ul[@aria-labelledby='language-label']/li[text()='$language1']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Номер телефона
        if (elements(byCssSelector("#phone")).size > 0){
            val tel = (9000000000..9999999999).random()
            element(byCssSelector("#phone")).sendKeys("+7$tel")
        }
        //ФИО
        if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
            && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
            element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname inc")
            element(byCssSelector("input[id='fio.firstname']")).sendKeys("INC 0010 Firstname")
        }
        //адрес
        element(byXpath("//input[@id='callAddress']"))
            .click()
        element(byXpath("//input[@id='callAddress']"))
            .sendKeys("INC 0010 adr $dateTime")
        Thread.sleep(500)
        //заполняем дополнительную информацию
        element(byCssSelector("div[role='textbox']>p")).click()
        element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest INC 0010 inc $dateTime")
        //запоминаем источник обращения
        var callType = element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype']"))
            .ownText
        //регистрируем обращение
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        //выбираем тип происшествия
        inputRandomNew("incidentTypeId-textfield", false, waitTime)
        //Создаем карточку
        element(byXpath("//span[text()='Сохранить карточку']/..")).click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        element(byCssSelector("button[style='min-width: 140px; white-space: nowrap; border-radius: 20px;']"))
            .shouldHave(text("В обработке"), ofSeconds(waitTime))
        //и что это именно так карточка которую мы только что создали
        element(byXpath("//div[text()='AutoTest INC 0010 inc $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Assertions.assertTrue(
            elements(byXpath("//*[text()='1']/ancestor::div[@id='panel1a-header']//*[text()='$callType']"))
                .size
                == 1
        )
//        element(byCssSelector("button[style='min-width: 140px; white-space: nowrap; border-radius: 20px;']"))
//            .scrollIntoView(true)
        element(byXpath("//main//header//*[text()='Обращения']//ancestor::button"))
            .scrollTo()
        element(byXpath("//main//header//*[text()='Обращения']//ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        var languageColumn = numberOfColumn("Язык", waitTime)
        var fioColumn = numberOfColumn("ФИО", waitTime)
        var callTypeColumm = numberOfColumn("Тип источника", waitTime)
        for (str in 1..elements(byXpath("//table/tbody/tr")).size){
            if (elements(byXpath("//table/tbody/tr[$str]/td[$fioColumn][contains(text(),'2022-09-08 AutoTestLastname inc')]")).size == 1){
                Assertions.assertTrue(elements(byXpath("//table/tbody/tr[$str]/td[$languageColumn][text()='$language1']")).size == 1)
                Assertions.assertTrue(elements(byXpath("//table/tbody/tr[$str]/td[$callTypeColumm][text()='$callType']")).size == 1)
                break
            }
        }
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        //заполняем карточку
        //Источник события - выбираем случайно
        element(byCssSelector("div#calltype"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        iI = (1..10).random()
        element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($iI)")).click()
        //язык обращения
        element(byXpath("//label[text()='Язык']/following-sibling::div/div[@id='language']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем язык
        element(byXpath("//ul[@aria-labelledby='language-label']/li[text()='$language2']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Номер телефона
        if (elements(byCssSelector("#phone")).size > 0){
            val tel = (9180000000..9189999999).random()
            element(byCssSelector("#phone")).sendKeys("+7$tel")
        }
        //ФИО
        if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
            && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
            element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname call")
            element(byCssSelector("input[id='fio.firstname']")).sendKeys("INC 0010 Firstname")
        }
        //адрес
//        addressInput("callAddress","INC 0010 adr $dateTime", waitTime)
        element(byXpath("//input[@id='callAddress']"))
            .click()
        element(byXpath("//input[@id='callAddress']"))
            .sendKeys("INC 0010 adr2 $dateTime")
        Thread.sleep(500)
        //заполняем дополнительную информацию
        element(byCssSelector("div[role='textbox']>p")).click()
        element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest call INC 0010 $dateTime")
        //запоминаем источник обращения
        callType = element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype']"))
            .ownText
        //делаем привязку к первой КП
        element(byXpath("//*[text()='INC 0010 adr $dateTime']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Привязать к происшествию']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        element(byCssSelector("button[style='min-width: 140px; white-space: nowrap; border-radius: 20px;']"))
            .shouldHave(text("В обработке"), ofSeconds(waitTime))
        //и что это именно так карточка которую мы только что создали
        element(byXpath("//div[text()='AutoTest INC 0010 inc $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Assertions.assertTrue(
            elements(byXpath("//*[text()='2']/ancestor::div[@id='panel1a-header']//*[text()='$callType']"))
                .size
                == 1
        )
        element(byXpath("//main//header//*[text()='Обращения']//ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        languageColumn = numberOfColumn("Язык", waitTime)
        fioColumn = numberOfColumn("ФИО", waitTime)
        callTypeColumm = numberOfColumn("Тип источника", waitTime)
        for (str in 1..elements(byXpath("//table/tbody/tr")).size){
            if (elements(byXpath("//table/tbody/tr[$str]/td[$fioColumn][contains(text(),'2022-09-08 AutoTestLastname call')]")).size == 1){
                Assertions.assertTrue(elements(byXpath("//table/tbody/tr[$str]/td[$languageColumn][text()='$language2']")).size == 1)
                Assertions.assertTrue(elements(byXpath("//table/tbody/tr[$str]/td[$callTypeColumm][text()='$callType']")).size == 1)
                break
            }
        }
        logoffTool()
    }

}