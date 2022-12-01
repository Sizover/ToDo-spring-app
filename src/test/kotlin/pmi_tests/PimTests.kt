package pmi_tests

//import kotlin.collections.EmptyMap.keys
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


class PimTests : BaseTest(){
    var date = ""
    var dateTime = ""


    //Время ожидания элементов при выполнении теста
    val waitTime: Long = 5
    val longWait: Long = 10

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
        logonTool()
        //кликаем по иконке справочников в боковом меню
        //Переходим в "Муниципальные образования"
        var subMenu = ""
        for (i in 1..5){
            when(i){
                1 -> {subMenu = "Муниципальные образования"}
                2 -> {subMenu = "Должностные лица"}
                3 -> {subMenu = "Дежурные службы"}
                4 -> {subMenu = "Видеокамеры"}
                5 -> {subMenu = "Датчики"}
            }
            menuNavigation("Справочники", subMenu, waitTime)
            //сравниваем количество записей в таблице, на больше или равно 5
            element(byXpath("//table/tbody/tr"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            while (elements(byXpath("//table/thead/tr/th//*[@name='arrowRight']/ancestor::button")).size > 0){
                element(byXpath("//table/thead/tr/th//*[@name='arrowRight']/ancestor::button"))
                    .click()
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
        logonTool()
        //кликаем по иконке справочников в боковом меню
        //Переходим в "Типы происшествий"
        menuNavigation("Справочники", "Типы происшествий", waitTime)
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
        logonTool()
        //кликаем по иконке справочников в боковом меню
        //Переходим в справочник "Организации"
        menuNavigation("Справочники","Организации",waitTime)
        //сравниваем колличество строк организаций, по условию больше или равно с 5
        element(byCssSelector("tbody>tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
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
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия","Список происшествий",waitTime)
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
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Архив происшетвий"
        menuNavigation("Происшествия","Архив происшествий",waitTime)
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
        aThreeHundredAndTenOne()
        element(byName("login")).click()
        element(byCssSelector("#input-error")).shouldHave(exactText("Неправильное имя пользователя или пароль."))
        aThreeHundredAndTenOne()
        element(byName("username")).sendKeys("test")
        element(byName("login")).click()
        element(byCssSelector("#input-error")).shouldHave(exactText("Неправильное имя пользователя или пароль."))
        aThreeHundredAndTenOne()
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
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
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
        val iI = (1..10).random()
        element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($iI)")).click()
        //Источник события - факс
        //element(byCssSelector("#calltype")).click()
        //element(byCssSelector("li[role='option'][data-value='cd73ccc0-e740-4c5d-98ec-28bbe9a13be0']")).click()
        //Номер телефона
        if (elements(byCssSelector("#phone")).size > 0){
            element(byCssSelector("#phone")).sendKeys("9189999999")
        }
        //ФИО
        if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
            && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
            element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname")
            element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
        }
        //адрес с тестированием транслитерации и клик по dadata
        addressInput("callAddress","vbhf5", waitTime)
//        element(byCssSelector("#callAddress")).click()
//        element(byCssSelector("#callAddress"))
//        element(byCssSelector("#callAddress")).sendKeys("vbhf5")
////        element(byCssSelector("div.react-dadata__suggestions div.react-dadata__suggestion.react-dadata__suggestion--current")).click()
//        element(byXpath("//body/div[@role='presentation']//*[contains(text(),' м)')]"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("#callAddress"))
//            .sendKeys(Keys.DOWN, Keys.ENTER)
        //заполняем дополнительную информацию
        //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0110 $dateTime"
        element(byCssSelector("div[role='textbox']>p")).click()
        element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest N 0110 $dateTime")
        //регистрируем обращение
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        //выбираем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test").sendKeys(Keys.DOWN, Keys.RETURN)
        //Создаем карточку
        element(byXpath("//span[text()='Сохранить карточку']/..")).click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus("В обработке", waitTime)
        //и что это именно так карточка которую мы только что создали
//        element(byCssSelector("div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-md-8 > div:nth-child(4)"))
//            .shouldHave(text("AutoTest N 0110 $dateTime"), ofSeconds(waitTime))
        element(byXpath("//div[text()='AutoTest N 0110 $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //загружаем файлы проверяя их прикрепление
        element(byCssSelector("input#upload-file"))
            .uploadFile(File("/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/AutoTest.webp"))
        element(byXpath("//div[@role='alert']//*[text()='Файл загружен']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//div[@role='alert']//*[@name='snackbarClose']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@id='files']//*[text()='AutoTest.webp']"))
            .should(exist, ofSeconds(waitTime))
        element(byCssSelector("input#upload-file"))
            .uploadFile(File("/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/Тестовый файл_.docx"))
        element(byXpath("//div[@role='alert']//*[text()='Файл загружен']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//div[@role='alert']//*[@name='snackbarClose']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@id='files']//*[text()='Тестовый файл_.docx']"))
            .should(exist, ofSeconds(waitTime))

        logoffTool()

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //`A312 Проверка прикрепления файла к происшествию`
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий" )
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        checkbox("Описание", true, waitTime)
//        //Открываем выпадающий список
//        element(byCssSelector("button[data-testid='Колонки-iconButton']"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        //Дожидаемся, что список появился
//        element(byCssSelector("fieldset[aria-label='Показать/скрыть колонки']>div>label"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']")).should(exist, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']")).click()        //ждем пока выделится чекбокс
//        element(byXpath("//span[text()='Описание']/../span/span/*[name()='svg']/*[name()='path']"))
//            .shouldHave(attribute("d", checkboxTrue), ofSeconds(waitTime))
//        element(byCssSelector("button[aria-label='Close']")).click()
        //Переходим в созданное ранее происшествие
        element(byText("AutoTest N 0110 $dateTime")).click()
//        element(byCssSelector("div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-md-8 > div:nth-child(4)"))
//            .shouldHave(text("AutoTest N 0110 $dateTime"), ofSeconds(waitTime))
        element(byXpath("//div[text()='AutoTest N 0110 $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Прикрепляем файл
        element(byCssSelector("input#upload-file")).uploadFile(File("/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/test.pdf"))
        element(byXpath("//div[@role='alert']//*[text()='Файл загружен']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//div[@role='alert']//*[@name='snackbarClose']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@id='files']//*[text()='test.pdf']"))
            .should(exist, ofSeconds(waitTime))
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0120 Проверка наличия опросника абонента (заявителя)`() {
        //A313 Проверка наличия опросника абонента (заявителя)
        //val tools = Tools()
        //логинимся
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        //кликаем по "создать обращение"
        element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
        //Проверяем форму подсказки по положению стрелки и части текста
        element(byCssSelector("button.MuiIconButton-sizeSmall > span > svg > path[d='M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z']"))
            .should(exist, ofSeconds(waitTime))
        element(byCssSelector("button.MuiIconButton-sizeSmall > span > svg > path[d='M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z']"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byCssSelector("div.MuiPaper-root.MuiPaper-elevation1.MuiPaper-rounded:nth-child(2)"))
            .should(visible, ofSeconds(waitTime))
        element(byCssSelector("div.MuiPaper-root.MuiPaper-elevation1.MuiPaper-rounded:nth-child(2)"))
            .shouldHave(text("Здравствуйте, оперативный дежурный (имя и фамилия), представьтесь, пожалуйста")
                , ofSeconds(waitTime))
        element(byText("Опросник")).click()
        element(byCssSelector("div.MuiPaper-root.MuiPaper-elevation1.MuiPaper-rounded:nth-child(2)"))
            .shouldNot(visible, ofSeconds(waitTime))
        element(byCssSelector("button.MuiIconButton-sizeSmall > span > svg > path[d='M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z']"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byCssSelector("button.MuiIconButton-sizeSmall > span > svg > path[d='M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z']"))
            .should(exist, ofSeconds(waitTime))
        element(byText("Опросник")).click()

        logoffTool()

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0130 Регистрация вызова (формирование карточки происшествия) с проверкой выполнения плана реагирования`() {
        //A.3.11 Регистрация вызова (формирование карточки происшествия)
        //А.3.14 Проверка формирования плана (алгоритма) реагирования по заданному событию (происшествию)
        //A.3.15 Проверка формирования действий оператора системы в рамках плана (алгоритма) реагирования с автоматизированным контролем выполнения
        //логинимся
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        //кликаем по "создать обращение"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
        //заполняем карточку
        //Источник события - выбираем случайно
        element(byCssSelector("div#calltype")).click()
        val iI = (1..10).random()
        element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($iI)")).click()
        //Источник события - факс
        //element(byCssSelector("#calltype")).click()
        //element(byCssSelector("li[role='option'][data-value='cd73ccc0-e740-4c5d-98ec-28bbe9a13be0']")).click()
        //Номер телефона
        if (elements(byCssSelector("#phone")).size > 0){
            element(byCssSelector("#phone")).sendKeys("9189999999")
        }
        //ФИО
        if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
            && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
            element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname")
            element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
        }
        //адрес с тестированием транслитерации и клик по dadata
        addressInput("callAddress","vbhf5", waitTime)
        //заполняем дополнительную информацию
        //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0130 $dateTime"
        element(byCssSelector("div[role='textbox']>p")).click()
        element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest N 0130 $dateTime")
        //регистрируем обращение
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        //выбираем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test").sendKeys(Keys.DOWN, Keys.ENTER)
        //Создаем карточку
        element(byCssSelector("div.MuiGrid-root.MuiGrid-item > button[type='submit']")).click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus("В обработке", waitTime)
        //и что это именно так карточка которую мы только что создали
//        element(byCssSelector("div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-md-8 > div:nth-child(4)"))
//            .shouldHave(text("AutoTest N 0130 $dateTime"), ofSeconds(waitTime))
        element(byXpath("//div[text()='AutoTest N 0130 $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Здесь проверим А.3.14 Проверка формирования плана (алгоритма) реагирования по заданному событию (происшествию)
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Переходим в "Реагирование"
        element(byXpath("//*[text()='Реагирование']/ancestor::button")).click()
//        element(byCssSelector("div[role='tablist'] > button:nth-child(2)")).should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("div[role='tablist'] > button:nth-child(2)")).click()
        //Раскрываем всю карту реагирования
        element(byXpath("//span[text()='Развернуть все']/..")).click()
        //Проверяем подпункты 1.0; 1.1; 1.2; 2.0; 2.1; 2.2.
        val headSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child(%d) > div > div#panel1a-header"
        val headChildSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child(%d) >div>div>div>div>div#panel1a-content>div>div>div:nth-child(%d)>form"
//        element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child(1) > div > div#panel1a-header"))
//            .shouldHave(text("Мероприятие 1.0"))
        element(byCssSelector(headSelector.format(1)))
            .shouldHave(text("Мероприятие 1.0"))
//        element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child(1) >div>div>div>div>div#panel1a-content>div>div>div:nth-child(1)>form"))
//            .shouldHave(text("Мероприятие 1.1"))
        element(byCssSelector(headChildSelector.format(1, 1)))
            .shouldHave(text("Мероприятие 1.1"))
            .shouldHave(text("Ответственный исполнитель: "))
//        element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child(1) >div>div>div>div>div#panel1a-content>div>div>div:nth-child(1)>form"))
//            .shouldHave(text("Ответственный исполнитель: "))
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
//        element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child(2) >div>div>div>div>div#panel1a-content>div>div>div:nth-child(13)>form"))
//            .shouldHave(text("Оператор AutoTest"))

        logoffTool()

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //`A315 Проверка формирования действий оператора системы в рамках плана (алгоритма) реагирования с автоматизированным контролем выполнения
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        checkbox("Описание", true, waitTime)
//        //Открываем выпадающий список
//        element(byCssSelector("button[data-testid='Колонки-iconButton']"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        //Дожидаемся, что список появился
//        element(byCssSelector("fieldset[aria-label='Показать/скрыть колонки']>div>label"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']")).should(exist, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']")).click()
//        //ждем пока выделится чекбокс
//        element(byXpath("//span[text()='Описание']/../span/span/*[name()='svg']/*[name()='path']"))
//            .shouldHave(attribute("d", checkboxTrue), ofSeconds(waitTime))
//        element(byCssSelector("button[aria-label='Close']")).click()
        //Переходим в созданное ранее происшествие
        element(byText("AutoTest N 0130 $dateTime")).click()
//        element(byCssSelector("div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-md-8 > div:nth-child(4)"))
//            .shouldHave(text("AutoTest N 0130 $dateTime"), ofSeconds(waitTime))
        element(byXpath("//div[text()='AutoTest N 0130 $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Переходим в "Реагирование"
        element(byXpath("//*[text()='Реагирование']/ancestor::button")).click()
        //Раскрываем всю карту реагирования
        element(byXpath("//span[text()='Развернуть все']/..")).click()
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
                val colorItemSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) >div>div>div>div>div#panel1a-content>div>div>div:nth-child($c)>form>div[style='background-color: rgb(%s); border-color: rgb(%s);']"
                val itemSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) >div>div>div>div>div#panel1a-content>div>div>div:nth-child($c)>form"
                val circleSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) > div > div#panel1a-header div.MuiBox-root > div:nth-child($c) > span[title^='Статус: %s'] rect[fill='%s']"
                val textFieldSelector = "div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) >div>div>div>div>div#panel1a-content>div>div>div:nth-child($c)>form p[data-empty-text='Нажмите здесь, чтобы вводить текст']"
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
                element(byCssSelector(circleSelector.format("Не выполнен", "#BA3113")))
//                element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) > div > div#panel1a-header div.MuiBox-root > div:nth-child($c) > span[title^='Статус: Не выполнен'] rect[fill='#16BA13']"))
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
                element(byXpath("//span[text()='Выполнить']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //Проверяем позеленение кружочка
                element(byCssSelector(circleSelector.format("Не выполнен", "#BA3113")))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byCssSelector(circleSelector.format("Выполнен", "#16BA13")))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //Проверяем цвета полей дочерних пунктов
                element(byCssSelector(colorItemSelector.format("247, 248, 251", "208, 214, 220")))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byCssSelector(colorItemSelector.format("232, 255, 224", "122, 249, 102")))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //Проверяем и закрываем зеленую "всплывашку"
                checkGreenAlert("Пункт выполнен", true, waitTime)
                //Thread.sleep(50000)
            }
        }

        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0140 Проверка фильтрации карточек происшествия`() {
        //A.3.16 Фильтрация карточек происшествия
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        elements(byXpath("//table/tbody/tr")).shouldHave(
            CollectionCondition.sizeGreaterThanOrEqual(1))
        //открываем фильтр "Типы происшествий"
        element(byXpath("//*[text()='Типы происшествий']/ancestor::button")).click()
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Выбираем "Консультации"
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .sendKeys("К.1.1.1 Консультации")
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .sendKeys(Keys.DOWN, Keys.ENTER)
        repeat(20){
            element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
                .sendKeys(Keys.BACK_SPACE)
        }
//        element(byCssSelector("ol label[title='К Консультации'] span[role='checkbox']")).click()
        //Выбираем "Ложные"
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .sendKeys("Л.1.1 Ложные")
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .sendKeys(Keys.DOWN, Keys.ENTER)
        repeat(12){
            element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
                .sendKeys(Keys.BACK_SPACE)
        }
//        element(byCssSelector("ol label[title='Л Ложные'] span[role='checkbox']")).click()
        //Кликаем "В пустоту"
//        element(byCssSelector("body > div:nth-child(9) > div:nth-child(1)")).click()
        //Применить
        element(byXpath("//*[text()='Применить']/ancestor::button")).click()
        //Дожидаемся применения фильтра
        Thread.sleep(500)
        element(byXpath("//span[text()='Типы происшествий']/button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("button[style='width: 250px; min-width: 250px; display: flex; justify-content: space-between;']"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        elements(byText("Ложные")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0))
//        elements(byText("Консультации")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0))
//        tools.checkbox("Группа", false, waitTime)
        checkbox("Подгруппа", true, waitTime)
        var targetColumn = numberOfColumn("Подгруппа", waitTime)
        Thread.sleep(500)
        var intA: Int = elements(byXpath("//table/tbody/tr")).size
        var intB: Int = elements(byXpath("//table/tbody/tr/td[$targetColumn][text()='Ложные']")).size
        var intC: Int = elements(byXpath("//table/tbody/tr/td[$targetColumn][text()='Консультации']")).size
        var intS: Int = intB + intC
        //println("$intA" + " A")
        //println("$intB" + " B")
        //println("$intC" + " C")
        //println("$intS" + " S")
        Assertions.assertTrue(intS == intA)
        //Очищаем фильтр "Тип происшествия"
        element(byXpath("//span[text()='Типы происшествий']/button")).click()
        /////////////////////////////////////////////////////////////////////////////////////////
        //Открываем фильтр "Статусы"
        checkbox("Статус", true, waitTime)
        element(byXpath("//span[text()='Статусы']/..")).click()
        element(byCssSelector("div[tabindex='-1'] div[role='combobox']")).should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime)).click()
        //Заполняем фильтр
        element(byCssSelector("input#incidentStatusId")).sendKeys("В обработке")
        element(byCssSelector("input#incidentStatusId")).sendKeys(Keys.DOWN)
        element(byCssSelector("input#incidentStatusId")).sendKeys(Keys.ENTER)
        element(byCssSelector("input#incidentStatusId")).sendKeys("Реагирование")
        element(byCssSelector("input#incidentStatusId")).sendKeys(Keys.DOWN)
        element(byCssSelector("input#incidentStatusId")).sendKeys(Keys.ENTER)
        element(byCssSelector("input#incidentStatusId")).click()
        //Применить
        element(byXpath("//span[text()='Применить']/..")).click()
        //Дожидаемся применения фильтров
        Thread.sleep(500)
        element(byXpath("//span[text()='Статусы']/button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("button[style='width: 130px; min-width: 130px; display: flex; justify-content: space-between;']"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        elements(byText("В обработке")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0))
//        elements(byText("Реагирование")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0))
        targetColumn = numberOfColumn("Статус", waitTime)
        intA = elements(byXpath("//table/tbody/tr")).size
        intB = elements(byXpath("//tbody/tr/td[$targetColumn]//*[text()='В обработке']")).size
        intC = elements(byXpath("//tbody/tr/td[$targetColumn]//*[text()='Реагирование']")).size
        intS = intB + intC
//        println("intA $intA")
//        println("intB $intB")
//        println("intC $intC")
//        println("intS $intS")
        Assertions.assertTrue(intS == intA)
        //Очищаем фильтр "Статусы"
        element(byXpath("//span[text()='Статусы']/button")).click()
        /////////////////////////////////////////////////////////////////////////////////////////
        //Открываем фильтр "Уровни"
        var filterLevels = true
        if (elements(byXpath("//span[text()='Уровни']/..")).size != 1){
            element(byXpath("//span[contains(text(),'Еще фильтры')]/.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//*[contains(text(),'Уровень происшествия')]/..//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            filterLevels = false
        }
        else {
            element(byXpath("//span[text()='Уровни']/..")).click()
        }
        element(byCssSelector("div[tabindex='-1'] div[role='combobox']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Выбираем "ЧС" и "Угроза ЧС"
//        element(byCssSelector("input#operationModeId")).setValue("Угроза ЧС").sendKeys(Keys.DOWN, Keys.ENTER)
//        element(byCssSelector("input#operationModeId")).setValue("ЧС").sendKeys(Keys.DOWN)
//        element(byCssSelector("input#operationModeId")).setValue("ЧС").sendKeys(Keys.ENTER)
        element(byCssSelector("input#operationModeId")).sendKeys("Угроза ЧС", Keys.DOWN, Keys.ENTER)
        element(byCssSelector("input#operationModeId")).sendKeys("ЧС",Keys.DOWN, Keys.ENTER)
        element(byCssSelector("input#operationModeId")).click()
        //Применить
        element(byXpath("//span[text()='Применить']/..")).click()
        //Дожидаемся применения фильтра
        Thread.sleep(2000)
        if (filterLevels){
            element(byXpath("//span[text()='Уровни']/button"))
                .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        }
        checkbox("Уровень происшествия", true, waitTime)
        targetColumn = numberOfColumn("Уровень происшествия", waitTime)
        Thread.sleep(500)
        intA = elements(byXpath("//table/tbody/tr")).size
        intB = elements(byXpath("//tbody/tr/td[$targetColumn][text()='Угроза ЧС']")).size
        //intC = elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-'][style]")).size
        intC = elements(byXpath("//tbody/tr/td[$targetColumn][text()='ЧС']")).size
        intS = intB + intC
        Assertions.assertTrue(intS == intA)
        //Очищаем фильтр Уровни
        if (filterLevels){
            element(byXpath("//span[text()='Уровни']/button")).click()
        } else {
            element(byXpath("//span[contains(text(),'Еще фильтры')]/.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//*[contains(text(),'Уровень происшествия')]/..//input/following-sibling::div/button[@title='Clear']"))
                .hover()
            element(byXpath("//*[contains(text(),'Уровень происшествия')]/..//input/following-sibling::div/button[@title='Clear']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//span[text()='Применить']/..")).click()
        }
        /////////////////////////////////////////////////////////////////////////////////////////
        //Открываем фильтр "Источники"
        checkbox("Источник", true, waitTime)
        targetColumn = numberOfColumn("Источник", waitTime)
        element(byXpath("//span[text()='Источники']/..")).click()
        element(byCssSelector("div[tabindex='-1'] div[role='combobox']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Устанавливаем значения фильтров
        element(byCssSelector("input#callTypeId")).setValue("Видеоаналитика").sendKeys(Keys.DOWN, Keys.ENTER)
        element(byCssSelector("input#callTypeId")).setValue("СМС").sendKeys(Keys.DOWN)
        element(byCssSelector("input#callTypeId")).setValue("СМС").sendKeys(Keys.ENTER)
        element(byCssSelector("input#callTypeId")).click()
        //Применить
        element(byXpath("//span[text()='Применить']/..")).click()
        //ждем применения
        Thread.sleep(500)
        element(byXpath("//span[text()='Источники']/button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
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
                    element(byXpath("//span[text()='Обращения']/parent::button"))
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

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0150 Назначение карточки происшествия на службу ДДС-ЕДДС с последующей проверкой изменения статуса родительской карточки`() {
        //A311 Регистрация вызова (формирование карточки происшествия)
        //A.3.17 Назначение карточки происшествия на службу ДДС/ЕДДС (только службы, которые подключены к Системе)
        //Проверка изменения статуса родительской карточки при изменении статуса карточки назначения
        //логинимся
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        //кликаем по "создать обращение"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
//        element(byCssSelector("div span[aria-label='add'] button[type='button']")).shouldHave(exactText("Создать обращение"))
//            .click()
        //заполняем карточку
        //Источник события - выбираем случайно
        element(byCssSelector("div#calltype")).click()
        val calltypeRandom = (1..10).random()
        element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($calltypeRandom)")).click()
        //Источник события - факс
        //element(byCssSelector("#calltype")).click()
        //element(byCssSelector("li[role='option'][data-value='cd73ccc0-e740-4c5d-98ec-28bbe9a13be0']")).click()
        //Номер телефона
        if (elements(byCssSelector("#phone")).size > 0){
            element(byCssSelector("#phone")).sendKeys("9189999999")
        }
        //ФИО
        if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
            && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
            element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname")
            element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
        }
        //адрес с тестированием транслитерации и клик по dadata
        addressInput("callAddress","vbhf5",waitTime)
        //заполняем дополнительную информацию
        //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0150 $dateTime"
        element(byCssSelector("div[role='textbox']>p")).click()
        element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest PMI 0150 $dateTime")
        //регистрируем обращение
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        //выбираем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test").sendKeys(Keys.DOWN, Keys.ENTER)
        //Создаем карточку
        element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus("В обработке", waitTime)
        //и что это именно так карточка которую мы только что создали
        checkICToolsDopInfo("AutoTest PMI 0150 $dateTime", waitTime)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        checkbox("Описание", true, waitTime)
        //Переходим в созданное ранее происшествие
        element(byText("AutoTest PMI 0150 $dateTime")).click()
        checkICToolsDopInfo("AutoTest PMI 0150 $dateTime", waitTime)
        //Переходим во вкладку "Работа с ДДС"
        element(byXpath("//span[text()='Работа с ДДС']/ancestor::button")).click()
        //жмем "добавить"
        //element(byCssSelector("div#simple-tabpanel-hotlines button")).click()
        element(byXpath("//span[text()='Выбрать ДДС']/ancestor::button")).click()
        //выбираем ДДС-02 г.Черкесск
        //element(byCssSelector("p#alert-dialog-description>div>div>div>div:nth-child(1)")).click()
//        element(byXpath("//p[text()='ДДС ЭОС']/../../../..")).click()
        element(byXpath("//*[text()='ДДС ЭОС']/ancestor::div[@id='panel1a-header']")).click()
        //p[text()='ДДС ЭОС']/../../../..
        //Thread.sleep(10000)
        element(byXpath("//*[text()='ДДС-02 г.Черкесск']/ancestor::div/div/label//input")).click()
        //element(byCssSelector("p#alert-dialog-description>div>div>div>div:nth-child(1) div[style='width: 100%;']>div:nth-child(2) input")).click()
        //////div[text()='ДДС-02 г.Черкесск']/../div/label/span/span/input
        //element(byCssSelector("form>div:nth-child(3) button:nth-child(1)")).click()
        element(byXpath("//span[text()='Назначить']/ancestor::button")).click()
        element(byText("ДДС-02 г.Черкесск")).should(exist, ofSeconds(waitTime))
        element(byText("AutoTest PMI 0150 $dateTime")).should(exist, ofSeconds(waitTime))

        logoffTool()
        logonDds()

        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        checkbox("Описание", true, waitTime)
//        //Открываем выпадающий список
//        element(byCssSelector("button[data-testid='Колонки-iconButton']"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        //Дожидаемся, что список появился
//        element(byCssSelector("fieldset[aria-label='Показать/скрыть колонки']>div>label"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']"))
//            .should(exist, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']")).click()
//        element(byXpath("//span[text()='Описание']/../span/span/*[name()='svg']/*[name()='path']"))
//            .shouldHave(attribute("d", checkboxTrue), ofSeconds(waitTime))
//        element(byCssSelector("button[aria-label='Close']")).click()
        //Находим созданную КП в КИАП ДДС
        element(byText("AutoTest PMI 0150 $dateTime")).should(exist, ofSeconds(waitTime)).click()
        //устанавливаем статус "Реагирование"
        updateICToolStatus("Реагирование", waitTime)
//        element(byXpath("//span[text()='В обработке']/parent::button"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byXpath("//span[text()='В обработке']/parent::button")).click()
//        element(byXpath("//span[contains(@class,'MuiButton-label')][text()='Реагирование']/parent::button"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byXpath("//span[contains(@class,'MuiButton-label')][text()='Реагирование']/parent::button")).click()
//        element(byXpath("//span[text()='В обработке']/parent::button"))
//            .shouldNot(exist, ofSeconds(waitTime))
//        element(byXpath("//span[contains(@class,'MuiButton-label')][text()='Реагирование']/parent::button"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //Thread.sleep(500)

        logoffTool()

        //Возвращаемся в КИАП и проверяем статус родительской карточки
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        checkbox("Описание", true, waitTime)
//        //Открываем выпадающий список
//        element(byCssSelector("button[data-testid='Колонки-iconButton']"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        //Дожидаемся, что список появился
//        element(byCssSelector("fieldset[aria-label='Показать/скрыть колонки']>div>label"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']")).should(exist, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']")).click()
//        //ждем пока выделится чекбокс
//        element(byXpath("//span[text()='Описание']/../span/span/*[name()='svg']/*[name()='path']"))
//            .shouldHave(attribute("d", checkboxTrue), ofSeconds(waitTime))
//        element(byCssSelector("button[aria-label='Close']")).click()
        //Находим созданную КП
        element(byText("AutoTest PMI 0150 $dateTime")).should(exist, ofSeconds(waitTime)).click()
        //проверяем статус родительской карточки
        checkICToolIsStatus("Реагирование", waitTime)

        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0160 Назначение сил и средств в КП`() {
        //A.3.18 Назначение сил и средств ДДС
        logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        element(byCssSelector("table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //открываем фильтр "Типы происшествий"
        element(byXpath("//*[text()='Типы происшествий']/ancestor::button")).click()
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Выбираем "Повседневные"
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .sendKeys("П Повседневные")
        element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
            .sendKeys(Keys.DOWN, Keys.ENTER)
        repeat(14){
            element(byXpath("//label[text()='Типы происшествий']/..//input[@id='incidentTypeId-autocomplete']"))
                .sendKeys(Keys.BACK_SPACE)
        }
        element(byXpath("//*[text()='Применить']/ancestor::button")).click()
        //Дожидаемся применения фильтра
        Thread.sleep(500)
        element(byXpath("//span[text()='Типы происшествий']/button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("button[style='width: 250px; min-width: 250px; display: flex; justify-content: space-between;']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //т.к. из за библиотеки построения таблицы, элементы скрытые за прокруткой вниз,
        // с точки зрения драйвера браузера станут кликабельны раньше чем на самом деле до них докрутит прокрутка,
        // сразу опускаемся вниз страницы (с прокруткой вверх будет аналогично, поэтому следующая строка не канает)
        //element(byCssSelector("table[role='grid']")).sendKeys(Keys.END)
        //Thread.sleep(5000)
        //Выбираем случайную КП
        elements(byXpath("//table/tbody/tr")).size
        val rndA = (1..elements(byXpath("//table/tbody/tr")).size).random()
        //rndA = 0
        if (rndA == elements(byXpath("//table/tbody/tr")).size) {
            element(byCssSelector("table[role='grid']")).sendKeys(Keys.END)
        } else {
            element(byXpath("//table/tbody/tr[${rndA + 1}]")).scrollIntoView(false)
            //Thread.sleep(20000)
        }
        element(byXpath("//table/tbody/tr[$rndA]")).click()
        //Переходим в силы и средства
        element(byXpath("//span[text()='Силы и средства']/parent::button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//span[text()='Силы и средства']/parent::button")).click()
        //Thread.sleep(20000)
        //Определяемся куда кликать для добавления в зависимости от того, добавлены ли уже силы и средства
        //больше не надо определять назначена ли бригада, просто тыкаем по имени кнопки
        element(byXpath("//span[text()='Добавить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
//        val buttonA = elements(byCssSelector("div#simple-tabpanel-assets button")).size
//        //val buttonB = elements(byCssSelector("div#simple-tabpanel-assets span[title='Добавить единицу реагирования'] button")).size
//        if (buttonA > 0) {
//            element(byCssSelector("div#simple-tabpanel-assets button")).click()
//        } else {
//            element(byCssSelector("div#simple-tabpanel-assets span[title='Добавить единицу реагирования'] button")).click()
//        }
        //заполняем поля
        val team = ('A'..'Z').random()
        element(byXpath("//label[text()='Единица реагирования']/following-sibling::div/input")).sendKeys("Team$team")
        element(byXpath("//label[text()='Телефон']/following-sibling::div/input")).sendKeys("9189999999")
        val teamS = (1..100).random()
        element(byXpath("//label[text()='Численность ед.реагирования']/following-sibling::div/input")).sendKeys("$teamS")
        val teamST = (0..10).random()
        //element(byText("Численность ед.реагирования")).setValue("$teamST")
        element(byXpath("//label[text()='Количество техники']/following-sibling::div/input")).sendKeys("$teamST")
        element(byXpath("//label[text()='Примечание']/following-sibling::div/textarea[1]"))
            .sendKeys("С попытки №$rndA, бригада-Team$team, в количестве $teamS человек, выехала на место происшествия, используя $teamST единиц(ы) техники")
        element(byXpath("//span[text()='Сохранить']/..")).click()
        //проверяем что бригада добавилась
        element(byText("Team$team")).should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //Thread.sleep(10000)

        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0112 Назначение КП из 112 в КИАП`() {
        //A.3.19 Убедиться на стороне Системы-112 в наличии возможности назначать   карточку на ЕЦОР  (КИАП) из Системы-112
        //A.3.20 Прием карточки из Системы-112
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        logon112()
        //Thread.sleep(100000)
        //Создаем новое происшествие
        //element(byCssSelector("button#btnNewEvent")).click()
        //Выбираем тип происшествия
        //element(byCssSelector("span#select2-cmbIncType-container")).click()
        //element(byCssSelector("ul#select2-cmbIncType-results>li:nth-child(3)")).click()
        //Создаем происшествие
        //element(byCssSelector("button#btnNewCard")).click()
        //Создаем новое обращение
        //пытаемся убрать бурацкую шторку со статусами
        element(byXpath("//span[text()='Cостояние рабочего места']/parent::div")).click()
        element(byXpath("//span[text()='Нет входящего обращения']/parent::div")).click()
        element(byXpath("//span[text()='Создать обращение']")).click()
        //Заполняем парамерты обращения
        element(byCssSelector("input#city")).sendKeys("Краснодар", Keys.DOWN, Keys.ENTER)
        element(byCssSelector("input#district")).sendKeys("Краснодарский Край", Keys.DOWN, Keys.ENTER)
        element(byCssSelector("input#street")).sendKeys("Красная")
        element(byCssSelector("input#house")).sendKeys("1")
        element(byCssSelector("input#flat")).sendKeys("1")
        element(byCssSelector("input#lastName")).sendKeys("AutoTest A.3.19 A.3.20")
        element(byCssSelector("input#firstName")).sendKeys("AutoTest $date")
        element(byCssSelector("textarea#details")).sendKeys("AutoTest 112 $dateTime")
        //Выбираем тип происшествия
        element(byCssSelector("span#select2-cmbIncType-container")).click()
        element(byXpath("//span[text()='AutoTest']/..")).click()
        //Создаем происшествие
        element(byCssSelector("button#btnNewCard")).click()
        //Сохраняем
        //element(byCssSelector("button#btnSave")).should(exist, ofSeconds(10))
        //Назначаем на test КИАП
        //element(byXpath("//*[text()='KИАП-test']/ancestor::table//select")).selectOptionByValue("6")
        element(byXpath("//*[text()='KИАП-test']/ancestor::table//select")).selectOptionContainingText("КИАП Черкесского ГО (33302)")
        element(byXpath("//strong[text()='KИАП-test']/../..//span[text()=' Назначить']")).click()
        //Завершаем обработку
        element(byXpath("//span[text()='Завершить обработку']/..")).click()
        //Вернуться к списку
        element(byCssSelector("button#btnBack>span")).click()
        //"input#street"firstName
        //Thread.sleep(50000)

        logoffTool()

        logonTool()
        //кликаем по иконке происшествий в боковом меню
        element(byXpath("//span[@title='Происшествия']/..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//span[@title='Список происшествий']/..")).click()
        //Сбрасываем фильтры
        //По непонятным мне причинам с отрисовкой окна браузера в одинаковом разрешении помещается разное количество кнопок фильтров, поэтому последнюю кнопку "Еще фильтры" будем искать так
//        val fil = elements(byCssSelector("div[role='toolbar'] form button")).size
//        //println("$fil")
//        if (fil == 8) {
//            element(byCssSelector("div[role='toolbar'] form button:nth-child(8)")).click()
//        } else if (fil == 9) {
//            element(byCssSelector("div[role='toolbar'] form button:nth-child(9)")).click()
//        }
        element(byXpath("//span[contains(text(),'Еще фильтры')]/.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //element(byCssSelector("div[role='toolbar'] form button:nth-child(9)")).click()
        element(byXpath("//span[text()='Очистить все']/..")).click()
        //добавляем в таблицу происшествий столбец "Описание"
        checkbox("Описание", true, waitTime)
//        //Открываем выпадающий список
//        element(byCssSelector("button[data-testid='Колонки-iconButton']"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        //Дожидаемся, что список появился
//        element(byCssSelector("fieldset[aria-label='Показать/скрыть колонки']>div>label"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']"))
//            .should(exist, ofSeconds(waitTime))
//        element(byCssSelector("input[value='description']")).click()
//        //ждем пока выделится чекбокс
////        element(byXpath("//span[text()='Дополнительная информация']/../span/span/*[name()='svg']/*[name()='path']"))
////            .shouldHave(attribute("d", checkboxTrue), ofSeconds(waitTime))
//        element(byXpath("//span[text()='Описание']/../span/span/*[name()='svg']/*[name()='path']"))
//            .shouldHave(attribute("d", checkboxTrue), ofSeconds(waitTime))
//        element(byCssSelector("button[aria-label='Close']")).click()
        //Thread.sleep(500)
        //Переходим в созданное ранее в 112 происшествие
        element(byText("AutoTest 112 $dateTime")).click()
        element(byXpath("//h3[text()='Описание происшествия']/../following-sibling::div//p"))
            .shouldHave(text("AutoTest 112 $dateTime"), ofSeconds(waitTime))
        element(byCssSelector("div#panel1a-header")).shouldHave(text("Система-112"), ofSeconds(waitTime))
        checkICToolIsStatus("В обработке", waitTime)
        logoffTool()

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0180 Просмотр паспортов опасных объектов`() {
        //A.3.21 Просмотр паспортов опасных объектов
        logonTool()
        menuNavigation("Справочники", "Организации", waitTime)
        stringsOnPage(50, waitTime)
        //кликаем по иконке справочников в боковом меню
//        element(byXpath("//span[@title='Справочники']/..")).click()
//        //Переходим в справочник "Организации"
//        element(byXpath("//span[text()='Организации']/../..")).click()
        //ждем загрузки
        element(byCssSelector("tbody>tr"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //ищем столбец "файлы"
//        tools.checkbox("Файлы",true,waitTime)
        checkbox("",true,waitTime)
        val columsNameElements = elements(byXpath("//thead/tr/th//*[text()]"))
        val columsName = mutableListOf<String>()
        columsNameElements.forEach {
            columsName.add(it.ownText)
        }
//        if (!columsName.contains("Файлы")){
//            tools.checkbox("Файлы",true,waitTime)
//        }
        //считаем количество записей на странице
        val all = elements(byCssSelector("tbody>tr")).size
        //Ищем строки организаций с прикрепленными файлами построчно проверяя наличие скрепки
        for (i in 1..all) {
            //опять боремся с глюком видимости но не кликабельности нижних элементов
            if (i == all) {
                element(byCssSelector("body")).sendKeys(Keys.END)
            } else {
                element(byXpath("//tbody/tr[${i + 1}]")).scrollIntoView(false)
            }
            element(byXpath("//tbody/tr[$i]"))
                .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            //ищем скрепку в строке
            val haveFile =
                elements(byXpath("//tbody/tr[$i]/td[${columsName.indexOf("Файлы")+1}]/*[name()='svg']")).size
            //если нашли, то...
            if (haveFile == 1) {
                //переходим в карточку организации
                element(byXpath("//tbody/tr[$i]")).click()
                element(byXpath("//form[@novalidate]")).should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byCssSelector("body")).sendKeys(Keys.END)
                //считаем файлы прикрепленные к организации
                val countFile = elements(byCssSelector("div[style='padding: 5px;']>div a")).size
                //и построчно их опознаем и открываем
                for (u in 1..countFile) {
                    element(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) a"))
                        .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                    //т.к. элемент отображается на форме так как был загружен, пока не придумал как обойти зависимость от регистра для части текста
                    val filePDF =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.PDF']")).size
                    val filepdf =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.pdf']")).size
                    val fileSVG =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.SVG']")).size
                    val filesvg =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.svg']")).size
                    val fileJPEG =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.JPEG']")).size
                    val filejpeg =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.jpeg']")).size
                    val fileJPG =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.JPG']")).size
                    val filejpg =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.jpg']")).size
                    val filePNG =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.PNG']")).size
                    val filepng =
                        elements(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) span[title$='.png']")).size
                    if (filePDF == 1 || filepdf == 1) {
                        //пдф пока только скачивать придумал...
                        val href =
                            element(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) a")).getAttribute("href")
                        open("$href")
                        Thread.sleep(1000)
                    } else if (fileSVG == 1 || filesvg == 1) {
                        val href =
                            element(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) a")).getAttribute("href")
                        open("$href")
                        element(byCssSelector("rect")).should(exist, ofSeconds(waitTime))
                        Thread.sleep(1000)
                        back()
                    } else if (fileJPEG == 1 || fileJPG == 1 || filePNG == 1 || filejpeg == 1 || filejpg == 1 || filepng == 1) {
                        val href =
                            element(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) a")).getAttribute("href")
                        open("$href")
                        element(byCssSelector("img")).should(exist, ofSeconds(waitTime))
                        Thread.sleep(1000)
                        back()
                    }
                }
                back()
            }
        }
        //Удаляем нафиг все что скачали
        FileUtils.deleteDirectory(File("/home/isizov/Kotlin_tests/build/downloads"))

        logoffTool()
    }







    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0220 Проверка открытия карточек справочных сущностей`() {
        //A.3.28 Проверка централизованного хранения и управления структурированной справочной информации
        logonTool()

        for (i in 1..10){
            //кликаем по иконке справочников
            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul")).click()
            //переходим в каждый справочник
            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div/ul[$i]")).click()
            //если в справочнике не пусто, то переходим в первую найденную строку и проверяем там наличие блока описания и заголовка "общие данные" в нем
            //ждем загрузки таблицы
            element(byCssSelector("main table>tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
//            if (elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']")).size>0 && i!=10){
            if (i!=10){
                element(byXpath("//table/tbody/tr"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
//                val stringsList = elements(byXpath("//table/tbody/tr"))
//                stringsList.shuffle()
//                stringsList[0].click()
                // отказался от изящной конструкции выше, т.к. придется встаялять победу над тем что подвал таблицы перекрывает таблицу, оставил как есть
                element(byXpath("//table/tbody/tr[1]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            } else if (i==10){
                element(byXpath("//table/tbody/tr"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
//                val columnMenu = tools.numberOfColumnII(" ", waitTime)
//                element(byXpath("//table/tbody/tr/td[$columnMenu]//button"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
//                element(byXpath("//*[text()='Просмотреть']/parent::button"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
            }
            element(byXpath("//h6[text()='Общие данные']/parent::form"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            shrinkCheckTool()
        //Thread.sleep(5000)
        }
    logoffTool()
    }

    @DataProvider(name = "Справочники")
    open fun Справочники(): Any {
        return arrayOf<Array<Any>>(
            arrayOf("Алгоритмы реагирования"),
            arrayOf("Видеокамеры"),
            arrayOf("Датчики"),
            arrayOf("Дежурные службы"),
            arrayOf("Должностные лица"),
            arrayOf("Метки"),
            arrayOf("Муниципальные образования"),
            arrayOf("Организации"),
            arrayOf("Силы и средства"),
            arrayOf("Типы происшествий")
        )
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, dataProvider = "Справочники", groups = ["ПМИ", "ALL"])
    fun `PMI 0230 Проверка поиска справочных данных`(subMenu: String) {
        //A.3.29 Проверка поиска справочных данных
        //хорошо бы в таблицу включать все столбцы, но это потом доработаю //доработано
        logonTool()
        //временно не проверяем поиск по справочнику типов происшествий т.к. не можем там контролировать число строк
        //(ну как бы можем, но не по такому алгоритму, а вероятно в отдельном тесте. еще подумаю...)
//        for (dicts in 1..10) {
//            //кликаем по иконке справочников
//            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul")).click()
//            //переходим в каждый справочник
//            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div/ul[$dicts]")).click()
        menuNavigation("Справочники", subMenu, waitTime)
            //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            //добавляем все доступные колонки в таблицу
        checkbox("", true, waitTime)
        //отчищаем фильтры
        elements(byXpath("//form[@novalidate]//button//button//*[@name='close']"))
            .forEach { it.click() }
            //получаем счетчик строк в левом нижнем углу страницы, в виде числа
        var allRecordCountUse: Int
//        allRecordCountUse = if (subMenu != "Типы происшествий") {
//            val allRecordCountStringList =
//                element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]")).ownText.toString().split("\n")
//                val allRecordCountNotUseStringList = allRecordCountStringList[1].split(" ")
//            allRecordCountNotUseStringList[0].toInt()
//            } else {
//                elements(byXpath("//tbody/tr")).size
//            }
        allRecordCountUse = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
            .ownText
            .toString()
            .split("\n")[1]
            .split(" ")[0]
            .toInt()
            //ситаем количество слолбцов, при том что последний нам не пригодится
        val comumnCount = elements(byXpath("//table/thead/tr/th")).size
//            println("allRecordCountString $allRecordCountString")
//            println("allRecordCountNotUse $allRecordCountNotUse")
//            println("allRecordCountUse $allRecordCountUse")
            //отркрываем поисковую строку
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder]"))
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
//                    hierarchy = true
//                    if (subMenu == "Типы происшествий"){
//                        allRecordCountUse = elements(byXpath("//tbody/tr")).size
//                    }
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
                            if (subMenu != "Типы происшествий") {
                                searchValue = elements(byXpath("//table/tbody/tr/td[$col]//text()/parent::*[not(text()='  ')]")).random().ownText
                                fullSearchValue = searchValue
                                //проверяем не номер ли это телефона и видоизменяем запись , к.т. в формате +Х(ХХХ)ХХХ-ХХ-ХХ в поисковой строке не вернет результатов, только +ХХХХХХХХХХ
                                //аналогично с ФИО
                                val ioRegex = Regex("[а-яА-Яa-zA-Z]{1}[.]\\s{1}[а-яА-Яa-zA-Z]{1}[.]{1}")
                                val telRegex = Regex("[+7(]{1}[0-9]{3}[)]{1}[0-9]{3}[-]{1}[0-9]{2}[-]{1}[0-9]{2}")
                                val workTelRegex = Regex("[0-9]{1}[-][0-9]{5}[-][0-9]{3}[-][0-9]{3}")
                                    if (telRegex.containsMatchIn(searchValue)
                                        || workTelRegex.containsMatchIn(searchValue)
                                    ) {
                                        searchValue = searchValue.filter { it.isDigit() }
                                    } else if (ioRegex.containsMatchIn(searchValue)) {
                                        searchValue = searchValue.split(" ")[0]
                                    }
                            } else if (subMenu == "Типы происшествий") { //Отдельно обрабатываем справочник типов происшествий
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
                            val nowRecordCountUse:Int
//                            if (subMenu != "Типы происшествий"){
//                                element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
//                                    .should(exist, ofSeconds(longWait))
//                                    .shouldBe(visible, ofSeconds(longWait))
                                nowRecordCountUse = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
                                    .ownText
                                    .toString()
                                    .split("\n")[1]
                                    .split(" ")[0]
                                    .toInt()
//                            } else {
//                                nowRecordCountUse = elements(byXpath("//tbody/tr")).size
//                                Assertions.assertTrue(nowRecordCountUse != 0)
//                            }
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
        logonTool()
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
        logonTool()
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation("Справочники", "Организации", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки и переходим в случайную организацию
        val organizationTableStringCount = elements(byXpath("//table/tbody/tr")).size
        val rndOrganization = (1..organizationTableStringCount).random()
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byXpath("//table/tbody/tr[${rndOrganization + 1}]"))
            .scrollIntoView(false)
        }
        element(byXpath("//table/tbody/tr[($rndOrganization)]")).click()
        //ждем Редактировать
        element(byXpath("//span[text()='Редактировать']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //запомнинаем какую организацию редактируем
        val organizationName = element(byCssSelector("input[name='title']")).getAttribute("value")
        //считаем существующие метки на случай когда они есть и когда их нет
        var amountLabel = elements(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()]")).size
        val oldLabelsList = mutableListOf<String>()
        val newLabelsList = mutableListOf<String>()
        if (amountLabel > 0){
            for (i in 1..amountLabel){
                oldLabelsList.add(element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;'][$i]//span[text()]")).ownText)
            }
        }
        //жмем Редактировать
        element(byXpath("//span[text()='Редактировать']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем кнопки "Сохранить", как показатель загрузки страницы
        element(byXpath("//span[text()='Сохранить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //скролим до Указать на карте
        element(byXpath("//span[text()='Указать на карте']/../parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .scrollIntoView(false)
        //вставляем новую метку
        inputRandomNew("labelsId-textfield", true, waitTime)
        //жмем кнопки "Сохранить"
//        Thread.sleep(2000)
        //пересчитываем метки
        amountLabel = elements(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()]")).size
        //вносим каждую в список
        for (i in 1..amountLabel){
            newLabelsList.add(element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;'][$i]//span[text()]")).ownText)
        }
        element(byXpath("//span[text()='Сохранить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем загрузки карточки организации
//        //ждем загрузки таблицы в которую нас выкидывает после сохранения
//        element(byCssSelector("main table>tbody"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//        //переходим в ту же организацию
//        if (rndOrganization == organizationTableStringCount){
//            element(byCssSelector("body")).sendKeys(Keys.END)
//        } else { element(byXpath("//table/tbody/tr[${rndOrganization + 1}]"))
//            .scrollIntoView(false)
//        }
//        element(byText(organizationName.toString())).click()
        //ждем Редактировать
        element(byXpath("//span[text()='Редактировать']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //убеждаемся, что не затерли ни одной метки, а именно добавили одну.
        Assertions.assertTrue(newLabelsList.containsAll(oldLabelsList))
        Assertions.assertTrue(newLabelsList.size > oldLabelsList.size)
        //убеждаемся что все метки есть на карточке
        newLabelsList.forEach {label ->
            element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()='$label']"))
                .should(exist, ofSeconds(waitTime))
        }
        logoffTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //теперь удалим эту (эти) метку(и)
        logonTool()
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation("Справочники", "Организации", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //открываем поиск (т.к. теперь не знаем до какой строки листать)
        //переходим в ту же организацию
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byXpath("//table/tbody/tr[${rndOrganization + 1}]"))
            .scrollIntoView(false)
        }
        element(byText(organizationName.toString())).click()
        //жмем Редактировать
        element(byXpath("//span[text()='Редактировать']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val newLabelList = newLabelsList.minus(oldLabelsList.toSet())
        val newLabelNum = newLabelsList.size
        newLabelList.forEach {
            element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()='$it']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
//        println("newLabel $newLabel")
        //убеждаемся, что поставленная метка на месте
        //жмем Редактировать
        element(byXpath("//span[text()='Редактировать']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //удаляем метку
        element(byXpath("//span[text()='Указать на карте']/../parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .scrollIntoView(false)
        newLabelList.forEach {
            element(byXpath("//label[text()='Метки']/..//span[text()='$it']/../*[name()='svg']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
        element(byXpath("//span[text()='Сохранить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем Редактировать
        element(byXpath("//span[text()='Редактировать']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //убеждаемся, что нашей метки нет
        newLabelList.forEach {
            element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()='$it']"))
                .shouldNot(exist, ofSeconds(waitTime))
                .shouldNotBe(visible, ofSeconds(waitTime))
        }
        logoffTool()
//        println("newLabel $newLabel")
//        println("oldLabelsList $oldLabelsList")
//        println("newLabelsList $newLabelsList")
//        Thread.sleep(10000)

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0260 Проверка использования ассоциативных связей-ссылок между объектами справочников`(){
        //A.3.32 Проверка использования ассоциативных связей-ссылок между объектами справочников
        logonTool()
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation("Справочники", "Дежурные службы", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Собираем все ФИО, что бы не проверять организацию у которой руководитель не указан
        checkbox("Руководитель", true, waitTime)
        val officialIdColumn = numberOfColumn("Руководитель", waitTime)
//        val officialIdElementList = elements(byXpath("//table/tbody/tr/td[$officialIdColumn][text()]"))
//        val officialIdFIOList = mutableListOf<String>()
//        officialIdElementList.forEach {
//            officialIdFIOList.add(it.ownText)
//        }
        //считаем строки и переходим в случайную службу
        val organizationTableStringCount = elements(byXpath("//table/tbody/tr")).size
        var rndOrganization: Int
        do {
            rndOrganization = (1..organizationTableStringCount).random()
        } while (elements(byXpath("//table/tbody/tr[$rndOrganization]/td[$officialIdColumn][text()]")).size == 0)
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byXpath("//table/tbody/tr[${rndOrganization + 1}]"))
            .scrollIntoView(false)
        }
        //переходим в дежурную службу
        element(byXpath("//table/tbody/tr[$rndOrganization]")).click()
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //element(byXpath("//div[@data-testid='officialId']/../..//span[@title='Перейти']/button"))
        //запоминаем ФИО руководителя
        element(byXpath("//label[text()='Руководитель']/..//input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val officialFIO = element(byXpath("//label[text()='Руководитель']/..//input")).getAttribute("value")
//        val companyName = element(byXpath("//label[text()='Организация']/..//input")).getAttribute("value")
        val officialFIOList = officialFIO?.split(" ")
//        println("officialFIO $officialFIO")
//        println("companyName $companyName")
        //label[text()='Руководитель']/..//input
        //кликаем на стрелку перехода к руководителю
        element(byXpath("//label[text()='Руководитель']/../../../..//span[@title='Перейти']/button"))
//        element(byXpath("//label[text()='Руководитель']/ancestor::div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-sm-6']//span[@title='Перейти']/button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//label[text()='Фамилия']/..//input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Assertions.assertTrue(officialFIO!!.contains(
            element(byXpath("//label[text()='Фамилия']/..//input"))
                .getAttribute("value").toString(), false))
        element(byXpath("//label[text()='Имя']/..//input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Assertions.assertTrue(officialFIO.contains(
            element(byXpath("//label[text()='Имя']/..//input"))
                .getAttribute("value").toString(), false))
        if (officialFIOList!!.size > 2) {
            element(byXpath("//label[text()='Отчество']/..//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            Assertions.assertTrue(officialFIO.contains(
                element(byXpath("//label[text()='Отчество']/..//input"))
                    .getAttribute("value").toString(), false))

        }
//        element(byXpath("//label[text()='Организация']/..//input"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .shouldHave(attribute("value", companyName.toString()))
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `PMI 0270 Проверка возможности создания, удаления записей, просмотра детальной истории в справочнике должностных лиц`(){
        //A.3.33 Проверка возможности создания записей в справочниках
        //A.3.34 Проверка возможности удаления записей в справочниках
        //A.3.35 Просмотр детальной истории записи/источников данных записи
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        logonTool()
        //сначала проверим остатки неудачных запусков и удалим их.
        menuNavigation("Справочники", "Должностные лица", waitTime)
        checkbox("", true, waitTime)
        //воспользуемся поиском
        var menuColumnNubber = numberOfColumn(" ", waitTime)
        val fioColumnNubber = numberOfColumn("ФИО", waitTime)
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder^='ФИО']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder^='ФИО']")).sendKeys("N 0270 AutoTest", Keys.ENTER)
        //ждем результатов
        Thread.sleep(1000)
        //удалим потуги неудачных тестов с учетом того, что некоторые ДЛ могли стать пользователями
        val userColumn = numberOfColumn("Пользователь", waitTime)
        if (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0) {
            for (i in 1..elements(byXpath("//table/tbody/tr")).size) {
                if (elements(byXpath("//table/tbody/tr[$i]/td[$userColumn]//*[@name='user']")).size == 0) {
                    val removedPersonFIO = element(byXpath("//table/tbody/tr[$i]/td[$fioColumnNubber]")).ownText
                    //открываем три точки
                    element(byXpath("//table/tbody/tr[$i]/td[$menuColumnNubber]//button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    //удаляем
                    element(byXpath("//span[text()='Удалить']/parent::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    //подтверждаем удаление
                    element(byXpath("//div[@role='dialog']//span[text()='Удалить']/parent::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//table/tbody/tr[$i]/td[$fioColumnNubber][text()='$removedPersonFIO']"))
                        .shouldNot(exist, ofSeconds(waitTime))
                    Thread.sleep(500)
                }
            }
        }
        //идем в справочник ДС что бы достать оттуда ДС и их организации, что бы лицо создавать с этими параметрами
        menuNavigation("Справочники", "Дежурные службы", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //переключаемся на 500 записей на странице
        stringsOnPage(500, waitTime)
        //выставляем отображение нужных столбцов
        checkbox("Наименование;Организация", true, waitTime)
        element(byXpath("//table/thead/tr/th//*[text()='Наименование']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
//        tools.checkbox("Организация", true, waitTime)
        //ждем появления выставленных столбцов (последнего из них)
        element(byXpath("//table/thead/tr/th//*[text()='Организация']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val hotLineList = mutableListOf<String>()
        val organizationList = mutableListOf<String>()
        val columnCount = elements(byXpath("//thead//*[text()]")).size
        var hotlineColumnNumber: Int = 2
        var organizationColumnNumber: Int = 4
        for (i in 1..columnCount){
            val columnName = element(byXpath("(//thead//*[text()])[$i]")).ownText
            if (columnName == "Наименование"){
                hotlineColumnNumber = i
            } else if (columnName == "Организация"){
                organizationColumnNumber = i
            }
        }
        val stringsCount = elements(byXpath("//tbody/tr")).size
        for (u in 1.. stringsCount){


            hotLineList.add(element(byXpath("//tbody/tr[$u]/td[$hotlineColumnNumber][text()]")).ownText)
            organizationList.add(element(byXpath("//tbody/tr[$u]/td[$organizationColumnNumber][text()]")).ownText)
        }
        Assertions.assertTrue(hotLineList.size == organizationList.size)
        menuNavigation("Справочники","Должностные лица",waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        menuColumnNubber = numberOfColumn(" ", waitTime)
        element(byXpath("//span[text()='Добавить новое']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val addAtributeSelector = "//label[text()='%s']/following-sibling::div/input"
//        element(byXpath("//label[text()='Фамилия']/following-sibling::div/input")).sendKeys(dateTime)
        element(byXpath(addAtributeSelector.format("Фамилия"))).sendKeys(dateTime)
        element(byXpath(addAtributeSelector.format("Имя"))).sendKeys("N 0270")
        element(byXpath(addAtributeSelector.format("Отчество"))).sendKeys("AutoTest")
        val telR = (1000000..9999999).random()
        element(byXpath(addAtributeSelector.format("Рабочий телефон"))).sendKeys("+7918$telR")
        element(byXpath(addAtributeSelector.format("Должность"))).sendKeys("Robot")
        element(byXpath(addAtributeSelector.format("E-mail"))).sendKeys("test@test.com")
        val telM = (1000000..9999999).random()
        element(byXpath(addAtributeSelector.format("Мобильный телефон"))).sendKeys("+7961$telM")
        //Выбираем организацию
        element(byXpath("//input[@name='companyId']")).click()
        val randomAtridute = (0 until hotLineList.size).random()
        element(byXpath("//input[@name='companyId']")).sendKeys(organizationList[randomAtridute])
        element(byXpath("//input[@name='companyId']")).sendKeys(Keys.DOWN, Keys.ENTER)
        val companyId = element(byXpath("//input[@name='companyId']")).getAttribute("value")
        Assertions.assertTrue(organizationList[randomAtridute] == companyId)
        //Выбираем службу
        element(byXpath("//input[@name='hotlineId']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//input[@name='hotlineId']")).sendKeys(hotLineList[randomAtridute])
        element(byXpath("//input[@name='hotlineId']")).sendKeys(Keys.DOWN, Keys.ENTER)
        val hotlineId = element(byXpath("//input[@name='hotlineId']")).getAttribute("value")
        Assertions.assertTrue(hotLineList[randomAtridute] == hotlineId)
        //сохраняем
        element(byXpath("//span[text()='Добавить']/parent::button")).click()
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //воспользуемся поиском
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder^='ФИО']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder^='ФИО']")).sendKeys(dateTime, Keys.ENTER)
        //открываем три точки
        element(byXpath("//td[contains(text(),'$dateTime')]/parent::tr/td[$menuColumnNubber]//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //открываем историю
        element(byXpath("//span[text()='История']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем загрузки
        element(byXpath("//p[text()='История должностного лица']/a[text()='$dateTime N 0270 AutoTest']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //смотрим историю развернув всё
        element(byXpath("//table/thead//*[@name='arrowRight']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        val atributeSelector = "//table/tbody//li/label[text()='%s']/following-sibling::*[text()='%s']"
        //проверяем что в истории
        element(byXpath(atributeSelector.format("Дежурная служба", hotlineId)))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath(atributeSelector.format("Организация", companyId)))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //разворачиваем ФИО и пр.
        element(byXpath("//label[text()='ФИО']/preceding-sibling::div/div[text()='▶']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //проверяем то что развернули
        element(byXpath(atributeSelector.format("Фамилия", dateTime)))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath(atributeSelector.format("Имя", "N 0270")))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath(atributeSelector.format("Отчество", "AutoTest")))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath(atributeSelector.format("Электронная почта", "test@test.com")))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath(atributeSelector.format("Должность", "Robot")))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath(atributeSelector.format("Мобильный телефон", "+7961$telM")))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath(atributeSelector.format("Рабочий телефон", "+7918$telR")))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //а теперь удалим его
        logonTool()
        //кликаем по иконке справочников
        //переходим в нужный справочник
        menuNavigation("Справочники", "Должностные лица", waitTime)
        menuColumnNubber = numberOfColumn(" ", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //воспользуемся поиском
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder^='ФИО']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder^='ФИО']")).sendKeys(dateTime, Keys.ENTER)
        //открываем три точки
        element(byXpath("//td[contains(text(),'$dateTime')]/parent::tr/td[$menuColumnNubber]//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //удаляем
        element(byXpath("//span[text()='Удалить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //подтверждаем удаление
        element(byXpath("//div[@role='dialog']//span[text()='Удалить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//tbody/tr//*[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
    }

}
