


import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.text
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.openqa.selenium.Keys
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime

class JustTests {
    val tools = Tools()
    var date = LocalDate.now()
    var dateTime = LocalDateTime.now()
    var waitTime: Long = 5

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `T 0010`() {
        //проверим создание метки и прикрепление метки к происшествию, возможно с удалением метки из КИАП
        date = LocalDate.now()
        dateTime = LocalDateTime.now()
        tools.logonTool()
        tools.menuNavigation("Справочники", "Метки", waitTime)
        //воспользуемся поиском, что бы найти созданную метку не удаленную в упавший проход
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("input[placeholder]")).sendKeys("AutoTest", Keys.ENTER)
        Thread.sleep(500)
        val menuColumn = elements(byXpath("//thead/tr/th")).size
//        val nameColumn = tools.numberOfColumn("Метка", waitTime)
        if (elements(byXpath("//tbody/tr//*[text()='Нет данных']")).size == 0){
            while (elements(byXpath("//tbody/tr//*[text()='Нет данных']")).size == 0) {
                val removedLadel = element(byXpath("(//tbody/tr/td[1]//*[text()])[last()]")).ownText.toString()
                //открываем трехточечное меню
                element(byXpath("(//tbody/tr/td[$menuColumn]//button)[last()]")).click()
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
                element(byXpath("//tbody/tr/td//*[text()='$removedLadel']"))
                    .shouldNot(exist, ofSeconds(waitTime))
            }
            //убеждаемся что удалили
            element(byXpath("//tbody/tr//*[text()='Нет данных']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        val labelList = listOf<String>("AutoTest", "Child label")
        val labelListName = mutableListOf<String>()
        labelList.forEach {
            //жмем добавить метку
            element(byXpath("//span[text()='Добавить метку']/parent::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//label[text()='Родительский тип']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            if (it == "Child label") {
                element(byCssSelector("input#parent")).click()
                element(byXpath("//body/div[@role='presentation']//*[contains(text(),'AutoTest')]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            }
            //вводим имя метки
            element(byCssSelector("input#title")).click()
            element(byCssSelector("input#title")).sendKeys(it)
            //запоминаем образец метки, что бы убеждаться в её создании
            val labelSample = element(byXpath("//label[text()='Предварительный просмотр']/following-sibling::div//*[text()]")).ownText
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
//            labelListName.add(labelSample.replace(" ", "_"))
            labelListName.add(labelSample)
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //добавляем описание
            element(byCssSelector("textarea[name='description']")).click()
            element(byCssSelector("textarea[name='description']")).sendKeys("Метка \"$labelSample\" создана автотестом и должна быть удалена им же")
            //выбираем цвет индиго (случайно?)
            val elementsCollection = elements(byCssSelector("input[value][type='radio']"))
            val colorList = mutableListOf<String>()
            elementsCollection.forEach { colorList.add(it.getAttribute("value").toString()) }
            element(byCssSelector("input[value='${colorList.random()}']")).click()
//            element(byCssSelector("input[value='indigo']")).click()
            //создаем
            element(byXpath("//span[text()='Добавить']/parent::button")).click()
            //проверяем наличие созданной метки
            element(byXpath("//tr/td//*[contains(text(),'$labelSample')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//tr/td[text()='Метка \"$labelSample\" создана автотестом и должна быть удалена им же']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        tools.logoffTool()
        Thread.sleep(500)
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Регистрируем КП
        tools.logonTool()
        tools.menuNavigation("Происшествия","Создать карточку", waitTime)
        tools.firstHalfIC("T 0010",date.toString(),dateTime.toString(),waitTime)
        element(byXpath("//span[text()='Создать карточку']/parent::button")).click()
        tools.inputRandomNew("incidentTypeId-textfield", false, waitTime)
        //добавляем метку при создании КП
        //т.к. меток может добавится много, нам придется проверить все на совпадение с создаваемыми

        val createdLabel = mutableListOf<String>()
        var again: Boolean
        do {
            again = false
            tools.inputRandomNew("labelsId-textfield", true, waitTime)
            repeat(element(byXpath("//input[@name='labelsId-textfield']")).getAttribute("value")!!.length){
                element(byXpath("//input[@name='labelsId-textfield']")).sendKeys(Keys.BACK_SPACE)
            }
            val labelElementsCollection = elements(byXpath("//label[text()='Метки']/..//span[@style='line-height: 1;']//span[text()]"))
            labelElementsCollection.forEach {
                createdLabel.add(it.ownText)
            }
            labelListName.forEach{
                if (createdLabel.contains(it)){
                    again = true
                }
            }
            if (again){
                createdLabel.forEach {
                    element(byXpath("//label[text()='Метки']/..//span[text()='$it']/../*[name()='svg']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    element(byXpath("//label[text()='Метки']/..//span[text()='$it']/../*[name()='svg']"))
                        .shouldNot(exist, ofSeconds(waitTime))
                }
            }
        } while (again)
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
            .shouldHave(text("AutoTest T 0010 $dateTime"), ofSeconds(waitTime))
        createdLabel.forEach {
            element(byXpath(" //span[contains(text(),'$it')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        //добавляем метку
        element(byXpath("//h3[text()='Метки']/following-sibling::span/button")).click()
        element(byCssSelector("input[name='labelsId-textfield']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//body/div[@role='presentation']//ul/li[1]/div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        labelListName.forEach {
            element(byXpath("//body/div[@role='presentation']//*[text()='$it']")).click()
            element(byXpath("//div[@role='combobox']//*[text()='$it']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        element(byXpath("//span[text()='Сохранить']/parent::button")).click()
        //проверяем что все метки на месте
        labelListName.forEach {
            element(byXpath(" //span[contains(text(),'$it')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        createdLabel.forEach {
            element(byXpath(" //span[contains(text(),'$it')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        tools.logoffTool()
        //перезалогиниваемся, что бы исключить кеширование и расширить тест
        Thread.sleep(500)
        tools.logonTool()
        tools.menuNavigation("Справочники", "Метки", waitTime)
        //воспользуемся поиском, что бы найти созданную метку
        element(byXpath("//*[@name='search']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("input[placeholder]")).sendKeys("AutoTest", Keys.ENTER)
//        element(byXpath("//*[contains(text(),'1 строка')]"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
        Thread.sleep(500)
        if (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0){
            while (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0) {
                val removedLabel = element(byXpath("(//tbody/tr/td[1]//*[text()])[last()]")).ownText.toString()
                //открываем трехточечное меню
                element(byXpath("(//tbody/tr//button)[last()]")).click()
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
                element(byXpath("//tbody/tr/td[1]//*[text()='$removedLabel']"))
                    .shouldNot(exist, ofSeconds(waitTime))
            }
        }
        //убеждаемся что удалили
        element(byXpath("//table/tbody/tr//*[text()='Нет данных']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
    }

//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `T 0020`() {
        //создадим пару МО, один оставив навсегда, а второй создавая и удаляя каждый раз
        val moATItWas = mutableListOf<String>()
        var moATCreated = mutableListOf<String>("AutoTest T 0020 МО")
        tools.logonTool()
        tools.menuNavigation("Справочники","Муниципальные образования", waitTime)
        tools.checkbox("", true, waitTime)
        Thread.sleep(1000)
        //внесем существующие телефонные коды в отдельный список
        var telCodeElementsCollection = elements(byXpath(""))
        val telCodeList = mutableListOf<String>()
        var telCodeColumnIndex = 10
        //Выясняем в каком столбце хранятся телефонные коды
        val telCodeElements = elements(byXpath("//thead/tr/th"))
        telCodeElements.forEachIndexed{index, element ->
            if (element.ownText == "Телефонный код"){
                telCodeColumnIndex = index + 1
            }
        }
//        for (i in 1..elements(byXpath("//thead/tr/th")).size){
//            if (element(byXpath("//thead/tr/th[$i]//*[text()]")).ownText == "Телефонный код"){
//                telCodeColumnIndex = i
//            }
//        }
        //Внесем телефонные коды в отдельный список
        do {
            telCodeElementsCollection = elements(byXpath("//tbody/tr/td[$telCodeColumnIndex]//*[text()]"))
            telCodeElementsCollection.forEach {
                telCodeList.add(it.ownText)
            }
            //побеждаем пагинацию, проходясь по всему справочнику и собирая телефонное коды
            val pagesLi = elements(byXpath("//nav/ul/li")).size
            if (!element(byXpath("//nav/ul/li[${pagesLi - 2}]/div")).getAttribute("style")?.contains("color: blue")!!){
                element(byXpath("//nav/ul/li[${pagesLi - 1}]/div")).click()
            }
            Thread.sleep(1000)
        }
        while (!element(byXpath("//nav/ul/li[${pagesLi - 2}]/div")).getAttribute("style")?.contains("color: blue")!!)
        //воспользуемся поиском что бы найти МО с "AutoTest" в названии
        element(byCssSelector("button[data-testid='Поиск-iconButton']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byCssSelector("input[placeholder]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byCssSelector("input[placeholder]")).sendKeys("AutoTest", Keys.ENTER)
        Thread.sleep(1000)
        //если они есть, то составляем их список, не удаляем из него "AutoTest Основное МО" (что бы еще раз не искать его потом)
        // и поштучно удаляем все МО из списка
        if (elements(byXpath("//tbody/tr//div[text()='Нет данных']")).size == 0) {
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
            tools.addressInput("address","г Краснодар, ул Путевая",waitTime)
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
}