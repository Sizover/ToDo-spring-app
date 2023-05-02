package dicts

import BaseTest
import Retry
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.junit.jupiter.api.Assertions
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import test_library.menu.MyMenu
import test_library.menu.MyMenu.Dictionaries
import test_library.menu.MyMenu.Incidents
import test_library.statuses.StatusEnum.`В обработке`
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

class Dicts_Incidents :BaseTest() {


    @DataProvider(name = "Языки")
    open fun Statuses(): Any {
        return arrayOf<Array<Any>>(
            arrayOf("Русский", "Английский"),
            arrayOf("Английский", "Русский")
        )
    }

    @Test (retryAnalyzer = Retry::class, dataProvider = "Языки", groups = ["ALL"])
    fun `Dicts INC 0010 Проверка наличия и работоспособности языка обращения в карточке обращения`(language1 : String, language2 : String) {
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool(false)
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        menuNavigation(Incidents.IncidentsList, waitTime)
        //кликаем по "создать обращение"
        element(byXpath("//*[text()='Создать обращение']/text()/ancestor::button")).click()
        //заполняем карточку
        //Источник события - выбираем случайно
        createICToolCalltype("", waitTime)
        Thread.sleep(200)
        //язык обращения
        element(byXpath("//label[@id='language-label' and text()='Язык общения']/following-sibling::div/div[@id='language']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем язык
        element(byXpath("//ul[@aria-labelledby='language-label']/li[text()='$language1']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Номер телефона
        createICToolPhone("", waitTime)
        //ФИО
        createICToolFIO(generateLastNameF(),generateFirstNameI(), generateMiddleNameO(), waitTime)
        //адрес
        element(byXpath("//input[@id='callAddress']"))
            .click()
        element(byXpath("//input[@id='callAddress']"))
            .sendKeys("INC 0010 adr $dateTime")
        Thread.sleep(500)
        //заполняем дополнительную информацию
        createICToolsDopInfo("AutoTest INC 0010 inc $dateTime $language1", waitTime)
        //запоминаем источник обращения
        var callType = element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype']"))
            .ownText
        //регистрируем обращение
        element(byXpath("//*[text()='Создать карточку']/text()/ancestor::button")).click()
        //выбираем тип происшествия
        inputRandomNew("incidentTypeId-textfield", false, waitTime)
        //Создаем карточку
        pushButtonCreateIC("AutoTest INC 0010 inc $dateTime $language1", waitTime)
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card"))
            .should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus(`В обработке`, longWait)
        //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest INC 0010 inc $dateTime $language1", waitTime)
        //считаем количество обращений
        Assertions.assertTrue(
            elements(byXpath("//*[text()='1']/ancestor::div[@id='panel1a-header']//*[text()='$callType']"))
                .size
                == 1
        )
        element(byXpath("//main//header//*[text()='Обращения']//ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableColumnCheckbox("", true, waitTime)
        var languageColumn = tableNumberOfColumn("Язык", waitTime)
        var fioColumn = tableNumberOfColumn("ФИО", waitTime)
        var callTypeColumm = tableNumberOfColumn("Тип источника", waitTime)
        element(byXpath("//table/tbody//*[text()='INC 0010 adr $dateTime']/ancestor::tr/td[$languageColumn][text()='$language1']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//table/tbody//*[text()='INC 0010 adr $dateTime']/ancestor::tr/td[$callTypeColumm][text()='$callType']"))
            .should(exist, ofSeconds(waitTime))
        menuNavigation(Incidents.CreateIncident, waitTime)
        //заполняем карточку
        //Источник события - выбираем случайно
        createICToolCalltype("", waitTime)
        //язык обращения
        element(byXpath("//label[text()='Язык общения']/following-sibling::div/div[@id='language']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //выбираем язык
        element(byXpath("//ul[@aria-labelledby='language-label']/li[text()='$language2']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Номер телефона
        createICToolPhone("", waitTime)
        //ФИО
        createICToolFIO("$date AutoTestLastname call","INC 0010 Firstname", language2, waitTime)
        //адрес
//        addressInput("callAddress","INC 0010 adr $dateTime", waitTime)
        element(byXpath("//input[@id='callAddress']"))
            .click()
        element(byXpath("//input[@id='callAddress']"))
            .sendKeys("INC 0010 adr2 $dateTime")
        Thread.sleep(500)
        //заполняем дополнительную информацию
        createICToolsDopInfo("AutoTest call INC 0010 $dateTime $language2", waitTime)
        //запоминаем источник обращения
        callType = element(byXpath("//label[text()='Источник события']/following-sibling::div/div[@id='calltype']"))
            .ownText
        //делаем привязку к первой КП
        element(byXpath("//*[text()='INC 0010 adr $dateTime']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        Thread.sleep(500)
        element(byXpath("//*[text()='Привязать к происшествию']/text()/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus(`В обработке`, waitTime)
        //развернём второе обращение
        element(byXpath("//*[text()='2']/ancestor::div[@id='panel1a-header']//*[name()='svg']/ancestor::div[1]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //и что это именно так карточка которую мы только что создали
        checkICToolDopInfo("AutoTest call INC 0010 $dateTime $language2", waitTime)
        Assertions.assertTrue(
            element(byXpath("//*[text()='2']/ancestor::div[@id='panel1a-header']//*[text()='$callType']"))
                .exists()
        )
        element(byXpath("//main//header//*[text()='Обращения']//ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//table"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableColumnCheckbox("", true, waitTime)
        languageColumn = tableNumberOfColumn("Язык", waitTime)
        fioColumn = tableNumberOfColumn("ФИО", waitTime)
        callTypeColumm = tableNumberOfColumn("Тип источника", waitTime)
        element(byXpath("//table/tbody//*[text()='INC 0010 adr2 $dateTime']/ancestor::tr/td[$languageColumn]//text()/parent::*[text()='$language2']"))
            .should(exist, ofSeconds(longWait))
        element(byXpath("//table/tbody//*[text()='INC 0010 adr2 $dateTime']/ancestor::tr/td[$callTypeColumm]//text()/parent::*[text()='$callType']"))
            .should(exist, ofSeconds(longWait))
        logoffTool()
    }

    @Test (retryAnalyzer = Retry::class, groups = ["ALL"])
    fun `Dicts INC 0020 Проверка подсказки КП`() {
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        val incidentTypesMap: MutableMap<String, String> = mutableMapOf()
        logonTool(false)
        //находим организацию, адрес которой будем указывать в КП, что бы потом проверить наличие этой организации в подсказке
        menuNavigation(Dictionaries.Companies, waitTime)
        tableStringsOnPage(50, waitTime)
        tableColumnCheckbox("Наименование;Физ.адрес;Метки;Телефон организации", true, waitTime)
        var nameColumn = tableNumberOfColumn("Наименование", waitTime)
        var addressOrTupeColumn = tableNumberOfColumn("Физ.адрес", waitTime)
        var labelsColumn = tableNumberOfColumn("Метки", waitTime)
        val telColumn = tableNumberOfColumn("Телефон организации", waitTime)
        var max = elements(byXpath("//table/tbody/tr")).size
        var rnd = 0
        var again = true
        while (again){
            rnd = (1..max).random()
            if (element(byXpath("//table/tbody/tr[$rnd]/td[$addressOrTupeColumn]//text()/..")).exists()){
                if (element(byXpath("//table/tbody/tr[$rnd]/td[$addressOrTupeColumn]//text()/..")).ownText.contains(", д ")){
                    again = false
                }
            }
        }
        val orgName = element(byXpath("//table/tbody/tr[$rnd]/td[$nameColumn]//text()/..")).ownText
        val orgAddress = element(byXpath("//table/tbody/tr[$rnd]/td[$addressOrTupeColumn]//text()/..")).ownText
        val orgTel = element(byXpath("//table/tbody/tr[$rnd]/td[$telColumn]//text()/..")).ownText
        val orgLabelsList = elements(byXpath("//table/tbody/tr[$rnd]/td[$labelsColumn]//text()/..")).map { it.ownText }
        //находим статью с меткой и статью с типом происшествия, что бы потом проверить обе в подсказке
        menuNavigation(MyMenu.KB.Articles, waitTime)
        tableStringsOnPage(50, waitTime)
        tableColumnCheckbox("Статья;Типы происшествий;Метки",true, waitTime)
        nameColumn = tableNumberOfColumn("Статья", waitTime)
        addressOrTupeColumn = tableNumberOfColumn("Типы происшествий", waitTime)
        labelsColumn = tableNumberOfColumn("Метки", waitTime)
        max = elements(byXpath("//table/tbody/tr")).size
        do {
            rnd = (1..max).random()
        } while (!element(byXpath("//table/tbody/tr[$rnd]/td[$addressOrTupeColumn]//text()/..")).exists())
//        var articleName = element(byXpath("//table/tbody/tr[$rnd]/td[$nameColumn]//text()/..")).ownText
        //создаем пару: имя статьи - тип происшествия
        val acticleForType = element(byXpath("//table/tbody/tr[$rnd]/td[$nameColumn]//text()/..")).ownText to elements(byXpath("//table/tbody/tr[$rnd]/td[$addressOrTupeColumn]//text()/..")).map { it.ownText }.random()
        //создаем пару: имя статьи - метка
        do {
            rnd = (1..max).random()
        } while (!element(byXpath("//table/tbody/tr[$rnd]/td[$labelsColumn]//text()/..")).exists())
        val acticleForLabel = element(byXpath("//table/tbody/tr[$rnd]/td[$nameColumn]//text()/..")).ownText to elements(byXpath("//table/tbody/tr[$rnd]/td[$labelsColumn]//text()/..")).map { it.ownText }.random()
        //т.к. код может быть не полный, то идем в справочник типов происшествия и находим ближайший подходящий конечный тип происшествия
        menuNavigation(Dictionaries.IncidentTypes, waitTime)
        tableColumnCheckbox("Код;Тип происшествия", true, waitTime)
        //ждем
        element(byXpath("//table/thead/tr/th[1]//*[contains(@name,'arrow')]/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//table/tbody/tr/td[1]//*[contains(@name,'arrow')]/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //раскрываем всю иерархию
        while (element(byXpath("//table/tbody/tr/td[1]//*[@name='arrowRight']/ancestor::button")).exists()){
            element(byXpath("//table/thead/tr/th[1]//*[contains(@name,'arrow')]/ancestor::button"))
                .click()
            Thread.sleep(300)
        }
        nameColumn = tableNumberOfColumn("Тип происшествия", waitTime)
        addressOrTupeColumn = tableNumberOfColumn("Код", waitTime)
        var incidentTypeCodeForIC = ""
        var incidentTypeNameForIC = ""
        var incidentTypeInIC = ""
        if (element(byXpath("//table/tbody//td[$addressOrTupeColumn]//text()/parent::*[text()='${acticleForType.second}']/ancestor::tr//*[contains(@name,'arrow')]")).exists()){
            incidentTypeCodeForIC = element(byXpath("(//table/tbody//td[$addressOrTupeColumn]//text()/parent::*[text()='${acticleForType.second}']/ancestor::tr/following-sibling::tr[not(.//*[contains(@name,'arrow')])])[1]/td[$addressOrTupeColumn]//text()/..")).ownText
            incidentTypeNameForIC = element(byXpath("(//table/tbody//td[$addressOrTupeColumn]//text()/parent::*[text()='${acticleForType.second}']/ancestor::tr/following-sibling::tr[not(.//*[contains(@name,'arrow')])])[1]/td[$nameColumn]//text()/..")).ownText
            incidentTypeInIC = "$incidentTypeCodeForIC $incidentTypeNameForIC"
        } else {
            incidentTypeCodeForIC = acticleForType.second
            incidentTypeNameForIC = element(byXpath("//table/tbody//td[$addressOrTupeColumn]//text()/parent::*[text()='${acticleForType.second}']/ancestor::tr/td[$nameColumn]//text()/..")).ownText
            incidentTypeInIC = "$incidentTypeCodeForIC $incidentTypeNameForIC"
        }
        //создаем КП
        menuNavigation(Incidents.CreateIncident, waitTime)
        createICToolCalltype("", waitTime)
        createICToolPhone("", waitTime)
        createICToolFIO(generateLastNameF(), generateFirstNameI(), generateMiddleNameO(), waitTime)
        createICToolAddressInput("callAddress", orgAddress, waitTime)
        createICToolsDopInfo("Dicts INC 0010 Проверка подсказки КП $dateTime", waitTime)
        createICToolButtonCreateNewCall()
        createICToolSelectIncidentType(incidentTypeInIC, waitTime)
        pushButtonCreateIC("Dicts INC 0010 Проверка подсказки КП $dateTime", waitTime)
        //проверяем подсказки
        element(byXpath("//h3[text()='База знаний']/following-sibling::*//a[contains(@href,'/kb/articles/') and text()='${acticleForType.first}']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//h3[text()='Ближайшие объекты']/following-sibling::*//a[contains(@href,'/dicts/companies/') and text()='$orgName']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Thread.sleep(500)
        //TODO дописать тест по завершении реализации
    }

}