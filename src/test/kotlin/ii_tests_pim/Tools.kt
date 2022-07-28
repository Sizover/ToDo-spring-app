package ii_tests_pim

//import kotlin.collections.EmptyMap.keys
import com.codeborne.selenide.Browsers.CHROME
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.WebDriverRunner
import org.openqa.selenium.Keys
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration.ofSeconds
import java.time.LocalDateTime


class Tools {
    fun logonTool(){

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
//        element(byName("username")).value = "a.sizov"
//        element(byName("password")).value = "a.sizov"
        element(byName("username")).sendKeys("a.sizov")
        element(byName("password")).sendKeys("a.sizov")
        element(byName("login")).click()
    }

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
//        element(byName("username")).value = "a.sizov"
//        element(byName("password")).value = "a.sizov"
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
        //Selenide.open("http://test.kiap.local:8000")
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
    /*
    fun aThreeHundredAndTenTwo(){
        //на случай невыполнения шага, ждем что бы можно было успеть глазками посмотреть и руками потыкать
        Configuration.timeout = 10000
        //выбираем браузер
        //Configuration.browser = FIREFOX
        Configuration.browser = Browsers.CHROME
        //Открываем КИАП
        Selenide.open("http://test.kiap.local:8000")
        Selenide.element(Selectors.byName("username")).value = "test"
    }

    fun aThreeHundredAndTenThree(){
        //на случай невыполнения шага, ждем что бы можно было успеть глазками посмотреть и руками потыкать
        Configuration.timeout = 10000
        //выбираем браузер
        //Configuration.browser = FIREFOX
        Configuration.browser = Browsers.CHROME
        //Открываем КИАП
        Selenide.open("http://test.kiap.local:8000")
        Selenide.element(Selectors.byName("username")).value = "test"
        Selenide.element(Selectors.byName("password")).value = "test"
    }


     */
//    //добавляем в таблицу происшествий столбец "Дополнительная информация"
//    element(byCssSelector("button[data-testid='Колонки-iconButton']")).click()
//    element(byCssSelector("input[value='description']")).click()
//    //ждем пока выделится чекбокс
//    val checkboxTrue = "path(\"M 19 3 H 5 C 3.89 3 3 3.9 3 5 V 19 C 3 20.1 3.89 21 5 21 H 19 C 20.11 21 21 20.1 21 19 V 5 C 21 3.9 20.11 3 19 3 Z M 10 17 L 5 12 L 6.41 10.59 L 10 14.17 L 17.59 6.58 L 19 8 L 10 17 Z\")"
//    element(byXpath("//span[text()='Дополнительная информация']/../span/span/*[name()='svg']/*[name()='path']"))
//    .shouldHave(cssValue("d", checkboxTrue), ofSeconds(waitTime))
//    element(byCssSelector("button[aria-label='Close']")).click()


    fun checkbox(checkboxName: String, checkboxCondition: Boolean, waitTime: Long)
    //По названию колонки, необходимому значению чекбокса и waitTime выставляет отображаемые колонки в табличных РМ
    //При пустом имени, выклацывает весь список в указанное состояние
    {
        val checkboxTrue = "M19 3H5c-1.11 0-2 .9-2 2v14c0 1.1.89 2 2 2h14c1.11 0 2-.9 2-2V5c0-1.1-.89-2-2-2zm-9 14l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"
        val checkboxFalse = "M19 5v14H5V5h14m0-2H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2z"
//        val checkboxAlias = ""
        val checkboxNameList = mutableListOf<String>()
        //Открываем выпадающий список
        element(byCssSelector("button[data-testid='Колонки-iconButton']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Дожидаемся, что список появился
        element(byCssSelector("fieldset[aria-label='Показать/скрыть колонки']>div>label"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //если передали пустое значение, то проходимся по всем чек-боксам, если нет, то нет =)
        if (checkboxName.isEmpty()){
            val checkboxesCount = elements(byXpath("//fieldset[@aria-label='Показать/скрыть колонки']//label/span[text()]"))
                .size
            for (i in 1..checkboxesCount){
                checkboxNameList.add(element(byXpath("//fieldset[@aria-label='Показать/скрыть колонки']//label[$i]/span[text()]")).ownText)
            }
        } else {
            checkboxNameList.add(checkboxName)
        }
        checkboxNameList.forEach{
            //проверяем что нам выдали и что надо сделать
            element(byXpath("//span[text()='$it']/parent::label//input"))
                .should(exist, ofSeconds(waitTime))
            var checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='path']"))
                .should(exist, ofSeconds(waitTime))
                .getAttribute("d")
            //если чекбокс выбран, а надо не выбирать
            if (checkboxState == checkboxTrue && !checkboxCondition){
                //иногда драйвер опережает браузер и чек-бокс не прокликивается с первого раза, поэтому делаем так:
                while (checkboxState == checkboxTrue){
                    element(byXpath("//span[text()='$it']/parent::label//input")).click()
                    checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='path']"))
                        .should(exist, ofSeconds(waitTime))
                        .getAttribute("d")
                }
                element(byXpath("//span[text()='$it']/parent::label//*[name()='path']"))
                    .shouldHave(attribute("d", checkboxFalse), ofSeconds(waitTime))
                //если чекбокс не выбран, а надо выбирать
            } else if (checkboxState == checkboxFalse && checkboxCondition){
                //иногда драйвер опережает браузер и чек-бокс не прокликивается с первого раза, поэтому делаем так:
                while (checkboxState == checkboxFalse){
                    element(byXpath("//span[text()='$it']/parent::label//input")).click()
                    checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='path']"))
                        .should(exist, ofSeconds(waitTime))
                        .getAttribute("d")
                }
                element(byXpath("//span[text()='$it']/parent::label//*[name()='path']"))
                    .shouldHave(attribute("d", checkboxTrue), ofSeconds(waitTime))
            }
        }
        element(byCssSelector("button[aria-label='Close']")).click()
    }

    fun inputRundom(inputName: String)
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
    }

    fun addressInput(inputID: String,address: String, waitTime: Long)
    //унификация введения адреса
    {
        element(byCssSelector("#$inputID")).click()
//        element(byCssSelector("#callAddress[aria-controls='callAddress-popup']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//div[@role='presentation']//*[text()='Начните вводить адрес для подсказки']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val addressList = address.split(" ")

//        address.toList()


        address.toList().forEach{
//        addressList.forEach{
            element(byCssSelector("#$inputID")).sendKeys("$it")
            Thread.sleep(100)
        }
//        element(byCssSelector("#$inputID")).sendKeys(address)
//        Thread.sleep(1000)
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
//        val dateTime = LocalDateTime.now().toString()
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
}