package events
import Retry
import Tools
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.testng.annotations.DataProvider
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

open class StatusTests {

    val tools = Tools()
    var date = LocalDate.now()
    var dateTime = LocalDateTime.now()
    //Время ожидания элементов при выполнении теста
    val waitTime: Long = 5
    val longWait: Long = 10

//    @DataProvider(name = "Provider S 0010")
//    fun Object[][]
    @DataProvider(name = "Статусы детей и родителей")
    open fun Statuses(): Any {
        return arrayOf<Array<Any>>(
            arrayOf("Завершена", "Завершена", "Завершена"),
            arrayOf("Отменена", "Отменена", "Завершена"),
            arrayOf("Отменена", "Завершена", "Завершена")
        )
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, dataProvider = "Статусы детей и родителей", groups = ["ALL"])
    fun `Status 0010 Проверка сумарного статуса родительской КП`(Status1: String, Status2: String, StutusSum: String) {
        //Проверка изменения статусов родительской карточки в зависимости от статусов дочерних


        //проверяемые комбинации - 2 статуса дочерней карточки, последний - искомой родительской
//        val testtedStatus: List<String> = listOf("Завершена Завершена Завершена", "Отменена Отменена Завершена", "Отменена Завершена Завершена")
        //используемые службы и их логины операторов, пар соотвецтвенно должно быть как и статусов дочерних КП
        val hotlinesMap = mapOf("AutoTest dds-01 1" to "at_dds_01", "AutoTest dds-01 2" to "at_dds_01_2")
        //для каждого пакета статусов по сути запускаем тест заново (можно конечно озадачится параметрическим запуском теста, но бла-бла-бла)
        //хотя траблшутить по параметрическим запускам наверное проще
        date = LocalDate.now()
        dateTime = LocalDateTime.now()
//        val statusList = mutableListOf<String>()
//        statusList.add(Status1)
//        statusList.add(Status2)
        tools.logonTool()
        tools.menuNavigation("Происшествия", "Создать карточку", waitTime)
        tools.firstHalfIC("S 0010 $Status1 $Status2 $StutusSum", date.toString(), dateTime.toString(), waitTime)
//            tools.addressInput("callAddress", "ktyyf", waitTime)
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        tools.inputRandomNew("incidentTypeId-textfield", false, waitTime)
            //добавляем метку при создании КП
        tools.inputRandomNew("labelsId-textfield", true, waitTime)
            //Создаем карточку
        element(byXpath("//span[text()='Сохранить карточку']/..")).click()
            //Убеждаемся, что нам загрузило созданную карточку
            //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
            //что она в статусе "В обработке"
        element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
            .shouldHave(text("В обработке"), ofSeconds(waitTime))
            //и что это именно так карточка которую мы только что создали
//        element(byCssSelector("div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-md-8 > div:nth-child(4)"))
//            .shouldHave(text("AutoTest N 0110 $dateTime"), ofSeconds(waitTime))
        element(byXpath("//div[text()='AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//span[text()='Работа с ДДС']/parent::button")).click()
        element(byXpath("//span[text()='Выбрать ДДС']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
            //выбираем ДДС-02 г.Черкесск
            //element(byCssSelector("p#alert-dialog-description>div>div>div>div:nth-child(1)")).click()
//        element(byXpath("//p[text()='ДДС ЭОС']/../../../..")).click()
            element(byXpath("//p[text()='ДДС ЭОС']/ancestor::div[@id='panel1a-header']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //p[text()='ДДС ЭОС']/../../../..
            //Thread.sleep(10000)
        val ddsSelector =
            "//*[text()='%s']/ancestor::div[@class='MuiGrid-root MuiGrid-item']/following-sibling::div/label//input"
        val ddsCheckboxSelector = "//*[text()='%s']/ancestor::div[@class='MuiGrid-root MuiGrid-item']/following-sibling::div/label//*[name()='svg']"
//            element(byXpath(ddsSelector.format("AutoTest dds-01 1"))).click()
//            element(byXpath(ddsSelector.format("AutoTest dds-01 2"))).click()
        //Выбирает и назначаем ДДС
        for ((hotline, login) in hotlinesMap){
            //Побеждаем зависание чекбокса
            while (element(byXpath(ddsCheckboxSelector.format(hotline))).getAttribute("name") != "checkboxFocus"){
                element(byXpath(ddsSelector.format(hotline))).click()
                Thread.sleep(500)
            }
        }
//        element(byXpath("//*[text()='AutoTest dds-01 1']/ancestor::div[@class='MuiGrid-root MuiGrid-item']/following-sibling::div/label//input")).click()
//        element(byXpath("//*[text()='ДДС-01 г.Черкесск']/../parent::div/following-sibling::div/label//input")).click()
            //element(byCssSelector("p#alert-dialog-description>div>div>div>div:nth-child(1) div[style='width: 100%;']>div:nth-child(2) input")).click()
            //////div[text()='ДДС-02 г.Черкесск']/../div/label/span/span/input
            //element(byCssSelector("form>div:nth-child(3) button:nth-child(1)")).click()
            element(byXpath("//span[text()='Назначить']/..")).click()
            for ((hotline, login) in hotlinesMap){
                element(byText(hotline)).should(exist, ofSeconds(waitTime))
            }
//            element(byText("AutoTest dds-01 1")).should(exist, ofSeconds(waitTime))
//            element(byText("AutoTest dds-01 2")).should(exist, ofSeconds(waitTime))
            element(byText("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime"))
                .should(exist, ofSeconds(waitTime))
            elements(byText("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(hotlinesMap.size))
            tools.logoffTool()

            var i = 1
        var statusI = ""
            for ((hotline, login) in hotlinesMap) {
                when(i){
                    1 -> statusI = Status1
                    2 -> statusI = Status2
                }
                tools.anyLogonTool(login, login)
                tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
                tools.checkbox("Описание", true, waitTime)
                //Находим созданную КП в КИАП ДДС
                element(byText("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime"))
                    .should(exist, ofSeconds(waitTime))
                    .click()
                Thread.sleep(500)
                //устанавливаем статус
                element(byXpath("//span[text()='В обработке']/parent::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                Thread.sleep(500)
                element(byXpath("//span[contains(@class,'MuiButton-label')][text()='$statusI']/parent::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                Thread.sleep(500)
                element(byXpath("//span[text()='В обработке']/parent::button"))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//span[contains(@class,'MuiButton-label')][text()='$statusI']/parent::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                i += 1
                tools.logoffTool()
            }
            tools.logonTool()
            tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
            tools.checkbox("Описание", true, waitTime)
            //Находим созданную родительскую КП
            element(byText("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime"))
                .should(exist, ofSeconds(waitTime))
                .click()
            //проверяем статус родительской карточки
            element(byXpath("//span[contains(@class,'MuiButton-label')][text()='$StutusSum']/parent::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
    }
}