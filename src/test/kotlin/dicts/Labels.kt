package dicts

import Retry
import BaseTest
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.*
import org.openqa.selenium.Keys
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import org.testng.annotations.Test

class Labels : BaseTest(){

    var date = ""
    var dateTime = ""
    //Время ожидания элементов при выполнении теста
    val waitTime: Long = 5
    val longWait: Long = 10

    @Test (retryAnalyzer = Retry::class, groups = ["ALL"])
    fun `Labels 0010 Проверка создания, прикрепления к КП и удаления метки`() {
        //проверим создание метки и прикрепление метки к происшествию, возможно с удалением метки из КИАП
        date = LocalDate.now().toString()
        dateTime = LocalDateTime.now().toString()
        logonTool()
        menuNavigation("Справочники", "Метки", waitTime)
        //воспользуемся поиском, что бы найти созданную метку не удаленную в упавший проход
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("input[placeholder]")).sendKeys("AutoTest", Keys.ENTER)
        Thread.sleep(500)
        val menuColumn = elements(byXpath("//thead/tr/th")).size
//        val nameColumn = tools.numberOfColumn("Метка", waitTime)
        if (elements(byXpath("//tbody/tr//*[text()='Нет данных']")).size == 0){
            while (elements(byXpath("//tbody/tr//*[text()='Нет данных']")).size == 0) {
                val removedLadel = element(byXpath("(//tbody/tr/td[1]//*[text()])[last()]")).ownText.toString()
                //открываем трехточечное меню
                element(byXpath("(//tbody/tr/td[$menuColumn]//button)[last()]")).click()
                //удаляем метку
                element(byXpath("//span[text()='Удалить']/parent::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //подтверждаем удаление
                element(byXpath("//span[@title='Удалить']/button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//tbody/tr/td//*[text()='$removedLadel']"))
                    .shouldNot(exist, ofSeconds(waitTime))
            }
            //убеждаемся что удалили
            element(byXpath("//tbody/tr//*[text()='Нет данных']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val labelList = listOf<String>("AutoTest", "Child label")
        val labelListName = mutableListOf<String>()
        labelList.forEach {
            //жмем добавить метку
            element(byXpath("//span[text()='Добавить метку']/parent::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//label[text()='Родительский тип']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            if (it == "Child label") {
                element(byCssSelector("input#parent")).click()
                element(byXpath("//body/div[@role='presentation']//*[contains(text(),'AutoTest')]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            }
            //вводим имя метки
            element(byCssSelector("input#title")).click()
            element(byCssSelector("input#title")).sendKeys(it)
            //запоминаем образец метки, что бы убеждаться в её создании
            val labelSample = element(byXpath("//label[text()='Предварительный просмотр']/following-sibling::div//*[text()]")).ownText
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            labelListName.add(labelSample.replace(" ", "_"))
            labelListName.add(labelSample)
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //добавляем описание
            element(byCssSelector("textarea[name='description']")).click()
            element(byCssSelector("textarea[name='description']")).sendKeys("Метка \"$labelSample\" создана автотестом и должна быть удалена им же")
            //выбираем цвет индиго (случайно?)
            val elementsCollection = elements(byCssSelector("input[value][type='radio']"))
            val colorList = mutableListOf<String>()
            elementsCollection.forEach { colorList.add(it.getAttribute("value").toString()) }
            element(byCssSelector("input[value='${colorList.random()}']")).click()
//            element(byCssSelector("input[value='indigo']")).click()
            //создаем
            element(byXpath("//span[text()='Добавить']/parent::button")).click()
            //проверяем наличие созданной метки
            element(byXpath("//tr/td//*[contains(text(),'$labelSample')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//tr/td[text()='Метка \"$labelSample\" создана автотестом и должна быть удалена им же']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        logoffTool()
        Thread.sleep(500)
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Регистрируем КП
        logonTool()
        menuNavigation("Происшествия","Создать карточку", waitTime)
        firstHalfIC("T 0010", date, dateTime, waitTime)
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        inputRandomNew("incidentTypeId-textfield", false, waitTime)
        //добавляем метку при создании КП
        //т.к. меток может добавится много, нам придется проверить все на совпадение с создаваемыми

        val createdLabel = mutableListOf<String>()
        var again: Boolean
        do {
            again = false
            inputRandomNew("labelsId-textfield", true, waitTime)
            element(byXpath("//input[@name='labelsId-textfield']")).sendKeys(Keys.END)
            repeat(element(byXpath("//input[@name='labelsId-textfield']")).getAttribute("value")!!.length){
                element(byXpath("//input[@name='labelsId-textfield']")).sendKeys(Keys.BACK_SPACE)
            }
            val labelElementsCollection = elements(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()]"))
            labelElementsCollection.forEach {
                createdLabel.add(it.ownText)
            }
            labelListName.forEach{
                if (createdLabel.contains(it)){
                    again = true
                }
            }
            if (again){
                createdLabel.forEach {
                    element(byXpath("//label[text()='Метки']/..//span[text()='$it']/../*[name()='svg']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Метки']/..//span[text()='$it']/../*[name()='svg']"))
                        .shouldNot(exist, ofSeconds(waitTime))
                }
            }
        } while (again)
        element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card"))
            .should(exist, ofSeconds(waitTime))
        //что она в нужном статусе
        element(byCssSelector("button[style='min-width: 140px; white-space: nowrap; border-radius: 20px;']"))
            .shouldHave(text("В обработке"), ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //и что это именно так карточка которую мы только что создали
        element(byCssSelector("div#panel1a-content div[style='gap: 16px 0px;']>div:nth-child(5)"))
            .shouldHave(text("AutoTest T 0010 $dateTime"), ofSeconds(waitTime))
        createdLabel.forEach {
            element(byXpath(" //span[contains(text(),'$it')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        //добавляем метку
        element(byXpath("//h3[text()='Метки']/following-sibling::span/button")).click()
        element(byCssSelector("input[name='labelsId-textfield']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//body/div[@role='presentation']//ul/li[1]/div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        labelListName.forEach {
            element(byXpath("//body/div[@role='presentation']//*[text()='$it']")).click()
            element(byXpath("//div[@role='combobox']//*[text()='$it']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        element(byXpath("//span[text()='Сохранить']/parent::button")).click()
        //проверяем что все метки на месте
        labelListName.forEach {
            element(byXpath(" //span[contains(text(),'$it')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        createdLabel.forEach {
            element(byXpath(" //span[contains(text(),'$it')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        logoffTool()
        //перезалогиниваемся, что бы исключить кеширование и расширить тест
        Thread.sleep(500)
        logonTool()
        menuNavigation("Справочники", "Метки", waitTime)
        //воспользуемся поиском, что бы найти созданную метку
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("input[placeholder]")).sendKeys("AutoTest", Keys.ENTER)
//        element(byXpath("//*[contains(text(),'1 строка')]"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        Thread.sleep(500)
        if (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0){
            while (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0) {
                val removedLabel = element(byXpath("(//tbody/tr/td[1]//*[text()])[last()]")).ownText.toString()
                //открываем трехточечное меню
                element(byXpath("(//tbody/tr//button)[last()]")).click()
                //удаляем метку
                element(byXpath("//span[text()='Удалить']/parent::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //подтверждаем удаление
                element(byXpath("//span[@title='Удалить']/button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//tbody/tr/td[1]//*[text()='$removedLabel']"))
                    .shouldNot(exist, ofSeconds(waitTime))
            }
        }
        //убеждаемся что удалили
        element(byXpath("//table/tbody/tr//*[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
    }
}