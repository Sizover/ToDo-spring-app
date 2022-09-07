package reports

import Retry
import BaseTest
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.*
//import com.codeborne.selenide.Selenide.element
//import com.codeborne.selenide.Selenide.elements
//import com.codeborne.selenide.Selenide.open
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

class Reports : BaseTest(){
    var date = ""
    var dateTime = ""
    //Время ожидания элементов при выполнении теста
    val waitTime: Long = 5
    val longWait: Long = 10

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `Reports 0010 Проверка формирования отчетов по обращениям`() {
        //A.3.23 Проверка формирования отчетов по обращениям
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
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
            .sendKeys("A.3.23 Проверка формирования отчетов по обращениям $dateTime отсчет")
        //впердоливаем говнокод по преобразование даты
        val dateM = date.split("-")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        //вбиваем адрес
        addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач", waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        element(byXpath("//span[text()='Создать']/..")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("A.3.23 Проверка формирования отчетов по обращениям $dateTime отсчет"))
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
//            element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-0']>td[data-colindex='1']>div")).ownText.toInt()
        //println(tableAll)
        if (elements(byXpath("//table/tbody/tr[1]//*[text()='Нет данных']")).size == 0) {
            //всего записей в смысле в графе всего
            tableAll = element(byXpath(tableSelector.format("Всего"))).ownText.toInt()
            //Видеоаналитика из общего числа
            tableVideo = element(byXpath(tableSelector.format("Видеоаналитика"))).ownText.toInt()
            //Внешняя АИС из общего числа
            tableAIS = element(byXpath(tableSelector.format("Внешняя АИС"))).ownText.toInt()
            //Датчик из общего числа
            tableSensor = element(byXpath(tableSelector.format("Датчик"))).ownText.toInt()
            //Портал населения из общего числа
            tablePortal = element(byXpath(tableSelector.format("Портал населения"))).ownText.toInt()
            //Портал УИВ из общего числа
            tableUIV = element(byXpath(tableSelector.format("Портал УИВ"))).ownText.toInt()
        }

        //служебный счетчик, всего строк
//        var tableStringCount = elements(byCssSelector("tr[id^='MUIDataTableBodyRow-']")).size
//        //запоминаем те значения, что будем создавать
//        for (y in 0 until tableStringCount) {
//            if (y!=1){
//            val st: String =
//                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$y']>td[data-colindex='0']>div")).ownText
//            val va: Int =
//                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$y']>td[data-colindex='1']>div")).ownText.toInt()
//            when (st) {
//                "Всего" -> {tableAll = va}
//                "Видеоаналитика" -> {tableVideo = va}
//                "Внешняя АИС" -> {tableAIS = va}
//                "Датчик" -> {tableSensor = va}
//                "Портал населения" -> {tablePortal = va}
//                "Портал УИВ" -> {tableUIV = va}
//            }
//            }
//        }
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
        var dateTime2 = ""
        for (i in 1..5) {
            dateTime = LocalDateTime.now().toString()
            date = LocalDate.now().toString()
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
            if (elements(byCssSelector("#phone")).size > 0){
                element(byCssSelector("#phone")).sendKeys("9189999999")
            }
            //ФИО
            if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
                && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
                element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname")
                element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
            }
            //Вводим случайный адрес
//            var aA = ('A'..'Z').random()
            val bB = (1..100).random()
            if (i != 3){
                addressInput("callAddress", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач $bB", waitTime)
            } else {
                addressInput("callAddress", adr, waitTime)
            }
//            element(byCssSelector("#callAddress"))
//                .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач$bB")
//            //ждем появления списка dadata
//            element(byCssSelector("div.react-dadata__suggestions"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//            //Кликаем на первую строку списка
//            element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current")).click()
            //запоминаем адрес
            //Thread.sleep(500)
            if (i == 2) {
                adr = element(byCssSelector("input#callAddress")).value.toString()
                dateTime2 = dateTime
            }
            //заполняем дополнительную информацию
            //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0190, i=$i $dateTime"
            element(byCssSelector("div[role='textbox']>p")).click()
            element(byCssSelector("div[role='textbox']"))
                .sendKeys("Reports 0010, i=$i $dateTime")
            if (i < 3) {
                //регистрируем обращение
                element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
                //выбираем тип происшествия
                element(byCssSelector("input#incidentTypeId-autocomplete")).setValue("П.5.1.5 Auto-Test")
                    .sendKeys(Keys.DOWN, Keys.ENTER)
                //Создаем карточку
                element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()
            } else if (i == 3) {
                //регистрируем обращение в ранее созданную карточку.
//                element(byText(adr)).click()
                element(byXpath("//*[@name='refetch']/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
//                val troubleshoot = element(byXpath("//*[contains(text(),'$adr')]")).ownText
//                println("troubleshoot = $troubleshoot")
                element(byXpath("//*[contains(text(),'$adr')]/ancestor::button")).click()
                element(byXpath("//span[text()='Привязать к происшествию']/parent::button")).click()
            } else if (i == 4) {
                //регистрируем ложное обращение
                element(byXpath("//*[text()='Ложное обращение']/ancestor::button")).click()
//                element(byXpath("//*[text()='Звонок без информации']/.."))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
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
            if (i < 4) {
                //Убеждаемся, что нам загрузило созданную карточку
                //проверяя что нам в принципе загрузило какую-то карточку
                element(byCssSelector("#simple-tabpanel-card"))
                    .should(exist, ofSeconds(waitTime))
                //что она в нужном статусе
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldHave(text("В обработке"), ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
//            else {
//                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
//                    .shouldHave(text("Завершена"), ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//            }
            //и что это именно так карточка которую мы только что создали
            if (i < 3) {
                element(byXpath("//strong[text()='Дополнительная информация:']/parent::div"))
                    .shouldHave(text("Reports 0010, i=$i $dateTime"), ofSeconds(waitTime))
            } else if(i==3) {
                element(byXpath("//strong[text()='Дополнительная информация:']/parent::div"))
                    .shouldHave(text("Reports 0010, i=2 $dateTime2"), ofSeconds(waitTime))
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
            .sendKeys("A.3.23 Проверка формирования отчетов по обращениям $dateTime сверка")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        //вбиваем адрес
        addressInput("address","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("A.3.23 Проверка формирования отчетов по обращениям $dateTime сверка"))
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
//        falD = element(byCssSelector(diagramSelector.format(1))).ownText.toInt()
//        falD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(1)>text")).ownText.toInt()
//        //консультации
//        conD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(2)>text")).ownText.toInt()
//        //происшествия
//        incD = element(byCssSelector("g.recharts-layer.recharts-pie-labels>g:nth-child(3)>text")).ownText.toInt()
//        //Сверяем
//        Assertions.assertTrue(falT == falD)
//        Assertions.assertTrue(conT == conD)
//        Assertions.assertTrue(incT == incD)
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
//        tableStringCount = elements(byCssSelector("tr[id^='MUIDataTableBodyRow-']")).size
//        for (u in 2 until tableStringCount) {
//            val st: String =
//                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$u']>td[data-colindex='0']>div")).ownText.toString()
//            val va: Int =
//                element(byCssSelector("tr[data-testid='MUIDataTableBodyRow-$u']>td[data-colindex='1']>div")).ownText.toInt()
//            when (st) {
//                "Видеоаналитика" -> { tableVideoT = va }
//                "Внешняя АИС" -> { tableAIST = va }
//                "Датчик" -> { tableSensorT = va }
//                "Портал населения" -> { tablePortalT = va }
//                "Портал УИВ" -> { tableUIVT = va }
//            }
//        }

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
        //Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач
        //Thread.sleep(50000)

        logoffTool()
    }



    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `Reports 0020 Проверка формирования отчетов по деятельности сотрудников`() {
        //A.3.24 Проверка формирования отчетов по деятельности сотрудников
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        logonTool()
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        menuNavigation("Отчеты", "По сотрудникам", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button")).click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет")
        //впердоливаем говнокод по преобразование даты
        val dateM = date.split("-")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач", waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
        //убедимся что мы за оператор:
        //кликаем по иконке оператора сверху справа
        //element(byCssSelector("header>div>div>div>span>button")).click()
        element(byXpath("//span[contains(text(),'.sizov')]/parent::button")).click()
        //пероеходим в профиль пользователя
        element(byCssSelector("a[href='/profile']>button"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("a[href='/profile']>button")).click()
        //Извлекаем имя оператора
        element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //val operator = element(byCssSelector("main>div:nth-child(3)>div:nth-child(3)>p")).ownText
        val operator = element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p")).ownText.trim()
//        val operatorMas = operator.split("\n")
//        val operatorFIO = operatorMas[1].trim()
//        val operatorFIO = operator.trim()
//        println("ФИО $operator")
//        println("operatorMas $operatorMas")
//        println("operatorFIO $operatorFIO")
        back()
        //$$("tr[data-testid^=MUIDataTableBodyRow-1]>td[data-testid='MuiDataTableBodyCell-0-1']>div:nth-child(2)") - фио в отчете
        //ждем
        element(byXpath("//h6[contains(text(),'по муниципальному образованию')]/../h5"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            .shouldHave(text("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime отсчет"))
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
        var dateTime2 = ""
        for (i in 1..6) {
            dateTime = LocalDateTime.now().toString()
            date = LocalDate.now().toString()
            //кликаем по иконке происшествий в боковом меню
            //Переходим в "Список происшетвий"
            menuNavigation("Происшествия", "Список происшествий", waitTime)
            //кликаем по "создать обращение"
            element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
            //заполняем карточку
            //Источник события - выбираем случайно
            element(byCssSelector("div#calltype")).click()
            val iI = (1..10).random()
            element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($iI)")).click() //не случайно =)
            //element(byCssSelector("li[role='option'][data-value='cd73ccc0-e740-4c5d-98ec-28bbe9a13be0']")).click()
            //Номер телефона
            if (elements(byCssSelector("#phone")).size > 0){
                val tel = (1000000..9999999).random()
                element(byCssSelector("#phone")).sendKeys("918$tel")
            }
            //ФИО
            if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
                && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
                element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname")
                element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
            }
            //Вводим случайный адрес
//            var aA = ('A'..'Z').random()
            val bB = (1..100).random()
            if (i != 4){
                addressInput("callAddress", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач $bB", waitTime)
            } else {
                addressInput("callAddress", adr, waitTime)
            }

//            element(byCssSelector("#callAddress"))
//                .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач$bB")
//            //ждем появления списка dadata
//            element(byCssSelector("div.react-dadata__suggestions"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
//            //Кликаем на первую строку списка
//            element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current")).click()
            //запоминаем адрес
            //Thread.sleep(500)
            if (i == 3) {
                adr = element(byCssSelector("input#callAddress")).value.toString()
                dateTime2 = dateTime
            }
            //заполняем дополнительную информацию
            //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0190, i=$i $dateTime"
            element(byCssSelector("div[role='textbox']>p")).click()
            element(byCssSelector("div[role='textbox']")).sendKeys("Reports 0020, i=$i $dateTime")
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
//                element(byXpath("//*[text()='Звонок без информации']/.."))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
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
            }
            //что она в нужном статусе
            if (i < 4) {
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldHave(text("В обработке"), ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } else if (i == 4) {
                //закрываем одну карточку
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldHave(text("В обработке"), ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']")).click()
                element(byXpath("//span[text()='Закрыта']/parent::button"))
                    .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//span[text()='Закрыта']/parent::button")).click()
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldHave(text("Закрыта"), ofSeconds(waitTime))
            }
//            это нужно было пока создание ложной и консультации перекидывало в КП, а не в список КП
//            else {
//                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
//                    .shouldHave(text("Завершена"), ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//            }
            //и что это именно так карточка которую мы только что создали
            if (i < 4) {
                element(byXpath("//strong[text()='Дополнительная информация:']/parent::div"))
                    .shouldHave(text("Reports 0020, i=$i $dateTime"), ofSeconds(waitTime))
            } else if (i==4) {
                element(byXpath("//strong[text()='Дополнительная информация:']/parent::div"))
                    .shouldHave(text("Reports 0020, i=3 $dateTime2"), ofSeconds(waitTime))
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Thread.sleep(500)
        //ждем загрузки таблицы происшествий
//        element(byCssSelector("main div table>tbody"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        menuNavigation("Отчеты", "По сотрудникам", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title")).sendKeys("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td"))
            .shouldHave(text("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime)).click()
//        //убедимся что мы за оператор:
//        //кликаем по иконке оператора сверху справа
//        element(byCssSelector("header>div>div>div>span>button")).click()
//        //пероеходим в профиль пользователя
//        element(byCssSelector("a[href='/profile']>button"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//        element(byCssSelector("a[href='/profile']>button")).click()
//        element(byCssSelector("main>div:nth-child(3)>div:nth-child(3)>p"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
////        val operator = element(byCssSelector("main>div:nth-child(3)>div:nth-child(3)>p")).ownText
////        val operatorMas = operator.split("\n")
////        val operatorFIO = operatorMas[1].toString()
//        //println("ФИО ${operator[1]}")
//        //println(operatorFIO)
//        back()
//        //$$("tr[data-testid^=MUIDataTableBodyRow-1]>td[data-testid='MuiDataTableBodyCell-0-1']>div:nth-child(2)") - фио в отчете
        //ждем
        element(byXpath("//h6[contains(text(),'по муниципальному образованию')]/../h5"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            .shouldHave(text("A.3.24 Проверка формирования отчетов по деятельности сотрудников $dateTime сверка"))
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
    fun `Reports 0030 Проверка формирования отчетов по происшествиям`() {
        //PMI 0210
        //A.3.25 Проверка формирования отчетов по происшествиям
        //просто кошмар какой объемный тест получился(
        dateTime = LocalDateTime.now().toString()
        date = LocalDate.now().toString()
        logonTool()
        //кликаем по иконке отчетов
        //Переходим в "отчет По происшествиям"
        menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime отсчет")
        //впердоливаем говнокод по преобразование даты
        val dateM = date.split("-")
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd")).sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
//        element(byCssSelector("input#address"))
//            .sendKeys("Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач")
//        //кликаем по первому адресу dadata
//        element(byCssSelector("div.react-dadata__suggestions>div.react-dadata__suggestion.react-dadata__suggestion--current"))
//            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
//            .click()
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime отсчет"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем заголовок таблицы
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime отсчет']"))
            .should(exist, ofSeconds(waitTime)).shouldBe(visible, ofSeconds(waitTime))
        //Запоминаем значения отчета
        //значения в легенде
        val colorTableSelector = "//table/tbody/tr/td[text()='%s']/following-sibling::td"
//        val legendSelector = "table[aria-label='simple table'] tr:nth-child(%d)>td:nth-child(2)"
        val legendAllBefore = element(byXpath(colorTableSelector.format("Всего"))).ownText.toInt()
        val legendNewBefore = element(byXpath(colorTableSelector.format("Новые"))).ownText.toInt()
        val legendProcessingBefore = element(byXpath(colorTableSelector.format("В обработке"))).ownText.toInt()
        val legendReactionBefore = element(byXpath(colorTableSelector.format("Реагирование"))).ownText.toInt()
        val legendDoneBefore = element(byXpath(colorTableSelector.format("Завершены"))).ownText.toInt()
        val legendCanselBefore = element(byXpath(colorTableSelector.format("Отменены"))).ownText.toInt()
        val legendCloseBefore = element(byXpath(colorTableSelector.format("Закрыты"))).ownText.toInt()
        //значения в таблице
        val oldIncidentTypeList = mutableListOf<String>()
        val oldAmountIncidentList = mutableListOf<Int>()
        val oldAmountAffectedPeopleList = mutableListOf<Int>()
        val oldAmountAffectedChildrenList = mutableListOf<Int>()
        val oldAmountDiePeopleList = mutableListOf<Int>()
        val oldAmountDieChildrenList = mutableListOf<Int>()
        val tableSelector = "//table[@aria-label='sticky table']/tbody/tr[%d]/td[%d]"
        //table[@aria-label='sticky table']/tbody/tr
        var oldTableStringCount = 0
        if (elements(byXpath("//table/tbody/tr[1]//*[text()='Нет данных']")).size == 0) {
            oldTableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
            for (i in 0 until oldTableStringCount) {
                oldIncidentTypeList.add(i, element(byXpath(tableSelector.format(i + 1, 1))).ownText)
                oldAmountIncidentList.add(i, element(byXpath(tableSelector.format(i + 1, 2))).ownText.toInt())
                oldAmountAffectedPeopleList.add(i, element(byXpath(tableSelector.format(i + 1, 3))).ownText.toInt())
                oldAmountAffectedChildrenList.add(i, element(byXpath(tableSelector.format(i + 1, 4))).ownText.toInt())
                oldAmountDiePeopleList.add(i, element(byXpath(tableSelector.format(i + 1, 5))).ownText.toInt())
                oldAmountDieChildrenList.add(i, element(byXpath(tableSelector.format(i + 1, 6))).ownText.toInt())
            }
        }
        val oldIncidentTypeListCount: Int = oldIncidentTypeList.count()
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Создаем карточки происшествий
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //если отчет отсчета был пуст, или имел меньше шести строк, то при создании КП тип происшествия кладем сюда
        val shortSelectedIncidentTypeList = mutableListOf<String>()
        //тип создаваемого происшествия выбираем "случайно": это последовательные значения стартующие от случайной величины
        val rnd = (2..30).random()
        //создаем семь происшествий, одно из которых не попадет в отчет, т.к. идет по другому населенному пункту (а адрес при создании отчета мы закладываем)
        for (i in 1..7) {
            dateTime = LocalDateTime.now().toString()
            date = LocalDate.now().toString()
            //кликаем по иконке происшествий в боковом меню
            //Переходим в "Список происшетвий"
            menuNavigation("Происшествия", "Список происшествий", waitTime)
            //кликаем по "создать обращение"
            element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
            //заполняем карточку
            //Источник события - выбираем случайно
            element(byCssSelector("div#calltype")).click()
            val listbox = (1..10).random()
            element(byCssSelector("div#menu->div>ul[role='listbox']>li:nth-child($listbox)")).click()
            //element(byCssSelector("li[role='option'][data-value='cd73ccc0-e740-4c5d-98ec-28bbe9a13be0']")).click()//это фиксированное значение "факс"
            //Номер телефона
            if (elements(byCssSelector("#phone")).size > 0){
                val tel = (1000000..9999999).random()
                element(byCssSelector("#phone")).sendKeys("918$tel")
            }
            //ФИО
            if (elements(byCssSelector("input[id='fio.lastname']")).size > 0
                && elements(byCssSelector("input[id='fio.firstname']")).size > 0){
                element(byCssSelector("input[id='fio.lastname']")).sendKeys("$date AutoTestLastname")
                element(byCssSelector("input[id='fio.firstname']")).sendKeys("AutoTestFirstname")
            }
            //Вводим случайный адрес
//                val randomLetter = ('A'..'Z').random()
            val randomNumber = (1..100).random()
            //Вбиваем первый символ
            if (i==4){
                addressInput("callAddress","Карачаево-Черкесская Респ, г Усть-Джегута, ул Мира $randomNumber",waitTime)
            } else {
                addressInput("callAddress","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач $randomNumber",waitTime)
            }
            //заполняем дополнительную информацию
            //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0190, i=$i $dateTime"
            element(byCssSelector("div[role='textbox']>p")).click()
            element(byCssSelector("div[role='textbox']"))
                .sendKeys("Reports 0030, i=$i $dateTime")
            //регистрируем обращение
            //element(byXpath("//*[@id=\"skeleton\"]/div/main/div[3]/div[1]/div/form/div[9]/div[1]/button")).click()
            element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
            //ждем поле выбора происшествия
            element(byCssSelector("input#incidentTypeId-autocomplete"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //выбираем тип происшествия
            //Сначала создаем происшествия с теми же типами, что были в первом отчете, если они были
            if (oldIncidentTypeListCount >= i && i<7){
                element(byCssSelector("input#incidentTypeId-autocomplete"))
                    .sendKeys(oldIncidentTypeList[i-1])
//                element(byXpath("//div[@role='presentation']//ul/li[last()]"))
                //недоконца осмысленная конструкция стыренная с интернета, указывающая на элемент, текст которого в конце содержит нечто
                element(byXpath("//*[substring(text(), string-length(text())- string-length('${oldIncidentTypeList[i-1]}') + 1) = '${oldIncidentTypeList[i-1]}']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
//                element(byCssSelector("input#incidentTypeId-autocomplete"))
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//                Thread.sleep(10000)
//            if (oldIncidentTypeListCount > 5 && i == 6) {
//                //(oldIncidentTypeListCount >= i && i<7)
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[5])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 4 && i == 5) {
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[4])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 3 && i == 4) {
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[3])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 2 && i == 3) {
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[2])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 1 && i == 2) {
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[1])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
//            } else if (oldIncidentTypeListCount > 0 && i == 1) {
//                //element(byCssSelector("input#incidentTypeId")).setValue("${incidentTypeList[0]}")
//                element(byCssSelector("input#incidentTypeId")).setValue(oldIncidentTypeList[0])
//                    .sendKeys(Keys.DOWN, Keys.ENTER)
                //контрольное, заренее известное по типу, происшествие
            } else if(i == 7){
                element(byCssSelector("input#incidentTypeId-autocomplete"))
                    .sendKeys("П.5.1.5 Auto-Test", Keys.DOWN, Keys.ENTER)
                //случайные типы происшествий на те первые 6 позиций отчета, которых в первом отчете могло не быть
            } else {
                //кликаем по типу происшествия
//                element(byCssSelector("input#incidentTypeId")).click()
//                //случайное число раз жмем вниз, выбирая тип происшествия
//                repeat(rnd+i){
//                    element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.DOWN)
//                }
//                element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.ENTER)
//                tools.inputRandom("incidentTypeId")
                inputRandomNew("incidentTypeId-textfield", false, waitTime)
                element(byCssSelector("input#incidentTypeId-autocomplete[value*='.']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //убеждаемся что такого происшествия нет в первом отчете и если есть, то выбираем другое
                var longSelectedIncidentType = element(byCssSelector("input#incidentTypeId-autocomplete")).getAttribute("value").toString()
                var shortSelectedIncidentType = longSelectedIncidentType.substring(longSelectedIncidentType.indexOf(' ') + 1)
                while (oldIncidentTypeList.contains(shortSelectedIncidentType)
                    || shortSelectedIncidentTypeList.contains(shortSelectedIncidentType)
                    || shortSelectedIncidentType=="Auto-Test")
                {
                    repeat(longSelectedIncidentType.length)
                    {element(byCssSelector("input#incidentTypeId-autocomplete")).sendKeys(Keys.BACK_SPACE) }
//                    tools.inputRandom("incidentTypeId")
                    inputRandomNew("incidentTypeId-textfield", false, waitTime)
                    longSelectedIncidentType = element(byCssSelector("input#incidentTypeId-autocomplete")).getAttribute("value").toString()
                    shortSelectedIncidentType = longSelectedIncidentType.substring(longSelectedIncidentType.indexOf(' ') + 1)

//                    element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.DOWN)
//                    element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.DOWN)
//                    //убеждаемся что не переклацали список попав на невводимое значение между началом и концом
//                    if (elements(byCssSelector("input[name='incidentTypeId'][aria-activedescendant^='incidentTypeId-option']")).size ==0){
//                        element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.DOWN)
//                    }
//                    element(byCssSelector("input#incidentTypeId")).sendKeys(Keys.ENTER)
//                    longSelectedIncidentType = element(byCssSelector("input#incidentTypeId")).getAttribute("value").toString()
//                    shortSelectedIncidentType = longSelectedIncidentType.substring(longSelectedIncidentType.indexOf(' ') + 1)
                }
                //кладем созданные происшествия в список
                shortSelectedIncidentTypeList.add(
                    index = (i-oldIncidentTypeListCount-1),
                    element = shortSelectedIncidentType)
            }
            //указываем пострадавших
            val affectedSelector = "div[role='region']>div>div>div:first-child>div:nth-child(%d) input"
            if (i!=7 && i!=4){element(byCssSelector("input#isThreatPeople")).click()
                when (i) {
                    1 -> {element(byCssSelector(affectedSelector.format(1))).sendKeys("3") }
                    2 -> {element(byCssSelector(affectedSelector.format(1))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(2))).sendKeys("3")}
                    3 -> {element(byCssSelector(affectedSelector.format(3))).sendKeys("3") }
                    6 -> {element(byCssSelector(affectedSelector.format(3))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(4))).sendKeys("3") }
                    5 -> {element(byCssSelector(affectedSelector.format(1))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(2))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(3))).sendKeys("3")
                        element(byCssSelector(affectedSelector.format(4))).sendKeys("3")
                    }
                }
//                for (e in 1 until i){element(byCssSelector("div[role='region']>div>div>div:first-child>div:nth-child($e) input")).setValue("3")}//альтернатива блоку выше, но не создающая происшествие только с погибшими
            }
            //Создаем карточку
            element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()
            //Убеждаемся, что нам загрузило созданную карточку
            //проверяя что нам в принципе загрузило какую-то карточку

            element(byCssSelector("#simple-tabpanel-card"))
                .should(exist, ofSeconds(waitTime))
            //что она в нужном статусе
            element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                .shouldHave(text("В обработке"), ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //и что это именно так карточка которую мы только что создали
            element(byCssSelector("div#panel1a-content div[style='gap: 16px 0px;']>div:nth-child(5)"))
                .shouldHave(text("Reports 0030, i=$i $dateTime"), ofSeconds(waitTime))
            //меняем статус карточки
            //стату "В обработке" не кликабелен для карточки в этом статусе.
            //попытка установить статус "Новая" может сработать, а может и не сработать, что ломает логику теста
            if ((i in (2..6)) && (i != 4)) {
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']")).click()
                element(byCssSelector("div.MuiPaper-rounded>div.MuiGrid-spacing-xs-1>div:nth-child($i)>button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byCssSelector("div.MuiPaper-rounded>div.MuiGrid-spacing-xs-1>div:nth-child($i)>button")).click()
                //ждем загрузки нового статуса, а точнее есчезновения статуса "В обработке"
                element(byCssSelector("button[style='min-width: 140px; white-space: nowrap;']"))
                    .shouldNotHave(text("В обработке"), ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //создаем первый контрольный отчет
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime сверка")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address","Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime сверка"))
            .should(exist, ofSeconds(waitTime)).shouldBe(
                visible,
                ofSeconds(waitTime)
            ).click()
        //ждем заголовок
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime сверка']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //Запоминаем значения отчета
        //значения в легенде
        val legendAllAfter = element(byXpath(colorTableSelector.format("Всего"))).ownText.toInt()
        val legendNewAfter = element(byXpath(colorTableSelector.format("Новые"))).ownText.toInt()
        val legendProcessingAfter = element(byXpath(colorTableSelector.format("В обработке"))).ownText.toInt()
        val legendReactionAfter = element(byXpath(colorTableSelector.format("Реагирование"))).ownText.toInt()
        val legendDoneAfter = element(byXpath(colorTableSelector.format("Завершены"))).ownText.toInt()
        val legendCanselAfter = element(byXpath(colorTableSelector.format("Отменены"))).ownText.toInt()
        val legendCloseAfter = element(byXpath(colorTableSelector.format("Закрыты"))).ownText.toInt()
        //значения в таблице
        val newTableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
        val newIncidentTypeList = mutableListOf<String>()
        val newAmountIncidentList = mutableListOf<Int>()
        val newAmountAffectedPeopleList = mutableListOf<Int>()
        val newAmountAffectedChildrenList = mutableListOf<Int>()
        val newAmountDiePeopleList = mutableListOf<Int>()
        val newAmountDieChildrenList = mutableListOf<Int>()
        for (i in 0 until newTableStringCount) {
            newIncidentTypeList.add(i, element(byXpath(tableSelector.format(i + 1, 1))).ownText)
            newAmountIncidentList.add(i, element(byXpath(tableSelector.format(i + 1, 2))).ownText.toInt())
            newAmountAffectedPeopleList.add(i, element(byXpath(tableSelector.format(i + 1, 3))).ownText.toInt())
            newAmountAffectedChildrenList.add(i, element(byXpath(tableSelector.format(i + 1, 4))).ownText.toInt())
            newAmountDiePeopleList.add(i, element(byXpath(tableSelector.format(i + 1, 5))).ownText.toInt())
            newAmountDieChildrenList.add(i, element(byXpath(tableSelector.format(i + 1, 6))).ownText.toInt())
        }
//            var newIncidentTypeListCount: Int = newIncidentTypeList.count()
        //значения в диаграмме
//        var diagNew = 0
//        val diagSelector = "svg.recharts-surface>g>g.recharts-pie-labels>g:nth-child(%d)>text"
        //сравниваем новую диаграмму со старой легендой, учитывая созданные КП. "Новые" происшествия не рассматриваем
        //т.к. наличие цифры на диаграмме зависит от соотношения количества разных карточек, перед сравнением диаграммы убеждаемся, что вобще есть что сравнивать
        val diagSelector = "g.recharts-pie>g.recharts-pie-labels>g:nth-child(%d)>text"
//        val noDigitDiagSelector = "g.recharts-pie>g.recharts-pie-labels>g:nth-child(%s)>text>tspan"
        //diagNew = element(byCssSelector("svg.recharts-surface>g>g.recharts-pie-labels>g:nth-child(1)>text")).ownText.toInt()
        var diagProcessing = 0
        var diagReaction = 0
        var diagDone = 0
        var daigCansel = 0
        var daagClose = 0
        for (i in 2..6){
            if (element(byCssSelector(diagSelector.format(i))).getCssValue("stroke") == "white"){
                when(i){
                    2 -> { diagProcessing = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(diagProcessing == (legendProcessingBefore + 2)) }
                    3 -> { diagReaction = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(diagReaction == (legendReactionBefore + 1)) }
                    4 -> { diagDone = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(diagDone == (legendDoneBefore + 1)) }
                    5 -> {daigCansel = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(daigCansel == (legendCanselBefore + 1))}
                    6 -> {daagClose = element(byXpath(diagSelector.format(i))).ownText.toInt()
                        Assertions.assertTrue(daagClose == (legendCloseBefore + 1))}
                }
            }
        }
//        if (elements(byXpath(diagSelector.format(2))).size == 1){
//            diagProcessing = element(byXpath(diagSelector.format(2))).ownText.toInt()
//            Assertions.assertTrue(diagProcessing == (legendProcessingBefore + 2))
//        }
//        if (elements(byXpath(diagSelector.format(3))).size == 1){
//            diagReaction = element(byXpath(diagSelector.format(3))).ownText.toInt()
//            Assertions.assertTrue(diagReaction == (legendReactionBefore + 1))
//        }
//        if (elements(byXpath(diagSelector.format(4))).size == 1){
//            diagDone = element(byXpath(diagSelector.format(4))).ownText.toInt()
//            Assertions.assertTrue(diagDone == (legendDoneBefore + 1))
//        }
//        if (elements(byXpath(diagSelector.format(5))).size == 1){
//            daigCansel = element(byXpath(diagSelector.format(5))).ownText.toInt()
//            Assertions.assertTrue(daigCansel == (legendCanselBefore + 1))
//        }
//        if (elements(byXpath(diagSelector.format(6))).size == 1){
//            daagClose = element(byXpath(diagSelector.format(6))).ownText.toInt()
//            Assertions.assertTrue(daagClose == (legendCloseBefore + 1))
//        }
        //сравниваем две леганды - старую и новую
        Assertions.assertTrue(legendAllAfter == (legendAllBefore + 6))
        Assertions.assertTrue(legendNewBefore == legendNewAfter)
        Assertions.assertTrue(legendProcessingAfter == (legendProcessingBefore + 2))
        Assertions.assertTrue(legendReactionAfter == (legendReactionBefore + 1))
        Assertions.assertTrue(legendDoneAfter == (legendDoneBefore + 1))
        Assertions.assertTrue(legendCanselAfter == (legendCanselBefore + 1))
        Assertions.assertTrue(legendCloseAfter == (legendCloseBefore + 1))
        val deltaAffectedPeopleList = mutableListOf(3, 3, 0, 0, 3, 0)
        val deltaAffectedChildrenList = mutableListOf(0, 3, 0, 0, 3, 0)
        val deltaDiePeopleList = mutableListOf(0, 0, 3, 0, 3, 3)
        val deltaDieChildrenList = mutableListOf(0, 0, 0, 0, 3, 3)
        for (i in 0..5) {
            //проводим проверку в таблице для типов происшествий существовавших в первом отчете
            if ((oldTableStringCount > i) && (i != 3)) {
                newIncidentTypeList.forEachIndexed { newListIndex, newValue ->
                    //Учитываем, что обязательно создаваемое происшествие Auto-Test могло существовать в первом отчете
                    if (oldIncidentTypeList[i] == newValue) {
//                        println("i = $i, oldIncidentTypeList[i] ${oldIncidentTypeList[i]}")
                        if (oldIncidentTypeList[i] != "Auto-Test") {
//                            println("oldAmountIncidentList[i] ${oldAmountIncidentList[i]} +1 == ${newAmountIncidentList[newListIndex]}")
                            Assertions.assertTrue((oldAmountIncidentList[i] + 1) == newAmountIncidentList[newListIndex])
                        } else {
//                            println("oldAmountIncidentList[i] ${oldAmountIncidentList[i]} +2 == ${newAmountIncidentList[newListIndex]}")
                            Assertions.assertTrue((oldAmountIncidentList[i] + 2) == newAmountIncidentList[newListIndex])
                        }
                        Assertions.assertTrue((oldAmountAffectedPeopleList[i] + deltaAffectedPeopleList[i]) == newAmountAffectedPeopleList[newListIndex])
                        Assertions.assertTrue((oldAmountAffectedChildrenList[i] + deltaAffectedChildrenList[i]) == newAmountAffectedChildrenList[newListIndex])
                        Assertions.assertTrue((oldAmountDiePeopleList[i] + deltaDiePeopleList[i]) == newAmountDiePeopleList[newListIndex])
                        Assertions.assertTrue((oldAmountDieChildrenList[i] + deltaDieChildrenList[i]) == newAmountDieChildrenList[newListIndex])
                    }
                }
                //проводим проверку в таблице для типов созданных происшествий, при том надо учесть возможность того, что созданное происшествие чей тип случайный, не совпало с происшествием которое было в первом отчете //учтено при создании КП
            } else if ((i >= oldTableStringCount) && (i != 3)){
                newIncidentTypeList.forEachIndexed { newListIndex, newValue ->
                    if (shortSelectedIncidentTypeList[i-oldTableStringCount] == newValue) {
                        Assertions.assertTrue(newAmountIncidentList[newListIndex]==1)
                        Assertions.assertTrue(deltaAffectedPeopleList[i] == newAmountAffectedPeopleList[newListIndex])
                        Assertions.assertTrue(deltaAffectedChildrenList[i] == newAmountAffectedChildrenList[newListIndex])
                        Assertions.assertTrue(deltaDiePeopleList[i] == newAmountDiePeopleList[newListIndex])
                        Assertions.assertTrue(deltaDieChildrenList[i] == newAmountDieChildrenList[newListIndex])
                    }
                }
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//понеслись создавать отчеты по пострадавшим
//создаем отчет с указанием максимума из имеющихся пострадавших, и ждем соответственно 0 строк
        val maximumAffectedPeople = newAmountAffectedPeopleList.maxOrNull()
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
        //вбиваем число пострадавших соответствующее максимальному числу второго отчета, ждем 0 строк потом
        element(byXpath("//label[text()='Число пострадавших, более']/following-sibling::div/input"))
            .sendKeys(maximumAffectedPeople.toString())
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем заголовок
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки в таблице, их должно быть 0
        var tableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
//        Assertions.assertTrue(tableStringCount==0)
        element(byXpath("//table[@aria-label='sticky table']/tbody/tr/td//*[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//строим отчет с числом пострадавших на 1 меньше и ожидаем, что записей будет столько сколько должно быть)
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max -1")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
        //вбиваем число пострадавших соответствующее максимальному числу второго отчета, ждем строк более 0 потом
        element(byXpath("//label[text()='Число пострадавших, более']/following-sibling::div/input")).sendKeys((maximumAffectedPeople?.minus(1)).toString())
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max -1"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем заголовок
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime пострадавшие max -1']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //считаем строки, и ожидаем что их столько же, сколько наибольшего числа в массиве пострадавших
        val amountMaxAffected = newAmountAffectedPeopleList.filter { it == maximumAffectedPeople}
        tableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
        Assertions.assertTrue(amountMaxAffected.size == tableStringCount)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//создаем отчет с указанием типа происшествия и просто проверяем, что строка одна
        //кликаем по иконке отчетов
        //Переходим в "отчет по деятельности сотрудников"
        menuNavigation("Отчеты", "По происшествиям", waitTime)
        //кликаем по "Создать отчет"
        element(byXpath("//span[text()='Создать отчет']/parent::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //Заполняем поля отчета - название
        element(byCssSelector("input#title"))
            .sendKeys("A.3.25 Проверка формирования отчетов по происшествиям $dateTime Тип происшествия")
        //впердоливаем говнокод по преобразование даты
        //заполняем дату начала и конца периода отчета сегоднешним числом
        element(byCssSelector("input#periodStart"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        element(byCssSelector("input#periodEnd"))
            .sendKeys("${dateM[2]}.${dateM[1]}.${dateM[0]}")
        addressInput("address", "Карачаево-Черкесская Респ, Усть-Джегутинский р-н, аул Эльтаркач",waitTime)
        //вбиваем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete"))
            .sendKeys(newIncidentTypeList[0])
        element(byCssSelector("input#incidentTypeId-autocomplete"))
            .sendKeys(Keys.DOWN, Keys.ENTER)
        //создаем отчет
        //Thread.sleep(50000)
        element(byXpath("//span[text()='Создать']/parent::button")).click()
        //переходим в созданный отчет
        element(byXpath("//tbody/tr/td[1]"))
            .shouldHave(text("A.3.25 Проверка формирования отчетов по происшествиям $dateTime Тип происшествия"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //ждем заголовок
        element(byXpath("//h5[text()='A.3.25 Проверка формирования отчетов по происшествиям $dateTime Тип происшествия']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableStringCount = elements(byXpath("//table[@aria-label='sticky table']/tbody/tr")).size
        Assertions.assertTrue(tableStringCount == 1)

        logoffTool()
    }
}