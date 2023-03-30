package dicts

import BaseTest
import Retry
import com.codeborne.selenide.Condition
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.DownloadOptions
import com.codeborne.selenide.FileDownloadMode
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selenide.element
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

class CheckingFiles: BaseTest()  {

    @DataProvider(name = "Табличные справочники")
    fun returnAllClasses(): Array<Array<SubmenuInterface>> =
        arrayOf<SubmenuInterface>(*MyMenu.Incidents.values(), *MyMenu.Reports.values(), *MyMenu.Dictionaries.values(), *MyMenu.KB.values()).filter { it.table }.map { arrayOf ( it) }
            .toTypedArray()


    @Test(retryAnalyzer = Retry::class, dataProvider = "Табличные справочники", groups = ["ALL"])
    fun `CF 0010 Проверка скачивания и корректности табличного CSV файла`(submenuInterface: SubmenuInterface) {
        Configuration.downloadsFolder = "/home/isizov/IdeaProjects/testing-e2e/build/Черновик2"
        Configuration.proxyEnabled = true
//        Configuration.fileDownload = FileDownloadMode.PROXY
        FileUtils.deleteDirectory(File("/home/isizov/IdeaProjects/testing-e2e/build/Черновик2"))
        logonTool()
        menuNavigation(submenuInterface, waitTime)
        tableColumnCheckbox("", true, waitTime)
        //Если таблица иерархическая раскроем иерархию
        if (element(Selectors.byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).exists()){
            element(Selectors.byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                .click()
            element(Selectors.byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                .shouldNot(Condition.exist, Duration.ofSeconds(waitTime))
            element(Selectors.byXpath("//table/thead/tr/th[1]//*[@name='arrowDown']/ancestor::button"))
                .should(Condition.exist, Duration.ofSeconds(waitTime))
                .shouldBe(Condition.visible, Duration.ofSeconds(waitTime))
        }
//        element(Selectors.byXpath("//a[@download='download.csv']"))
//            .download(DownloadOptions.using(FileDownloadMode.PROXY))
        val testFile: File = element(Selectors.byXpath("//a[@download='download.csv']"))
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
                        }
                    }
                }
        }
        logoffTool()
        Thread.sleep(1000)
        //Удаляем нафиг все что скачали
        FileUtils.deleteDirectory(File("/home/isizov/IdeaProjects/testing-e2e/build/Черновик2"))
    }
}