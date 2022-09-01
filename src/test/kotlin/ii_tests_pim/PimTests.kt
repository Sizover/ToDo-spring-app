package ii_tests_pim

//import kotlin.collections.EmptyMap.keys
import Retry
import Tools
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exactText
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byName
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.back
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.Selenide.open
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.DataProvider
import java.io.File
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
//import selenium.webdriver.common.keys






//import org.junit.Assert.*

//@org.testng.annotations.Test (retryAnalyzer = Retry::class)
class PimTests {
    val tools = Tools()
    var date = ""
    var dateTime = ""
//    val checkboxTrue = "M19 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2V5c0-1.1-.89-2-2-2zm-9 14l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"
//    val checkboxFalse = "M19 5v14H5V5h14m0-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2z"

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
            arrayOf("Типы происшествий"),
        )
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0010`() {
        //A31 Убедиться в наличии списка объектов  в справочнике «Муниципальные образования»
        //A34 Убедиться в наличии списка объектов  в справочнике «Должностные лица»
        //A35 Убедиться в наличии списка объектов  в справочнике «Дежурные службы»
        //A36 Убедиться в наличии списка объектов  в справочнике «Видеокамеры»
        //A37 Убедиться в наличии списка объектов  в справочнике «Датчики»
        //логинимся
        //val tools = Tools()
//        tools.logonTool()
        tools.logonTool()
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
            tools.menuNavigation("Справочники", subMenu, waitTime)
            //сравниваем количество записей в таблице, на больше или равно 5
            element(byXpath("//table/tbody/tr"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            elements(byXpath("//table/tbody/tr"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5))
        }
        //разлогиниваемся и закрываем браузер
        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0020`() {
        //A32 Убедиться в наличии списка объектов  в справочнике «Типы происшествий»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке справочников в боковом меню
        //Переходим в "Типы происшествий"
        tools.menuNavigation("Справочники", "Типы происшествий", waitTime)
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
        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)

    fun `N 0030`() {
        //A33 Убедиться в наличии списка объектов  в справочнике «Организации»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке справочников в боковом меню
        //Переходим в справочник "Организации"
        tools.menuNavigation("Справочники","Организации",waitTime)
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
        tools.logoffTool()
    }


//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
//    fun `N 0040`() {
//        //A34 Убедиться в наличии списка объектов  в справочнике «Должностные лица»
//        //логинимся
//        //val tools = Tools()
//        tools.logonTool()
//        //кликаем по иконке справочников в боковом меню
//        //Переходим в справочник "Должностные лица"
//        tools.menuNavigation("Справочники", "Должностные лица", waitTime)
//        //сравниваем колличество строк должностных лиц, по условию больше или равно с 5
//        element(byXpath("//table/tbody/tr"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//        elements(byXpath("//table/tbody/tr"))
//            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5))
//        tools.logoffTool()
//    }

//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
//    fun `N 0050`() {
//        //A35 Убедиться в наличии списка объектов  в справочнике «Дежурные службы»
//        //логинимся
//        //val tools = Tools()
//        tools.logonTool()
//        //кликаем по иконке справочников в боковом меню
//        //Переходим в справочник "Дежурные службы"
//        tools.menuNavigation("Справочники", "Дежурные службы", waitTime)
//        //сравниваем колличество строк дежурных служб, по условию больше или равно с 3
//        element(byXpath("//table/tbody/tr"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//        elements(byXpath("//table/tbody/tr"))
//            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(3))
//        tools.logoffTool()
//    }

//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
//    fun `N 0060`() {
//        //A36 Убедиться в наличии списка объектов  в справочнике «Видеокамеры»
//        //логинимся
//        //val tools = Tools()
//        tools.logonTool()
//        //кликаем по иконке справочников в боковом меню
//        //Переходим в справочник "Видеокамеры"
//        tools.menuNavigation("Справочники", "Видеокамеры", waitTime)
//        //сравниваем колличество строк дежурных служб, по условию больше или равно с 2
//        elements(byXpath("//table/tbody/tr"))
//            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(2))
//        tools.logoffTool()
//    }

//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
//    fun `N 0070`() {
//        //A37 Убедиться в наличии списка объектов  в справочнике «Датчики»
//        //логинимся
//        //val tools = Tools()
//        tools.logonTool()
//        //кликаем по иконке справочников в боковом меню
//        tools.menuNavigation("Справочники", "Датчики", waitTime)
//        //сравниваем колличество строк дежурных служб, по условию больше или равно с 2
//        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
//            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(2))
//        tools.logoffTool()
//    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0080`() {
        //A38 Убедиться в наличии списка объектов  в меню «Список происшествий»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия","Список происшествий",waitTime)
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
        tools.logoffTool()
    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0090`() {
        //A39 Убедиться в наличии списка объектов  в меню «Архив происшествий»
        //логинимся
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Архив происшетвий"
        tools.menuNavigation("Происшествия","Архив происшествий",waitTime)
        //сравниваем колличество карточек в архиве, по условию больше или равно с 2
        element(byXpath("//table/tbody/tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byXpath("//table/tbody/tr"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(2))
        tools.logoffTool()

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0100`() {
        //A310 Проверка процедуры авторизации в системе 1
        //val tools = Tools()
        tools.aThreeHundredAndTenOne()
        element(byName("login")).click()
        element(byCssSelector("#input-error")).shouldHave(exactText("Неправильное имя пользователя или пароль."))
        tools.aThreeHundredAndTenOne()
        element(byName("username")).sendKeys("test")
        element(byName("login")).click()
        element(byCssSelector("#input-error")).shouldHave(exactText("Неправильное имя пользователя или пароль."))
        tools.aThreeHundredAndTenOne()
        element(byName("username")).sendKeys("test")
        element(byName("password")).sendKeys("test")
        element(byName("login")).click()
        element(byCssSelector("#input-error")).shouldHave(exactText("Неправильное имя пользователя или пароль."))
        tools.logoffTool()
    }
