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
        logonTool()
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype' and text()='Телефон (ССОП)']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("#phone")).sendKeys(tel.toString())
        element(byXpath("//*[@name='noteError']/ancestor::div[@role='alert']//*[text()='Номер данного абонента был зафиксирован ранее как ложный']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
    }


    @Test (retryAnalyzer = Retry::class, groups = ["ALL"])
    fun `Event INC 5020 Nicholas`() {
        //Проверяем баг который нашел Коля, но который не удается воспроизвести. Т.о. положительный проход подтвердит невоспроизведение
        logonTool()
        open("https://test.kiap.local/audit/dicts?page=0&perPage=20&das=2021-12-31T21%3A00%3A00.000Z&dae=2022-04-01T20%3A59%3A59.999Z")
        element(byXpath("//table/tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Thread.sleep(5000)
        menuNavigation("Справочники", "Муниципальные образования", waitTime)
        element(byXpath("//table/tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val buttonsColumn = numberOfColumn(" ", waitTime)
        element(byXpath("//table/tbody/tr/td[$buttonsColumn]//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='presentation']//*[text()='Редактировать']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//main/div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//div[@role='textbox']/p[last()]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='textbox']/p[last()]"))
            .sendKeys(Keys.ENTER)
        element(byXpath("//div[@role='textbox']/p[last()]"))
            .sendKeys("Event INC 5020 Test")
        element(byXpath("//*[text()='Сохранить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//h2[text()='Муниципальные образования']/parent::div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[text()='Добавить новое']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
    }

    //160 строк надо выполнять дважды с маленькой разницей, поэтому вынесено в отдельный метод без абстракции
    fun `Set_incident_filters` (dateStart: String, dateEnd: String){

        val dateStartList = dateStart.split("-")
        val dateEndList = dateEnd.split("-")
        while (elements(byXpath("//form[@novalidate]//button//button")).size >0){
            val cleanFilterName = element(byXpath("(//form[@novalidate]//button//button)[1]/ancestor::button//*[text()]"))
                .ownText
            element(byXpath("(//form[@novalidate]//button//button)[1]"))
                .click()
            element(byXpath("//form[@novalidate]//*[text()='$cleanFilterName']/ancestor::button//button"))
                .shouldNot(exist, ofSeconds(waitTime))
            Thread.sleep(200)
        }
        if (elements(byXpath("//form[@novalidate]//*[contains(text(),'Еще фильтры')]/ancestor::button")).size == 1){
            element(byXpath("//form[@novalidate]//*[contains(text(),'Еще фильтры')]/ancestor::button"))
                .click()
            element(byXpath("//div[@role='presentation']//*[text()='Очистить все']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        } else if (elements(byXpath("//form[@novalidate]//*[contains(text(),'Все фильтры')]/ancestor::button")).size == 1){
            element(byXpath("//form[@novalidate]//*[contains(text(),'Все фильтры')]/ancestor::button"))
                .click()
            element(byXpath("//div[@role='presentation']//*[text()='Очистить все']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
        //устанавливаем фильры
        //если нет ни одной отдельной кнопки фильтра, то заполняем все фильтры в кнопке "Все фильтры"
        //если "все фильтры" нет, то заполняем имеющиеся кнопки и запоминаем что еще нам надо заполнить
        //если что-то не заполнили с отдельных кнопок, то идем в "Еще фильтры" и заполняем там то что не удалось заполнить отдельными кнопками
        if (elements(byXpath("//form[@novalidate]//*[contains(text(),'Все фильтры')]/ancestor::button")).size == 1) {
            element(byXpath("//form[@novalidate]//*[contains(text(),'Все фильтры')]/ancestor::button"))
                .click()
            element(byXpath("//div[@role='dialog']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//label[text()='Типы происшествий']/following-sibling::div"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']//*[contains(text(),'Ложные')]/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//label[text()='Источники событий']/following-sibling::div"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']//li[text()='Телефон (ССОП)']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            if (dateStart.isNotEmpty()) {
                element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='с']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .doubleClick()
                element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='с']"))
                    .sendKeys("${dateStartList[2]}.${dateStartList[1]}.${dateStartList[0]}0000")
            }
            element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='по']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .doubleClick()
            element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='по']"))
                .sendKeys("${dateEndList[2]}.${dateEndList[1]}.${dateEndList[0]}2359")
            element(byXpath("//div[@role='dialog']//*[text()='Применить']/ancestor::button"))
                .click()
        } else {
            var incidentTypeNeed = true
            var dataNeed = true
            var callTypeNeed = true
            if (elements(byXpath("//form[@novalidate]//*[text()='Типы происшествий']/ancestor::button")).size == 1) {
                element(byXpath("//form[@novalidate]//*[text()='Типы происшествий']/ancestor::button"))
                    .click()
                element(byXpath("//div[@role='presentation']//label[text()='Типы происшествий']/following-sibling::div"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']//*[contains(text(),'Ложные')]/ancestor::li"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']//*[text()='Применить']/ancestor::button"))
                    .click()
                incidentTypeNeed = false
            }
            if (elements(byXpath("//form[@novalidate]//*[text()='Источники']/ancestor::button")).size == 1) {
                element(byXpath("//form[@novalidate]//*[text()='Источники']/ancestor::button"))
                    .click()
                element(byXpath("//div[@role='presentation']//label[text()='Источники событий']/following-sibling::div"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                Thread.sleep(500)
                element(byXpath("//div[@role='presentation']//li[text()='Телефон (ССОП)']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']//*[text()='Применить']/ancestor::button"))
                    .click()
                callTypeNeed = false
            }
            if (elements(byXpath("//form[@novalidate]//*[text()='Регистрация']/ancestor::button")).size == 1) {
                element(byXpath("//form[@novalidate]//*[text()='Регистрация']/ancestor::button"))
                    .click()
//                element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='с']"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .doubleClick()
                if (dateStart.isNotEmpty()) {
                    element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='с']"))
                        .sendKeys("${dateStartList[2]}.${dateStartList[1]}.${dateStartList[0]}0000")
                }
//                element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='по']"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .doubleClick()
                element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='по']"))
                    .sendKeys("${dateEndList[2]}.${dateEndList[1]}.${dateEndList[0]}2359")
                element(byXpath("//div[@role='presentation']//*[text()='Применить']/ancestor::button"))
                    .click()
                dataNeed = false
            }
            if (incidentTypeNeed || dataNeed || callTypeNeed){
                element(byXpath("//form[@novalidate]//*[contains(text(),'Еще фильтры')]/ancestor::button"))
                    .click()
                element(byXpath("//div[@role='presentation']//div[@role='dialog']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                if (incidentTypeNeed) {
                    element(byXpath("//label[text()='Типы происшествий']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//div[@role='presentation']//*[contains(text(),'Ложные')]/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
                if (callTypeNeed){
                    element(byXpath("//label[text()='Источники событий']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//div[@role='presentation']//li[text()='Телефон (ССОП)']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
                if (dataNeed){
                    if (dateStart.isNotEmpty()) {
                        element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='с']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .doubleClick()
                        element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='с']"))
                            .sendKeys("${dateStartList[2]}.${dateStartList[1]}.${dateStartList[0]}0000")
                    }
                    element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='по']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .doubleClick()
                    element(byXpath("//label[text()='Дата регистрации']/ancestor::fieldset//input[@placeholder='по']"))
                        .sendKeys("${dateEndList[2]}.${dateEndList[1]}.${dateEndList[0]}2359")
                }
                element(byXpath("//div[@role='dialog']//*[text()='Применить']/ancestor::button"))
                    .click()
            }
        }

    }
    @Test (retryAnalyzer = Retry::class, groups = ["ALL"])
    fun `Event INC 5030 Предупреждение о ложном вызове актуально месяц`() {
        //сначала соберем номера за последний месяц
        date = LocalDate.now()
        dateTime = LocalDateTime.now()
//        val dateStartList = LocalDate.now().minusMonths(1).toString().split("-")
//        val dateEndList = LocalDate.now().toString().split("-")
        logonTool()
        menuNavigation("Происшествия","Список происшествий", waitTime)
        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Отчищаем фильтры
        Set_incident_filters(LocalDate.now().minusMonths(1).toString(), LocalDate.now().toString())
        //устанавливаем фильры
        //заполнили фильтры
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Thread.sleep(500)
        //открываем все КП, проходясь и по пагинации и складываем контактный номер в список, не занося в него дубликаты
        val falseСallsNumbersList = mutableListOf<String>()
        var anotherRound: Boolean
        do {
            Thread.sleep(500)
            for (i in 1..elements(byXpath("//table/tbody/tr")).size){
                element(byXpath("//table/tbody/tr[$i]"))
                    .click()
                element(byXpath("//strong[text()='Контактный номер:']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                if (!falseСallsNumbersList.contains(element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]"))
                        .ownText
                        .filter { it.isLetterOrDigit() })){
                    falseСallsNumbersList.add(element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]"))
                        .ownText
                        .filter { it.isLetterOrDigit() })
                }
                back()
                Thread.sleep(200)
            }
            if (element(byXpath("(//tfoot//nav//span[contains(@title,'На страницу номер')])[last()]/parent::div"))
                    .getAttribute("style")!!
                    .contains("color: black;")){
                anotherRound = true
                element(byXpath("//tfoot//nav//span[contains(@title,'На следующую страницу')]/p[text()='Вперед']"))
                    .click()
            } else {anotherRound = false}
        } while (anotherRound)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //меняем фильтры и заполняем список ложных, но более месяца, не более чем 10 записями
        Set_incident_filters("", LocalDate.now().minusMonths(1).minusDays(1).toString())
        Thread.sleep(1000)
        val moreMonthFalseСallsNumbersList = mutableListOf<String>()
        do {
            Thread.sleep(500)
            for (i in 1..elements(byXpath("//table/tbody/tr")).size){
                element(byXpath("//table/tbody/tr[$i]"))
                    .click()
                element(byXpath("//strong[text()='Контактный номер:']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))

                if (!moreMonthFalseСallsNumbersList.contains(element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]"))
                        .ownText
                        .filter { it.isLetterOrDigit() })){
                    moreMonthFalseСallsNumbersList.add(element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]"))
                        .ownText
                        .filter { it.isLetterOrDigit() })
                }
                back()
                Thread.sleep(200)
            }
            if ((element(byXpath("(//tfoot//nav//span[contains(@title,'На страницу номер')])[last()]/parent::div"))
                    .getAttribute("style")!!
                    .contains("color: black;"))
                &&(moreMonthFalseСallsNumbersList.size <= 10)){
                anotherRound = true
                element(byXpath("//tfoot//nav//span[contains(@title,'На следующую страницу')]/p[text()='Вперед']"))
                    .click()
            } else {anotherRound = false}
        } while (anotherRound)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //переходим в форму обращения, вставляем номер ложный моложе месяца, ждем банер,
        // очищаем вставляем ложный более месяца, банера быть не должно
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype' and text()='Телефон (ССОП)']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("#phone")).sendKeys(falseСallsNumbersList.random())
        element(byXpath("//*[@name='noteError']/ancestor::div[@role='alert']//*[text()='Номер данного абонента был зафиксирован ранее как ложный']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("#phone"))
            .click()
        element(byCssSelector("#phone")).sendKeys(Keys.END)
        repeat(element(byCssSelector("#phone")).getAttribute("value")!!.length) {
            element(byCssSelector("#phone")).sendKeys(Keys.BACK_SPACE)
        }
        element(byCssSelector("#phone")).sendKeys(moreMonthFalseСallsNumbersList.random())
        Thread.sleep(1000)
        element(byXpath("//*[@name='noteError']/ancestor::div[@role='alert']//*[text()='Номер данного абонента был зафиксирован ранее как ложный']"))
            .shouldNot(exist, ofSeconds(waitTime))
        logoffTool()
    }



}