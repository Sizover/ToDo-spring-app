package ii_tests_pim

//import kotlin.collections.EmptyMap.keys
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide.*
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
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

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0010`() {
        //A31 Убедиться в наличии списка объектов  в справочнике «Муниципальные образования»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке справочников в боковом меню
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Переходим в "Муниципальные образования"
        element(byXpath("//div[@data-testid='app-menu-Муниципальные образования']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //сравниваем количество записей в таблице, на больше или равно 5
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5))
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
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Переходим в "Типы происшествий"
        element(byXpath("//div[@data-testid='app-menu-Типы происшествий']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //сравниваем количество записей в таблице, на больше или равно 5
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //раскрываем весь список
        element(byXpath("//thead//*[name()='svg'][@id='expandable-button']/../parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//thead//*[name()='svg'][@id='expandable-button']/../parent::button//*[name()='path'][@d='M19 13H5v-2h14v2z']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5))
        tools.logoffTool()

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)

    fun `N 0030`() {
        //A33 Убедиться в наличии списка объектов  в справочнике «Организации»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке справочников в боковом меню
//        element(byXpath("//div[@data-testid='app-menu-Справочники']/../.."))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        //Переходим в справочник "Организации"
//        element(byXpath("//div[@data-testid='app-menu-Организации']/../.."))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
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


    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0040`() {
        //A34 Убедиться в наличии списка объектов  в справочнике «Должностные лица»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке справочников в боковом меню
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Переходим в справочник "Должностные лица"
        element(byXpath("//div[@data-testid='app-menu-Должностные лица']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //сравниваем колличество строк должностных лиц, по условию больше или равно с 5
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(5))
        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0050`() {
        //A35 Убедиться в наличии списка объектов  в справочнике «Дежурные службы»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке справочников в боковом меню
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Переходим в справочник "Дежурные службы"
        element(byXpath("//div[@data-testid='app-menu-Дежурные службы']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //сравниваем колличество строк дежурных служб, по условию больше или равно с 3
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(3))
        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0060`() {
        //A36 Убедиться в наличии списка объектов  в справочнике «Видеокамеры»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке справочников в боковом меню
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Переходим в справочник "Видеокамеры"
        element(byXpath("//div[@data-testid='app-menu-Видеокамеры']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //сравниваем колличество строк дежурных служб, по условию больше или равно с 2
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(2))
        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0070`() {
        //A37 Убедиться в наличии списка объектов  в справочнике «Датчики»
        //логинимся
        //val tools = Tools()
        tools.logonTool()
        //кликаем по иконке справочников в боковом меню
        tools.menuNavigation("Справочники", "Датчики", waitTime)
        //сравниваем колличество строк дежурных служб, по условию больше или равно с 2
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(2))
        tools.logoffTool()
    }

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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
        val collectionAll = elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
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
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
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
        element(byCssSelector("input#incidentTypeId")).setValue("П.5.1.5 Auto-Test").sendKeys(Keys.DOWN, Keys.RETURN)
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
            .uploadFile(File("/home/isizov/Downloads/Sharash-montash_pasport.pdf"))
        //Thread.sleep(50000)
        element(byCssSelector("div[style='padding: 5px;'] > div:first-child"))
            .shouldHave(text("Sharash-montash_pasport.pdf"), ofSeconds(waitTime))
        element(byCssSelector("input#upload-file"))
            .uploadFile(File("/home/isizov/Pictures/post_5a9d4ef332426-1.jpg"))
        element(byCssSelector("div[style='padding: 5px;'] > div:first-child"))
            .shouldHave(text("post_5a9d4ef332426-1.jpg"), ofSeconds(waitTime))

        tools.logoffTool()

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        //`A312 Проверка прикрепления файла к происшествию`
        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        //кликаем по иконке происшествий в боковом меню
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
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
        element(byCssSelector("input#upload-file")).uploadFile(File("/home/isizov/Documents/AutoTest.odt"))
        //Thread.sleep(50000)
        element(byCssSelector("div[style='padding: 5px;'] > div:first-child"))
            .shouldHave(text("AutoTest.odt"), ofSeconds(waitTime))

        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0120`() {
        //A313 Проверка наличия опросника абонента (заявителя)
        //val tools = Tools()
        //логинимся
        tools.logonTool()
        //кликаем по иконке происшествий в боковом меню
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
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
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
        //кликаем по "создать обращение"
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
        element(byCssSelector("input#incidentTypeId")).setValue("П.5.1.5 Auto-Test").sendKeys(Keys.DOWN, Keys.ENTER)
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
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
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
        var parentCount = elements(byCssSelector("div#simple-tabpanel-iplan>div>div>div>div")).size
        //большая петля для родительских пунктов
        for (p in 1..parentCount) {
            element(byCssSelector("div#simple-tabpanel-iplan > div > div > div > div:nth-child($p) > div > div#panel1a-header")).shouldHave(
                text("Мероприятие $p.0")
            )
            //определяем количество дочерших пунктов внутри каждого родителя
            var childCount =
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
        elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']")).shouldHave(
            CollectionCondition.sizeGreaterThanOrEqual(1))
        //открываем фильтр "Типы происшествий"
        element(byXpath("//span[text()='Типы происшествий']/..")).click()
        element(byCssSelector("div[tabindex='-1'] div[role='combobox']")).should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime)).click()
        //Выбираем "Консультации"
        element(byCssSelector("ol label[title='К Консультации'] span[role='checkbox']")).click()
        //Выбираем "Ложные"
        element(byCssSelector("ol label[title='Л Ложные'] span[role='checkbox']")).click()
        //Кликаем "В пустоту"
        element(byCssSelector("body > div:nth-child(9) > div:nth-child(1)")).click()
        //Применить
        element(byXpath("//span[text()='Применить']/..")).click()
        //Дожидаемся применения фильтра
        Thread.sleep(500)
        element(byXpath("//span[text()='Типы происшествий']/button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("button[style='width: 250px; min-width: 250px; display: flex; justify-content: space-between;']"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        elements(byText("Ложные")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0))
        elements(byText("Консультации")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0))
        var intA: Int = elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']")).size
        var intB: Int = elements(byText("Ложные")).size
        var intC: Int = elements(byText("Консультации")).size
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
        elements(byText("В обработке")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0))
        elements(byText("Реагирование")).shouldHave(CollectionCondition.sizeGreaterThanOrEqual(0))
        intA = elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']")).size
        intB = elements(byText("В обработке")).size
        intC = elements(byText("Реагирование")).size
        intS = intB + intC
        Assertions.assertTrue(intS == intA)
        //Очищаем фильтр "Статусы"
        element(byXpath("//span[text()='Статусы']/button")).click()
        /////////////////////////////////////////////////////////////////////////////////////////
        //Открываем фильтр "Уровни"
        element(byXpath("//span[text()='Уровни']/..")).click()
        element(byCssSelector("div[tabindex='-1'] div[role='combobox']")).should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime)).click()
        //Выбираем "ЧС" и "Угроза ЧС"
        element(byCssSelector("input#operationModeId")).setValue("Угроза ЧС").sendKeys(Keys.DOWN, Keys.ENTER)
        element(byCssSelector("input#operationModeId")).setValue("ЧС").sendKeys(Keys.DOWN)
        element(byCssSelector("input#operationModeId")).setValue("ЧС").sendKeys(Keys.ENTER)
        element(byCssSelector("input#operationModeId")).click()
        //Применить
        element(byXpath("//span[text()='Применить']/..")).click()
        //Дожидаемся применения фильтра
        Thread.sleep(500)
        element(byXpath("//span[text()='Уровни']/button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        tools.checkbox("Уровень происшествия", true, waitTime)
        intA = elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']")).size
        intB = elements(byText("Угроза ЧС")).size
        //intC = elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-'][style]")).size
        intC = elements(byText("ЧС")).size
        intS = intB + intC
        Assertions.assertTrue(intS == intA)
        //Очищаем фильтр Уровни
        element(byXpath("//span[text()='Уровни']/button")).click()
        /////////////////////////////////////////////////////////////////////////////////////////
        //Открываем фильтр "Источники"
        element(byXpath("//span[text()='Источники']/..")).click()
        element(byCssSelector("div[tabindex='-1'] div[role='combobox']"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
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
        intA = elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']")).size
        intB = elements(byText("Видеоаналитика")).size
        intC = elements(byText("СМС")).size
        intS = intB + intC
        //с источниками все не так просто - в таблицу происшествий выносится источник последнего обращения по происшествию, а фильтр работает по источникам всех обращений по проишествию
        //Проверяем не закрались ли записи с последним обращением из источника не взятого в фильтр
        if (intS < intA) {
            //и если это произошло, открываем каждую карточку и переходя в её обращения смотрим что там есть что-то из того, что взяли в фильтр
            for (i in 0 until intA) {
                //опять побеждаем отлистывание страницы вниз
                if (i >= intA-2){
                    Thread.sleep(500)
                    element(byCssSelector("body"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .sendKeys(Keys.END)
                } else {
                    element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-${i+1}']")).scrollIntoView(false)
                }
                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$i']")).click()
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
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
        //кликаем по "создать обращение"
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
        element(byCssSelector("input#incidentTypeId")).setValue("П.5.1.5 Auto-Test").sendKeys(Keys.DOWN, Keys.ENTER)
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
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
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
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
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
        element(byXpath("//div[@data-testid='app-menu-Происшествия']/../..")).click()
        //Переходим в "Список происшетвий"
        element(byXpath("//div[@data-testid='app-menu-Список происшествий']/../..")).click()
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
        //т.к. из за библиотеки построения таблицы, элементы скрытые за прокруткой вниз,
        // с точки зрения драйвера браузера станут кликабельны раньше чем на самом деле до них докрутит прокрутка,
        // сразу опускаемся вниз страницы (с прокруткой вверх будет аналогично, поэтому следующая строка не канает)
        //element(byCssSelector("table[role='grid']")).sendKeys(Keys.END)
        //Thread.sleep(5000)
        //Выбираем случайную КП
        val rndA = (0..19).random()
        //rndA = 0
        if (rndA == 19) {
            element(byCssSelector("table[role='grid']")).sendKeys(Keys.END)
        } else {
            element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-${rndA + 1}']")).scrollIntoView(false)
            //Thread.sleep(20000)
        }
        element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$rndA']")).click()
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
    fun `N 0170`() {
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
        //кликаем по иконке справочников в боковом меню
//        element(byXpath("//span[@title='Справочники']/..")).click()
//        //Переходим в справочник "Организации"
//        element(byXpath("//span[text()='Организации']/../..")).click()
        //ждем загрузки
        element(byCssSelector("tbody>tr"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //ищем столбец "файлы"
        tools.checkbox("Файлы",true,waitTime)
        val columsNameElements = elements(byXpath("//thead/tr/th//*[text()]"))
        var columsName = mutableListOf<String>()
        columsNameElements.forEach {
            columsName.add(it.ownText)
        }
//        if (!columsName.contains("Файлы")){
//            tools.checkbox("Файлы",true,waitTime)
//        }
        //считаем количество записей на странице
        val all = elements(byCssSelector("tbody>tr")).size
        //Ищем строки организаций с прикрепленными файлами построчно проверяя наличие скрепки
        for (i in 0 until all) {
            //опять боремся с глюком видимости но не кликабельности нижних элементов
            if (i >= all - 2) {
                element(byCssSelector("body")).sendKeys(Keys.END)
            } else {
                element(byXpath("//tbody/tr[${i + 1}]")).scrollIntoView(false)
            }
            element(byCssSelector("tbody>tr[$i]"))
                .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            //ищем скрепку в строке
            val haveFile =
                elements(byXpath("//tbody/tr[$i]/td[${columsName.indexOf("Файлы")+1}]/*[name()='svg']")).size
            //если нашли, то...
            if (haveFile == 1) {
                //переходим в карточку организации
                element(byCssSelector("//tbody/tr[$i]")).click()
                element(byCssSelector("form[novalidate]")).should(exist, ofSeconds(waitTime))
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
                    } else if (fileSVG == 1 || filesvg == 1) {
                        val href =
                            element(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) a")).getAttribute("href")
                        open("$href")
                        element(byCssSelector("rect")).should(exist, ofSeconds(waitTime))
                        back()
                    } else if (fileJPEG == 1 || fileJPG == 1 || filePNG == 1 || filejpeg == 1 || filejpg == 1 || filepng == 1) {
                        val href =
                            element(byCssSelector("div[style='padding: 5px;']>div:nth-child($u) a")).getAttribute("href")
                        open("$href")
                        element(byCssSelector("img")).should(exist, ofSeconds(waitTime))
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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(text("A.3.23 Проверка формирования отчетов по обращениям $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //проверяем и запоминаем общее количество обращений
        val all = element(byXpath("//td[text()='Общее количество вызовов (обращений):']/following-sibling::td"))
            .ownText.toInt()
        //ложных
        val fal = element(byXpath("//td[text()='Ложных']/following-sibling::td"))
            .ownText.toInt()
        //консультаций
        val con = element(byXpath("//td[text()='Консультаций']/following-sibling::td"))
            .ownText.toInt()
        //по происшествиям
        val inc = element(byXpath("//td[text()='По происшествиям']/following-sibling::td"))
            .ownText.toInt()
        var falD = 0
        var conD =0
        var incD = 0
        //сверим цветастую табличку с диаграммой
        //если вобще есть диаграмма
        if (all > 0){
            //ложные, если они есть
            if (elements(byCssSelector("text[name='Ложных']")).size == 0){
                falD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(1)>text")).ownText.toInt()
                Assertions.assertTrue(fal == falD)
            }
            //консультации, если они есть
            if (elements(byCssSelector("text[name='Консультаций']")).size == 0){
                conD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(2)>text")).ownText.toInt()
                Assertions.assertTrue(con == conD)
            }
            //происшествия, если они есть
            if (elements(byCssSelector("text[name='По происшествиям']")).size == 0){
                incD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(3)>text")).ownText.toInt()
                Assertions.assertTrue(inc == incD)
            }
        }
        //Сверяем, но возможна ситуация, когда отношение количества каких-то обращений к остальным, мало и обращения есть, а цифры на диаграмме нету, решать будем, когда появится такой отчет
        //рассматриваем таблицу источников
        //всего записей в смысле в графе всего
        var tableAll = 0
//            element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-0']>td[data-colindex='1']>div")).ownText.toInt()
        //println(tableAll)
        //Видеоаналитика из общего числа
        var tableVideo = 0
        //Внешняя АИС из общего числа
        var tableAIS = 0
        //Датчик из общего числа
        var tableSensor = 0
        //Портал населения из общего числа
        var tablePortal = 0
        //Портал УИВ из общего числа
        var tableUIV = 0
        //служебный счетчик, всего строк
        var tableStringCount = elements(byCssSelector("tr[id^='MUIDataTableBodyRow-']")).size
        //запоминаем те значения, что будем создавать
        for (y in 0 until tableStringCount) {
            if (y!=1){
            var st: String =
                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$y']>td[data-colindex='0']>div")).ownText
            var va: Int =
                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$y']>td[data-colindex='1']>div")).ownText.toInt()
            when (st) {
                "Всего" -> {tableAll = va}
                "Видеоаналитика" -> {tableVideo = va}
                "Внешняя АИС" -> {tableAIS = va}
                "Датчик" -> {tableSensor = va}
                "Портал населения" -> {tablePortal = va}
                "Портал УИВ" -> {tableUIV = va}
            }
            }
        }
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
                element(byCssSelector("input#incidentTypeId")).setValue("П.5.1.5 Auto-Test")
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
                val troubleshoot = element(byXpath("//*[contains(text(),'$adr')]")).ownText
                println("troubleshoot = $troubleshoot")
                element(byXpath("//*[contains(text(),'$adr')]/ancestor::button")).click()
                element(byXpath("//span[text()='Привязать к происшествию']/parent::button")).click()
            } else if (i == 4) {
                //регистрируем ложное обращение
                element(byXpath("//span[text()='Ложное обращение']/parent::button")).click()
                element(byXpath("//*[text()='Звонок без информации']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(text("A.3.23 Проверка формирования отчетов по обращениям $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //проверяем и запоминаем общее количество обращений
        val allT = element(byXpath("//td[text()='Общее количество вызовов (обращений):']/following-sibling::td"))
            .ownText.toInt()
        //ложных
        val falT = element(byXpath("//td[text()='Ложных']/following-sibling::td"))
            .ownText.toInt()
        //консультаций
        val conT = element(byXpath("//td[text()='Консультаций']/following-sibling::td"))
            .ownText.toInt()
        //по происшествиям
        val incT = element(byXpath("//td[text()='По происшествиям']/following-sibling::td"))
            .ownText.toInt()

        //сверим цветастую табличку с диаграммой
        val diagramSelector = "g.recharts-layer.recharts-pie-labels>g:nth-child(%d)>text"
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
            element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-0']>td[data-colindex='1']>div")).ownText.toInt()
        //println(tableAll)
        //Видеоаналитика из общего числа
        var tableVideoT: Int = 0
        //Внешняя АИС из общего числа
        var tableAIST: Int = 0
        //Датчик из общего числа
        var tableSensorT: Int = 0
        //Портал населения из общего числа
        var tablePortalT: Int = 0
        //Портал УИВ из общего числа
        var tableUIVT: Int = 0
        //служебный счетчик, всего строк
        tableStringCount = elements(byCssSelector("tr[id^='MUIDataTableBodyRow-']")).size
        for (u in 2 until tableStringCount) {
            val st: String =
                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$u']>td[data-colindex='0']>div")).ownText.toString()
            val va: Int =
                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$u']>td[data-colindex='1']>div")).ownText.toInt()
            when (st) {
                "Видеоаналитика" -> { tableVideoT = va }
                "Внешняя АИС" -> { tableAIST = va }
                "Датчик" -> { tableSensorT = va }
                "Портал населения" -> { tablePortalT = va }
                "Портал УИВ" -> { tableUIVT = va }
            }
        }

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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
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
        val operator = element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p")).ownText
//        val operatorMas = operator.split("\n")
//        val operatorFIO = operatorMas[1].trim()
        val operatorFIO = operator.trim()
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
        var operators = elements(byCssSelector("tr[id^=MUIDataTableBodyRow-]")).size
        for (w in 0 until operators) {
            var momentOperator =
                element(byCssSelector("tr[data-testid^=MUIDataTableBodyRow-]>td[data-testid='MuiDataTableBodyCell-0-$w']>div:nth-child(2)")).ownText.trim()
//            println("0 momentOperator $momentOperator")
            if (operatorFIO == momentOperator) {
                //поехали запоминать значения в отчете для оператора
                val operatorDataSelector = "td[data-testid='MuiDataTableBodyCell-%d-$w']>div:nth-child(2)"
                first = element(byCssSelector(operatorDataSelector.format(2))).ownText.toInt()
                second = element(byCssSelector(operatorDataSelector.format(3))).ownText.toInt()
                third = element(byCssSelector(operatorDataSelector.format(4))).ownText.toInt()
                fourth = element(byCssSelector(operatorDataSelector.format(5))).ownText.toInt()
                fifth = element(byCssSelector(operatorDataSelector.format(6))).ownText.toInt()
                sixth = element(byCssSelector(operatorDataSelector.format(7))).ownText.toInt()
                seventh = element(byCssSelector(operatorDataSelector.format(8))).ownText.toInt()
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
                element(byCssSelector("input#incidentTypeId")).setValue("П.5.1.5 Auto-Test")
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
                element(byXpath("//span[text()='Ложное обращение']/parent::button")).click()
                element(byXpath("//*[text()='Звонок без информации']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
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
        operators = elements(byCssSelector("tr[id^=MUIDataTableBodyRow-]")).size
        var firstT = 0
        var secondT = 0
        var thirdT = 0
        var fourthT = 0
        var fifthT = 0
        var sixthT = 0
        var seventhT = 0
        //ищем кто в списке мы
        for (e in 0 until operators) {
            var momentOperator =
                element(byCssSelector("tr[data-testid^=MUIDataTableBodyRow-]>td[data-testid='MuiDataTableBodyCell-0-$e']>div:nth-child(2)")).ownText.trim()
            if (operatorFIO == momentOperator) {
                val operatorDataSelector = "td[data-testid='MuiDataTableBodyCell-%d-$e']>div:nth-child(2)"
                //поехали запоминать значения в отчете для оператора
                firstT = element(byCssSelector(operatorDataSelector.format(2))).ownText.toInt()
                secondT = element(byCssSelector(operatorDataSelector.format(3))).ownText.toInt()
                thirdT = element(byCssSelector(operatorDataSelector.format(4))).ownText.toInt()
                fourthT = element(byCssSelector(operatorDataSelector.format(5))).ownText.toInt()
                fifthT = element(byCssSelector(operatorDataSelector.format(6))).ownText.toInt()
                sixthT = element(byCssSelector(operatorDataSelector.format(7))).ownText.toInt()
                seventhT = element(byCssSelector(operatorDataSelector.format(8))).ownText.toInt()
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
        //Переходим в "отчет по деятельности сотрудников"
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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем таблицу
//        element(byCssSelector("main > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > div"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("div[class*='MuiPaper-elevation'][class*='MuiPaper-rounded'][class*='jss']"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //Запоминаем значения отчета
        //значения в легенде
        val legendSelector = "table[aria-label='simple table'] tr:nth-child(%d)>td:nth-child(2)"
        val legendAllBefore = element(byCssSelector(legendSelector.format(1))).ownText.toInt()
        val legendNewBefore = element(byCssSelector(legendSelector.format(2))).ownText.toInt()
        val legendProcessingBefore = element(byCssSelector(legendSelector.format(3))).ownText.toInt()
        val legendReactionBefore = element(byCssSelector(legendSelector.format(4))).ownText.toInt()
        val legendDoneBefore = element(byCssSelector(legendSelector.format(5))).ownText.toInt()
        val legendCanselBefore = element(byCssSelector(legendSelector.format(6))).ownText.toInt()
        val legendCloseBefore = element(byCssSelector(legendSelector.format(7))).ownText.toInt()
        //значения в таблице
        val oldTableStringCount = elements(byCssSelector("table[role='grid']>tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
        val oldIncidentTypeList = mutableListOf<String>()
        val oldAmountIncidentList = mutableListOf<Int>()
        val oldAmountAffectedPeopleList = mutableListOf<Int>()
        val oldAmountAffectedChildrenList = mutableListOf<Int>()
        val oldAmountDiePeopleList = mutableListOf<Int>()
        val oldAmountDieChildrenList = mutableListOf<Int>()
        val tableSelector = "table[role='grid']>tbody>tr[data-testid^='MUIDataTableBodyRow-']:nth-child(%d)>td:nth-child(%d)>div"
        for (i in 0 until oldTableStringCount) {
            oldIncidentTypeList.add(i, element(byCssSelector(tableSelector.format(i + 1, 1))).ownText)
            oldAmountIncidentList.add(i, element(byCssSelector(tableSelector.format(i + 1, 2))).ownText.toInt())
            oldAmountAffectedPeopleList.add(i, element(byCssSelector(tableSelector.format(i + 1, 3))).ownText.toInt())
            oldAmountAffectedChildrenList.add(i, element(byCssSelector(tableSelector.format(i + 1, 4))).ownText.toInt())
            oldAmountDiePeopleList.add(i, element(byCssSelector(tableSelector.format(i + 1, 5))).ownText.toInt())
            oldAmountDieChildrenList.add(i, element(byCssSelector(tableSelector.format(i + 1, 6))).ownText.toInt())
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
            element(byCssSelector("input#incidentTypeId"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //выбираем тип происшествия
            //Сначала создаем происшествия с теми же типами, что были в первом отчете, если они были
            if (oldIncidentTypeListCount >= i && i<7){
                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[i-1])
                    .sendKeys(Keys.DOWN, Keys.ENTER)
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
                element(byCssSelector("input#incidentTypeId")).setValue("П.5.1.5 Auto-Test")
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
                tools.inputRundom("incidentTypeId")
                element(byCssSelector("input#incidentTypeId[value*='.']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //убеждаемся что такого происшествия нет в первом отчете и если есть, то выбираем другое
                var longSelectedIncidentType = element(byCssSelector("input#incidentTypeId")).getAttribute("value").toString()
                var shortSelectedIncidentType = longSelectedIncidentType.substring(longSelectedIncidentType.indexOf(' ') + 1)
                while (oldIncidentTypeList.contains(shortSelectedIncidentType)
                    || shortSelectedIncidentTypeList.contains(shortSelectedIncidentType)
                    || shortSelectedIncidentType=="Auto-Test")
                {
                    repeat(longSelectedIncidentType.length)
                        {element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.BACK_SPACE) }
                    tools.inputRundom("incidentTypeId")
                    longSelectedIncidentType = element(byCssSelector("input#incidentTypeId")).getAttribute("value").toString()
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
            element(byCssSelector("div.MuiGrid-root.MuiGrid-item > button[type='submit']")).click()
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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(
                visible,
                ofSeconds(waitTime)
            ).click()
        //ждем таблицу
        element(byCssSelector("main > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > div"))
            .should(exist, ofSeconds(waitTime)).shouldBe(
                visible,
                ofSeconds(waitTime)
            )
        //Запоминаем значения отчета
        //значения в легенде
        val legendAllAfter = element(byCssSelector(legendSelector.format(1))).ownText.toInt()
        val legendNewAfter = element(byCssSelector(legendSelector.format(2))).ownText.toInt()
        val legendProcessingAfter = element(byCssSelector(legendSelector.format(3))).ownText.toInt()
        val legendReactionAfter = element(byCssSelector(legendSelector.format(4))).ownText.toInt()
        val legendDoneAfter = element(byCssSelector(legendSelector.format(5))).ownText.toInt()
        val legendCanselAfter = element(byCssSelector(legendSelector.format(6))).ownText.toInt()
        val legendCloseAfter = element(byCssSelector(legendSelector.format(7))).ownText.toInt()
        //значения в таблице
        val newTableStringCount = elements(byCssSelector("table[role='grid']>tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
        val newIncidentTypeList = mutableListOf<String>()
        val newAmountIncidentList = mutableListOf<Int>()
        val newAmountAffectedPeopleList = mutableListOf<Int>()
        val newAmountAffectedChildrenList = mutableListOf<Int>()
        val newAmountDiePeopleList = mutableListOf<Int>()
        val newAmountDieChildrenList = mutableListOf<Int>()
        for (i in 0 until newTableStringCount) {
            newIncidentTypeList.add(i, element(byCssSelector(tableSelector.format(i + 1, 1))).ownText)
            newAmountIncidentList.add(i, element(byCssSelector(tableSelector.format(i + 1, 2))).ownText.toInt())
            newAmountAffectedPeopleList.add(i, element(byCssSelector(tableSelector.format(i + 1, 3))).ownText.toInt())
            newAmountAffectedChildrenList.add(i, element(byCssSelector(tableSelector.format(i + 1, 4))).ownText.toInt())
            newAmountDiePeopleList.add(i, element(byCssSelector(tableSelector.format(i + 1, 5))).ownText.toInt())
            newAmountDieChildrenList.add(i, element(byCssSelector(tableSelector.format(i + 1, 6))).ownText.toInt())
        }
//            var newIncidentTypeListCount: Int = newIncidentTypeList.count()
        //значения в диаграмме
//        var diagNew = 0
//        val diagSelector = "svg.recharts-surface>g>g.recharts-pie-labels>g:nth-child(%d)>text"
        //сравниваем новую диаграмму со старой легендой, учитывая созданные КП. "Новые" происшествия не рассматриваем
        //т.к. наличие цифры на диаграмме зависит от соотношения количества разных карточек, перед сравнением диаграммы убеждаемся, что вобще есть что сравнивать
        val diagSelector = "//*[name()='svg'][@class='recharts-surface']/*[name()='g']/*[name()='g']/*[name()='g'][%d]/*[name()='text'][text()]"
        //diagNew = element(byCssSelector("svg.recharts-surface>g>g.recharts-pie-labels>g:nth-child(1)>text")).ownText.toInt()
        var diagProcessing = 0
        var diagReaction = 0
        var diagDone = 0
        var daigCansel = 0
        var daagClose = 0
        if (elements(byXpath(diagSelector.format(2))).size == 1){
            diagProcessing = element(byXpath(diagSelector.format(2))).ownText.toInt()
            Assertions.assertTrue(diagProcessing == (legendProcessingBefore + 2))
        }
        if (elements(byXpath(diagSelector.format(3))).size == 1){
            diagReaction = element(byXpath(diagSelector.format(3))).ownText.toInt()
            Assertions.assertTrue(diagReaction == (legendReactionBefore + 1))
        }
        if (elements(byXpath(diagSelector.format(4))).size == 1){
            diagDone = element(byXpath(diagSelector.format(4))).ownText.toInt()
            Assertions.assertTrue(diagDone == (legendDoneBefore + 1))
        }
        if (elements(byXpath(diagSelector.format(5))).size == 1){
            daigCansel = element(byXpath(diagSelector.format(5))).ownText.toInt()
            Assertions.assertTrue(daigCansel == (legendCanselBefore + 1))
        }
        if (elements(byXpath(diagSelector.format(6))).size == 1){
            daagClose = element(byXpath(diagSelector.format(6))).ownText.toInt()
            Assertions.assertTrue(daagClose == (legendCloseBefore + 1))
        }
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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем таблицу
        element(byCssSelector("main > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки в таблице, их должно быть 0
        var tableStringCount = elements(byCssSelector("table[role='grid']>tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
        Assertions.assertTrue(tableStringCount==0)
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
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max -1"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем таблицу
        element(byCssSelector("main > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки, и ожидаем что их столько же, сколько наибольшего числа в массиве пострадавших
        val amountMaxAffected = newAmountAffectedPeopleList.filter { it == maximumAffectedPeople}
        tableStringCount = elements(byCssSelector("table[role='grid']>tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
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
        element(byCssSelector("input#incidentTypeId"))
            .sendKeys(newIncidentTypeList[0])
        element(byCssSelector("input#incidentTypeId"))
            .sendKeys(Keys.DOWN, Keys.ENTER)
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime Тип происшествия"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем таблицу
        element(byCssSelector("main > div:nth-child(3) > div:nth-child(2) > div:nth-child(2) > div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableStringCount = elements(byCssSelector("table[role='grid']>tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
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
                element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']>td:first-child"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            } else if (i==10){
//            } else if (elements(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']")).size>0 && i==10){
                element(byCssSelector("tr[data-testid^='MUIDataTableBodyRow-']>td:last-child button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//*[text()='Просмотреть']/parent::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            }
            element(byXpath("//h6[text()='Общие данные']/parent::form"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        //Thread.sleep(5000)
        }
    tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0230`() {
        //A.3.29 Проверка поиска справочных данных
        //хорошо бы в таблицу включать все столбцы, но это потом доработаю //доработано
        tools.logonTool()

        for (i in 1..10) {
            //кликаем по иконке справочников
            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul")).click()
            //переходим в каждый справочник
            element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div/ul[$i]")).click()
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
            val allRecordCountString = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]")).ownText.toString().split("\n")
            val allRecordCountNotUse = allRecordCountString[1].split(" ")
            val allRecordCountUse = allRecordCountNotUse[0].toInt()
            //ситаем количество слолбцов, при том что последний нам не пригодится
            val comumnCount = elements(byXpath("//table/thead/tr/th[@data-colindex]//div[text()]")).size
//            println("allRecordCountString $allRecordCountString")
//            println("allRecordCountNotUse $allRecordCountNotUse")
//            println("allRecordCountUse $allRecordCountUse")
            //отркрываем поисковую строку
            element(byCssSelector("button[data-testid='Поиск-iconButton']")).click()
            element(byCssSelector("input[placeholder]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //читаем что есть в подсказке
            val searchHintString = element(byCssSelector("input[placeholder]")).getAttribute("placeholder")
            val searchHintList = searchHintString?.split(", ")
//            println("searchHintList $searchHintList")
            //проходимся по заголовкам столбцов, сверяя их с каждой позицией подсказки
            var searchValue = ""
            for (q in 1 until comumnCount) {
                val columnName = element(byXpath("//table/thead/tr/th[@data-colindex][$q]//div[text()]")).ownText.toString()
//                println("columnName $columnName")
                searchHintList?.forEach{
//                    println("$i searchHintList $it")
                    //если заголовок столбца совпал с подсказкой, то вбиваем значение этого столбца из первой строки в подсказку и ждем что нижний счетчик строк будет строго меньше исходного
                    if (columnName.contains(it)
                        || (columnName == "Телефонный код" && it == "Тел.код")
                        || (columnName == "Метка" && it == "Имя метки")
                        || (columnName == "№" && it == "Номер пункта")) {
                        //искомое значение определяем случайно, среди имеющихся но с типами происшествий и откидыванием пустых значений других справочников придется возится
                        var maxRandom: Int = 1
                        var randomColumnValue =0
                        if (i != 10){
                            val countAllString = elements(byXpath("//table/tbody/tr")).size
                            var trueValueList = mutableListOf<String>()
                            for (r in 1..countAllString) {
//                                if (elements(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).size == 1) {
//                                    trueValueList.add(element(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).ownText.trim())
//                                }
//                                if (element(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).ownText.trim().isNotEmpty()
//                                    && elements(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).size > 0) {
//                                    trueValueList.add(element(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).ownText.trim())
//                                }
                                if (elements(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).size == 1){
                                    if (element(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).ownText.trim().isNotEmpty()){
                                        trueValueList.add(element(byXpath("//table/tbody/tr[$r]/td[$q]//*[text()]")).ownText.trim())
                                    }
                                }

                            }
                                maxRandom = trueValueList.size -1
                                randomColumnValue = (0..maxRandom).random()
                                searchValue = trueValueList[randomColumnValue].toString()
                            //проверяем не номер ли это телефона и видоизменяем запись , к.т. в формате +Х(ХХХ)ХХХ-ХХ-ХХ в поисковой строке не вернет результатов, только +ХХХХХХХХХХ
                            //аналогично с ФИО
                            val  ioRegex = Regex("[А-Я]{1}[.]\\s{1}[А-Я]{1}[.]{1}")
                            val telRegex = Regex("[+]{1}[7]{1}[(]{1}[0-9]{3}[)]{1}[0-9]{3}[-]{1}[0-9]{2}[-]{1}[0-9]{2}")
//                            if (searchValue.contains("+")
//                                && searchValue.contains("(")
//                                && searchValue.contains(")")
//                                && searchValue.contains("-"))
                            if (telRegex.containsMatchIn(searchValue))
                            {
                                val newSearchValue = searchValue.filter { it.isDigit() }
                                searchValue = newSearchValue
                            } else if (ioRegex.containsMatchIn(searchValue))
                                {
                                val searchValueList = searchValue.split(" ")
                                    searchValue = searchValueList[0]
                            }
//                                println("countAllString $i $countAllString")
//                                println("trueValueList $i $trueValueList")
//                                println("maxRandom $i $maxRandom")
//                                println("randomColumnValue $i $randomColumnValue")
//                                println("searchValue $searchValue")
//                            searchValue = element(byXpath("//table/tbody/tr[1]/td[$q]//*[text()]")).ownText.toString()
                        } else if (i==10) { //Отдельно обрабатываем справочник типов происшествий
                            element(byXpath("//thead//*[name()='svg'][@id='expandable-button']/../parent::button")).click()
                            element(byXpath("//thead//*[name()='svg'][@id='expandable-button']/../parent::button//*[name()='path'][@d='M19 13H5v-2h14v2z']"))
                                .should(exist, ofSeconds(waitTime))
                                .shouldBe(visible, ofSeconds(waitTime))
                            val countAllString = elements(byXpath("//table/tbody/tr")).size
                            var trueValueList = mutableListOf<String>()
                            for (r in 1..countAllString){
                                if (elements(byXpath("//table/tbody/tr[$r]/td[1]//button")).size ==0
                                    && elements(byXpath("//table/tbody/tr[$r]/td[${q+1}]//*[text()]")).size !=0){
                                    trueValueList.add(element(byXpath("//table/tbody/tr[$r]/td[${q+1}]//*[text()]")).ownText)
                                }
                            }
                            maxRandom = trueValueList.size -1
                            randomColumnValue = (0..maxRandom).random()
                            searchValue = trueValueList[randomColumnValue].toString()
//                            println("countAllString $i $countAllString")
//                            println("trueValueList $i $trueValueList")
//                            println("maxRandom $i $maxRandom")
//                            println("randomColumnValue $i $randomColumnValue")

                        }
//                        println("searchValue $searchValue")
                        if ( i==10 ){element(byCssSelector("button[data-testid='Поиск-iconButton']")).click()}
                        element(byCssSelector("input[placeholder]")).sendKeys(searchValue, Keys.ENTER)
                        Thread.sleep(500)
                        element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
                            .should(exist, ofSeconds(longWait))
                            .shouldBe(visible, ofSeconds(longWait))
                        val nowRecordCountString = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]")).ownText.toString().split("\n")
                        val nowRecordCountNotUse = nowRecordCountString[1].split(" ")
                        val nowRecordCountUse = nowRecordCountNotUse[0].toInt()
//                        println("searchValue $searchValue")
//                        println("allRecordCountUse $allRecordCountUse")
//                        println("nowRecordCountUse $nowRecordCountUse")
                        Assertions.assertTrue(allRecordCountUse > nowRecordCountUse)
                        if ( i==10 ){Assertions.assertTrue(nowRecordCountUse == 1)}
//                        println("nowRecordCountUse $nowRecordCountUse")
                        //Thread.sleep(2500)
                        element(byXpath("//input[@placeholder]/following-sibling::div/button")).click()
                        element(byCssSelector("input[placeholder]")).click()
//                        Thread.sleep(5000)
                        }
                    }
                }
        }
        tools.logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `N 0240`(){
        //A.3.30 Проверка наличия справочников с иерархической системой классификации
        tools.logonTool()
        //кликаем по иконке справочников
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //переходим в нужный справочник
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div[@data-testid='app-menu-Типы происшествий']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
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
    fun `N 0250`(){
        //A.3.31 Проверка задания меток для указания признаков объектов
        tools.logonTool()
        //кликаем по иконке справочников
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //переходим в нужный справочник
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div[@data-testid='app-menu-Организации']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки и переходим в случайную организацию
        val organizationTableStringCount = elements(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
        val rndOrganization = (1..organizationTableStringCount).random()
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']:nth-child(${rndOrganization + 1})"))
            .scrollIntoView(false)

        }
        element(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']:nth-child($rndOrganization)")).click()
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
        tools.inputRundom("labels")
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
        } else { element(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']:nth-child(${rndOrganization + 1})"))
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
        //теперь удалим эту метку
        tools.logonTool()
        //кликаем по иконке справочников
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //переходим в нужный справочник
        element(byXpath("//div[@data-testid='app-menu-Справочники']/../parent::ul//div[@data-testid='app-menu-Организации']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //открываем поиск (т.к. теперь не знаем до какой строки листать)
        //переходим в ту же организацию
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']:nth-child(${rndOrganization + 1})"))
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
//        println("newLabel $newLabel")
        //убеждаемся, что поставленная метка на месте
        element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()='${newLabelList[0]}']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
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
        element(byXpath("//label[text()='Метки']/..//span[text()='${newLabelList[0]}']/../*[name()='svg']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//span[text()='Сохранить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //открываем поиск (т.к. теперь не знаем до какой строки листать)
        //переходим в ту же организацию
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']:nth-child(${rndOrganization + 1})"))
            .scrollIntoView(false)
        }
        element(byText(organizationName.toString())).click()
        //убеждаемся, что нашей метки нет
        element(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()='${newLabelList[0]}']"))
            .shouldNot(exist, ofSeconds(waitTime))
            .shouldNotBe(visible, ofSeconds(waitTime))

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
        //считаем строки и переходим в случайную службу
        val organizationTableStringCount = elements(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']")).size
        val rndOrganization = (1..organizationTableStringCount).random()
        if (rndOrganization == organizationTableStringCount){
            element(byCssSelector("body")).sendKeys(Keys.END)
        } else { element(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']:nth-child(${rndOrganization + 1})"))
            .scrollIntoView(false)

        }
        //переходим в дежурную службу
        element(byCssSelector("tbody>tr[data-testid^='MUIDataTableBodyRow-']:nth-child($rndOrganization)")).click()
        element(byCssSelector("form"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //element(byXpath("//div[@data-testid='officialId']/../..//span[@title='Перейти']/button"))
        //запоминаем ФИО руководителя
        val officialFIO = element(byXpath("//label[text()='Руководитель']/..//input")).getAttribute("value")
//        val companyName = element(byXpath("//label[text()='Организация']/..//input")).getAttribute("value")
        val officialFIOList = officialFIO?.split(" ")
//        println("officialFIO $officialFIO")
//        println("companyName $companyName")
        //label[text()='Руководитель']/..//input
        //кликаем на стрелку перехода к руководителю
        element(byXpath("//label[text()='Руководитель']/../../../..//span[@title='Перейти']/button"))
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

        //идем в справочник ДС что бы достать оттуда ДС и их организации, что бы лицо создавать с этими параметрами
        tools.menuNavigation("Справочники", "Дежурные службы", waitTime)
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //переключаемся на 500 записей на странице
        tools.stringsOnPage(500, waitTime)
        //выставляем отображение нужных столбцов
        tools.checkbox("Наименование", true, waitTime)
        tools.checkbox("Организация", true, waitTime)
        //ждем появления выставленных столбцов (последнего из них)
        element(byText("Организация"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //определяем порядковые номера столбцов, которые положим в хэш
//        var hotLineANDOrganization: MutableMap<String, String> = mutableMapOf("in" to "in")
//        hotLineANDOrganization.remove("in")
//        hotLineANDOrganization.clear()
//        var hotLineList: MutableListOf<String>
        var hotLineList = mutableListOf("init")
        hotLineList.remove("init")
        var organizationList = mutableListOf("init")
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


            hotLineList.add(element(byXpath("//tbody/tr[$u]/td[$hotlineColumnNumber]//*[text()]")).ownText)
            organizationList.add(element(byXpath("//tbody/tr[$u]/td[$organizationColumnNumber]//*[text()]")).ownText)

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
        var telR = (1000000..9999999).random()
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
        element(byXpath("//input[@name='hotlineId']")).click()
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
        element(byCssSelector("button[data-testid='Поиск-iconButton']")).click()
        element(byCssSelector("input[placeholder^='ФИО']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder^='ФИО']")).sendKeys(dateTime, Keys.ENTER)
        //открываем три точки
        element(byXpath("//div[contains(text(),'$dateTime')]/parent::td/parent::tr/td[@data-colindex='11']//button"))
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
        element(byXpath("//table/thead//*[name()='svg'][@id='expandable-button']/parent::span/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        val atributeSelector = "//label[text()='%s']/following-sibling::span[text()='%s']"
        //проверяем что в истории
        element(byXpath(atributeSelector.format("Дежурная служба", hotlineId)))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath(atributeSelector.format("Организация", companyId)))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //разворачиваем ФИО и пр.
        element(byXpath("//label[text()='ФИО']/parent::li/div/div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
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
        //ждем загрузки таблицы
        element(byCssSelector("main table>tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //воспользуемся поиском
        element(byCssSelector("button[data-testid='Поиск-iconButton']")).click()
        element(byCssSelector("input[placeholder^='ФИО']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder^='ФИО']")).sendKeys(dateTime, Keys.ENTER)
        //открываем три точки
        element(byXpath("//div[contains(text(),'$dateTime')]/parent::td/parent::tr/td[@data-colindex='11']//button"))
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
        element(byXpath("//tbody/tr//div[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
//        Thread.sleep(5000)
    }

}
