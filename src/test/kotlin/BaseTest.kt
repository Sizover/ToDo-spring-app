//import kotlin.collections.EmptyMap.keys

import com.codeborne.selenide.Browsers.CHROME
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.attribute
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byName
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.clearBrowserCookies
import com.codeborne.selenide.Selenide.clearBrowserLocalStorage
import com.codeborne.selenide.Selenide.closeWindow
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.Selenide.open
import com.codeborne.selenide.WebDriverRunner
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities
import test_library.alerts.AlertsEnum
import test_library.filters.FilterEnum
import test_library.filters.FilterTypeEnum
import test_library.menu.SubmenuInterface
import test_library.operator_data.OperatorDataEnum
import test_library.operator_data.OperatorDataEnum.values
import test_library.statuses.StatusEnum
import java.text.SimpleDateFormat
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random


open class BaseTest {
    //просто переменные с текущей датой, для различных целей
    public var date = LocalDate.now()
    public var dateTime = LocalDateTime.now()

    //"короткое" ожидание для совершения действия направленного на элемент страницы
    public val waitTime: Long = 5

    //"длинное" ожидание для совершения действия направленного на элемент страницы
    public val longWait: Long = 15

    // отладочная переменная для выведения (или нет) отладочной информации, в консоль
    public val print = true

    fun test10(){
        val options = ChromeOptions()
        options.addArguments("--disable-extensions")
        options.addArguments("--headless")
        options.addArguments("--no-sandbox")
        options.addArguments("--window-size=1920,1080")
        options.addArguments("--allow-file-access-from-files")
        options.addArguments("--use-fake-device-for-media-stream")
        options.addArguments("--use-fake-ui-for-media-stream")
        options.addArguments("--user-data-dir=/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/ChromeProfile")
        options.addArguments("--window-size=1920,1080")
        val capabilities = DesiredCapabilities()
        capabilities.setCapability(ChromeOptions.CAPABILITY, options)
        Configuration.browserCapabilities = capabilities
        open("/")
        options.addArguments("--proxy-bypass-list=<-loopback>")
//        val list = config.browserCapabilities().getCapability(ChromeOptions.CAPABILITY)["args"] as List<*>?
//        for (argument in list!!) {
//            options.addArguments(argument.toString())
//        }
//        options.merge(createCommonCapabilities(config, proxy))
    }




    fun logonTool() {

        //https://overcoder.net/q/1369284/%D0%BA%D0%B0%D0%BA-%D1%80%D0%B0%D0%B7%D1%80%D0%B5%D1%88%D0%B8%D1%82%D1%8C-%D0%B8%D0%BB%D0%B8-%D0%B7%D0%B0%D0%BF%D1%80%D0%B5%D1%82%D0%B8%D1%82%D1%8C-%D1%83%D0%B2%D0%B5%D0%B4%D0%BE%D0%BC%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BE-%D0%B2%D1%81%D0%BF%D0%BB%D1%8B%D0%B2%D0%B0%D1%8E%D1%89%D0%B5%D0%B9-%D0%BA%D0%B0%D0%BC%D0%B5%D1%80%D0%B5-%D0%BC%D0%B8%D0%BA%D1%80%D0%BE%D1%84%D0%BE%D0%BD%D0%B0


        //ChromeOptions addArguments


        //на случай невыполнения шага, ждем что бы можно было успеть глазками посмотреть и руками потыкать
        System.setProperty("webdriver.http.factory", "jdk-http-client")
//        val options = ChromeOptions()
//        options.addArguments("--remote-allow-origins=*")
//        val driver = ChromeDriver(options)
//        driver.get("https://test.kiap.local/")
//        driver.findElement(byXpath("//input[@id='username']")).sendKeys("a.sizov")
//        element(byName("username")).sendKeys("a.sizov")
//        element(byName("password")).sendKeys("a.sizov")
//        element(byName("login")).click()

        Configuration.timeout = 10000
        //выбираем браузер
//        Configuration.browser = FIREFOX
        Configuration.browser = CHROME
//
        Configuration.browserSize = "1920x1080"
        Configuration.holdBrowserOpen = false
        Configuration.webdriverLogsEnabled = false
        Configuration.headless = false
        Configuration.baseUrl = "https://test.kiap.local/"
        open("https://test.kiap.local/")
//        //open("https://stage.kiap.local/")
        //Костыль для обхода проблем с тестами которые не завершились и упали
        clearBrowserCookies()
        clearBrowserLocalStorage()
        closeWindow()
        //Thread.sleep(1000)
        open("https://test.kiap.local/")
//        //open("https://stage.kiap.local/")
//        //логинимся
        element(byName("username")).sendKeys("a.sizov")
        element(byName("password")).sendKeys("a.sizov")
        //element(byName("username")).sendKeys("test")
        //element(byName("password")).sendKeys("test!1+1")
        element(byName("login")).click()

    }


//    fun logonTool2(browser: String){
//
//        //https://overcoder.net/q/1369284/%D0%BA%D0%B0%D0%BA-%D1%80%D0%B0%D0%B7%D1%80%D0%B5%D1%88%D0%B8%D1%82%D1%8C-%D0%B8%D0%BB%D0%B8-%D0%B7%D0%B0%D0%BF%D1%80%D0%B5%D1%82%D0%B8%D1%82%D1%8C-%D1%83%D0%B2%D0%B5%D0%B4%D0%BE%D0%BC%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BE-%D0%B2%D1%81%D0%BF%D0%BB%D1%8B%D0%B2%D0%B0%D1%8E%D1%89%D0%B5%D0%B9-%D0%BA%D0%B0%D0%BC%D0%B5%D1%80%D0%B5-%D0%BC%D0%B8%D0%BA%D1%80%D0%BE%D1%84%D0%BE%D0%BD%D0%B0
//
//
//        //ChromeOptions addArguments
//
//
//        //на случай невыполнения шага, ждем что бы можно было успеть глазками посмотреть и руками потыкать
//        Configuration.timeout = 20000
//        //выбираем браузер
//        //Configuration.browser = FIREFOX
////        Configuration.browser = CHROME
////        Configuration.browser = browser
//        when (browser){
//            CHROME -> {
//                Configuration.browser = CHROME
//                WebDriverRunner.isChrome()
//                val chromeOpt = ChromeOptions()
//                chromeOpt.addArguments("use-fake-device-for-media-stream")
//                chromeOpt.addArguments("use-fake-ui-for-media-stream")
//            }
//            FIREFOX -> {
//                Configuration.browser = FIREFOX
//                WebDriverRunner.isFirefox()
//                val firefoxOpt = FirefoxOptions()
//                firefoxOpt.addArguments("-profile")
//                firefoxOpt.addArguments("/home/isizov/snap/firefox/common/.mozilla/firefox/4m2vdd0k.default")
//            }
//        }
//        Configuration.browserSize = "1920x1080"
//        Configuration.holdBrowserOpen = false
//        //Открываем КИАП
//        //Selenide.open("http://test.kiap.local:8000")
//
//        Selenide.open("https://test.kiap.local/")
//
//        //Костыль для обхода проблем с тестами которые не завершились и упали
//        clearBrowserCookies()
//        clearBrowserLocalStorage()
//        closeWindow()
//        //Thread.sleep(1000)
//        open("https://test.kiap.local/")
//        //логинимся
////        element(byName("username")).value = "a.sizov"
////        element(byName("password")).value = "a.sizov"
//        element(byName("username")).sendKeys("a.sizov")
//        element(byName("password")).sendKeys("a.sizov")
//        element(byName("login")).click()

    //    WebDriverRunner.isChrome()
    //    val options = ChromeOptions()
    //    options.addArguments("--disable-extensions")
    //    options.addArguments("--headless")
    //    options.addArguments("--no-sandbox")
    //    options.addArguments("--window-size=1920,1080")
    //    options.addArguments("--allow-file-access-from-files")
    //    options.addArguments("--use-fake-device-for-media-stream")
    //    options.addArguments("--use-fake-ui-for-media-stream")
    //    options.addArguments("--user-data-dir=/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/ChromeProfile")
    //    options.addArguments("--window-size=1920,1080")
    //    val capabilities = DesiredCapabilities()
    //    capabilities.setCapability(ChromeOptions.CAPABILITY, options)
    //    Configuration.browserCapabilities = capabilities
    //    //        open("/")
    //    val webDriver: WebDriver = ChromeDriver(options)
    //    webDriver.get("https://test.kiap.local/")
//    }

