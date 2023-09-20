//import kotlin.collections.EmptyMap.keys

import com.codeborne.selenide.*
import com.codeborne.selenide.Browsers.CHROME
import com.codeborne.selenide.Browsers.FIREFOX
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.logevents.SelenideLogger
import io.qameta.allure.Allure
import io.qameta.allure.selenide.AllureSelenide
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.chrome.ChromeOptions.CAPABILITY
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxOptions.FIREFOX_OPTIONS
import org.openqa.selenium.remote.RemoteWebDriver
import org.testng.ITestResult
import org.testng.annotations.*
import test_library.IncidentLevels.IncidentLevelEnum
import test_library.alerts.AlertsEnum
import test_library.filters.FilterEnum
import test_library.filters.FilterTypeEnum
import test_library.icTabs.TabEnum
import test_library.menu.SubmenuInterface
import test_library.operator_data.OperatorDataEnum
import test_library.statuses.StatusEnum
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random


open class BaseTest {
    //просто переменные с текущей датой, для различных целей
    var date = LocalDate.now()
    var dateTime = LocalDateTime.now()
    val videoSessionsList = mutableListOf<String>()
    //Переменные определяемые через энвы/параметры запуска
    companion object {
        lateinit var standUrl: String
        lateinit var mainLogin: String
        lateinit var mainPassword: String
        lateinit var adminLogin: String
        lateinit var adminPassword: String
        lateinit var attachFolder: String
        lateinit var browserhead: String
        lateinit var no_sandbox: String
        lateinit var disable_gpu: String
        var remote_url: String? = null
        val options: MutableMap<String, Any> = HashMap()
/*        var video_env: String? = null*/
        var attach_video_after_success_test: String? = null
    }
    //<parameter name="video_env" value="${VIDEO_ENV}"/>



    //"короткое" ожидание для совершения действия направленного на элемент страницы
    val shortWait: Long = 5

    //"длинное" ожидание для совершения действия направленного на элемент страницы
    val longWait: Long = 15

    // отладочная переменная для выведения (или нет) отладочной информации, в консоль
    val print = true


    @Parameters("url", "remoteUrl")
    @BeforeSuite(alwaysRun = true)
    open fun getStandUrl(urlValue: String, remoteurl: String?){
        standUrl = urlValue
        remote_url = remoteurl
    }

    @Parameters("mainLogin", "mainPassword")
    @BeforeSuite(alwaysRun = true)
    open fun getMainLogin(mainLoginValue: String, mainPasswordValue: String){
        mainLogin = mainLoginValue
        mainPassword = mainPasswordValue
    }

    @Parameters("adminLogin", "adminPassword")
    @BeforeSuite(alwaysRun = true)
    open fun getAdminLogin(adminLoginValue: String, adminPasswordValue: String){
        adminLogin = adminLoginValue
        adminPassword = adminPasswordValue
    }

    @Parameters("attachFolder", "headless", "noSandbox", "disableGpu")
    @BeforeSuite(alwaysRun = true)
    open fun getBrowserSetting(attachFolderValue: String, headless: String, nosandbox: String, disablegpu: String){
        attachFolder = attachFolderValue
        browserhead = headless
        no_sandbox = nosandbox
        disable_gpu = disablegpu
    }

