package dicts

import BaseTest
import Retry
import com.codeborne.selenide.Condition
import com.codeborne.selenide.DownloadOptions
import com.codeborne.selenide.FileDownloadMode
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.junit.jupiter.api.Assertions
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import test_library.menu.MyMenu
import test_library.menu.SubmenuInterface
import java.time.Duration

class CheckingFiles: BaseTest()  {

    @DataProvider(name = "Табличные справочники", parallel = true)
    fun returnAllClasses(): Array<Array<SubmenuInterface>> =
        arrayOf<SubmenuInterface>(*MyMenu.Incidents.values(), *MyMenu.Reports.values(), *MyMenu.Dictionaries.values(), *MyMenu.KB.values()).filter { it.table }.map { arrayOf ( it) }
            .toTypedArray()


    @Test(retryAnalyzer = Retry::class, dataProvider = "Табличные справочники", groups = ["ALL", "PROXY"])
    fun `CF 0010 Проверка скачивания и корректности табличного CSV файла`(submenuInterface: SubmenuInterface) {
//        Configuration.downloadsFolder = "/home/isizov/IdeaProjects/testing-e2e/build/CF_0010"
//        Configuration.proxyEnabled = true
//        Configuration.fileDownload = FileDownloadMode.PROXY
//        FileUtils.deleteDirectory(File("/home/isizov/IdeaProjects/testing-e2e/build/CF_0010"))
        logonTool(false)
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
        var contolCSVStringCount = 0
        var contolColumnCount = 0
        val contolTableColumnCount = elements(byXpath("//table/thead/tr/th")).size
        val contolTableStringCount = elements(byXpath("//table/tbody/tr")).size
        val testFile = element(byXpath("//a[@download='download.csv']"))
            .download(DownloadOptions.using(FileDownloadMode.FOLDER).withTimeout(59999))
            .reader()
            .readText()
            .split("\n")
            .forEach { oneString ->
                Assertions.assertFalse(oneString.lowercase().contains("object"))
                contolCSVStringCount += 1
                if (print) println(oneString)
        }
//        val testFile: File = element(byXpath("//a[@download='download.csv']"))
//            .download(DownloadOptions.using(FileDownloadMode.FOLDER).withTimeout(59999))
//        val fileReader = FileReader(testFile, StandardCharsets.UTF_8)
//        val parser = CSVParserBuilder().withQuoteChar('\u0000').withSeparator(';').build()
//        fileReader.use { br ->
//            CSVReaderBuilder(br).withCSVParser(parser)
//                .build().use { reader ->
//                    val rows = reader.readAll()
//                    for (row in rows) {
//                        for (substring in row) {
//                            Assertions.assertFalse(substring.lowercase().contains("object"))
//                            contolColumnCount += 1
//                        }
//                        contolStringCount += 1
//                    }
//                }
//        }
        if (!element(byXpath("//table/tbody/tr//h6[text()='Нет данных']")).exists()){
            Assertions.assertTrue(contolCSVStringCount > 1)
            Assertions.assertTrue(contolCSVStringCount >= contolTableStringCount + 1)
        } else {
            Assertions.assertTrue(contolCSVStringCount == 1)
            Assertions.assertTrue(contolCSVStringCount == contolTableStringCount)
        }
//        Assertions.assertTrue(contolColumnCount > contolTableColumnCount)
    }
}