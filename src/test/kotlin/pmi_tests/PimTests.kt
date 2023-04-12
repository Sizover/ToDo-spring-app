package pmi_tests

//import kotlin.collections.EmptyMap.keys
//import test_library.statuses.StatusEnum.`В обработке`
//import test_library.statuses.StatusEnum.Реагирование
import BaseTest
import Retry
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byName
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.back
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.Selenide.switchTo
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import test_library.alerts.AlertsEnum.snackbarSuccess
import test_library.alerts.AlertsEnum.snackbarWarning
import test_library.filters.FilterEnum.Источники
import test_library.filters.FilterEnum.Пользователь
import test_library.filters.FilterEnum.Статусы
import test_library.filters.FilterEnum.Типы_происшествий
import test_library.filters.FilterEnum.Уровни
import test_library.menu.MyMenu.Dictionaries
import test_library.menu.MyMenu.Incidents
import test_library.menu.SubmenuInterface
import test_library.statuses.StatusEnum
import test_library.statuses.StatusEnum.`В обработке`
import test_library.statuses.StatusEnum.Завершена
import test_library.statuses.StatusEnum.Закрыта
import test_library.statuses.StatusEnum.Отменена
import test_library.statuses.StatusEnum.Реагирование
import java.io.File
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PimTests : BaseTest(){

    @DataProvider(name = "BrowserProvider")
    open fun Statuses(): Any {
        return arrayOf<Array<Any>>(
            arrayOf("CHROME"),
            arrayOf("FIREFOX"),
        )
    }



    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0010 Проверка загрузки таблиц справочников`() {
        //A31 Убедиться в наличии списка объектов  в справочнике «Муниципальные образования»
        //A34 Убедиться в наличии списка объектов  в справочнике «Должностные лица»
        //A35 Убедиться в наличии списка объектов  в справочнике «Дежурные службы»
        //A36 Убедиться в наличии списка объектов  в справочнике «Видеокамеры»
        //A37 Убедиться в наличии списка объектов  в справочнике «Датчики»
        //логинимся
        //val tools = Tools()
//        tools.logonTool()
        logonTool(false)
        //кликаем по иконке справочников в боковом меню
        //Переходим в "Муниципальные образования"
        var subMenu = Dictionaries.Municipalities
        for (i in 1..5){
            when(i){
                1 -> {subMenu = Dictionaries.Municipalities}
                2 -> {subMenu = Dictionaries.Officials}
                3 -> {subMenu = Dictionaries.Hotlines}
                4 -> {subMenu = Dictionaries.VideoCameras}
                5 -> {subMenu = Dictionaries.Labels}
            }
            menuNavigation(subMenu, waitTime)
            //сравниваем количество записей в таблице, на больше или равно 5
            element(byXpath("//table/tbody/tr"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            while (elements(byXpath("//table/thead/tr/th//*[@name='arrowRight']/ancestor::button")).size > 0){
                element(byXpath("//table/thead/tr/th//*[@name='arrowRight']/ancestor::button"))
                    .click()
                Thread.sleep(200)
            }
            elements(byXpath("//table/tbody/tr"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5))
        }
        //разлогиниваемся и закрываем браузер
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0020 Проверка загрузки и иерархии справочника "Типы происшествий"`() {
        //A32 Убедиться в наличии списка объектов  в справочнике «Типы происшествий»
        //логинимся
        //val tools = Tools()
        logonTool(false)
        //кликаем по иконке справочников в боковом меню
        //Переходим в "Типы происшествий"
        menuNavigation(Dictionaries.IncidentTypes, waitTime)
        //сравниваем количество записей в таблице, на больше или равно 5
        element(byXpath("//table/tbody/tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //раскрываем весь список, контролируя это изменением стрелки
        element(byXpath("//table/thead/tr//*[@name='arrowRight']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//table/thead/tr//*[@name='arrowRight']/ancestor::button"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//table/thead/tr//*[@name='arrowDown']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Thread.sleep(3000)
        val tableAllStringCount = elements(byXpath("//table/tbody/tr/td[1]")).size
        var leafStringCount = 0
        for (i in 1..tableAllStringCount){
            if (elements(byXpath("//table/tbody/tr[$i]/td[1]//button")).size == 0){
                leafStringCount += 1
            }
        }
        Assertions.assertTrue(leafStringCount >= 5)
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])

    fun `PMI 0030 Проверка наличия Организаций с метками ОМПЛ, ПОО и СЗО в справочнике «Организации»`() {
        //A33 Убедиться в наличии списка объектов  в справочнике «Организации»
        //логинимся
        //val tools = Tools()
        logonTool(false)
        //кликаем по иконке справочников в боковом меню
        //Переходим в справочник "Организации"
        menuNavigation(Dictionaries.Companies, waitTime)
        //сравниваем колличество строк организаций, по условию больше или равно с 5
        element(byCssSelector("tbody>tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableStringsOnPage(100, waitTime)
        elements(byCssSelector("tbody>tr"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5))
        //ищем метки ОМПЛ и сравниваем их количество на больше или равно 1
        elements(byXpath("//tbody/tr//*[text()='ОМПЛ']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
        //ищем метки ПОО и сравниваем их количество на больше или равно 1
        elements(byXpath("//tbody/tr//*[text()='ПОО']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
        //ищем метки СЗО и сравниваем их количество на больше или равно 1
        elements(byXpath("//tbody/tr//*[text()='СЗО']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
        logoffTool()
    }


    @Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0080 Проверка наличия КП в различных статусах`() {
        //A38 Убедиться в наличии списка объектов  в меню «Список происшествий»
        //логинимся
        //val tools = Tools()
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        //Подсчитываем всего карточек происшествий на странице и убеждаемся, что их больше чем необходимый минимум
        element(byXpath("//table/tbody/tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byXpath("//table/tbody/tr"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
        val collectionAll = elements(byXpath("//table/tbody/tr"))
        val countAll = collectionAll.size
        //подсчитываем количество карточек в разном статусе
        val collectionA = elements(byText("Новая"))
        val countA = collectionA.size
        val collectionB = elements(byText("В обработке"))
        val countB = collectionB.size
        val collectionC = elements(byText("Реагирование"))
        val countC = collectionC.size
        val collectionD = elements(byText("Завершена"))
        val countD = collectionD.size
        //сравниваем обшее число происшествий с карточкой каждого типа
        Assertions.assertTrue(countAll > countA)
        Assertions.assertTrue(countAll > countB)
        Assertions.assertTrue(countAll > countC)
        Assertions.assertTrue(countAll > countD)
        //println("$countA countA")
        //println("$countB countB")
        //println("$countC countC")
        //println("$countD countD")
        logoffTool()
    }


    @Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0090 Проверка загрузки таблицы «Архив происшествий»`() {
        //A39 Убедиться в наличии списка объектов  в меню «Архив происшествий»
        //логинимся
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Архив происшетвий"
        menuNavigation(Incidents.IncidentsArchive, waitTime)
        //сравниваем колличество карточек в архиве, по условию больше или равно с 2
        element(byXpath("//table/tbody/tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byXpath("//table/tbody/tr"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(2))
        logoffTool()

    }

//    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    @Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0100 Проверка процедуры авторизации в системе`() {
        //A310 Проверка процедуры авторизации в системе 1
        //val tools = Tools()
        authorizationTest()
        element(byName("login")).click()
        element(byCssSelector("#input-error")).shouldHave(exactText("Неправильное имя пользователя или пароль."))
        authorizationTest()
        element(byName("username")).sendKeys("test")
        element(byName("login")).click()
        element(byCssSelector("#input-error")).shouldHave(exactText("Неправильное имя пользователя или пароль."))
        authorizationTest()
        element(byName("username")).sendKeys("test")
        element(byName("password")).sendKeys("test")
        element(byName("login")).click()
        element(byCssSelector("#input-error")).shouldHave(exactText("Неправильное имя пользователя или пароль."))
        logoffTool()
    }


    @Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0110 Регистрация вызова (формирование карточки происшествия)(С прикреплением файлов)`() {
        //A311 Регистрация вызова (формирование карточки происшествия)(С прикреплением файлов)
        //A312 Проверка прикрепления файла к происшествию
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        //кликаем по "создать обращение"
        element(byXpath("//*[text()='Создать обращение']/text()/ancestor::button")).click()
        //заполняем карточку
        //Источник события - выбираем случайно
        createICToolCalltype("", waitTime)
        //Номер телефона
        createICToolPhone("", waitTime)
        //ФИО
        createICToolFIO("$date AutoTest", "PMI 0110", "", waitTime)
        //адрес с тестированием транслитерации и клик по dadata
        addressInput("callAddress","vbhf5", waitTime)
        //заполняем дополнительную информацию
        createICToolsDopInfo("AutoTest N 0110 $dateTime", waitTime)
        //регистрируем обращение
        element(byXpath("//*[text()='Создать карточку']/text()/ancestor::button")).click()
        //выбираем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test").sendKeys(Keys.DOWN, Keys.RETURN)
        //Создаем карточку
        pushButtonCreateIC("AutoTest N 0110 $dateTime", waitTime)
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus(`В обработке`, waitTime)
        //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest N 0110 $dateTime", waitTime)
        Thread.sleep(5000)
        //загружаем файлы проверяя их прикрепление
        element(byCssSelector("input#upload-file"))
            .uploadFile(File("/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/AutoTest.webp"))
        checkAlert(snackbarSuccess, "Файл загружен", true, longWait)
        element(byXpath("//div[@id='files']//*[text()='AutoTest.webp']"))
            .should(exist, ofSeconds(longWait))
        element(byCssSelector("input#upload-file"))
            .uploadFile(File("/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/Тестовый файл_.docx"))
        checkAlert(snackbarSuccess, "Файл загружен", true, waitTime)
        element(byXpath("//div[@id='files']//*[text()='Тестовый файл_.docx']"))
            .should(exist, ofSeconds(longWait))
        logoffTool()
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //`A312 Проверка прикрепления файла к происшествию`
        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий" )
        menuNavigation(Incidents.IncidentsList, waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tableColumnCheckbox("Описание", true, waitTime)
        //Переходим в созданное ранее происшествие
        element(byText("AutoTest N 0110 $dateTime")).click()
        checkICToolDopInfo("AutoTest N 0110 $dateTime", waitTime)
        //Проверяем ранее прикрепленные файлы
        element(byXpath("//div[@id='files']//*[text()='Тестовый файл_.docx']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//div[@id='files']//*[text()='AutoTest.webp']"))
            .should(exist, ofSeconds(waitTime))
        //Прикрепляем файл
        element(byCssSelector("input#upload-file")).uploadFile(File("/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/test.pdf"))
        checkAlert(snackbarSuccess, "Файл загружен", true, waitTime)
        element(byXpath("//div[@id='files']//*[text()='test.pdf']"))
            .should(exist, ofSeconds(longWait))
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0120 Проверка наличия опросника абонента (заявителя)`() {
        //A313 Проверка наличия опросника абонента (заявителя)
        //val tools = Tools()
        //логинимся
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        //кликаем по "создать обращение"
        element(byXpath("//*[text()='Создать обращение']/text()/ancestor::button")).click()
        //Проверяем форму подсказки по положению стрелки и части текста
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button//*[@d='M10 6 8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button//*[@d='M15.41 7.41 14 6l-6 6 6 6 1.41-1.41L10.83 12z']"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='\"Здравствуйте, оператор дежурной службы (имя и фамилия), представьтесь, пожалуйста\"']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button"))
            .click()
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button//*[@d='M15.41 7.41 14 6l-6 6 6 6 1.41-1.41L10.83 12z']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button//*[@d='M10 6 8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z']"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='\"Здравствуйте, оператор дежурной службы (имя и фамилия), представьтесь, пожалуйста\"']"))
            .should(exist, ofSeconds(waitTime))
            .shouldNotBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button"))
            .click()
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button//*[@d='M10 6 8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='Опросник']/text()/ancestor::button//*[@d='M15.41 7.41 14 6l-6 6 6 6 1.41-1.41L10.83 12z']"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='\"Здравствуйте, оператор дежурной службы (имя и фамилия), представьтесь, пожалуйста\"']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0130 Регистрация вызова (формирование карточки происшествия) с проверкой выполнения плана реагирования`() {
        //A.3.11 Регистрация вызова (формирование карточки происшествия)
        //А.3.14 Проверка формирования плана (алгоритма) реагирования по заданному событию (происшествию)
        //A.3.15 Проверка формирования действий оператора системы в рамках плана (алгоритма) реагирования с автоматизированным контролем выполнения
        //логинимся
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        //кликаем по "создать обращение"
        menuNavigation(Incidents.IncidentsList, waitTime)
        element(byXpath("//*[text()='Создать обращение']/text()/ancestor::button")).click()
        //заполняем карточку
        //Источник события - выбираем случайно
        createICToolCalltype("", waitTime)
        //Номер телефона
        createICToolPhone("", waitTime)
        //ФИО
        createICToolFIO("$date AutoTest", "PMI 0130", "", waitTime)
        //адрес с тестированием транслитерации и клик по dadata
        addressInput("callAddress","vbhf5", waitTime)
        //заполняем дополнительную информацию
        createICToolsDopInfo("AutoTest N 0130 $dateTime", waitTime)
        //регистрируем обращение
        element(byXpath("//*[text()='Создать карточку']/text()/ancestor::button")).click()
        //выбираем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test").sendKeys(Keys.DOWN, Keys.ENTER)
        //Создаем карточку
//        element(byXpath("//*[text()='Сохранить карточку']/text()/ancestor::button"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
        pushButtonCreateIC("AutoTest N 0130 $dateTime", waitTime)
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus(`В обработке`, longWait)
        //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest N 0130 $dateTime", waitTime)

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Здесь проверим А.3.14 Проверка формирования плана (алгоритма) реагирования по заданному событию (происшествию)
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Переходим в "Реагирование"
        element(byXpath("//*[text()='Реагирование']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Раскрываем всю карту реагирования
        element(byXpath("//*[text()='Развернуть все']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Проверяем подпункты 1.0; 1.1; 1.2; 2.0; 2.1; 2.2.
        val headSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child(%d) > div > div#panel1a-header"
        val headChildSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child(%d) >div>div>div>div>div#panel1a-content>div>div>div:nth-child(%d)"//>form"
        element(byCssSelector(headSelector.format(1)))
            .shouldHave(text("Мероприятие 1.0"))
        element(byCssSelector(headChildSelector.format(1, 1)))
            .shouldHave(text("Мероприятие 1.1"))
            .shouldHave(text("Ответственный исполнитель: "))
        element(byCssSelector(headChildSelector.format(1, 2)))
            .shouldHave(text("Мероприятие 1.2"))
        element(byCssSelector(headSelector.format(2)))
            .shouldHave(text("Мероприятие 2.0"))
        element(byCssSelector(headChildSelector.format(2, 1)))
            .shouldHave(text("Мероприятие 2.1"))
        element(byCssSelector(headChildSelector.format(2, 2)))
            .shouldHave(text("Мероприятие 2.2"))
        //Проверяем правильность упорядочевания пунктов (по седержимому пунктов)
        element(byCssSelector(headChildSelector.format(2, 10)))
            .shouldHave(text("Мероприятие 2.10"))
        element(byCssSelector(headChildSelector.format(2, 11)))
            .shouldHave(text("Мероприятие 2.11"))
        element(byCssSelector(headChildSelector.format(2, 12)))
            .shouldHave(text("Мероприятие 2.12"))
        element(byCssSelector(headChildSelector.format(2, 13)))
            .shouldHave(text("Мероприятие 2.13"))
            .shouldHave(text("Оператор AutoTest"))

        logoffTool()

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //`A315 Проверка формирования действий оператора системы в рамках плана (алгоритма) реагирования с автоматизированным контролем выполнения
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tableColumnCheckbox("Описание", true, waitTime)
        //Переходим в созданное ранее происшествие
        element(byText("AutoTest N 0130 $dateTime")).click()
        checkICToolDopInfo("AutoTest N 0130 $dateTime", waitTime)
        //Переходим в "Реагирование"
        element(byXpath("//*[text()='Реагирование']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Раскрываем всю карту реагирования
        element(byXpath("//*[text()='Развернуть все']/text()/ancestor::button")).click()
        //Определяем количество родительских пунктов
        element(byCssSelector("div#simple-tabpanel-iplan>div>div>div>div"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        val parentCount = elements(byCssSelector("div#simple-tabpanel-iplan>div>div>div>div")).size
        //большая петля для родительских пунктов
        for (p in 1..parentCount) {
            element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) > div > div#panel1a-header")).shouldHave(
                text("Мероприятие $p.0")
            )
            //определяем количество дочерших пунктов внутри каждого родителя
            val childCount =
                elements(byCssSelector("div#simple-tabpanel-iplan>div>div>div>div:nth-child($p) div#panel1a-content form")).size
            for (c in 1..childCount) {
                val colorItemSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) >div>div>div>div>div#panel1a-content>div>div>div:nth-child($c)>div[style='background-color: rgb(%s); border-color: rgb(%s);']"
                val itemSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) >div>div>div>div>div#panel1a-content>div>div>div:nth-child($c)"
                val circleSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) > div > div#panel1a-header div.MuiBox-root > div:nth-child($c) > span[aria-label^='Статус: %s'] rect[fill='%s']"
                val textFieldSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) >div>div>div>div>div#panel1a-content>div>div>div:nth-child($c) p[data-empty-text='Нажмите здесь, чтобы вводить текст']"
                element(byCssSelector(colorItemSelector.format("247, 248, 251", "208, 214, 220")))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //Переходим в дочерний пункт
                element(byCssSelector(itemSelector))
                    .shouldHave(text("Мероприятие $p.$c"))
                element(byCssSelector(itemSelector))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //Проверяем изменение стиля поля дочернего пункта, после его разворачивания
                element(byCssSelector(colorItemSelector.format("255, 255, 255", "66, 106, 210")))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //Проверяем красненький кружочек первого пункта (для проверки позеленения по мере выполнения)
//                element(byCssSelector(circleSelector.format("Не выполнен", "#BA3113")))
//                element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) > div > div#panel1a-header div.MuiBox-root > div:nth-child($c) > span[title^='Статус: Не выполнен'] rect[fill='#16BA13']"))
                element(byCssSelector(circleSelector.format("Не выполнен", "#63666C")))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //кликаем по полю ввода текста
                element(byCssSelector(textFieldSelector))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //вводим текст
                element(byCssSelector(textFieldSelector))
                    .sendKeys("Выполнено мероприятие $p.$c")
                //сохраняем
                element(byXpath("//*[text()='Выполнить']/text()/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //Проверяем и закрываем зеленую "всплывашку"
                try {
                    checkAlert(snackbarSuccess, "Пункт выполнен", true, longWait)
                } catch (_:  Throwable) {
                    if (print){ println("PMI 0130: При выполнении пункта реагирования не появился зеленый алерт") }
                }
                //Проверяем позеленение кружочка
                element(byCssSelector(circleSelector.format("Не выполнен", "#63666C")))
                    .shouldNot(exist, ofSeconds(longWait))
                element(byCssSelector(circleSelector.format("Выполнен", "#16BA13")))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //Проверяем цвета полей дочерних пунктов
                element(byCssSelector(colorItemSelector.format("247, 248, 251", "208, 214, 220")))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byCssSelector(colorItemSelector.format("232, 255, 224", "122, 249, 102")))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
        }

        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0140 Проверка фильтрации карточек происшествия`() {
        //A.3.16 Фильтрация карточек происшествия
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        elements(byXpath("//table/tbody/tr")).shouldHave(
            CollectionCondition.sizeGreaterThanOrEqual(1))
        //применяем фильтр "Типы происшествий"
        setFilterByEnum(Типы_происшествий, "К Консультации;Л Ложные", waitTime)
        //Дожидаемся применения фильтра
        Thread.sleep(500)
        tableColumnCheckbox("Подгруппа", true, waitTime)
        var targetColumn = tableNumberOfColumn("Подгруппа", waitTime)
        Thread.sleep(500)
        var intA: Int = elements(byXpath("//table/tbody/tr")).size
        var intB: Int = elements(byXpath("//table/tbody/tr/td[$targetColumn]//text()/parent::*[text()='Ложные']")).size
        var intC: Int = elements(byXpath("//table/tbody/tr/td[$targetColumn]//text()/parent::*[text()='Консультации']")).size
        var intS: Int = intB + intC
        Assertions.assertTrue(intS == intA)
        //Очищаем фильтр "Тип происшествия"
        cleanFilterByEnum(listOf(Типы_происшествий), waitTime)
        /////////////////////////////////////////////////////////////////////////////////////////
        //применяем фильтр "Статусы"
        tableColumnCheckbox("Статус", true, waitTime)
        setFilterByEnum(Статусы, "В обработке;Реагирование", waitTime)
        targetColumn = tableNumberOfColumn("Статус", waitTime)
        intA = elements(byXpath("//table/tbody/tr")).size
        intB = elements(byXpath("//tbody/tr/td[$targetColumn]//*[text()='В обработке']")).size
        intC = elements(byXpath("//tbody/tr/td[$targetColumn]//*[text()='Реагирование']")).size
        intS = intB + intC
        Assertions.assertTrue(intS == intA)
        //Очищаем фильтр "Статусы"
        cleanFilterByEnum(listOf(Статусы), waitTime)
        /////////////////////////////////////////////////////////////////////////////////////////
        //применяем фильтр "Уровни"
        setFilterByEnum(Уровни, "Угроза ЧС;ЧС", waitTime)
        //Дожидаемся применения фильтра
        Thread.sleep(2000)
        tableColumnCheckbox("Уровень происшествия", true, waitTime)
        targetColumn = tableNumberOfColumn("Уровень происшествия", waitTime)
        Thread.sleep(500)
        intA = elements(byXpath("//table/tbody/tr")).size
        intB = elements(byXpath("//tbody/tr/td[$targetColumn][text()='Угроза ЧС']")).size
        //intC = elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-'][style]")).size
        intC = elements(byXpath("//tbody/tr/td[$targetColumn][text()='ЧС']")).size
        intS = intB + intC
        Assertions.assertTrue(intS == intA)
        //Очищаем фильтр Уровни
        cleanFilterByEnum(listOf(Уровни), waitTime)
        /////////////////////////////////////////////////////////////////////////////////////////
        //Открываем фильтр "Источники"
        tableColumnCheckbox("Источник", true, waitTime)
        //Устанавливаем значения фильтров
        setFilterByEnum(Источники, "Видеоаналитика;СМС", waitTime)
        targetColumn = tableNumberOfColumn("Источник", waitTime)
        //ждем применения
        Thread.sleep(500)
        //Подсчитываем сработали ли фильтры
        intA = elements(byXpath("//table/tbody/tr")).size
        intB = elements(byText("Видеоаналитика")).size
        intC = elements(byText("СМС")).size
        intS = intB + intC
        //с источниками все не так просто - в таблицу происшествий выносится источник последнего обращения по происшествию, а фильтр работает по источникам всех обращений по проишествию
        //Проверяем не закрались ли записи с последним обращением из источника не взятого в фильтр
        if (intS < intA) {
            //и если это произошло, открываем каждую карточку и переходя в её обращения смотрим что там есть что-то из того, что взяли в фильтр
            for (i in 1 .. intA) {
                //опять побеждаем отлистывание страницы вниз
                if (i == intA){
                    Thread.sleep(500)
                    element(byCssSelector("body"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .sendKeys(Keys.END)
                } else {
                    element(byXpath("//table/tbody/tr[${i+1}]")).scrollIntoView(false)
                }
                if (element(byXpath("//table/tbody/tr[$i]/td[$targetColumn]")).ownText != "Видеоаналитика"
                    && element(byXpath("//table/tbody/tr[$i]/td[$targetColumn]")).ownText != "СМС")
                {
                    element(byXpath("//table/tbody/tr[$i]")).click()
                    element(byXpath("//*[text()='Обращения']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    //не учитывается, что обращений может быть больше 20(
                    Thread.sleep(500)
                    val sms = elements(byText("СМС")).size
                    val video = elements(byText("Видеоаналитика")).size
                    val summ = sms + video
                    //println("$sms - sms, $video - video")
                    Assertions.assertTrue(summ > 0)
                    back()
                    back()
                }
            }
        } else {
            Assertions.assertTrue(intS == intA)
        }
        logoffTool()
    }


    @DataProvider(name = "Статусы проверки единичной дочерней", parallel = false)
    open fun Status(): Any {
        return arrayOf<Array<Any>>(
            arrayOf(Завершена),
            arrayOf(Отменена),
            arrayOf(Реагирование),
            arrayOf(Закрыта)
        )
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, dataProvider = "Статусы проверки единичной дочерней", groups = ["ПМИ", "ALL"])
    fun `PMI 0150 Назначение карточки происшествия на службу ДДС-ЕДДС с последующей проверкой изменения статуса родительской карточки`(status: StatusEnum) {
        //A311 Регистрация вызова (формирование карточки происшествия)
        //A.3.17 Назначение карточки происшествия на службу ДДС/ЕДДС (только службы, которые подключены к Системе)
        //Проверка изменения статуса родительской карточки при изменении статуса карточки назначения
        //логинимся
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        //кликаем по "создать обращение"
        menuNavigation(Incidents.IncidentsList, waitTime)
        element(byXpath("//*[text()='Создать обращение']/text()/ancestor::button")).click()
        //заполняем карточку
        //Источник события - выбираем случайно
        createICToolCalltype("", waitTime)
        //Номер телефона
        createICToolPhone("", waitTime)
        //ФИО
        createICToolFIO("$date AutoTestLastname", "AutoTestFirstname", "", waitTime)
        //адрес с тестированием транслитерации и клик по dadata
        addressInput("callAddress","vbhf5",waitTime)
        //заполняем дополнительную информацию
        createICToolsDopInfo("AutoTest PMI 0150 $dateTime", waitTime)
        //регистрируем обращение
        element(byXpath("//*[text()='Создать карточку']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("П.5.1.5 Auto-Test", Keys.DOWN, Keys.ENTER)
        //Создаем карточку
        pushButtonCreateIC("AutoTest PMI 0150 $dateTime", waitTime)
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card"))
            .should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus(`В обработке`, waitTime)
        //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest PMI 0150 $dateTime", waitTime)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tableColumnCheckbox("Описание", true, waitTime)
        //Переходим в созданное ранее происшествие
        element(byText("AutoTest PMI 0150 $dateTime"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        checkICToolDopInfo("AutoTest PMI 0150 $dateTime", waitTime)
        //Переходим во вкладку "Работа с ДДС"
        element(byXpath("//*[text()='Работа с ДДС']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //жмем "добавить"
        //element(byCssSelector("div#simple-tabpanel-hotlines button")).click()
        element(byXpath("//*[text()='Выбрать ДДС']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем ДДС-02 г.Черкесск
        element(byXpath("//*[text()='ДДС ЭОС']/ancestor::div[@id='panel1a-header']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='ДДС-02 г.Черкесск']/ancestor::div/div/label//input"))
            .should(exist, ofSeconds(waitTime))
            .click()
        val descriptionFunction = "Назначение ДДС для проверки статусов $dateTime"
        val ddsCardSelector = "//*[text()='Назначенные службы']/ancestor::div[@role='tabpanel']//form[@novalidate]//*[text()='%s']"
        enterTextInMDtextboxByName("Описание назначения", descriptionFunction, waitTime)
        element(byXpath("//*[text()='Назначить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath(ddsCardSelector.format(descriptionFunction)))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='Назначенные службы']/ancestor::div[@role='tabpanel']//form[@novalidate]//*[text()='$descriptionFunction']"))
            .should(exist, ofSeconds(waitTime))
        element(byText("ДДС-02 г.Черкесск"))
            .should(exist, ofSeconds(waitTime))

        logoffTool()
        logonDds()

        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tableColumnCheckbox("Описание", true, waitTime)
        //Находим созданную КП в КИАП ДДС
        element(byText("AutoTest PMI 0150 $dateTime")).should(exist, ofSeconds(waitTime)).click()
        //устанавливаем статус "Реагирование"
        checkICToolIsStatus(`В обработке`, waitTime)
        updateICToolStatus(status, waitTime)
        checkICToolIsStatus(status, waitTime)
        logoffTool()
        //Возвращаемся в КИАП и проверяем статус родительской карточки
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tableColumnCheckbox("Описание", true, waitTime)
        //Находим созданную КП
        element(byText("AutoTest PMI 0150 $dateTime")).should(exist, ofSeconds(waitTime)).click()
        //проверяем статус родительской карточки
        if (status != Реагирование){
            checkICToolIsStatus(Завершена, waitTime)
        } else {
            checkICToolIsStatus(Реагирование, waitTime)
        }
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0160 Назначение сил и средств в КП`() {
        //A.3.18 Назначение сил и средств ДДС
        val teamStatusList: List<String> = listOf("Уведомлена", "Отправлена", "Прибыла", "Ликвидировала")
        var teamStatusButtonColor = ""
        var nowTime: LocalDateTime = LocalDateTime.now()
        var teamChangeStatusTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd.MM.yyyy"))
        var teamChangeStatusTimePlus = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd.MM.yyyy"))
        var teamChangeStatusTimeMinus = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd.MM.yyyy"))
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        element(byCssSelector("table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //открываем фильтр "Типы происшествий"
        setFilterByEnum(Типы_происшествий, "П Повседневные", waitTime)
        //Дожидаемся применения фильтра
        Thread.sleep(500)
        element(byXpath("//*[text()='Типы происшествий']//ancestor::button//*[@name='close']//ancestor::button[1]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Выбираем случайную КП
        elements(byXpath("//table/tbody/tr"))
            .random()
            .scrollIntoView("{block: \"center\"}")
            .click()
        //Переходим в силы и средства
        element(byXpath("//*[text()='Силы и средства']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[text()='Силы и средства']/text()/ancestor::button")).click()
        //Определяемся куда кликать для добавления в зависимости от того, добавлены ли уже силы и средства
        //больше не надо определять назначена ли бригада, просто тыкаем по имени кнопки
        element(byXpath("//*[text()='Добавить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //заполняем поля
        val team = ('A'..'Z').random()
        element(byXpath("//label[text()='Единица реагирования']/following-sibling::div/input"))
            .sendKeys("Team$team")
        val tel = (100000001..999999999).random()
        element(byXpath("//label[text()='Телефон']/following-sibling::div/input"))
            .sendKeys("+79$tel")
        val teamS = (1..100).random()
        element(byXpath("//label[text()='Численность ед.реагирования']/following-sibling::div/input"))
            .sendKeys("$teamS")
        val teamST = (0..10).random()
        element(byXpath("//label[text()='Количество техники']/following-sibling::div/input"))
            .sendKeys("$teamST")
        element(byXpath("//form//label[text()='Примечание']/following-sibling::div/textarea[@name='description']"))
            .sendKeys("Бригада-Team$team, в количестве $teamS человек, выехала на место происшествия, используя $teamST единиц(ы) техники")
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //запоминаем текущее время, что бы проверить его отображение под кнопкой статуса
        teamChangeStatusTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss, dd.MM.yyyy"))
        //проверяем что бригада добавилась
        element(byText("Team$team"))
            .should(exist, ofSeconds(longWait))
            .shouldBe(visible, ofSeconds(waitTime))
        //раскрываем баян
        element(byXpath("//*[text()='Team$team']/ancestor::div[@role='button' and @id='panel1a-header' and @aria-expanded='false']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Team$team']/ancestor::div[@role='button' and @id='panel1a-header' and @aria-expanded='false']"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='Team$team']/ancestor::div[@role='button' and @id='panel1a-header' and @aria-expanded='true']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //проверяем отображение атрибутов заданных при создании
        element(byXpath("//label[text()='Телефон']/following-sibling::div/input[@value='+79$tel']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//label[text()='Численность ед.реагирования']/following-sibling::div/input[@value='$teamS']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//label[text()='Количество техники']/following-sibling::div/input[@value='$teamST']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//form//label[text()='Примечание']/following-sibling::div/textarea[@name='description' and text()='Бригада-Team$team, в количестве $teamS человек, выехала на место происшествия, используя $teamST единиц(ы) техники']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //передвигаем бригаду по статусам
        teamStatusButtonColor = element(byXpath("//*[text()='Назначена']/text()/ancestor::button")).getCssValue("background-color")
        teamStatusList.forEach { teamStatus ->
            //переводим в новый статус
            element(byXpath("//*[text()='Team$team']/ancestor::div[@role='button' and @id='panel1a-header' and @aria-expanded]//*[text()='$teamStatus']/text()/ancestor::button"))
                .click()
            Thread.sleep(500)
            //подтверждаем перевод
            element(byXpath("//div[@role='presentation']//*[text()='Вы действительно желаете изменить статус?']/ancestor::div[@role='dialog']//*[text()='Да']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //запоминаем время
            nowTime = LocalDateTime.now()
            teamChangeStatusTime = nowTime.format(DateTimeFormatter.ofPattern("HH:mm:ss, dd.MM.yyyy"))
            teamChangeStatusTimeMinus = nowTime.minusSeconds(1).format(DateTimeFormatter.ofPattern("HH:mm:ss, dd.MM.yyyy"))
            teamChangeStatusTimePlus = nowTime.plusSeconds(1).format(DateTimeFormatter.ofPattern("HH:mm:ss, dd.MM.yyyy"))
            checkAlert(snackbarSuccess, "OK", true, longWait)
            var count = 0
            while ((element(byXpath("//*[text()='$teamStatus']/text()/ancestor::button")).getCssValue("background-color") != teamStatusButtonColor) && count < 100){
                Thread.sleep(100)
                count += 1
            }
            val timeLocator = "//*[text()='Team%s']/ancestor::div[@role='button' and @id='panel1a-header' and @aria-expanded]//*[text()='%s']/text()/ancestor::button/following-sibling::*[text()%s]"
            element(byXpath(timeLocator.format(team, teamStatus, "")))
                .should(exist, ofSeconds(longWait))
                .shouldBe(visible, ofSeconds(waitTime))
            Assertions.assertTrue(
                element(byXpath(timeLocator.format(team, teamStatus, "='$teamChangeStatusTime'"))).exists()
                    ||
                    element(byXpath(timeLocator.format(team, teamStatus, "='$teamChangeStatusTimePlus'"))).exists()
                    ||
                    element(byXpath(timeLocator.format(team, teamStatus, "='$teamChangeStatusTimeMinus'"))).exists()
            )
            Assertions.assertTrue(
                element(byXpath(timeLocator.format(team, teamStatus, "='$teamChangeStatusTime'"))).isDisplayed
                    ||
                    element(byXpath(timeLocator.format(team, teamStatus, "='$teamChangeStatusTimePlus'"))).isDisplayed
                    ||
                    element(byXpath(timeLocator.format(team, teamStatus, "='$teamChangeStatusTimeMinus'"))).isDisplayed
            )
//            element(byXpath("//*[text()='Team$team']/ancestor::div[@role='button' and @id='panel1a-header' and @aria-expanded]//*[text()='$teamStatus']/text()/ancestor::button/following-sibling::*[text()='$teamChangeStatusTime']"))
//                .should(exist, ofSeconds(longWait))
//                .shouldBe(visible, ofSeconds(waitTime)) либо так, либо конструкция выше. минус в том что конструкция выше не включает ожидание, но попробуем выполнить его в while
            Assertions.assertTrue(element(byXpath("//*[text()='$teamStatus']/text()/ancestor::button")).getCssValue("background-color") == teamStatusButtonColor)
        }
        element(byXpath("//*[text()='Team$team']/ancestor::div[@role='button' and @id='panel1a-header' and @aria-expanded]//*[text()='Отменена']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='presentation']//*[text()='Вы действительно желаете отменить назначение?']/ancestor::div[@role='dialog']//*[text()='Да']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        checkAlert(snackbarWarning, "Данный статус не может быть присвоен", true, longWait)


        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0112 Назначение КП из 112 в КИАП`() {
        //A.3.19 Убедиться на стороне Системы-112 в наличии возможности назначать   карточку на ЕЦОР  (КИАП) из Системы-112
        //A.3.20 Прием карточки из Системы-112
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logon112()
        switchTo().window(0)
        //Создаем новое обращение
        //пытаемся убрать дурацкую шторку со статусами
        element(byXpath("//span[text()='Cостояние рабочего места']/parent::div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//span[text()='Нет входящего обращения']/parent::div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//span[text()='Создать обращение']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем парамерты обращения
        element(byCssSelector("input#city"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("Краснодар", Keys.DOWN, Keys.ENTER)
        element(byCssSelector("input#district"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("Краснодарский Край", Keys.DOWN, Keys.ENTER)
        element(byCssSelector("input#street"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("Красная")
        element(byCssSelector("input#house"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("1")
        element(byCssSelector("input#flat"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("1")
        element(byCssSelector("input#lastName"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("AutoTest A.3.19 A.3.20")
        element(byCssSelector("input#firstName"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("AutoTest $date")
        element(byCssSelector("textarea#details"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .sendKeys("AutoTest 112 $dateTime")
        //Выбираем тип происшествия
        element(byCssSelector("span#select2-cmbIncType-container"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//span[text()='AutoTest']/.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Создаем происшествие
        element(byCssSelector("button#btnNewCard"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Сохраняем
        //element(byCssSelector("button#btnSave")).should(exist, ofSeconds(10))
        //Назначаем на test КИАП
        //element(byXpath("//*[text()='KИАП-test']/ancestor::table//select")).selectOptionByValue("6")
        element(byXpath("//*[text()='KИАП-test']/ancestor::table//select"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .selectOptionContainingText("КИАП Черкесского ГО (33302)")
        element(byXpath("//strong[text()='KИАП-test']/../..//span[text()=' Назначить']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Завершаем обработку
        element(byXpath("//span[text()='Завершить обработку']/.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Вернуться к списку
        element(byCssSelector("button#btnBack>span"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()

        logoffTool()

        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        menuNavigation(Incidents.IncidentsList, waitTime)
        //Сбрасываем фильтры
        cleanFilterByEnum(listOf(), waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tableColumnCheckbox("Описание", true, waitTime)
        //Переходим в созданное ранее в 112 происшествие
        element(byText("AutoTest 112 $dateTime")).click()
        element(byXpath("//h3[text()='Описание происшествия']/../following-sibling::div//p"))
            .shouldHave(text("AutoTest 112 $dateTime"), ofSeconds(waitTime))
        element(byXpath("(//div[@id='calls']//div[@id='panel1a-header'])[1]//strong[text()='Источник/Оператор:']/ancestor::div[1 and text()='Система-112 стенд']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        checkICToolIsStatus(`В обработке`, waitTime)
        logoffTool()

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0180 Просмотр паспортов опасных объектов`() {
        //A.3.21 Просмотр паспортов опасных объектов
        //Из различных вариантов проверок скачивания+целостности скачивания было нагуглено, что самое оптимальное это метод .download()
        //который упадет если не сможет скачать файл
        Configuration.downloadsFolder = "/home/isizov/IdeaProjects/testing-e2e/build/download_PMI_0180"
        val companiesList = mutableListOf<String>()
        logonTool(false)
        menuNavigation(Dictionaries.Companies, waitTime)
        tableStringsOnPage(50, waitTime)
        //кликаем по иконке справочников в боковом меню
        //ждем загрузки
        element(byCssSelector("tbody>tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //ищем столбец "файлы"
        tableColumnCheckbox("Наименование;Файлы", true, waitTime)
        element(byXpath("//*[text()='Файлы']/ancestor::thead"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//*[text()='Наименование']/ancestor::thead"))
            .should(exist, ofSeconds(waitTime))
        val columnOfFile = tableNumberOfColumn("Файлы", waitTime)
        val columnOfName = tableNumberOfColumn("Наименование", waitTime)
        elements(byXpath("//table/tbody/tr/td[$columnOfFile]//*[@name='clip']/ancestor::tr/td[$columnOfName]//text()/..")).forEach { companie ->
            companiesList.add(companie.ownText)
        }
        companiesList.forEach { companieName ->
            element(byXpath("//table/tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//*[text()='$companieName']/text()/ancestor::td"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byCssSelector("div#card"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//h3[text()='Паспорт объекта, файлы']/parent::div[@id='files']//*[@aria-label and not(@aria-label='Действия над файлом')]/a"))
                .scrollIntoView(true)
            elements(byXpath("//h3[text()='Паспорт объекта, файлы']/parent::div[@id='files']//*[@aria-label and not(@aria-label='Действия над файлом')]/a"))
                .forEach { oneFile ->
                    oneFile.download()
                }
            back()
        }
        //Удаляем нафиг все что скачали
        FileUtils.deleteDirectory(File("/home/isizov/IdeaProjects/testing-e2e/build/download_PMI_0180"))
        logoffTool()
//Оставлю пока как пример если все-таки надумаю просматривать файлы
//                    val fileExtension = subFileElement.getAttribute("aria-label")?.substringAfterLast('.')?.lowercase()
//                    if (listOf("docx", "pdf").contains(fileExtension)){
//                        subFileElement
//                            .click()
//                    } else if (listOf("svg", "png", "jpeg", "jpg").contains(fileExtension)){
//                        subFileElement
//                            .click()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0220 Проверка открытия карточек справочных сущностей`() {
        //A.3.28 Проверка централизованного хранения и управления структурированной справочной информации
        logonTool(false)

//        for (i in 1..10){
        Dictionaries.values().forEach {     it ->
            //кликаем по иконке справочников
            menuNavigation(it , waitTime)
            //если в справочнике не пусто, то переходим в первую найденную строку и проверяем там наличие блока описания и заголовка "общие данные" в нем
            //ждем загрузки таблицы
            element(byCssSelector("main table>tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            if (it != Dictionaries.IncidentTypes){
                element(byXpath("//table/tbody/tr"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                // отказался от изящной конструкции выше, т.к. придется встаялять победу над тем что подвал таблицы перекрывает таблицу, оставил как есть
                element(byXpath("//table/tbody/tr[1]/td[1]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            } else if (it == Dictionaries.IncidentTypes){
                element(byXpath("//table/tbody/tr"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            }
            element(byXpath("//*[text()='Общие данные']/ancestor::div[1]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            shrinkCheckTool()
        //Thread.sleep(5000)
        }
    logoffTool()
    }

    @DataProvider(name = "Справочники")
    open fun Справочники(): Any {
        return Dictionaries.values().map { arrayOf(it) }.toTypedArray()

//        return arrayOf(Dictionaries.Positions)
//        return arrayOf(Dictionaries.VideoCameras)
//        return arrayOf(Dictionaries.Sensors)
//        return arrayOf(Dictionaries.Hotlines)
//        return arrayOf(Dictionaries.Officials)
//        return arrayOf(Dictionaries.Labels)
//        return arrayOf(Dictionaries.Municipalities)
//        return arrayOf(Dictionaries.Companies)
//        return arrayOf(Dictionaries.HotlineAssets)
//        return arrayOf(Dictionaries.IncidentTypes)

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, dataProvider = "Справочники", groups = ["ПМИ", "ALL"])
    fun `PMI 0230 Проверка поиска справочных данных`(subMenu: SubmenuInterface) {
        //A.3.29 Проверка поиска справочных данных
        logonTool(false)
        menuNavigation(subMenu, waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //отчищаем фильтры
        if (subMenu != Dictionaries.Positions) {
            cleanFilterByEnum(listOf(), waitTime)
        }
        //добавляем все доступные колонки в таблицу
        tableColumnCheckbox("", true, waitTime)
        //получаем счетчик строк в левом нижнем углу страницы, в виде числа
        val allRecordCountUse = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
            .ownText
            .toString()
            .split("\n")[1]
            .split(" ")[0]
            .toInt()
        //ситаем количество слолбцов, при том что последний нам не пригодится
        val comumnCount = elements(byXpath("//table/thead/tr/th")).size
        element(byXpath("//*[@name='search']/following-sibling::input[@placeholder]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //читаем что есть в подсказке
        val searchHintList = element(byCssSelector("input[placeholder]")).getAttribute("placeholder")!!.split(", ")
        //проходимся по заголовкам столбцов, сверяя их с каждой позицией подсказки
        var searchValue = ""
        var fullSearchValue = ""
        var firstColumnIsButton = 0
        var breakReadColumnName = 0
        for (col in 1 until comumnCount) {
                element(byXpath("//table/thead/tr/th[$col]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //если столбец, кнопка без текста, то жмем её и переходим к другим столбцам
                // (по идее это только иерархический столбец)
                if ((elements(byXpath("//table/thead/tr/th[$col]//text()/..")).size == 0)
                    && (elements(byXpath("//table/thead/tr/th[$col]//button")).size == 1)){
                    element(byXpath("//table/thead/tr/th[$col]//button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byCssSelector("thead>tr>th svg[name='arrowDown']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    firstColumnIsButton = 1
                }
                //проверяем существует ли заголовок столбца, и если существует, то:
                else {
                    val columnName = element(byXpath("//table/thead/tr/th[$col]//text()/..")).ownText.toString()
                    for (hint in 0 until searchHintList!!.size){
                        //если заголовок столбца совпал с подсказкой, то вбиваем значение этого столбца из каждой строки в список из которого выберем случайное значение для поиска, ждем что нижний счетчик строк будет строго меньше исходного
                        if (columnName.contains(searchHintList[hint])
                            || ((columnName == "Телефонный код") && (searchHintList[hint] == "Тел.код"))
                            || ((columnName == "Метка") && (searchHintList[hint] == "Имя метки"))
                            || ((columnName == "№") && (searchHintList[hint] == "Номер пункта"))
                        ) {
                            //искомое значение определяем случайно, среди имеющихся но с типами происшествий и откидыванием пустых значений других справочников придется возится
                            if (subMenu != Dictionaries.IncidentTypes) {
                                if (elements(byXpath("//table/tbody/tr/td[$col]//text()/parent::*[not(text()='  ')]")).isNotEmpty()) {
                                    do {
                                        searchValue = elements(byXpath("//table/tbody/tr/td[$col]//text()/parent::*[not(text()='  ')]"))
                                            .random().ownText
                                    } while (searchValue.length < 3)
                                } else {
                                    tableStringsOnPage(500, waitTime)
                                    do {
                                        searchValue = elements(byXpath("//table/tbody/tr/td[$col]//text()/parent::*[not(text()='  ')]"))
                                            .random().ownText
                                    } while (searchValue.length < 3)
                                    tableStringsOnPage(20, waitTime)
                                }
                                fullSearchValue = searchValue
                                //проверяем не номер ли это телефона и видоизменяем запись , к.т. в формате +Х(ХХХ)ХХХ-ХХ-ХХ в поисковой строке не вернет результатов, только +ХХХХХХХХХХ
                                //аналогично с ФИО
                                val ioRegex = Regex("[а-яА-Яa-zA-Z]{2,}(\\s{1}[а-яА-Яa-zA-Z]{1}[.]{1}){1,}")
                                val telRegex = Regex("[+7(]{1}[0-9]{3}[)]{1}[0-9]{3}[-]{1}[0-9]{2}[-]{1}[0-9]{2}")
                                val workTelRegex = Regex("[0-9]{1}[-][0-9]{5}[-][0-9]{3}[-][0-9]{3}")
                                    if (telRegex.containsMatchIn(searchValue)
                                        || workTelRegex.containsMatchIn(searchValue)
                                    ) {
                                        searchValue = searchValue.filter { it.isDigit() }
                                    } else if (ioRegex.containsMatchIn(searchValue)) {
                                        searchValue = searchValue.split(" ")[0]
                                    }
                            } else if (subMenu == Dictionaries.IncidentTypes) { //Отдельно обрабатываем справочник типов происшествий
                                searchValue = elements(byXpath("//table/tbody/tr/td[1][not(.//button)]/ancestor::tr/td[$col]//text()/.."))
                                    .random()
                                    .ownText
                                fullSearchValue = searchValue
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
                            val nowRecordCountUse = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
                                    .ownText
                                    .toString()
                                    .split("\n")[1]
                                    .split(" ")[0]
                                    .toInt()
                            Assertions.assertTrue(allRecordCountUse > nowRecordCountUse)
                            element(byXpath("//table/tbody/tr/td[${col - firstColumnIsButton}]//text()/parent::*[text()='$fullSearchValue']"))
                                .should(exist, ofSeconds(waitTime))
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
//        }
        logoffTool()
    }



    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0241 Проверка наличия справочников с иерархической системой классификации`(){
        //иерархических справосников стало больше, проверяем все
        logonTool(false)
        for (dicts in 1..10) {
            //кликаем по иконке справочников
            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul")).click()
            //переходим в каждый справочник
            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div/ul[$dicts]")).click()
            //ждем загрузки таблицы
            element(byCssSelector("main table>tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            if (elements(byXpath("//table/thead/tr[1]/th[1]//button")).size == 1
                && elements(byXpath("//table/thead/tr[1]/th[1]//*[text()]")).size ==0
            ){
                val stringCount = elements(byXpath("//table/tbody/tr")).size
                element(byXpath("//table/thead/tr[1]/th[1]//button")).click()
                element(byXpath("//table/thead/tr[1]/th[1]//*[@name='arrowDown']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                val  newStringCount = elements(byXpath("//table/tbody/tr")).size
                Assertions.assertTrue(stringCount < newStringCount)

            }
        }
        logoffTool()
    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0250 Проверка присвоения и удаления меток в карточке организации`(){
        //A.3.31 Проверка задания меток для указания признаков объектов
        logonTool(false)
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation(Dictionaries.Companies, waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки и переходим в случайную организацию
        val organizationTableStringCount = elements(byXpath("//table/tbody/tr")).size
        val rndOrganization = (1..organizationTableStringCount).random()
//        if (rndOrganization == organizationTableStringCount){
//            element(byCssSelector("body")).sendKeys(Keys.END)
//        } else { element(byXpath("//table/tbody/tr[${rndOrganization + 1}]"))
//            .scrollIntoView(true)
//        }
        element(byXpath("//table/tbody/tr[$rndOrganization]"))
            .scrollIntoView(false)
        element(byXpath("//table/tbody/tr[$rndOrganization]"))
            .click()
        //ждем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //запомнинаем какую организацию редактируем
        val organizationName = element(byXpath("//h1"))
            .ownText
        //считаем существующие метки на случай когда они есть и когда их нет
        var amountLabels = elements(byXpath("//div[@id='labels']//span[@aria-label]//span[text()]")).size
        val beforeLabelsList = mutableListOf<String>()
        val afterLabelsList = mutableListOf<String>()
        if (amountLabels > 0){
            for (i in 1..amountLabels){
                beforeLabelsList.add(element(byXpath("//div[@id='labels']//span[@aria-label][$i]//span[text()]")).ownText)
            }
        }
        //жмем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем кнопки "Сохранить", как показатель загрузки страницы
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //скролим до Указать на карте
        element(byXpath("//*[text()='Указать на карте']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .scrollIntoView(false)
        //вставляем новую метку
        inputRandomNew("labelsId-textfield", true, waitTime)
        //пересчитываем метки
        amountLabels = elements(byXpath("//label[text()='Метки']/..//span[@aria-label]//span[text()]")).size
        //вносим каждую в список
        for (i in 1..amountLabels){
            afterLabelsList.add(element(byXpath("//label[text()='Метки']/..//span[@aria-label][$i]//span[text()]")).ownText)
        }
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем загрузки карточки организации
        //ждем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //убеждаемся, что не затерли ни одной метки, а именно добавили.
        Assertions.assertTrue(afterLabelsList.containsAll(beforeLabelsList))
        Assertions.assertTrue(afterLabelsList.size > beforeLabelsList.size)
        //убеждаемся что все метки есть на карточке
        afterLabelsList.forEach { label ->
            element(byXpath("//div[@id='labels']//span[@aria-label]//span[text()='$label']"))
                .should(exist, ofSeconds(waitTime))
        }
        logoffTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //теперь удалим эту (эти) метку(и)
        logonTool(false)
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation(Dictionaries.Companies, waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableColumnCheckbox("Наименование", true, waitTime)
        tableSearch(organizationName, waitTime)
        //переходим в ту же организацию
        element(byXpath("//table/tbody//*[text()='$organizationName']/text()/ancestor::td"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val newLabelsList = afterLabelsList.minus(beforeLabelsList.toSet())
        afterLabelsList.forEach { label ->
            element(byXpath("//div[@id='labels']//span[@aria-label]//span[text()='$label']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        //жмем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //удаляем метку
        element(byXpath("//*[text()='Указать на карте']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .scrollIntoView(false)
        newLabelsList.forEach {
            element(byXpath("//label[text()='Метки']/..//span[text()='$it']/ancestor::span[@aria-label]//*[name()='svg']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
        element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем Редактировать
        element(byXpath("//*[text()='Изменить']/ancestor::*[@aria-label='Редактировать']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //убеждаемся, что нашей метки нет
        newLabelsList.forEach {
            element(byXpath("//div[@id='labels']//span[@aria-label]//span[text()='$it']"))
                .shouldNot(exist, ofSeconds(waitTime))
                .shouldNotBe(visible, ofSeconds(waitTime))
        }
        beforeLabelsList.forEach {
            element(byXpath("//div[@id='labels']//span[@aria-label]//span[text()='$it']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0260 Проверка использования ассоциативных связей-ссылок между объектами справочников`(){
        //A.3.32 Проверка использования ассоциативных связей-ссылок между объектами справочников
        logonTool(false)
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation(Dictionaries.Hotlines, waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Собираем все ФИО, что бы не проверять организацию у которой руководитель не указан
        tableColumnCheckbox("Руководитель;Наименование", true, waitTime)
        val officialIdColumn = tableNumberOfColumn("Руководитель", waitTime)
        elements(byXpath("//table/tbody/tr/td[$officialIdColumn]//text()/ancestor::td"))
            .random()
            .scrollIntoView(false)
            .click()

        //переходим в дежурную службу
        element(byCssSelector("h1"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //запоминаем ФИО руководителя
        element(byXpath("//*[text()='Руководитель']/following-sibling::*//*[text()]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[text()='Руководитель']/following-sibling::*//*[text()]//*[@aria-label='Перейти']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val officialFIO = element(byXpath("//*[text()='Руководитель']/following-sibling::*//*[text()]"))
            .ownText
        //кликаем на стрелку перехода к руководителю
        element(byXpath("//*[text()='Руководитель']/following-sibling::*//*[text()]//*[@aria-label='Перейти']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//main//h1[text()='$officialFIO']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//span[@aria-label='Редактировать']//*[text()='Изменить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//label[text()='Фамилия']/..//input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Assertions.assertTrue(officialFIO!!.contains(
            element(byXpath("//label[text()='Фамилия']/..//input"))
                .getAttribute("value").toString().trim(), false))
        element(byXpath("//label[text()='Имя']/..//input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Assertions.assertTrue(officialFIO.contains(
            element(byXpath("//label[text()='Имя']/..//input"))
                .getAttribute("value").toString(), false))
        element(byXpath("//label[text()='Отчество']/..//input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        if (element(byXpath("//label[text()='Отчество']/..//input")).getAttribute("value").toString().isNotEmpty()) {
            Assertions.assertTrue(officialFIO.contains(
                element(byXpath("//label[text()='Отчество']/..//input"))
                    .getAttribute("value").toString(), false))
        }
        element(byXpath("//form//*[text()='Отмена']//text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//main//h1[text()='$officialFIO']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0270 Проверка возможности создания, удаления записей, просмотра детальной истории в справочнике должностных лиц`(){
        //A.3.33 Проверка возможности создания записей в справочниках
        //A.3.34 Проверка возможности удаления записей в справочниках
        //A.3.35 Просмотр детальной истории записи/источников данных записи
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool(false)
        //Технический хардкодный счетчик на случай когда надо посоздавать должностных лиц не удаляя их
        val techCount = 1
        //Удалять ли предшествующие записи? флаг для локального использования теста для генерации метаданных
        val deleteOfficials = true
        var telR = (1000000..9999999).random()
        var telM = (1000000..9999999).random()
        var officialName = generateFirstNameI()
        var officialLastName = generateLastNameF()
        val hotLineList = mutableListOf<String>()
        val organizationList = mutableListOf<String>()
        if (deleteOfficials){
            // TODO: доделать удаление по должности или еще какому признаку
            menuNavigation(Dictionaries.Officials, waitTime)
            setFilterByEnum(Пользователь, "Нет", waitTime)
            tableColumnCheckbox("ФИО", true, waitTime)
            tableSearch("N0270AutoTest", waitTime)
            val fioColumnNubber = tableNumberOfColumn("ФИО", waitTime)
            Thread.sleep(1000)
            element(byXpath("//table/tbody/tr//*[text()='Нет данных' or contains(text(),'AutoTest')]/ancestor::tr"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            while (!element(byXpath("//table/tbody/tr//*[text()='Нет данных']")).exists()){
                //запоминаем что удаляем
                val removedPersonFIO = element(byXpath("//table/tbody/tr[1]/td[$fioColumnNubber]//text()/..")).ownText
                //открываем три точки
                element(byXpath("//table/tbody/tr[1]/td[last()]//button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //удаляем
                element(byXpath("//*[text()='Удалить']/text()/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //подтверждаем удаление
                element(byXpath("//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//table/tbody/tr[1]/td[$fioColumnNubber]//text()/parent::*[text()='$removedPersonFIO']"))
                    .shouldNot(exist, ofSeconds(waitTime))
                Thread.sleep(500)
                element(byXpath("//table/tbody/tr//*[(text()='Нет данных' or contains(text(),'AutoTest')) and not(contains(text(),'$removedPersonFIO'))]/ancestor::tr"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
        }
        //идем в справочник ДС что бы достать оттуда ДС и их организации, что бы лицо создавать с этими параметрами
        menuNavigation(Dictionaries.Hotlines, waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //переключаемся на 500 записей на странице
        tableStringsOnPage(500, waitTime)
        //выставляем отображение нужных столбцов
        tableColumnCheckbox("Наименование;Организация", true, waitTime)
        element(byXpath("//table/thead/tr/th//*[text()='Наименование']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //ждем появления выставленных столбцов (последнего из них)
        element(byXpath("//table/thead/tr/th//*[text()='Организация']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Выбираем случайную строку и из нее берем организацию и ДС
        val hotlineColumnNumber: Int = tableNumberOfColumn("Наименование", waitTime)
        val organizationColumnNumber: Int = tableNumberOfColumn("Организация", waitTime)
        val rndTableStringNumder = (1..elements(byXpath("//table/tbody/tr")).size).random()
        val organizationForOfficial = element(byXpath("//table/tbody/tr[$rndTableStringNumder]/td[$organizationColumnNumber]//text()/..")).ownText
        val hotLineForOfficial = element(byXpath("//table/tbody/tr[$rndTableStringNumder]/td[$hotlineColumnNumber]//text()/..")).ownText
        //на всякий случай проверяем, что взяли из одной строки
        element(byXpath("//table/tbody/tr[*[text()='$hotLineForOfficial'] and *[text()='$organizationForOfficial']]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Переходим в справочник должностных лиц
        menuNavigation(Dictionaries.Officials, waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        repeat(techCount) {
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            element(byXpath("//*[text()='Добавить новое']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byCssSelector("form"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            val addAtributeSelector = "//label[text()='%s']/following-sibling::div/input"
            if (techCount != 1){
                telR = (1000000..9999999).random()
                telM = (1000000..9999999).random()
                officialName = generateFirstNameI()
                officialLastName = generateLastNameF()
            }
            element(byXpath(addAtributeSelector.format("Фамилия"))).sendKeys(officialLastName)
            element(byXpath(addAtributeSelector.format("Имя"))).sendKeys(officialName)
            element(byXpath(addAtributeSelector.format("Отчество"))).sendKeys("N0270AutoTest")
            element(byXpath(addAtributeSelector.format("Рабочий телефон"))).sendKeys("+7918$telR")
//        element(byXpath(addAtributeSelector.format("Должность"))).sendKeys("Robot")
            element(byXpath(addAtributeSelector.format("E-mail"))).sendKeys("${officialName.lowercase()}@test.com")
            element(byXpath(addAtributeSelector.format("Мобильный телефон"))).sendKeys("+7961$telM")
//            //Выбираем организацию
//            element(byXpath("//input[@name='companyId']")).click()
//            val randomAtridute = (0 until hotLineList.size).random()
//            element(byXpath("//input[@name='companyId']")).sendKeys(organizationList[randomAtridute])
//            element(byXpath("//input[@name='companyId']")).sendKeys(Keys.DOWN, Keys.ENTER)
//            val companyId = element(byXpath("//input[@name='companyId']")).getAttribute("value")
//            Assertions.assertTrue(organizationList[randomAtridute] == companyId)
//            //Выбираем службу
//            element(byXpath("//input[@name='hotlineId']"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
//            element(byXpath("//input[@name='hotlineId']")).sendKeys(hotLineList[randomAtridute])
//            element(byXpath("//input[@name='hotlineId']")).sendKeys(Keys.DOWN, Keys.ENTER)
//            val hotlineId = element(byXpath("//input[@name='hotlineId']")).getAttribute("value")
//            Assertions.assertTrue(hotLineList[randomAtridute] == hotlineId)
            shrinkCheckTool()
            //сохраняем
            element(byXpath("//*[text()='Добавить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//*[text()='Добавить']/text()/ancestor::button"))
                .shouldNot(exist, ofSeconds(waitTime))
                .shouldNotBe(visible, ofSeconds(waitTime))
//            element(byCssSelector("form"))
//                .shouldNot(exist, ofSeconds(waitTime))
//                .shouldNotBe(visible, ofSeconds(waitTime))
            element(byXpath("//table/tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
//            tableSearch("$officialLastName $officialName N0270AutoTest", waitTime)
//            element(byXpath("//table/tbody//*[text()='Нет данных']"))
//                .shouldNot(exist, ofSeconds(waitTime))
//            element(byXpath("//table/tbody/tr//*[text()='$officialLastName $officialName N0270AutoTest']/ancestor::tr"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
//

        }
        if (techCount ==1){
            //воспользуемся поиском
            tableSearch("$officialLastName $officialName N0270AutoTest", waitTime)
            //открываем три точки
            element(byXpath("//table/tbody/tr//*[text()='$officialLastName $officialName N0270AutoTest']/ancestor::tr/td[last()]//button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //открываем историю
            element(byXpath("//*[text()='История']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем загрузки
            element(byXpath("//div[@id='dict-title']//p[text()='История должностного лица']/a[text()='$officialLastName $officialName N0270AutoTest']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //смотрим историю развернув всё
            element(byXpath("//table/thead//*[@name='arrowRight']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//table/thead//*[@name='arrowRight']/ancestor::button"))
                .shouldNot(exist, ofSeconds(waitTime))
                .shouldNotBe(visible, ofSeconds(waitTime))
            element(byXpath("//table/thead//*[@name='arrowDown']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            val atributeSelector = "//table/tbody//li/label[text()='%s']/following-sibling::*[text()='%s']"
            //проверяем что в истории
//            element(byXpath(atributeSelector.format("Дежурная служба", hotlineId)))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//            element(byXpath(atributeSelector.format("Организация", companyId)))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
            //разворачиваем ФИО и пр.
            element(byXpath("//label[text()='ФИО']/preceding-sibling::div/div[text()='▶']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //проверяем то что развернули
            element(byXpath(atributeSelector.format("Фамилия", officialLastName)))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath(atributeSelector.format("Имя", officialName)))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath(atributeSelector.format("Отчество", "N0270AutoTest")))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath(atributeSelector.format("Электронная почта", "${officialName.lowercase()}@test.com")))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
//            element(byXpath(atributeSelector.format("Должность", "Robot")))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath(atributeSelector.format("Мобильный телефон", "+7961$telM")))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath(atributeSelector.format("Рабочий телефон", "+7918$telR")))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            logoffTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //а теперь удалим его
            logonTool(false)
            //переходим в нужный справочник
            menuNavigation(Dictionaries.Officials, waitTime)
            //ждем загрузки таблицы
            element(byCssSelector("main table>tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //воспользуемся поиском
            tableSearch("$officialLastName $officialName N0270AutoTest", waitTime)
            //открываем три точки
            element(byXpath("//table/tbody/tr//*[text()='$officialLastName $officialName N0270AutoTest']/ancestor::tr/td[last()]//button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //удаляем
            element(byXpath("//*[text()='Удалить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //подтверждаем удаление
            element(byXpath("//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//tbody/tr//*[text()='Нет данных']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            logoffTool()
        }
    }

}
