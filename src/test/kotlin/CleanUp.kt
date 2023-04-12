
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.back
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.openqa.selenium.Keys
import org.testng.annotations.DataProvider
import test_library.alerts.AlertsEnum
import test_library.filters.FilterEnum
import test_library.menu.MyMenu
import test_library.menu.SubmenuInterface
import test_library.operator_data.OperatorDataEnum
import test_library.statuses.StatusEnum
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CleanUp : BaseTest(){


    @DataProvider(name = "Справочники отчетов")
    open fun Справочники(): Any {
        return MyMenu.Reports.values().map { arrayOf(it) }.toTypedArray()

//        return arrayOf(MyMenu.Reports.IncidentsReport)
//        return arrayOf(MyMenu.Reports.CallsReport)
//        return arrayOf(MyMenu.Reports.UsersReport)
//        return arrayOf(MyMenu.Reports.SummaryReport)

    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class, dataProvider = "Справочники отчетов", groups = ["CleanUp"])
    fun `CleanUp 9998 Удаление отчетов созданных автотестами (по наличию части имени в отчете)`(subMenu: SubmenuInterface){
        //Удаляем все отчеты
        logonTool(false)
//        for (m in 1..3){
//            when (m) {
//                1 -> {menuNavigation(MyMenu.Reports.IncidentsReport, waitTime)}
//                2 -> {menuNavigation(MyMenu.Reports.CallsReport, waitTime)}
//                3 -> {menuNavigation(MyMenu.Reports.UsersReport, waitTime)}
//            }
        menuNavigation(subMenu, waitTime)
        tableStringsOnPage(50, waitTime)
        //считаем количество строк - понадобится для прерываемого цикла
        var countString = elements(byXpath("//tbody/tr")).size
        //определяем индекс последнего столбца, что бы потом кликать по трем точкам
        val menuColumn = elements(byXpath("//thead/tr/th")).size
        val nameColumn = tableNumberOfColumn("Наименование отчета", waitTime)
        //входим в большой цикл без защитного счетчика
        tableColumnCheckbox("Наименование отчета", true, waitTime)
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
                    val deletedReport = element(byXpath("//tbody/tr[$i]/td[$nameColumn and contains(text(),'Проверка формирования отчетов')]")).ownText
                    element(byXpath("//tbody/tr[$i]/td[$menuColumn]//button")).click()
                    element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Подтвердите удаление записи']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    Thread.sleep(200)
                    element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    //                    Thread.sleep(1000)
                    //хдем зеленую всплывашку
                    checkAlert(AlertsEnum.snackbarSuccess, "Запись удалена", true, waitTime)
                    //ждем исчезновения отчета, который удаляли
                    element(byXpath("//tbody/tr[$i]/td[$nameColumn]//text()/parent::*[text()='$deletedReport']"))
                        .shouldNot(exist, ofSeconds(waitTime))
                    Thread.sleep(1500)
                    // прерываем цикл for потому что таблица перестроилась после удаления отчета
                    break
                }
            }
        }
//        }
        logoffTool()
    }


    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["CleanUp"])
    fun `CleanUp 9999 Закрытие КП созданных под логином работы автотестов` (){
        //Закроем все происшествия созданные автотестом , например за неделю
        date = LocalDate.now()
        val again = false
        val dateStart = LocalDate.now().minusDays(50).toString()
        val dateEnd = LocalDate.now().toString()
        logonTool(false)
        //запомним что мы за оператор:
        val operatorFIO = operatorData(OperatorDataEnum.`Должностное лицо`)
        logoffTool()
        //переходим к списку происшествий и ждем загрузки
        anyLogonTool("autotest_admin", "autotest_admin")
        menuNavigation(MyMenu.Incidents.IncidentsList, waitTime)
        element(byCssSelector("table>tbody>tr"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableStringsOnPage(10, waitTime)
        tableColumnCheckbox("Идентификатор;Статус", true, waitTime)
        val idColumn = tableNumberOfColumn("Идентификатор", waitTime)
        val statusColumn = tableNumberOfColumn("Статус", waitTime)
        //отчищаем фильтры, что бы закрывать и дочерние карточки ДДС
        cleanFilterByEnum(listOf(), waitTime)
        Thread.sleep(2500)
        //устанавливаем фильтр по оператору по которому логинится автотест
        setFilterByEnum(FilterEnum.Оператор, operatorFIO, waitTime)
        Thread.sleep(2500)
        //устанавливаем фильтр по дате регистрации
        setFilterByEnum(FilterEnum.Дата_регистрации, LocalDate.now().minusWeeks(20).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()+";", waitTime)
        Thread.sleep(2500)
        //Получим счетчик строк внизу страницы
//        val allRecordCountUse = element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
//            .ownText
//            .toString()
//            .split("\n")[1]
//            .split(" ")[0]
//            .toInt()
        //ищем надпись "Нет данных"
        // и войдя в цикл без защитного счетчика
        var again2 = 1
        if (element(byXpath("//table/tbody/tr//*[text()='Нет данных']")).exists()){
            again2 = 0
        }
        while (again2 > 0){//переходим в каждую первую карточку и меняем статус, на "Закрыта"
            //карточки в статусе новая, вызывают проблемы из-за того что меняют статус автоматически, даже когда по нему клацаешь
            //поэтому для них дождемся перехода в статус "в обработке"
//            val statusIC = element(byXpath("//table/tbody/tr[1]/td[$statusColumn]//*[text()]")).ownText
            val idIC = element(byXpath("//table/tbody/tr[1]/td[$idColumn]//text()/..")).ownText
            element(byXpath("//table/tbody/tr[1]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
//            if (statusIC == "Новая"){
//                try {
//                    checkICToolIsStatus(StatusEnum.`В обработке`, waitTime)
//                } catch (_:  Throwable) {
//                }
//            }
            Thread.sleep(500)
            updateICToolStatus(StatusEnum.Закрыта, longWait)
            back()
            element(byXpath("//table/tbody/tr[1]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            try {
                element(byXpath("//table/tfoot//p[contains(text(),'Всего ')]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                } catch (_:  Throwable) {
                }
            if (element(byXpath("//table/tbody/tr[2]")).exists()){
                again2 = 2
            }
            element(byXpath("//table/tbody/tr[1]/td[$idColumn]//text()/parent::*[text()='$idIC']"))
                .shouldNot(exist, ofSeconds(waitTime))
            Thread.sleep(500)
            again2 -= 1
        }
        element(byXpath("//table/tbody/tr//*[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        logoffTool()
    }
}