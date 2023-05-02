


import com.codeborne.selenide.Condition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.DownloadOptions
import com.codeborne.selenide.FileDownloadMode
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import test_library.menu.MyMenu
import test_library.menu.SubmenuInterface
import java.io.File
import java.io.FileReader
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Duration.ofSeconds


class PlayGround : BaseTest(){





//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `Черновик теста на создание МО`() {
        //создадим пару МО, один оставив навсегда, а второй создавая и удаляя каждый раз
    val moATItWas = mutableListOf<String>()
    val moATCreated = mutableListOf<String>("AutoTest T 0020 МО")
    logonTool(false)
    menuNavigation(MyMenu.Dictionaries.Municipalities, waitTime)
    tableColumnCheckbox("", true, waitTime)
    Thread.sleep(1000)
    //внесем существующие телефонные коды в отдельный список
//       var telCodeElementsCollection = elements(byXpath(""))
    val telCodeList = mutableListOf<String>()
    //Выясняем в каком столбце хранятся телефонные коды
    val telCodeColumnIndex = tableNumberOfColumn("Телефонный код", waitTime)
        //Выясняем в каком столбце хранятся телефонные коды
//        val telCodeElements = elements(byXpath("//thead/tr/th"))
//        telCodeElements.forEachIndexed{index, element ->
//            println(index.toString() + " ," + element.ownText )
//            if (element.ownText == "Телефонный код"){
//                telCodeColumnIndex = index + 1
//            }
//        }
//        for (i in 1..elements(byXpath("//thead/tr/th")).size){
//            if (element(byXpath("//thead/tr/th[$i]//*[text()]")).ownText == "Телефонный код"){
//                telCodeColumnIndex = i
//            }
//        }
        //Внесем телефонные коды в отдельный список
        //пагинации теперь нет
//        do {
//            telCodeElementsCollection = elements(byXpath("//tbody/tr/td[$telCodeColumnIndex]//*[text()]"))
//            telCodeElementsCollection.forEach {
//                telCodeList.add(it.ownText)
//            }
//            //побеждаем пагинацию, проходясь по всему справочнику и собирая телефонное коды
//            val pagesLi = elements(byXpath("//nav/ul/li")).size
//            if (!element(byXpath("//nav/ul/li[${pagesLi - 2}]/div")).getAttribute("style")?.contains("color: blue")!!){
//                element(byXpath("//nav/ul/li[${pagesLi - 1}]/div")).click()
//            }
//            Thread.sleep(1000)
//        }
//        while (!element(byXpath("//nav/ul/li[${pagesLi - 2}]/div")).getAttribute("style")?.contains("color: blue")!!)
        while(elements(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).size == 1){
            element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).click()
        }
        val telCodeElementsCollection = elements(byXpath("//tbody/tr/td[$telCodeColumnIndex][text()]"))
            telCodeElementsCollection.forEach {
                telCodeList.add(it.ownText)
            }
        //воспользуемся поиском, что бы найти МО с "AutoTest" в названии
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[@name='search']/following-sibling::input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[@name='search']/following-sibling::input"))
            .sendKeys("AutoTest", Keys.ENTER)
        Thread.sleep(1000)
        if (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0){
            val moNameColumn = tableNumberOfColumn("Наименование", waitTime)
            val moParentNameColumn = tableNumberOfColumn("Субъект", waitTime)

        }
        //если они есть, то составляем их список, не удаляем из него "AutoTest Основное МО" (что бы еще раз не искать его потом)
        // и поштучно удаляем все МО из списка
        if (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0) {
            val autotestMOElementsList = elements(byXpath("//tbody/tr/td[1]//*[contains(text(),'AutoTest')]"))
            //можно при наполнении moATItWas через else if проверить на соответствие заголовка МО "AutoTest Основное МО"
            // и удалить его из moAtCreated в таком случае
            //или просто потом проверить есть ли "AutoTest Основное МО" в moATItWas, и если нет положить в moAtCreated
            // перед этим не удаляя его из КИАП
            autotestMOElementsList.forEach {
                if (
//                    (it.ownText.toString() != "AutoTest Основное МО")
//                    &&
                    (it.ownText.toString().contains("AutoTest"))
                ) {
                    moATItWas.add(it.ownText.toString().trim())
                }
            }
            moATItWas.forEach{
                if (it != "AutoTest Основное МО"){
                element(byCssSelector("input[placeholder]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                var inputSearchValue = ""
                inputSearchValue = element(byCssSelector("input[placeholder]")).getAttribute("value").toString()
                if (inputSearchValue.isNotEmpty()){
                    element(byCssSelector("input[placeholder]")).sendKeys(Keys.END)
                    repeat(inputSearchValue.length){
                        element(byCssSelector("input[placeholder]")).sendKeys(Keys.BACK_SPACE)
                    }
                }
                element(byCssSelector("input[placeholder]")).sendKeys(it, Keys.ENTER)
                Thread.sleep(1000)
                //открываем трехточечное меню
                element(byXpath("//*[text()='$it']/parent::td[1]/parent::tr//button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //удаляем метку
                element(byXpath("//span[text()='Удалить']/parent::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //подтверждаем удаление
                element(byXpath("//span[@title='Удалить']/button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//tbody/tr/td[1]//*[text()='$it']"))
                    .shouldNot(exist, ofSeconds(waitTime))
                }
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //удалили все что нас интересовало, теперь будем создавать
        if (!moATItWas.contains("AutoTest Основное МО")){
            moATCreated.add("AutoTest Основное МО")
        }
        moATCreated.forEach{
            element(byXpath("//span[text()='Добавить новое']/parent::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//label[text()='Наименование']/parent::div//input[@id='title']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//label[text()='Наименование']/parent::div//input[@id='title']"))
                .sendKeys(it)
            val kladr = (10..99).random()
            element(byXpath("//label[text()='КЛАДР']/parent::div//input[@id='kladrId']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//label[text()='КЛАДР']/parent::div//input[@id='kladrId']"))
                .sendKeys("${kladr}00000000000")
            createICToolAddressInput("address","г Краснодар, ул Путевая",waitTime)
            var telCode: Int
            do {
                telCode = (10000..99999).random()
            } while (telCodeList.contains(telCode.toString()))
            element(byXpath("//label[text()='Телефонный код МО']/parent::div//input")).click()
            element(byXpath("//label[text()='Телефонный код МО']/parent::div//input")).sendKeys(telCode.toString())
            telCodeList.add(telCode.toString())
            element(byXpath("//span[text()='Добавить']/parent::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `Черновик`() {

    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `Черновик2`() {
        logonTool(false)
        menuNavigation(MyMenu.Incidents.CreateIncident, waitTime)
        createICToolCalltype("", waitTime)
        createICToolPhone("", waitTime)
        createICToolFIO(generateLastNameF(), generateFirstNameI(), generateMiddleNameO(), waitTime)
        createICToolRandomCoordinates("", waitTime)
        createICToolButtonCreateNewCall()
        element(byXpath("//label[text()='Типы происшествий']/following-sibling::div//input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .scrollIntoView("{block: \"center\"}")
            .click()
        element(byXpath("//div[@role='presentation']/div/ul/li[1]/div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        Thread.sleep(1000)
        println(element(byXpath("//body//div[@role='presentation']")).innerHtml())


    }
    @DataProvider(name = "Табличные справочники")
    fun returnAllClasses(): Array<Array<SubmenuInterface>> =
        arrayOf<SubmenuInterface>(*MyMenu.Incidents.values(), *MyMenu.Reports.values(), *MyMenu.Dictionaries.values(), *MyMenu.KB.values()).filter { it.table }.map { arrayOf ( it) }
            .toTypedArray()


    @Test(retryAnalyzer = Retry::class, dataProvider = "Табличные справочники", groups = ["ALL"])
    fun `CF 0010 Проверка скачивания и корректности табличного CSV файла`(submenuInterface: SubmenuInterface) {
        Configuration.downloadsFolder = "/home/isizov/IdeaProjects/testing-e2e/build/Черновик2"
//        Configuration.proxyEnabled = true
//        Configuration.fileDownload = FileDownloadMode.PROXY
        FileUtils.deleteDirectory(File("/home/isizov/IdeaProjects/testing-e2e/build/Черновик2"))
        logonTool(true)
        menuNavigation(submenuInterface, waitTime)
        tableColumnCheckbox("", true, waitTime)
        //Если таблица иерархическая раскроем иерархию
        if (element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).exists()){
            element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                .click()
            element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                .shouldNot(Condition.exist, Duration.ofSeconds(waitTime))
            element(byXpath("//table/thead/tr/th[1]//*[@name='arrowDown']/ancestor::button"))
                .should(Condition.exist, Duration.ofSeconds(waitTime))
                .shouldBe(Condition.visible, Duration.ofSeconds(waitTime))
        }
//        element(Selectors.byXpath("//a[@download='download.csv']"))
//            .download(DownloadOptions.using(FileDownloadMode.PROXY))
        var contolStringCount = 0
        var contolColumnCount = 0
        val contolTableColumnCount = elements(byXpath("//table/thead/tr/th")).size
        val contolTableStringCount = elements(byXpath("//table/tbody/tr")).size
        val testFile: File = element(byXpath("//a[@download='download.csv']"))
            .download(DownloadOptions.using(FileDownloadMode.FOLDER).withTimeout(59999))
        val fileReader = FileReader(testFile, StandardCharsets.UTF_8)
        val parser = CSVParserBuilder().withQuoteChar('\u0000').withSeparator(';').build()
        fileReader.use { br ->
            CSVReaderBuilder(br).withCSVParser(parser)
                .build().use { reader ->
                    val rows = reader.readAll()
                    for (row in rows) {
                        for (substring in row) {
                            Assertions.assertFalse(substring.lowercase().contains("object"))
                            contolColumnCount += 1
                        }
                        contolStringCount += 1
                    }
                }

        }
        Assertions.assertTrue(contolStringCount > 1)
        Assertions.assertTrue(contolStringCount >= contolTableStringCount + 1)
        Assertions.assertTrue(contolColumnCount > contolTableColumnCount)
        logoffTool()
        Thread.sleep(1000)
        //Удаляем нафиг все что скачали
        FileUtils.deleteDirectory(File("/home/isizov/IdeaProjects/testing-e2e/build/Черновик2"))

    }






}