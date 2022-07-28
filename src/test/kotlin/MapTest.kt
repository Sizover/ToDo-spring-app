
//import kotlin.collections.EmptyMap.keys
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.*
import ii_tests_pim.Retry
import ii_tests_pim.Tools
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import java.time.Duration.ofSeconds


class MapTest
{
    var waitTime: Long = 5
    val tools = Tools()

    fun `MTtool_001`(checkboxName: String){
        val checkboxSelector = "//h6[text()='%s']/../parent::span/parent::label/span[@style]//input"
        val checkboxStatus = "//h6[text()='%s']/../parent::span/parent::label/span[@style]//*[name()='path']"
        //включаем дежурные службы
        element(byXpath(checkboxSelector.format(checkboxName)))
            .should(exist, ofSeconds(waitTime))
            .click()
        element(byXpath(checkboxStatus.format(checkboxName)))
            .should(exist, ofSeconds(waitTime))
        //считаем маркеры
        val numericMarker = elements(byXpath("//div[contains(@class,'markerBlue10')]/*[text()]")).size
        var src:String = ""
        when (checkboxName) {
            "Дежурные службы" -> { src = "kiap_hotline" }
            "Камеры" -> { src = "kiap_camera" }
            "Датчики" -> { src = "kiap_sensor" }
            "Организации" -> { src = "kiap_mass" }
        }
        val checkboxNameMarker = elements(byCssSelector("img.markerBlue10[src*='$src']")).size
        val summaryMarker = numericMarker + checkboxNameMarker
        Assertions.assertTrue(summaryMarker > 0)
        //выключаем дежурные службы
        element(byXpath(checkboxSelector.format(checkboxName)))
            .should(exist, ofSeconds(waitTime))
            .click()
        element(byXpath(checkboxStatus.format(checkboxName)))
            .shouldNot(exist, ofSeconds(waitTime))

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `MT_001`(){
        tools.logonTool()
    //открываем карту
    element(byXpath("//span[@title='Открыть карту в отдельном окне']/button"))
        .should(exist, ofSeconds(waitTime))
        .shouldBe(visible, ofSeconds(waitTime))
        .click()
    //Переключаемся между окнами
    switchTo().window(1)
    element(byCssSelector("div.leaflet-control button.MuiIconButton-sizeSmall"))
        .should(exist, ofSeconds(waitTime))
        .shouldBe(visible, ofSeconds(waitTime))
        .click()
    //открываем настройки карты
    element(byXpath("//main/div[@style='display: block;']"))
        .should(exist, ofSeconds(waitTime))
        .shouldBe(visible, ofSeconds(waitTime))
    //отключаем все чек боксы
    while (elements(byXpath("//main/div[@style='display: block;']//*[name()='path'][@d]/../../input")).size > 0) {
//        element(byXpath("//main/div[@style='display: block;']")).scrollIntoView(false)
        element(byXpath("//main/div[@style='display: block;']//*[name()='path'][@d]/../../input")).sendKeys(Keys.END)
        element(byXpath("//main/div[@style='display: block;']//*[name()='path'][@d]/../../input")).click()
        Thread.sleep(500)
    }
    //включаем чекбокс всех происшествий
        val checkboxSelector = "//h6[text()='%s']/../parent::span/parent::label/span[@style]//input"
        val checkboxStatus = "//h6[text()='%s']/../parent::span/parent::label/span[@style]//*[name()='path']"
    element(byXpath(checkboxSelector.format("Все статусы")))
        .should(exist, ofSeconds(waitTime))
        .click()
    //убеждаемся, что он включился
    element(byXpath(checkboxStatus.format("Все статусы")))
        .should(exist, ofSeconds(waitTime))
    //подсчитываем элементы, что бы сравнить их сумму с нулем
    val markerSelector = "div.leaflet-pane.leaflet-marker-pane>img.%s"
    val markerIncidentInProcess = elements(byCssSelector(markerSelector.format("markerIncidentInProcess"))).size
    val markerIncidentFinished = elements(byCssSelector(markerSelector.format("markerIncidentFinished"))).size
    val markerIncidentNew = elements(byCssSelector(markerSelector.format("markerIncidentNew"))).size
    val markerIncidentResponse = elements(byCssSelector(markerSelector.format("markerIncidentResponse"))).size
    val numericMarker = elements(byXpath("//div[contains(@class,'markerIncident')]/*[text()]")).size
    val summaryMarker:Int = markerIncidentInProcess + markerIncidentFinished + markerIncidentNew + markerIncidentResponse + numericMarker
    Assertions.assertTrue(summaryMarker > 0)
    //выключаем чекбокс происшествий
        element(byXpath(checkboxSelector.format("Все статусы")))
        .should(exist, ofSeconds(waitTime))
        .click()
    //убеждаемся, что он выключился
        element(byXpath(checkboxStatus.format("Все статусы")))
        .shouldNot(exist, ofSeconds(waitTime))
//        //включаем дежурные службы
//        element(byXpath(checkboxSelector.format("Дежурные службы")))
//            .should(exist, ofSeconds(waitTime))
//            .click()
//        element(byXpath(checkboxStatus.format("Дежурные службы")))
//            .should(exist, ofSeconds(waitTime))
//        //считаем маркеры
//        numericMarker = elements(byXpath("//div[contains(@class,'markerBlue10')]/*[text()]")).size
//        val hotlineMarker = elements(byCssSelector("img.markerBlue10[src*='kiap_hotline']")).size
//        summaryMarker = numericMarker + hotlineMarker
//        Assertions.assertTrue(summaryMarker > 0)
//        //выключаем дежурные службы
//        element(byXpath(checkboxSelector.format("Дежурные службы")))
//            .should(exist, ofSeconds(waitTime))
//            .click()
//        element(byXpath(checkboxStatus.format("Дежурные службы")))
//            .shouldNot(exist, ofSeconds(waitTime))
//        //включаем Камеры
//        element(byXpath(checkboxSelector.format("Камеры")))
//            .should(exist, ofSeconds(waitTime))
//            .click()
//        element(byXpath(checkboxStatus.format("Камеры")))
//            .should(exist, ofSeconds(waitTime))
//        numericMarker = elements(byXpath("//div[contains(@class,'markerBlue10')]/*[text()]")).size
//        val cameraMarker = elements(byCssSelector("img.markerBlue10[src*='kiap_camera']")).size
//        summaryMarker = numericMarker + cameraMarker
//        Assertions.assertTrue(summaryMarker > 0)
//        //выключаем Камеры
//        element(byXpath(checkboxSelector.format("Камеры")))
//            .should(exist, ofSeconds(waitTime))
//            .click()
//        element(byXpath(checkboxStatus.format("Камеры")))
//            .shouldNot(exist, ofSeconds(waitTime))
//        //включаем Датчики
//        element(byXpath(checkboxSelector.format("Датчики")))
//            .should(exist, ofSeconds(waitTime))
//            .click()
//        element(byXpath(checkboxStatus.format("Датчики")))
//            .should(exist, ofSeconds(waitTime))
//        numericMarker = elements(byXpath("//div[contains(@class,'markerBlue10')]/*[text()]")).size
//        val sensorMarker = elements(byCssSelector("img.markerBlue10[src*='kiap_sensor']")).size
//        summaryMarker = numericMarker + sensorMarker
//        Assertions.assertTrue(summaryMarker > 0)
//        //выключаем Датчики
//        element(byXpath(checkboxSelector.format("Датчики")))
//            .should(exist, ofSeconds(waitTime))
//            .click()
//        element(byXpath(checkboxStatus.format("Датчики")))
//            .shouldNot(exist, ofSeconds(waitTime))
        MTtool_001("Дежурные службы")
        MTtool_001("Камеры")
        MTtool_001("Датчики")
        MTtool_001("Организации")

        tools.logoffTool()



        //"div.leaflet-pane.leaflet-marker-pane>img.markerIncidentInProcess"
        //"div.leaflet-pane.leaflet-marker-pane>img.markerIncidentFinished"
        //"div.leaflet-pane.leaflet-marker-pane>img.markerIncidentNew"
        //"div.leaflet-pane.leaflet-marker-pane>img.markerIncidentResponse"
        ////div[contains(@class,'markerIcon')]/*[text()]


        //main/div[2]//*[name()='path'][@d]/../../input


    }
}