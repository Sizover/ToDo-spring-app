package reports

import Retry
import BaseTest
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.SelenideElement.*
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.LinkedHashMap
import kotlin.random.Random

class Reports : BaseTest(){

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `Reports 0010 Проверка формирования отчетов по обращениям`() {
        //A.3.23 Проверка формирования отчетов по обращениям
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool()
        //кликаем по иконке отчетов
        //Переходим в "отчет по обращениям"
        menuNavigation("Отчеты","По обращениям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("Reports 0010 Проверка формирования отчетов по обращениям $dateTime отсчет")
        //впердоливаем говнокод по преобразование даты
        val dateM = date.toString().split("-")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        //вбиваем адрес
        addressInput("address", "Карачаево-Черкесская Респ, г Карачаевск", waitTime)
        //создаем отчет
        element(byXpath("//span[text()='Создать']/..")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("Reports 0010 Проверка формирования отчетов по обращениям $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //проверяем и запоминаем общее количество обращений
        val tableSelector = "//table/tbody/tr/td[text()='%s']/following-sibling::td"
        val all = element(byXpath(tableSelector.format("Общее количество вызовов (обращений):"))).ownText.toInt()
        //ложных
        val fal = element(byXpath(tableSelector.format("Ложных"))).ownText.toInt()
        //консультаций
        val con = element(byXpath(tableSelector.format("Консультаций"))).ownText.toInt()
        //по происшествиям
        val inc = element(byXpath(tableSelector.format("По происшествиям"))).ownText.toInt()
        var falD = 0
        var conD =0
        var incD = 0
        //сверим цветастую табличку с диаграммой
        val diagramSelector = "g.recharts-layer.recharts-pie-labels>g:nth-child(%d)>text"
        //если вобще есть диаграмма
        if (all > 0){
            //ложные, если они есть
            if (elements(byCssSelector("text[name='Ложных']")).size == 0){
                falD = element(byCssSelector(diagramSelector.format(1))).ownText.toInt()
                Assertions.assertTrue(fal == falD)
            }
            //консультации, если они есть
            if (elements(byCssSelector("text[name='Консультаций']")).size == 0){
                conD = element(byCssSelector(diagramSelector.format(2))).ownText.toInt()
                Assertions.assertTrue(con == conD)
            }
            //происшествия, если они есть
            if (elements(byCssSelector("text[name='По происшествиям']")).size == 0){
                incD = element(byCssSelector(diagramSelector.format(3))).ownText.toInt()
                Assertions.assertTrue(inc == incD)
            }
        }
        //Сверяем, но возможна ситуация, когда отношение количества каких-то обращений к остальным, мало и обращения есть, а цифры на диаграмме нету, решать будем, когда появится такой отчет
        //рассматриваем таблицу источников (которой может и не существовать)
        var tableAll =0
        var tableVideo = 0
        var tableAIS = 0
        var tableSensor = 0
        var tablePortal = 0
        var tableUIV = 0
        //println(tableAll)
        if (elements(byXpath("//table/tbody/tr[1]//*[text()='Нет данных']")).size == 0) {
            //всего записей в смысле в графе всего
            if (elements(byXpath(tableSelector.format("Всего"))).size != 0) {
                tableAll = element(byXpath(tableSelector.format("Всего"))).ownText.toInt()
            }
            //Видеоаналитика из общего числа
            if (elements(byXpath(tableSelector.format("Видеоаналитика"))).size != 0) {
                tableVideo = element(byXpath(tableSelector.format("Видеоаналитика"))).ownText.toInt()
            }
            //Внешняя АИС из общего числа
            if (elements(byXpath(tableSelector.format("Внешняя АИС"))).size != 0) {
                tableAIS = element(byXpath(tableSelector.format("Внешняя АИС"))).ownText.toInt()
            }
            //Датчик из общего числа
            if (elements(byXpath(tableSelector.format("Датчик"))).size != 0) {
                tableSensor = element(byXpath(tableSelector.format("Датчик"))).ownText.toInt()
            }
            //Портал населения из общего числа
            if (elements(byXpath(tableSelector.format("Портал населения"))).size != 0) {
                tablePortal = element(byXpath(tableSelector.format("Портал населения"))).ownText.toInt()
            }
            //Портал УИВ из общего числа
            if (elements(byXpath(tableSelector.format("Портал УИВ"))).size != 0) {
                tableUIV = element(byXpath(tableSelector.format("Портал УИВ"))).ownText.toInt()
            }
        }
//        println("all $all")
//        println("fal $fal")
//        println("con $con")
//        println("inc $inc")
//        println("tableAll $tableAll")
//        println("Видеоаналитика $tableVideo")
//        println("Внешняя АИС $tableAIS")
//        println("Датчик $tableSensor")
//        println("Портал населения $tablePortal")
//        println("Портал УИВ $tableUIV")
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var adr: String = ""
        var dateTime2 = LocalDateTime.now()
        for (i in 1..6) {
            dateTime = LocalDateTime.now()
            date = LocalDate.now()
            //кликаем по иконке происшествий в боковом меню
            //Переходим в "Список происшетвий"
            menuNavigation("Происшествия","Список происшествий",waitTime)
            //кликаем по "создать обращение"
            element(byXpath("//span[text()='Создать обращение']/..")).click()
            //заполняем карточку
            //Источник события - выбираем по порядку
            element(byCssSelector("div#calltype")).click()
            element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($i)")).click()
            //Номер телефона
            createICToolPhone("", waitTime)
            //ФИО
            createICToolFIO(date.toString() + "_AutoTestLastname", "AutoTest_Reports_0010", "", waitTime)
            //Вводим случайный адрес
            val bB = (1..100).random()
            if (i == 2){
                addressInput("callAddress", "Карачаево-Черкесская Респ, г Карачаевск $bB", waitTime)
                //запоминаем адрес, что бы ввести его потом
                adr = element(byCssSelector("input#callAddress")).value.toString()
                dateTime2 = dateTime
            } else if (i == 3){
                //Вводим ранее запомненный адрес
                addressInput("callAddress", adr, waitTime)
            } else if (i == 6){
                //Вводим адрес не попадающий в отчет
                addressInput("callAddress", "г Черкесск, ул Мира $bB", waitTime)
            } else {
                addressInput("callAddress", "Карачаево-Черкесская Респ, г Карачаевск $bB", waitTime)
            }
            //заполняем дополнительную информацию
            createICToolsDopInfo("Reports 0010, i=$i $dateTime", waitTime)
            if (i < 3 || i == 6) {
                //регистрируем обращение
                element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
                //выбираем тип происшествия
                element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test")
                    .sendKeys(Keys.DOWN, Keys.ENTER)
                //Создаем карточку
                pushButtonCreateIC("Reports 0010, i=$i $dateTime", waitTime)
            } else if (i == 3) {
                //регистрируем обращение в ранее созданную карточку.
//                element(byText(adr)).click()
                element(byXpath("//*[@name='refetch']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//*[contains(text(),'$adr')]/ancestor::button")).click()
                element(byXpath("//span[text()='Привязать к происшествию']/parent::button")).click()
            } else if (i == 4) {
                //регистрируем ложное обращение
                element(byXpath("//*[text()='Ложное обращение']/ancestor::button")).click()
                //ждем загрузки таблицы происшествий
                element(byCssSelector("main div table>tbody"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } else if (i == 5) {
                //регистрируем консультацию
                element(byXpath("//span[text()='Консультация']/parent::button")).click()
                //ждем загрузки таблицы происшествий
                element(byCssSelector("main div table>tbody"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            if (i < 4 || i == 6) {
                //Убеждаемся, что нам загрузило созданную карточку
                //проверяя что нам в принципе загрузило какую-то карточку
                element(byCssSelector("#simple-tabpanel-card"))
                    .should(exist, ofSeconds(waitTime))
                //что она в нужном статусе
                checkICToolIsStatus("В обработке", waitTime)
            }
            //и что это именно так карточка которую мы только что создали
            if (i < 3 || i == 6) {
                checkICToolDopInfo("Reports 0010, i=$i $dateTime", waitTime)
            } else if(i==3) {
                checkICToolDopInfo("Reports 0010, i=2 $dateTime2", waitTime)
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //кликаем по иконке отчетов
        //Переходим в "отчет по обращениям"
        menuNavigation("Отчеты", "По обращениям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button")).click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("Reports 0010 Проверка формирования отчетов по обращениям $dateTime сверка")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        //вбиваем адрес
        addressInput("address","Карачаево-Черкесская Респ, г Карачаевск",waitTime)
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("Reports 0010 Проверка формирования отчетов по обращениям $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //проверяем и запоминаем общее количество обращений
        val allT = element(byXpath(tableSelector.format("Общее количество вызовов (обращений):"))).ownText.toInt()
        //ложных
        val falT = element(byXpath(tableSelector.format("Ложных"))).ownText.toInt()
        //консультаций
        val conT = element(byXpath(tableSelector.format("Консультаций"))).ownText.toInt()
        //по происшествиям
        val incT = element(byXpath(tableSelector.format("По происшествиям"))).ownText.toInt()
        //сверим цветастую табличку с диаграммой
        //ложные
        if (elements(byCssSelector(diagramSelector.format(1))).size == 1){
            falD = element(byCssSelector(diagramSelector.format(1))).ownText.toInt()
            Assertions.assertTrue(falT == falD)
        }
        //консультации
        if (elements(byCssSelector(diagramSelector.format(2))).size == 1){
            conD = element(byCssSelector(diagramSelector.format(2))).ownText.toInt()
            Assertions.assertTrue(conT == conD)
        }
        //происшествия
        if (elements(byCssSelector(diagramSelector.format(3))).size == 1){
            incD = element(byCssSelector(diagramSelector.format(3))).ownText.toInt()
            Assertions.assertTrue(incT == incD)
        }
        //рассматриваем таблицу источников
        //всего записей в смысле в графе всего
        val tableAllT =
            element(byXpath(tableSelector.format("Всего"))).ownText.toInt()
        //println(tableAll)
        //Видеоаналитика из общего числа
        val tableVideoT: Int =
            element(byXpath(tableSelector.format("Видеоаналитика"))).ownText.toInt()
        //Внешняя АИС из общего числа
        val tableAIST: Int =
            element(byXpath(tableSelector.format("Внешняя АИС"))).ownText.toInt()
        //Датчик из общего числа
        val tableSensorT: Int =
            element(byXpath(tableSelector.format("Датчик"))).ownText.toInt()
        //Портал населения из общего числа
        val tablePortalT: Int =
            element(byXpath(tableSelector.format("Портал населения"))).ownText.toInt()
        //Портал УИВ из общего числа
        val tableUIVT: Int =
            element(byXpath(tableSelector.format("Портал УИВ"))).ownText.toInt()
        //служебный счетчик, всего строк
//        println("allT $allT")
//        println("falT $falT")
//        println("conT $conT")
//        println("incT $incT")
//        println("tableAllT $tableAllT")
//        println("ВидеоаналитикаT $tableVideoT")
//        println("Внешняя АИС T $tableAIST")
//        println("ДатчикT $tableSensorT")
//        println("Портал населенияT $tablePortalT")
//        println("Портал УИВ T $tableUIVT")
        //сравниваем старуе значения в старом отчете, со значениями в новом
        Assertions.assertTrue(allT == all + 5)
        Assertions.assertTrue(falT == fal + 1)
        Assertions.assertTrue(conT == con + 1)
        Assertions.assertTrue(incT == inc + 3)
        Assertions.assertTrue(tableAll == all)
        Assertions.assertTrue(tableAllT == allT)
        Assertions.assertTrue(tableAllT == tableAll + 5)
        Assertions.assertTrue(tableVideoT == tableVideo + 1)
        Assertions.assertTrue(tableAIST == tableAIS + 1)
        Assertions.assertTrue(tableSensorT == tableSensor + 1)
        Assertions.assertTrue(tablePortalT == tablePortal + 1)
        Assertions.assertTrue(tableUIVT == tableUIV + 1)
        //Карачаево-Черкесская Респ, г Карачаевск
        //Thread.sleep(50000)

        logoffTool()
    }



    @Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `Reports 0020 Проверка формирования отчетов по деятельности сотрудников`() {
        //A.3.24 Проверка формирования отчетов по деятельности сотрудников
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        logonTool()
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        menuNavigation("Отчеты", "По сотрудникам", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button")).click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("Reports 0020 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет")
        //впердоливаем говнокод по преобразование даты
        val dateM = date.toString().split("-")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address", "Карачаево-Черкесская Респ, г Карачаевск", waitTime)
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("Reports 0020 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //убедимся что мы за оператор:
        //кликаем по иконке оператора сверху справа
        element(byXpath("//header//*[name()='svg' and @name='user']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //пероеходим в профиль пользователя
        element(byXpath("//div[@role='presentation']//*[text()='Профиль пользователя']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Извлекаем имя оператора
        element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        val operator = element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p")).ownText.trim()
        back()
        //ждем
        element(byXpath("//h4[text()='Отчет']/following-sibling::h4[text()='Reports 0020 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //служебный счетчик
        var first = 0
        var second = 0
        var third = 0
        var fourth = 0
        var fifth = 0
        var sixth = 0
        var seventh = 0
        var operators = elements(byXpath("//tbody/tr/td")).size
        for (w in 1..operators) {
            val momentOperator =
                element(byXpath("//tbody/tr[$w]/td[1]")).ownText.trim()
//            println("0 momentOperator $momentOperator")
            if (operator == momentOperator) {
                //поехали запоминать значения в отчете для оператора
                val operatorDataSelector = "//tbody/tr[$w]/td[%d]"
                first = element(byXpath(operatorDataSelector.format(3))).ownText.toInt()
                second = element(byXpath(operatorDataSelector.format(4))).ownText.toInt()
                third = element(byXpath(operatorDataSelector.format(5))).ownText.toInt()
                fourth = element(byXpath(operatorDataSelector.format(6))).ownText.toInt()
                fifth = element(byXpath(operatorDataSelector.format(7))).ownText.toInt()
                sixth = element(byXpath(operatorDataSelector.format(8))).ownText.toInt()
                seventh = element(byXpath(operatorDataSelector.format(9))).ownText.toInt()
                break
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        var adr: String = ""
        var dateTime2 = LocalDateTime.now()
        for (i in 1..6) {
            dateTime = LocalDateTime.now()
            date = LocalDate.now()
            //кликаем по иконке происшествий в боковом меню
            //Переходим в "Список происшетвий"
            menuNavigation("Происшествия", "Список происшествий", waitTime)
            //кликаем по "создать обращение"
            element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
            //заполняем карточку
            //Источник события
            createICToolCalltype("", waitTime)
            //Номер телефона
            createICToolPhone("", waitTime)
            //ФИО
            createICToolFIO("$date AutoTest", "Reports 0020", "", waitTime)
            //Вводим случайный адрес
//            var aA = ('A'..'Z').random()
            val bB = (1..100).random()
            if (i != 4){
                addressInput("callAddress", "Карачаево-Черкесская Респ, г Карачаевск $bB", waitTime)
            } else {
                addressInput("callAddress", adr, waitTime)
            }
            //запоминаем адрес
            //Thread.sleep(500)
            if (i == 3) {
                adr = element(byCssSelector("input#callAddress")).value.toString()
                dateTime2 = dateTime
            }
            //заполняем дополнительную информацию
            createICToolsDopInfo("Reports 0020, i=$i $dateTime", waitTime)
            if (i < 4) {
                //регистрируем обращение
                element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
                //выбираем тип происшествия
                element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test")
                    .sendKeys(Keys.DOWN, Keys.ENTER)
                //Создаем карточку
                element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()
            } else if (i == 4) {
                //регистрируем обращение в ранее созданную карточку.
                element(byXpath("//*[@name='refetch']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byText(adr))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//span[text()='Привязать к происшествию']/parent::button")).click()
            } else if (i == 5) {
                //регистрируем ложное обращение
                element(byXpath("//*[text()='Ложное обращение']/ancestor::button")).click()
                //ждем загрузки таблицы происшествий
                element(byCssSelector("main div table>tbody"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } else if (i == 6) {
                //регистрируем консультацию
                element(byXpath("//span[text()='Консультация']/parent::button")).click()
                //ждем загрузки таблицы происшествий
                element(byCssSelector("main div table>tbody"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //Убеждаемся, что нам загрузило созданную карточку
            //проверяя что нам в принципе загрузило какую-то карточку (кроме случаев ложного и консультации)
            if (i<5) {
                element(byCssSelector("#simple-tabpanel-card"))
                    .should(exist, ofSeconds(waitTime))
                //что она в нужном статусе
                checkICToolIsStatus("В обработке", waitTime)
            }
            if (i == 4) {
                //закрываем одну карточку
                updateICToolStatus("Завершена", waitTime)
                updateICToolStatus("Закрыта", waitTime)
            }
            //и что это именно так карточка которую мы только что создали
            if (i < 4) {
                checkICToolDopInfo("Reports 0020, i=$i $dateTime", waitTime)
            } else if (i==4) {
                checkICToolDopInfo("Reports 0020, i=3 $dateTime2", waitTime)
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        menuNavigation("Отчеты", "По сотрудникам", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title")).sendKeys("Reports 0020 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address","Карачаево-Черкесская Респ, г Карачаевск",waitTime)
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("Reports 0020 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //ждем
        element(byXpath("//h6[contains(text(),'по муниципальному образованию')]/../h5"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            .shouldHave(text("Reports 0020 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка"))
        //служебный счетчик числа операторов
        operators = elements(byXpath("//tbody/tr/td")).size
        var firstT = 0
        var secondT = 0
        var thirdT = 0
        var fourthT = 0
        var fifthT = 0
        var sixthT = 0
        var seventhT = 0
        //ищем кто в списке мы
        for (e in 1..operators) {
            val momentOperator =
                element(byXpath("//tbody/tr[$e]/td[1]")).ownText.trim()
            if (operator == momentOperator) {
                val operatorDataSelector = "//tbody/tr[$e]/td[%d]"
                //поехали запоминать значения в отчете для оператора
                firstT = element(byXpath(operatorDataSelector.format(3))).ownText.toInt()
                secondT = element(byXpath(operatorDataSelector.format(4))).ownText.toInt()
                thirdT = element(byXpath(operatorDataSelector.format(5))).ownText.toInt()
                fourthT = element(byXpath(operatorDataSelector.format(6))).ownText.toInt()
                fifthT = element(byXpath(operatorDataSelector.format(7))).ownText.toInt()
                sixthT = element(byXpath(operatorDataSelector.format(8))).ownText.toInt()
                seventhT = element(byXpath(operatorDataSelector.format(9))).ownText.toInt()
                break
            }
        }
//        println("first $first")
//        println("second $second")
//        println("third $third")
//        println("fourth $fourth")
//        println("fifth $fifth")
//        println("sixth $sixth")
//        println("seventh $seventh")
//        println("firstT $firstT")
//        println("secondT $secondT")
//        println("thirdT $thirdT")
//        println("fourthT $fourthT")
//        println("fifthT $fifthT")
//        println("sixthT $sixthT")
//        println("seventhT $seventhT")
        Assertions.assertTrue(firstT == first + 5)
        Assertions.assertTrue(thirdT == third + 2)
        Assertions.assertTrue(fourthT == fourth + 2)
        Assertions.assertTrue(fifthT == fifth + 1)
        Assertions.assertTrue(sixthT == sixth + 1)
        Assertions.assertTrue(seventhT == seventh + 1)

        //Thread.sleep(50000)
        logoffTool()

    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `Reports 0030 проверка отчетов по происшествиям с использованием фильтров по пострадавшим, адресу и типу происшествия`() {
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        //возможные статусы КП
        val statusList = listOf("Новая", "В обработке", "Реагирование", "Завершена", "Отменена","Закрыта")
        //два отчета, с адресом и без, вынесены с список, для сокращения кода через forEach
        val reportsAddressList = mutableListOf("без адреса", "адресный")
        //временный список отчетов в которые попадет создаваемая КП
        val icReportsList = mutableListOf<String>()
        //одна строка любой таблицы (кроме первого столбца - он ключ)
        val tableRowList: MutableList<Int> = mutableListOf()
        //Карта вмещающая в себя один отчет как значение и некоторое внутри тестовое значение как ключ (reportsAddressList)
        val oneReportMap: LinkedHashMap<String, MutableList<Int>> = linkedMapOf()
        //данные "старого" отчета к которым прибавятся новые, по мере их создания
        val oldValuesMap: LinkedHashMap<String, MutableMap<String, MutableList<Int>>> = linkedMapOf()
        //данные "нового" отчета для сравнения с "старыми" данными
        val newValuesMap: LinkedHashMap<String, MutableMap<String, MutableList<Int>>> = linkedMapOf()
        //Операделим по какому типу потерь будем потом создавать отчет
        val lookingForVictims = (1..4).random()
        //Опеределим сколько жертв будем искать
        val nVictims = if (lookingForVictims > 2){
            (12..38).random()
        } else {
            (25..75).random()
        }
        //по какому типу происшествия строим отчет
        var icReportCreate: String = ""
        logonTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("form[novalidate]"))
            .should(exist, ofSeconds(waitTime))
        //впердоливаем говнокод по преобразование даты
        val today = date.toString().split("-")
        //заполняем дату начала и конца периода отчета сегодняшним числом
        element(byCssSelector("form[novalidate] input#periodStart"))
            .sendKeys(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        element(byCssSelector("form[novalidate] input#periodEnd"))
            .sendKeys(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        //Определим имя импута по потерям
        val victimsName = element(byXpath("(//form[@novalidate]//label[contains(text(),'Число')])[$lookingForVictims]")).ownText
        //добавим новый отчет в список
        reportsAddressList.add("$victimsName N = $nVictims")
        reportsAddressList.add("Тип происшествия")
        //создаем отчеты по списку
        reportsAddressList.forEach { address ->
//            element(byCssSelector("form[novalidate] input#title")).let { el ->
            element(byCssSelector("form[novalidate] input#title"))
                .sendKeys("Reports 0030 Проверка формирования отчетов по происшествиям $dateTime отсчет $address")
            when (address){
                "адресный" -> {
                    addressInput("address", "Карачаево-Черкесская Респ, г Карачаевск", waitTime)
                }
                "$victimsName N = $nVictims" -> {
                    element(byXpath("//form[@novalidate]//label[text()='$victimsName']/..//input"))
                        .click()
                    element(byXpath("//form[@novalidate]//label[text()='$victimsName']/..//input"))
                        .sendKeys(nVictims.toString())
                }
                "Тип происшествия" -> {
                    clearInput("//form[@novalidate]//label[text()='$victimsName']/..//input", waitTime)
                    element(byXpath("//label[text()='Тип происшествия']/..//button[@title='Раскрыть']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='П Повседневные']/ancestor::li/div[1]"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(150)
                    var sbstr = elements(byXpath("//div[@role='presentation']/div/ul//text()[starts-with(.,'П.')]/.."))
                        .random()
                        .ownText
                        .substringBefore(' ')
                    element(byXpath("//div[@role='presentation']/div/ul//*[starts-with(text(),'$sbstr')]/ancestor::li/div[1]"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(150)
                    sbstr = elements(byXpath("//div[@role='presentation']/div/ul//text()[starts-with(.,'$sbstr.')]/.."))
                        .random()
                        .ownText
                        .substringBefore(' ')
                    element(byXpath("//div[@role='presentation']/div/ul//*[starts-with(text(),'$sbstr')]/ancestor::li/div[1]"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(150)
                    sbstr = elements(byXpath("//div[@role='presentation']/div/ul//text()[starts-with(.,'$sbstr.')]/.."))
                        .random()
                        .ownText
                        .substringBefore(' ')
                    element(byXpath("//div[@role='presentation']/div/ul//*[starts-with(text(),'$sbstr')]/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Тип происшествия']/..//div[@role='button']"))
                        .should(exist, ofSeconds(waitTime))
                    elements(byXpath("//label[text()='Тип происшествия']/..//div[@role='button']"))
                        .should(CollectionCondition.size(1), ofSeconds(waitTime))
                    icReportCreate = element(byXpath("//label[text()='Тип происшествия']/..//div[@role='button']//text()/..")).ownText
                    element(byXpath("//label[text()='Тип происшествия']/..//button[@title='Close']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
            }
                //создаем отчет
                element(byXpath("//span[text()='Создать']/ancestor::button")).click()
                //ждем созданный отчет
                element(byText("Reports 0030 Проверка формирования отчетов по происшествиям $dateTime отсчет $address"))
                    .should(exist, ofSeconds(waitTime))
                //очищаем поле названия если нам еще делать отчеты
                if (address != reportsAddressList.last()) {
                    clearInput("//form[@novalidate]//input[@id='title']", waitTime)
                }
//            }
        }
        //скрываем форму создания отчета
        element(byXpath("//form[@novalidate]//*[text()='Очистить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Скрыть форму']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //открываем созданные отчеты и запоминаем значения
        reportsAddressList.forEach {address ->
            element(byXpath("//table[contains(@id,'-reports')]/tbody//*[text()='Reports 0030 Проверка формирования отчетов по происшествиям $dateTime отсчет $address']/ancestor::tr"))
                .should(exist, ofSeconds(waitTime))
                .click()
            //дожидаемся заголовка
//            element(byXpath("//*[text()='Reports 0030 Проверка формирования отчетов по происшествиям $dateTime отсчет $address']/parent::div//*[text()=' за период с ${today[2]}.${today[1]}.${(today[0])[2]}${(today[0])[3]} по ${today[2]}.${today[1]}.${(today[0])[2]}${(today[0])[3]}']"))
//                .should(exist, ofSeconds(waitTime))
            element(byXpath("//h4[text()='Отчет']/following-sibling::h4[text()='Reports 0030 Проверка формирования отчетов по происшествиям $dateTime отсчет $address']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //для каждой таблицы
            for (table in 1..elements(byXpath("//table")).size){
                if (elements(byXpath("(//table)[$table]/tbody/tr[1]//*[text()='Нет данных']")).size == 0) {
                    //для каждой строки
                    for (row in 1..elements(byXpath("(//table)[$table]/tbody/tr")).size) {
                        //для каждого столбца, начиная со второго
                        for (column in 2..elements(byXpath("(//table)[$table]/tbody/tr[$row]/td")).size) {
                            if(elements(byXpath("(//table)[$table]/tbody/tr[$row]/td[$column]//text()/..")).size == 1){
                                //кладем значение в список
                                tableRowList.add(element(byXpath("(//table)[$table]/tbody/tr[$row]/td[$column]//text()/..")).ownText.toInt())
                            } else tableRowList.add(0)
                        }
                        //список кладем в карту с ключем - значение строки в первом столбце
                        oneReportMap.put(element(byXpath("(//table)[$table]/tbody/tr[$row]/td[1]//text()/..")).ownText,
                            tableRowList.toMutableList())
                        tableRowList.clear()
                    }
                }
            }
            //готовый отчет кладем в карту с ключем - внутритестовое название отчета
            oldValuesMap.put(address, oneReportMap.clone() as MutableMap<String, MutableList<Int>>)
            oneReportMap.clear()
            //сверим диаграмму с легендой, не запоминая отдельно в этом блоке ни то, ни другое
            for (i in 2..elements(byXpath("//table[@id='legend']/tbody/tr")).size){
                if (element(byXpath("//table[@id='legend']/tbody/tr[$i]/td[2]//text()/..")).ownText != "0"){
                    Assertions.assertTrue(
                        element(byXpath("//table[@id='legend']/tbody/tr[$i]/td[2]//text()/.."))
                            .ownText
                            ==
                            element(byXpath("//*[name()='svg']/*[name()='g' and contains(@class,'recharts-pie')]/*[name()='g' and contains(@class,'recharts-pie-labels')]/*[name()='g'][${i-1}]//text()/.."))
                                .ownText
                    )
                }
            }
            back()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Создаем КП
        val rndIC = (2..5).random()
        for (i in 1..5){
            menuNavigation("Происшествия", "Создать карточку", waitTime)
//            menuNavigation("Происшествия", "Список происшествий", waitTime)
//            element(byCssSelector("table#incidents"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//            element(byXpath("//*[text()='Создать обращение']/ancestor::button"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
            //источник - случайный
            createICToolCalltype("random", waitTime)
            Thread.sleep(100)
            //телефон - случайный
            createICToolPhone("", waitTime)
            //Фио
            createICToolFIO("Reports 0030", "i = $i", "rndIC = $rndIC", waitTime)
            //номер дома
            val random = (1..100).random()
            if (i != rndIC) {
                addressInput("callAddress", "Карачаево-Черкесская Респ, г Карачаевск $random", waitTime)
            } else {
                addressInput("callAddress", "г Черкесск $random", waitTime)
            }
            //доп. инфо.
            createICToolsDopInfo("i = $i, rndIC = $rndIC Reports 0030 Проверка формирования отчетов по происшествиям $dateTime", waitTime)
            //сохраняем обращение
            element(byXpath("//*[text()='Создать карточку']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //тип происшествия - случайный
            if (i == 1){
                element(byXpath("//label[text()='Типы происшествий']/ancestor::div[@data-testid='incidentTypeId']//input"))
                    .click()
                element(byXpath("//label[text()='Типы происшествий']/ancestor::div[@data-testid='incidentTypeId']//input"))
                    .sendKeys(icReportCreate, Keys.DOWN, Keys.ENTER)
            } else {
                inputRandomNew("incidentTypeId-textfield", false, waitTime)
            }
            Thread.sleep(200)
            //запоминаем тип происшествия
            val icType = element(byXpath("//input[@name='incidentTypeId-textfield']")).getAttribute("value").toString()
            //Угроза людям - случано
            val isThreatPeople = Random.nextBoolean()
            val victimsCount = (0..100).random()
            val victimsChildren = (0..victimsCount).random()
            val deathToll = (0..50).random()
            val deathChildren = (0..deathToll).random()
            var deathList: MutableList<Int>
            //создадим список для назначения ДДС
            val ddsTypeList:MutableList<String> = mutableListOf()
            val ddsNameList:MutableList<String> = mutableListOf()
            for (i in 1..3){
                if (Random.nextBoolean()){
                    ddsTypeList.add("0$i")
                }
            }
            if (isThreatPeople){
                deathList =
//                    mutableListOf(
//                        1 + ddsTypeList.size,
//                        (victimsCount * (1 + ddsTypeList.size)),
//                        (victimsChildren * (1 + ddsTypeList.size)),
//                        (deathToll * (1 + ddsTypeList.size)),
//                        (deathChildren * (1 + ddsTypeList.size))
//                    )
//                    скрыто по причине того, что в отчет перестали считать дочерние карточки, но мало ли как еще повернется
                    mutableListOf(1, victimsCount, victimsChildren, deathToll, deathChildren)
                createICToolIsThreatPeople(isThreatPeople, victimsCount.toString(), victimsChildren.toString(), deathToll.toString(), deathChildren.toString(), waitTime)
            } else {
                deathList = mutableListOf(1, 0, 0, 0, 0)
            }
            element(byXpath("//*[text()='Сохранить карточку']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            checkICToolIsStatus("В обработке", waitTime)
            //выполним назначение ДДС
            if (ddsTypeList.isNotEmpty()){
                element(byXpath(" //header//*[text()='Работа с ДДС']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                Thread.sleep(100)
                element(byXpath(" //div[@id='simple-tabpanel-hotlines']//*[text()='Выбрать ДДС']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                Thread.sleep(100)
                element(byXpath("//*[text()='ДДС ЭОС']/ancestor::div[@id='panel1a-header']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                Thread.sleep(250)
                ddsTypeList.forEach { ddsType ->
                    ddsNameList.add(
                        elements(byXpath("//*[text()='ДДС ЭОС']/ancestor::div[@id='panel1a-header']/following-sibling::div//*[contains(text(),'$ddsType')]"))
                            .random().ownText
                    )
                }
                ddsNameList.forEach { ddsName ->
                    element(byXpath("//*[text()='$ddsName']/ancestor::div[2]//input"))
                        .should(exist, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(250)
                }
                element(byXpath("//span[text()='Назначить']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                Thread.sleep(250)
                ddsNameList.forEach { ddsName ->
                    element(byXpath("//form//*[text()='$ddsName']"))
                        .should(exist, ofSeconds(longWait))
                }
            }
            Thread.sleep(250)
            var  updateICStatus = (1..5).random()
            val statusList: MutableList<String> = mutableListOf()
            if (ddsTypeList.isNotEmpty()){
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //этот блок оставим до момента определения с тем как должны сортироваться дочерние КП в их списке
                //и починки соответствующего бага
//                for (i in 1..ddsTypeList.size){
//                    //(//form//button[contains(@style,'border-radius')])[2]
//                    var ddsICStatus = ""
//                    updateICStatus = (1..5).random()
//                    if (updateICStatus != 1) {
//                        element(byXpath("(//form)[$i]//button[@style]"))
//                            .should(exist, ofSeconds(waitTime))
//                            .click()
//                        element(byXpath("//div[@role='presentation']"))
//                            .should(exist, ofSeconds(waitTime))
//                        ddsICStatus = elements(byXpath("//div[@role='presentation']//*[not(text()='Новая')]/ancestor::button[not (@disabled)]//text()/.."))
//                            .random()
//                            .ownText
//                        element(byXpath("//div[@role='presentation']//*[text()='$ddsICStatus']/ancestor::button[not (@disabled)]"))
//                            .should(exist, ofSeconds(waitTime))
//                            .click()
//                        element(byXpath("//div[@role='presentation']"))
//                            .shouldNot(exist, ofSeconds(waitTime))
//                        element(byXpath("(//form)[$i]//button[@style]//*[text()='$ddsICStatus']"))
//                            .should(exist, ofSeconds(waitTime))
//                        if (ddsICStatus.last() == 'а'){
//                            ddsICStatus = ddsICStatus.replaceFirst(".$".toRegex(), "ы")
//                        }
//                    } else {
//                        ddsICStatus = element(byXpath("(//form)[$i]//button[@style]//text()/..")).ownText
//                    }
//                    statusList.add(ddsICStatus)
//                    Thread.sleep(100)
//                }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                //альтернатива блоку выше, ориентируется не по порядковому номеру, а по именам назначенных служб
                ddsNameList.forEach { ddsName ->
                    var ddsICStatus = ""
                    updateICStatus = (1..6).random()
                    if (updateICStatus== 1) {
                        ddsICStatus = "Новые"
                    }
                    if (updateICStatus > 1) {
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='Новая']/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//div[@role='presentation']"))
                            .should(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                        element(byXpath("//div[@role='presentation']//*[text()='В обработке']/ancestor::button[not (@disabled)]"))
                            .should(exist, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//div[@role='presentation']"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='Новая']/ancestor::button"))
                            .shouldNot(exist, ofSeconds(longWait))
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//button[@style]//*[text()='В обработке']"))
                            .should(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                    }
                    if (updateICStatus == 3) {
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='В обработке']/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//div[@role='presentation']"))
                            .should(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                        element(byXpath("//div[@role='presentation']//*[text()='Реагирование']/ancestor::button[not (@disabled)]"))
                            .should(exist, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//div[@role='presentation']"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='В обработке']/ancestor::button"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//button[@style]//*[text()='Реагирование']"))
                            .should(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                    }
                    if (updateICStatus in 4..6) {
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='В обработке']/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//div[@role='presentation']"))
                            .should(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                        ddsICStatus = elements(byXpath("//div[@role='presentation']//*[not(text()='Реагирование')]/ancestor::button[not (@disabled)]//text()/.."))
                            .random()
                            .ownText
                        element(byXpath("//div[@role='presentation']//*[text()='$ddsICStatus']/ancestor::button[not (@disabled)]"))
                            .should(exist, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//div[@role='presentation']"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='В обработке']/ancestor::button"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//button[@style]//*[text()='$ddsICStatus']"))
                            .should(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                    }
                    if (updateICStatus == 6) {
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='$ddsICStatus']/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//div[@role='presentation']"))
                            .should(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                        element(byXpath("//div[@role='presentation']//*[text()='Закрыта']/ancestor::button[not (@disabled)]"))
                            .should(exist, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//div[@role='presentation']"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        Thread.sleep(100)
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='$ddsICStatus']/ancestor::button"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        element(byXpath("//*[text()='$ddsName']/ancestor::form//button[@style]//*[text()='Закрыта']"))
                            .should(exist, ofSeconds(waitTime))
                    }
                    Thread.sleep(300)
                    ddsICStatus = element(byXpath("(//*[text()='$ddsName']/ancestor::form//*[text()]/ancestor::button)[1]//text()/.."))
                        .ownText
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                    if (updateICStatus != 1) {
//                        element(byXpath("//*[text()='$ddsName']/ancestor::form//*[text()='Новая']/ancestor::button"))
//                            .should(exist, ofSeconds(waitTime))
//                            .click()
//                        element(byXpath("//div[@role='presentation']"))
//                            .should(exist, ofSeconds(waitTime))
//                        ddsICStatus = elements(byXpath("//div[@role='presentation']//*[not(text()='Новая')]/ancestor::button[not (@disabled)]//text()/.."))
//                            .random()
//                            .ownText
//                        element(byXpath("//div[@role='presentation']//*[text()='$ddsICStatus']/ancestor::button[not (@disabled)]"))
//                            .should(exist, ofSeconds(waitTime))
//                            .click()
//                        element(byXpath("//div[@role='presentation']"))
//                            .shouldNot(exist, ofSeconds(waitTime))
//                        element(byXpath("//*[text()='$ddsName']/ancestor::form//button[@style]//*[text()='$ddsICStatus']"))
//                            .should(exist, ofSeconds(waitTime))
//                        if (ddsICStatus.last() == 'а'){
//                            ddsICStatus = ddsICStatus.replaceFirst(".$".toRegex(), "ы")
//                        }
//                    } else {
//                        ddsICStatus = "Новые"
//                    }
//                    statusList.add(ddsICStatus)
//                    скрыто по причине того, что в отчет перестали считать дочерние карточки, но мало ли как еще повернется
//                    + новая логика выбора произвольного статуса дочерней КП
                    Thread.sleep(100)
                }
            } else {
                updateICStatus = (1..6).random()
                if (updateICStatus != 1) {
                    updateICToolStatus("", waitTime)
                }
            }
            Thread.sleep(500)
            var iCStatus = element(byXpath("//div[@id='incidentButtons']//button[1]//text()/..")).ownText
            if (iCStatus.last() == 'а'){
                iCStatus = iCStatus.replaceFirst(".$".toRegex(), "ы")
            }
            statusList.add(iCStatus)
            //Наполняем список отчетов в которые попадает созданная КП
            icReportsList.add("без адреса")
            if (i != rndIC) {
                icReportsList.add("адресный")
                if (isThreatPeople) {
                    when (lookingForVictims) {
                        1 -> {
                            if (victimsCount > nVictims) {
                                icReportsList.add("$victimsName N = $nVictims")
                            }
                        }
                        2 -> {
                            if (victimsChildren > nVictims) {
                                icReportsList.add("$victimsName N = $nVictims")
                            }
                        }
                        3 -> {
                            if (deathToll > nVictims) {
                                icReportsList.add("$victimsName N = $nVictims")
                            }
                        }
                        4 -> {
                            if (deathChildren > nVictims) {
                                icReportsList.add("$victimsName N = $nVictims")
                            }
                        }
                    }
                }
                if (icType == icReportCreate) {
                    icReportsList.add("Тип происшествия")
                }
            }
            //наполняем карты изменениями
            icReportsList.forEach { report ->
                    //сначала дополним значение "Всего"
                    oldValuesMap.get(report)?.put(
                        "Всего",
                        listOf(oldValuesMap.get(report)?.get("Всего")?.get(0)!! + 1) as MutableList<Int>
                    )
                    //дополним значение в легенде, согласно имеющемся статусам
                    statusList.forEach { status ->
                        oldValuesMap.get(report)?.put(status,
                            listOf(oldValuesMap.get(report)?.get(status)?.get(0)!! + 1) as MutableList<Int>
                        )
                    }
                    //дополним значение по типу происшествия созданной КП, с учетом созданных пострадавших
                    if (oldValuesMap.get(report)?.keys?.contains(icType) == true){
                        val newList:MutableList<Int> = mutableListOf()
                        oldValuesMap.get(report)?.get(icType)?.forEachIndexed { index, value ->
                            newList.add(value + deathList[index])
                        }
                        oldValuesMap.get(report)?.put(icType, newList.toMutableList())
                        newList.clear()
                    } else {
                        oldValuesMap.get(report)?.put(icType, deathList.toMutableList())
                    }
                    //дополним третью табличку, по типам назначения ДДС
                    if (ddsTypeList.isNotEmpty()){
                        ddsTypeList.forEach { ddsType ->
                            if (oldValuesMap.get(report)?.keys?.contains(ddsType) == true){
                                oldValuesMap.get(report)?.put(
                                    ddsType,
                                    listOf(oldValuesMap.get(report)?.get(ddsType)?.get(0)!! + 1) as MutableList<Int>
                                )
                            } else {
                                oldValuesMap.get(report)?.put(
                                    ddsType,
                                    listOf(1) as MutableList<Int>)
                            }
                        }
                    } else {
                        //дополним значение "Без назначения служб"
                        if (oldValuesMap.get(report)?.keys?.contains("Без назначения служб") == true){
                            oldValuesMap.get(report)?.put(
                                "Без назначения служб",
                                listOf(oldValuesMap.get(report)?.get("Без назначения служб")?.get(0)!! + 1) as MutableList<Int>)
                        } else {
                            oldValuesMap.get(report)?.put(
                                "Без назначения служб",
                                listOf(1) as MutableList<Int>)
                        }
                    }
            }
            icReportsList.clear()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("form[novalidate]"))
            .should(exist, ofSeconds(waitTime))
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегодняшним числом
        element(byCssSelector("form[novalidate] input#periodStart"))
            .sendKeys("${today[2]}.${today[1]}.${today[0]}")
        element(byCssSelector("form[novalidate] input#periodEnd"))
            .sendKeys("${today[2]}.${today[1]}.${today[0]}")
        //создаем новые отчеты
        reportsAddressList.forEach { address ->
//            element(byCssSelector("form[novalidate] input#title")).let { el ->
            element(byCssSelector("form[novalidate] input#title"))
                .sendKeys("Reports 0030 Проверка формирования отчетов по происшествиям $dateTime сверка $address")
            when (address){
                "адресный" -> {
                    addressInput("address", "Карачаево-Черкесская Респ, г Карачаевск", waitTime)
                }
                "$victimsName N = $nVictims" -> {
                    element(byXpath("//form[@novalidate]//label[text()='$victimsName']/..//input"))
                        .click()
                    element(byXpath("//form[@novalidate]//label[text()='$victimsName']/..//input"))
                        .sendKeys(nVictims.toString())
                }
                "Тип происшествия" -> {
                    clearInput("//form[@novalidate]//label[text()='$victimsName']/..//input", waitTime)
                    element(byXpath("//label[text()='Тип происшествия']/..//input"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Тип происшествия']/..//input"))
                        .sendKeys(icReportCreate, Keys.DOWN, Keys.ENTER)
                }
            }
            //создаем отчет
            element(byXpath("//span[text()='Создать']/ancestor::button")).click()
            //ждем созданный отчет
            element(byText("Reports 0030 Проверка формирования отчетов по происшествиям $dateTime сверка $address"))
                .should(exist, ofSeconds(waitTime))
            //очищаем поле названия если нам еще делать отчеты
            if (address != reportsAddressList.last()) {
                clearInput("//form[@novalidate]//input[@id='title']", waitTime)
            }
        }
        //скрываем форму создания отчета
        element(byXpath("//form[@novalidate]//*[text()='Очистить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Скрыть форму']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //открываем созданные отчеты и запоминаем значения
        reportsAddressList.forEach {address ->
            element(byXpath("//table[contains(@id,'-reports')]/tbody//*[text()='Reports 0030 Проверка формирования отчетов по происшествиям $dateTime сверка $address']/ancestor::tr"))
                .should(exist, ofSeconds(waitTime))
                .click()
            //дожидаемся заголовка
//            element(byXpath("//*[text()='Reports 0030 Проверка формирования отчетов по происшествиям $dateTime сверка $address']/parent::div//*[text()=' за период с ${today[2]}.${today[1]}.${(today[0])[2]}${(today[0])[3]} по ${today[2]}.${today[1]}.${(today[0])[2]}${(today[0])[3]}']"))
//                .should(exist, ofSeconds(waitTime))
            element(byXpath("//h4[text()='Отчет']/following-sibling::h4[text()='Reports 0030 Проверка формирования отчетов по происшествиям $dateTime сверка $address']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //для каждой таблицы
            for (table in 1..elements(byXpath("//table")).size){
                if (elements(byXpath("(//table)[$table]/tbody/tr[1]//*[text()='Нет данных']")).size == 0) {
                    //для каждой строки
                    for (row in 1..elements(byXpath("(//table)[$table]/tbody/tr")).size) {
                        //для каждого столбца, начиная со второго
                        for (column in 2..elements(byXpath("(//table)[$table]/tbody/tr[$row]/td")).size) {
                            if(elements(byXpath("(//table)[$table]/tbody/tr[$row]/td[$column]//text()/..")).size == 1){
                                //кладем значение в список
                                tableRowList.add(element(byXpath("(//table)[$table]/tbody/tr[$row]/td[$column]//text()/..")).ownText.toInt())
                            } else tableRowList.add(0)
                        }
                        //список кладем в карту с ключем - значение строки в первом столбце
                        oneReportMap.put(element(byXpath("(//table)[$table]/tbody/tr[$row]/td[1]//text()/..")).ownText,
                            tableRowList.toMutableList())
                        tableRowList.clear()
                    }
                }
            }
            //готовый отчет кладем в карту с ключем - внутритестовое название отчета
            newValuesMap.put(address, oneReportMap.clone() as MutableMap<String, MutableList<Int>>)
            oneReportMap.clear()
            //сверим диаграмму с легендой, не запоминая отдельно ни то, ни другое
            for (i in 2..elements(byXpath("//table[@id='legend']/tbody/tr")).size){
                if (element(byXpath("//table[@id='legend']/tbody/tr[$i]/td[2]//text()/..")).ownText != "0"){
                    Assertions.assertTrue(
                        element(byXpath("//table[@id='legend']/tbody/tr[$i]/td[2]//text()/.."))
                            .ownText
                            ==
                            element(byXpath("//*[name()='svg']/*[name()='g' and contains(@class,'recharts-pie')]/*[name()='g' and contains(@class,'recharts-pie-labels')]/*[name()='g'][${i-1}]//text()/.."))
                                .ownText
                    )
                }
            }
            back()
        }
        reportsAddressList.forEach { address ->
            println("keys: $address newValuesMap[address]?.keys!!.sorted() = ${newValuesMap[address]?.keys!!.sorted()} vs oldValuesMap[address]?.keys!!.sorted() = ${oldValuesMap[address]?.keys!!.sorted()}")
            Assertions.assertTrue(newValuesMap[address]?.keys!!.sorted() == oldValuesMap[address]?.keys!!.sorted())
            newValuesMap[address]?.keys?.forEach { key ->
                println("address = $address key = $key oldValues = ${oldValuesMap[address]?.get(key)} newValuesMap = ${newValuesMap[address]?.get(key)}")
                //тут пока вылазит баг про не копирование угроз и пострадавших
                Assertions.assertTrue(oldValuesMap[address]?.get(key) == newValuesMap[address]?.get(key))
//                if (oldValuesMap[address]?.get(key) != newValuesMap[address]?.get(key)){
//                    println("address = $address, key = $key, oldValuesMap = ${oldValuesMap[address]?.get(key)}, newValuesMap = ${newValuesMap[address]?.get(key)}")
//                }
            }
        }
//        Assertions.assertTrue(oldValuesMap == newValuesMap)
    }




    @DataProvider(name = "Расширенная проверка формирования отчетов")
    open fun Statuses(): Any {
        return arrayOf<Array<Any>>(
//            arrayOf("По происшествиям", "2 МО, Зеленчукский район КЧР, ГО Черкесский, ЕДДС, Оператор, Уровень происшествия", 2),
            arrayOf("По обращениям", "2 МО, Зеленчукский район КЧР, ГО Черкесский, ЕДДС, Оператор", 2),
            arrayOf("По сотрудникам", "2 МО, Зеленчукский район КЧР, ГО Черкесский, ЕДДС", 3)
        )
    }

    @Test(retryAnalyzer = Retry::class, dataProvider = "Расширенная проверка формирования отчетов", groups = ["ПМИ", "ALL"])
    fun `Reports 0070 Расширенная проверка формирования отчетов`(reportType: String, reportsString: String, valueColumnNumber: Int ) {
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        val reportList = reportsString.split(", ")
        val oldReportsMap : LinkedHashMap<String, MutableMap<String, Int>> = linkedMapOf()
        val oneReportMap : LinkedHashMap<String, Int> = linkedMapOf()
        val newReportsMap : LinkedHashMap<String, MutableMap<String, Int>> = linkedMapOf()
        val today = date.toString().split("-")
        logonTool()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Переходим в "отчет По..."
        menuNavigation("Отчеты",reportType, waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${today[2]}.${today[1]}.${today[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${today[2]}.${today[1]}.${today[0]}")
        reportList.forEach{report ->
            //Заполняем поля отчета - название
            element(byCssSelector("input#title"))
                .sendKeys("Reports 0070 Проверка формирования отчетов $reportType $dateTime отсчет $report")
            //задаем МО
            when(report){
                "2 МО" -> {
                    while (elements(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).size >0){
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).hover()
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).click()
                    }
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div//button[@title='Раскрыть']"))
                        .click()
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='${reportList[1]}']/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']/*[text()='${reportList[1]}']"))
                        .should(exist, ofSeconds(waitTime))
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='${reportList[2]}']/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']/*[text()='${reportList[2]}']"))
                        .should(exist, ofSeconds(waitTime))
                }
                "Зеленчукский район КЧР" -> {
                    while (elements(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).size >0){
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).hover()
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).click()
                    }
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div"))
                        .click()
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='$report']/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']/*[text()='$report']"))
                        .should(exist, ofSeconds(waitTime))
                }
                "ГО Черкесский" -> {
                    while (elements(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).size >0){
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']"))
                            .should(exist, ofSeconds(waitTime))
                            .hover()
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']"))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                    }
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='$report']/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']/*[text()='$report']"))
                        .should(exist, ofSeconds(waitTime))
                }
                "ЕДДС" -> {
                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div/input"))
                        .sendKeys("AutoTest EDDS 2")
                    element(byXpath("//div[@role='presentation']//*[text()='AutoTest EDDS 2']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
                "Оператор" -> {
                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div//button[@aria-label='Clear']"))
                        .should(exist, ofSeconds(waitTime))
                        .hover()
                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div//button[@aria-label='Clear']"))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Оператор']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Оператор']/following-sibling::div/input"))
                        .sendKeys("Сизов AutoTest EDDS 2-2")
                    element(byXpath("//div[@role='presentation']//*[text()='Сизов AutoTest EDDS 2-2']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
                "Уровень происшествия" -> {
                    element(byXpath("//label[text()='Оператор']/following-sibling::div//button[@aria-label='Clear']"))
                        .should(exist, ofSeconds(waitTime))
                        .hover()
                    element(byXpath("//label[text()='Оператор']/following-sibling::div//button[@aria-label='Clear']"))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Уровень происшествия']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Уровень происшествия']/following-sibling::div/input"))
                        .sendKeys("ЧС")
                    element(byXpath("//div[@role='presentation']//*[text()='ЧС']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//span[text()='ЧС']/parent::div[@role='button']"))
                        .should(exist, ofSeconds(waitTime))
                }
            }
            //создаем отчет
            element(byXpath("//span[text()='Создать']/..")).click()
            //ждем созданный отчет
            element(byText("Reports 0070 Проверка формирования отчетов $reportType $dateTime отсчет $report"))
                .should(exist, ofSeconds(waitTime))
            if (report != reportList.last()){
                clearInput("//input[@id='title']", waitTime)
            }
        }
        element(byXpath("//*[text()='Очистить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Скрыть форму']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        reportList.forEach{ report ->
            //переходим в созданный отчет
            element(byXpath("//tbody/tr//*[text()='Reports 0070 Проверка формирования отчетов $reportType $dateTime отсчет $report']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            elements(byXpath("//main//table"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1), ofSeconds(waitTime))
            element(byXpath("//*[text()='Печать']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            for (i in 1..elements(byXpath("//main//table/tbody/tr")).size){
                if (elements(byXpath("(//main//table/tbody/tr)[$i]//*[text()='Нет данных']")).size == 0
                    && elements(byXpath("(//main//table/tbody/tr)[$i]//*[text()='из них:']")).size == 0){
                    oneReportMap.put(
                        element(byXpath("(//main//table/tbody/tr)[$i]/td[1]")).ownText,
                        element(byXpath("(//main//table/tbody/tr)[$i]/td[$valueColumnNumber]")).ownText.toInt()
                    )
                }
            }
            oldReportsMap.put(report, oneReportMap.clone() as MutableMap<String, Int>)
            oneReportMap.clear()
            back()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Поехали создавать карточки по происшествиям
        var login:String = ""
        var password:String = ""
        var reportsMapKeysList: List<String> = listOf()
        val validatedValues: MutableList<String> = mutableListOf<String>()
//        reportList = listOf("Зеленчукский район КЧР", "ГО Черкесский", "Оператор", "ЕДДС", "Уровень происшествия")
        for (i in 1 until reportList.size) {
            when (i) {
                1 -> {login = "a.sizov.edds.1.1"; password = "a.sizov.edds.1.1"; reportsMapKeysList = listOf("2 МО", "ГО Черкесский") }
                2 -> {login = "test-edds-zel"; password = "test-edds-zel"; reportsMapKeysList = listOf("2 МО", "Зеленчукский район КЧР") }
                3 -> {login = "a.sizov.edds.2.1"; password = "a.sizov.edds.2.1"; reportsMapKeysList = listOf("2 МО", "ГО Черкесский", "ЕДДС") }
                4 -> {login = "a.sizov.edds.2.2"; password = "a.sizov.edds.2.2"; reportsMapKeysList = listOf("2 МО", "ГО Черкесский", "ЕДДС", "Оператор") }
                5 -> {login = "a.sizov.edds.2.2"; password = "a.sizov.edds.2.2"; reportsMapKeysList = listOf("2 МО", "ГО Черкесский", "ЕДДС", "Оператор", "Уровень происшествия") }
            }
            anyLogonTool(login, password)
            //убедимся что мы за оператор:
            //кликаем по иконке оператора сверху справа
            element(byXpath("//header//button//*[text()]/ancestor::button")).click()
            //пероеходим в профиль пользователя
            element(byCssSelector("a[href='/profile']>button"))
                .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            element(byCssSelector("a[href='/profile']>button")).click()
            //Извлекаем имя оператора
            element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p"))
                .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            val operator = element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p")).ownText.trim()
//            back()
            menuNavigation("Происшествия", "Создать карточку", waitTime)
            //Источник события - выбираем случайно
            createICToolCalltype("", waitTime)
            //Номер телефона
            createICToolPhone("", waitTime)
            //ФИО
            createICToolFIO(date.toString() + "_AutoTest", "Reports 0070", "", waitTime)
            //Вводим случайный адрес
            element(byCssSelector("#callAddress")).sendKeys("$i Reports 0050 $reportsMapKeysList", Keys.ENTER)
            //заполняем дополнительную информацию
            createICToolsDopInfo("Reports 0070, i=$i $dateTime $reportsMapKeysList", waitTime)
            //создаем КП
            element(byXpath("//*[text()='Создать карточку']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //Случайно выбираем и запоминаем тип происшествия
            if (i == 5){
                element(byXpath("//label[text()='Уровень происшествия']/following-sibling::div"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']//li[text()='ЧС']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//label[text()='Уровень происшествия']/following-sibling::div//*[text()='ЧС']"))
                    .should(exist, ofSeconds(waitTime))
                Thread.sleep(500)
            }
            inputRandomNew("incidentTypeId-textfield", false, waitTime)
            element(byCssSelector("input#incidentTypeId-autocomplete[value*='.']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            val longSelectedIncidentType = element(byCssSelector("input#incidentTypeId-autocomplete")).getAttribute("value").toString()
            val shortSelectedIncidentType = longSelectedIncidentType.substring(longSelectedIncidentType.indexOf(' ') + 1)
            val createdCallType = element(byXpath("//label[text()='Источник события']/following-sibling::div/div")).ownText
            if (elements(byXpath("//*[text()='Место происшествия']/../..//label[text()='Уточните адрес происшествия']/following-sibling::div[contains(@class,'error')]"))
                    .size > 0){
                element(byXpath("(//*[text()='Место происшествия']/../..//span[@aria-label='add'])[1]/button"))
                    .click()
            }
            pushButtonCreateIC("Reports 0070, i=$i $dateTime $reportsMapKeysList", waitTime)
//            element(byXpath("//*[text()='Сохранить карточку']/ancestor::button"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//                .click()
            //Убеждаемся, что нам загрузило созданную карточку
            //проверяя что нам в принципе загрузило какую-то карточку
            element(byCssSelector("#simple-tabpanel-card"))
                .should(exist, ofSeconds(waitTime))
            //что она в нужном статусе
            checkICToolIsStatus("В обработке", waitTime)
            checkICToolDopInfo("Reports 0070, i=$i $dateTime $reportsMapKeysList", waitTime)
            //дополняем значения карт
            var validatedValue = ""//некоторое значение которое мы проверяем в отчете, в зависимости от отчета, это либо тип происшествия, либо источник обращения, либо оператор
            when(reportType){
                "По происшествиям" -> {validatedValue = shortSelectedIncidentType}
                "По обращениям" -> {validatedValue = createdCallType}
                "По сотрудникам" -> {validatedValue = operator}
            }
            reportsMapKeysList.forEach{reportKey ->
//                oldReportsMap.get(reportKey)
                if (oldReportsMap.get(reportKey)!!.contains(validatedValue)){
                    var createdValidatedValue = oldReportsMap.get(reportKey)?.get(validatedValue)?.toInt()
                    createdValidatedValue = createdValidatedValue?.plus(1)
                    oldReportsMap.get(reportKey)?.put(validatedValue, createdValidatedValue!!)
                } else {
                    oldReportsMap.get(reportKey)?.put(validatedValue, 1)
                }
                if (oldReportsMap.get(reportKey)!!.contains("Всего")){
                    var valueAllIncident = oldReportsMap.get(reportKey)?.get("Всего")?.toInt()
                    valueAllIncident = valueAllIncident?.plus(1)
                    oldReportsMap.get(reportKey)?.put("Всего", valueAllIncident!!)
                }
                if (oldReportsMap.get(reportKey)!!.contains("В обработке")) {
                    var valueInProcessingCall = oldReportsMap.get(reportKey)?.get("В обработке")?.toInt()
                    valueInProcessingCall = valueInProcessingCall?.plus(1)
                    oldReportsMap.get(reportKey)?.put("В обработке", valueInProcessingCall!!)
                }
            }
            validatedValues.add(validatedValue)

            logoffTool()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        logonTool()
        menuNavigation("Отчеты",reportType, waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${today[2]}.${today[1]}.${today[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${today[2]}.${today[1]}.${today[0]}")
        reportList.forEach{report ->
            //Заполняем поля отчета - название
            element(byCssSelector("input#title"))
                .sendKeys("Reports 0070 Проверка формирования отчетов $reportType $dateTime сверка $report")
            //задаем МО
            when(report){
                "2 МО" -> {
                    while (elements(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).size >0){
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).hover()
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).click()
                    }
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div//button[@title='Раскрыть']"))
                        .click()
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='${reportList[1]}']/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']/*[text()='${reportList[1]}']"))
                        .should(exist, ofSeconds(waitTime))
//                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div//button[@title='Open']"))
//                        .click()
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='${reportList[2]}']/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']/*[text()='${reportList[2]}']"))
                        .should(exist, ofSeconds(waitTime))
                }
                "Зеленчукский район КЧР" -> {
                    while (elements(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).size >0){
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).hover()
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).click()
                    }
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div"))
                        .click()
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='$report']/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']/*[text()='$report']"))
                        .should(exist, ofSeconds(waitTime))
                }
                "ГО Черкесский" -> {
                    while (elements(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']")).size >0){
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']"))
                            .should(exist, ofSeconds(waitTime))
                            .hover()
                        element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']//*[name()='svg']"))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                    }
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//div[@role='presentation']/div/ul//*[text()='$report']/ancestor::li"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Муниципальное образование']/following-sibling::div/div[@role='button']/*[text()='$report']"))
                        .should(exist, ofSeconds(waitTime))
                }
                "ЕДДС" -> {
                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div/input"))
                        .sendKeys("AutoTest EDDS 2")
                    element(byXpath("//div[@role='presentation']//*[text()='AutoTest EDDS 2']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
                "Оператор" -> {
                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div//button[@aria-label='Clear']"))
                        .should(exist, ofSeconds(waitTime))
                        .hover()
                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div//button[@aria-label='Clear']"))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
//                    element(byXpath("//label[text()='Служба ЕДДС']/following-sibling::div/div[@role='button']/*[text()]"))
//                        .shouldNot(exist, ofSeconds(waitTime))
                    element(byXpath("//label[text()='Оператор']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Оператор']/following-sibling::div/input"))
                        .sendKeys("Сизов AutoTest EDDS 2-2")
                    element(byXpath("//div[@role='presentation']//*[text()='Сизов AutoTest EDDS 2-2']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
                "Уровень происшествия" -> {
                    element(byXpath("//label[text()='Оператор']/following-sibling::div//button[@aria-label='Clear']"))
                        .should(exist, ofSeconds(waitTime))
                        .hover()
                    element(byXpath("//label[text()='Оператор']/following-sibling::div//button[@aria-label='Clear']"))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
//                    element(byXpath("//label[text()='Оператор']/following-sibling::div/div[@role='button']/*[text()]"))
//                        .shouldNot(exist, ofSeconds(waitTime))
                    element(byXpath("//label[text()='Уровень происшествия']/following-sibling::div"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Уровень происшествия']/following-sibling::div/input"))
                        .sendKeys("ЧС")
                    element(byXpath("//div[@role='presentation']//*[text()='ЧС']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//span[text()='ЧС']/parent::div[@role='button']"))
                        .should(exist, ofSeconds(waitTime))
                }
            }
            //создаем отчет
            element(byXpath("//span[text()='Создать']/..")).click()
            //ждем созданный отчет
            element(byText("Reports 0070 Проверка формирования отчетов $reportType $dateTime сверка $report"))
                .should(exist, ofSeconds(waitTime))
            if (report != reportList.last()){
                clearInput("//input[@id='title']", waitTime)
            }
        }
        element(byXpath("//*[text()='Очистить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[text()='Скрыть форму']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        reportList.forEach{ report ->
            //переходим в созданный отчет
//            for (i in 1 until elements(byXpath("//tbody/tr")).size){
//                if (elements(byXpath("//tbody/tr[$i]//*[text()='Reports 0070 Проверка формирования отчетов $reportType $dateTime сверка $report']")).size == 1){
//                    if (elements(byXpath("//tbody/tr[${i+2}]//*[text()]")).size == 1){
//                        element(byXpath("//tbody/tr[${i+2}]")).scrollIntoView(false)
//                    } else {
//                        element(byXpath("//tbody/tr[$i]")).sendKeys(Keys.END)
//                    }
//                    element(byXpath("//tbody/tr[$i]//*[text()='Reports 0070 Проверка формирования отчетов $reportType $dateTime сверка $report']"))
//                        .should(exist, ofSeconds(waitTime))
//                        .shouldBe(visible, ofSeconds(waitTime))
//                        .click()
//                    break
//                }
//            }
            element(byXpath("//tbody/tr//*[text()='Reports 0070 Проверка формирования отчетов $reportType $dateTime сверка $report']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            elements(byXpath("//main//table"))
                .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1), ofSeconds(waitTime))
            element(byXpath("//*[text()='Печать']/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            for (i in 1..elements(byXpath("//main//table/tbody/tr")).size){
                if (elements(byXpath("(//main//table/tbody/tr)[$i]//*[text()='Нет данных']")).size == 0
                    && elements(byXpath("(//main//table/tbody/tr)[$i]//*[text()='из них:']")).size == 0){
                    oneReportMap.put(
                        element(byXpath("(//main//table/tbody/tr)[$i]/td[1]")).ownText,
                        element(byXpath("(//main//table/tbody/tr)[$i]/td[$valueColumnNumber]")).ownText.toInt()
                    )
                }
            }
//            callTypeList.forEach { callType ->
//                if (elements(byXpath("//table/tbody/tr/td[1][text()='$callType']")).size == 0){
//                    TableMap.put(callType, 0)
//                }
//            }
            newReportsMap.put(report, oneReportMap.clone() as MutableMap<String, Int>)
            oneReportMap.clear()
            back()
            //т.к. сравниваем карты только по конкретным значениям созданных происшествий, то кладем в список значений "всего" и "в обработке", что бы сравнить и их
            if (oldReportsMap.get(report)!!.contains("Всего")){
                validatedValues.add(report)
            }
            if (oldReportsMap.get(report)!!.contains("В обработке")){
                validatedValues.add("В обработке")
            }
        }
        reportList.forEach{report ->
            validatedValues.forEach{assertionsValue ->
                if (newReportsMap.get(report)!!.containsKey(assertionsValue)){
                    Assertions.assertTrue(newReportsMap.get(report)!!.get(assertionsValue)
                        ==
                        oldReportsMap.get(report)!!.get(assertionsValue))
                }
            }
        }
        logoffTool()
    }

}