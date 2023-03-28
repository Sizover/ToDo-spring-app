package dicts

import BaseTest
import Retry
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.openqa.selenium.Keys
import org.testng.annotations.Test
import test_library.menu.MyMenu.Dictionaries
import test_library.menu.MyMenu.Incidents
import test_library.statuses.StatusEnum.`В обработке`
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

class Labels : BaseTest(){



    @Test (retryAnalyzer = Retry::class, groups = ["ALL"])
    fun `Labels 0010 Проверка создания, прикрепления к КП и удаления метки`() {
        //проверим создание метки и прикрепление метки к происшествию, возможно с удалением метки из КИАП
        date = LocalDate.now()
        dateTime = LocalDateTime.now()
        logonTool()
        menuNavigation(Dictionaries.Labels, waitTime)
        tableColumnCheckbox("Метка;Описание", true, waitTime)
        //воспользуемся поиском, что бы найти созданную метку не удаленную в упавший проход
        tableSearch("АвтоТест", waitTime)
        Thread.sleep(500)
//        if (elements(byXpath("//tbody/tr//*[text()='Нет данных']")).size == 0){
            while (!element(byXpath("//tbody/tr//text()/parent::*[text()='Нет данных']")).exists()) {
//                val removedLadel = element(byXpath("(//tbody/tr/td[1]//text()/..)[last()]")).ownText.toString()
                val removedLadel = element(byXpath("(//tbody/tr/td[1])[last()]")).innerText()
                //открываем трехточечное меню
                element(byXpath("(//tbody/tr/td[last()]//button)[last()]")).click()
                //удаляем метку
                element(byXpath("//*[text()='Удалить']/text()/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //подтверждаем удаление
                element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//tbody/tr/td//*[text()='$removedLadel']"))
                    .shouldNot(exist, ofSeconds(longWait))
            }
            //убеждаемся что удалили
            element(byXpath("//tbody/tr//text()/parent::*[text()='Нет данных']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
//        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val labelList = listOf<String>("АвтоТест", "Дочерняя метка")
        val labelListName = mutableListOf<String>()
        labelList.forEach {
            //жмем добавить метку
            element(byXpath("//*[text()='Добавить метку']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//label[text()='Родительский тип']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            if (it != labelList[0]) {
                element(byCssSelector("input#parent"))
                    .click()
                element(byXpath("//body/div[@role='presentation']//*[text()='${labelList[0]}']/ancestor::li"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            }
            //вводим имя метки
            element(byCssSelector("input#title"))
                .should(exist, ofSeconds(longWait))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byCssSelector("input#title"))
                .should(exist, ofSeconds(longWait))
                .shouldBe(visible, ofSeconds(waitTime))
                .sendKeys(it)
            //запоминаем образец метки, что бы убеждаться в её создании
            val labelSample = element(byXpath("//label[text()='Предварительный просмотр']/..//*[@type='form']//*[text()]")).ownText
            labelListName.add(labelSample)
            //добавляем описание
            element(byCssSelector("textarea[name='description']")).click()
            element(byCssSelector("textarea[name='description']")).sendKeys("Метка \"$labelSample\" создана автотестом и должна быть удалена им же")
            //выбираем случайный цвет
            elements(byXpath("//form[@novalidate]//*[@aria-labelledby]//input"))
                .random()
                .click()
            //выбираем случайный оттенок
            elements(byXpath("//form[@novalidate]//*[@aria-labelledby]/ancestor::div[1]/following-sibling::div[1]//input"))
                .random()
                .click()
            //создаем
            element(byXpath("//*[text()='Добавить']/text()/ancestor::button"))
                .click()
            //проверяем наличие созданной метки
            element(byXpath("//table/thead/tr/th//*[contains(@name,'arrow')]/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
            while (element(byXpath("//table/tbody/tr/td//*[@name='arrowRight']/ancestor::button")).exists()){
                element(byXpath("//table/thead/tr/th//*[contains(@name,'arrow')]/ancestor::button"))
                    .click()
                Thread.sleep(100)
            }
            element(byXpath("//table/thead/tr/th//*[@name='arrowDown']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
            element(byXpath("//tr/td//*[contains(text(),'$labelSample')]"))
                .should(exist, ofSeconds(longWait))
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
        menuNavigation(Incidents.CreateIncident, waitTime)
        createICToolCalltype("", waitTime)
        createICToolPhone("", waitTime)
        createICToolFIO("AutoTest", "Labels 0010", "", waitTime)
        createICToolRandomCoordinates("", waitTime)
        createICToolsDopInfo("AutoTest Labels 0010 $dateTime", waitTime)
        element(byXpath("//*[text()='Создать карточку']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
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
            elements(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()]"))
                .forEach {
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
        element(byXpath("//*[text()='Сохранить карточку']/text()/ancestor::button")).click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card"))
            .should(exist, ofSeconds(waitTime))
        //что она в нужном статусе
        checkICToolIsStatus(`В обработке`, waitTime)
        //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest Labels 0010 $dateTime", waitTime)
        createdLabel.forEach {
            element(byXpath("//div[@id='labels']//*[contains(text(),'$it')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        //добавляем метку
        element(byXpath("//h3[text()='Метки']/following-sibling::*//*[text()='Изменить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[name='labelsId-textfield']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        with("//body/div[@role='presentation']//button/ancestor::li//*[@name='%s']"){
            while (element(byXpath(this.format("arrowRight"))).exists()){
                element(byXpath(this.format("arrowRight")))
                    .click()
                Thread.sleep(100)
            }
            element(byXpath(this.format("arrowDown")))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        labelListName.forEach {
            with("//body/div[@role='presentation']//*[text()='$it']/ancestor::li//*[@name='%s']"){
                while (element(byXpath(this.format("checkboxNormal"))).exists()){
                    element(byXpath(this.format("checkboxNormal")))
                        .click()
                    Thread.sleep(100)
                }
                element(byXpath(this.format("checkboxNormal")))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath(this.format("checkboxFocus")))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
        }
        element(byCssSelector("input#labelsId-autocomplete"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .shouldNot(exist, ofSeconds(waitTime))
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
        menuNavigation(Dictionaries.Labels, waitTime)
        //воспользуемся поиском, что бы найти созданную метку
        tableSearch("АвтоТест", waitTime)
        Thread.sleep(500)
        while (!element(byXpath("//tbody/tr//text()/parent::*[text()='Нет данных']")).exists()) {
            val removedLadel = element(byXpath("(//tbody/tr/td[1])[last()]")).innerText()
            //открываем трехточечное меню
            element(byXpath("(//tbody/tr/td[last()]//button)[last()]")).click()
            //удаляем метку
            element(byXpath("//*[text()='Удалить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //подтверждаем удаление
            element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//tbody/tr/td//*[text()='$removedLadel']"))
                .shouldNot(exist, ofSeconds(longWait))
        }
        //убеждаемся что удалили
        element(byXpath("//tbody/tr//text()/parent::*[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
    }
}