    fun anyLogonTool(username: String, password: String) {
        //https://overcoder.net/q/1369284/%D0%BA%D0%B0%D0%BA-%D1%80%D0%B0%D0%B7%D1%80%D0%B5%D1%88%D0%B8%D1%82%D1%8C-%D0%B8%D0%BB%D0%B8-%D0%B7%D0%B0%D0%BF%D1%80%D0%B5%D1%82%D0%B8%D1%82%D1%8C-%D1%83%D0%B2%D0%B5%D0%B4%D0%BE%D0%BC%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BE-%D0%B2%D1%81%D0%BF%D0%BB%D1%8B%D0%B2%D0%B0%D1%8E%D1%89%D0%B5%D0%B9-%D0%BA%D0%B0%D0%BC%D0%B5%D1%80%D0%B5-%D0%BC%D0%B8%D0%BA%D1%80%D0%BE%D1%84%D0%BE%D0%BD%D0%B0
        //ChromeOptions addArguments
        //на случай невыполнения шага, ждем что бы можно было успеть глазками посмотреть и руками потыкать
        Configuration.timeout = 20000
        //выбираем браузер
        //Configuration.browser = FIREFOX
//        Configuration.browser = CHROME
        WebDriverRunner.isChrome()
        Configuration.browserSize = "1920x1080"
        Configuration.holdBrowserOpen = false
        //Открываем КИАП
        //Selenide.open("http://test.kiap.local:8000")
        val opt = ChromeOptions()
        opt.addArguments("use-fake-device-for-media-stream")
        opt.addArguments("use-fake-ui-for-media-stream")
        Selenide.open("https://test.kiap.local/")
        //Костыль для обхода проблем с тестами которые не завершились и упали
        clearBrowserCookies()
        clearBrowserLocalStorage()
        closeWindow()
        //Thread.sleep(1000)
        open("https://test.kiap.local/")
        //логинимся
        element(byName("username")).sendKeys(username)
        element(byName("password")).sendKeys(password)
        element(byName("login")).click()
    }

    fun logoffTool() {
        clearBrowserCookies()
        clearBrowserLocalStorage()
        closeWindow()

    }

    fun authorizationTest() {
        //выбираем браузер
        //Configuration.browser = FIREFOX
        Configuration.browser = CHROME
        //Открываем КИАП
        open("https://test.kiap.local/")

    }

    fun logonDds() {
        //Selenide.open("http://test.kiap.local:8000")
        open("https://test.kiap.local/")
        //логинимся
        element(byName("username")).sendKeys("test-dds")
        element(byName("password")).sendKeys("test!1+1")
        element(byName("login")).click()

    }

    fun logon112() {
        Selenide.open("http://11202:11202@172.16.41.21/")
    }


