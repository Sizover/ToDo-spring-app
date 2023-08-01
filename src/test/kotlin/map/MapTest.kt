package map
//import kotlin.collections.EmptyMap.keys
import BaseTest
import Retry
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.back
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.Selenide.refresh
import com.codeborne.selenide.Selenide.switchTo
import org.junit.jupiter.api.Assertions
import test_library.alerts.AlertsEnum
import test_library.menu.MyMenu.Dictionaries
import test_library.menu.MyMenu.Incidents
import test_library.statuses.StatusEnum.`В обработке`
import java.time.Duration.ofSeconds
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asinh
import kotlin.math.floor
import kotlin.math.tan


class MapTest  : BaseTest(){

    fun `MTtool_001`(checkboxName: String){
        val checkboxSelector = "//h6[text()='%s']/../parent::span/parent::label/span[@style]//input"
        val checkboxStatus = "//h6[text()='%s']/../parent::span/parent::label/span[@style]//*[name()='path']"
        //включаем дежурные службы
        element(byXpath(checkboxSelector.format(checkboxName)))
            .should(exist, ofSeconds(waitTime))
            .scrollIntoView("{block: \"center\"}")
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
        Thread.sleep(2000)
        val checkboxNameMarker = elements(byCssSelector("img.markerBlue10[src*='$src']")).size
        val summaryMarker = numericMarker + checkboxNameMarker
        Assertions.assertTrue(summaryMarker > 0)
        //выключаем дежурные службы
        element(byXpath(checkboxSelector.format(checkboxName)))
            .should(exist, ofSeconds(waitTime))
            .scrollIntoView("{block: \"center\"}")
            .click()
        element(byXpath(checkboxStatus.format(checkboxName)))
            .shouldNot(exist, ofSeconds(waitTime))

    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `MT_001 Проверка отображения значов на карте при применении различных фильтров карты`(){
        logonTool(false)
        //открываем карту
        element(byXpath("//*[@aria-label='Открыть карту в отдельном окне']/button"))
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
//            element(byXpath("//main/div[@style='display: block;']//*[name()='path'][@d]/../../input")).sendKeys(Keys.END)
            element(byXpath("//main/div[@style='display: block;']//*[name()='path'][@d]/../../input"))
                .should(exist, ofSeconds(waitTime))
                .scrollIntoView("{block: \"center\"}")
                .click()
            Thread.sleep(500)
        }
        //включаем чекбокс всех происшествий
        val checkboxSelector = "//h6[text()='%s']/../parent::span/parent::label/span[@style]//input"
        val checkboxStatus = "//h6[text()='%s']/../parent::span/parent::label/span[@style]//*[name()='path']"
        element(byXpath(checkboxSelector.format("Все статусы")))
            .should(exist, ofSeconds(waitTime))
            .scrollIntoView("{block: \"center\"}")
            .click()
        //убеждаемся, что он включился
        element(byXpath(checkboxStatus.format("Все статусы")))
            .should(exist, ofSeconds(waitTime))
        Thread.sleep(2000)
        //подсчитываем элементы, что бы сравнить их сумму с нулем
        val markerSelector = "div.leaflet-pane.leaflet-markers-pane>img.%s"
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
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `MT_002 Проверка перехода по координатам места происшествия`() {
        //Создаем КП, указывая координаты вручную (случайные из диапазона), затем переходим на карту
        //считываем ссылку в свойствах центрующей иконки, достаем оттуда координаты и сравниваем с заданными с учетом некоторой погрешности
        //убеждаемся что на карте присутствуют 5 кусочков из openStreetMap соответствующих координатам, возвращаемся в КП и убеждаемся что вернулись в КП
        try {
            logonTool(false)
        } catch (_:  Throwable) {
            element(byCssSelector("header button svg[name='user']"))
                .should(exist, ofSeconds(waitTime))
        }
        menuNavigation(Incidents.CreateIncident, waitTime)
        createICToolCalltype("", waitTime)
        createICToolPhone("", waitTime)
        createICToolFIO("Map", "Test", "002", waitTime)
        val lat = (10..70).random() + kotlin.random.Random.nextFloat()
        val lon = (10..100).random() + kotlin.random.Random.nextFloat()
        createICToolRandomCoordinates("$lat;$lon", waitTime)
        createICToolsDopInfo("Autotest MT_002, Широта = $lat, Долгота = $lon", waitTime)
        element(byXpath("//*[text()='Создать карточку']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        inputRandomNew("incidentTypeId-textfield", false, waitTime)
        createICToolButtonCreateNewIC("Autotest MT_002, Широта = $lat, Долгота = $lon", waitTime)
//        element(byXpath("//*[text()='Сохранить карточку']/text()/ancestor::button"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
        checkICToolIsStatus(`В обработке`, longWait)
        checkICToolDopInfo("Autotest MT_002, Широта = $lat, Долгота = $lon", waitTime)
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
        //div[@id='skeleton']//main//img[@alt and @role='presentation' and contains(@src,'16/$xtile/$ytile.png')]"
        element(byXpath("//div[@id='skeleton']//main//img[@alt and contains(@src,'16/$xtile/$ytile.png')]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        back()
        element(byXpath("//div[@role='tabpanel' and @id='simple-tabpanel-card']/form"))
            .should(exist, ofSeconds(waitTime))
        checkICToolIsStatus(`В обработке`, waitTime)
        checkICToolDopInfo("Autotest MT_002, Широта = $lat, Долгота = $lon", waitTime)
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `MT_003 Проверка PM1363~B - Общая ошибка приложения после обновления страницы карты`() {
        logonTool(false)
        //открываем карту
        element(byXpath("//*[@aria-label='Открыть карту в отдельном окне']/button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Переключаемся между окнами
        switchTo().window(1)
        //Ждем некоторый кусочек карты. Картинку.
        element(byXpath("//div[@id='skeleton']//main//img[@alt and @src]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //ждем кнопку настроек карты (что б наверняка)
        element(byXpath("//button/*[@aria-label='Показать слои']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //обновляем страницу
        refresh()
        //Ждем некоторый кусочек карты. Картинку.
        element(byXpath("//div[@id='skeleton']//main//img[@alt and @src]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //ждем кнопку настроек карты
        element(byXpath("//button/*[@aria-label='Показать слои']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Возвращаемся в основное окно
        switchTo().window(0)
        checkAlert(AlertsEnum.snackbarInfo, "Закрыта карта в другом окне", true, 30)
        //Переходим в справочник организаций
        menuNavigation(Dictionaries.Companies, waitTime)
        //выводим столбец карты
        tableColumnCheckbox("Карта", true, waitTime)
        val mapColumn = tableNumberOfColumn("Карта", waitTime)
        element(byXpath("//table/tbody/tr/td[$mapColumn]/*[@aria-label='Показать на карте']/button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        elements(byXpath("//table/tbody/tr/td[$mapColumn]/*[@aria-label='Показать на карте']/button"))
            .random()
            .click()
        element(byXpath("//div[@role='alert']//*[@name='snackbarError']"))
            .shouldNot(exist, ofSeconds(waitTime))
        //Ждем некоторый кусочек карты. Картинку.
        element(byXpath("//div[@id='skeleton']//main//img[@alt and @src]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //ждем кнопку настроек карты
        element(byXpath("//button/*[@aria-label='Показать слои']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//div[@role='alert']//*[@name='snackbarError']"))
            .shouldNot(exist, ofSeconds(waitTime))
    }
}