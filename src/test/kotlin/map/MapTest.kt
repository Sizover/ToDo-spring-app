package map
//import kotlin.collections.EmptyMap.keys
import Retry
import BaseTest
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.*
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import java.time.Duration.ofSeconds
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asinh
import kotlin.math.floor
import kotlin.math.tan


class MapTest  : BaseTest(){
    var waitTime: Long = 5

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

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `MT_001 Проверка отображения значов на карте при применении различных фильтров карты`(){
        logonTool()
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
        MTtool_001("Дежурные службы")
        MTtool_001("Камеры")
        MTtool_001("Датчики")
        MTtool_001("Организации")
        logoffTool()
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `MT_002 Проверка перехода по координатам места происшествия`() {
        //Создаем КП, указывая координаты вручную (случайные из диапазона), затем переходим на карту
        //считываем ссылку в свойствах центрующей иконки, достаем оттуда координаты и сравниваем с заданными с учетом некоторой погрешности
        //убеждаемся что на карте присутствуют 5 кусочков из openStreetMap соответствующих координатам, возвращаемся в КП и убеждаемся что вернулись в КП
        try {
            logonTool()
        } catch (_:  Throwable) {
            element(byCssSelector("header button svg[name='user']"))
                .should(exist, ofSeconds(waitTime))
        }
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        createIcToolCalltype("", waitTime)
        createICToolPhone("", waitTime)
        createICToolFIO("Map", "Test", "002", waitTime)
        val lat = (10..70).random() + kotlin.random.Random.nextFloat()
        val lon = (10..100).random() + kotlin.random.Random.nextFloat()
        element(byXpath("//form//label[text()='Широта']/..//input[@name='lat']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//form//label[text()='Широта']/..//input[@name='lat']"))
            .sendKeys(lat.toString())
        element(byXpath("//form//label[text()='Долгота']/..//input[@name='lon']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//form//label[text()='Долгота']/..//input[@name='lon']"))
            .sendKeys(lon.toString())
        createICToolsDopInfo("Autotest MT_002, Широта = $lat, Долгота = $lon", waitTime)
        element(byXpath("//*[text()='Создать карточку']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        inputRandomNew("incidentTypeId-textfield", false, waitTime)
        element(byXpath("//*[text()='Сохранить карточку']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        checkICToolIsStatus("В обработке", waitTime)
        checkICToolsDopInfo("Autotest MT_002, Широта = $lat, Долгота = $lon", waitTime)
        //переходим в карту
        element(byCssSelector("div#place div[style*='cursor']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
//        element(byXpath("//div[@id='skeleton']//main//img[@alt='center']"))
        element(byCssSelector("div#skeleton main img[alt='center']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val coordinates = element(byCssSelector("div#skeleton main img[alt='center']"))
            .getAttribute("baseURI")?.substringAfterLast('/')!!.split(",")
        Assertions.assertTrue(coordinates[2].toInt() == 16)
        Assertions.assertTrue(abs(coordinates[0].toFloat() - lat) < 0.00001)
        Assertions.assertTrue(abs(coordinates[1].toFloat() - lon) < 0.00001)
        //проверка картинок карты по ссылкам коррелирующими с координатами
        //честно сPIзжено с https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
        val latRad = Math.toRadians(lat.toDouble())
        var xtile = floor( (lon + 180) / 360 * (1 shl coordinates[2].toInt()) ).toInt()
        var ytile = floor( (1.0 - asinh(tan(latRad)) / PI) / 2 * (1 shl coordinates[2].toInt()) ).toInt()
        if (xtile < 0) {
            xtile = 0
        }
        if (xtile >= (1 shl coordinates[2].toInt())) {
            xtile= (1 shl coordinates[2].toInt()) - 1
        }
        if (ytile < 0) {
            ytile = 0
        }
        if (ytile >= (1 shl coordinates[2].toInt())) {
            ytile = (1 shl coordinates[2].toInt()) - 1
        }
        element(byXpath("//div[@id='skeleton']//main//img[@alt and @role='presentation' and contains(@src,'16/$xtile/$ytile.png')]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        back()
        checkICToolIsStatus("В обработке", waitTime)
        checkICToolsDopInfo("Autotest MT_002, Широта = $lat, Долгота = $lon", waitTime)
        logoffTool()
    }
}