    fun tableColumnCheckbox(checkboxesName: String, checkboxCondition: Boolean, waitTime: Long)
    //По названию колонки, необходимому значению чекбокса и waitTime выставляет отображаемые колонки в табличных РМ
    //При пустом имени, выклацывает весь список в указанное состояние
    //Допустимо передавать несколько значений разделяя их ; без пробелов
    {
        val checkboxTrue = "checkboxFocus"
        val checkboxFalse = "checkboxNormal"
//        val checkboxAlias = ""
        var checkboxNameListDraft = mutableListOf<String>()
        val checkboxNameList = mutableListOf<String>()
        // Определяем надо ли выставлять колонки и формируем их список
        if (checkboxesName.isNotEmpty()) {
            if (checkboxesName.contains(";")) {
                checkboxNameListDraft = checkboxesName.split(';').map { it.trim() }.toMutableList()
            } else {
                checkboxNameListDraft.add(checkboxesName)
            }
            checkboxNameListDraft.forEach { column ->
                if (element(byXpath("//table/thead//*[text()='$column']")).exists() != checkboxCondition){
                    checkboxNameList.add(column)
                }
            }
        }
        if (checkboxesName.isEmpty() || checkboxNameList.isNotEmpty()){
            //Открываем выпадающий список
            element(byXpath("//*[@name='table']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //Дожидаемся, что список появился
            element(byCssSelector("div[role='presentation'] label"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        //если передали пустое значение, то проходимся по всем чек-боксам, если нет, то нет =)
        if (checkboxesName.isEmpty()) {
            elements(byXpath("//label/span[text()]")).forEach {
                checkboxNameList.add(it.ownText)
            }
        }
        checkboxNameList.forEach {
            //проверяем что нам выдали и что надо сделать
            element(byXpath("//span[text()='$it']/parent::label//input"))
                .should(exist, ofSeconds(waitTime))
            var checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                .should(exist, ofSeconds(waitTime))
                .getAttribute("name")
            //если чекбокс выбран, а надо не выбирать
            if (checkboxState == checkboxTrue && !checkboxCondition) {
                //иногда драйвер опережает браузер и чек-бокс не прокликивается с первого раза, поэтому делаем так:
                while (checkboxState == checkboxTrue) {
                    element(byXpath("//span[text()='$it']/parent::label//input")).click()
                    checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                        .should(exist, ofSeconds(waitTime))
                        .getAttribute("name")
                }
                element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                    .shouldHave(attribute("name", checkboxFalse), ofSeconds(waitTime))
                //если чекбокс не выбран, а надо выбирать
            } else if (checkboxState == checkboxFalse && checkboxCondition) {
                //иногда драйвер опережает браузер и чек-бокс не прокликивается с первого раза, поэтому делаем так:
                while (checkboxState == checkboxFalse) {
                    element(byXpath("//span[text()='$it']/parent::label//input")).click()
                    checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                        .should(exist, ofSeconds(waitTime))
                        .getAttribute("name")
                }
                element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                    .shouldHave(attribute("name", checkboxTrue), ofSeconds(waitTime))

            }
        }
        if (checkboxesName.isEmpty() || checkboxNameList.isNotEmpty()){
            Thread.sleep(500)
            while (element(byXpath("//div[@role='presentation']")).exists()) {
                element(byXpath("//div[@role='presentation']")).click()
                Thread.sleep(500)
            }
            element(byXpath("//div[@role='presentation']"))
                .shouldNot(exist, ofSeconds(waitTime))
        }
    }



    fun inputRandomNew(inputName: String, parentInclusive: Boolean, waitTime: Long) {
        //Выбирает случайное значение в переданном по id импуте, учитывая иерархический он или нет
        while (elements(byXpath("//body/div[@role='presentation']//*[text()]")).size == 0) {
            element(byXpath("//input[@name='$inputName']")).click()
            Thread.sleep(500)
        }
        //определяем иерархический ли селект, и если да, то обрабатываем по новому, если нет, по старому
        if (elements(byCssSelector("body div[role='presentation'] li svg[name^='arrow']")).size > 0) {
            if (elements(byXpath("//body//div[@role='presentation']//li[1]//*[@name='arrowRight']")).size > 0) {
                element(byXpath("//div[@role='presentation']/div/ul/li[1]/div")).click()
                Thread.sleep(500)
            }
            val incTypes = mutableListOf<String>()
            val count = elements(byXpath("//div[@role='presentation']/div/ul/li")).size
            for (i in 1..count) {
                if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).size == 1) {
                    if (
                        ((elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[contains(@name,'arrow')]")).size == 0)
                            ||
                            parentInclusive)
                        &&
                        elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[@name='checkboxFocus']")).size == 0
                    ) {
                        incTypes.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
                    }
                }
            }
            val incTypesRandom = incTypes.random()
            element(byXpath("//div[@role='presentation']/div/ul//*[text()='$incTypesRandom']/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            while ((elements(byXpath("//div[@role='presentation']/div/ul//*[text()='$incTypesRandom']/ancestor::li//*[@name='checkboxFocus']")).size == 0)
                &&
                (elements(byCssSelector("div[role='presentation']")).size > 0)
            ) {
                element(byXpath("//div[@role='presentation']/div/ul//*[text()='$incTypesRandom']/ancestor::li"))
                    .click()
                Thread.sleep(100)
            }
//            element(byCssSelector("input[name='$inputName']")).sendKeys(incTypes.random(), Keys.DOWN, Keys.ENTER)
            if (elements(byCssSelector("div[role='presentation']")).size > 0) {
                element(byCssSelector("input[name='$inputName']"))
                    .click()
            }
        } else {
            var countInputString = 0
            do {
                element(byXpath("//input[@name='$inputName']")).sendKeys(Keys.DOWN)
                countInputString += 1
            } while (elements(byCssSelector("input[name='$inputName'][aria-activedescendant^='$inputName-option']")).size > 0)
            //выбираем случайную строчку из доступных
            val rndInputValue = (1 until countInputString).random()
            repeat(rndInputValue) {
                element(byXpath("//input[@name='$inputName']")).sendKeys(Keys.DOWN)
            }
            element(byXpath("//input[@name='$inputName']")).sendKeys(Keys.ENTER)
        }
    }


    fun menuNavigation(submenuInterface: SubmenuInterface, waitTime: Long){
    //просто унифицируем переход по различным РМ, что бы было легче поддерживать тесты
        val menu = submenuInterface.menu.name
        val subMenu = submenuInterface.subMenu
        element(byXpath("//div[@data-testid='app-menu-$menu']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Переходим в справочник "Видеокамеры"
        element(byXpath("//div[@data-testid='app-menu-$subMenu']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
    }

    fun addressInput(inputID: String,address: String, waitTime: Long)
    //унификация введения адреса
    {
        element(byCssSelector("#$inputID")).click()
        element(byXpath("//div[@role='presentation']//*[text()='Начните вводить адрес для подсказки']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        address.toList().forEach{
            element(byCssSelector("#$inputID")).sendKeys("$it")
            Thread.sleep(100)
        }
        element(byXpath("//div[@role='presentation']//*[text()='Начните вводить адрес для подсказки']"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//div[@role='presentation']//*[text()]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("#$inputID")).sendKeys(Keys.DOWN, Keys.ENTER)
        element(byXpath("//input[@id='$inputID']/parent::div//button[@title='Clear']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
    }


    fun tableStringsOnPage(stringsOnPage: Int, waitTime: Long){
        element(byCssSelector("div[aria-labelledby='RPP']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("div[role='presentation'] ul"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("div[role='presentation'] ul [data-value='$stringsOnPage']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("div[role='presentation'] ul"))
            .shouldNot(exist, ofSeconds(waitTime))
        Thread.sleep((10*stringsOnPage).toLong())
    }


    fun tableNumberOfColumn(columnName: String, waitTime: Long): Int{
        val columnsElements = elements(byXpath("//table/thead/tr/th"))
        val columsName = mutableListOf<String>()
        columnsElements.forEachIndexed{index, element ->
            if (elements(byXpath("//table/thead/tr/th[${index + 1}]//*[text()]")).size == 1){
                columsName.add(element(byXpath("//table/thead/tr/th[${index + 1}]//*[text()]")).ownText)
            } else {columsName.add("")}
                }
        return columsName.indexOf(columnName) + 1
    }


    fun shrinkCheckTool()
    //проверяем наплыв подписи на значение поля
    {
        Thread.sleep(500)
//        Assertions.assertTrue(
//            elements(byXpath("//label[@data-shrink]")).size ==
//                (elements(byXpath("//input[@type='text']")).size +
//                    elements(byXpath("//input[@type='number']")).size +
//                    elements(byXpath("//input[contains(@class,'Select')]")).size)
//        )
        for (i in 1..elements(byXpath("//input[@type='text']")).size){
            val shrinkStatus = element(byXpath("(//input[@type='text'])[$i]/parent::div/preceding-sibling::label")).getAttribute("data-shrink").toBoolean()
            if (element(byXpath("(//input[@type='text'])[$i]")).getAttribute("value")!!.isNotEmpty()
                || elements(byXpath("(//input[@type='text'])[$i]/preceding-sibling::div/span")).size > 0
                || elements(byXpath("(//input[@type='text'])[$i]/preceding-sibling::span")).size > 0
            ){
                Assertions.assertTrue(shrinkStatus)
            } else {Assertions.assertTrue(!shrinkStatus)}
        }
        for (i in 1..elements(byXpath("//input[@type='number']")).size){
            val shrinkStatus = element(byXpath("(//input[@type='number'])[$i]/parent::div/preceding-sibling::label")).getAttribute("data-shrink").toBoolean()
            if (element(byXpath("(//input[@type='number'])[$i]")).getAttribute("value")!!.isNotEmpty()){
                Assertions.assertTrue(shrinkStatus)
            } else {Assertions.assertTrue(!shrinkStatus)}
        }
        for (i in 1..elements(byXpath("//input[contains(@class,'Select')]")).size){
            val shrinkStatus = element(byXpath("(//input[contains(@class,'Select')])[$i]/parent::div/preceding-sibling::label")).getAttribute("data-shrink").toBoolean()
            if (element(byXpath("(//input[contains(@class,'Select')])[$i]")).getAttribute("value")!!.isNotEmpty()){
                Assertions.assertTrue(shrinkStatus)
            } else {Assertions.assertTrue(!shrinkStatus)}
        }
        for (i in 1..elements(byXpath("//textarea[@rows]")).size){
            val shrinkStatus = element(byXpath("(//textarea[@rows])[$i]/parent::div/preceding-sibling::label")).getAttribute("data-shrink").toBoolean()
            if (element(byXpath("(//textarea[@rows])[$i]")).ownText.isNotEmpty()){
                Assertions.assertTrue(shrinkStatus)
            } else {Assertions.assertTrue(!shrinkStatus)}
        }
    }


    fun checkAlert(alertEnum: AlertsEnum, text: String, clickButtonClose: Boolean, waitTime: Long){
        //Проверяем оранжевую всплывашку
        element(byXpath("//div[@role='alert']//*[@name='${alertEnum.name}']"))
            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        if (text.isNotEmpty()){
            element(byXpath("//div[@role='alert']//*[text()='$text']"))
                .should(exist, ofSeconds(waitTime))
        } else {
            element(byXpath("//div[@role='alert']//*[text()]"))
                .should(exist, ofSeconds(waitTime))
        }
        if (clickButtonClose){
            Thread.sleep(300)
            element(byXpath("//div[@role='alert']//*[@name='snackbarClose']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
        Thread.sleep(300)
    }

    //Что бы не править каждый тест, перевевожу создание и проверку полей КП на абстрактные методы для каждого поля
    fun createICToolCalltype(calltype: String, waitTime: Long){
        element(byCssSelector("div#calltype"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("div[role='presentation'] ul[aria-labelledby='calltype-label']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        if (calltype == "random" || calltype.isEmpty()){
            elements(byCssSelector("div[role='presentation'] ul[aria-labelledby='calltype-label'] > li"))
                .random()
                .click()
        } else {
            element(byXpath("//div[@role='presentation']//ul[@aria-labelledby='calltype-label']/li[text()='$calltype']"))
                .click()
        }
        element(byCssSelector("div[role='presentation'] ul[aria-labelledby='calltype-label']"))
            .shouldNot(exist, ofSeconds(waitTime))
    }

    fun createICToolPhone(phone: String, waitTime: Long){
        if (elements(byCssSelector("input#phone")).size == 1) {
            element(byCssSelector("input#phone"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            if (phone == "random" || phone.isEmpty()) {
                val telNumber = (100000000..999999999).random().toString()
                element(byCssSelector("input#phone"))
                    .sendKeys("+79$telNumber")
            } else {
                element(byCssSelector("input#phone"))
                    .sendKeys(phone)
            }
        }
    }

    fun createICToolLanguage(language: String, waitTime: Long){
        element(byCssSelector("div#calltype"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("div[role='presentation'] ul[aria-labelledby='calltype-label']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        if (language == "random"){
            elements(byCssSelector("div[role='presentation'] ul[aria-labelledby='language-label'] > li"))
                .random()
                .click()
        } else {
            element(byXpath("//div[@role='presentation']//ul[@aria-labelledby='language-label']/li[text()='$language']"))
                .click()
        }
        element(byCssSelector("div[role='presentation'] ul[aria-labelledby='language-label']"))
            .shouldNot(exist, ofSeconds(waitTime))
    }

    fun createICToolFIO(lastname: String, firstname: String, middlename: String, waitTime: Long){
        if (elements(byCssSelector("input[id^=fio]")).size == 3){
            if (lastname.isNotEmpty()){
                element(byCssSelector("input[id='fio.lastname']"))
                    .click()
                element(byCssSelector("input[id='fio.lastname']"))
                    .sendKeys(lastname)
            }
            if (lastname.isNotEmpty()){
                element(byCssSelector("input[id='fio.firstname']"))
                    .click()
                element(byCssSelector("input[id='fio.firstname']"))
                    .sendKeys(firstname)
            }
            if (lastname.isNotEmpty()){
                element(byCssSelector("input[id='fio.middlename']"))
                    .click()
                element(byCssSelector("input[id='fio.middlename']"))
                    .sendKeys(middlename)
            }
            elements(byXpath("//input[contains(@id,'fio.')]/parent::div[contains(@class,'error')]"))
                .should(CollectionCondition.size(0), ofSeconds(waitTime))
        }
    }


    fun createICToolIsThreatPeople(isThreatPeople: Boolean, victimsCount: String, victimsChildren: String, deathToll: String, deathChildren: String, waitTime: Long){
        val inputslist : List<List<String>> = listOf(listOf("victimsCount", victimsCount), listOf("victimsChildren", victimsChildren), listOf("deathToll", deathToll), listOf("deathChildren", deathChildren))
        element(byCssSelector("input#isThreatPeople"))
            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        //сначала проверяем и меняем при необходимости состояние чекбокса угороз
        if (element(byCssSelector("input#isThreatPeople")).getAttribute("value").toBoolean() != isThreatPeople){
            var checkboxCondition = element(byCssSelector("input#isThreatPeople")).getAttribute("value").toBoolean()
            while (checkboxCondition != isThreatPeople) {
                element(byCssSelector("input#isThreatPeople"))
                    .click()
                Thread.sleep(100)
                checkboxCondition = element(byCssSelector("input#isThreatPeople")).getAttribute("value").toBoolean()
            }
        }
        if (isThreatPeople) {
            inputslist.forEach{input ->
                if (input[1].isNotEmpty()) {
                    if (element(byCssSelector("input#${input[0]}")).getAttribute("value")!!.isNotEmpty()
                        && element(byCssSelector("input#${input[0]}")).getAttribute("value") != input[1]) {
                        element(byCssSelector("input#${input[0]}")).click()
                        element(byCssSelector("input#${input[0]}")).sendKeys(Keys.END)
                        repeat(element(byCssSelector("input#${input[0]}")).getAttribute("value")!!.length){
                            element(byCssSelector("input#${input[0]}")).sendKeys(Keys.BACK_SPACE)
                        }
                    }
                    element(byCssSelector("input#${input[0]}")).sendKeys(input[1])
                    Assertions.assertTrue(element(byCssSelector("input#${input[0]}")).getAttribute("value") == input[1])
                }
            }
        }
    }

    fun createICToolsDopInfo(dopInfo: String, waitTime: Long){
        element(byCssSelector("div[role='textbox']>p"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("div[role='textbox']"))
            .sendKeys(dopInfo)
    }

    fun createICToolLabels(Labels: String, waitTime: Long){
        element(byXpath("//label[text()='Метки']/..//input[@id='labelsId-autocomplete']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
    }

    fun createICToolRandomCoordinates(lat_lon: String, waitTime: Long){
        val lat_lonList: MutableList<String> = mutableListOf()
        if (lat_lon.isNotEmpty()){
            if (lat_lon.contains(';')){
                lat_lon.split(';').forEachIndexed { index, it ->
                    if (it.isNotEmpty()){
                        lat_lonList.add(it.trim())
                    } else {
                        if (index == 0){
                            lat_lonList.add(((10..70).random() + kotlin.random.Random.nextFloat()).toString())
                        } else if (index == 1){
                            lat_lonList.add(((10..100).random() + kotlin.random.Random.nextFloat()).toString())
                        } else {
                            if (print) println("Координаты переданы в неверном формате")
                            Assertions.assertTrue(false)
                        }
                    }
                }
            } else {
                if (print) println("Координаты переданы в неверном формате")
                Assertions.assertTrue(false)
            }
        } else {
            lat_lonList.add(((10..70).random() + kotlin.random.Random.nextFloat()).toString())
            lat_lonList.add(((10..100).random() + kotlin.random.Random.nextFloat()).toString())
        }
        if (lat_lonList.size != 2){
            if (print) println("Что-то пошло не так: координат не две")
            Assertions.assertTrue(false)
        } else {
            element(byXpath("//form//label[text()='Широта']/..//input[@name='lat']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//form//label[text()='Широта']/..//input[@name='lat']"))
                .sendKeys(lat_lonList[0])
            element(byXpath("//form//label[text()='Долгота']/..//input[@name='lon']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//form//label[text()='Долгота']/..//input[@name='lon']"))
                .sendKeys(lat_lonList[1])
        }
    }

    fun checkICToolIsStatus(status: StatusEnum, waitTime: Long) {
        elements(byXpath("//div[@id='incidentButtons']//button"))
            .shouldHave(CollectionCondition.size(2))
        element(byXpath("//div[@id='incidentButtons']//*[text()='${status.name}']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
    }

    fun checkICToolDopInfo(dopInfo: String, waitTime: Long) {
        element(byXpath("//form//*[text()='Дополнительная информация:']/..//div[@role='textbox']/*[text()='$dopInfo']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
    }

    fun updateICToolLabels(labelsList: List<String>, waitTime: Long){
        val availableLabelsList = mutableListOf<String>()
        val unavailableLabelsList = mutableListOf<String>()
        val randomLabelsList = mutableListOf<String>()
        element(byXpath("//div[@id='labels']//h3[text()='Метки']/following-sibling::span//*[text()='Изменить']/ancestor::button"))
            .click()
        element(byXpath("//div[@id='labels']//h3[text()='Метки']/following-sibling::span//*[text()='Изменить']/ancestor::button"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//div[@id='labels']//*[text()='Добавить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId' and @role='combobox']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Open']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Close']"))
            .should(exist, ofSeconds(waitTime))
        if (elements(byXpath("//body//div[@role='presentation']//li[1]//*[@name='arrowRight']")).size > 0) {
            element(byXpath("//div[@role='presentation']/div/ul/li[1]/div"))
                .should(exist, ofSeconds(waitTime))
                .click()
            element(byXpath("//body//div[@role='presentation']//li[1]//*[@name='arrowDown']"))
                .should(exist, ofSeconds(waitTime))
            Thread.sleep(500)
        }
        val count = elements(byXpath("//div[@role='presentation']/div/ul/li")).size
        labelsList.forEachIndexed(){index, label ->
            if (index != 0) {
                element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Open']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Close']"))
                    .should(exist, ofSeconds(waitTime))
            }
            for (i in 1..count ){
                if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).size == 1){
                    if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[name()='svg' and @name='checkboxNormal']")).size == 1){
                        availableLabelsList.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
                    } else if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[name()='svg' and @name='checkboxExpanded']")).size == 1){
                        unavailableLabelsList.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
                    }
                }
            }
            if (label == "random" && availableLabelsList.isNotEmpty()){
                val selectedLabel = availableLabelsList.random()
                element(byXpath("//div[@role='presentation']//*[text()='$selectedLabel']/ancestor::li"))
                    .click()
                element(byXpath("//div[@role='presentation']//*[text()='Добавить']/ancestor::button"))
                    .click()
                element(byXpath("//div[@id='labels']//span//*[text()='$selectedLabel']"))
                    .should(exist, ofSeconds(waitTime))
                randomLabelsList.add(selectedLabel)
            } else if (availableLabelsList.contains(label)){
                element(byXpath("//div[@role='presentation']//*[text()='$label']/ancestor::li"))
                    .click()
                element(byXpath("//div[@role='presentation']//*[text()='Добавить']/ancestor::button"))
                    .click()
                element(byXpath("//div[@id='labels']//span//*[text()='$label']"))
                    .should(exist, ofSeconds(waitTime))
            }
            availableLabelsList.clear()
            unavailableLabelsList.clear()
        }
        element(byXpath("//div[@role='presentation']//*[text()='Отменить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@id='labels']//*[text()='Сохранить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        labelsList.filter { it != "random" }
        val finishLabelList = (labelsList.filter { it != "random" } + randomLabelsList).toList()
        finishLabelList.forEach{
            element(byXpath("//div[@id='labels']//*[text()='$it']/ancestor::span[@title]"))
                .should(exist, ofSeconds(waitTime))
        }

    }

    fun updateICToolStatusOld(iCStatus: String, waitTime: Long){
        val statusList = listOf("Новая", "В обработке", "Реагирование", "Завершена", "Отменена","Закрыта")
        if (element(byXpath("//div[@id='incidentButtons']//button[1]//text()/..")).ownText != iCStatus) {
            element(byXpath("//div[@id='incidentButtons']//button[1]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']"))
                .should(exist, ofSeconds(waitTime))
/*            elements(byXpath("//div[@role='presentation']//button[not (@disabled)]"))
                .shouldHave(CollectionCondition.size(5), ofSeconds(waitTime))
            elements(byXpath("//div[@role='presentation']//button[@disabled]"))
                .shouldHave(CollectionCondition.size(1), ofSeconds(waitTime))
                пока не понятно как поступать с этими проверками в новой статусной модели*/
            if (iCStatus.isNotEmpty()) {
                element(byXpath("//div[@role='presentation']//*[text()='$iCStatus']/ancestor::button[not (@disabled)]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']"))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//div[@id='incidentButtons']//button[1]//*[text()='$iCStatus']"))
                    .should(exist, ofSeconds(waitTime))
            } else {
                val rndSt = elements(byXpath("//div[@role='presentation']//*[not(text()='Новая')]/ancestor::button[not (@disabled)]//text()/.."))
                    .random().ownText
                element(byXpath("//div[@role='presentation']//*[text()='$rndSt']/ancestor::button[not (@disabled)]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']"))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//div[@id='incidentButtons']//button[1]//*[text()='$rndSt']"))
                    .should(exist, ofSeconds(waitTime))
            }
        }
    }

    fun updateICToolStatus(iCStatusEnum: StatusEnum?, waitTime: Long) {
        //просто линейный список статусов задающий направление переходов
//        val statusList = listOf("Новая", "В обработке", "Реагирование", "Завершена", "Отменена", "Закрыта")
        val statusList = StatusEnum.values()
        //Доступные статусы для каждого статуса
        val possibleStatusesMap: LinkedHashMap<StatusEnum, List<StatusEnum>> = linkedMapOf(
            StatusEnum.Новая to listOf(StatusEnum.`В обработке`, StatusEnum.Реагирование, StatusEnum.Завершена, StatusEnum.Отменена, StatusEnum.Закрыта),
            StatusEnum.`В обработке` to listOf(StatusEnum.Реагирование, StatusEnum.Завершена, StatusEnum.Отменена, StatusEnum.Закрыта),
            StatusEnum.Реагирование to listOf(StatusEnum.Завершена, StatusEnum.Отменена, StatusEnum.Закрыта),
            StatusEnum.Завершена to listOf(StatusEnum.Закрыта),
            StatusEnum.Отменена to listOf(StatusEnum.Закрыта)
        )
        val targetedStatus: StatusEnum
        var nextStatus: StatusEnum
        //определяем текущий статус

//        var statusNow = element(byXpath("//div[@id='incidentButtons']//button[1]//text()/..")).ownText
        var statusNow = StatusEnum.values().find { it.name == element(byXpath("//div[@id='incidentButtons']//button[1]//text()/..")).ownText }
        //определяем целевой статус
        targetedStatus = iCStatusEnum ?: possibleStatusesMap[statusNow]!!.random()
        //пока не достигнем целового статуса выполняем последовательные переходы
        do {
            nextStatus = statusList[statusList.indexOf(statusNow) + 1]
            element(byXpath("//div[@id='incidentButtons']//*[text()='${statusNow!!.name}']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']"))
                .should(exist, ofSeconds(waitTime))
            Thread.sleep(200)
            elements(byXpath("//div[@role='presentation']//*[text()]/text()/ancestor::button[not (@disabled)]"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
            if (elements(byXpath("//div[@role='presentation']//*[text()='${targetedStatus.name}']/text()/ancestor::button[not (@disabled)]")).size ==1){
                nextStatus = targetedStatus
            }
            element(byXpath("//div[@role='presentation']//*[text()='${nextStatus.name}']/text()/ancestor::button[not (@disabled)]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']"))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//div[@id='incidentButtons']//button[1]//text()/parent::*[text()='${nextStatus.name}']"))
                .should(exist, ofSeconds(waitTime))
            //statusNow = element(byXpath("//div[@id='incidentButtons']//button[1]//text()/..")).ownText
            statusNow = nextStatus
            Thread.sleep(200)
        } while (statusNow != targetedStatus)
    }




    fun clearInput(xPathLocator: String, waitTime: Long){
        if (element(byXpath(xPathLocator)).getAttribute("value")!!.isNotEmpty()){
            element(byXpath("//button[@aria-label='open drawer']"))
                .hover()
            Thread.sleep(150)
            element(byXpath(xPathLocator)).click()
            element(byXpath(xPathLocator)).sendKeys(Keys.END)
            repeat(element(byXpath(xPathLocator)).getAttribute("value")!!.length){
                element(byXpath(xPathLocator)).sendKeys(Keys.BACK_SPACE)
            }
        }
    }


    fun `Черновик подсчета длины МД контента`(locatorXpath: String){
        //*[text()='Описание']/ancestor::div[2]//div[@role='textbox']//h1//*[text() and not(text()='#')]
//      (//*[text()='Описание']/ancestor::div[2]//div[@role='textbox']//strong)[1]//text()/parent::*[text() and not(text()='#')]
        var lengthMD: Int = 0
        val typeMD = listOf<String>("strong", "del", "mark", "code", "h1", "h2")
        typeMD.forEach { type ->
            if (elements(byXpath("$locatorXpath//$type")).size > 0){
                var numberOfCharacters =0
                when(type){
                    "strong", "del", "mark" -> {numberOfCharacters = 4}
                    "code", "h1" -> {numberOfCharacters = 2}
                    "h2" -> {numberOfCharacters = 3}
                }
//                lengthMD += (elements(byXpath("$locatorXpath//$type")).size) * numberOfCharacters
                for (i in 1..elements(byXpath("$locatorXpath//$type")).size){
                    lengthMD += element(byXpath("($locatorXpath//$type)[$i]//text()/parent::*[text() and not(text()='#')]")).ownText.length
                    lengthMD += numberOfCharacters
                }
            }
        }
    }

    fun tableSearch(searchValue: String, waitTime: Long){
        element(byXpath("//table/tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        while (elements(byXpath("//*[@name='search']/following-sibling::input[@placeholder]")).size == 0){
            element(byXpath("//*[@name='search']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
        element(byXpath("//*[@name='search']/following-sibling::input[@placeholder]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        clearInput("//*[@name='search']/following-sibling::input[@placeholder]", waitTime)
        element(byXpath("//*[@name='search']/following-sibling::input[@placeholder]"))
            .sendKeys(searchValue, Keys.ENTER)
        Thread.sleep(1000)
    }

    fun tableSearchClose(){
        element(byXpath("//table/tbody/tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        if (element(byXpath("//*[@name='search']/following-sibling::input[@placeholder]")).exists()){
            if (element(byXpath("//*[@name='search']/following-sibling::input[@placeholder]")).getAttribute("value")!!.isNotEmpty()){
                element(byXpath("//*[@name='search']/following-sibling::input[@placeholder]/ancestor::div[1]//*[@name='close']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            }
            element(byXpath("//*[@name='search']/following-sibling::input[@placeholder]/ancestor::div[1]//*[@name='close']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }

    }

    fun pushButtonCreateIC(dopInfo: String, waitTime: Long){
        element(byXpath("//*[text()='Сохранить карточку']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        try {
            element(byXpath("//div[@role='tabpanel' and @id='simple-tabpanel-card']/form"))
                .should(exist, ofSeconds(waitTime))
        } catch (_:  Throwable) {
            println("pushButtonCreateIC said \"БЛЭТ!\"")
            element(byCssSelector("table#incidents"))
                .should(exist, ofSeconds(waitTime))
            tableColumnCheckbox("Описание", true, waitTime)
            element(byXpath("//*[text()='$dopInfo']/text()/ancestor::td"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='tabpanel' and @id='simple-tabpanel-card']/form"))
                .should(exist, ofSeconds(waitTime))
            checkICToolIsStatus(StatusEnum.`В обработке`, waitTime)
        }
    }

    //абстракция применения фильтра по имени фильтра и переданным значениям,
    //в случае если значение не передано, "но логика работы фильтра это допускает", по метод подставляет произвольное случайное значение
    fun setFilterByEnumOld(filter: FilterEnum, filterValues: String, waitTime: Long){

        val filterFullName = filter.filterAlias.fullName
        val filterShortName = filter.filterAlias.shortName
        val filterType = filter.filterAlias.type
        val clickLocator = filter.filterAlias.type.filterType.clickLocator
        val valueLocator = filter.filterAlias.type.filterType.valueLocator
        //список значений фильтра для унификации последующего кода
        val listOfTargetValues: MutableList<String> = mutableListOf()
        //для контроля изменения цвета кнопки фильтра запомним текущий класс, т.к. цвет зашифрован в стиль, который зашифрован в класс
        var filterMainButtonColor: String = ""
        //флаг полезли ли мы в "Еще фильтры" или "Все фильтры"
        val checkButtonClass: Boolean
        //количество действующих фильтров в "Еще фильтры" или "Все фильтры"
        var amountFilters = 0
        //ждем
        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
        //если не надо лезть в "Еще фильтры", то
        if (element(byXpath("//form[@novalidate]//*[text()='$filterShortName']/ancestor::button")).exists()){
            checkButtonClass = true
            //для контроля изменения цвета основной кнопки фильтра запомним текущий класс, т.к. цвет зашифрован в стиль, который зашифрован в класс
            filterMainButtonColor = element(byXpath("//form[@novalidate]//*[text()='$filterShortName']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .getCssValue("background-color")
            //жмем кнопку фильтра
            element(byXpath("//form[@novalidate]//*[text()='$filterShortName']/ancestor::button"))
                .click()
        } else {
            //если полезли в "Еще фильтры"
            checkButtonClass = false
            //фиксируем число текущих фильтров в "Еще фильтры", с учетом того что цифры может и не быть (0 фильтров не указывается)
            with(
                element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button//*[text()]"))
                    .ownText
                    .substringAfterLast('(')
                    .substringBeforeLast(')')
            ){
                amountFilters = if(!this.contains("фильтры...")){
                    this.toInt()
                } else {
                    0
                }
            }
            filterMainButtonColor = element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .getCssValue("background-color")
            element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
        //ждем
        element(byXpath("//div[@role='presentation']//*[text()='$filterFullName']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //формируем применяемый список значений, если на вход передана пустая строка, то выбираем одно случайное значение
        if (filterValues.trim().isNotEmpty()) {
            if (filterValues.contains(';')) {
                filterValues.split(';').forEach { oneValue ->
                    listOfTargetValues.add(oneValue.trim())
                }
            } else {
                listOfTargetValues.add(filterValues.trim())
            }
        } else {
            //формируем применяемый список значений, если на вход передана пустая строка, то выбираем одно случайное значение
            if ((filterType == FilterTypeEnum.POOLBUTTONS) || (filterType == FilterTypeEnum.RADIOBUTTON)){
                listOfTargetValues.add(
                    elements(byXpath(valueLocator?.invoke(filterFullName).toString()))
                        .random()
                        .innerText()
                )
            } else if (filterType == FilterTypeEnum.HIERARCHICATALOG){
                //открываем весь список
                element(byXpath("//*[text()='$filterFullName']/ancestor::div[@data-testid]//input"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("(//div[@role='presentation']//*[@name='arrowRight'])[1]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                elements(byXpath("//div[@role='presentation']"))
                    .shouldHave(CollectionCondition.size(2), ofSeconds(waitTime))
                listOfTargetValues
                    .add(elements(byXpath(valueLocator?.invoke(filterFullName).toString()))
                        .random()
                        .ownText)
            } else if (filterType == FilterTypeEnum.DATE){
                val rndStart = (1..365).random()
                val rndEnd = (1..rndStart).random()
                listOfTargetValues.add(LocalDate.now().minusDays(rndStart.toLong()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString())
                listOfTargetValues.add(LocalDate.now().minusDays(rndEnd.toLong()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString())
            } else {
                if (print){println("для данного фильтра затруднительно выбрать случайное значение")}
                Assertions.assertTrue(false)}
        }
        //теперь добавляем в фильтр значения по сформированному списку
        listOfTargetValues.forEachIndexed {indexOfFilterValue, oneFilterValue ->
            if (filterType == FilterTypeEnum.POOLBUTTONS || filterType == FilterTypeEnum.RADIOBUTTON){
                //для каждой кнопки единичного значения отслеживаем изменение цвета
                val filterButtonColor = element(byXpath(clickLocator(filterFullName, oneFilterValue)))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .getCssValue("background-color")
                //чтобы обойти машинный глюк с несработавшим кликом, кликаем в цикле
                while (element(byXpath(clickLocator(filterFullName, oneFilterValue))).getCssValue("background-color") == filterButtonColor) {
                    element(byXpath(clickLocator(filterFullName, oneFilterValue)))
                        .click()
                    Thread.sleep(200)
                }
            } else if (filterType == FilterTypeEnum.HIERARCHICATALOG){
                //открываем весь список, если до этого мы не выбирали случайное значение и уже открыли список
                if (filterValues.isNotEmpty() && indexOfFilterValue == 0){
                    element(byXpath("//*[text()='$filterFullName']/ancestor::div[@data-testid]//input"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("(//div[@role='presentation']//*[@name='arrowRight'])[1]"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    elements(byXpath("//div[@role='presentation']"))
                        .shouldHave(CollectionCondition.size(2), ofSeconds(waitTime))
                }
                //чтобы обойти машинный глюк с несработавшим кликом, кликаем в цикле
                while (element(byXpath(clickLocator(filterFullName, oneFilterValue))).exists()){
                    element(byXpath(clickLocator(filterFullName, oneFilterValue)))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(300)
                }
            } else if (filterType == FilterTypeEnum.DATE){
                //можно еще вставить проверку на то что даты не больше сегодняшней, но пока оставлю так
//                var dateS = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(oneFilterValue)
                if (listOfTargetValues.size == 2){
                    if (listOfTargetValues[0].trim().isNotEmpty()
                        && listOfTargetValues[1].trim().isNotEmpty()
                        && (SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(listOfTargetValues[0])
                            >
                            SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(listOfTargetValues[1]))
                        ){
                        if (print){println("в списке два значения, но значение \"с\" больше значения \"по\"") }
                        Assertions.assertTrue(false)
                    }
                    if (indexOfFilterValue == 0){
                        if (oneFilterValue.trim().isNotEmpty()){
                            element(byXpath(filter.filterAlias.type.filterType.clickLocator(filterFullName, "с")))
                                .should(exist, ofSeconds(waitTime))
                                .shouldBe(visible, ofSeconds(waitTime))
                                .sendKeys(oneFilterValue +"0000")
                        }
                    } else if ((indexOfFilterValue == 1)) {
                        if (oneFilterValue.trim().isNotEmpty()){
                            element(byXpath(filter.filterAlias.type.filterType.clickLocator(filterFullName, "по")))
                                .should(exist, ofSeconds(waitTime))
                                .shouldBe(visible, ofSeconds(waitTime))
                                .sendKeys(oneFilterValue +"2359")
                        }
                    }
                } else {
                    if (print){println("в списке значений не два, что не соответствует функционалу фильтра по датам") }
                    Assertions.assertTrue(false)}//в списке значений более двух, что не соответствует функционалу фильтра по датам
            } else if (filterType == FilterTypeEnum.FLATCATALOG){
                with(element(byXpath(clickLocator(filterFullName, "")))){
                    this
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    this.sendKeys(oneFilterValue.trim())
                    Thread.sleep(1000)
                    this.sendKeys(Keys.DOWN, Keys.ENTER)
                }
            } else {
                if (print){println("Переданный энум отсутствует в перечислении")}
                Assertions.assertTrue(false)
            }
        }
        //применяем фильтр
        element(byXpath("//div[@role='presentation']//*[text()='Применить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем
        element(byXpath("//div[@role='presentation']//*[text()='$filterFullName']"))
            .shouldNot(exist, ofSeconds(waitTime))
        //проверяем изменение "цвета" или цифры в "Еще фильтры"
        if (checkButtonClass) {
            element(byXpath("//form[@novalidate]//*[text()='$filterShortName']/ancestor::button//button//*[@name='close']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем изменение "цвета" кнопки фильтра
            Assertions.assertTrue(
                element(byXpath("//form[@novalidate]//*[text()='$filterShortName']/ancestor::button"))
                    .getCssValue("background-color")
                    != filterMainButtonColor
            )
        } else {
            //В хорошем случае мы проверим изменение цвета кнопки, в плохом - выявим неполноценность такой проверки в сценарии else
            if (amountFilters == 0){
                Assertions.assertTrue(element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .getCssValue("background-color") != filterMainButtonColor)
            } else {Assertions.assertTrue(element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .getCssValue("background-color") == filterMainButtonColor)}
            Assertions.assertTrue(
                element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button//*[text()]"))
                    .ownText
                    .substringAfterLast('(')
                    .substringBeforeLast(')')
                    .toInt()
                    == amountFilters + 1
            )
        }
    }

    fun setFilterByEnum(filter: FilterEnum, filterValues: String, waitTime: Long){
        // single: Boolean?,
        val filterFullName = filter.filterAlias.fullName
        val filterShortName = filter.filterAlias.shortName
        val filterType = filter.filterAlias.type
        val clickLocator = filter.filterAlias.type.filterType.clickLocator
        val valueLocator = filter.filterAlias.type.filterType.valueLocator
        //список значений фильтра для унификации последующего кода
        val listOfTargetValues: MutableList<String> = mutableListOf()
        //ждем
        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
        Thread.sleep(200)
        //Если такой фильтр уже применен, то сбрасываем, потом установим новое значение
        if (element(byXpath("//form[@novalidate]//button//*[starts-with(text(),'$filterShortName')]/ancestor::button//button")).exists()){
            element(byXpath("//form[@novalidate]//button//*[starts-with(text(),'$filterShortName')]/ancestor::button//button"))
                .click()
            element(byXpath("//form[@novalidate]//button//*[starts-with(text(),'$filterShortName')]"))
                .shouldNot(exist, ofSeconds(waitTime))
        }
        // заходим в кнопку "Фильртры"
        element(byXpath("//form[@novalidate]//*[@aria-label='Фильтры']//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .scrollIntoView("{block: \"center\"}")
            .click()
        //ждем
        element(byXpath("//div[@role='presentation']//*[text()='Фильтры']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//div[@role='presentation']//*[text()='$filterFullName']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //формируем применяемый список значений, если на вход передана пустая строка, то выбираем одно случайное значение, если возможно
        if (filterValues.trim().isNotEmpty()) {
            if (filterValues.contains(';')) {
                filterValues.split(';').forEach { oneValue ->
                    listOfTargetValues.add(oneValue.trim())
                }
            } else {
                listOfTargetValues.add(filterValues.trim())
            }
        } else {
            //формируем применяемый список значений, если на вход передана пустая строка, то выбираем одно случайное значение
            if ((filterType == FilterTypeEnum.POOLBUTTONS) || (filterType == FilterTypeEnum.RADIOBUTTON)){
                listOfTargetValues.add(
                    elements(byXpath(valueLocator?.invoke(filterFullName).toString()))
                        .random()
                        .innerText()
                )
            } else if (filterType == FilterTypeEnum.HIERARCHICATALOG){
                //открываем весь список
                element(byXpath("//*[text()='$filterFullName']/ancestor::div[@data-testid]//input"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("(//div[@role='presentation']//*[@name='arrowRight'])[1]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                elements(byXpath("//div[@role='presentation']"))
                    .shouldHave(CollectionCondition.size(2), ofSeconds(waitTime))
                listOfTargetValues
                    .add(elements(byXpath(valueLocator?.invoke(filterFullName).toString()))
                        .random()
                        .ownText)
            } else if (filterType == FilterTypeEnum.DATE){
                val rndStart = (1..365).random()
                val rndEnd = (1..rndStart).random()
                listOfTargetValues.add(LocalDate.now().minusDays(rndStart.toLong()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString())
                listOfTargetValues.add(LocalDate.now().minusDays(rndEnd.toLong()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString())
            } else {
                if (print){println("для данного фильтра затруднительно выбрать случайное значение")}
                Assertions.assertTrue(false)}
        }
        //теперь добавляем в фильтр значения по сформированному списку
        listOfTargetValues.forEachIndexed {indexOfFilterValue, oneFilterValue ->
            if (filterType == FilterTypeEnum.POOLBUTTONS || filterType == FilterTypeEnum.RADIOBUTTON){
                //для каждой кнопки единичного значения отслеживаем изменение цвета
                val filterButtonColor = element(byXpath(clickLocator(filterFullName, oneFilterValue)))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .getCssValue("background-color")
                //чтобы обойти машинный глюк с несработавшим кликом, кликаем в цикле
                while (element(byXpath(clickLocator(filterFullName, oneFilterValue))).getCssValue("background-color") == filterButtonColor) {
                    element(byXpath(clickLocator(filterFullName, oneFilterValue)))
                        .click()
                    Thread.sleep(200)
                }
            } else if (filterType == FilterTypeEnum.HIERARCHICATALOG){
                //открываем весь список, если до этого мы не выбирали случайное значение и уже открыли список
                if (filterValues.isNotEmpty() && indexOfFilterValue == 0){
                    element(byXpath("//*[text()='$filterFullName']/ancestor::div[@data-testid]//input"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("(//div[@role='presentation']//*[@name='arrowRight'])[1]"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    elements(byXpath("//div[@role='presentation']"))
                        .shouldHave(CollectionCondition.size(2), ofSeconds(waitTime))
                }
                //чтобы обойти машинный глюк с несработавшим кликом, кликаем в цикле
                val test = oneFilterValue.substringBefore("&||&").trim()
                while (element(byXpath(clickLocator(filterFullName, oneFilterValue.substringBefore("&||&").trim()))).exists()){
                    element(byXpath(clickLocator(filterFullName, oneFilterValue.substringBefore("&||&").trim())))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(300)
                }
                //удаляем то что не выбирали, для этого опознаем спец комбинацию
//                Assertions.assertTrue(withFriends.)
                if (oneFilterValue.contains("&||&")){
                    elements(byXpath("//*[text()='$filterFullName']/ancestor::div[@role='presentation']/following-sibling::div[@role='presentation']//li[not(.//*[text()='${oneFilterValue.substringBefore("&||&").trim()}'])]//*[@name='checkboxFocus']"))
                        .forEach { el ->
                            el.click()
                            Thread.sleep(300)
                    }
                }
                //сворачиваем выпадающий список
                //println(element(byXpath("//div[@role='presentation'][last()]")).innerHtml())
                element(byXpath("//*[text()='$filterFullName']/ancestor::div[@data-testid]//input/..//button[@aria-label='Close']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            } else if (filterType == FilterTypeEnum.DATE){
                //можно еще вставить проверку на то что даты не больше сегодняшней, но пока оставлю так
//                var dateS = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(oneFilterValue)
                if (listOfTargetValues.size == 2){
                    if (listOfTargetValues[0].trim().isNotEmpty()
                        && listOfTargetValues[1].trim().isNotEmpty()
                        && (SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(listOfTargetValues[0])
                            >
                            SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).parse(listOfTargetValues[1]))
                    ){
                        if (print){println("в списке два значения, но значение \"с\" больше значения \"по\"") }
                        Assertions.assertTrue(false)
                    }
                    if (indexOfFilterValue == 0){
                        if (oneFilterValue.trim().isNotEmpty()){
                            element(byXpath(filter.filterAlias.type.filterType.clickLocator(filterFullName, "с")))
                                .should(exist, ofSeconds(waitTime))
                                .shouldBe(visible, ofSeconds(waitTime))
                                .sendKeys(oneFilterValue +"0000")
                        }
                    } else if ((indexOfFilterValue == 1)) {
                        if (oneFilterValue.trim().isNotEmpty()){
                            element(byXpath(filter.filterAlias.type.filterType.clickLocator(filterFullName, "по")))
                                .should(exist, ofSeconds(waitTime))
                                .shouldBe(visible, ofSeconds(waitTime))
                                .sendKeys(oneFilterValue +"2359")
                        }
                    }
                } else {
                    if (print){println("в списке значений не два, что не соответствует функционалу фильтра по датам") }
                    Assertions.assertTrue(false)}//в списке значений более двух, что не соответствует функционалу фильтра по датам
            } else if (filterType == FilterTypeEnum.FLATCATALOG){
                with(element(byXpath(clickLocator(filterFullName, "")))){
                    this
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    this.sendKeys(oneFilterValue.trim())
                    Thread.sleep(1000)
                    this.sendKeys(Keys.DOWN, Keys.ENTER)
                }
            } else {
                if (print){println("Переданный энум отсутствует в перечислении")}
                Assertions.assertTrue(false)
            }
        }
        //применяем фильтр
        element(byXpath("//div[@role='presentation']//*[text()='Применить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем
        element(byXpath("//div[@role='presentation']//*[text()='$filterFullName']"))
            .shouldNot(exist, ofSeconds(waitTime))
        Thread.sleep(200)
        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//form[@novalidate]//button//*[starts-with(text(),'$filterShortName')]/ancestor::button//button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
    }


    fun cleanFilterByEnum(filtersList: List<FilterEnum>, waitTime: Long){
        //для контроля изменения цвета кнопки фильтра запомним текущий цвет
        var filterButtonColor = ""
        //количество действующих фильтров в "Еще фильтры" или "Все фильтры"
        var amountFilters = 0
        //список фильтров к очистке
        var listOfTargetFiltersEnum: MutableList<FilterEnum> = mutableListOf()
        //список фильтров к очистке у которых не оказалось отдельной кнопки
        val listOfNoButtonTargetFilters: MutableList<FilterEnum> = mutableListOf()
        //WA разного поведения фильтров в таблице происшествий и в архиве происшествий
        var temporaryCount = 0
        //В зависимости от переданного на вход списка, очищаем все или точечно
        if (filtersList.isNotEmpty()){
            filtersList.forEach { filterEnum ->
                element(byXpath("//form[@novalidate]//button//*[starts-with(text(),'${filterEnum.filterAlias.shortName}')]/ancestor::button//button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//form[@novalidate]//button//*[starts-with(text(),'${filterEnum.filterAlias.shortName}')]"))
                    .shouldNot(exist, ofSeconds(waitTime))
            }
        } else {
            // заходим в кнопку "Фильртры"
            element(byXpath("//form[@novalidate]//*[@aria-label='Фильтры']//button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//div[@role='presentation']//*[text()='Фильтры']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //жмем отчистить все
            element(byXpath("//div[@role='presentation']//*[text()='Очистить все']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//div[@role='presentation']//*[text()='Фильтры']"))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//form[@novalidate]//button//text()/ancestor::button//button"))
                .shouldNot(exist, ofSeconds(waitTime))
        }
    }

    //абстракция очистки фильтра по подписи в кнопке
    fun cleanFilterByEnumOld(filtersList: List<FilterEnum>, waitTime: Long){
        //для контроля изменения цвета кнопки фильтра запомним текущий цвет
        var filterButtonColor = ""
        //количество действующих фильтров в "Еще фильтры" или "Все фильтры"
        var amountFilters = 0
        //список фильтров к очистке
        var listOfTargetFiltersEnum: MutableList<FilterEnum> = mutableListOf()
        //список фильтров к очистке у которых не оказалось отдельной кнопки
        val listOfNoButtonTargetFilters: MutableList<FilterEnum> = mutableListOf()
        //WA разного поведения фильтров в таблице происшествий и в архиве происшествий
        var temporaryCount = 0
        //В зависимости от переданного на вход списка, наполняем список по которому будем работать
        listOfTargetFiltersEnum = if (filtersList.isEmpty()){
            FilterEnum.values().toMutableList()
        } else {
            filtersList.toMutableList()
        }

        listOfTargetFiltersEnum.forEach { oneFilterEnum ->
            if (element(byXpath("//form[@novalidate]//*[text()='${oneFilterEnum.filterAlias.shortName}']/text()/ancestor::button//button//*[@name='close']")).exists()) {
                //для контроля изменения цвета кнопки фильтра запомним его
                filterButtonColor =
                    element(byXpath("//form[@novalidate]//*[text()='${oneFilterEnum.filterAlias.shortName}']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .getCssValue("background-color")
                //очищаем фильтр по кнопке крестика в кнопке фильтра
                with(element(byXpath("//*[text()='${oneFilterEnum.filterAlias.shortName}']/text()/ancestor::button//*[@name='close']//ancestor::button[1]"))){
                    this.should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //WA разного поведения фильтров в таблице происшествий и в архиве происшествий
                    while (this.exists() && temporaryCount < 5) {
                        this.click()
                        Thread.sleep(300)
                        temporaryCount += 1
                    }
                }
                //ждем
                //WA разного поведения фильтров в таблице происшествий и в архиве происшествий
                if (temporaryCount < 5){
                    element(byXpath("//form[@novalidate]//*[text()='${oneFilterEnum.filterAlias.shortName}']/text()/ancestor::button//button//*[@name='close']"))
                        .shouldNot(exist, ofSeconds(waitTime))
                }

                //убеждаемся в изменениии "цвета"
                //WA разного поведения фильтров в таблице происшествий и в архиве происшествий
                if (temporaryCount < 5){
                    Assertions.assertTrue(
                        element(byXpath("//form[@novalidate]//*[text()='${oneFilterEnum.filterAlias.shortName}']/text()/ancestor::button"))
                            .getCssValue("background-color")
                            != filterButtonColor
                    )
                }
            } else {
                listOfNoButtonTargetFilters.add(oneFilterEnum)
            }
        }
        //если нет отдельной кнопки фильтра
        if (listOfNoButtonTargetFilters.isNotEmpty()
            && element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры (')]/ancestor::button")).exists()) {
            amountFilters =
                element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button//*[text()]"))
                    .ownText
                    .substringAfterLast('(')
                    .substringBeforeLast(')')
                    .toInt()
            filterButtonColor =
                element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .getCssValue("background-color")
            element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//div[@role='presentation']//*[text()='Применить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            listOfNoButtonTargetFilters.forEach { oneFilterEnum ->
                val filterFullName = oneFilterEnum.filterAlias.fullName
                val filterShortName = oneFilterEnum.filterAlias.shortName
                val filterType = oneFilterEnum.filterAlias.type
                val clickLocator = oneFilterEnum.filterAlias.type.filterType.clickLocator
                val cleanLocator = oneFilterEnum.filterAlias.type.filterType.cleanLocator
                if (filterType == FilterTypeEnum.DATE) {
                    var dateClean = false
                    if (element(byXpath(clickLocator(filterFullName, "с"))).exists()){
                        if (element(byXpath(clickLocator(filterFullName, "с"))).getAttribute("value")!!.isNotEmpty()) {
                            clearInput(clickLocator(filterFullName, "с"), waitTime)
                            dateClean = true
                        }
                    }
                    if (element(byXpath(clickLocator(filterFullName, "по"))).exists()){
                        if (element(byXpath(clickLocator(filterFullName, "по"))).getAttribute("value")!!.isNotEmpty()) {
                            clearInput(clickLocator(filterFullName, "по"), waitTime)
                            dateClean = true
                        }
                    }
                    if (dateClean) {
                        amountFilters -= 1
                    }
                } else if ((filterType == FilterTypeEnum.FLATCATALOG) || (filterType == FilterTypeEnum.HIERARCHICATALOG)) {
                    if (element(byXpath(cleanLocator?.invoke(filterFullName).toString())).exists()) {
                        element(byXpath(cleanLocator?.invoke(filterFullName).toString()))
                            .hover()
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        amountFilters -= 1
                    }
                } else if (filterType == FilterTypeEnum.POOLBUTTONS){
                    if (element(byXpath(cleanLocator?.invoke(filterFullName).toString())).exists()){
                        if (element(byXpath(cleanLocator?.invoke(filterFullName).toString()))
                                .getCssValue("color") != "rgba(231, 234, 239, 1)"
                        ){
                            //rgba(231, 234, 239, 1) - фильтр пуст (color)
                            element(byXpath(cleanLocator?.invoke(filterFullName).toString()))
                                .hover()
                                .shouldBe(visible, ofSeconds(waitTime))
                                .click()
                            amountFilters -= 1
                        }
                    }

                } else if (filterType == FilterTypeEnum.RADIOBUTTON){
                    if (element(byXpath(cleanLocator?.invoke(filterFullName).toString())).exists()){
                        if (element(byXpath(cleanLocator?.invoke(filterFullName).toString()))
                                .getCssValue("color") == "rgba(77, 80, 88, 1)"
                        //rgba(77, 80, 88, 1) - Радиобатон не выбран
                        ){element(byXpath(cleanLocator?.invoke(filterFullName).toString()))
                            .hover()
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                            amountFilters -= 1
                        }
                    }
                } else {
                    if (print) {
                        println("В переборе не учтены все существующие энумы")
                    }
                    Assertions.assertTrue(false)
                }
            }
            while (element(byXpath("//div[@role='presentation']//*[text()='Применить']/text()/ancestor::button")).exists()) {
                element(byXpath("//div[@role='presentation']//*[text()='Применить']/text()/ancestor::button"))
                    .click()
                Thread.sleep(500)
            }
            with(
                element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button//*[text()]"))
                    .ownText
                    .substringAfterLast('(')
                    .substringBeforeLast(')')
            ) {
                if (amountFilters == 0) {
                    Assertions.assertTrue(this.contains(" фильтры..."))
                    Assertions.assertTrue(
                        element(byXpath("//form[@novalidate]//*[contains(text(),' фильтры')]/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .getCssValue("background-color")
                            != filterButtonColor
                    )
                } else {
                    Assertions.assertTrue(this.toInt() == amountFilters)
                }
            }
        }
    }

    fun operatorData(what: OperatorDataEnum): String {
        val operatorDataMap = mutableMapOf<String, String>()
        //кликаем по иконке оператора сверху справа
        element(byXpath("//header//button//*[@name='user']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //пероеходим в профиль пользователя
        element(byCssSelector("a[href='/profile']>button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("a[href='/profile']>button"))
            .click()
        //Извлекаем имя оператора
        values().forEach {
            element(byXpath("//p[text()='${it.name}:']/following-sibling::p"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            operatorDataMap.put(it.name, element(byXpath("//p[text()='${it.name}:']/following-sibling::p")).ownText.trim())
        }
//        element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//        val operatorFIO = element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p")).ownText.trim()
        return operatorDataMap[what.name].toString()
    }

    fun enterTextInMDtextboxByName(textboxName: String, enterText: String, waitTime: Long){
        //element(byXpath("//*[text()='$textboxName']/ancestor::div[3]//div[@role='textbox']//p[last()]"))
        element(byXpath("//*[text()='$textboxName']/../following-sibling::*//div[@role='textbox']//p[last()]"))
            //*[text()='$textboxName']/ancestor::div[3]//div[@role='textbox']//p[@data-empty-text='Нажмите здесь, чтобы вводить текст']
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='$textboxName']/ancestor::div[3]//div[@role='textbox']"))
            .sendKeys(enterText)
    }

    //методы генерирования некоторых человекоподобных имен
    val c = "бвгджзклмнпрстфхцчшщ".toList()
    val v = "аеиоуэюя".toList()
    val cc = "бббвввввввввгггдддддджжзззккккккклллллллллмммммнннннннннннннппппппрррррррррсссссссссссттттттттттттфххцчччшшщ".toList()
    val vv = "ааааааааааааааааеееееееееееееееееиииииииииииииииооооооооооооооооооооооуууууэюяяяя".toList()
    val lnTails = listOf("ов", "ин", "ев")
    val posTails = listOf("ер", "ор", "ист", "ик")
    val cityTails = listOf("дар", "град", "рск", "йск", "во", "хабль", "бург")
    val e = "abcdefghijklmnopqrstuvwxyz".toList()

    fun generateFirstNameI(): String {
        var name = ""
        for (i in 1..(2..5).random()){
            name = if (i == 1){
                name + cc.random()
            } else {
                name + vv.random() + cc.random()
            }
        }
        if (Random.nextBoolean()){
            name = vv.random() + name
        }
        return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
    fun generateMiddleNameO(): String {
        return if (Random.nextBoolean()){
            generateFirstNameI() + "овна"
        } else generateFirstNameI() + "ович"
    }
    fun generateLastNameF(): String {
        return generateFirstNameI() + lnTails.random()
    }
    fun generateEmail(): String {
        var email = ""
        repeat((2..6).random()){
            email += e.random()
        }
        email += "@"
        repeat((2..6).random()){
            email += e.random()
        }
        email += ".ru"
        return email
    }


}