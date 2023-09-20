package otus

import BaseTest
import Retry
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.DownloadOptions
import com.codeborne.selenide.FileDownloadMode
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.back
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import test_library.filters.FilterEnum
import test_library.menu.MyMenu
import test_library.menu.SubmenuInterface
import java.time.Duration
import java.time.Duration.ofSeconds

class Otus: BaseTest() {

    @DataProvider(name = "Табличные справочники")//, parallel = true)
    fun returnTablesForCSV(): Array<Array<MyMenu.OtusDictionaries>> =
        MyMenu.OtusDictionaries.values().filter { it.table }.map { arrayOf ( it) }.toTypedArray()


    @Test(retryAnalyzer = Retry::class, dataProvider = "Табличные справочники", groups = ["OTUS"])
    fun `OTUS 0010 Проверка скачивания и корректности табличного CSV файла`(submenuInterface: SubmenuInterface) {
        val listOfExceptionColumn = listOf<String>("Карта", "Файлы", "Отобразить при назначении", "Пользователь", "Статус камеры", "Статус датчика")
        logonTool(false)
        menuNavigation(submenuInterface, shortWait)
        tableColumnCheckbox("", true, shortWait)
        //Если таблица иерархическая раскроем иерархию
        if (element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).exists()){
            element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                .click()
            element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                .shouldNot(exist, ofSeconds(shortWait))
            element(byXpath("//table/thead/tr/th[1]//*[@name='arrowDown']/ancestor::button"))
                .should(exist, ofSeconds(shortWait))
                .shouldBe(visible, ofSeconds(shortWait))
        }
        var contolCSVStringCount = 0
        val columnNameList = mutableListOf<String>()
        elements(byXpath("//table/thead/tr/th")).forEach { columnNameList.add(it.innerText())  }
        columnNameList.removeLast()
        val contolTableStringCount = elements(byXpath("//table/tbody/tr")).size
        val testFile = element(byXpath("//a[@download='download.csv']/.."))
            .download(DownloadOptions.using(FileDownloadMode.FOLDER).withTimeout(59999))
            .reader()
            .readText()
            .split("\n")
            .forEachIndexed {indexOfString, oneString ->
                if (indexOfString == 0){
                    columnNameList.forEach {
                        if (!listOfExceptionColumn.contains(it)){
                            Assertions.assertTrue(oneString.contains(it))
                        }
                    }
                }
                Assertions.assertFalse(oneString.lowercase().contains("object"))
                contolCSVStringCount += 1
            }
        if (!element(byXpath("//table/tbody/tr//h6[text()='Нет данных']")).exists()){
            Assertions.assertTrue(contolCSVStringCount > 1)
            Assertions.assertTrue(contolCSVStringCount >= contolTableStringCount + 1)
        } else {
            Assertions.assertTrue(contolCSVStringCount == 1)
            Assertions.assertTrue(contolCSVStringCount == contolTableStringCount)
        }
    }

    @Test (retryAnalyzer = Retry::class, groups = ["OTUS"])
    fun `OTUS 0020 Проверка наличия Организаций с метками ОМПЛ, ПОО и СЗО в справочнике «Организации»`() {
        //A33 Убедиться в наличии списка объектов  в справочнике «Организации»
        //логинимся
        logonTool(false)
        //кликаем по иконке справочников в боковом меню
        //Переходим в справочник "Организации"
        menuNavigation(MyMenu.Dictionaries.Companies, shortWait)
        //сравниваем колличество строк организаций, по условию больше или равно с 5
        element(byCssSelector("tbody>tr"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        setTableStringsOnPage(100, shortWait)
        elements(byCssSelector("tbody>tr"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5))
        setFilterByEnum(FilterEnum.Метки, "ОМПЛ;ПОО;СЗО", shortWait)
        //ищем метки ОМПЛ и сравниваем их количество на больше или равно 1
        elements(byXpath("//tbody/tr//*[text()='ОМПЛ']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
        //ищем метки ПОО и сравниваем их количество на больше или равно 1
        elements(byXpath("//tbody/tr//*[text()='ПОО']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
        //ищем метки СЗО и сравниваем их количество на больше или равно 1
        elements(byXpath("//tbody/tr//*[text()='СЗО']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
    }

    @Test (retryAnalyzer = Retry::class, groups = ["OTUS"])
    fun `OTUS 0030 Просмотр паспортов опасных объектов`() {
        val listOfOpenedFiles = listOf<String>("jpeg", "jpg", "png", "svg")
        //A.3.21 Просмотр паспортов опасных объектов
        //Из различных вариантов проверок скачивания+целостности скачивания было нагуглено, что самое оптимальное это метод .download()
        //который упадет если не сможет скачать файл
        val companiesList = mutableListOf<String>()
        logonTool(true)
        menuNavigation(MyMenu.Dictionaries.Companies, shortWait)
        setTableStringsOnPage(50, shortWait)
        //кликаем по иконке справочников в боковом меню
        //ждем загрузки
        element(byCssSelector("tbody>tr"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //ищем столбец "файлы"
        tableColumnCheckbox("Наименование;Файлы", true, shortWait)
        element(byXpath("//*[text()='Файлы']/ancestor::thead"))
            .should(exist, ofSeconds(shortWait))
        element(byXpath("//*[text()='Наименование']/ancestor::thead"))
            .should(exist, ofSeconds(shortWait))
        val columnOfFile = tableNumberOfColumn("Файлы", shortWait)
        val columnOfName = tableNumberOfColumn("Наименование", shortWait)
        elements(byXpath("//table/tbody/tr/td[$columnOfFile]//*[@name='clip']/ancestor::tr/td[$columnOfName]//text()/..")).forEach { companie ->
            companiesList.add(companie.ownText)
        }
        companiesList.forEach { companieName ->
            element(byXpath("//table/tbody"))
                .should(exist, ofSeconds(shortWait))
                .shouldBe(visible, ofSeconds(shortWait))
            element(byXpath("//*[text()='$companieName']/text()/ancestor::td"))
                .should(exist, ofSeconds(shortWait))
                .shouldBe(visible, ofSeconds(shortWait))
                .click()
            element(byCssSelector("div#card"))
                .should(exist, ofSeconds(shortWait))
                .shouldBe(visible, ofSeconds(shortWait))
            element(byXpath("//h3[text()='Паспорт объекта, файлы']/parent::div[@id='files']//*[@aria-label and not(@aria-label='Действия над файлом')]/a"))
                .scrollIntoView(true)
            elements(byXpath("//h3[text()='Паспорт объекта, файлы']/parent::div[@id='files']//*[@aria-label and not(@aria-label='Действия над файлом')]/a/.."))
                .forEach { oneFile ->
                    oneFile.findAll(byXpath(".//*[text()]")).shouldHave(CollectionCondition.size(1))
                    val fileName = oneFile.find(byXpath(".//*[text()]")).ownText
                    val fileExtension = oneFile.find(byXpath("./a")).getAttribute("href")?.substringAfterLast('.')?.lowercase()
                    if (listOfOpenedFiles.contains(fileExtension)){
                        oneFile
                            .scrollIntoView("{block: \"center\"}")
                            .click()
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']"))
                            .should(exist, ofSeconds(shortWait))
                            .shouldBe(visible, ofSeconds(shortWait))
                            .shouldHave(text(fileName))
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Сохранить ']//*[text()='файл']"))
                            .download(DownloadOptions.using(FileDownloadMode.PROXY).withTimeout(59999))
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']//button[@aria-label='close']"))
                            .should(exist, ofSeconds(shortWait))
                            .shouldBe(visible, ofSeconds(shortWait))
                            .scrollIntoView("{block: \"center\"}")
                            .click()
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']"))
                            .shouldNot(exist, ofSeconds(shortWait))
                    } else {
                        oneFile.download(DownloadOptions.using(FileDownloadMode.PROXY).withTimeout(59999))
                    }
                }
            back()
        }
    }


    @Test (retryAnalyzer = Retry::class, dataProvider = "Табличные справочники", groups = ["OTUS"])
    fun `OTUS 0040 Проверка поиска справочных данных`(subMenu: SubmenuInterface) {
        //A.3.29 Проверка поиска справочных данных
        logonTool(false)
        menuNavigation(subMenu, shortWait)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //отчищаем фильтры
            cleanFilterByEnum(listOf(), shortWait)
        //добавляем все доступные колонки в таблицу
        tableColumnCheckbox("", true, shortWait)
        //получаем счетчик строк в левом нижнем углу страницы, в виде числа
        val allRecordCountUse = getTableStringsTotal(shortWait)
        //ситаем количество слолбцов, при том что последний нам не пригодится
        val comumnCount = elements(byXpath("//table/thead/tr/th")).size
        element(byXpath("//*[@name='search']/following-sibling::input[@placeholder]"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //читаем что есть в подсказке
        val searchHintList = element(byCssSelector("input[placeholder]")).getAttribute("placeholder")!!.split(", ")
        //проходимся по заголовкам столбцов, сверяя их с каждой позицией подсказки
        var searchValue = ""
        var fullSearchValue = ""
        var firstColumnIsButton = 0
        var breakReadColumnName = 0
        for (col in 1 until comumnCount) {
            element(byXpath("//table/thead/tr/th[$col]"))
                .should(exist, ofSeconds(shortWait))
                .shouldBe(visible, ofSeconds(shortWait))
            //если столбец, кнопка без текста, то жмем её и переходим к другим столбцам
            // (по идее это только иерархический столбец)
            if ((elements(byXpath("//table/thead/tr/th[$col]//text()/..")).size == 0)
                && (elements(byXpath("//table/thead/tr/th[$col]//button")).size == 1)){
                element(byXpath("//table/thead/tr/th[$col]//button"))
                    .should(exist, ofSeconds(shortWait))
                    .shouldBe(visible, ofSeconds(shortWait))
                    .click()
                element(byCssSelector("thead>tr>th svg[name='arrowDown']"))
                    .should(exist, ofSeconds(shortWait))
                    .shouldBe(visible, ofSeconds(shortWait))
                firstColumnIsButton = 1
            }
            //проверяем существует ли заголовок столбца, и если существует, то:
            else {
                val columnName = element(byXpath("//table/thead/tr/th[$col]//text()/..")).ownText.toString()
                for (hint in 0 until searchHintList!!.size){
                    //если заголовок столбца совпал с подсказкой, то вбиваем значение этого столбца из каждой строки в список из которого выберем случайное значение для поиска, ждем что нижний счетчик строк будет строго меньше исходного
//                        if (columnName.contains(searchHintList[hint])
                    if (columnName == searchHintList[hint]
                        || ((columnName == "Телефонный код") && (searchHintList[hint] == "Тел.код"))
                        || ((columnName == "Метка") && (searchHintList[hint] == "Имя метки"))
                        || ((columnName == "№") && (searchHintList[hint] == "Номер пункта"))
                    ) {
                            if (elements(byXpath("//table/tbody/tr/td[$col]//text()/parent::*[not(text()='  ')]")).isNotEmpty()) {
                                do {
                                    searchValue = elements(byXpath("//table/tbody/tr/td[$col]//text()/parent::*[not(text()='  ')]"))
                                        .random().ownText
                                } while (searchValue.length < 3)
                            } else {
                                setTableStringsOnPage(500, shortWait)
                                do {
                                    searchValue = elements(byXpath("//table/tbody/tr/td[$col]//text()/parent::*[not(text()='  ')]"))
                                        .random().ownText
                                } while (searchValue.length < 3)
                                setTableStringsOnPage(20, shortWait)
                            }
                            fullSearchValue = searchValue
                            //проверяем не номер ли это телефона и видоизменяем запись , к.т. в формате +Х(ХХХ)ХХХ-ХХ-ХХ в поисковой строке не вернет результатов, только +ХХХХХХХХХХ
                            //аналогично с ФИО
                            val ioRegex = Regex("[а-яА-Яa-zA-Z]{2,}\\s{0,}(\\s{1}[а-яА-Яa-zA-Z]{1}[.]{1}){1,2}")
                            val telRegex = Regex("[+7(]{1}[0-9]{3}[)]{1}[0-9]{3}[-]{1}[0-9]{2}[-]{1}[0-9]{2}")
                            val workTelRegex = Regex("[0-9]{1}[-][0-9]{5}[-][0-9]{3}[-][0-9]{3}")
                            if (telRegex.containsMatchIn(searchValue)
                                || workTelRegex.containsMatchIn(searchValue)
                            ) {
                                searchValue = searchValue.filter { it.isDigit() }
                            } else if (ioRegex.containsMatchIn(searchValue)) {
                                searchValue = searchValue.split(" ")[0].trim()
                            }
                        //обрежем поисковое значение если оно слишком длинное
                        if (searchValue.length > 100){
                            searchValue = searchValue.take(100)
                        }
                        //открываем строку поиска, если закрылась (бывает с иерархическими справочниками)
                        if (elements(byCssSelector("input[placeholder]")).size == 0) {
                            element(byXpath("//*[@name='search']/ancestor::button")).click()
                        }
                        element(byCssSelector("input[placeholder]")).sendKeys(searchValue, Keys.ENTER)
                        if ((allRecordCountUse*15)<1000){
                            Thread.sleep(1000)
                        } else if ((allRecordCountUse*15)>5000){
                            Thread.sleep(5000)
                        } else {
                            Thread.sleep(allRecordCountUse.toLong() * 15)
                        }
                        val nowRecordCountUse = getTableStringsTotal(shortWait)
                        Assertions.assertTrue(allRecordCountUse > nowRecordCountUse)
                        element(byXpath("//table/tbody/tr/td[${col - firstColumnIsButton}]//text()/parent::*[text()='$fullSearchValue']"))
                            .should(exist, ofSeconds(shortWait))
                        element(byXpath("//input[@placeholder]/ancestor::div[1]//button")).click()
                        element(byCssSelector("input[placeholder]")).click()
                        if ((allRecordCountUse*15)<1000){
                            Thread.sleep(1000)
                        } else if ((allRecordCountUse*15)>5000){
                            Thread.sleep(5000)
                        } else Thread.sleep(allRecordCountUse.toLong() * 15)
                        breakReadColumnName += 1
                        break
                    }
                }
            }
            //если выполнили поиск по каждой подсказке импута поиска, то перестаем перебирать и сравнивать с подсказкой имена столбцов
            if (breakReadColumnName == searchHintList.size) break
        }
    }


    @Test (retryAnalyzer = Retry::class, groups = ["OTUS"])
    fun `OTUS 0050 Проверка присвоения и удаления меток в карточке организации`(){
        //A.3.31 Проверка задания меток для указания признаков объектов
        logonTool(false)
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation(MyMenu.Dictionaries.Companies, shortWait)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //считаем строки и переходим в случайную организацию
        val organizationTableStringCount = elements(byXpath("//table/tbody/tr")).size
        val rndOrganization = (1..organizationTableStringCount).random()
        element(byXpath("//table/tbody/tr[$rndOrganization]"))
            .scrollIntoView("{block: \"center\"}")
            .click()
        //ждем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //из-за задержки в загрузке страницы, на месте названия организации отображается "Организации", поэтому подождем некоторого количества элементов
        element(byXpath("//main//div[@id='dict-title']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='left-menu']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='labels']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='main']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='contacts']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='hotlines']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='details']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='assets']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='additional']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='files']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byXpath("//main//div[@id='card']/div[@id='positions']"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //запомнинаем какую организацию редактируем
        val organizationName = element(byXpath("//h1"))
            .ownText
        //считаем существующие метки на случай когда они есть и когда их нет
        var amountLabels = elements(byXpath("//div[@id='labels']//*[@aria-label]//span[text()]")).size
        val beforeLabelsList = mutableListOf<String>()
        val afterLabelsList = mutableListOf<String>()
        if (amountLabels > 0){
            for (i in 1..amountLabels){
                beforeLabelsList.add(element(byXpath("//div[@id='labels']//*[@aria-label][$i]//span[text()]")).ownText)
            }
        }
        //жмем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
            .click()
        //ждем кнопки "Сохранить", как показатель загрузки страницы
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        element(byCssSelector("form"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //скролим до Указать на карте
        element(byXpath("//*[text()='Указать на карте']/text()/ancestor::button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
            .scrollIntoView(false)
        //вставляем новую метку
        inputRandomNew("labelsId-textfield", true, shortWait)
        //пересчитываем метки
        amountLabels = elements(byXpath("//label[text()='Метки']/..//*[@aria-label]//span[text()]")).size
        //вносим каждую в список
        for (i in 1..amountLabels){
            afterLabelsList.add(element(byXpath("//label[text()='Метки']/..//*[@aria-label][$i]//span[text()]")).ownText)
        }
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
            .click()
        //ждем загрузки карточки организации
        //ждем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //убеждаемся, что не затерли ни одной метки, а именно добавили.
        Assertions.assertTrue(afterLabelsList.containsAll(beforeLabelsList))
        Assertions.assertTrue(afterLabelsList.size > beforeLabelsList.size)
        //убеждаемся что все метки есть на карточке
        afterLabelsList.forEach { label ->
            element(byXpath("//div[@id='labels']//*[@aria-label]//span[text()='$label']"))
                .should(exist, ofSeconds(shortWait))
        }
        logoffTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //теперь удалим эту (эти) метку(и)
        logonTool(false)
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation(MyMenu.Dictionaries.Companies, shortWait)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        tableColumnCheckbox("Наименование", true, shortWait)
        tableSearch(organizationName, shortWait)
        //переходим в ту же организацию
        element(byXpath("//table/tbody//*[text()='$organizationName']/text()/ancestor::td"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
            .click()
        //ждем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        val newLabelsList = afterLabelsList.minus(beforeLabelsList.toSet())
        afterLabelsList.forEach { label ->
            element(byXpath("//div[@id='labels']//*[@aria-label]//span[text()='$label']"))
                .should(exist, ofSeconds(shortWait))
                .shouldBe(visible, ofSeconds(shortWait))
        }
        //жмем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
            .click()
        //удаляем метку
        element(byXpath("//*[text()='Указать на карте']/text()/ancestor::button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
            .scrollIntoView(false)
        newLabelsList.forEach {
            element(byXpath("//label[text()='Метки']/..//span[text()='$it']/ancestor::*[@aria-label]//*[name()='svg']"))
                .should(exist, ofSeconds(shortWait))
                .shouldBe(visible, ofSeconds(shortWait))
                .click()
        }
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
            .click()
        //ждем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(shortWait))
            .shouldBe(visible, ofSeconds(shortWait))
        //убеждаемся, что нашей метки нет
        newLabelsList.forEach {
            element(byXpath("//div[@id='labels']//*[@aria-label]//span[text()='$it']"))
                .shouldNot(exist, ofSeconds(shortWait))
                .shouldNotBe(visible, ofSeconds(shortWait))
        }
        beforeLabelsList.forEach {
            element(byXpath("//div[@id='labels']//*[@aria-label]//span[text()='$it']"))
                .should(exist, ofSeconds(shortWait))
                .shouldBe(visible, ofSeconds(shortWait))
        }
    }
}