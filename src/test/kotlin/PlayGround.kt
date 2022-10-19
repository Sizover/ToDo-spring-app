


import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors.byCssSelector
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.back
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.Test
import java.time.Duration
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.LinkedHashMap

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

    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
    fun `Черновик2`() {
        logonTool()
        menuNavigation("Происшествия", "Список происшествий", waitTime)
        element(byXpath("//table[@id='incidents']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//table[@id='incidents']/tbody/tr"))
            .should(exist, ofSeconds(waitTime))
            .click()

//        updateICToolLabels(listOf("AutoTest.Child label", "хз", "random", "ТБО"), waitTime)
        val labelsList = listOf("AutoTest.Child label", "хз", "random", "ТБО")

        val availableLabelsList = mutableListOf<String>()
        val unavailableLabelsList = mutableListOf<String>()
        val randomLabelsList = mutableListOf<String>()
        element(byXpath("//div[@id='labels']//h3[text()='Метки']/following-sibling::span//*[text()='Изменить']/ancestor::button"))
            .click()
        element(byXpath("//div[@id='labels']//h3[text()='Метки']/following-sibling::span//*[text()='Изменить']/ancestor::button"))
            .shouldNot(exist, ofSeconds(waitTime))
        element(byXpath("//div[@id='labels']//*[text()='Добавить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId' and @role='combobox']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Open']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Close']"))
            .should(exist, ofSeconds(waitTime))
        if (elements(byXpath("//body//div[@role='presentation']//li[1]//*[@name='arrowRight']")).size > 0) {
            element(byXpath("//div[@role='presentation']/div/ul/li[1]/div"))
                .should(exist, ofSeconds(waitTime))
                .click()
            element(byXpath("//body//div[@role='presentation']//li[1]//*[@name='arrowDown']"))
                .should(exist, ofSeconds(waitTime))
            Thread.sleep(500)
        }
        val count = elements(byXpath("//div[@role='presentation']/div/ul/li")).size
//        for (i in 1..count ){
//            if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).size == 1){
//                if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[name()='svg' and @name='checkboxNormal']")).size == 1){
//                    availableLabelsList.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
//                } else if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[name()='svg' and @name='checkboxExpanded']")).size == 1){
//                    unavailableLabelsList.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
//                }
//            }
//        }
//        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Close']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()

        labelsList.forEachIndexed(){index, label ->
            if (index != 0) {
                element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Open']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Close']"))
                    .should(exist, ofSeconds(waitTime))
            }
            for (i in 1..count ){
                if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).size == 1){
                    if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[name()='svg' and @name='checkboxNormal']")).size == 1){
                        availableLabelsList.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
                    } else if (elements(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[name()='svg' and @name='checkboxExpanded']")).size == 1){
                        unavailableLabelsList.add(element(byXpath("//div[@role='presentation']/div/ul/li[$i]//*[text()]")).ownText)
                    }
                }
            }
            if (label == "random" && availableLabelsList.isNotEmpty()){
                val selectedLabel = availableLabelsList.random()
                element(byXpath("//div[@role='presentation']//*[text()='$selectedLabel']/ancestor::li"))
                    .click()
                element(byXpath("//div[@role='presentation']//*[text()='Добавить']/ancestor::button"))
                    .click()
                element(byXpath("//div[@id='labels']//span//*[text()='$selectedLabel']"))
                    .should(exist, ofSeconds(waitTime))
                randomLabelsList.add(selectedLabel)
            } else if (availableLabelsList.contains(label)){
                element(byXpath("//div[@role='presentation']//*[text()='$label']/ancestor::li"))
                    .click()
                element(byXpath("//div[@role='presentation']//*[text()='Добавить']/ancestor::button"))
                    .click()
                element(byXpath("//div[@id='labels']//span//*[text()='$label']"))
                    .should(exist, ofSeconds(waitTime))
            }
            availableLabelsList.clear()
            unavailableLabelsList.clear()
        }
        element(byXpath("//div[@role='presentation']//*[text()='Отменить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@id='labels']//*[text()='Сохранить']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        labelsList.filter { it != "random" }
        val finishLabelList = (labelsList.filter { it != "random" } + randomLabelsList).toList()
        finishLabelList.forEach{
            element(byXpath("//div[@id='labels']//*[text()='$it']/ancestor::span[@title]"))
                .should(exist, ofSeconds(waitTime))
        }


//        element(byXpath("//div[@id='labels']//h3[text()='Метки']/following-sibling::span//*[text()='Изменить']/ancestor::button"))
//            .click()
//        element(byXpath("//div[@id='labels']//h3[text()='Метки']/following-sibling::span//*[text()='Изменить']/ancestor::button"))
//            .shouldNot(exist, ofSeconds(waitTime))
//        element(byXpath("//div[@id='labels']//*[text()='Добавить']/ancestor::button"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId' and @role='combobox']"))
//            .should(exist, ofSeconds(waitTime))
//        element(byXpath("//div[@role='presentation']//div[@data-testid='labelsId']//button[@aria-label='Open']"))
//            .should(exist, ofSeconds(waitTime))
//            .shouldBe(visible, ofSeconds(waitTime))
//            .click()
//        if (elements(byXpath("//body//div[@role='presentation']//li[1]//*[@name='arrowRight']")).size > 0) {
//            element(byXpath("//div[@role='presentation']/div/ul/li[1]/div"))
//                .should(exist, ofSeconds(waitTime))
//                .click()
//            Thread.sleep(500)
//        }

//        println(elements(byXpath("//div[@role='presentation']/div[1]/ul/li[1]/div/*/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[1]/ul/li[2]//*")))
//        println(elements(byXpath("//div[@role='presentation']/div[1]/ul/li[2]/div[2]/div/*[name()='span']/div//*")))
//        println(elements(byXpath("//div[@role='presentation']/div[1]/ul/li[3]/div[1]/div/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[1]/ul/li[3]/div[2]/div/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[1]/ul/li[4]/div[1]/div/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[1]/ul/li[4]/div[2]/div/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[3]/div/div[1]/div/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[3]/div/div[2]/button[1]/*[name()='span'][1]/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[3]/div/div[2]/button[1]/*[name()='span'][2]/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[3]/div/div[2]/button[2]/*[name()='span'][1]/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[3]/div/div[2]/button[2]/*[name()='span'][2]/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[4]/*")))
//        println(elements(byXpath("//div[@role='presentation']/div[5]/*")))

    }

}