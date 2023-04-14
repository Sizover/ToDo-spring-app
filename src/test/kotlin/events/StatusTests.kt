package events
import BaseTest
import Retry
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.testng.annotations.DataProvider
import test_library.menu.MyMenu.Incidents
import test_library.statuses.StatusEnum
import test_library.statuses.StatusEnum.Завершена
import test_library.statuses.StatusEnum.Отменена
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

open class StatusTests : BaseTest(){


//    @DataProvider(name = "Provider S 0010")
//    fun Object[][]
    @DataProvider(name = "Статусы детей и родителей", parallel = false)
    open fun Statuses(): Any {
        return arrayOf<Array<Any>>(
            arrayOf(Завершена, Завершена, Завершена),
            arrayOf(Отменена, Отменена, Завершена),
            arrayOf(Отменена, Завершена, Завершена)
        )
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, dataProvider = "Статусы детей и родителей", groups = ["ALL"])
    fun `Status 0010 Проверка сумарного статуса родительской КП`(status1: StatusEnum, status2: StatusEnum, statusSum: StatusEnum) {
        //Проверка изменения статусов родительской карточки в зависимости от статусов дочерних
        //проверяемые комбинации - 2 статуса дочерней карточки, последний - искомой родительской
        //используемые службы и их логины операторов, пар соответственно должно быть как и статусов дочерних КП
        val hotlinesMap = mapOf("AutoTest dds-01 1" to "at_dds_01", "AutoTest dds-01 2" to "at_dds_01_2")
        //хотя траблшутить по параметрическим запускам наверное проще
        date = LocalDate.now()
        dateTime = LocalDateTime.now()
        logonTool(false)
        menuNavigation(Incidents.CreateIncident, waitTime)
        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        createICToolCalltype("", waitTime)
        createICToolPhone("", waitTime)
        createICToolFIO("Autotest", "Status 0010", "", waitTime)
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
        createICToolsDopInfo("AutoTest S 0010 $status1 $status2 $statusSum $dateTime", waitTime)
        element(byXpath("//*[text()='Создать карточку']/text()/parent::button")).click()
        inputRandomNew("incidentTypeId-textfield", false, waitTime)
            //добавляем метку при создании КП
        inputRandomNew("labelsId-textfield", true, waitTime)
            //Создаем карточку
        pushButtonCreateIC("AutoTest S 0010 $status1 $status2 $statusSum $dateTime", longWait)
//        element(byXpath("//*[text()='Сохранить карточку']/text()/ancestor::button"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
            //Убеждаемся, что нам загрузило созданную карточку
            //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(longWait))
            //что она в статусе "В обработке"
        checkICToolIsStatus(StatusEnum.`В обработке`, longWait)
            //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest S 0010 $status1 $status2 $statusSum $dateTime", waitTime)
        element(byXpath("//*[text()='Работа с ДДС']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Выбрать ДДС']/text()/ancestor::button"))
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
        val descriptionFunction = "Назначение ДДС для проверки статусов $dateTime"
        enterTextInMDtextboxByName("Описание назначения", descriptionFunction, waitTime)
        element(byXpath("//*[text()='Назначить']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        for ((hotline, login) in hotlinesMap){
            element(byXpath(ddsCardSelector.format(hotline)))
                .should(exist, ofSeconds(longWait))
        }
        element(byXpath(ddsCardSelector.format(descriptionFunction)))
            .should(exist, ofSeconds(waitTime))
        elements(byXpath(ddsCardSelector.format(descriptionFunction)))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(hotlinesMap.size))
        logoffTool()

        var i = 1
        var statusI: StatusEnum = status1
            for ((hotline, login) in hotlinesMap) {
                when(i){
                    1 -> statusI = status1
                    2 -> statusI = status2
                }
                anyLogonTool(login, login)
                menuNavigation(Incidents.IncidentsList, waitTime)
                tableColumnCheckbox("Описание", true, waitTime)
                //Находим созданную КП в КИАП ДДС
                element(byText("AutoTest S 0010 $status1 $status2 $statusSum $dateTime"))
                    .should(exist, ofSeconds(waitTime))
                    .click()
                checkICToolIsStatus(StatusEnum.`В обработке`, longWait)
                //устанавливаем статус
                updateICToolStatus(statusI, longWait)
                i += 1
                logoffTool()
            }
        logonTool(false)
        menuNavigation(Incidents.IncidentsList, waitTime)
        tableColumnCheckbox("Описание", true, waitTime)
        //Находим созданную родительскую КП
        element(byText("AutoTest S 0010 $status1 $status2 $statusSum $dateTime"))
            .should(exist, ofSeconds(waitTime))
            .click()
        //проверяем статус родительской карточки
        checkICToolIsStatus(statusSum, waitTime)
        logoffTool()
    }
}