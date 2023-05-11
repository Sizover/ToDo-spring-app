


import com.codeborne.selenide.Condition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.DownloadOptions
import com.codeborne.selenide.FileDownloadMode
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions
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



    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["LOCAL"])
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