/*
    @Test
    fun `A310 Проверка процедуры авторизации в системе 2` (){
        val tools = Tools()
        tools.aThreeHundredAndTenTwo()
        Selenide.element(Selectors.byName("login")).click()
        Selenide.element(Selectors.byCssSelector("#input-error")).shouldHave(Condition.exactText("Неправильное имя пользователя или пароль."))
    }

    @Test
    fun `A310 Проверка процедуры авторизации в системе 3` (){
        val tools = Tools()
        tools.aThreeHundredAndTenThree()
        Selenide.element(Selectors.byName("login")).click()
        Selenide.element(Selectors.byCssSelector("#input-error")).shouldHave(Condition.exactText("Неправильное имя пользователя или пароль."))
    }
*/

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0110`() {
        //A311 Регистрация вызова (формирование карточки происшествия)(С прикреплением файлов)
        //A312 Проверка прикрепления файла к происшествию
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
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
        tools.addressInput("callAddress","vbhf5", waitTime)
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
        element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
            .shouldHave(text("В обработке"), ofSeconds(waitTime))
        //и что это именно так карточка которую мы только что создали
//        element(byCssSelector("div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-md-8 > div:nth-child(4)"))
//            .shouldHave(text("AutoTest N 0110 $dateTime"), ofSeconds(waitTime))
        element(byXpath("//div[text()='AutoTest N 0110 $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //загружаем файлы проверяя их прикрепление
        element(byCssSelector("input#upload-file"))
            .uploadFile(File("/home/isizov/Метаданные/AutoTest.webp"))
        //Thread.sleep(50000)
        element(byCssSelector("div[style='padding: 5px;'] > div:first-child"))
            .shouldHave(text("AutoTest.webp"), ofSeconds(waitTime))
        element(byCssSelector("input#upload-file"))
            .uploadFile(File("/home/isizov/Метаданные/Тестовый файл_.docx"))
        element(byCssSelector("div[style='padding: 5px;'] > div:first-child"))
            .shouldHave(text("Тестовый файл_.docx"), ofSeconds(waitTime))

        tools.logoffTool()

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //`A312 Проверка прикрепления файла к происшествию`
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий" )
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tools.checkbox("Описание", true, waitTime)
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
        element(byCssSelector("input#upload-file")).uploadFile(File("/home/isizov/Метаданные/test.pdf"))
        //Thread.sleep(50000)
        element(byCssSelector("div[style='padding: 5px;'] > div:first-child"))
            .shouldHave(text("test.pdf"), ofSeconds(waitTime))

        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0120`() {
        //A313 Проверка наличия опросника абонента (заявителя)
        //val tools = Tools()
        //логинимся
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
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

        tools.logoffTool()

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0130`() {
        //A.3.11 Регистрация вызова (формирование карточки происшествия)
        //А.3.14 Проверка формирования плана (алгоритма) реагирования по заданному событию (происшествию)
        //A.3.15 Проверка формирования действий оператора системы в рамках плана (алгоритма) реагирования с автоматизированным контролем выполнения
        //логинимся
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        //кликаем по "создать обращение"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
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
        tools.addressInput("callAddress","vbhf5", waitTime)
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
        element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']")).shouldHave(
            text("В обработке"),
            ofSeconds(waitTime)
        ).shouldBe(visible, ofSeconds(waitTime))
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
        element(byXpath("//span[text()='Реагирование']/..")).click()
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

        tools.logoffTool()

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //`A315 Проверка формирования действий оператора системы в рамках плана (алгоритма) реагирования с автоматизированным контролем выполнения
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tools.checkbox("Описание", true, waitTime)
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
        //element(byCssSelector("div[role='tablist'] > button:nth-child(1)")).should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//span[text()='Реагирование']/..")).click()
//        element(byCssSelector("div[role='tablist'] > button:nth-child(2)")).should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("div[role='tablist'] > button:nth-child(2)")).click()
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
                element(byCssSelector(itemSelector)).click()
                //Проверяем изменение стиля поля дочернего пункта, после его разворачивания
                element(byCssSelector(colorItemSelector.format("255, 255, 255", "66, 106, 210")))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                //Проверяем красненький кружочек первого пункта (для проверки позеленения по мере выполнения)
                element(byCssSelector(circleSelector.format("Не выполнен", "#BA3113")))
//                element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) > div > div#panel1a-header div.MuiBox-root > div:nth-child($c) > span[title^='Статус: Не выполнен'] rect[fill='#16BA13']"))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                //кликаем по полю ввода текста
                element(byCssSelector(textFieldSelector))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //вводим текст
                element(byCssSelector(textFieldSelector))
                    .sendKeys("Выполнено мероприятие $p.$c")
                //сохраняем
                element(byXpath("//span[text()='Выполнить']/..")).click()
                //Проверяем позеленение кружочка
                element(byCssSelector(circleSelector.format("Не выполнен", "#BA3113")))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byCssSelector(circleSelector.format("Выполнен", "#16BA13")))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                //Проверяем цвета полей дочерних пунктов
                element(byCssSelector(colorItemSelector.format("247, 248, 251", "208, 214, 220")))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byCssSelector(colorItemSelector.format("232, 255, 224", "122, 249, 102")))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                //Thread.sleep(50000)
            }
        }

        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0140`() {
        //A.3.16 Фильтрация карточек происшествия
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
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
        tools.checkbox("Подгруппа", true, waitTime)
        var targetColumn = tools.numberOfColumn("Подгруппа", waitTime)
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
        tools.checkbox("Статус", true, waitTime)
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
        targetColumn = tools.numberOfColumn("Статус", waitTime)
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
        tools.checkbox("Уровень происшествия", true, waitTime)
        targetColumn = tools.numberOfColumn("Уровень происшествия", waitTime)
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
        tools.checkbox("Источник", true, waitTime)
        targetColumn = tools.numberOfColumn("Источник", waitTime)
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
        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0150`() {
        //A311 Регистрация вызова (формирование карточки происшествия)
        //A.3.17 Назначение карточки происшествия на службу ДДС/ЕДДС (только службы, которые подключены к Системе)
        //Проверка изменения статуса родительской карточки при изменении статуса карточки назначения
        //логинимся
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        //кликаем по "создать обращение"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
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
        tools.addressInput("callAddress","vbhf5",waitTime)
        //заполняем дополнительную информацию
        //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0150 $dateTime"
        element(byCssSelector("div[role='textbox']>p")).click()
        element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest N 0150 $dateTime")
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
        element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']")).shouldHave(
            text("В обработке"),
            ofSeconds(waitTime)
        ).shouldBe(visible, ofSeconds(waitTime))
        //и что это именно так карточка которую мы только что создали
        element(byXpath("//h3[text()='Описание происшествия']/../..//p")).shouldHave(
            text("AutoTest N 0150 $dateTime"),
            ofSeconds(waitTime)
        )
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tools.checkbox("Описание", true, waitTime)
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
        element(byText("AutoTest N 0150 $dateTime")).click()
        element(byXpath("//h3[text()='Описание происшествия']/../..//p"))
            .shouldHave(text("AutoTest N 0150 $dateTime"), ofSeconds(waitTime))
        //Переходим во вкладку "Работа с ДДС"
        element(byXpath("//span[text()='Работа с ДДС']/..")).click()
        //жмем "добавить"
        //element(byCssSelector("div#simple-tabpanel-hotlines button")).click()
        element(byXpath("//span[text()='Выбрать ДДС']/..")).click()
        //выбираем ДДС-02 г.Черкесск
        //element(byCssSelector("p#alert-dialog-description>div>div>div>div:nth-child(1)")).click()
        element(byXpath("//p[text()='ДДС ЭОС']/../../../..")).click()
        //p[text()='ДДС ЭОС']/../../../..
        //Thread.sleep(10000)
        element(byXpath("//*[text()='ДДС-02 г.Черкесск']/../parent::div/following-sibling::div/label//input")).click()
        //element(byCssSelector("p#alert-dialog-description>div>div>div>div:nth-child(1) div[style='width: 100%;']>div:nth-child(2) input")).click()
        //////div[text()='ДДС-02 г.Черкесск']/../div/label/span/span/input
        //element(byCssSelector("form>div:nth-child(3) button:nth-child(1)")).click()
        element(byXpath("//span[text()='Назначить']/..")).click()
        element(byText("ДДС-02 г.Черкесск")).should(exist, ofSeconds(waitTime))
        element(byText("AutoTest N 0150 $dateTime")).should(exist, ofSeconds(waitTime))

        tools.logoffTool()
        tools.logonDds()

        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tools.checkbox("Описание", true, waitTime)
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
        element(byText("AutoTest N 0150 $dateTime")).should(exist, ofSeconds(waitTime)).click()
        //устанавливаем статус "Реагирование"
        element(byXpath("//span[text()='В обработке']/parent::button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//span[text()='В обработке']/parent::button")).click()
        element(byXpath("//span[contains(@class,'MuiButton-label')][text()='Реагирование']/parent::button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//span[contains(@class,'MuiButton-label')][text()='Реагирование']/parent::button")).click()
        element(byXpath("//span[text()='В обработке']/parent::button"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//span[contains(@class,'MuiButton-label')][text()='Реагирование']/parent::button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //Thread.sleep(500)

        tools.logoffTool()

        //Возвращаемся в КИАП и проверяем статус родительской карточки
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
        //добавляем в таблицу происшествий столбец "Описание"
        tools.checkbox("Описание", true, waitTime)
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
        element(byText("AutoTest N 0150 $dateTime")).should(exist, ofSeconds(waitTime)).click()
        //проверяем статус родительской карточки
        element(byXpath("//span[contains(@class,'MuiButton-label')][text()='Реагирование']/.."))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))

        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0160`() {
        //A.3.18 Назначение сил и средств ДДС
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
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

        val rndA = (1..20).random()
        //rndA = 0
        if (rndA == 20) {
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

        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0112`() {
        //A.3.19 Убедиться на стороне Системы-112 в наличии возможности назначать   карточку на ЕЦОР  (КИАП) из Системы-112
        //A.3.20 Прием карточки из Системы-112
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        tools.logon112()
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
        element(byXpath("//strong[text()='KИАП-test']/../..//span[text()=' Назначить']")).click()
        //Завершаем обработку
        element(byXpath("//span[text()='Завершить обработку']/..")).click()
        //Вернуться к списку
        element(byCssSelector("button#btnBack>span")).click()
        //"input#street"firstName
        //Thread.sleep(50000)

        tools.logoffTool()

        tools.logonTool()
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
        tools.checkbox("Описание", true, waitTime)
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

        tools.logoffTool()

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0180`() {
        //A.3.21 Просмотр паспортов опасных объектов
        tools.logonTool()
        tools.menuNavigation("Справочники", "Организации", waitTime)
        tools.stringsOnPage(50, waitTime)
        //кликаем по иконке справочников в боковом меню
//        element(byXpath("//span[@title='Справочники']/..")).click()
//        //Переходим в справочник "Организации"
//        element(byXpath("//span[text()='Организации']/../..")).click()
        //ждем загрузки
        element(byCssSelector("tbody>tr"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //ищем столбец "файлы"
//        tools.checkbox("Файлы",true,waitTime)
        tools.checkbox("",true,waitTime)
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

        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0190`() {
        //A.3.23 Проверка формирования отчетов по обращениям
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        tools.logonTool()
        //кликаем по иконке отчетов
        //Переходим в "отчет по обращениям"
        tools.menuNavigation("Отчеты","По обращениям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.23 Проверка формирования отчетов по обращениям $dateTime отсчет")
        //впердоливаем говнокод по преобразование даты
        val dateM = date.split("-")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        //вбиваем адрес
        tools.addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач", waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        element(byXpath("//span[text()='Создать']/..")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("A.3.23 Проверка формирования отчетов по обращениям $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //проверяем и запоминаем общее количество обращений
        val tableSelector = "//table/tbody/tr/td[text()='%s']/following-sibling::td"
        val all = element(byXpath(tableSelector.format("Общее количество вызовов (обращений):"))).ownText.toInt()
        //ложных
        val fal = element(byXpath(tableSelector.format("Ложных"))).ownText.toInt()
        //консультаций
        val con = element(byXpath(tableSelector.format("Консультаций"))).ownText.toInt()
        //по происшествиям
        val inc = element(byXpath(tableSelector.format("По происшествиям"))).ownText.toInt()
        var falD = 0
        var conD =0
        var incD = 0
        //сверим цветастую табличку с диаграммой
        val diagramSelector = "g.recharts-layer.recharts-pie-labels>g:nth-child(%d)>text"
        //если вобще есть диаграмма
        if (all > 0){
            //ложные, если они есть
            if (elements(byCssSelector("text[name='Ложных']")).size == 0){
                falD = element(byCssSelector(diagramSelector.format(1))).ownText.toInt()
                Assertions.assertTrue(fal == falD)
            }
            //консультации, если они есть
            if (elements(byCssSelector("text[name='Консультаций']")).size == 0){
                conD = element(byCssSelector(diagramSelector.format(2))).ownText.toInt()
                Assertions.assertTrue(con == conD)
            }
            //происшествия, если они есть
            if (elements(byCssSelector("text[name='По происшествиям']")).size == 0){
                incD = element(byCssSelector(diagramSelector.format(3))).ownText.toInt()
                Assertions.assertTrue(inc == incD)
            }
        }
        //Сверяем, но возможна ситуация, когда отношение количества каких-то обращений к остальным, мало и обращения есть, а цифры на диаграмме нету, решать будем, когда появится такой отчет
        //рассматриваем таблицу источников (которой может и не существовать)
        var tableAll =0
        var tableVideo = 0
        var tableAIS = 0
        var tableSensor = 0
        var tablePortal = 0
        var tableUIV = 0
//            element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-0']>td[data-colindex='1']>div")).ownText.toInt()
        //println(tableAll)
        if (elements(byXpath("//table/tbody/tr[1]//*[text()='Нет данных']")).size == 0) {
            //всего записей в смысле в графе всего
            tableAll = element(byXpath(tableSelector.format("Всего"))).ownText.toInt()
            //Видеоаналитика из общего числа
            tableVideo = element(byXpath(tableSelector.format("Видеоаналитика"))).ownText.toInt()
            //Внешняя АИС из общего числа
            tableAIS = element(byXpath(tableSelector.format("Внешняя АИС"))).ownText.toInt()
            //Датчик из общего числа
            tableSensor = element(byXpath(tableSelector.format("Датчик"))).ownText.toInt()
            //Портал населения из общего числа
            tablePortal = element(byXpath(tableSelector.format("Портал населения"))).ownText.toInt()
            //Портал УИВ из общего числа
            tableUIV = element(byXpath(tableSelector.format("Портал УИВ"))).ownText.toInt()
        }

        //служебный счетчик, всего строк
//        var tableStringCount = elements(byCssSelector("tr[id^='MUIDataTableBodyRow-']")).size
//        //запоминаем те значения, что будем создавать
//        for (y in 0 until tableStringCount) {
//            if (y!=1){
//            val st: String =
//                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$y']>td[data-colindex='0']>div")).ownText
//            val va: Int =
//                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$y']>td[data-colindex='1']>div")).ownText.toInt()
//            when (st) {
//                "Всего" -> {tableAll = va}
//                "Видеоаналитика" -> {tableVideo = va}
//                "Внешняя АИС" -> {tableAIS = va}
//                "Датчик" -> {tableSensor = va}
//                "Портал населения" -> {tablePortal = va}
//                "Портал УИВ" -> {tableUIV = va}
//            }
//            }
//        }
//        println("all $all")
//        println("fal $fal")
//        println("con $con")
//        println("inc $inc")
//        println("tableAll $tableAll")
//        println("Видеоаналитика $tableVideo")
//        println("Внешняя АИС $tableAIS")
//        println("Датчик $tableSensor")
//        println("Портал населения $tablePortal")
//        println("Портал УИВ $tableUIV")
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var adr: String = ""
        var dateTime2 = ""
        for (i in 1..5) {
            dateTime = LocalDateTime.now().toString()
            date = LocalDate.now().toString()
            //кликаем по иконке происшествий в боковом меню
            //Переходим в "Список происшетвий"
            tools.menuNavigation("Происшествия","Список происшествий",waitTime)
            //кликаем по "создать обращение"
            element(byXpath("//span[text()='Создать обращение']/..")).click()
            //заполняем карточку
            //Источник события - выбираем по порядку
            element(byCssSelector("div#calltype")).click()
            element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($i)")).click()
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
            //Вводим случайный адрес
            var aA = ('A'..'Z').random()
            val bB = (1..100).random()
            if (i != 3){
                tools.addressInput("callAddress", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач $bB", waitTime)
            } else {
                tools.addressInput("callAddress", adr, waitTime)
            }
//            element(byCssSelector("#callAddress"))
//                .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач$bB")
//            //ждем появления списка dadata
//            element(byCssSelector("div.react-dadata__suggestions"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//            //Кликаем на первую строку списка
//            element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current")).click()
            //запоминаем адрес
            //Thread.sleep(500)
            if (i == 2) {
                adr = element(byCssSelector("input#callAddress")).value.toString()
                dateTime2 = dateTime
            }
            //заполняем дополнительную информацию
            //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0190, i=$i $dateTime"
            element(byCssSelector("div[role='textbox']>p")).click()
            element(byCssSelector("div[role='textbox']"))
                .sendKeys("AutoTest N 0190, i=$i $dateTime")
            if (i < 3) {
                //регистрируем обращение
                element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
                //выбираем тип происшествия
                element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test")
                    .sendKeys(Keys.DOWN, Keys.ENTER)
                //Создаем карточку
                element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()
            } else if (i == 3) {
                //регистрируем обращение в ранее созданную карточку.
//                element(byText(adr)).click()
                element(byXpath("//*[@name='refetch']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
//                val troubleshoot = element(byXpath("//*[contains(text(),'$adr')]")).ownText
//                println("troubleshoot = $troubleshoot")
                element(byXpath("//*[contains(text(),'$adr')]/ancestor::button")).click()
                element(byXpath("//span[text()='Привязать к происшествию']/parent::button")).click()
            } else if (i == 4) {
                //регистрируем ложное обращение
                element(byXpath("//*[text()='Ложное обращение']/ancestor::button")).click()
//                element(byXpath("//*[text()='Звонок без информации']/.."))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
                //ждем загрузки таблицы происшествий
                element(byCssSelector("main div table>tbody"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } else if (i == 5) {
                //регистрируем консультацию
                element(byXpath("//span[text()='Консультация']/parent::button")).click()
                //ждем загрузки таблицы происшествий
                element(byCssSelector("main div table>tbody"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            if (i < 4) {
                //Убеждаемся, что нам загрузило созданную карточку
                //проверяя что нам в принципе загрузило какую-то карточку
                element(byCssSelector("#simple-tabpanel-card"))
                    .should(exist, ofSeconds(waitTime))
                //что она в нужном статусе
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldHave(text("В обработке"), ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
//            else {
//                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
//                    .shouldHave(text("Завершена"), ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//            }
            //и что это именно так карточка которую мы только что создали
            if (i < 3) {
                element(byXpath("//strong[text()='Дополнительная информация:']/parent::div"))
                    .shouldHave(text("AutoTest N 0190, i=$i $dateTime"), ofSeconds(waitTime))
            } else if(i==3) {
                element(byXpath("//strong[text()='Дополнительная информация:']/parent::div"))
                    .shouldHave(text("AutoTest N 0190, i=2 $dateTime2"), ofSeconds(waitTime))
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //кликаем по иконке отчетов
        //Переходим в "отчет по обращениям"
        tools.menuNavigation("Отчеты", "По обращениям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button")).click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.23 Проверка формирования отчетов по обращениям $dateTime сверка")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        //вбиваем адрес
        tools.addressInput("address","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("A.3.23 Проверка формирования отчетов по обращениям $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //проверяем и запоминаем общее количество обращений
        val allT = element(byXpath(tableSelector.format("Общее количество вызовов (обращений):"))).ownText.toInt()
        //ложных
        val falT = element(byXpath(tableSelector.format("Ложных"))).ownText.toInt()
        //консультаций
        val conT = element(byXpath(tableSelector.format("Консультаций"))).ownText.toInt()
        //по происшествиям
        val incT = element(byXpath(tableSelector.format("По происшествиям"))).ownText.toInt()
        //сверим цветастую табличку с диаграммой
        //ложные
        if (elements(byCssSelector(diagramSelector.format(1))).size == 1){
            falD = element(byCssSelector(diagramSelector.format(1))).ownText.toInt()
            Assertions.assertTrue(falT == falD)
        }
        //консультации
        if (elements(byCssSelector(diagramSelector.format(2))).size == 1){
            conD = element(byCssSelector(diagramSelector.format(2))).ownText.toInt()
            Assertions.assertTrue(conT == conD)
        }
        //происшествия
        if (elements(byCssSelector(diagramSelector.format(3))).size == 1){
            incD = element(byCssSelector(diagramSelector.format(3))).ownText.toInt()
            Assertions.assertTrue(incT == incD)
        }
//        falD = element(byCssSelector(diagramSelector.format(1))).ownText.toInt()
//        falD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(1)>text")).ownText.toInt()
//        //консультации
//        conD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(2)>text")).ownText.toInt()
//        //происшествия
//        incD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(3)>text")).ownText.toInt()
//        //Сверяем
//        Assertions.assertTrue(falT == falD)
//        Assertions.assertTrue(conT == conD)
//        Assertions.assertTrue(incT == incD)
        //рассматриваем таблицу источников
        //всего записей в смысле в графе всего
        val tableAllT =
            element(byXpath(tableSelector.format("Всего"))).ownText.toInt()
        //println(tableAll)
        //Видеоаналитика из общего числа
        val tableVideoT: Int =
            element(byXpath(tableSelector.format("Видеоаналитика"))).ownText.toInt()
        //Внешняя АИС из общего числа
        val tableAIST: Int =
            element(byXpath(tableSelector.format("Внешняя АИС"))).ownText.toInt()
        //Датчик из общего числа
        val tableSensorT: Int =
            element(byXpath(tableSelector.format("Датчик"))).ownText.toInt()
        //Портал населения из общего числа
        val tablePortalT: Int =
            element(byXpath(tableSelector.format("Портал населения"))).ownText.toInt()
        //Портал УИВ из общего числа
        val tableUIVT: Int =
            element(byXpath(tableSelector.format("Портал УИВ"))).ownText.toInt()
        //служебный счетчик, всего строк
//        tableStringCount = elements(byCssSelector("tr[id^='MUIDataTableBodyRow-']")).size
//        for (u in 2 until tableStringCount) {
//            val st: String =
//                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$u']>td[data-colindex='0']>div")).ownText.toString()
//            val va: Int =
//                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$u']>td[data-colindex='1']>div")).ownText.toInt()
//            when (st) {
//                "Видеоаналитика" -> { tableVideoT = va }
//                "Внешняя АИС" -> { tableAIST = va }
//                "Датчик" -> { tableSensorT = va }
//                "Портал населения" -> { tablePortalT = va }
//                "Портал УИВ" -> { tableUIVT = va }
//            }
//        }

//        println("allT $allT")
//        println("falT $falT")
//        println("conT $conT")
//        println("incT $incT")
//        println("tableAllT $tableAllT")
//        println("ВидеоаналитикаT $tableVideoT")
//        println("Внешняя АИС T $tableAIST")
//        println("ДатчикT $tableSensorT")
//        println("Портал населенияT $tablePortalT")
//        println("Портал УИВ T $tableUIVT")
        //сравниваем старуе значения в старом отчете, со значениями в новом
        Assertions.assertTrue(allT == all + 5)
        Assertions.assertTrue(falT == fal + 1)
        Assertions.assertTrue(conT == con + 1)
        Assertions.assertTrue(incT == inc + 3)
        Assertions.assertTrue(tableAll == all)
        Assertions.assertTrue(tableAllT == allT)
        Assertions.assertTrue(tableAllT == tableAll + 5)
        Assertions.assertTrue(tableVideoT == tableVideo + 1)
        Assertions.assertTrue(tableAIST == tableAIS + 1)
        Assertions.assertTrue(tableSensorT == tableSensor + 1)
        Assertions.assertTrue(tablePortalT == tablePortal + 1)
        Assertions.assertTrue(tableUIVT == tableUIV + 1)
        //Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач
        //Thread.sleep(50000)

        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0200`() {
        //A.3.24 Проверка формирования отчетов по деятельности сотрудников
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        tools.logonTool()
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        tools.menuNavigation("Отчеты", "По сотрудникам", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button")).click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет")
        //впердоливаем говнокод по преобразование даты
        val dateM = date.split("-")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        tools.addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач", waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //убедимся что мы за оператор:
        //кликаем по иконке оператора сверху справа
        //element(byCssSelector("header>div>div>div>span>button")).click()
        element(byXpath("//span[contains(text(),'.sizov')]/parent::button")).click()
        //пероеходим в профиль пользователя
        element(byCssSelector("a[href='/profile']>button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("a[href='/profile']>button")).click()
        //Извлекаем имя оператора
        element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //val operator = element(byCssSelector("main>div:nth-child(3)>div:nth-child(3)>p")).ownText
        val operator = element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p")).ownText.trim()
//        val operatorMas = operator.split("\n")
//        val operatorFIO = operatorMas[1].trim()
//        val operatorFIO = operator.trim()
//        println("ФИО $operator")
//        println("operatorMas $operatorMas")
//        println("operatorFIO $operatorFIO")
        back()
        //$$("tr[data-testid^=MUIDataTableBodyRow-1]>td[data-testid='MuiDataTableBodyCell-0-1']>div:nth-child(2)") - фио в отчете
        //ждем
        element(byXpath("//h6[contains(text(),'по муниципальному образованию')]/../h5"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            .shouldHave(text("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет"))
        //служебный счетчик
        var first = 0
        var second = 0
        var third = 0
        var fourth = 0
        var fifth = 0
        var sixth = 0
        var seventh = 0
        var operators = elements(byXpath("//tbody/tr/td")).size
        for (w in 1..operators) {
            val momentOperator =
                element(byXpath("//tbody/tr[$w]/td[1]")).ownText.trim()
//            println("0 momentOperator $momentOperator")
            if (operator == momentOperator) {
                //поехали запоминать значения в отчете для оператора
                val operatorDataSelector = "//tbody/tr[$w]/td[%d]"
                first = element(byXpath(operatorDataSelector.format(3))).ownText.toInt()
                second = element(byXpath(operatorDataSelector.format(4))).ownText.toInt()
                third = element(byXpath(operatorDataSelector.format(5))).ownText.toInt()
                fourth = element(byXpath(operatorDataSelector.format(6))).ownText.toInt()
                fifth = element(byXpath(operatorDataSelector.format(7))).ownText.toInt()
                sixth = element(byXpath(operatorDataSelector.format(8))).ownText.toInt()
                seventh = element(byXpath(operatorDataSelector.format(9))).ownText.toInt()
                break
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var adr: String = ""
        var dateTime2 = ""
        for (i in 1..6) {
            dateTime = LocalDateTime.now().toString()
            date = LocalDate.now().toString()
            //кликаем по иконке происшествий в боковом меню
            //Переходим в "Список происшетвий"
            tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
            //кликаем по "создать обращение"
            element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
            //заполняем карточку
            //Источник события - выбираем случайно
            element(byCssSelector("div#calltype")).click()
            var iI = (1..10).random()
            element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($iI)")).click() //не случайно =)
            //element(byCssSelector("li[role='option'][data-value='cd73ccc0-e740-4c5d-98ec-28bbe9a13be0']")).click()
            //Номер телефона
            if (elements(byCssSelector("#phone")).size > 0){
                val tel = (1000000..9999999).random()
                element(byCssSelector("#phone")).sendKeys("918$tel")
            }
            //ФИО
            if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
                && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
                element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname")
                element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
            }
            //Вводим случайный адрес
            var aA = ('A'..'Z').random()
            var bB = (1..100).random()
            if (i != 4){
                tools.addressInput("callAddress", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач $bB", waitTime)
            } else {
                tools.addressInput("callAddress", adr, waitTime)
            }

//            element(byCssSelector("#callAddress"))
//                .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач$bB")
//            //ждем появления списка dadata
//            element(byCssSelector("div.react-dadata__suggestions"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//            //Кликаем на первую строку списка
//            element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current")).click()
            //запоминаем адрес
            //Thread.sleep(500)
            if (i == 3) {
                adr = element(byCssSelector("input#callAddress")).value.toString()
                dateTime2 = dateTime
            }
            //заполняем дополнительную информацию
            //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0190, i=$i $dateTime"
            element(byCssSelector("div[role='textbox']>p")).click()
            element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest N 0200, i=$i $dateTime")
            if (i < 4) {
                //регистрируем обращение
                element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
                //выбираем тип происшествия
                element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test")
                    .sendKeys(Keys.DOWN, Keys.ENTER)
                //Создаем карточку
                element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()
            } else if (i == 4) {
                //регистрируем обращение в ранее созданную карточку.
                element(byXpath("//*[@name='refetch']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byText(adr))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//span[text()='Привязать к происшествию']/parent::button")).click()
            } else if (i == 5) {
                //регистрируем ложное обращение
                element(byXpath("//*[text()='Ложное обращение']/ancestor::button")).click()
//                element(byXpath("//*[text()='Звонок без информации']/.."))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
                //ждем загрузки таблицы происшествий
                element(byCssSelector("main div table>tbody"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } else if (i == 6) {
                //регистрируем консультацию
                element(byXpath("//span[text()='Консультация']/parent::button")).click()
                //ждем загрузки таблицы происшествий
                element(byCssSelector("main div table>tbody"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //Убеждаемся, что нам загрузило созданную карточку
            //проверяя что нам в принципе загрузило какую-то карточку (кроме случаев ложного и консультации)
            if (i<5) {
                element(byCssSelector("#simple-tabpanel-card"))
                    .should(exist, ofSeconds(waitTime))
            }
            //что она в нужном статусе
            if (i < 4) {
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldHave(text("В обработке"), ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } else if (i == 4) {
                //закрываем одну карточку
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldHave(text("В обработке"), ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']")).click()
                element(byXpath("//span[text()='Закрыта']/parent::button"))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//span[text()='Закрыта']/parent::button")).click()
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldHave(text("Закрыта"), ofSeconds(waitTime))
            }
//            это нужно было пока создание ложной и консультации перекидывало в КП, а не в список КП
//            else {
//                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
//                    .shouldHave(text("Завершена"), ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//            }
            //и что это именно так карточка которую мы только что создали
            if (i < 4) {
                element(byXpath("//strong[text()='Дополнительная информация:']/parent::div"))
                    .shouldHave(text("AutoTest N 0200, i=$i $dateTime"), ofSeconds(waitTime))
            } else if (i==4) {
                element(byXpath("//strong[text()='Дополнительная информация:']/parent::div"))
                    .shouldHave(text("AutoTest N 0200, i=3 $dateTime2"), ofSeconds(waitTime))
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Thread.sleep(500)
        //ждем загрузки таблицы происшествий
//        element(byCssSelector("main div table>tbody"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        tools.menuNavigation("Отчеты", "По сотрудникам", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title")).sendKeys("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        tools.addressInput("address","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
//        //убедимся что мы за оператор:
//        //кликаем по иконке оператора сверху справа
//        element(byCssSelector("header>div>div>div>span>button")).click()
//        //пероеходим в профиль пользователя
//        element(byCssSelector("a[href='/profile']>button"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("a[href='/profile']>button")).click()
//        element(byCssSelector("main>div:nth-child(3)>div:nth-child(3)>p"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
////        val operator = element(byCssSelector("main>div:nth-child(3)>div:nth-child(3)>p")).ownText
////        val operatorMas = operator.split("\n")
////        val operatorFIO = operatorMas[1].toString()
//        //println("ФИО ${operator[1]}")
//        //println(operatorFIO)
//        back()
//        //$$("tr[data-testid^=MUIDataTableBodyRow-1]>td[data-testid='MuiDataTableBodyCell-0-1']>div:nth-child(2)") - фио в отчете
        //ждем
        element(byXpath("//h6[contains(text(),'по муниципальному образованию')]/../h5"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            .shouldHave(text("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка"))
        //служебный счетчик числа операторов
        operators = elements(byXpath("//tbody/tr/td")).size
        var firstT = 0
        var secondT = 0
        var thirdT = 0
        var fourthT = 0
        var fifthT = 0
        var sixthT = 0
        var seventhT = 0
        //ищем кто в списке мы
        for (e in 1..operators) {
            val momentOperator =
                element(byXpath("//tbody/tr[$e]/td[1]")).ownText.trim()
            if (operator == momentOperator) {
                val operatorDataSelector = "//tbody/tr[$e]/td[%d]"
                //поехали запоминать значения в отчете для оператора
                firstT = element(byXpath(operatorDataSelector.format(3))).ownText.toInt()
                secondT = element(byXpath(operatorDataSelector.format(4))).ownText.toInt()
                thirdT = element(byXpath(operatorDataSelector.format(5))).ownText.toInt()
                fourthT = element(byXpath(operatorDataSelector.format(6))).ownText.toInt()
                fifthT = element(byXpath(operatorDataSelector.format(7))).ownText.toInt()
                sixthT = element(byXpath(operatorDataSelector.format(8))).ownText.toInt()
                seventhT = element(byXpath(operatorDataSelector.format(9))).ownText.toInt()
                break
            }
        }
//        println("first $first")
//        println("second $second")
//        println("third $third")
//        println("fourth $fourth")
//        println("fifth $fifth")
//        println("sixth $sixth")
//        println("seventh $seventh")
//        println("firstT $firstT")
//        println("secondT $secondT")
//        println("thirdT $thirdT")
//        println("fourthT $fourthT")
//        println("fifthT $fifthT")
//        println("sixthT $sixthT")
//        println("seventhT $seventhT")
        Assertions.assertTrue(firstT == first + 5)
        Assertions.assertTrue(thirdT == third + 2)
        Assertions.assertTrue(fourthT == fourth + 2)
        Assertions.assertTrue(fifthT == fifth + 1)
        Assertions.assertTrue(sixthT == sixth + 1)
        Assertions.assertTrue(seventhT == seventh + 1)

        //Thread.sleep(50000)
        tools.logoffTool()

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0210`() {
        //A.3.25 Проверка формирования отчетов по происшествиям
        //просто кошмар какой объемный тест получился(
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        tools.logonTool()
        //кликаем по иконке отчетов
        //Переходим в "отчет По происшествиям"
        tools.menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime отсчет")
        //впердоливаем говнокод по преобразование даты
        val dateM = date.split("-")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        tools.addressInput("address","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем заголовок таблицы
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime отсчет']"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //Запоминаем значения отчета
        //значения в легенде
        val colorTableSelector = "//table/tbody/tr/td[text()='%s']/following-sibling::td"
//        val legendSelector = "table[aria-label='simple table'] tr:nth-child(%d)>td:nth-child(2)"
        val legendAllBefore = element(byXpath(colorTableSelector.format("Всего"))).ownText.toInt()
        val legendNewBefore = element(byXpath(colorTableSelector.format("Новые"))).ownText.toInt()
        val legendProcessingBefore = element(byXpath(colorTableSelector.format("В обработке"))).ownText.toInt()
        val legendReactionBefore = element(byXpath(colorTableSelector.format("Реагирование"))).ownText.toInt()
        val legendDoneBefore = element(byXpath(colorTableSelector.format("Завершены"))).ownText.toInt()
        val legendCanselBefore = element(byXpath(colorTableSelector.format("Отменены"))).ownText.toInt()
        val legendCloseBefore = element(byXpath(colorTableSelector.format("Закрыты"))).ownText.toInt()
        //значения в таблице
        val oldIncidentTypeList = mutableListOf<String>()
        val oldAmountIncidentList = mutableListOf<Int>()
        val oldAmountAffectedPeopleList = mutableListOf<Int>()
        val oldAmountAffectedChildrenList = mutableListOf<Int>()
        val oldAmountDiePeopleList = mutableListOf<Int>()
        val oldAmountDieChildrenList = mutableListOf<Int>()
        val tableSelector = "//table[@aria-label='sticky table']/tbody/tr[%d]/td[%d]"
        //table[@aria-label='sticky table']/tbody/tr
        var oldTableStringCount = 0
        if (elements(byXpath("//table/tbody/tr[1]//*[text()='Нет данных']")).size == 0) {
            oldTableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
            for (i in 0 until oldTableStringCount) {
                oldIncidentTypeList.add(i, element(byXpath(tableSelector.format(i + 1, 1))).ownText)
                oldAmountIncidentList.add(i, element(byXpath(tableSelector.format(i + 1, 2))).ownText.toInt())
                oldAmountAffectedPeopleList.add(i, element(byXpath(tableSelector.format(i + 1, 3))).ownText.toInt())
                oldAmountAffectedChildrenList.add(i, element(byXpath(tableSelector.format(i + 1, 4))).ownText.toInt())
                oldAmountDiePeopleList.add(i, element(byXpath(tableSelector.format(i + 1, 5))).ownText.toInt())
                oldAmountDieChildrenList.add(i, element(byXpath(tableSelector.format(i + 1, 6))).ownText.toInt())
            }
        }
        val oldIncidentTypeListCount: Int = oldIncidentTypeList.count()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Создаем карточки происшествий
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //если отчет отсчета был пуст, или имел меньше шести строк, то при создании КП тип происшествия кладем сюда
        val shortSelectedIncidentTypeList = mutableListOf<String>()
        //тип создаваемого происшествия выбираем "случайно": это последовательные значения стартующие от случайной величины
        val rnd = (2..30).random()
        //создаем семь происшествий, одно из которых не попадет в отчет, т.к. идет по другому населенному пункту (а адрес при создании отчета мы закладываем)
        for (i in 1..7) {
            dateTime = LocalDateTime.now().toString()
            date = LocalDate.now().toString()
            //кликаем по иконке происшествий в боковом меню
            //Переходим в "Список происшетвий"
            tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
            //кликаем по "создать обращение"
            element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
            //заполняем карточку
            //Источник события - выбираем случайно
            element(byCssSelector("div#calltype")).click()
            val listbox = (1..10).random()
            element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($listbox)")).click()
            //element(byCssSelector("li[role='option'][data-value='cd73ccc0-e740-4c5d-98ec-28bbe9a13be0']")).click()//это фиксированное значение "факс"
            //Номер телефона
            if (elements(byCssSelector("#phone")).size > 0){
                val tel = (1000000..9999999).random()
                element(byCssSelector("#phone")).sendKeys("918$tel")
            }
            //ФИО
            if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
                && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
                element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname")
                element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
            }
            //Вводим случайный адрес
//                val randomLetter = ('A'..'Z').random()
            val randomNumber = (1..100).random()
            //Вбиваем первый символ
            if (i==4){
                tools.addressInput("callAddress","Карачаево-Черкесская Респ, г Усть-Джегута, ул Мира $randomNumber",waitTime)
            } else {
                tools.addressInput("callAddress","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач $randomNumber",waitTime)
            }
            //заполняем дополнительную информацию
            //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0190, i=$i $dateTime"
            element(byCssSelector("div[role='textbox']>p")).click()
            element(byCssSelector("div[role='textbox']"))
                .sendKeys("AutoTest N 0210, i=$i $dateTime")
            //регистрируем обращение
            //element(byXpath("//*[@id=\"skeleton\"]/div/main/div[3]/div[1]/div/form/div[9]/div[1]/button")).click()
            element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
            //ждем поле выбора происшествия
            element(byCssSelector("input#incidentTypeId-autocomplete"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //выбираем тип происшествия
            //Сначала создаем происшествия с теми же типами, что были в первом отчете, если они были
            if (oldIncidentTypeListCount >= i && i<7){
                element(byCssSelector("input#incidentTypeId-autocomplete"))
                    .sendKeys(oldIncidentTypeList[i-1])
                element(byXpath("//div[@role='presentation']//ul/li[last()]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
//                element(byCssSelector("input#incidentTypeId-autocomplete"))
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//                Thread.sleep(10000)
//            if (oldIncidentTypeListCount > 5 && i == 6) {
//                //(oldIncidentTypeListCount >= i && i<7)
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[5])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 4 && i == 5) {
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[4])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 3 && i == 4) {
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[3])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 2 && i == 3) {
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[2])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 1 && i == 2) {
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[1])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 0 && i == 1) {
//                //element(byCssSelector("input#incidentTypeId")).setValue("${incidentTypeList[0]}")
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[0])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
                //контрольное, заренее известное по типу, происшествие
            } else if(i == 7){
                element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test")
                    .sendKeys(Keys.DOWN, Keys.ENTER)
                //случайные типы происшествий на те первые 6 позиций отчета, которых в первом отчете могло не быть
            } else {
                //кликаем по типу происшествия
//                element(byCssSelector("input#incidentTypeId")).click()
//                //случайное число раз жмем вниз, выбирая тип происшествия
//                repeat(rnd+i){
//                    element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.DOWN)
//                }
//                element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.ENTER)
//                tools.inputRandom("incidentTypeId")
                tools.inputRandomNew("incidentTypeId-textfield", false, waitTime)
                element(byCssSelector("input#incidentTypeId-autocomplete[value*='.']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //убеждаемся что такого происшествия нет в первом отчете и если есть, то выбираем другое
                var longSelectedIncidentType = element(byCssSelector("input#incidentTypeId-autocomplete")).getAttribute("value").toString()
                var shortSelectedIncidentType = longSelectedIncidentType.substring(longSelectedIncidentType.indexOf(' ') + 1)
                while (oldIncidentTypeList.contains(shortSelectedIncidentType)
                    || shortSelectedIncidentTypeList.contains(shortSelectedIncidentType)
                    || shortSelectedIncidentType=="Auto-Test")
                {
                    repeat(longSelectedIncidentType.length)
                        {element(byCssSelector("input#incidentTypeId-autocomplete")).sendKeys(Keys.BACK_SPACE) }
//                    tools.inputRandom("incidentTypeId")
                    tools.inputRandomNew("incidentTypeId-textfield", false, waitTime)
                    longSelectedIncidentType = element(byCssSelector("input#incidentTypeId-autocomplete")).getAttribute("value").toString()
                    shortSelectedIncidentType = longSelectedIncidentType.substring(longSelectedIncidentType.indexOf(' ') + 1)

//                    element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.DOWN)
//                    element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.DOWN)
//                    //убеждаемся что не переклацали список попав на невводимое значение между началом и концом
//                    if (elements(byCssSelector("input[name='incidentTypeId'][aria-activedescendant^='incidentTypeId-option']")).size ==0){
//                        element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.DOWN)
//                    }
//                    element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.ENTER)
//                    longSelectedIncidentType = element(byCssSelector("input#incidentTypeId")).getAttribute("value").toString()
//                    shortSelectedIncidentType = longSelectedIncidentType.substring(longSelectedIncidentType.indexOf(' ') + 1)
                }
                //кладем созданные происшествия в список
                shortSelectedIncidentTypeList.add(
                    index = (i-oldIncidentTypeListCount-1),
                    element = shortSelectedIncidentType)
            }
            //указываем пострадавших
            val affectedSelector = "div[role='region']>div>div>div:first-child>div:nth-child(%d) input"
            if (i!=7 && i!=4){element(byCssSelector("input#isThreatPeople")).click()
                when (i) {
                    1 -> {element(byCssSelector(affectedSelector.format(1))).sendKeys("3") }
                    2 -> {element(byCssSelector(affectedSelector.format(1))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(2))).sendKeys("3")}
                    3 -> {element(byCssSelector(affectedSelector.format(3))).sendKeys("3") }
                    6 -> {element(byCssSelector(affectedSelector.format(3))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(4))).sendKeys("3") }
                    5 -> {element(byCssSelector(affectedSelector.format(1))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(2))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(3))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(4))).sendKeys("3")
                    }
                }
//                for (e in 1 until i){element(byCssSelector("div[role='region']>div>div>div:first-child>div:nth-child($e) input")).setValue("3")}//альтернатива блоку выше, но не создающая происшествие только с погибшими
            }
            //Создаем карточку
            element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()
            //Убеждаемся, что нам загрузило созданную карточку
            //проверяя что нам в принципе загрузило какую-то карточку

            element(byCssSelector("#simple-tabpanel-card"))
                .should(exist, ofSeconds(waitTime))
            //что она в нужном статусе
            element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                .shouldHave(text("В обработке"), ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //и что это именно так карточка которую мы только что создали
            element(byCssSelector("div#panel1a-content div[style='gap: 16px 0px;']>div:nth-child(5)"))
                .shouldHave(text("AutoTest N 0210, i=$i $dateTime"), ofSeconds(waitTime))
            //меняем статус карточки
            //стату "В обработке" не кликабелен для карточки в этом статусе.
            //попытка установить статус "Новая" может сработать, а может и не сработать, что ломает логику теста
            if ((i in (2..6)) && (i != 4)) {
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']")).click()
                element(byCssSelector("div.MuiPaper-rounded>div.MuiGrid-spacing-xs-1>div:nth-child($i)>button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byCssSelector("div.MuiPaper-rounded>div.MuiGrid-spacing-xs-1>div:nth-child($i)>button")).click()
                //ждем загрузки нового статуса, а точнее есчезновения статуса "В обработке"
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldNotHave(text("В обработке"), ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //создаем первый контрольный отчет
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        tools.menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime сверка")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        tools.addressInput("address","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(
                visible,
                ofSeconds(waitTime)
            ).click()
        //ждем заголовок
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime сверка']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Запоминаем значения отчета
        //значения в легенде
        val legendAllAfter = element(byXpath(colorTableSelector.format("Всего"))).ownText.toInt()
        val legendNewAfter = element(byXpath(colorTableSelector.format("Новые"))).ownText.toInt()
        val legendProcessingAfter = element(byXpath(colorTableSelector.format("В обработке"))).ownText.toInt()
        val legendReactionAfter = element(byXpath(colorTableSelector.format("Реагирование"))).ownText.toInt()
        val legendDoneAfter = element(byXpath(colorTableSelector.format("Завершены"))).ownText.toInt()
        val legendCanselAfter = element(byXpath(colorTableSelector.format("Отменены"))).ownText.toInt()
        val legendCloseAfter = element(byXpath(colorTableSelector.format("Закрыты"))).ownText.toInt()
        //значения в таблице
        val newTableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
        val newIncidentTypeList = mutableListOf<String>()
        val newAmountIncidentList = mutableListOf<Int>()
        val newAmountAffectedPeopleList = mutableListOf<Int>()
        val newAmountAffectedChildrenList = mutableListOf<Int>()
        val newAmountDiePeopleList = mutableListOf<Int>()
        val newAmountDieChildrenList = mutableListOf<Int>()
        for (i in 0 until newTableStringCount) {
            newIncidentTypeList.add(i, element(byXpath(tableSelector.format(i + 1, 1))).ownText)
            newAmountIncidentList.add(i, element(byXpath(tableSelector.format(i + 1, 2))).ownText.toInt())
            newAmountAffectedPeopleList.add(i, element(byXpath(tableSelector.format(i + 1, 3))).ownText.toInt())
            newAmountAffectedChildrenList.add(i, element(byXpath(tableSelector.format(i + 1, 4))).ownText.toInt())
            newAmountDiePeopleList.add(i, element(byXpath(tableSelector.format(i + 1, 5))).ownText.toInt())
            newAmountDieChildrenList.add(i, element(byXpath(tableSelector.format(i + 1, 6))).ownText.toInt())
        }
//            var newIncidentTypeListCount: Int = newIncidentTypeList.count()
        //значения в диаграмме
//        var diagNew = 0
//        val diagSelector = "svg.recharts-surface>g>g.recharts-pie-labels>g:nth-child(%d)>text"
        //сравниваем новую диаграмму со старой легендой, учитывая созданные КП. "Новые" происшествия не рассматриваем
        //т.к. наличие цифры на диаграмме зависит от соотношения количества разных карточек, перед сравнением диаграммы убеждаемся, что вобще есть что сравнивать
        val diagSelector = "g.recharts-pie>g.recharts-pie-labels>g:nth-child(%d)>text"
//        val noDigitDiagSelector = "g.recharts-pie>g.recharts-pie-labels>g:nth-child(%s)>text>tspan"
        //diagNew = element(byCssSelector("svg.recharts-surface>g>g.recharts-pie-labels>g:nth-child(1)>text")).ownText.toInt()
        var diagProcessing = 0
        var diagReaction = 0
        var diagDone = 0
        var daigCansel = 0
        var daagClose = 0
        for (i in 2..6){
            if (element(byCssSelector(diagSelector.format(i))).getCssValue("stroke") == "white"){
                when(i){
                    2 -> { diagProcessing = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(diagProcessing == (legendProcessingBefore + 2)) }
                    3 -> { diagReaction = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(diagReaction == (legendReactionBefore + 1)) }
                    4 -> { diagDone = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(diagDone == (legendDoneBefore + 1)) }
                    5 -> {daigCansel = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(daigCansel == (legendCanselBefore + 1))}
                    6 -> {daagClose = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(daagClose == (legendCloseBefore + 1))}
                }
            }
        }
//        if (elements(byXpath(diagSelector.format(2))).size == 1){
//            diagProcessing = element(byXpath(diagSelector.format(2))).ownText.toInt()
//            Assertions.assertTrue(diagProcessing == (legendProcessingBefore + 2))
//        }
//        if (elements(byXpath(diagSelector.format(3))).size == 1){
//            diagReaction = element(byXpath(diagSelector.format(3))).ownText.toInt()
//            Assertions.assertTrue(diagReaction == (legendReactionBefore + 1))
//        }
//        if (elements(byXpath(diagSelector.format(4))).size == 1){
//            diagDone = element(byXpath(diagSelector.format(4))).ownText.toInt()
//            Assertions.assertTrue(diagDone == (legendDoneBefore + 1))
//        }
//        if (elements(byXpath(diagSelector.format(5))).size == 1){
//            daigCansel = element(byXpath(diagSelector.format(5))).ownText.toInt()
//            Assertions.assertTrue(daigCansel == (legendCanselBefore + 1))
//        }
//        if (elements(byXpath(diagSelector.format(6))).size == 1){
//            daagClose = element(byXpath(diagSelector.format(6))).ownText.toInt()
//            Assertions.assertTrue(daagClose == (legendCloseBefore + 1))
//        }
        //сравниваем две леганды - старую и новую
        Assertions.assertTrue(legendAllAfter == (legendAllBefore + 6))
        Assertions.assertTrue(legendNewBefore == legendNewAfter)
        Assertions.assertTrue(legendProcessingAfter == (legendProcessingBefore + 2))
        Assertions.assertTrue(legendReactionAfter == (legendReactionBefore + 1))
        Assertions.assertTrue(legendDoneAfter == (legendDoneBefore + 1))
        Assertions.assertTrue(legendCanselAfter == (legendCanselBefore + 1))
        Assertions.assertTrue(legendCloseAfter == (legendCloseBefore + 1))
        val deltaAffectedPeopleList = mutableListOf(3, 3, 0, 0, 3, 0)
        val deltaAffectedChildrenList = mutableListOf(0, 3, 0, 0, 3, 0)
        val deltaDiePeopleList = mutableListOf(0, 0, 3, 0, 3, 3)
        val deltaDieChildrenList = mutableListOf(0, 0, 0, 0, 3, 3)
        for (i in 0..5) {
            //проводим проверку в таблице для типов происшествий существовавших в первом отчете
            if ((oldTableStringCount > i) && (i != 3)) {
                newIncidentTypeList.forEachIndexed { newListIndex, newValue ->
                    //Учитываем, что обязательно создаваемое происшествие Auto-Test могло существовать в первом отчете
                    if (oldIncidentTypeList[i] == newValue) {
//                        println("i = $i, oldIncidentTypeList[i] ${oldIncidentTypeList[i]}")
                        if (oldIncidentTypeList[i] != "Auto-Test") {
//                            println("oldAmountIncidentList[i] ${oldAmountIncidentList[i]} +1 == ${newAmountIncidentList[newListIndex]}")
                            Assertions.assertTrue((oldAmountIncidentList[i] + 1) == newAmountIncidentList[newListIndex])
                        } else {
//                            println("oldAmountIncidentList[i] ${oldAmountIncidentList[i]} +2 == ${newAmountIncidentList[newListIndex]}")
                            Assertions.assertTrue((oldAmountIncidentList[i] + 2) == newAmountIncidentList[newListIndex])
                        }
                        Assertions.assertTrue((oldAmountAffectedPeopleList[i] + deltaAffectedPeopleList[i]) == newAmountAffectedPeopleList[newListIndex])
                        Assertions.assertTrue((oldAmountAffectedChildrenList[i] + deltaAffectedChildrenList[i]) == newAmountAffectedChildrenList[newListIndex])
                        Assertions.assertTrue((oldAmountDiePeopleList[i] + deltaDiePeopleList[i]) == newAmountDiePeopleList[newListIndex])
                        Assertions.assertTrue((oldAmountDieChildrenList[i] + deltaDieChildrenList[i]) == newAmountDieChildrenList[newListIndex])
                    }
                }
                //проводим проверку в таблице для типов созданных происшествий, при том надо учесть возможность того, что созданное происшествие чей тип случайный, не совпало с происшествием которое было в первом отчете //учтено при создании КП
            } else if ((i >= oldTableStringCount) && (i != 3)){
                newIncidentTypeList.forEachIndexed { newListIndex, newValue ->
                    if (shortSelectedIncidentTypeList[i-oldTableStringCount] == newValue) {
                        Assertions.assertTrue(newAmountIncidentList[newListIndex]==1)
                        Assertions.assertTrue(deltaAffectedPeopleList[i] == newAmountAffectedPeopleList[newListIndex])
                        Assertions.assertTrue(deltaAffectedChildrenList[i] == newAmountAffectedChildrenList[newListIndex])
                        Assertions.assertTrue(deltaDiePeopleList[i] == newAmountDiePeopleList[newListIndex])
                        Assertions.assertTrue(deltaDieChildrenList[i] == newAmountDieChildrenList[newListIndex])
                    }
                }
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//понеслись создавать отчеты по пострадавшим
//создаем отчет с указанием максимума из имеющихся пострадавших, и ждем соответственно 0 строк
        val maximumAffectedPeople = newAmountAffectedPeopleList.maxOrNull()
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        tools.menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        tools.addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
        //вбиваем число пострадавших соответствующее максимальному числу второго отчета, ждем 0 строк потом
        element(byXpath("//label[text()='Число пострадавших, более']/following-sibling::div/input"))
            .sendKeys(maximumAffectedPeople.toString())
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем заголовок
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки в таблице, их должно быть 0
        var tableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
//        Assertions.assertTrue(tableStringCount==0)
        element(byXpath("//table[@aria-label='sticky table']/tbody/tr/td//*[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//строим отчет с числом пострадавших на 1 меньше и ожидаем, что записей будет столько сколько должно быть)
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        tools.menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max -1")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        tools.addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
        //вбиваем число пострадавших соответствующее максимальному числу второго отчета, ждем строк более 0 потом
        element(byXpath("//label[text()='Число пострадавших, более']/following-sibling::div/input")).sendKeys((maximumAffectedPeople?.minus(1)).toString())
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max -1"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем заголовок
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max -1']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки, и ожидаем что их столько же, сколько наибольшего числа в массиве пострадавших
        val amountMaxAffected = newAmountAffectedPeopleList.filter { it == maximumAffectedPeople}
        tableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
        Assertions.assertTrue(amountMaxAffected.size == tableStringCount)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//создаем отчет с указанием типа происшествия и просто проверяем, что строка одна
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        tools.menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime Тип происшествия")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        tools.addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
        //вбиваем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete"))
            .sendKeys(newIncidentTypeList[0])
        element(byCssSelector("input#incidentTypeId-autocomplete"))
            .sendKeys(Keys.DOWN, Keys.ENTER)
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime Тип происшествия"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем заголовок
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime Тип происшествия']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
        Assertions.assertTrue(tableStringCount == 1)

        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0220`() {
        //A.3.28 Проверка централизованного хранения и управления структурированной справочной информации
        tools.logonTool()

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
        //Thread.sleep(5000)
        }
    tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, dataProvider = "Справочники")
    fun `N 0230`(subMenu: String) {
        //A.3.29 Проверка поиска справочных данных
        //хорошо бы в таблицу включать все столбцы, но это потом доработаю //доработано
        tools.logonTool()
        //временно не проверяем поиск по справочнику типов происшествий т.к. не можем там контролировать число строк
        //(ну как бы можем, но не по такому алгоритму, а вероятно в отдельном тесте. еще подумаю...)
//        for (dicts in 1..10) {
//            //кликаем по иконке справочников
//            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul")).click()
//            //переходим в каждый справочник
//            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div/ul[$dicts]")).click()
        tools.menuNavigation("Справочники", subMenu, waitTime)
            //ждем загрузки таблицы
            element(byCssSelector("main table>tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //добавляем все доступные колонки в таблицу
            tools.checkbox("", true, waitTime)
//            element(byCssSelector("button[data-testid='Колонки-iconButton']")).click()
//            element(byCssSelector("fieldset[aria-label='Показать/скрыть колонки']"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//            val countString = elements(byCssSelector("fieldset label input")).size
//            for (w in 1..countString){
//                val d = element(byXpath("//fieldset//label[$w]//input/following-sibling::*[name()='svg']/*[name()='path']")).getAttribute("d")
//                //val b = element(byXpath("//fieldset//label[$i]//input/following-sibling::*[name()='svg']/*[name()='path']")).getCssValue("d")
//                if (d == checkboxFalse){
//                    element(byXpath("//fieldset//label[$w]//input")).click()
//                    element(byXpath("//fieldset//label[$w]//input/following-sibling::*[name()='svg']/*[name()='path']"))
//                        .shouldHave(attribute("d", checkboxTrue))
//                }
//            }
//            element(byXpath("//fieldset[@aria-label='Показать/скрыть колонки']/../button")).click()
            //получаем счетчик строк в левом нижнем углу страницы, в виде числа
            var allRecordCountUse: Int
            if (subMenu != "Типы происшествий") {
                val allRecordCountString =
                    element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]")).ownText.toString().split("\n")
                val allRecordCountNotUse = allRecordCountString[1].split(" ")
                allRecordCountUse = allRecordCountNotUse[0].toInt()
            } else {
                allRecordCountUse = elements(byXpath("//tbody/tr")).size
            }
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
            val searchHintString = element(byCssSelector("input[placeholder]")).getAttribute("placeholder")
            val searchHintList = searchHintString?.split(", ")
//            println("searchHintList $searchHintList")
            //проходимся по заголовкам столбцов, сверяя их с каждой позицией подсказки
            var searchValue = ""
            for (col in 1 until comumnCount) {
//                var hierarchy: Boolean
                element(byXpath("//table/thead/tr/th[$col]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //если столбец, кнопка без текста, то жмем её и переходим к другим столбцам
                // (по идее это только иерархический столбец)
                if ((elements(byXpath("//table/thead/tr/th[$col]//*[text()]")).size == 0)
                    && (elements(byXpath("//table/thead/tr/th[$col]//button")).size == 1)){
                    element(byXpath("//table/thead/tr/th[$col]//button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byCssSelector("thead>tr>th svg[name='arrowDown']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
//                    hierarchy = true
                    if (subMenu == "Типы происшествий"){
                        allRecordCountUse = elements(byXpath("//tbody/tr")).size
                    }
                }
                //проверяем существует ли заголовок столбца, и если существует, то:
//                if (elements(byXpath("//table/thead/tr/th[$q]//*[text()]")).size == 1){
                else {
//                    hierarchy = false
                    val columnName = element(byXpath("//table/thead/tr/th[$col]//*[text()]")).ownText.toString()
    //                println("columnName $columnName")
                    for (hint in 0 until searchHintList!!.size){
//                    searchHintList?.forEach { it ->
    //                    println("$i searchHintList $it")
                        //если заголовок столбца совпал с подсказкой, то вбиваем значение этого столбца из каждой строки в список из которого выберем случайное значение для поиска, ждем что нижний счетчик строк будет строго меньше исходного
//                        println("i = $dicts columnName = $columnName подсказка = $it")
                        if (columnName.contains(searchHintList[hint])
                            || ((columnName == "Телефонный код") && (searchHintList[hint] == "Тел.код"))
                            || ((columnName == "Метка") && (searchHintList[hint] == "Имя метки"))
                            || ((columnName == "№") && (searchHintList[hint] == "Номер пункта"))
                        ) {
//                            println("i = $dicts true columnName = $columnName подсказка = ${searchHintList[hint]}")
                            //искомое значение определяем случайно, среди имеющихся но с типами происшествий и откидыванием пустых значений других справочников придется возится
                            var maxRandom: Int = 1
                            var randomColumnValue = 0
                            if (subMenu != "Типы происшествий") {
                                val countAllString = elements(byXpath("//table/tbody/tr")).size
                                val trueValueList = mutableListOf<String>()
                                for (str in 1..countAllString) {
    //                                if (elements(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).size == 1) {
    //                                    trueValueList.add(element(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).ownText.trim())
    //                                }
    //                                if (element(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).ownText.trim().isNotEmpty()
    //                                    && elements(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).size > 0) {
    //                                    trueValueList.add(element(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).ownText.trim())
    //                                }
                                    if ((elements(byXpath("//table/tbody/tr[$str]/td[$col][text()]")).size == 1)
                                        && (element(byXpath("//table/tbody/tr[$str]/td[$col][text()]")).ownText.trim()
                                            .isNotEmpty())) {
                                            trueValueList.add(element(byXpath("//table/tbody/tr[$str]/td[$col][text()]")).ownText.trim())
                                    } else if ((elements(byXpath("//table/tbody/tr[$str]/td[$col]//*[text()]")).size == 1)
                                        &&(element(byXpath("//table/tbody/tr[$str]/td[$col]//*[text()]")).ownText.trim()
                                            .isNotEmpty())) {
                                            trueValueList.add(element(byXpath("//table/tbody/tr[$str]/td[$col]//*[text()]")).ownText.trim())
                                    }
                                }
                                maxRandom = trueValueList.size - 1
                                randomColumnValue = (0..maxRandom).random()
                                searchValue = trueValueList[randomColumnValue].toString()
                                //проверяем не номер ли это телефона и видоизменяем запись , к.т. в формате +Х(ХХХ)ХХХ-ХХ-ХХ в поисковой строке не вернет результатов, только +ХХХХХХХХХХ
                                //аналогично с ФИО
//                                val ioRegex = Regex("[А-ЯA-Z]{1}[.]\\s{1}[А-ЯA-Z]{1}[.]{1}")
                                val ioRegex = Regex("[а-яА-Яa-zA-Z]{1}[.]\\s{1}[а-яА-Яa-zA-Z]{1}[.]{1}")
                                val telRegex = Regex("[+7(]{1}[0-9]{3}[)]{1}[0-9]{3}[-]{1}[0-9]{2}[-]{1}[0-9]{2}")
                                val workTelRegex = Regex("[0-9]{1}[-][0-9]{5}[-][0-9]{3}[-][0-9]{3}")
    //                            if (searchValue.contains("+")
    //                                && searchValue.contains("(")
    //                                && searchValue.contains(")")
    //                                && searchValue.contains("-"))
                                    if (telRegex.containsMatchIn(searchValue)
                                        || workTelRegex.containsMatchIn(searchValue)
                                    ) {
                                        val newSearchValue = searchValue.filter { it.isDigit() }
                                        searchValue = newSearchValue
                                    } else if (ioRegex.containsMatchIn(searchValue)) {
                                        val searchValueList = searchValue.split(" ")
                                        searchValue = searchValueList[0]
                                    }
//                                else if (workTelRegex.containsMatchIn(searchValue)){
//                                    val newSearchValue = searchValue.filter { it.isDigit() }
//                                    searchValue = newSearchValue
//                                }
    //                                println("countAllString $i $countAllString")
    //                                println("trueValueList $i $trueValueList")
    //                                println("maxRandom $i $maxRandom")
    //                                println("randomColumnValue $i $randomColumnValue")
    //                                println("searchValue $searchValue")
    //                            searchValue = element(byXpath("//table/tbody/tr[1]/td[$q]//*[text()]")).ownText.toString()
                            } else if (subMenu == "Типы происшествий") { //Отдельно обрабатываем справочник типов происшествий
//                                element(byXpath("//thead//*[name()='svg'][@id='expandable-button']/../parent::button")).click()
//                                element(byXpath("//thead//*[name()='svg'][@id='expandable-button']/../parent::button//*[name()='path'][@d='M19 13H5v-2h14v2z']"))
//                                    .should(exist, ofSeconds(waitTime))
//                                    .shouldBe(visible, ofSeconds(waitTime))
//                                val countAllString = elements(byXpath("//table/tbody/tr")).size
                                val trueValueList = mutableListOf<String>()
                                for (r in 1..allRecordCountUse) {
                                    if (elements(byXpath("//table/tbody/tr[$r]/td[$col]//button")).size == 0
                                        && elements(byXpath("//table/tbody/tr[$r]/td[$col][text()]")).size != 0
                                    ) {
                                        trueValueList.add(element(byXpath("//table/tbody/tr[$r]/td[$col][text()]")).ownText)
                                    }
                                }
                                maxRandom = trueValueList.size - 1
                                randomColumnValue = (0..maxRandom).random()
                                searchValue = trueValueList[randomColumnValue].toString()
    //                            println("countAllString $i $countAllString")
    //                            println("trueValueList $i $trueValueList")
    //                            println("maxRandom $i $maxRandom")
    //                            println("randomColumnValue $i $randomColumnValue")

                            }
    //                        println("searchValue $searchValue")
                            //открываем строку поиска, если закрылась (бывает с иерархическими справочниками)
                            if (elements(byCssSelector("input[placeholder]")).size == 0) {
                                element(byXpath("//*[@name='search']/ancestor::button")).click()
                            }
                            element(byCssSelector("input[placeholder]")).sendKeys(searchValue, Keys.ENTER)
                            Thread.sleep(1000)

                            val nowRecordCountUse:Int
                            if (subMenu != "Типы происшествий"){
                                element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
                                    .should(exist, ofSeconds(longWait))
                                    .shouldBe(visible, ofSeconds(longWait))
                                val nowRecordCountString =
                                    element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]")).ownText.toString()
                                        .split("\n")
                                val nowRecordCountNotUse = nowRecordCountString[1].split(" ")
                                nowRecordCountUse = nowRecordCountNotUse[0].toInt()
                            } else {
                                nowRecordCountUse = elements(byXpath("//tbody/tr")).size
                                Assertions.assertTrue(nowRecordCountUse != 0)
                            }
    //                        println("searchValue $searchValue")
    //                        println("allRecordCountUse $allRecordCountUse")
    //                        println("nowRecordCountUse $nowRecordCountUse")
//                            Assertions.assertTrue(allRecordCountUse > nowRecordCountUse)
//                            println("allRecordCountUse $allRecordCountUse")
//                            println("nowRecordCountUse $nowRecordCountUse")
                            Assertions.assertTrue(allRecordCountUse > nowRecordCountUse)
//                            if (dicts == 10) {
//                                Assertions.assertTrue(nowRecordCountUse == 1)
//                            } else {
//                                Assertions.assertTrue(allRecordCountUse > nowRecordCountUse)
//                            }
    //                        println("nowRecordCountUse $nowRecordCountUse")
                            //Thread.sleep(2500)
                            element(byXpath("//input[@placeholder]/following-sibling::div/button")).click()
                            element(byCssSelector("input[placeholder]")).click()
                            Thread.sleep(1000)
                            break
                            }
                        }
                    }
                }
//        }
        tools.logoffTool()
    }


//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0231`() {
        tools.logonTool()
        //для каждого справочника выполнить
        for (dict in 1..9){
            //кликаем по иконке справочников
            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul")).click()
            //переходим в каждый справочник
            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div/ul[$dict]")).click()
            //ждем загрузки таблицы
            element(byCssSelector("main table>tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //добавляем все доступные колонки в таблицу
            tools.checkbox("", true, waitTime)
            //получаем счетчик строк в левом нижнем углу страницы, в виде числа
            val allRecordCountString = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]")).ownText.toString().split("\n")
            val allRecordCountNotUse = allRecordCountString[1].split(" ")
            val allRecordCountUse = allRecordCountNotUse[0].toInt()
            //ситаем количество слолбцов, при том что последний нам не пригодится
//            val comumnsCount = elements(byXpath("//table/thead/tr/th//*[text()]")).size
            //создаем список заголовков столбцов, обрезая последний элемент - заголовок трех точек
            val columnsElementList = elements(byXpath("//table/thead/tr/th//*[text()]"))
            val columnsList = mutableListOf<String>()
            columnsElementList.forEach {
                columnsList.add(it.ownText.trim())
            }
//            val del = columnsList.last()
//            println(del)
            columnsList.remove(columnsList.last())
//            println(columnsList)
//            println("$dict ${columnsList.size}")
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
            val searchHintString = element(byCssSelector("input[placeholder]")).getAttribute("placeholder")
            val searchHintList = searchHintString?.split(", ")
            //каждый столбец сверяем с каждым елементом подсказки (так сложно потому что нам еще на странице надо ориентироваться)
            //каждый столбец проверяем на наличие в списке подсказок

            columnsList.forEachIndexed { colInd, colVal ->
//                println("1 colInd $colInd colVal $colVal searchHintList $searchHintList")
                if (searchHintList!!.contains(colVal)){
//                    println("2 colInd $colInd colVal $colVal searchHintList $searchHintList")
                    //если столбец есть в списке подсказок, то для каждой строки достаем его текстовое значение и кладем в список
                    val stringElement = elements(byXpath("//table/tbody/tr"))
                    val inColumnsValueList = mutableListOf<String>()
                    stringElement.forEachIndexed { elind, el ->
                        val colNum = tools.numberOfColumn(colVal, waitTime)
//                        println(it)
                        if (
                            (elements(byXpath("//table/tbody/tr[$elind]/td[$colNum][text()]")).size == 1)
                            && element(byXpath("//table/tbody/tr[$elind]/td[${colInd + 1}][text()]")).ownText != "  "
//                            && (!Regex("\\s+").matches(element(byXpath("//table/tbody/tr[$elind]/td[$colNum][text()]")).ownText))
                            )
                        {
//                            println("first")
                            inColumnsValueList.add(element(byXpath("//table/tbody/tr[$elind]/td[$colNum][text()]")).ownText.trim())
                        } else if (
                            (elements(byXpath("//table/tbody/tr[$elind]/td[$colNum]//*[text()]")).size == 1)
                            && element(byXpath("//table/tbody/tr[$elind]/td[${colInd + 1}]//*[text()]")).ownText != "  "
//                            && (!Regex("\\s+").matches(element(byXpath("//table/tbody/tr[$elind]/td[$colNum]//*[text()]")).ownText))
                        )
                        {
//                            println("second")
                            inColumnsValueList.add(element(byXpath("//table/tbody/tr[$elind]/td[$colNum]//*[text()]")).ownText.trim())
                        }
                    }
                    //получили список значений столбца на странице теперь преобразуем его в пригодный для поиска вид, если это ФИО или телефонный номер
                    val  rnd = (0 until inColumnsValueList.size).random()
                    var rndSearchValue = inColumnsValueList[rnd]
//                    val fioRegex = Regex("[А-Я]{1}[.]\\s{1}[А-Я]{1}[.]{1}")
                    val fioRegex = Regex("[a-zA-Zа-яА-Я]{1}[.]\\s{1}[a-zA-Zа-яА-Я]{1}[.]{1}")
                    val telRegex = Regex("[+]{1}[7]{1}[(]{1}[0-9]{3}[)]{1}[0-9]{3}[-]{1}[0-9]{2}[-]{1}[0-9]{2}")
                    if (telRegex.containsMatchIn(rndSearchValue)) {
                        val newSearchValue = rndSearchValue.filter { it.isDigit() }
                        rndSearchValue = newSearchValue
                        } else if (fioRegex.containsMatchIn(rndSearchValue)) {
                        val searchValueList = rndSearchValue.split(" ")
                        rndSearchValue = searchValueList[0]
                        }
                    element(byCssSelector("input[placeholder]")).click()
                    element(byCssSelector("input[placeholder]")).sendKeys(rndSearchValue)
                    element(byCssSelector("input[placeholder]")).sendKeys(Keys.ENTER)
                    Thread.sleep(1000)
                    val newRecordCountString = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]")).ownText.toString().split("\n")
                    val newRecordCountNotUse = newRecordCountString[1].split(" ")
                    val newRecordCountUse = newRecordCountNotUse[0].toInt()
//                    println("newRecordCountUse $newRecordCountUse")
//                    println("allRecordCountUse $allRecordCountUse")
//                    println("rndSearchValue |$rndSearchValue|")
//                    println("inColumnsValueList |$inColumnsValueList|")

                    Assertions.assertTrue(allRecordCountUse > newRecordCountUse)
                    element(byXpath("//input[@placeholder]/following-sibling::div//button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
            }
//            columnsList.forEachIndexed { colInd, colVal ->
//                searchHintList!!.forEachIndexed {hintInd, hintVal ->
//                    if ((colVal == hintVal)
//                        || (colVal == "Телефонный код" && colVal == "Тел.код")
//                        || (colVal == "Метка" && colVal == "Имя метки")
//                        || (colVal == "№" && colVal == "Номер пункта")){
//
//                        if (elements(byXpath("")).size == 1){
//
//                        }
//
//                    }
//                }
//            }

        }

    }

//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0240`(){
        //A.3.30 Проверка наличия справочников с иерархической системой классификации
        tools.logonTool()
        //кликаем по иконке справочников
        //переходим в нужный справочник
        tools.menuNavigation("Справочники", "Типы происшествий", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем и запоминаем строки в tbody
        val stringCountBefore = elements(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
        //раскрываем весь список
        element(byXpath("//thead//*[name()='svg'][@id='expandable-button']/../parent::button")).click()
        element(byXpath("//thead//*[name()='svg'][@id='expandable-button']/../parent::button//*[name()='path'][@d='M19 13H5v-2h14v2z']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем количество строк
        val stringCountAfter = elements(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
        //сравниваем число строк
        Assertions.assertTrue(stringCountAfter > stringCountBefore)
        tools.logoffTool()
    }
    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0241`(){
        //иерархических справосников стало больше, проверяем все
        tools.logonTool()
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
        tools.logoffTool()
    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0250`(){
        //A.3.31 Проверка задания меток для указания признаков объектов
        tools.logonTool()
        //кликаем по иконке справочников
        //переходим в нужный справочник
        tools.menuNavigation("Справочники", "Организации", waitTime)
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
        //кликаем по строке ввода новых меток
        element(byXpath("//span[text()='Указать на карте']/../parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .scrollIntoView(false)
//        element(byXpath("//body"))
//            .should(exist, ofSeconds(waitTime))
////            .shouldBe(visible, ofSeconds(waitTime))
////            .scrollIntoView(false)
//            .sendKeys(Keys.END)
//        tools.inputRandom("labels")
        tools.inputRandomNew("labelsId-textfield", true, waitTime)
////        Thread.sleep(2000)
//        element(byXpath("//input[@name='labels']")).click()
////        Thread.sleep(2000)
//        //считаем сколько меток нам доступно к выбору
//        var countPotentialLabels = 0
//        do {
//            element(byXpath("//input[@name='labels']")).sendKeys(Keys.DOWN)
//            countPotentialLabels += 1
//        } while (elements(byCssSelector("input[name='labels'][aria-activedescendant^='labels-option']")).size > 0)
//        //выбираем случайную метку из доступных
////        Thread.sleep(2000)
//        val rndLabel = (1 until countPotentialLabels).random()
////        println("countPotentialLabels $countPotentialLabels")
//        repeat(rndLabel){
//            element(byXpath("//input[@name='labels']")).sendKeys(Keys.DOWN)
//        }
////        Thread.sleep(2000)
//        element(byXpath("//input[@name='labels']")).sendKeys(Keys.ENTER)
        //жмем кнопки "Сохранить"
//        Thread.sleep(2000)
        element(byXpath("//span[text()='Сохранить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем загрузки таблицы в которую нас выкидывает после сохранения
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
//        Thread.sleep(2000)
        //переходим в ту же организацию
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byXpath("//table/tbody/tr[${rndOrganization + 1}]"))
            .scrollIntoView(false)
        }
        element(byText(organizationName.toString())).click()
//        Thread.sleep(2000)
        //ждем Редактировать
        element(byXpath("//span[text()='Редактировать']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //пересчитываем метки
        amountLabel = elements(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()]")).size
        for (i in 1..amountLabel){
            newLabelsList.add(element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;'][$i]//span[text()]")).ownText)
        }
        //убеждаемся, что не затерли ни одной метки, а именно добавили одну.
//        println("organizationName $organizationName")
//        println("oldLabelsList $oldLabelsList")
//        println("newLabelsList $newLabelsList")
        Assertions.assertTrue(newLabelsList.containsAll(oldLabelsList))
//        Assertions.assertTrue(newLabelsList.size == oldLabelsList.size +1)

        tools.logoffTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //теперь удалим эту (эти) метку(и)
        tools.logonTool()
        //кликаем по иконке справочников
        //переходим в нужный справочник
        tools.menuNavigation("Справочники", "Организации", waitTime)
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
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //переходим в ту же организацию
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byXpath("//table/tbody/tr[${rndOrganization + 1}]"))
            .scrollIntoView(false)
        }
        element(byText(organizationName.toString())).click()
        //убеждаемся, что нашей метки нет
        newLabelList.forEach {
            element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()='$it']"))
                .shouldNot(exist, ofSeconds(waitTime))
                .shouldNotBe(visible, ofSeconds(waitTime))
        }
        tools.logoffTool()
//        println("newLabel $newLabel")
//        println("oldLabelsList $oldLabelsList")
//        println("newLabelsList $newLabelsList")
//        Thread.sleep(10000)

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0260`(){
        //A.3.32 Проверка использования ассоциативных связей-ссылок между объектами справочников
        tools.logonTool()
        //кликаем по иконке справочников
        //переходим в нужный справочник
        tools.menuNavigation("Справочники", "Дежурные службы", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Собираем все ФИО, что бы не проверять организацию у которой руководитель не указан
        tools.checkbox("Руководитель", true, waitTime)
        val officialIdColumn = tools.numberOfColumn("Руководитель", waitTime)
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
        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0270`(){
        //A.3.33 Проверка возможности создания записей в справочниках
        //A.3.34 Проверка возможности удаления записей в справочниках
        //A.3.35 Просмотр детальной истории записи/источников данных записи
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        tools.logonTool()
        //сначала проверим остатки неудачных запусков и удалим их.
        tools.menuNavigation("Справочники", "Должностные лица", waitTime)
        tools.checkbox("", true, waitTime)
        //воспользуемся поиском
        var menuColumnNubber = tools.numberOfColumn(" ", waitTime)
        val fioColumnNubber = tools.numberOfColumn("ФИО", waitTime)
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
        val userColumn = tools.numberOfColumn("Пользователь", waitTime)
//        while ((elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0)
//            ||(elements(byXpath("//table/tbody/tr/td[$userColumn]//*[@name='user']")).size
//                ==elements(byXpath("//table/tbody/tr")).size )
//        ){
//            //запоминаем ФИО что бы проконтролировать удаление
//            val removedPersonFIO = element(byXpath("//table/tbody/tr[1]/td[$fioColumnNubber]")).ownText
//            //открываем три точки
//            element(byXpath("//table/tbody/tr[1]/td[$menuColumnNubber]//button"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
//            //удаляем
//            element(byXpath("//span[text()='Удалить']/parent::button"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
//            //подтверждаем удаление
//            element(byXpath("//div[@role='dialog']//span[text()='Удалить']/parent::button"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
//            element(byXpath("//table/tbody/tr[1]/td[$fioColumnNubber][text()='$removedPersonFIO']"))
//                .shouldNot(exist, ofSeconds(waitTime))
//            Thread.sleep(500)
//        }
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
        tools.menuNavigation("Справочники", "Дежурные службы", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //переключаемся на 500 записей на странице
        tools.stringsOnPage(500, waitTime)
        //выставляем отображение нужных столбцов
        tools.checkbox("Наименование;Организация", true, waitTime)
        element(byXpath("//table/thead/tr/th//*[text()='Наименование']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
//        tools.checkbox("Организация", true, waitTime)
        //ждем появления выставленных столбцов (последнего из них)
        element(byXpath("//table/thead/tr/th//*[text()='Организация']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //определяем порядковые номера столбцов, которые положим в хэш
//        var hotLineANDOrganization: MutableMap<String, String> = mutableMapOf("in" to "in")
//        hotLineANDOrganization.remove("in")
//        hotLineANDOrganization.clear()
//        var hotLineList: MutableListOf<String>
        val hotLineList = mutableListOf("init")
        hotLineList.remove("init")
        val organizationList = mutableListOf("init")
        organizationList.remove("init")

//        var organizationList: MutableList<String>
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

//            hotLineANDOrganization
//                .put(element(byXpath("//tbody/tr[$u]/td[$hotlineColumnNumber]//*[text()]")).ownText,
//                element(byXpath("//tbody/tr[$u]/td[$organizationColumnNumber]//*[text()]")).ownText
//                )
        }
        Assertions.assertTrue(hotLineList.size == organizationList.size)





//        //кликаем по иконке справочников
//        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        //переходим в нужный справочник
//        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div[@data-testid='app-menu-Должностные лица']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
        tools.menuNavigation("Справочники","Должностные лица",waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        menuColumnNubber = tools.numberOfColumn(" ", waitTime)
        //удалим потуги неудачных тестов
        //воспользуемся поиском
//        element(byXpath("//*[@name='search']/ancestor::button"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        element(byCssSelector("input[placeholder^='ФИО']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        element(byCssSelector("input[placeholder^='ФИО']"))
//            .sendKeys("N 0270 AutoTest", Keys.ENTER)
//        Thread.sleep(1000)
//        while (elements(byXpath("//table/tbody/tr//*[text()='Нет информации']")).size == 0){
//            //открываем трехточечное меню
//            element(byXpath("//table/tbody/tr[1]/td[$menuColumnNubber]//button"))
//                .click()
//            //удаляем
//            element(byXpath("//span[text()='Удалить']/parent::button"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
//            //подтверждаем удаление
//            element(byXpath("//div[@role='dialog']//span[text()='Удалить']/parent::button"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
//            Thread.sleep(500)
//        }
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
//        var countPotentialOrganization = 0
//        do {
//            element(byXpath("//input[@name='companyId']")).sendKeys(Keys.DOWN)
//            countPotentialOrganization += 1
//        } while (elements(byCssSelector("input[name='companyId'][aria-activedescendant^='companyId-option']")).size > 0)
//        //выбираем случайную организацию из доступных
//        val rndCompany = (1 until countPotentialOrganization).random()
//        repeat(rndCompany){
//            element(byXpath("//input[@name='companyId']")).sendKeys(Keys.DOWN)
//        }
//        element(byXpath("//input[@name='companyId']")).sendKeys(Keys.ENTER)
        val companyId = element(byXpath("//input[@name='companyId']")).getAttribute("value")
        //Выбираем службу
        //т.к. служба зависима от организации, придется убедится в отсутствии элемента noOption
        element(byXpath("//input[@name='hotlineId']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//input[@name='hotlineId']")).sendKeys(hotLineList[randomAtridute])
        element(byXpath("//input[@name='hotlineId']")).sendKeys(Keys.DOWN, Keys.ENTER)
//        var noOption = true
//        Thread.sleep(500)
//        if (elements(byXpath("//body/div[@role='presentation']//*[text()='Отсутствуют записи в справочнике!']")).size == 0) {
//            var countPotentialHotline = 0
//            do {
//                element(byXpath("//input[@name='hotlineId']")).sendKeys(Keys.DOWN)
//                countPotentialHotline += 1
//            } while (elements(byCssSelector("input[name='hotlineId'][aria-activedescendant^='hotlineId-option']")).size > 0)
//            //выбираем случайную службу из доступных
//            val rndHotline = (1 until countPotentialHotline).random()
//            repeat(rndCompany){
//                element(byXpath("//input[@name='hotlineId']")).sendKeys(Keys.DOWN)
//            }
//        element(byXpath("//input[@name='hotlineId']")).sendKeys(Keys.ENTER)
//        noOption = false
//        }
        val hotlineId = element(byXpath("//input[@name='hotlineId']")).getAttribute("value")
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
//        val atributeSelector = "//label[text()='%s']/following-sibling::span[text()='%s']"
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
//        element(byXpath("//label[text()='ФИО']/parent::li/div/div"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //проверяем то что развернули
//        element(byXpath("//label[text()='Фамилия']/following-sibling::span[text()='$dateTime']"))
        element(byXpath(atributeSelector.format("Фамилия", dateTime)))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
//        element(byXpath("//label[text()='Имя']/following-sibling::span[text()='N 0270']"))
        element(byXpath(atributeSelector.format("Имя", "N 0270")))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
//        element(byXpath("//label[text()='Отчество']/following-sibling::span[text()='AutoTest']"))
        element(byXpath(atributeSelector.format("Отчество", "AutoTest")))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
//        element(byXpath("//label[text()='Отчество']/following-sibling::span[text()='AutoTest']"))
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

        tools.logoffTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //а теперь удалим его
        tools.logonTool()
        //кликаем по иконке справочников
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //переходим в нужный справочник
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div[@data-testid='app-menu-Должностные лица']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        tools.menuNavigation("Справочники", "Должностные лица", waitTime)
        menuColumnNubber = tools.numberOfColumn(" ", waitTime)
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
//        element(byXpath("//table/tbody/tr"))
//            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//tbody/tr//*[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
//        Thread.sleep(5000)
    }

}
