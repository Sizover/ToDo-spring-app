package ii_tests_pim

import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide.*
import org.apache.commons.io.FileUtils
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import java.io.File
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

class Status_tests {

    val tools = Tools()
    var date = LocalDate.now()
    var dateTime = LocalDateTime.now()
    //Время ожидания элементов при выполнении теста
    val waitTime: Long = 5
    val longWait: Long = 10

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `S 0010`() {
        //Проверка изменения статусов родительской карточки в зависимости от статусов дочерних
        date = LocalDate.now()
        dateTime = LocalDateTime.now()
        tools.logonTool()
        tools.menuNavigation("Происшествия","Создать карточку",waitTime)
        tools.firstHalfIC("S 0010", date.toString(), dateTime.toString(), waitTime)
        tools.addressInput("callAddress", "ktyyf", waitTime)
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
        element(byXpath("//div[text()='AutoTest S 0010 $dateTime']/strong[text()='Дополнительная информация:']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//span[text()='Работа с ДДС']/parent::button")).click()
        element(byXpath("//span[text()='Выбрать ДДС']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем ДДС-02 г.Черкесск
        //element(byCssSelector("p#alert-dialog-description>div>div>div>div:nth-child(1)")).click()
        element(byXpath("//p[text()='ДДС ЭОС']/../../../..")).click()
        //p[text()='ДДС ЭОС']/../../../..
        //Thread.sleep(10000)
        element(byXpath("//*[text()='ДДС-02 г.Черкесск']/../parent::div/following-sibling::div/label//input")).click()
        element(byXpath("//*[text()='ДДС-01 г.Черкесск']/../parent::div/following-sibling::div/label//input")).click()
        //element(byCssSelector("p#alert-dialog-description>div>div>div>div:nth-child(1) div[style='width: 100%;']>div:nth-child(2) input")).click()
        //////div[text()='ДДС-02 г.Черкесск']/../div/label/span/span/input
        //element(byCssSelector("form>div:nth-child(3) button:nth-child(1)")).click()
        element(byXpath("//span[text()='Назначить']/..")).click()
        element(byText("ДДС-02 г.Черкесск")).should(exist, ofSeconds(waitTime))
        element(byText("ДДС-01 г.Черкесск")).should(exist, ofSeconds(waitTime))
        element(byText("AutoTest S 0010 $dateTime")).should(exist, ofSeconds(waitTime))
        elements(byText("AutoTest S 0010 $dateTime"))
            .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(2))
        //вносим не значительное изменение


        Thread.sleep(5000)
    }




}