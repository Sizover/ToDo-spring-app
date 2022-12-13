


import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.openqa.selenium.Keys
import java.io.File
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

class PlayGround : BaseTest(){
    var date = LocalDate.now()
    var dateTime = LocalDateTime.now()
    var waitTime: Long = 5



//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `Черновик теста на создание МО`() {
        //создадим пару МО, один оставив навсегда, а второй создавая и удаляя каждый раз
        val moATItWas = mutableListOf<String>()
        var moATCreated = mutableListOf<String>("AutoTest T 0020 МО")
        logonTool()
        menuNavigation("Справочники","Муниципальные образования", waitTime)
        checkbox("", true, waitTime)
        Thread.sleep(1000)
        //внесем существующие телефонные коды в отдельный список
//        var telCodeElementsCollection = elements(byXpath(""))
        val telCodeList = mutableListOf<String>()
        //Выясняем в каком столбце хранятся телефонные коды
        var telCodeColumnIndex = numberOfColumn("Телефонный код", waitTime)
        //Выясняем в каком столбце хранятся телефонные коды
//        val telCodeElements = elements(byXpath("//thead/tr/th"))
//        telCodeElements.forEachIndexed{index, element ->
//            println(index.toString() + " ," + element.ownText )
//            if (element.ownText == "Телефонный код"){
//                telCodeColumnIndex = index + 1
//            }
//        }
//        for (i in 1..elements(byXpath("//thead/tr/th")).size){
//            if (element(byXpath("//thead/tr/th[$i]//*[text()]")).ownText == "Телефонный код"){
//                telCodeColumnIndex = i
//            }
//        }
        //Внесем телефонные коды в отдельный список
        //пагинации теперь нет
//        do {
//            telCodeElementsCollection = elements(byXpath("//tbody/tr/td[$telCodeColumnIndex]//*[text()]"))
//            telCodeElementsCollection.forEach {
//                telCodeList.add(it.ownText)
//            }
//            //побеждаем пагинацию, проходясь по всему справочнику и собирая телефонное коды
//            val pagesLi = elements(byXpath("//nav/ul/li")).size
//            if (!element(byXpath("//nav/ul/li[${pagesLi - 2}]/div")).getAttribute("style")?.contains("color: blue")!!){
//                element(byXpath("//nav/ul/li[${pagesLi - 1}]/div")).click()
//            }
//            Thread.sleep(1000)
//        }
//        while (!element(byXpath("//nav/ul/li[${pagesLi - 2}]/div")).getAttribute("style")?.contains("color: blue")!!)
        while(elements(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).size == 1){
            element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).click()
        }
        val telCodeElementsCollection = elements(byXpath("//tbody/tr/td[$telCodeColumnIndex][text()]"))
            telCodeElementsCollection.forEach {
                telCodeList.add(it.ownText)
            }
        //воспользуемся поиском что бы найти МО с "AutoTest" в названии
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//*[@name='search']/following-sibling::input"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[@name='search']/following-sibling::input"))
            .sendKeys("AutoTest", Keys.ENTER)
        Thread.sleep(1000)
        if (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0){
            val moNameColumn = numberOfColumn("Наименование", waitTime)
            val moParentNameColumn = numberOfColumn("Субъект", waitTime)

        }















        //если они есть, то составляем их список, не удаляем из него "AutoTest Основное МО" (что бы еще раз не искать его потом)
        // и поштучно удаляем все МО из списка
        if (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0) {
            val autotestMOElementsList = elements(byXpath("//tbody/tr/td[1]//*[contains(text(),'AutoTest')]"))
            //можно при наполнении moATItWas через else if проверить на соответствие заголовка МО "AutoTest Основное МО"
            // и удалить его из moAtCreated в таком случае
            //или просто потом проверить есть ли "AutoTest Основное МО" в moATItWas, и если  нет положить в moAtCreated
            // перед этим не удаляя его из КИАП
            autotestMOElementsList.forEach {
                if (
//                    (it.ownText.toString() != "AutoTest Основное МО")
//                    &&
                    (it.ownText.toString().contains("AutoTest"))
                ) {
                    moATItWas.add(it.ownText.toString().trim())
                }
            }
            moATItWas.forEach{
                if (it != "AutoTest Основное МО"){
                element(byCssSelector("input[placeholder]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                var inputSearchValue = ""
                inputSearchValue = element(byCssSelector("input[placeholder]")).getAttribute("value").toString()
                if (inputSearchValue.isNotEmpty()){
                    element(byCssSelector("input[placeholder]")).sendKeys(Keys.END)
                    repeat(inputSearchValue.length){
                        element(byCssSelector("input[placeholder]")).sendKeys(Keys.BACK_SPACE)
                    }
                }
                element(byCssSelector("input[placeholder]")).sendKeys(it, Keys.ENTER)
                Thread.sleep(1000)
                //открываем трехточечное меню
                element(byXpath("//*[text()='$it']/parent::td[1]/parent::tr//button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //удаляем метку
                element(byXpath("//span[text()='Удалить']/parent::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                //подтверждаем удаление
                element(byXpath("//span[@title='Удалить']/button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//tbody/tr/td[1]//*[text()='$it']"))
                    .shouldNot(exist, ofSeconds(waitTime))
                }
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //удалили все что нас интересовало, теперь будем создавать
        if (!moATItWas.contains("AutoTest Основное МО")){
            moATCreated.add("AutoTest Основное МО")
        }
        moATCreated.forEach{
            element(byXpath("//span[text()='Добавить новое']/parent::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//label[text()='Наименование']/parent::div//input[@id='title']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//label[text()='Наименование']/parent::div//input[@id='title']"))
                .sendKeys(it)
            val kladr = (10..99).random()
            element(byXpath("//label[text()='КЛАДР']/parent::div//input[@id='kladrId']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//label[text()='КЛАДР']/parent::div//input[@id='kladrId']"))
                .sendKeys("${kladr}00000000000")
            addressInput("address","г Краснодар, ул Путевая",waitTime)
            var telCode: Int
            do {
                telCode = (10000..99999).random()
            } while (telCodeList.contains(telCode.toString()))
            element(byXpath("//label[text()='Телефонный код МО']/parent::div//input")).click()
            element(byXpath("//label[text()='Телефонный код МО']/parent::div//input")).sendKeys(telCode.toString())
            telCodeList.add(telCode.toString())
            element(byXpath("//span[text()='Добавить']/parent::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
        }
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `Черновик`() {
        logonTool()
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        element(byXpath("//*[text()='Создать карточку']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        createICToolIsThreatPeople(true, "", "", "15", "", waitTime)
        Thread.sleep(20000)
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class, groups = ["ПМИ", "ALL"])
    fun `MT_003 Черновик`() {
        //Создаем КП, указывая координаты вручную (случайные из диапазона), затем переходим на карту
        //считываем ссылку в свойствах центрующей иконки, достаем оттуда координаты и сравниваем с заданными с учетом некоторой погрешности
        //убеждаемся что на карте присутствуют 5 кусочков из openStreetMap соответствующих координатам, возвращаемся в КП и убеждаемся что вернулись в КП
        try {
            logonTool()
        } catch (_: Throwable) {
            element(byCssSelector("header button svg[name='user']"))
                .should(exist, ofSeconds(waitTime))
        }
        menuNavigation("Происшествия", "Создать карточку", waitTime)
        element(byXpath("//*[text()='Создать карточку']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//label[text()='Метки']/..//input[@id='labelsId-autocomplete']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='presentation']"))
            .should(exist, ofSeconds(waitTime))
        println(elements(byXpath("//div[@role='presentation']/div/ul/*")))
    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `PMI 0150 черновик`() {
        //A311 Регистрация вызова (формирование карточки происшествия)
        //A.3.17 Назначение карточки происшествия на службу ДДС/ЕДДС (только службы, которые подключены к Системе)
        //Проверка изменения статуса родительской карточки при изменении статуса карточки назначения
        //логинимся
        dateTime = LocalDateTime.now()
        date = LocalDate.now()
        anyLogonTool("a.sizov.edds.1.1", "a.sizov.edds.1.1")
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        //кликаем по "создать обращение"
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        element(byXpath("//span[text()='Создать обращение']/parent::button")).click()
//        element(byCssSelector("div span[aria-label='add'] button[type='button']")).shouldHave(exactText("Создать обращение"))
//            .click()
        //заполняем карточку
        //Источник события - выбираем случайно
        createICToolCalltype("", waitTime)
        //Номер телефона
        createICToolPhone("", waitTime)
        //ФИО
        createICToolFIO("$date AutoTestLastname", "AutoTestFirstname", "", waitTime)
        //адрес с тестированием транслитерации и клик по dadata
        addressInput("callAddress","vbhf5",waitTime)
        //заполняем дополнительную информацию
        //element(byCssSelector("textarea[name='comment']")).value = "AutoTest N 0150 $dateTime"
        createICToolsDopInfo("AutoTest PMI 0150 $dateTime", waitTime)
        //регистрируем обращение
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        //выбираем тип происшествия
        element(byCssSelector("input#incidentTypeId-autocomplete")).sendKeys("П.5.1.5 Auto-Test", Keys.DOWN, Keys.ENTER)
        val isThreatPeople = true
        val victimsCount = (0..100).random()
        val victimsChildren = (0..victimsCount).random()
        val deathToll = (0..50).random()
        val deathChildren = (0..deathToll).random()
        //Создаем карточку
        createICToolIsThreatPeople(isThreatPeople, victimsCount.toString(), victimsChildren.toString(), deathToll.toString(), deathChildren.toString(), waitTime)
        element(byXpath("//span[text()='Сохранить карточку']/parent::button")).click()


        //Убеждаемся, что нам загрузило созданную карточку
        //проверяя что нам в принципе загрузило какую-то карточку
        element(byCssSelector("#simple-tabpanel-card")).should(exist, ofSeconds(waitTime))
        //что она в статусе "В обработке"
        checkICToolIsStatus("В обработке", waitTime)
        //и что это именно так карточка которую мы только что создали
        checkICToolsDopInfo("AutoTest PMI 0150 $dateTime", waitTime)
        element(byCssSelector("input#upload-file")).uploadFile(File("/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/test.pdf"))
        element(byXpath("//div[@role='alert']//*[text()='Файл загружен']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//div[@role='alert']//*[@name='snackbarClose']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@id='files']//*[text()='test.pdf']"))
            .should(exist, ofSeconds(waitTime))
        //кликаем по иконке происшествий в боковом меню
        //Переходим в "Список происшетвий"
        //Переходим во вкладку "Работа с ДДС"
        element(byXpath("//span[text()='Работа с ДДС']/ancestor::button")).click()
        //жмем "добавить"
        //element(byCssSelector("div#simple-tabpanel-hotlines button")).click()
        element(byXpath("//span[text()='Выбрать ДДС']/ancestor::button")).click()
        Thread.sleep(150)
        //выбираем ДДС-02 г.Черкесск
        element(byXpath("//*[text()='ДДС ЭОС']/ancestor::div[@id='panel1a-header']")).click()
        Thread.sleep(150)
        element(byXpath("//*[text()='AutoTest dds-01 1']/ancestor::div/div/label//input")).click()
        Thread.sleep(150)
        element(byXpath("//span[text()='Назначить']/ancestor::button")).click()
        Thread.sleep(150)
        element(byText("AutoTest dds-01 1")).should(exist, ofSeconds(waitTime))
        element(byText("AutoTest PMI 0150 $dateTime")).should(exist, ofSeconds(waitTime))

        logoffTool()

    }

}