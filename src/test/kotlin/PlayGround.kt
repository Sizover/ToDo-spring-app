


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
        menuNavigation("Отчеты", "По происшествиям", waitTime)
        element(byXpath("//*[text()='Создать отчет']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        addressInput("address", "Карачаево-Черкесская Респ, г Карачаевск", waitTime)
        element(byXpath("//*[text()='Определить адрес']"))
            .should(exist, ofSeconds(waitTime))
        Thread.sleep(10000)
        println("width = ")
        println(element(byXpath("//*[text()='Определить адрес']")).getCssValue("width"))














        element(byXpath("//form[@novalidate]"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//*[text()='Создать карточку']/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@data-testid='incidentTypeId']//input[@id='incidentTypeId-autocomplete']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        element(byXpath("//div[@role='presentation']"))
            .should(exist, ofSeconds(waitTime))
        element(byXpath("//div[@role='presentation']/div/ul/li[1]/div"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        Thread.sleep(1000)
        element(byXpath("//div[@role='presentation']/div/ul//*[text()='П.1.1.1 Авиационное происшествие']/ancestor::li"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        Thread.sleep(1000)
        element(byXpath("//div[@data-testid='incidentTypeId']//input[@id='incidentTypeId-autocomplete']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        Thread.sleep(1000)
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[1]/div/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[2]/div[1]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[2]/div[2]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[2]/div[3]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[3]/div[1]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[3]/div[2]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[3]/div[3]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[4]/div[1]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[4]/div[1]/div/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[4]/div[2]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[4]/div[3]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[5]/div[1]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[5]/div[2]/*")))
        println(elements(byXpath("//div[@role='presentation']/div/ul/li[5]/div[3]/*")))
//        println(elements(byXpath("//div[@role='presentation']//*")))

    }

//    elements(byXpath("//table/tbody/tr[$str]/td[$col][text()]")).let { path1 ->
//        if (path1.size == 1 && path1.get(0).ownText.trim().isNotEmpty()) {
//            path1[0].ownText.trim()
//        }
//    }                                    }
//
//}
//
//val path1 = byXpath("//table/tbody/tr[$str]/td[$col]//*[text()]")
//val path2 = byXpath("//table/tbody/tr[$str]/td[$col]//*[text(sdfasdf)]")
//
//if ((elements(path1)).size == 1)
//&& (element(path1).ownText.trim()
//.isNotEmpty())) {
//    trueValueList.add(element(path1).ownText.trim())
//
//                                    if ((elements(byXpath("//table/tbody/tr[$str]/td[$col][text()]")).size == 1)
//                                        && (element(byXpath("//table/tbody/tr[$str]/td[$col][text()]")).ownText.trim()
//                                            .isNotEmpty())) {
//                                            trueValueList.add(element(byXpath("//table/tbody/tr[$str]/td[$col][text()]")).ownText.trim())
//                                    } else if ((elements(byXpath("//table/tbody/tr[$str]/td[$col]//*[text()]")).size == 1)
//                                        &&(element(byXpath("//table/tbody/tr[$str]/td[$col]//*[text()]")).ownText.trim()
//                                            .isNotEmpty())) {
//                                            trueValueList.add(element(byXpath("//table/tbody/tr[$str]/td[$col]//*[text()]")).ownText.trim())
//                                    }

//}


//    @org.testng.annotations.Test (retryAnalyzer = Retry::class)
//    fun main() {
//        val length = "test4321".with() {
//            println(this)
//            this.length
//        }
//        println(length)
//    }

}