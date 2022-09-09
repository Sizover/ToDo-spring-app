package dicts

import Retry
import BaseTest
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.DataProvider
import java.time.Duration.ofSeconds
import java.time.LocalDateTime

class SearchTests : BaseTest(){
//Поштучная проверка справочников на работоспособность поиска,
// путем создания записи справочника с уникальными значениями поисковых атрибутов,
// с последующим удалением записи
//набор поисковых полей определяется как считыванием из тултипа подсказки, так и хардкодом, в случае несоответствия подсказки названиям полей.

    var date = ""
    var dateTime = ""
    //Время ожидания элементов при выполнении теста
    val waitTime: Long = 5
    val longWait: Long = 10

    @DataProvider(name = "Справочники единого алгоритма полной проверки поиска")
    open fun `Справочники полной проверки поиска`(): Any {
        return arrayOf<Array<Any>>(
            arrayOf("Видеокамеры", "Наименование", "Наименование"),
            arrayOf("Датчики", "Наименование", "Наименование"),
            arrayOf("Метки", "Имя метки", "Метка"),
            arrayOf("Силы и средства", "Наименование", "Наименование")
        )
    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class, dataProvider = "Справочники единого алгоритма полной проверки поиска" , groups = ["ALL"])
    fun `Search 0010 Проверка создания, поиска и удаления справочных сущностей некоторых справочников`
            (subMenu: String, nameOfName: String, nameColumnName: String) {
        //Видеокамеры
        logonTool()
        menuNavigation("Справочники", subMenu, waitTime)
        //открываем поиск что бы прочитать подсказку
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[@name='search']/following-sibling::input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val placeohlderList = element(byXpath("//*[@name='search']/following-sibling::input"))
            .getAttribute("placeholder")!!
            .split(", ")
        //жмем добавить
        element(byXpath("//*[contains(text(),'Добавить ')]/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//form[@novalidate]//*[text()='Общие данные']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //определяем переменные, дату как уникальный идентификатор, длинный селектор, список того что будем вбивать в поиск, список обязательных полей карточки
        dateTime = LocalDateTime.now().toString()
        val uniqueName = dateTime.filter { it.isDigit() }
        val mkdwnLabelSel= "//p[contains(text(),'%s')]/parent::div/following-sibling::div//p[@data-empty-text='Нажмите здесь, чтобы вводить текст']"
        var count = true
        val searchUnitsList = mutableListOf<String>()
        val redInputsList = mutableListOf<String>()
        Thread.sleep(500)
        //для каждого элемента подсказки, проверяем если ли поля ввода или МД поля с метками содержащими подсказку
        //и если есть, вбиваем туда значение
        placeohlderList.forEach { unitPlaceohlder ->
            //МД поля и просто импуты обрабатываем по отдельности
            if (elements(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/input")).size == 1){
                element(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/input"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                if ((unitPlaceohlder == "Источник") && (subMenu == "Датчики")){
                    element(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/input"))
                        .sendKeys("https://AT/source/${uniqueName}.com")
//                } else if ((unitPlaceohlder == "Код") && (subMenu == "Типы происшествий")){
//                    element(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/input"))
//                        .sendKeys("АТ")
                } else {
                    element(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/input"))
                        .sendKeys("AT_${unitPlaceohlder}_$uniqueName")
                }
                searchUnitsList
                    .add(element(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/input"))
                        .getAttribute("value").toString())
            } else if (elements(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/textarea[@name]")).size == 1){
                element(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/textarea[@name]"))
                    .sendKeys("AT_${unitPlaceohlder}_$uniqueName")
                searchUnitsList.add(element(byXpath("//label[contains(text(),'$unitPlaceohlder')]/following-sibling::div/textarea[@name]"))
                    .getAttribute("value").toString())
            } else if (elements(byXpath(mkdwnLabelSel.format(unitPlaceohlder))).size == 1){
                element(byXpath(mkdwnLabelSel.format(unitPlaceohlder)))
                    .click()
                element(byXpath(mkdwnLabelSel.format(unitPlaceohlder)))
                    .sendKeys("AT_${unitPlaceohlder}_$uniqueName")
                searchUnitsList.add("AT_${unitPlaceohlder}_$uniqueName")
//            } else if ((unitPlaceohlder == "Наименование") && (subMenu == "Организации")){
//                element(byXpath("//label[contains(text(),'Краткое наименование')]/following-sibling::div/input"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
//                element(byXpath("//label[contains(text(),'Краткое наименование')]/following-sibling::div/input"))
//                    .sendKeys("AT_Краткое наименование_$uniqueName")
//                searchUnitsList
//                    .add(element(byXpath("//label[contains(text(),'Краткое наименование')]/following-sibling::div/input"))
//                        .getAttribute("value").toString())
//                element(byXpath("//label[contains(text(),'Полное наименование')]/following-sibling::div/input"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
//                element(byXpath("//label[contains(text(),'Полное наименование')]/following-sibling::div/input"))
//                    .sendKeys("AT_Полное наименование_$uniqueName")
//                searchUnitsList
//                    .add(element(byXpath("//label[contains(text(),'Полное наименование')]/following-sibling::div/input"))
//                        .getAttribute("value").toString())
//                Thread.sleep(10000)
            } else {
                //а если нет, то укладываем тест, т.к. либо тест кривой, либо что-то не так с наименованиями подсказок и полей
                count = false
                println(unitPlaceohlder)
            }
            Assertions.assertTrue(count)
        }
        //для всех красных полей вытаскиваем ИД их импутов
        elements(byXpath("//div[contains(@class,'Mui-error')]/input")).forEach { el ->
            if (el.getAttribute("class").toString().contains("Select", true)){
                el.ancestor("div").click()
                Thread.sleep(500)
                elements(byXpath("//ul/li")).random().click()
            } else {
                redInputsList.add(el.getAttribute("id")!!)
            }
        }
        //и только потом (т.к. поле перестает быть красным при вводе, не хочу что бы это вызвало проблемы)
        //заполняем в эти импуты условно уникальные значения
        redInputsList.forEach { id ->
            element(byXpath("//input[@id='$id']")).click()
            if (id == "source"){
                element(byXpath("//input[@id='$id']")).sendKeys("http://search_0010_${id}.com")
            } else {
                element(byXpath("//input[@id='$id']")).sendKeys("Search 0010 $id")
            }
        }
        //просто кликаем на карандаш в шапке, что бы свернуть возможно развернутые импуты
        element(byXpath("//*[@name='edit']/ancestor::button"))
            .click()
        //проверяем что красных полей нет
        elements(byXpath("//div[contains(@class,'Mui-error')]/input"))
            .shouldHave(CollectionCondition.size(0), ofSeconds(waitTime))
        //сохраняем
        element(byXpath("//*[text()='Добавить']/ancestor::button"))
            .click()
        //ждем загрузки
        element(byXpath("//table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //проверяем что записей в таблице более одной
        elements(byXpath("//table/tbody/tr"))
            .shouldHave(CollectionCondition.sizeGreaterThan(1), ofSeconds(waitTime))
        checkbox(nameColumnName, true, waitTime)
        //проверяем открыт ли, и если нет открываем поиск
        if (elements(byXpath("//*[@name='search']/following-sibling::input")).size != 1){
            element(byXpath("//*[@name='search']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
        //проверяем заполнен ли он и если да, то отчищаем
        if (element(byXpath("//*[@name='search']/following-sibling::input")).getAttribute("value")!!.isNotEmpty()){
            element(byXpath("//*[@name='search']/following-sibling::input")).click()
            element(byXpath("//*[@name='search']/following-sibling::input")).sendKeys(Keys.END)
            repeat(element(byXpath("//*[@name='search']/following-sibling::input")).getAttribute("value")!!.length){
                element(byXpath("//*[@name='search']/following-sibling::input")).sendKeys(Keys.BACK_SPACE)
            }
        }

        //для каждой подсказки выполняем поиск по тому чначению, что вбили при создании
        searchUnitsList.forEach { searchUnit ->
            element(byXpath("//*[@name='search']/following-sibling::input")).click()
            Thread.sleep(500)
            element(byXpath("//*[@name='search']/following-sibling::input")).sendKeys(searchUnit)
            Thread.sleep(500)
            element(byXpath("//*[@name='search']/following-sibling::input")).sendKeys(Keys.ENTER)
            Thread.sleep(500)
            elements(byXpath("//table/tbody/tr"))
                .shouldHave(CollectionCondition.size(1), ofSeconds(waitTime))
            val nameColumn = numberOfColumn(nameColumnName, waitTime)
//            elements(byXpath("//table/tbody/tr/td[$nameColumn][text()='AT_${nameOfName}_$uniqueName']"))
//                .shouldHave(CollectionCondition.size(1), ofSeconds(waitTime))
            Assertions.assertTrue(
                elements(byXpath("//table/tbody/tr/td[$nameColumn][text()='AT_${nameOfName}_$uniqueName']")).size
                    +
                    elements(byXpath("//table/tbody/tr/td[$nameColumn]//*[text()='AT $nameOfName $uniqueName']")).size
                    == 1
            )
            if (subMenu == "Метки"){
                elements(byXpath("//table/tbody/tr/td[$nameColumn]//*[text()='AT $nameOfName $uniqueName']"))
                    .shouldHave(CollectionCondition.size(1), ofSeconds(waitTime))
            } else {
                elements(byXpath("//table/tbody/tr/td[$nameColumn][text()='AT_${nameOfName}_$uniqueName']"))
                    .shouldHave(CollectionCondition.size(1), ofSeconds(waitTime))
            }
            // и на последнем круге, удаляем созданную запись
            if (searchUnit == searchUnitsList.last()){
//                val buttonColumn = tools.numberOfColumn(" ", waitTime)
//                element(byXpath("//table/tbody/tr/td[$nameColumn][text()='AT_${nameOfName}_$uniqueName']/following-sibling::td[${buttonColumn - nameColumn}]//button"))
//                    .click()
//                if (subMenu == "Метки"){
//                    element(byXpath("//table/tbody/tr/td[$nameColumn]//*[text()='AT $nameOfName $uniqueName']/ancestor::td/following-sibling::td[${buttonColumn - nameColumn}]//button"))
//                        .click()
//                } else {
//                    element(byXpath("//table/tbody/tr/td[$nameColumn][text()='AT_${nameOfName}_$uniqueName']/following-sibling::td[${buttonColumn - nameColumn}]//button"))
//                        .click()
//                }
                element(byXpath("//table/tbody/tr/td[last()]//button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//*[text()='Удалить']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//*[@role='dialog']//*[text()='Удалить']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                Thread.sleep(500)
                element(byXpath("//table/tbody/tr/td[$nameColumn][text()='AT_${nameOfName}_$uniqueName']"))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//table/tbody/tr[1]//*[text()='Нет данных']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } else {
                //если круг не последний, то отчищаем поле поиска и ждем загрузки таблицы
                element(byXpath("//*[@name='search']/following-sibling::input/..//button"))
                    .click()
                Thread.sleep(500)
                elements(byXpath("//table/tbody/tr"))
                    .shouldHave(CollectionCondition.sizeGreaterThan(1), ofSeconds(waitTime))
                Thread.sleep(500)
            }
        }
        logoffTool()
    }
}