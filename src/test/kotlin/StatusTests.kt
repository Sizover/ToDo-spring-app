import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide.*
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

class StatusTests {

    val tools = Tools()
    var date = LocalDate.now()
    var dateTime = LocalDateTime.now()
    //Время ожидания элементов при выполнении теста
    val waitTime: Long = 5
    val longWait: Long = 10

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `S 0010`() {
        //Проверка изменения статусов родительской карточки в зависимости от статусов дочерних


        //проверяемые комбинации - 2 статуса дочерней карточки, последний - искомой родительской
        val testtedStatus: List<String> = listOf("Завершена Завершена Завершена", "Отменена Отменена Завершена", "Отменена Завершена Завершена")
        //используемые службы и их логины операторов, пар соотвецтвенно должно быть как и статусов дочерних КП
        val hotlinesMap = mapOf("AutoTest dds-01 1" to "at_dds_01", "AutoTest dds-01 2" to "at_dds_01_2")
        //для каждого пакета статусов по сути запускаем тест заново (можно конечно озадачится параметрическим запуском теста, но бла-бла-бла)
        //хотя траблшутить по параметрическим запускам наверное проще
        testtedStatus.forEach {
            date = LocalDate.now()
            dateTime = LocalDateTime.now()
            val statusList = it.split(" ")
            tools.logonTool()
            tools.menuNavigation("Происшествия", "Создать карточку", waitTime)
            tools.firstHalfIC("S 0010 $it", date.toString(), dateTime.toString(), waitTime)
//            tools.addressInput("callAddress", "ktyyf", waitTime)
            element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
            tools.inputRundom("incidentTypeId")
            //добавляем метку при создании КП
            tools.inputRundom("labels")
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
            element(byXpath("//div[text()='AutoTest S 0010 $it $dateTime']/strong[text()='Дополнительная информация:']"))
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
//            element(byXpath(ddsSelector.format("AutoTest dds-01 1"))).click()
//            element(byXpath(ddsSelector.format("AutoTest dds-01 2"))).click()
            for ((hotline, login) in hotlinesMap){
                element(byXpath(ddsSelector.format(hotline))).click()
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
            element(byText("AutoTest S 0010 $it $dateTime")).should(exist, ofSeconds(waitTime))
            elements(byText("AutoTest S 0010 $it $dateTime"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(hotlinesMap.size))
            tools.logoffTool()

            var i = 0
            for ((hotline, login) in hotlinesMap) {
                tools.anyLogonTool(login, login)
                tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
                tools.checkbox("Описание", true, waitTime)
                //Находим созданную КП в КИАП ДДС
                element(byText("AutoTest S 0010 $it $dateTime"))
                    .should(exist, ofSeconds(waitTime))
                    .click()
                //устанавливаем статус
                element(byXpath("//span[text()='В обработке']/parent::button"))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//span[text()='В обработке']/parent::button")).click()
                element(byXpath("//span[contains(@class,'MuiButton-label')][text()='${statusList[i]}']/parent::button"))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//span[contains(@class,'MuiButton-label')][text()='${statusList[i]}']/parent::button")).click()
                element(byXpath("//span[text()='В обработке']/parent::button"))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//span[contains(@class,'MuiButton-label')][text()='${statusList[i]}']/parent::button"))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                i += 1
                tools.logoffTool()
            }
            tools.logonTool()
            tools.menuNavigation("Происшествия", "Список происшествий", waitTime)
            tools.checkbox("Описание", true, waitTime)
            //Находим созданную родительскую КП
            element(byText("AutoTest S 0010 $it $dateTime"))
                .should(exist, ofSeconds(waitTime))
                .click()
            //проверяем статус родительской карточки
            element(byXpath("//span[contains(@class,'MuiButton-label')][text()='${statusList[2]}']/.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
    }
}