    @Parameters("attachVideoAfterSuccessTest")
    @BeforeSuite(alwaysRun = true)
    open fun getAttachSuccess(attachvideoaftersuccesstest: String?){
        attach_video_after_success_test = if (attachvideoaftersuccesstest == "true") {
            "true"
        } else {
            "false"
        }
    }

//        //https://overcoder.net/q/1369284/%D0%BA%D0%B0%D0%BA-%D1%80%D0%B0%D0%B7%D1%80%D0%B5%D1%88%D0%B8%D1%82%D1%8C-%D0%B8%D0%BB%D0%B8-%D0%B7%D0%B0%D0%BF%D1%80%D0%B5%D1%82%D0%B8%D1%82%D1%8C-%D1%83%D0%B2%D0%B5%D0%B4%D0%BE%D0%BC%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5-%D0%BE-%D0%B2%D1%81%D0%BF%D0%BB%D1%8B%D0%B2%D0%B0%D1%8E%D1%89%D0%B5%D0%B9-%D0%BA%D0%B0%D0%BC%D0%B5%D1%80%D0%B5-%D0%BC%D0%B8%D0%BA%D1%80%D0%BE%D1%84%D0%BE%D0%BD%D0%B0
    @Parameters("browserName")
    @BeforeMethod(alwaysRun = true)
    fun initDriver(_browser: String){
        val browser_Name = _browser
        // Поключение к selenoid
        //Переключатель на локальный/удаленный запуск тестов. Либо тут, либо в передаваемых параметрах
        Configuration.remote = remote_url
        options["enableVNC"] = true
        options["enableVideo"] = true
        options["enableLog"] = true
        options["profile.default_content_settings.popups"] = 0
        options["download.default_directory"] = attachFolder
        options["download.prompt_for_download"] = false
        options["download.directory_upgrade"] = true
        options["safebrowsing.enabled"] = true
        if (browser_Name == "chrome"){
            val chromeOptions = ChromeOptions()
            chromeOptions.addArguments("--use-fake-device-for-media-stream")
            chromeOptions.addArguments("--use-file-for-fake-audio-capture")
            chromeOptions.addArguments("--use-fake-ui-for-media-stream")
            //TODO после обновления (выхода новых версий) браузера/selenide надо раскоментить опцию --auto-accept-camera-and-microphone-capture
            // в текущем виде сборки (114.0.5735.90/6.15) браузер не запускается с ошибкой
            // "org.openqa.selenium.SessionNotCreatedException: Could not start a new session. Response code 500. Message: unknown error: Chrome failed to start: crashed.
            //  (unknown error: DevToolsActivePort file doesn't exist)
            //  (The process started from chrome location /usr/bin/google-chrome is no longer running, so ChromeDriver is assuming that Chrome has crashed.) "
            //chromeOptions.addArguments("--auto-accept-camera-and-microphone-capture")
            //chromeOptions.addArguments("-Dselenide.headless=true")
            if (no_sandbox.toBoolean()){
                chromeOptions.addArguments("--no-sandbox")
            }
            if (disable_gpu.toBoolean()){
                chromeOptions.addArguments("--disable-gpu")
            }
            Configuration.browserCapabilities.setCapability(CAPABILITY, chromeOptions)
            Configuration.browser = CHROME
        } else if (browser_Name == "firefox"){
            val fireFoxOptions = FirefoxOptions()
//            fireFoxOptions.addArguments("--disable-web-security")
//            fireFoxOptions.addArguments("--allow-running-insecure-content")
//            fireFoxOptions.addArguments("--use-fake-ui-for-media-stream")
            if (no_sandbox.toBoolean()){
                fireFoxOptions.addArguments("--no-sandbox")
            }
            if (disable_gpu.toBoolean()){
                fireFoxOptions.addArguments("--disable-gpu")
            }
            Configuration.browserCapabilities.setCapability(FIREFOX_OPTIONS, fireFoxOptions)
            Configuration.browser = FIREFOX
        }
        Configuration.browserCapabilities.setCapability("selenoid:options", options)
        Configuration.timeout = 10000
        Configuration.browserSize = "1920x1080"
        Configuration.holdBrowserOpen = false
        Configuration.webdriverLogsEnabled = false
        Configuration.headless = browserhead.toBoolean()
        Configuration.baseUrl = standUrl
        SelenideLogger.addListener("AllureSelenide", AllureSelenide())
    }

    fun getSessionId(): String {
        return (WebDriverRunner.getWebDriver() as RemoteWebDriver).sessionId.toString()
    }


    fun attachAllureVideo(sessionId: String) {
        try {
            val videoUrl = URL(remote_url?.substringBeforeLast(':') + "/video/" + sessionId + ".mp4")
            val `is` = getSelenoidVideo(videoUrl)
            Allure.addAttachment("Video", "video/mp4", `is`, "mp4")
            deleteSelenoidVideo(videoUrl)
        } catch (e: Exception) {
            println("attachAllureVideoError")
            e.printStackTrace()
        }
    }

    fun getSelenoidVideo(url: URL): InputStream? {
        var lastSize = 0
        var exit = 2
        for (i in 0..19) {
            try {
                val size = url.openConnection().getHeaderField("Content-Length").toInt()
                println("Content-Length: $size")
                println("i: $i")
                if (size > lastSize) {
                    lastSize = size
                    Thread.sleep(1500)
                } else if (size == lastSize) {
                    println("Content-Length: $size")
                    println("exit: $exit")
                    exit--
                    Thread.sleep(1000)
                }
                if (exit < 0) {
                    println("video ok!")
                    return url.openStream()
                }
            } catch (e: Exception) {
                println("getSelenoidVideo: " + e.message)
                //e.printStackTrace();
            }
        }
        return null
    }

    fun deleteSelenoidVideo(url: URL) {
        try {
            val deleteConn = url.openConnection() as HttpURLConnection
            deleteConn.setDoOutput(true)
            deleteConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            deleteConn.setRequestMethod("DELETE")
            deleteConn.connect()
            println("deleteSelenoidVideo")
            println(deleteConn.getResponseCode())
            println(deleteConn.getResponseMessage())
            deleteConn.disconnect()
        } catch (e: IOException) {
            println("deleteSelenoidVideoError")
            e.printStackTrace()
        }
    }


    fun logonTool(proxy: Boolean = false) {
        anyLogonTool(proxy, mainLogin, mainPassword)
    }

