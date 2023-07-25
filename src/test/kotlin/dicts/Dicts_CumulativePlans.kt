package dicts

import BaseTest
import Retry
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.attribute
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.Test
import test_library.alerts.AlertsEnum
import test_library.filters.FilterEnum
import test_library.menu.MyMenu.Dictionaries
import test_library.menu.MyMenu.Incidents
import test_library.statuses.StatusEnum.`В обработке`
import java.time.Duration.ofSeconds
import java.time.LocalDateTime

class Dicts_CumulativePlans:BaseTest() {



    @Test (retryAnalyzer = Retry::class, groups = ["ALL"])
    fun `Dicts CP 0010 Создание, изменение, перемещение и удаление пунктов реагирования`() {
        val dateTime = LocalDateTime.now()
        //хардкодный счетчик родительских пунктов, просто на всякий
        val parentCount = 1
        //Стартовый номер нового пункта, достается из таблицы, на 1 больше максимального
        val startNumber: Int
        //список существующих пунктов созданных тестом, но не удаленных после теста.
        //Проверяется и наполняется в начале теста
        val listOfRemoved = mutableListOf<String>()
        //Список созданных дочерних пунктов
        val listOfChildPC = mutableListOf<String>()
        logonTool(false)
        //Переходим в справочник
        menuNavigation(Dictionaries.CumulativePlans, waitTime)
        tableColumnCheckbox("Наименование мероприятий/блока", true, waitTime)
        var nameCPColumn = tableNumberOfColumn("Наименование мероприятий/блока", waitTime)
        //очищаем фильтр
        cleanFilterByEnum(listOf(), waitTime)
        //выводим столбец с нумерацией пунктов
        tableColumnCheckbox("№", true, waitTime)
        //запоминаем столбец с нумерацией пунктов
        var columnOfNumber = tableNumberOfColumn("№", waitTime)
        //удалим результаты предыдущих провальных тестов
        tableSearch("AutoTest Dicts CP 0010", waitTime)
        element(byXpath("//table/tbody/tr//*[contains(text(),'AutoTest Dicts CP 0010') or contains(text(),'Нет данных')]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //сначала добавим в список все дочерние пункты по принципу наличия точки в номере
        elements(byXpath("//table/tbody/tr[td[$nameCPColumn]//text()/parent::*[contains(text(),'AutoTest Dicts CP 0010')] and td[$columnOfNumber]//text()/parent::*[contains(text(),'.')]]/td[$nameCPColumn]/text()/..")).forEach {
            listOfRemoved.add(it.ownText)
        }
        //затем добавим в список все родительские пункты по принципу отсутствия точки в номере
        elements(byXpath("//table/tbody/tr[td[$nameCPColumn]//text()/parent::*[contains(text(),'AutoTest Dicts CP 0010')] and td[$columnOfNumber]//text()/parent::*[not(contains(text(),'.'))]]/td[$nameCPColumn]/text()/..")).forEach {
            listOfRemoved.add(it.ownText)
        }
        if (!element(byXpath("//table/tbody/tr//*[contains(text(),'Нет данных')]")).exists()){
            Assertions.assertTrue(elements(byXpath("//table/tbody/tr")).size == listOfRemoved.size)
        }
        listOfRemoved.forEach { nameCP ->
            element(byXpath("//table/tbody/tr[*[text()='$nameCP']]/td[last()]//button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            Thread.sleep(100)
            element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='dialog']//*[text()='Подтвердите удаление записи']/ancestor::div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            checkAlert(AlertsEnum.snackbarSuccess, "Запись удалена", true, waitTime)
            element(byXpath("//div[@role='dialog']"))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//div[@role='presentation']"))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//table/tbody/tr/td[$nameCPColumn]//text()/parent::*[text()='$nameCP']"))
                .shouldNot(exist, ofSeconds(longWait))
            element(byXpath("//table/tbody/tr//*[contains(text(),'AutoTest Dicts CP 0010') or contains(text(),'Нет данных')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        element(byXpath("//table/tbody/tr//*[contains(text(),'Нет данных')]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //устанавливаем фильтр
//        setFilterByEnum(FilterEnum.Типы_происшествий, "- отсутствует -", waitTime)
        setFilterByEnum(FilterEnum.Типы_происшествий, "П.5.1.5 Auto-Test", waitTime)
        tableSearchClose()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Упорядочим по номерам
        while (!element(byXpath("//table/thead/tr/th[$columnOfNumber]//*[@name='sortAsc']")).exists()){
            element(byXpath("//table/thead/tr/th[$columnOfNumber]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//table/thead/tr/th[$columnOfNumber]//*[@name]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        //ищем следующее порядковое значение для создания родительского пункта
        if (!element(byXpath("//table/tbody/tr//h6[text()='Нет данных']")).exists()){
            startNumber = element(byXpath("//table/tbody/tr[1]/td[$columnOfNumber]//text()/..")).ownText.substringBefore('.').toInt() + 1
        } else startNumber = 1
        //создаем родителей по количеству в parentCount
        for (i in 0 until parentCount){
            element(byXpath("//*[text()='Добавить новый']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //заполняем поля
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //Выбираем тип происшествия
            element(byXpath("//form[@novalidate]//*[text()='Тип происшествия*']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("(//div[@role='presentation']//*[@name='arrowRight'])[1]/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("(//div[@role='presentation']//li)[1]//*[@name='arrowDown']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@role='presentation']//*[text()='П.5.1.5 Auto-Test']/text()/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //Выбираем тип пункта плана
            element(byXpath("//form[@novalidate]//*[text()='Тип пункта плана']/following-sibling::*//*[@role='button' and text()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .shouldHave(text("Контакт"))
                .click()
            element(byXpath("//div[@role='presentation']//ul[@role='listbox']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@role='presentation']//ul[@role='listbox']//*[text()='Простой']/text()/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']//ul[@role='listbox']"))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//form[@novalidate]//*[text()='Тип пункта плана']/following-sibling::*//*[@role='button' and text()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .shouldHave(text("Простой"))
            //Номер пункта
            element(byXpath("//form[@novalidate]//*[text()='№ блока']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .sendKeys((startNumber + i).toString())
            //Исполнитель
            element(byXpath("//form[@novalidate]//*[text()='Ответственный']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .sendKeys("Robot")
            //Заполняем описание
            enterTextInMDtextboxByName("Описание блока", "AutoTest Dicts CP 0010 parent ${i + 1}", waitTime)
            //Уводим курсор на кнопку "Добавить и проверяем форму"
            element(byXpath("//*[text()='Добавить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .hover()
            shrinkCheckTool()
            element(byXpath("//*[contains(@class, 'rror') and not(contains(@class, 'Mirror') or contains(@class,'colorError'))]"))
                .shouldNot(exist, ofSeconds(waitTime))
            //жмем добавить
            element(byXpath("//*[text()='Добавить']/text()/ancestor::button"))
                .click()
            //ждем
            element(byXpath("//table/tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//h2[text()='Алгоритм реагирования']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Изменим первый родительский
        // Очищаем фильтры
        cleanFilterByEnum(listOf(), waitTime)
        //Ищем
        tableSearch("AutoTest Dicts CP 0010", waitTime)
        //считаем найденное
        elements(byXpath("//table/tbody/tr"))
            .should(CollectionCondition.size(parentCount))
        //Переходим в родителя 1
        element(byXpath("//table/tbody//*[text()='AutoTest Dicts CP 0010 parent 1']/ancestor::tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='textbox']/p[text()='AutoTest Dicts CP 0010 parent 1']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Меняем родителя 1
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//div[@role='textbox']/p[text()='AutoTest Dicts CP 0010 parent 1']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='textbox']/p[contains(text(),'Test Dicts CP 0010 parent 1')]"))
            .sendKeys(Keys.END, " отредактировано")
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .click()
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//div[@role='textbox']/p[text()='AutoTest Dicts CP 0010 parent 1 отредактировано']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Идем за данными для заполнения доп полей
        menuNavigation(Dictionaries.Hotlines, waitTime)
        tableColumnCheckbox("Наименование", true, waitTime)
        val hotlineNameColumn = tableNumberOfColumn("Наименование", waitTime)
        val rndHLName = elements(byXpath("//table/tbody/tr/td[$hotlineNameColumn]//text()/..")).random().ownText
        menuNavigation(Dictionaries.Officials, waitTime)
        tableColumnCheckbox("ФИО", true, waitTime)
        tableColumnCheckbox("Раб.телефон", true, waitTime)
        val officialsFIOColumn = tableNumberOfColumn("ФИО", waitTime)
        val officialsWorkPhoneColumn = tableNumberOfColumn("Раб.телефон", waitTime)
        val rndOfficialsFIO = elements(byXpath("//table/tbody/tr/td[$officialsFIOColumn]//text()/..")).random().ownText.trim()
        val officialWorkPhone = element(byXpath("//table/tbody/tr//*[text()='$rndOfficialsFIO']/ancestor::tr/td[$officialsWorkPhoneColumn]//text()/..")).ownText.trim()
        //Возвращаемся в справочник
        menuNavigation(Dictionaries.CumulativePlans, waitTime)
        var planItemType = ""
        for (i in 1..4){
            when(i){
                1 -> planItemType = "Простой"
                2 -> planItemType = "Контакт"
                3 -> planItemType = "Назначение"
                4 -> planItemType = "Информационный"
            }
            element(byXpath("//*[text()='Добавить новый']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //заполняем поля
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //Тип происшествия
            element(byXpath("//form[@novalidate]//*[text()='Тип происшествия*']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("(//div[@role='presentation']//*[@name='arrowRight'])[1]/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("(//div[@role='presentation']//li)[1]//*[@name='arrowDown']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@role='presentation']//*[text()='П.5.1.5 Auto-Test']/text()/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //Выбираем родителя
            element(byXpath("//form[@novalidate]//*[text()='Родительский блок']/following-sibling::*//input[@name='parent']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']//ul[@role='listbox']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@role='presentation']//ul[@role='listbox']//*[text()='$startNumber AutoTest Dicts CP 0010 parent 1 отредактировано']/text()/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//form[@novalidate]//*[text()='Родительский блок']/following-sibling::*//input[@name='parent']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .shouldHave(attribute("value", "$startNumber AutoTest Dicts CP 0010 parent 1 отредактировано"))
            //Выбираем тип пункта плана
            element(byXpath("//form[@novalidate]//*[text()='Тип пункта плана']/following-sibling::*//*[@role='button' and text()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .shouldHave(text("Контакт"))
                .click()
            element(byXpath("//div[@role='presentation']//ul[@role='listbox']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@role='presentation']//ul[@role='listbox']//*[text()='$planItemType']/text()/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']//ul[@role='listbox']"))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//form[@novalidate]//*[text()='Тип пункта плана']/following-sibling::*//*[@role='button' and text()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .shouldHave(text(planItemType))
            //Номер пункта
            element(byXpath("//form[@novalidate]//*[text()='№ пункта']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .sendKeys(i.toString())
            //Исполнитель
            element(byXpath("//form[@novalidate]//*[text()='Ответственный']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .sendKeys("Robot")
            //заполняем описание
            enterTextInMDtextboxByName("Описание мероприятий", "AutoTest Dicts CP 0010 child $i $planItemType", waitTime)
            listOfChildPC.add("AutoTest Dicts CP 0010 child $i $planItemType")
            //заполняем контакт или назначение
            when(planItemType){
                "Контакт" -> {
                    element(byXpath("//form[@novalidate]//h3[text()='Данные контакта']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    element(byXpath("//form[@novalidate]//*[text()='Выберите контакт из имеющихся']/following-sibling::*//input"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .sendKeys(rndOfficialsFIO)
                    element(byXpath("//div[@role='presentation']//*[text()='$rndOfficialsFIO $officialWorkPhone']/text()/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//div[@role='presentation']"))
                        .shouldNot(exist, ofSeconds(waitTime))
                    element(byXpath("//form[@novalidate]//*[text()='Выберите контакт из имеющихся']/following-sibling::*//input[@value='$rndOfficialsFIO $officialWorkPhone']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    element(byXpath("//form[@novalidate]//*[text()='Добавить контакт']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//form[@novalidate]//*[@name='contacts']/ancestor::div[2]//*[text()='$rndOfficialsFIO $officialWorkPhone']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                }
                "Назначение" -> {
                    element(byXpath("//form[@novalidate]//h3[text()='Данные адресата назначения']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //element(byXpath("//form[@novalidate]//*[text()='Выберите службу из имеющихся']/following-sibling::*//input"))
                    element(byXpath("//div[@data-testid='typeData']//*[text()='Выберите службу из имеющихся']/following-sibling::*//input"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .sendKeys(rndHLName)
                    element(byXpath("//div[@role='presentation']//*[contains(text(),'$rndHLName')]/text()/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//div[@role='presentation']"))
                        .shouldNot(exist, ofSeconds(waitTime))
                    element(byXpath("//div[@data-testid='typeData']//input[@value='$rndHLName']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
//                    element(byXpath("(//form[@novalidate]//*[text()='Добавить']/text()/ancestor::button)[1]"))
//                        .should(exist, ofSeconds(waitTime))
//                        .shouldBe(visible, ofSeconds(waitTime))
//                        .click()
//                    element(byXpath("//form[@novalidate]//p[text()='$rndHLName']"))
//                        .should(exist, ofSeconds(waitTime))
//                        .shouldBe(visible, ofSeconds(waitTime))
                }
            }
            //Уводим курсор на кнопку "Добавить и проверяем форму"
            element(byXpath("(//*[text()='Добавить']/text()/ancestor::button)[last()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .hover()
            shrinkCheckTool()
            element(byXpath("//*[contains(@class, 'rror') and not(contains(@class, 'Mirror') or contains(@class,'colorError'))]"))
                .shouldNot(exist, ofSeconds(waitTime))
            //Жмем "добавить"
            element(byXpath("(//*[text()='Добавить']/text()/ancestor::button)[last()]"))
                .click()
            //ждем
            element(byXpath("//table/tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//h2[text()='Алгоритм реагирования']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Создаем КП
        menuNavigation(Incidents.CreateIncident, waitTime)
        createICToolCalltype("", waitTime)
        createICToolPhone("", waitTime)
        createICToolFIO(generateLastNameF(), generateFirstNameI(), generateMiddleNameO(), waitTime)
        createICToolRandomCoordinates("", waitTime)
        createICToolsDopInfo("$dateTime AutoTest Dicts CP 0010 Создание, изменение, перемещение и удаление пунктов реагирования", waitTime)
        element(byXpath("//*[text()='Создать карточку']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Выбираем тип происшествия
        element(byXpath("//form[@novalidate]//*[text()='Типы происшествий']/following-sibling::*//input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("(//div[@role='presentation']//*[@name='arrowRight'])[1]/ancestor::li"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("(//div[@role='presentation']//li)[1]//*[@name='arrowDown']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//div[@role='presentation']//*[text()='П.5.1.5 Auto-Test']/text()/ancestor::li"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        createICToolButtonCreateNewIC("$dateTime AutoTest Dicts CP 0010 Создание, изменение, перемещение и удаление пунктов реагирования", waitTime)
        checkICToolIsStatus(`В обработке`, waitTime)
        checkICToolDopInfo("$dateTime AutoTest Dicts CP 0010 Создание, изменение, перемещение и удаление пунктов реагирования", waitTime)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Проверяем то, что до манипуляций "Информационный" пункт выполнен
        elements(byXpath("//div[@id='iplan']//*[contains(@aria-label,'Статус: Выполнен')]"))
            .shouldHave(CollectionCondition.size(1))
        var doneCount = 1
        //Раскрываем созданного родителя
        element(byXpath("//div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 parent 1 отредактировано']/ancestor::div[@role='button' and @aria-expanded='false']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 parent 1 отредактировано']/ancestor::div[@role='button' and @aria-expanded='false']"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 parent 1 отредактировано']/ancestor::div[@role='button' and @aria-expanded='true']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //раскрываем каждого из созданных детей
        listOfChildPC.forEachIndexed { index, childPCName ->
            val clickLocator = "//div[@id='iplan']//*[text()='$childPCName']"
            val stateLocator = "//div[@id='iplan']//*[text()='$childPCName']/ancestor::div[@role='button' and @aria-expanded='%s']"
            element(byXpath(clickLocator))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .scrollIntoView("{block: \"center\"}")
                .click()
            element(byXpath(stateLocator.format("false")))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath(stateLocator.format("true")))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //В зависимости от типа делаем доп действие
            if (childPCName.contains("Контакт")){
                //div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 child 2 Контакт']/ancestor::div[@role='button' and @aria-expanded='true']//*[@name='phoneCall']/ancestor::button[2]
                element(byXpath(stateLocator.format("true") + "//*[@name='phoneCall']/ancestor::button[2]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //Пока не победил микрофон для запуска браузера
//                    .click()
//                element(byXpath("//div[@role='presentation']//*[@name='phoneCall']/ancestor::*[text()='Позвонить']"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
//                element(byXpath("//div[@role='tooltip' and @data-popper-placement='bottom']//*[@name='incomingCall']"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                Thread.sleep(1000)
//                if (element(byXpath("//div[@role='tooltip' and @data-popper-placement='bottom']//*[text()='Завершить']/text()/ancestor::button")).exists()){
//                    element(byXpath("//div[@role='tooltip' and @data-popper-placement='bottom']//*[text()='Завершить']/text()/ancestor::button"))
//                        .shouldBe(visible, ofSeconds(waitTime))
//                        .click()
//                }
            } else if (childPCName.contains("Назначение")){
                //div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 child 3 Назначение']/ancestor::div[@role='button' and @aria-expanded='true']//form[@novalidate]//*[text()='Назначить']/text()/ancestor::button
                //div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 child 3 Назначение']/ancestor::div[@role='button' and @aria-expanded='true']/..//*[text()='Описание назначения']/../following-sibling::*//p
                //div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 child 3 Назначение']/ancestor::div[@role='button' and @aria-expanded='true']//form[@novalidate]//*[text()='Назначено']/text()/ancestor::button[@disabled]
                element(byXpath(stateLocator.format("true") + "/..//*[text()='Описание назначения']/../following-sibling::*//p"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .sendKeys("Назначено AutoTest Dicts CP 0010")
                element(byXpath(stateLocator.format("true") + "/ancestor::form[@novalidate]//*[text()='Назначить']/text()/ancestor::button"))
                    //div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 child 3 Назначение']/ancestor::div[@role='button' and @aria-expanded='true']
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .scrollIntoView("{block: \"center\"}")
                    .click()
                element(byXpath(stateLocator.format("true") + "/ancestor::form[@novalidate]//*[text()='Назначить']/text()/ancestor::button"))
                    .shouldNot(exist, ofSeconds(longWait))
                element(byXpath(stateLocator.format("true") + "/ancestor::form[@novalidate]//*[text()='Назначено']/text()/ancestor::button[@disabled]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
//            element(byXpath("//div[@id='iplan']//*[text()='$childPCName']/ancestor::div[@role='button' and @aria-expanded='true']/..//*[text()='Выполнить']/text()/ancestor::button"))
            if (childPCName.contains("Информационный")){
                element(byXpath(stateLocator.format("true") + "/..//*[text()='Уточните комментарий']/../following-sibling::*//p"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .sendKeys("\"$childPCName\" Проверен")
                element(byXpath(stateLocator.format("true") + "/..//*[text()='Выполнить']/text()/ancestor::button"))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath(stateLocator.format("true") + "/..//*[text()='Отменить выполнение']/text()/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath(stateLocator.format("true") + "/..//*[text()='Сохранить комментарий без изменения статуса']/text()/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .scrollIntoView("{block: \"center\"}")
                    .click()
            } else {
                element(byXpath(stateLocator.format("true") + "/..//*[text()='Уточните комментарий']/../following-sibling::*//p"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .sendKeys("\"$childPCName\" Выполнен")
                element(byXpath(stateLocator.format("true") + "/..//*[text()='Выполнить']/text()/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .scrollIntoView("{block: \"center\"}")
                    .click()
                doneCount += 1
            }
            element(byXpath(stateLocator.format("true")))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath(stateLocator.format("false")))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@id='iplan']//*[text()='AutoTest Dicts CP 0010 parent 1 отредактировано']/ancestor::div[@role='button' and @aria-expanded='true']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            elements(byXpath("//div[@id='iplan']//*[contains(@aria-label,'Статус: Выполнен')]"))
                .shouldHave(CollectionCondition.size(doneCount))
        }
        //Проверяем назначенную ДДС в карточке
        element(byXpath("//form[@novalidate]//div[@id='hotlines']//form[@novalidate]//*[text()='$rndHLName']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byXpath("//form[@novalidate]//div[@id='hotlines']//form[@novalidate]//*[text()='$rndHLName']"))
            .shouldHave(CollectionCondition.size(1))
        //Проверяем назначенную ДДС на вкладке
        element(byXpath("//header//div[@role='tablist']//*[text()='Работа с ДДС']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .scrollIntoView("{block: \"center\"}")
            .click()
        element(byXpath("//div[@role='tabpanel' and @id='simple-tabpanel-hotlines']//*[text()='Выбрать ДДС']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//form[@novalidate]//*[text()='$rndHLName']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byXpath("//form[@novalidate]//*[text()='$rndHLName']"))
            .shouldHave(CollectionCondition.size(1))
        //header//div[@role='tablist']//*[text()='Работа с ДДС']/text()/ancestor::button
        //А теперь удаляем все
        listOfRemoved.clear()
        menuNavigation(Dictionaries.CumulativePlans, waitTime)
        tableColumnCheckbox("Наименование мероприятий/блока", true, waitTime)
        nameCPColumn = tableNumberOfColumn("Наименование мероприятий/блока", waitTime)
        //очищаем фильтр
        cleanFilterByEnum(listOf(), waitTime)
        //выводим столбец с нумерацией пунктов
        tableColumnCheckbox("№", true, waitTime)
        //запоминаем столбец с нумерацией пунктов
        columnOfNumber = tableNumberOfColumn("№", waitTime)
        //удалим результаты предыдущих провальных тестов
        tableSearch("AutoTest Dicts CP 0010", waitTime)
        element(byXpath("//table/tbody/tr//*[contains(text(),'AutoTest Dicts CP 0010') or contains(text(),'Нет данных')]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //сначала добавим в список все дочерние пункты по принципу наличия точки в номере
        elements(byXpath("//table/tbody/tr[td[$nameCPColumn]//text()/parent::*[contains(text(),'AutoTest Dicts CP 0010')] and td[$columnOfNumber]//text()/parent::*[contains(text(),'.')]]/td[$nameCPColumn]/text()/..")).forEach {
            listOfRemoved.add(it.ownText)
        }
        //затем добавим в список все родительские пункты по принципу отсутствия точки в номере
        elements(byXpath("//table/tbody/tr[td[$nameCPColumn]//text()/parent::*[contains(text(),'AutoTest Dicts CP 0010')] and td[$columnOfNumber]//text()/parent::*[not(contains(text(),'.'))]]/td[$nameCPColumn]/text()/..")).forEach {
            listOfRemoved.add(it.ownText)
        }
        if (!element(byXpath("//table/tbody/tr//*[contains(text(),'Нет данных')]")).exists()){
            Assertions.assertTrue(elements(byXpath("//table/tbody/tr")).size == listOfRemoved.size)
        }
        listOfRemoved.forEach { nameCP ->
            element(byXpath("//table/tbody/tr[*[text()='$nameCP']]/td[last()]//button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            Thread.sleep(100)
            element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='dialog']//*[text()='Подтвердите удаление записи']/ancestor::div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            checkAlert(AlertsEnum.snackbarSuccess, "Запись удалена", true, waitTime)
            element(byXpath("//div[@role='dialog']"))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//div[@role='presentation']"))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//table/tbody/tr/td[$nameCPColumn]//text()/parent::*[text()='$nameCP']"))
                .shouldNot(exist, ofSeconds(longWait))
            element(byXpath("//table/tbody/tr//*[contains(text(),'AutoTest Dicts CP 0010') or contains(text(),'Нет данных')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        element(byXpath("//table/tbody/tr//*[contains(text(),'Нет данных')]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
    }

}