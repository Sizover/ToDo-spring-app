//import kotlin.collections.EmptyMap.keys
import com.codeborne.selenide.Browsers.CHROME
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition
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
import org.testng.asserts.Assertion
import java.time.Duration
import java.time.Duration.ofSeconds


open class BaseTest {
    fun logonTool(){

        //https://overcoder.net/q/1369284/%D0%BA%D0%B0%D0%BA-%D1%80%D0%B0%D0%B7%D1%80%D0%B5%D1%88%D0%B8%D1%82%D1%8C-%D0%B8%D0%BB%D0%B8-%D0%B7%D0%B0%D0%BF%D1%80%D0%B5%D1%82%D0%B8%D1%82%D1%8C-%D1%83%D0%B2%D0%B5%D0%B4%D0%BE%D0%BC%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BE-%D0%B2%D1%81%D0%BF%D0%BB%D1%8B%D0%B2%D0%B0%D1%8E%D1%89%D0%B5%D0%B9-%D0%BA%D0%B0%D0%BC%D0%B5%D1%80%D0%B5-%D0%BC%D0%B8%D0%BA%D1%80%D0%BE%D1%84%D0%BE%D0%BD%D0%B0


        //ChromeOptions addArguments


        //на случай невыполнения шага, ждем что бы можно было успеть глазками посмотреть и руками потыкать
        Configuration.timeout = 10000
        //выбираем браузер
//        Configuration.browser = FIREFOX
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
        element(byName("username")).sendKeys("a.sizov")
        element(byName("password")).sendKeys("a.sizov")
//        element(byName("username")).sendKeys("test")
//        element(byName("password")).sendKeys("test!1+1")
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
//    }

    fun anyLogonTool(username: String, password: String){
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

    fun logoffTool(){
        clearBrowserCookies()
        clearBrowserLocalStorage()
        closeWindow()

    }
    fun aThreeHundredAndTenOne(){
        //на случай невыполнения шага, ждем что бы можно было успеть глазками посмотреть и руками потыкать
        Configuration.timeout = 10000
        //выбираем браузер
        //Configuration.browser = FIREFOX
        Configuration.browser = CHROME
        //Открываем КИАП
        open("https://test.kiap.local/")

    }
    fun logonDds(){
        //Selenide.open("http://test.kiap.local:8000")
        open("https://test.kiap.local/")
        //логинимся
        element(byName("username")).sendKeys("test-dds")
        element(byName("password")).sendKeys("test!1+1")
        element(byName("login")).click()

    }
    fun logon112(){
        Selenide.open("http://11202:11202@172.16.41.21/")
    }




    fun checkbox(checkboxName: String, checkboxCondition: Boolean, waitTime: Long)
    //По названию колонки, необходимому значению чекбокса и waitTime выставляет отображаемые колонки в табличных РМ
    //При пустом имени, выклацывает весь список в указанное состояние
    //Допустимо передавать несколько значений разделяя их ; без пробелов
    {
        val checkboxTrue = "checkboxFocus"
        val checkboxFalse = "checkboxNormal"
//        val checkboxAlias = ""
        val checkboxNameList = mutableListOf<String>()
        //Открываем выпадающий список
        element(byXpath("//*[@name='table']/../parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Дожидаемся, что список появился
        element(byCssSelector("div.MuiPaper-root.MuiPopover-paper.MuiPaper-rounded[class*=MuiPaper-elevation] label"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //если передали пустое значение, то проходимся по всем чек-боксам, если нет, то нет =)
        if (checkboxName.isEmpty()){
            val checkboxes = elements(byXpath("//label/span[text()]"))
            checkboxes.forEach{
                checkboxNameList.add(it.ownText)
            }
        } else if (checkboxName.contains(";")){
            checkboxName.split(";").forEach {
                checkboxNameList.add(it)
            }
        } else {
            checkboxNameList.add(checkboxName)
        }
        checkboxNameList.forEach{
            //проверяем что нам выдали и что надо сделать
            element(byXpath("//span[text()='$it']/parent::label//input"))
                .should(exist, ofSeconds(waitTime))
            var checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                .should(exist, ofSeconds(waitTime))
                .getAttribute("name")
            //если чекбокс выбран, а надо не выбирать
            if (checkboxState == checkboxTrue && !checkboxCondition){
                //иногда драйвер опережает браузер и чек-бокс не прокликивается с первого раза, поэтому делаем так:
                while (checkboxState == checkboxTrue){
                    element(byXpath("//span[text()='$it']/parent::label//input")).click()
                    checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                        .should(exist, ofSeconds(waitTime))
                        .getAttribute("name")
                }
                element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                    .shouldHave(attribute("name", checkboxFalse), ofSeconds(waitTime))
                //если чекбокс не выбран, а надо выбирать
            } else if (checkboxState == checkboxFalse && checkboxCondition){
                //иногда драйвер опережает браузер и чек-бокс не прокликивается с первого раза, поэтому делаем так:
                while (checkboxState == checkboxFalse){
                    element(byXpath("//span[text()='$it']/parent::label//input")).click()
                    checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                        .should(exist, ofSeconds(waitTime))
                        .getAttribute("name")
                }
                element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                    .shouldHave(attribute("name", checkboxTrue), ofSeconds(waitTime))

            }
        }
        element(byXpath("//div[@role='presentation']")).click()
    }

    fun inputRandomOld(inputName: String)
    //Выбирает случайное значение в переданном импуте (сначала проклацывает весь список, считая проклацывания, потом проклацывает, до некоторого случайного значения)
    {
        while (elements(byXpath("//body/div[@role='presentation']//*[text()]")).size == 0){
            element(byXpath("//input[@name='$inputName']")).click()
            Thread.sleep(500)
        }
        var countInputString = 0
        do {
            element(byXpath("//input[@name='$inputName']")).sendKeys(Keys.DOWN)
            countInputString += 1
        } while (elements(byCssSelector("input[name='$inputName'][aria-activedescendant^='$inputName-option']")).size > 0)
        //выбираем случайную организацию из доступных
        val rndInputValue = (1 until countInputString).random()
        repeat(rndInputValue){
            element(byXpath("//input[@name='$inputName']")).sendKeys(Keys.DOWN)
        }
        element(byXpath("//input[@name='$inputName']")).sendKeys(Keys.ENTER)

    }


    fun inputRandomNew(inputName: String, parentInclusive: Boolean, waitTime: Long){
        //Выбирает случайное значение в переданном по id импуте, учитывая иерархический он или нет
        while (elements(byXpath("//body/div[@role='presentation']//*[text()]")).size == 0){
            element(byXpath("//input[@name='$inputName']")).click()
            Thread.sleep(500)
        }
        //определяем иерархический ли селект, и если да, то обрабатываем по новому, если нет, по старому
        if (elements(byCssSelector("body div[role='presentation'] li svg[name^='arrow']")).size > 0){
            if (elements(byXpath("//body//div[@role='presentation']//li[1]//*[@name='arrowRight']")).size > 0) {
                element(byXpath("//div[@role='presentation']/div/ul/li[1]/div")).click()
                Thread.sleep(500)
            }
            val incTypes = mutableListOf<String>()
            val count = elements(byXpath("//div[@role='presentation']/div/ul/li")).size
            for (i in 1..count ){
                if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).size == 1){
                    if (
                        ((elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[contains(@name,'arrow')]")).size == 0)
                        ||
                        parentInclusive)
                        &&
                        elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[@name='checkboxFocus']")).size == 0){
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
                (elements(byCssSelector("div[role='presentation']")).size > 0)) {
                element(byXpath("//div[@role='presentation']/div/ul//*[text()='$incTypesRandom']/ancestor::li"))
                    .click()
                Thread.sleep(100)
                }
//            element(byCssSelector("input[name='$inputName']")).sendKeys(incTypes.random(), Keys.DOWN, Keys.ENTER)
            if (elements(byCssSelector("div[role='presentation']")).size > 0){
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
            repeat(rndInputValue){
                element(byXpath("//input[@name='$inputName']")).sendKeys(Keys.DOWN)
            }
            element(byXpath("//input[@name='$inputName']")).sendKeys(Keys.ENTER)
        }
    }

    fun menuNavigation(menu: String, subMenu: String, waitTime: Long)
    //просто унифицируем переход по различным РМ, что бы было легче поддерживать тесты
    {
        element(byXpath("//div[@data-testid='app-menu-$menu']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Переходим в справочник "Видеокамеры"
        element(byXpath("//div[@data-testid='app-menu-$subMenu']/../.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //временная строка до починки бага
//        element(byXpath("//button[@data-testid='app-bar-button']")).click()
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


    fun firstHalfIC(testName: String, date: String, dateTime: String, waitTime: Long){
        //Источник события - выбираем случайно
        element(byCssSelector("div#calltype"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        val iI = (1..10).random()
        element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($iI)")).click()
        //ФИО
        if (elements(byCssSelector("#phone")).size > 0){
            val tel = (1000000..9999999).random()
            element(byCssSelector("#phone")).sendKeys("918$tel")
        }
        //ФИО
        if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
            && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
            element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date $testName AutoTestLastname")
            element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
        }
        //адрес укажем на карте
        element(byCssSelector("span[title='Указать на карте']>button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[contains(@class,'leaflet-touch-drag')][contains(@class,'leaflet-touch-zoom')]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//span[text()='Применить']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //заполняем дополнительную информацию
        element(byCssSelector("div[role='textbox']>p"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("div[role='textbox']")).sendKeys("AutoTest $testName $dateTime")
    }

    fun stringsOnPage(stringsOnPage: Int, waitTime: Long){
        element(byCssSelector("div[aria-labelledby='RPP']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector(".MuiList-padding"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector(".MuiList-padding > [data-value='$stringsOnPage']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector(".MuiList-padding"))
            .shouldNot(exist, ofSeconds(waitTime))
        Thread.sleep((10*stringsOnPage).toLong())
    }


    fun numberOfColumn(columnName: String, waitTime: Long): Int{
//        val columnsElements = elements(byXpath("//table/thead/tr/th//*[text()]"))
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



    fun checkGreenAlert(text: String, clickButton: Boolean, waitTime: Long){
        //Проверяем зеленую всплывашку
        element(byXpath("//div[@role='alert']//*[@name='snackbarSuccess']"))
            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//div[@role='alert']//*[text()='$text']"))
            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        if (clickButton){
            element(byXpath("//div[@role='alert']//*[@name='snackbarClose']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
        Thread.sleep(300)
    }

    //Что бы не править каждый тест, перевевожу создание и проверку полей КП на абстрактные методы для каждого поля
    fun createIcToolCalltype(calltype: String, waitTime: Long){
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
        element(byCssSelector("div[role='textbox']>p")).click()
        element(byCssSelector("div[role='textbox']"))
            .sendKeys(dopInfo)
    }

//    fun createICToolIsLabels(label: String, waitTime: Long){
//        element(byXpath("//div[@data-testid='labelsId']//button[@title='Open']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        if (){}
//    }

    fun checkICToolIsStatus(status: String, waitTime: Long) {
        elements(byXpath("//div[@id='incidentButtons']//button"))
            .shouldHave(CollectionCondition.size(2))
        element(byXpath("//div[@id='incidentButtons']//button[1]//*[text()='$status']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
    }

    fun checkICToolsDopInfo(dopInfo: String, waitTime: Long) {
        element(byXpath("//strong[text()='Дополнительная информация:']/parent::div[text()='$dopInfo']"))
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
//        for (i in 1..count ){
//            if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).size == 1){
//                if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[name()='svg' and @name='checkboxNormal']")).size == 1){
//                    availableLabelsList.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
//                } else if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[name()='svg' and @name='checkboxExpanded']")).size == 1){
//                    unavailableLabelsList.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
//                }
//            }
//        }
//        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Close']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()

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

    fun updateICToolStatus(iCStatus: String, waitTime: Long){
        if (element(byXpath("//div[@id='incidentButtons']//button[1]//text()/..")).ownText != iCStatus) {
            element(byXpath("//div[@id='incidentButtons']//button[1]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='presentation']"))
                .should(exist, ofSeconds(waitTime))
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


}