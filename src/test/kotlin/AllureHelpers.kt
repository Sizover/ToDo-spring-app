import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.WebDriverRunner
import io.qameta.allure.Attachment
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import java.nio.charset.StandardCharsets

class AllureHelpers {
    @Attachment(value = "AllureTextReport", type = "text/plain", fileExtension = ".txt")
    fun attachText(text: String): String {
        return text
    }

    @Attachment(value = "AllureCSVReport", type = "text/csv", fileExtension = ".csv")
    fun attachCSV(csv: String): String {
        return csv
    }

    @get:Attachment(value = "Html source", type = "text/html", fileExtension = ".html")
    val pageSource: ByteArray
        get() = pageSourceBytes

    @Attachment(value = "Screenshot", type = "image/png", fileExtension = ".png")
    fun takeScreenshot(): ByteArray {
        return screenshotBytes
    }

    @Attachment(value = "{name}", type = "image/png", fileExtension = ".png")
    fun takeScreenshot(name: String?): ByteArray {
        return screenshotBytes
    }

    @Attachment(value = "Element screenshot", type = "image/png", fileExtension = ".png")
    fun takeScreenshot(elem: SelenideElement): ByteArray {
        return getScreenshotBytes(elem)
    }

    val pageSourceBytes: ByteArray = WebDriverRunner.getWebDriver().pageSource.toByteArray(StandardCharsets.UTF_8)

    val screenshotBytes: ByteArray = (WebDriverRunner.getWebDriver() as TakesScreenshot).getScreenshotAs(OutputType.BYTES)

    fun getScreenshotBytes(elem: SelenideElement): ByteArray {
        return elem.getScreenshotAs(OutputType.BYTES)
    }
}