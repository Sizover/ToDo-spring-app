


import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.openqa.selenium.Keys
import test_library.filters.FilterEnum
import test_library.menu.MyMenu
import java.io.File
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class PlayGround : BaseTest(){





//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `Черновик теста на создание МО`() {
        //создадим пару МО, один оставив навсегда, а второй создавая и удаляя каждый раз
    val moATItWas = mutableListOf<String>()
    val moATCreated = mutableListOf<String>("AutoTest T 0020 МО")
    logonTool()
    menuNavigation(MyMenu.Dictionaries.Municipalities, waitTime)
    tableCheckbox("", true, waitTime)
    Thread.sleep(1000)
    //внесем существующие телефонные коды в отдельный список
//       var telCodeElementsCollection = elements(byXpath(""))
    val telCodeList = mutableListOf<String>()
    //Выясняем в каком столбце хранятся телефонные коды
    val telCodeColumnIndex = tableNumberOfColumn("Телефонный код", waitTime)
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
        //воспользуемся поиском, что бы найти МО с "AutoTest" в названии
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
            val moNameColumn = tableNumberOfColumn("Наименование", waitTime)
            val moParentNameColumn = tableNumberOfColumn("Субъект", waitTime)

        }
        //если они есть, то составляем их список, не удаляем из него "AutoTest Основное МО" (что бы еще раз не искать его потом)
        // и поштучно удаляем все МО из списка
        if (elements(byXpath("//table/tbody/tr//*[text()='Нет данных']")).size == 0) {
            val autotestMOElementsList = elements(byXpath("//tbody/tr/td[1]//*[contains(text(),'AutoTest')]"))
            //можно при наполнении moATItWas через else if проверить на соответствие заголовка МО "AutoTest Основное МО"
            // и удалить его из moAtCreated в таком случае
            //или просто потом проверить есть ли "AutoTest Основное МО" в moATItWas, и если нет положить в moAtCreated
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

        repeat(100){
//            val r = test2().generatelastNameF()
//            val rr = test2().generatefirstNameI()
//            val rrr = test2().generatemiddleNameO()
            println("${generateLastNameF()} ${generateFirstNameI()} ${generateMiddleNameO()}")
        }

//    logonTool()
//        tableCheckbox("ЧЧИД", true, waitTime)
//        logonTool()
//        menuNavigation("Происшествия", "Список происшествий", waitTime)
//        Thread.sleep(100)
//        setFilterByName("Дата регистрации", LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString()+";", waitTime)
        //setDateFilter("Дата регистрации", LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString(), "", waitTime)
//        while (true) {
//            println(
//                element(byXpath("//div[@role='presentation']//*[text()='Уровень происшествия']/following-sibling::*//button[1]"))
//                    .getCssValue("background-color")
//            )
//            Thread.sleep(100)
//        }




    }

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `Черновик2`() {

        logonTool()

        Thread.sleep(1000)
        setFilterByEnum(
            FilterEnum.Дата_регистрации,
            "${LocalDate.now().minusMonths(1).minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))};${LocalDate.now().minusMonths(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
            waitTime)
        Thread.sleep(1000)
        cleanFilterByEnum(listOf(), waitTime)
        Thread.sleep(1000)

//        element(byXpath("html/body/div[@role='presentation']//*[text()='Пользователь системы']/ancestor::fieldset//*[text()='Да']/ancestor::label//input/..")).click()
//        Thread.sleep(1000)
//        println(element(byXpath("html/body/div[@role='presentation']//*[text()='Пользователь системы']/ancestor::fieldset//*[text()='Все']/ancestor::label//input/..")).getCssValue("background-color"))
//        cleanFilter("", waitTime)
        //div[@role='presentation']//*[contains(text(),'Источники событий')]/following-sibling::*//*[@name='close']

//        element(byCssSelector("input#parent")).selectedOptionValue  background-color
//        val innerHtml = element(byXpath("//div[@role='presentation']")).innerHtml()
//        println(element(byXpath("//div[@role='presentation']")).innerHtml())



    }

    fun alignFile(inputName: String, lineLength: Int, outputName: String) {
        val writer = File(outputName).bufferedWriter()
        var currentLineLength = 0
        for (line in File(inputName).readLines()) {
            if (line.isEmpty()) {
                writer.newLine()
                if (currentLineLength > 0) {
                    writer.newLine()
                    currentLineLength = 0
                }
            }
            for (word in line.split(Regex("\\s+"))) {
                if (currentLineLength > 0) {
                    if (word.length + currentLineLength >= lineLength) {
                        writer.newLine()
                        currentLineLength = 0
                    }
                    else {
                        writer.write(" ")
                        currentLineLength++
                    }
                }
                writer.write(word)
                currentLineLength += word.length
            }
        }
        writer.close()
    }




}