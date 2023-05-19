

import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.testng.annotations.DataProvider
import test_library.filters.FilterEnum
import test_library.menu.MyMenu
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PlayGround : BaseTest(){



    @DataProvider(name = "TestProvider")
    fun TestProviderFun(): Array<Array<String>> {
        return arrayOf<Array<String>>(
            arrayOf("Русский", "Английский")
        )

    }

//    @Test (retryAnalyzer = Retry::class, dataProvider = "TestProvider", groups = ["LOCAL"])
//    @Parameters("testENV")
    @org.testng.annotations.Test(retryAnalyzer = Retry::class, dataProvider = "TestProvider", groups = ["LOCAL"])
    fun `Черновик`(language1 : String, language2 : String) {
    //сначала соберем номера за последний месяц
    date = LocalDate.now()
    dateTime = LocalDateTime.now()
    val menuList = listOf(MyMenu.Incidents.IncidentsList, MyMenu.Incidents.IncidentsArchive)
    val falseCallsNumbersList = mutableListOf<String>()
    val moreMonthFalseCallsNumbersList = mutableListOf<String>()
    var anotherRound: Boolean
    logonTool(false)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    menuList.forEach { menu ->
        menuNavigation(menu, waitTime)
        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Отчищаем фильтры
        if (menu != MyMenu.Incidents.IncidentsArchive){
            cleanFilterByEnum(listOf(), waitTime)
        }
        //устанавливаем фильры "Типы происшествий", "Дата регистрации", "Источники"
        setFilterByEnum(FilterEnum.Типы_происшествий, "Л Ложные", waitTime)
        setFilterByEnum(
            FilterEnum.Дата_регистрации,
            "${LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))};",
            waitTime)
        setFilterByEnum(FilterEnum.Источники, "Портал населения;Система-112;СМС;Телефон (ССОП);Факс;ЭРА Глонасс", waitTime)
        //заполнили фильтры
//            setIncidentFilters(LocalDate.now().minusMonths(1).toString(), LocalDate.now().toString())

        Thread.sleep(1000)
        //открываем все КП, проходясь и по пагинации и складываем контактный номер в список, не занося в него дубликаты
        if (elements(byXpath("//table/tbody/tr/td//*[text()='Нет данных']")).size == 0) {
            do {
                Thread.sleep(500)
                for (i in 1..elements(byXpath("//table/tbody/tr")).size) {
                    element(byXpath("//table/tbody/tr[$i]"))
                        .click()
                    element(byXpath("//strong[text()='Контактный номер:']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    element(byXpath("//*[@id='incidentButtons']//*[text()='Ложное']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    with(element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]")).ownText)
                    {
                        if (
                            (!falseCallsNumbersList.contains(this.filter { it.isDigit() }))
                            &&
                            ((this.filter { it.isDigit() }).length > 8)
                        ){
                            falseCallsNumbersList.add(this.filter { it.isDigit() })
                        }
                    }
                    if (
                        (!falseCallsNumbersList.contains(element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]"))
                            .ownText
                            .filter { it.isDigit() }))
                        &&
                        ((element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]"))
                            .ownText
                            .filter { it.isDigit() }).length > 8)
                    )
                    {
                        falseCallsNumbersList.add(element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]"))
                            .ownText
                            .filter { it.isDigit() })
                    }
                    Selenide.back()
                    Thread.sleep(200)
                }
                if (element(byXpath("(//tfoot//nav/ul/li//*[contains(@aria-label,'На страницу номер')])[last()]/parent::div"))
                        .getAttribute("style")!!
                        .contains("color: black;")) {
                    anotherRound = true
                    element(byXpath("//tfoot//nav//span[contains(@aria-label,'На следующую страницу')]/p[text()='Вперед']"))
                        .click()
                } else {
                    anotherRound = false
                }
            } while (anotherRound)
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    menuList.forEach { menu ->
        menuNavigation(menu, waitTime)
        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        if (moreMonthFalseCallsNumbersList.size <= 10) {
            //Отчищаем фильтры
            cleanFilterByEnum(listOf(), waitTime)
            //устанавливаем фильры "Типы происшествий", "Дата регистрации", "Источники"
            setFilterByEnum(FilterEnum.Типы_происшествий, "Л Ложные", waitTime)
            setFilterByEnum(
                FilterEnum.Дата_регистрации,
                ";${LocalDate.now().minusMonths(1).minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                waitTime)
            setFilterByEnum(FilterEnum.Источники, "Портал населения;Система-112;СМС;Телефон (ССОП);Факс;ЭРА Глонасс", waitTime)
            //заполнили фильтры
            Thread.sleep(1000)
            if (elements(byXpath("//table/tbody/tr/td//*[text()='Нет данных']")).size == 0) {
                do {
                    Thread.sleep(500)
                    val stringCount = elements(byXpath("//table/tbody/tr")).size
                    for (i in 1..stringCount) {
                        element(byXpath("//table/tbody/tr[$i]"))
                            .click()
                        element(byXpath("//strong[text()='Контактный номер:']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        element(byXpath("//*[@id='incidentButtons']//*[text()='Ложное']/text()/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        with(element(byXpath("//strong[text()='Контактный номер:']/following-sibling::span//*[text()]")).ownText)
                        {
                            if ((!moreMonthFalseCallsNumbersList.contains(this.filter { it.isDigit() }))
                                &&
                                ((this.filter { it.isDigit() }).length > 8)
                            ){
                                moreMonthFalseCallsNumbersList.add(this.filter { it.isDigit() })
                            }
                        }
                        Selenide.back()
                        Thread.sleep(200)
                    }
                    if ((element(byXpath("(//tfoot//nav/ul/li//*[contains(@aria-label,'На страницу номер')])[last()]/parent::div"))
                            .getAttribute("style")!!
                            .contains("color: black;"))
                        && (moreMonthFalseCallsNumbersList.size <= 10)
                    ) {
                        anotherRound = true
                        element(byXpath("//tfoot//nav/ul/li//*[contains(@aria-label,'На следующую страницу')]/p[text()='Вперед']"))
                            .click()
                    } else {
                        anotherRound = false
                    }
                } while (anotherRound)
            }
        }
    }
    //меняем фильтры и заполняем список ложных, но более месяца, не более чем 10 записями
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //переходим в форму обращения, вставляем номер ложный моложе месяца, ждем банер,
    // очищаем вставляем ложный более месяца, банера быть не должно
    menuNavigation(MyMenu.Incidents.CreateIncident, waitTime)
    createICToolCalltype("Телефон (ССОП)", waitTime)
    var rndTel = falseCallsNumbersList.random()
    createICToolPhone(rndTel, waitTime)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //маленький WA
    try {
        element(byXpath("//*[@name='noteError']/ancestor::div[@role='alert']//*[text()='Номер данного абонента был зафиксирован ранее как ложный']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
    } catch (_:  Throwable) {
        if (print) println("Autotest \"Event INC 5030 Предупреждение о ложном вызове актуально месяц\" said: \"ЪУЪ! Чините багу PM1729!\"")
        clearInput("//input[@id='phone']", waitTime)
        createICToolPhone("+$rndTel", waitTime)
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    element(byXpath("//*[@name='noteError']/ancestor::div[@role='alert']//*[text()='Номер данного абонента был зафиксирован ранее как ложный']"))
        .should(exist, ofSeconds(waitTime))
        .shouldBe(visible, ofSeconds(waitTime))
    rndTel = moreMonthFalseCallsNumbersList.random()
    clearInput("//input[@id='phone']", waitTime)
    createICToolPhone(rndTel, waitTime)
    Thread.sleep(5000)
    element(byXpath("//*[@name='noteError']/ancestor::div[@role='alert']//*[text()='Номер данного абонента был зафиксирован ранее как ложный']"))
        .shouldNot(exist, ofSeconds(waitTime))
    clearInput("//input[@id='phone']", waitTime)
    createICToolPhone("+$rndTel", waitTime)
    Thread.sleep(5000)
    element(byXpath("//*[@name='noteError']/ancestor::div[@role='alert']//*[text()='Номер данного абонента был зафиксирован ранее как ложный']"))
        .shouldNot(exist, ofSeconds(waitTime))

    /*        element(byXpath("//span[text()='Отменить']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='dialog']//span[text()='Да, выйти без сохранения']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()*/
    }
}