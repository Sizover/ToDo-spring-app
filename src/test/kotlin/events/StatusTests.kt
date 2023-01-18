package events
import Retry
import BaseTest
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

open class StatusTests : BaseTest(){


//    @DataProvider(name = "Provider S 0010")
//    fun Object[][]
    @DataProvider(name = "Статусы детей и родителей", parallel = false)
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
        //используемые службы и их логины операторов, пар соответственно должно быть как и статусов дочерних КП
        val hotlinesMap = mapOf("AutoTest dds-01 1" to "at_dds_01", "AutoTest dds-01 2" to "at_dds_01_2")
        //хотя траблшутить по параметрическим запускам наверное проще
        date = LocalDate.now()
        dateTime = LocalDateTime.now()
        logonTool()
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        firstHalfIC("S 0010 $Status1 $Status2 $StutusSum", date.toString(), dateTime.toString(), waitTime)
//            tools.addressInput("callAddress", "ktyyf", waitTime)
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        inputRandomNew("incidentTypeId-textfield", false, waitTime)
            //добавляем метку при создании КП
        inputRandomNew("labelsId-textfield", true, waitTime)
            //Создаем карточку
        element(byXpath("//span[text()='Сохранить карточку']/..")).click()
            //Убеждаемся, что нам загрузило созданную карточку
            //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(longWait))
            //что она в статусе "В обработке"
        checkICToolIsStatus("В обработке", longWait)
            //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime", waitTime)
        element(byXpath("//span[text()='Работа с ДДС']/parent::button")).click()
        element(byXpath("//span[text()='Выбрать ДДС']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
            element(byXpath("//p[text()='ДДС ЭОС']/ancestor::div[@id='panel1a-header']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        val ddsSelector = "//*[text()='%s']/ancestor::div/div/label//input"
        val ddsCheckboxSelector = "//*[text()='%s']/ancestor::div/div/label//*[name()='svg']"
        val ddsCardSelector = "//*[text()='Назначенные службы']/ancestor::div[@role='tabpanel']//form[@novalidate]//*[text()='%s']"
        //Выбирает и назначаем ДДС
        for ((hotline, login) in hotlinesMap){
            //Побеждаем зависание чекбокса
            while (element(byXpath(ddsCheckboxSelector.format(hotline))).getAttribute("name") != "checkboxFocus"){
                element(byXpath(ddsSelector.format(hotline))).click()
                Thread.sleep(500)
            }
        }

        element(byXpath("//span[text()='Назначить']/..")).click()
        for ((hotline, login) in hotlinesMap){
            element(byXpath(ddsCardSelector.format(hotline)))
                .should(exist, ofSeconds(longWait))
        }
        element(byXpath(ddsCardSelector.format("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime")))
            .should(exist, ofSeconds(waitTime))
        elements(byXpath(ddsCardSelector.format("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime")))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(hotlinesMap.size))
        logoffTool()

        var i = 1
        var statusI = ""
            for ((hotline, login) in hotlinesMap) {
                when(i){
                    1 -> statusI = Status1
                    2 -> statusI = Status2
                }
                anyLogonTool(login, login)
                menuNavigation("Происшествия", "Список происшествий", waitTime)
                checkbox("Описание", true, waitTime)
                //Находим созданную КП в КИАП ДДС
                element(byText("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime"))
                    .should(exist, ofSeconds(waitTime))
                    .click()
                checkICToolIsStatus("В обработке", waitTime)
                //устанавливаем статус
                updateICToolStatus(statusI, longWait)
                i += 1
                logoffTool()
            }
        logonTool()
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        checkbox("Описание", true, waitTime)
        //Находим созданную родительскую КП
        element(byText("AutoTest S 0010 $Status1 $Status2 $StutusSum $dateTime"))
            .should(exist, ofSeconds(waitTime))
            .click()
        //проверяем статус родительской карточки
        checkICToolIsStatus(StutusSum, waitTime)
        logoffTool()
    }
}