    fun anyLogonTool(proxy: Boolean, username: String, password: String) {
        Configuration.proxyEnabled = proxy
        if (proxy){
            Configuration.fileDownload = FileDownloadMode.PROXY
        } else {
            Configuration.fileDownload = FileDownloadMode.FOLDER
            Configuration.downloadsFolder = attachFolder
        }
        open(standUrl)
        //логинимся
        element(byName("username")).sendKeys(username)
        element(byName("password")).sendKeys(password)
        element(byName("login")).click()
    }

    fun authorizationTest() {
        open("https://test.kiap.local/")
    }


    fun logoffTool() {
        val sessionId = getSessionId()
        videoSessionsList.add(sessionId)
        clearBrowserCookies()
        clearBrowserLocalStorage()
        closeWindow()
        closeWebDriver()
    }

    @BeforeMethod(alwaysRun = true)
    fun cleanVideoList(result: ITestResult) {
        videoSessionsList.clear()
    }

    @AfterMethod(alwaysRun = true)
    fun attachVideoAfter(result: ITestResult) {
        try {
            val sessionId = getSessionId()
            videoSessionsList.add(sessionId)
            FileUtils.deleteDirectory(File(attachFolder))
            clearBrowserCookies()
            clearBrowserLocalStorage()
            closeWindow()
            closeWebDriver()
            val test = result.status
            if (result.status == ITestResult.FAILURE || (result.status == ITestResult.SUCCESS && attach_video_after_success_test.toBoolean())){
                videoSessionsList.forEach {
                    attachAllureVideo(it)
                }
            } else {
                videoSessionsList.forEach {
                    getSelenoidVideo(URL(remote_url?.substringBeforeLast(':') + "/video/" + it + ".mp4"))
                    deleteSelenoidVideo(URL(remote_url?.substringBeforeLast(':') + "/video/" + it + ".mp4"))
                }
            }
        } catch (_:  Throwable) {
        }

    }







    fun tableColumnCheckbox(checkboxesName: String, checkboxCondition: Boolean, waitTime: Long)
    //По названию колонки, необходимому значению чекбокса и waitTime выставляет отображаемые колонки в табличных РМ
    //При пустом имени, выклацывает весь список в указанное состояние
    //Допустимо передавать несколько значений разделяя их ; без пробелов
    {
        //открывался ли список, что бы закрывать его
        var open = false
        //константы состояния чекбоксов взятые из DOMа
        val checkboxTrue = "checkboxFocus"
        val checkboxFalse = "checkboxNormal"
        //черновик списка интересующих столбцов
        var checkboxNameListDraft = mutableListOf<String>()
        //чистовик списка интересующих столбцов
        val checkboxNameList = mutableListOf<String>()
        // Определяем надо ли выставлять колонки и формируем их список
        if (checkboxesName.isNotEmpty()) {
            //преобразуем переданную строку в черновой список
            if (checkboxesName.contains(";")) {
                checkboxNameListDraft = checkboxesName.split(';').map { it.trim() }.toMutableList()
            } else {
                checkboxNameListDraft.add(checkboxesName)
            }
            //если в текущей таблице интересующий нас столбец не в интересующем нас состоянии,
            //то добавляем столбец в чистовик
            checkboxNameListDraft.forEach { column ->
                if (element(byXpath("//table/thead//*[text()='$column']")).exists() != checkboxCondition){
                    checkboxNameList.add(column)
                }
            }
        }
        //если действия нужны над всеми столбцами или в таблице столбцы не так как мы хотим
        //то открываем список столбцов
        if (checkboxesName.isEmpty() || checkboxNameList.isNotEmpty()){
            open = true
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
        //если передали пустое значение, то проходимся по всем чек-боксам, добавляя их в чистовик
        if (checkboxesName.isEmpty()) {
            elements(byXpath("//label/span[text()]")).forEach {
                checkboxNameList.add(it.ownText)
            }
        }
        //по чистовику переводим чекбоксы в интересующее состояние
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
                    element(byXpath("//span[text()='$it']/parent::label//input"))
                        .scrollIntoView("{block: \"center\"}")
                        .click()
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
                    element(byXpath("//span[text()='$it']/parent::label//input"))
                        .scrollIntoView("{block: \"center\"}")
                        .click()
                    checkboxState = element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                        .should(exist, ofSeconds(waitTime))
                        .getAttribute("name")
                }
                element(byXpath("//span[text()='$it']/parent::label//*[name()='svg'][@name]"))
                    .shouldHave(attribute("name", checkboxTrue), ofSeconds(waitTime))

            }
        }
        //если открывали список чекбоксов, то закрываем его
        if (open){
            Thread.sleep(500)
            while (element(byXpath("//div[@role='presentation']")).exists()) {
                element(byXpath("//div[@role='presentation']")).click()
                Thread.sleep(500)
            }
            element(byXpath("//div[@role='presentation']"))
                .shouldNot(exist, ofSeconds(waitTime))
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


    fun setTableStringsOnPage(stringsOnPage: Int, waitTime: Long){
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



}