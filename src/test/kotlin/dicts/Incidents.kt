package dicts

import Retry
import BaseTest
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.*
import org.junit.jupiter.api.Assertions
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

class Incidents :BaseTest() {


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
        createICToolCalltype("", waitTime)
        Thread.sleep(200)
        //язык обращения
        element(byXpath("//label[@id='language-label' and text()='Язык общения']/following-sibling::div/div[@id='language']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем язык
        element(byXpath("//ul[@aria-labelledby='language-label']/li[text()='$language1']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Номер телефона
        createICToolPhone("", waitTime)
        //ФИО
        createICToolFIO("$date AutoTestLastname inc", "INC 0010 Firstname", language1, waitTime)
        //адрес
        element(byXpath("//input[@id='callAddress']"))
            .click()
        element(byXpath("//input[@id='callAddress']"))
            .sendKeys("INC 0010 adr $dateTime")
        Thread.sleep(500)
        //заполняем дополнительную информацию
        createICToolsDopInfo("AutoTest INC 0010 inc $dateTime $language1", waitTime)
        //запоминаем источник обращения
        var callType = element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype']"))
            .ownText
        //регистрируем обращение
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        //выбираем тип происшествия
        inputRandomNew("incidentTypeId-textfield", false, waitTime)
        //Создаем карточку
        element(byXpath("//span[text()='Сохранить карточку']/.."))
            .click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card"))
            .should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus("В обработке", waitTime)
        //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest INC 0010 inc $dateTime $language1", waitTime)
        Assertions.assertTrue(
            elements(byXpath("//*[text()='1']/ancestor::div[@id='panel1a-header']//*[text()='$callType']"))
                .size
                == 1
        )
        element(byXpath("//main//header//*[text()='Обращения']//ancestor::button"))
            .scrollTo()
        element(byXpath("//main//header//*[text()='Обращения']//ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        checkbox("", true, waitTime)
        var languageColumn = numberOfColumn("Язык", waitTime)
        var fioColumn = numberOfColumn("ФИО", waitTime)
        var callTypeColumm = numberOfColumn("Тип источника", waitTime)
        element(byXpath("//table/tbody//*[text()='INC 0010 adr $dateTime']/ancestor::tr/td[$languageColumn][text()='$language1']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//table/tbody//*[text()='INC 0010 adr $dateTime']/ancestor::tr/td[$callTypeColumm][text()='$callType']"))
            .should(exist, ofSeconds(waitTime))
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        //заполняем карточку
        //Источник события - выбираем случайно
        createICToolCalltype("", waitTime)
        //язык обращения
        element(byXpath("//label[text()='Язык общения']/following-sibling::div/div[@id='language']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем язык
        element(byXpath("//ul[@aria-labelledby='language-label']/li[text()='$language2']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Номер телефона
        createICToolPhone("", waitTime)
        //ФИО
        createICToolFIO("$date AutoTestLastname call","INC 0010 Firstname", language2, waitTime)
        //адрес
//        addressInput("callAddress","INC 0010 adr $dateTime", waitTime)
        element(byXpath("//input[@id='callAddress']"))
            .click()
        element(byXpath("//input[@id='callAddress']"))
            .sendKeys("INC 0010 adr2 $dateTime")
        Thread.sleep(500)
        //заполняем дополнительную информацию
        createICToolsDopInfo("AutoTest call INC 0010 $dateTime $language2", waitTime)
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
        checkICToolIsStatus("В обработке", waitTime)
        //развернём второе обращение
        element(byXpath("//*[text()='2']/ancestor::div[@id='panel1a-header']//*[name()='svg']/ancestor::div[1]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest call INC 0010 $dateTime $language2", waitTime)
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
        checkbox("", true, waitTime)
        languageColumn = numberOfColumn("Язык", waitTime)
        fioColumn = numberOfColumn("ФИО", waitTime)
        callTypeColumm = numberOfColumn("Тип источника", waitTime)
        element(byXpath("//table/tbody//*[text()='INC 0010 adr2 $dateTime']/ancestor::tr/td[$languageColumn][text()='$language2']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//table/tbody//*[text()='INC 0010 adr2 $dateTime']/ancestor::tr/td[$callTypeColumm][text()='$callType']"))
            .should(exist, ofSeconds(waitTime))
        logoffTool()
    }

}