
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.back
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.openqa.selenium.Keys
import test_library.menu.MyMenu
import test_library.statuses.StatusEnum
import java.time.Duration.ofSeconds
import java.time.LocalDate

class CleanUp : BaseTest(){


    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["CleanUp"])
    fun `CleanUp 9998 Удаление отчетов созданных автотестами (по наличию части имени в отчете)`(){
        //Удаляем все отчеты
        logonTool()
//        tools.menuNavigation("Отчеты", "По происшествиям", waitTime)
        var menuVariable =1
        for (m in 1..3){
            when (m) {
                1 -> {menuNavigation(MyMenu.Reports.IncidentReport, waitTime)}
                2 -> {menuNavigation(MyMenu.Reports.CallsReport, waitTime)}
                3 -> {menuNavigation(MyMenu.Reports.EmployeesReport, waitTime)}
            }
//        tools.menuNavigation("Отчеты", "По обращениям", waitTime)
//        tools.menuNavigation("Отчеты", "По сотрудникам", waitTime)
            tableStringsOnPage(50, waitTime)
//            var stringNumder = 1
            //считаем количество строк - понадобится для прерываемого цикла
            var countString = elements(byXpath("//tbody/tr")).size
//            println(countString)
            //определяем индекс последнего столбца, что бы потом кликать по трем точкам
            val menuColumn = elements(byXpath("//thead/tr/th")).size
//            println(menuColumn)
            //входим в большой цикл без защитного счетчика
            tableCheckbox("Наименование отчета", true, waitTime)
            //"Проверка формирования отчетов" это часть названия отчета присваемого всем отчетам создаваемыми автотестами
            while (elements(byXpath("//tbody/tr//*[contains(text(),'Проверка формирования отчетов')]")).size > 0 ){
                countString = elements(byXpath("//tbody/tr")).size
                for (i in 1..countString){
                    //если существует отчет созданный автотестом, то...
                    if (elements(byXpath("//tbody/tr[$i]//*[contains(text(),'Проверка формирования отчетов')]")).size > 0){
                        //смотрим не последний ли он на странице, и в любом случае пролистывает до того момента, когда подвал таблицы не будет загораживать его от клика
                        if (elements(byXpath("//tbody/tr[${i + 1}]")).size > 0){
                            element(byXpath("//tbody/tr[${i + 1}]")).scrollIntoView(false)
                        } else {
                            element(byXpath("//tbody/tr[$i]")).sendKeys(Keys.END)
                        }
                        val deletedReport = element(byXpath("//tbody/tr[$i]/td[1 and contains(text(),'Проверка формирования отчетов')]")).ownText
                        element(byXpath("//tbody/tr[$i]/td[$menuColumn]//button")).click()
                        element(byXpath("//*[text()='Удалить']/parent::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        element(byXpath("//*[text()='Подтвердите удаление записи']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        Thread.sleep(200)
                        element(byXpath("//span[@title='Удалить']/button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        //                    Thread.sleep(1000)
                        //хдем зеленую всплывашку
                        element(byXpath("//*[contains(@class, 'MuiAlert-standardSuccess')][@role='alert']//*[text()='Запись удалена']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        //закрываем зеленую всплывашку
                        element(byXpath("//*[contains(@class, 'MuiAlert-standardSuccess')][@role='alert']//button"))
                            .click()
                        //ждем исчезновения отчета, который удаляли
                        element(byXpath("//tbody/tr[$i]/td[1]//*[text()='$deletedReport']"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        Thread.sleep(1500)
                        // прерываем цикл for потому что таблица перестроилась после удаления отчета
                        break
                    }
                }
            }
        }
        logoffTool()
    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["CleanUp"])
    fun `CleanUp 9999 Закрытие КП созданных под логином работы автотестов` (){
        //Закроем все происшествия созданные автотестом , например за неделю
        date = LocalDate.now()
        val dateStart = LocalDate.now().minusDays(50).toString()
        val dateEnd = LocalDate.now().toString()
        logonTool()
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
        val operator = element(byXpath("//p[text()='Должностное лицо:']/following-sibling::p")).ownText
        val operatorFIO = operator.trim()
        logoffTool()
        //переходим к списку происшествий и ждем загрузки
        anyLogonTool("autotest_admin", "autotest_admin")
        menuNavigation(MyMenu.Incidents.IncidentsList, waitTime)
        element(byCssSelector("table>tbody>tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableCheckbox("Статус", true, waitTime)
        val statusColumn = tableNumberOfColumn("Статус", waitTime)
        //отчищаем фильтры, что бы закрывать и дочерние карточки ДДС
        element(byXpath("//span[contains(text(),'Еще фильтры')]/.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//span[text()='Очистить все']/.."))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        Thread.sleep(2500)
        //устанавливаем фильтр по оператору по которому логинится автотест
        if (elements(byXpath("//span[text()='Оператор']/parent::button")).size == 1 ){
            element(byXpath("//span[text()='Оператор']/parent::button")).click()
        } else {
            element(byXpath("//span[contains(text(),'Еще фильтры')]/parent::button")).click()
        }
        element(byXpath("//input[@name='createdBy']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//body/div[@role='presentation']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//input[@name='createdBy']")).sendKeys(operatorFIO)
        element(byXpath("//body/div[@role='presentation']//*[text()='$operatorFIO']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//span[text()='Применить']/parent::button")).click()
        Thread.sleep(2500)
        //устанавливаем фильтр по дате регистрации
        if (elements(byXpath("//span[text()='Дата регистрации']/parent::button")).size == 1){
            element(byXpath("//span[text()='Дата регистрации']/parent::button")).click()
        } else {
            element(byXpath("//span[contains(text(),'Еще фильтры')]/parent::button")).click()
        }
        val dateStartList = dateStart.split("-")
        val dateEndList = dateEnd.split("-")
        //заполняем дату начала и конца периода
        element(byCssSelector("input#callReceivedAtStart"))
            .sendKeys("${dateStartList[2]}.${dateStartList[1]}.${dateStartList[0]}0000")
        element(byCssSelector("input#callReceivedAtEnd"))
            .sendKeys("${dateEndList[2]}.${dateEndList[1]}.${dateEndList[0]}2359")
        element(byXpath("//span[text()='Применить']/parent::button")).click()
        element(byXpath("//table/tbody/tr//*[text()]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        Thread.sleep(2500)
        //ищем надпись "Нет данных"
        var noData = elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size
        // и войдя в цикл без защитного счетчика
        while (noData == 0){//переходим в каждую первую карточку и меняем статус, на "Закрыта"
            //карточки в статусе новая, вызывают проблемы из-за того что меняют статус автоматически, даже когда по нему клацаешь
            //поэтому для них дождемся перехода в статус "в обработке"
            val statusIC = element(byXpath("//table/tbody/tr[1]/td[$statusColumn]//*[text()]")).ownText
            element(byXpath("//table/tbody/tr[1]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            if (statusIC == "Новая"){
                try {
                    checkICToolIsStatus(StatusEnum.`В обработке`, waitTime)
                } catch (_:  Throwable) {
                }
            }
            Thread.sleep(500)
            updateICToolStatus(StatusEnum.Закрыта, waitTime)
            back()
            element(byXpath("//table/tbody/tr[1]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            Thread.sleep(1000)
            noData = elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size
        }
        logoffTool()
    }
}