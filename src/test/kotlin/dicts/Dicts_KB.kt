package dicts

import BaseTest
import Retry
import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.exist
import com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.Selectors.byXpath
import com.codeborne.selenide.Selenide.back
import com.codeborne.selenide.Selenide.element
import com.codeborne.selenide.Selenide.elements
import com.codeborne.selenide.Selenide.refresh
import org.junit.jupiter.api.Assertions
import org.openqa.selenium.Keys
import org.testng.annotations.Test
import test_library.alerts.AlertsEnum
import test_library.filters.FilterEnum
import test_library.menu.MyMenu
import java.io.File
import java.time.Duration.ofSeconds
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random

class Dicts_KB: BaseTest() {

    @Test(retryAnalyzer = Retry::class, groups = ["ALL", "KB"])
    fun `KB 0010 Создание раздела, добавление и редактирование статьи`() {
        date = LocalDate.now()
        dateTime = LocalDateTime.now()
        //хардкодный список создаваемых разделов и статей. по статье на раздел. Первый в списке раздел станет родителем для остальных
        val nameOfCategoriesList = listOf<String>("AutoTest KB 0010 Parent", "AutoTest KB 0010 Child")
        //Карта для проверки атрибутов созданной статьи при просмотре
        val articleAttributeMap: LinkedHashMap<String, MutableMap<String, String>> = linkedMapOf()
        //карта атрибутов одной статьи, которая кладется в карту атрибутов всех статей
        val oneArticleAttributeMap: LinkedHashMap<String, String> = linkedMapOf()
        var removalWasArticl = false
        var removalWasCategory = false
        logonTool()
        //сначала удалим статьи по списку, если они есть
        menuNavigation(MyMenu.KB.Articles, waitTime)
        //Ждем нужный заголовок, что бы убедится что мы там где должны
        element(byXpath("//div[@id='dict-title']//h2[text()='Статьи базы знаний']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//table/tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        tableColumnCheckbox("Статья", true, waitTime)
        val columnOfNameArticl = tableNumberOfColumn("Статья", waitTime)
        //для каждого элемента хардкодного списка
        nameOfCategoriesList.forEach { nameOfCategory ->
            //воспользуемся поиском
            tableSearch(nameOfCategory, waitTime)
            element(byXpath("//table/tbody/tr//*[text()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //если поиск успешен, то...
            if (element(byXpath("//table/tbody/tr/td[$columnOfNameArticl]/text()/parent::*[contains(text(),'$nameOfCategory')]")).exists()){
                //т.к. ищем по значениям хардкодного списка, рассчитываем на уникальность результатов
                elements(byXpath("//table/tbody/tr/td[$columnOfNameArticl]/text()/parent::*[contains(text(),'$nameOfCategory')]"))
                    .shouldHave(CollectionCondition.size(1))
                //запоминаем полное имя удаляемого элемента
                val removedName = element(byXpath("//table/tbody/tr/td[$columnOfNameArticl]/text()/parent::*[contains(text(),'$nameOfCategory')]")).ownText
                //удаляем элемент либо из трех точек в строке таблицы, либо из трех точек в карточке элемента
                if (Random.nextBoolean()){
                    //открываем три точки в строке таблицы
                    element(byXpath("//table/tbody/tr//*[text()='$removedName']/ancestor::tr/td[last()]//button"))
                        .click()
                    Thread.sleep(300)
                    //выбираем Удалить в выпавшем списке
                    element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(300)
                    //ждем диалоговое окно
                    element(byXpath("//div[@role='presentation']//div[@role='dialog']//h2[text()='Удалить запись?']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //подтверждаем удаление в диалоговом окне
                    element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    //ожидаем зеленый алерт и закрываем его
                    checkAlert(AlertsEnum.snackbarSuccess, "Запись удалена", true, waitTime)
                } else {
                    //переходим в карточку элемента
                    element(byXpath("//table/tbody/tr/td[$columnOfNameArticl]/text()/parent::*[contains(text(),'$nameOfCategory')]/ancestor::tr"))
                        .click()
                    //ждем карточки
                    element(byXpath("//div[@id='dict-title']//h1[text()='Просмотр статьи']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    element(byXpath("//div[@id='dict-title']//h4[text()='$removedName']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //открываем трехточечное меню
                    element(byXpath("//div[@id='dict-title']//*[@data-testid='MoreHorizIcon']/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(300)
                    //жмем Удалить в выпавшем списке
                    element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    Thread.sleep(300)
                    //ждем диалоговое окно
                    element(byXpath("//div[@role='presentation']//div[@role='dialog']//h2[text()='Удалить запись?']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //подтверждаем удаление в диалоговом окне
                    element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
                //ждем перехода в таблицу
                element(byXpath("//table/tbody/tr//*[text()]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //ищем удаленный элемент
                tableSearch(removedName,waitTime)
                element(byXpath("//table/tbody/tr//*[text()='Нет данных']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //очищаем поиск
                tableSearchClose()
                removalWasArticl = true
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //удалим разделы по списку, если они есть
        //TODO добавить удаление из карточки и добавить его же в хвост (баг 1872)
        //счетчик удаленных элементов
        var again = 0
        menuNavigation(MyMenu.KB.Categories, waitTime)
        //ждем и убеждаемся что мы там где надо
        element(byXpath("//div[@id='dict-title']//h2[text()='Разделы базы знаний']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//table/tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //т.к. родитель не должен удалиться с первого раза, то поставим счетчик удаленных элементов и пока он меньше списка, запускаем удаление по списку
        while (again < nameOfCategoriesList.size){
            nameOfCategoriesList.forEach { nameOfCategory ->
                //ждем
                element(byXpath("//table/tbody/tr//*[text()]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//table/thead/tr/th[1]//*[contains(@name, 'arrow')]/ancestor::button"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //раскрываем всю иерархию
                if (element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).exists()){
                    element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                        .click()
                    element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                        .shouldNot(exist, ofSeconds(waitTime))
                    element(byXpath("//table/thead/tr/th[1]//*[@name='arrowDown']/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                }
                Thread.sleep(300)
                //если интересующий нас элемент существует, то...
                if (element(byXpath("//table/tbody/tr//*[contains(text(),'$nameOfCategory')]/ancestor::tr/td[last()]//button")).exists()){
                    //т.к. ищем по значениям хардкодного списка, рассчитываем на уникальность результатов
                    elements(byXpath("//table/tbody/tr//*[contains(text(),'$nameOfCategory')]/ancestor::tr/td[last()]//button"))
                        .shouldHave(CollectionCondition.size(1))
                    //запоминаем имя удаляемого элемента
                    val removedName = element(byXpath("//table/tbody/tr//*[contains(text(),'$nameOfCategory')]")).ownText
                    //флаг, что рассматриваемый элемент является родителем
                    val parentRersist = element(byXpath("//table/tbody/tr//*[text()='$removedName']/ancestor::tr/td[1]//*[contains(@name,'arrow')]")).exists()
                    //открываем трехточечное меню интересующей строки таблицы
                    element(byXpath("//table/tbody/tr//*[text()='$removedName']/ancestor::tr/td[last()]//button"))
                        .click()
                    Thread.sleep(300)
                    element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    //ждем диалоговое окно
                    element(byXpath("//div[@role='presentation']//div[@role='dialog']//h2[text()='Удалить запись?']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //подтверждаем удаление в диалоговом окне
                    Thread.sleep(300)
                    element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    //если пробуем удалить родителя
                    if (parentRersist){
                        //проверяем ошибку
                        checkAlert(AlertsEnum.snackbarError, "Ошибка удаления записи", true, waitTime)
                        element(byXpath("//table/tbody/tr//*[text()='$removedName']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                    } else {
                        //проверяем подтверждение
                        checkAlert(AlertsEnum.snackbarSuccess, "Запись удалена", true, waitTime)
                        //проверяем исчезновение из таблицы
                        element(byXpath("//table/tbody/tr//*[text()='$removedName']"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        element(byXpath("//table/tbody/tr//*[contains(text(),'$nameOfCategory')]"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        again +=1
                        removalWasCategory = true
                    }
                //если интересующий нас элемент не существует, то...
                } else {
                    again +=1
                }
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Создаем разделы по списку
        nameOfCategoriesList.forEach { nameOfCategory ->
            element(byXpath("//div[@id='dict-title']//*[text()='Добавить раздел']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем заголовок в хлебных крошках
            element(byXpath("//nav//li//*[text()='Добавление раздела']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //ждем заголовок
            element(byXpath("//div[@id='dict-title']//h1[text()='Разделы базы знаний']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //первый элемент списка будет родителем для остальных
            if (nameOfCategory != nameOfCategoriesList[0]){
                //выбираем родительский раздел
                element(byXpath("//form[@novalidate]//label[text()='Родительский тип']/following-sibling::*//input"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//form[@novalidate]//label[text()='Родительский тип']/following-sibling::*//input"))
                    .sendKeys(nameOfCategoriesList[0])
                element(byXpath("//div[@role='presentation']//*[text()='${nameOfCategoriesList[0]}']/text()/ancestor::li"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
                element(byXpath("//div[@role='presentation']"))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//form[@novalidate]//label[text()='Родительский тип']/following-sibling::*//input[@value = '${nameOfCategoriesList[0]}']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //заполняем наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .sendKeys(nameOfCategory)
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input[@value='$nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем шринки
            shrinkCheckTool()
            //создаем
            element(byXpath("//*[text()='Добавить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //проверяем и закрываем успешный алерт
            checkAlert(AlertsEnum.snackbarSuccess, "Запись добавлена", true, waitTime)
            //убеждаемся, что мы в нужном РМ
            element(byXpath("//div[@id='dict-title']//h2[text()='Разделы базы знаний']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            tableColumnCheckbox("Раздел", true, waitTime)
            //проверяем что раздел отображен в таблице, раскрывая родителя, если надо
            if (nameOfCategory != nameOfCategoriesList[0]){
                element(byXpath("//table/tbody/tr/td//text()/parent::*[text()='${nameOfCategoriesList[0]}']/ancestor::tr/td[1]//*[contains(@name,'arrow')]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //table/tbody//*[text()='${nameOfCategoriesList[0]}']/ancestor::tr/td[1]//*[@name='arrowRight']/ancestor::button
                if (element(byXpath("//table/tbody//*[text()='${nameOfCategoriesList[0]}']/ancestor::tr/td[1]//*[@name='arrowRight']/ancestor::button")).exists()){
                    element(byXpath("//table/tbody//*[text()='${nameOfCategoriesList[0]}']/ancestor::tr/td[1]//*[@name='arrowRight']/ancestor::button"))
                        .click()
                    element(byXpath("//table/tbody//*[text()='${nameOfCategoriesList[0]}']/ancestor::tr/td[1]//*[@name='arrowRight']/ancestor::button"))
                        .shouldNot(exist, ofSeconds(waitTime))
                    element(byXpath("//table/tbody//*[text()='${nameOfCategoriesList[0]}']/ancestor::tr/td[1]//*[@name='arrowDown']/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                }
            }
            element(byXpath("//table/tbody/tr/td//text()/parent::*[text()='$nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Редактируем разделы
        val substringForUpdate = " Отредактировано"
        //для каждого элемента списка выполним...
        nameOfCategoriesList.forEach { nameOfCategory ->
            //tableSearchClose()
            //воспользуемся поиском
            tableSearch(nameOfCategory, waitTime)
            //открываем трехточечное меню
            element(byXpath("//table/tbody/tr//*[text()='$nameOfCategory']/ancestor::tr/td[last()]//button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            Thread.sleep(300)
            //выбираем Редактировать в выпавшем списке
            element(byXpath("//div[@role='presentation']//*[text()='Редактировать']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Редактирование раздела']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //редактируем наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //уводим курсор
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .hover()
            //меняем наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input"))
                .sendKeys(Keys.END, substringForUpdate)
            //убеждаемся что поменяли наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input[@value='$nameOfCategory$substringForUpdate']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            shrinkCheckTool()
            //сохраняем изменения
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            checkAlert(AlertsEnum.snackbarSuccess, "Запись обновлена", true, waitTime)
            //ждем
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .shouldNot(exist, ofSeconds(waitTime))
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Просмотр раздела']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем сохранность изменений в режиме просмотра
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input[@value='$nameOfCategory$substringForUpdate' and @disabled]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем минитабличку
            //TODO раскоментить блок проверки минитаблицы (баг 1873)
            if (nameOfCategory == nameOfCategoriesList[0]){
                element(byXpath("//*[text()='Порядок подчиненности']/..//table/tbody/tr[1]//*[text()='$nameOfCategory$substringForUpdate']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } else {
                element(byXpath("//*[text()='Порядок подчиненности']/..//table/tbody/tr[2]//*[text()='$nameOfCategory$substringForUpdate']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //переходим в изменение
            element(byXpath("//*[text()='Изменить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Редактирование раздела']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //редактируем наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .hover()
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input"))
                .sendKeys(Keys.END)
            //удаляем добавленное
            repeat(substringForUpdate.length){
                element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input"))
                    .sendKeys(Keys.BACK_SPACE)
            }
            //проверяем
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input[@value='$nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            shrinkCheckTool()
            //сохраняем
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            checkAlert(AlertsEnum.snackbarSuccess, "Запись обновлена", true, waitTime)
            //ждем
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .shouldNot(exist, ofSeconds(waitTime))
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Просмотр раздела']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем сохранность изменений в режиме просмотра
            element(byXpath("//form[@novalidate]//label[text()='Наименование раздела']/following-sibling::*//input[@value='$nameOfCategory' and @disabled]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //TODO раскоментить блок проверки минитаблицы
//            if (nameOfCategory == nameOfCategoriesList[0]){
//                element(byXpath("//*[text()='Порядок подчиненности']/..//table/tbody/tr[1]//*[text()='$nameOfCategory']"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//            } else {
//                element(byXpath("//*[text()='Порядок подчиненности']/..//table/tbody/tr[2]//*[text()='$nameOfCategory']"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//            }
            //возвращаемся в разделы
            element(byXpath("//nav/ol//*[text()='Разделы']/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //проверяем хлебные крошки и РМ
            element(byXpath("//nav/ol/li[last()]//*[text()='Разделы']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@id='dict-title']//h2[text()='Разделы базы знаний']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//table/tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Переходим в проводник и создаем статьи
        menuNavigation(MyMenu.KB.Explorer, waitTime)
        nameOfCategoriesList.forEach { nameOfCategory ->
            //переходим в папку
            element(byXpath("//*[@aria-label='$nameOfCategory']/../.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='$nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //жмем добавить
            element(byXpath("//*[text()='Добавить статью']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //проверяем заголовок раздела в статье
            element(byXpath("//*[text()='Раздел']/following-sibling::*//*[text()='$nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //заполняем наименование
            element(byXpath("//*[text()='Наименование']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .sendKeys("Статья $nameOfCategory")
            //заполняем содержание
            element(byXpath("//div[@role='textbox']//p[last()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@role='textbox']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .sendKeys("Содержимое статьи $nameOfCategory")
            //добиваем тип происшествия
            inputRandomNew("incidentTypeId-textfield", false, waitTime)
            //добиваем метку
            inputRandomNew("labelsId-textfield", true, waitTime)
            //запоминаем МО, тип происшествия, метку
            val articleMO = element(byXpath("//*[text()='Муниципальные образования']/following-sibling::*//*[text()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .ownText
            val articleIncidentTypeCode = element(byXpath("//*[text()='Типы происшествий']/following-sibling::*//*[text()]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .ownText
            var articleLabels = ""
            elements(byXpath("//*[text()='Метки']/following-sibling::*//*[text()]")).forEach { oneLabelEl ->
                if (!oneLabelEl.ownText.contains('.')){
                    articleLabels = oneLabelEl.ownText
                }
            }
            //Закидываем атрибуты в карту
            oneArticleAttributeMap["Муниципальные образования"] = articleMO
            oneArticleAttributeMap["Типы происшествий"] = articleIncidentTypeCode
            oneArticleAttributeMap["Метки"] = articleLabels
            articleAttributeMap["Статья $nameOfCategory"] = oneArticleAttributeMap.clone() as MutableMap<String, String>
            oneArticleAttributeMap.clear()
            //создаем статью
            element(byXpath("//*[text()='Добавить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //проверяем алерт
            checkAlert(AlertsEnum.snackbarSuccess, "Запись добавлена", true, waitTime)
            //проверяем в каком мы РМ
            element(byXpath("//*[text()='Добавить статью']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='$nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем отобразилась ли статья в папке
            //TODO удалить трайкейтч (баг 1874)
            try {
                element(byXpath("//h6[text()='Статья $nameOfCategory']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            } catch (_:  Throwable) {
                refresh()
                element(byXpath("//h6[text()='Статья $nameOfCategory']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                if (print){
                    println("Созданная статья \"Статья $nameOfCategory\" не отобразилась без обновления страницы")
                }
            }
            //открываем подробности/атрибуты статьи
            element(byXpath("//h6[text()='Статья $nameOfCategory']/ancestor::div[1]/following-sibling::div//*[text()='ОТКРЫТЬ']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//h6[text()='Статья $nameOfCategory']/ancestor::div[1]/following-sibling::div"))
                .click()
            element(byXpath("//h6[text()='Статья $nameOfCategory']/ancestor::div[1]/following-sibling::div//*[text()='ОТКРЫТЬ']"))
                .shouldNot(exist, ofSeconds(waitTime))
            //проверяем что атрибуты именно те что были при создании
            val articleLocator = "//*[text()='СКРЫТЬ']/ancestor::div[1]/preceding-sibling::div//*[text()='%s']/..//*[text()='%s']"
            articleAttributeMap["Статья $nameOfCategory"]!!.keys.forEach{ key ->
                element(byXpath(articleLocator.format(key, articleAttributeMap["Статья $nameOfCategory"]!![key])))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //через хлебные крошки возвращаемся в корневую папку
        element(byXpath("//nav/ol//*[text()='База знаний']/ancestor::li"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
            .click()
        //проверяем хлебные крошки
        element(byXpath("//nav/ol/li[last()]//*[text()='База знаний']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //для каждого элемента списка выполняем...
        nameOfCategoriesList.forEach { nameOfCategory ->
            //проверяем наличие папки
            element(byXpath("//*[@aria-label='$nameOfCategory']/../.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем всплывающую подсказку и счетчик элементов
            element(byXpath("(//*[@aria-label='$nameOfCategory']/../..//p)[last()]"))
                .hover()
            if (nameOfCategory == nameOfCategoriesList[0]){
                element(byXpath("//div[@role='tooltip']//*[contains(text(),'В папке 1 статья и ${nameOfCategoriesList.size - 1} пап')]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                Assertions.assertTrue(element(byXpath("(//*[@aria-label='$nameOfCategory']/../..//p)[last()]")).ownText == nameOfCategoriesList.size.toString())
            } else {
                element(byXpath("//div[@role='tooltip']//*[text()='В папке 1 статья и 0 папок']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                Assertions.assertTrue(element(byXpath("(//*[@aria-label='$nameOfCategory']/../..//p)[last()]")).ownText == "1")
//                element(byXpath("//nav/ol//*[text()='${nameOfCategoriesList[0]}']/ancestor::li"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
//                    .click()
//                element(byXpath("//nav/ol/li[last()]//*[text()='${nameOfCategoriesList[0]}']"))
//                    .should(exist, ofSeconds(waitTime))
//                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //переходим в папку
            element(byXpath("//*[@aria-label='$nameOfCategory']/../.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //открываем статью
            element(byXpath("//h6[text()='Статья $nameOfCategory']/../.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Просмотр статьи']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем РМ
            element(byXpath("//div[@id='dict-title']//h1[text()='Просмотр статьи']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@id='dict-title']//h4[text()='Статья $nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем атрибуты статьи
            articleAttributeMap["Статья $nameOfCategory"]!!.keys.forEach{ key ->
                element(byXpath("//*[text()='$key']/following-sibling::*//*[text()='${articleAttributeMap["Статья $nameOfCategory"]!![key]}']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //загружаем файл
            element(Selectors.byCssSelector("input#upload-file"))
                .uploadFile(File("/home/isizov/IdeaProjects/testing-e2e/src/test/resources/fixtures/Тестовый файл_.docx"))
            //проверяем отображение загруженного файла
            element(byXpath("//*[text()='Файлы']/following-sibling::*//*[text()='Тестовый файл_.docx']"))
                .should(exist, ofSeconds(longWait))
            checkAlert(AlertsEnum.snackbarSuccess, "Файл загружен", true, waitTime)
            //переходим в редактирование статьи
            element(byXpath("//div[@id='dict-title']//*[text()='Изменить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//div[@id='dict-title']//h1[text()='Редактирование статьи']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Редактирование статьи']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //ждем
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //редактируем наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //уводим курсор
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .hover()
            //меняем наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование']/following-sibling::*//input"))
                .sendKeys(Keys.END, substringForUpdate)
            //убеждаемся что поменяли наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование']/following-sibling::*//input[@value='Статья $nameOfCategory$substringForUpdate']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            shrinkCheckTool()
            //сохраняем изменения
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //TODO удалить трайкейтч (баг 1865)
            try {
                checkAlert(AlertsEnum.snackbarSuccess, "Запись обновлена", true, longWait)
            } catch (_:  Throwable) {
                if (print){
                    println("Не получен алерт после сохранения изменений")
                }
            }
            //ждем
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //TODO удалить трайкейтч (баг 1800?)
            try {
                element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                    .shouldNot(exist, ofSeconds(longWait))
            } catch (_:  Throwable) {
                if (print){
                    println("Оператор не переведен в режим просмотра")
                }
                back()
            }
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Просмотр статьи']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем сохранность изменений в режиме просмотра
            element(byXpath("//div[@id='dict-title']//h4[text()='Статья $nameOfCategory$substringForUpdate']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //переходим в изменение
            element(byXpath("//*[text()='Изменить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //ждем
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Редактирование статьи']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //редактируем наименование
            element(byXpath("//form[@novalidate]//label[text()='Наименование']/following-sibling::*//input"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .hover()
            element(byXpath("//form[@novalidate]//label[text()='Наименование']/following-sibling::*//input"))
                .sendKeys(Keys.END)
            //удаляем добавленное
            repeat(substringForUpdate.length){
                element(byXpath("//form[@novalidate]//label[text()='Наименование']/following-sibling::*//input"))
                    .sendKeys(Keys.BACK_SPACE)
            }
            //проверяем
            element(byXpath("//form[@novalidate]//label[text()='Наименование']/following-sibling::*//input[@value='Статья $nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            shrinkCheckTool()
            //сохраняем
            element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //TODO удалить трайкейтч (баг 1865)
            try {
                checkAlert(AlertsEnum.snackbarSuccess, "Запись обновлена", true, longWait)
            } catch (_:  Throwable) {
                if (print){
                    println("Не получен алерт после сохранения изменений")
                }
            }
            //ждем
            element(byXpath("//form[@novalidate]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //TODO удалить трайкейтч (баг 1800 1653?)
            try {
                element(byXpath("//*[text()='Сохранить']/text()/ancestor::button"))
                    .shouldNot(exist, ofSeconds(longWait))
            } catch (_:  Throwable) {
                if (print){
                    println("Оператор не переведен в режим просмотра")
                }
                back()
            }
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='Просмотр статьи']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем сохранность изменений в режиме просмотра
            element(byXpath("//div[@id='dict-title']//h4[text()='Статья $nameOfCategory']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //через хлебные крошки возвращаемся в корневую папку
            element(byXpath("//nav/ol//*[text()='База знаний']/ancestor::li"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .click()
            //проверяем хлебные крошки
            element(byXpath("//nav/ol/li[last()]//*[text()='База знаний']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            if (nameOfCategory == nameOfCategoriesList[0]){
                element(byXpath("//*[@aria-label='${nameOfCategoriesList[0]}']/../.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //удалим статьи по списку, те что создали, если не удаляли перед тестом
        if (!removalWasArticl){
            //сначала удалим статьи по списку, если они есть
            menuNavigation(MyMenu.KB.Articles, waitTime)
            //Ждем нужный заголовок, что бы убедится что мы там где должны
            element(byXpath("//div[@id='dict-title']//h2[text()='Статьи базы знаний']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//table/tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            tableColumnCheckbox("Статья", true, waitTime)
            val columnOfNameArticl = tableNumberOfColumn("Статья", waitTime)
            //для каждого элемента хардкодного списка
            nameOfCategoriesList.forEach { nameOfCategory ->
                //воспользуемся поиском
                tableSearch(nameOfCategory, waitTime)
                element(byXpath("//table/tbody/tr//*[text()]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                //если поиск успешен, то...
                if (element(byXpath("//table/tbody/tr/td[$columnOfNameArticl]/text()/parent::*[contains(text(),'$nameOfCategory')]")).exists()){
                    //т.к. ищем по значениям хардкодного списка, рассчитываем на уникальность результатов
                    elements(byXpath("//table/tbody/tr/td[$columnOfNameArticl]/text()/parent::*[contains(text(),'$nameOfCategory')]"))
                        .shouldHave(CollectionCondition.size(1))
                    //запоминаем полное имя удаляемого элемента
                    val removedName = element(byXpath("//table/tbody/tr/td[$columnOfNameArticl]/text()/parent::*[contains(text(),'$nameOfCategory')]")).ownText
                    //удаляем элемент либо из трех точек в строке таблицы, либо из трех точек в карточке элемента
                    if (Random.nextBoolean()){
                        //открываем три точки в строке таблицы
                        element(byXpath("//table/tbody/tr//*[text()='$removedName']/ancestor::tr/td[last()]//button"))
                            .click()
                        Thread.sleep(300)
                        //выбираем Удалить в выпавшем списке
                        element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        Thread.sleep(300)
                        //ждем диалоговое окно
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']//h2[text()='Удалить запись?']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        //подтверждаем удаление в диалоговом окне
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        //ожидаем зеленый алерт и закрываем его
                        checkAlert(AlertsEnum.snackbarSuccess, "Запись удалена", true, waitTime)
                    } else {
                        //переходим в карточку элемента
                        element(byXpath("//table/tbody/tr/td[$columnOfNameArticl]/text()/parent::*[contains(text(),'$nameOfCategory')]/ancestor::tr"))
                            .click()
                        //ждем карточки
                        element(byXpath("//div[@id='dict-title']//h1[text()='Просмотр статьи']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        element(byXpath("//div[@id='dict-title']//h4[text()='$removedName']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        //открываем трехточечное меню
                        element(byXpath("//div[@id='dict-title']//*[@data-testid='MoreHorizIcon']/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        Thread.sleep(300)
                        //жмем Удалить в выпавшем списке
                        element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        Thread.sleep(300)
                        //ждем диалоговое окно
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']//h2[text()='Удалить запись?']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        //подтверждаем удаление в диалоговом окне
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                    }
                    //ждем перехода в таблицу
                    element(byXpath("//table/tbody/tr//*[text()]"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //ищем удаленный элемент
                    tableSearch(removedName,waitTime)
                    element(byXpath("//table/tbody/tr//*[text()='Нет данных']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //очищаем поиск
                    tableSearchClose()
                }
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //удалим разделы по списку, те что создали, если не удаляли перед тестом
        if (!removalWasCategory){
            //удалим разделы по списку, если они есть
            //счетчик удаленных элементов
            again = 0
            menuNavigation(MyMenu.KB.Categories, waitTime)
            //ждем и убеждаемся что мы там где надо
            element(byXpath("//div[@id='dict-title']//h2[text()='Разделы базы знаний']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//table/tbody"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //т.к. родитель не должен удалиться с первого раза, то поставим счетчик удаленных элементов и пока он меньше списка, запускаем удаление по списку
            while (again < nameOfCategoriesList.size){
                nameOfCategoriesList.forEach { nameOfCategory ->
                    //ждем
                    element(byXpath("//table/tbody/tr//*[text()]"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    element(byXpath("//table/thead/tr/th[1]//*[contains(@name, 'arrow')]/ancestor::button"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                    //раскрываем всю иерархию
                    if (element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button")).exists()){
                        element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                            .click()
                        element(byXpath("//table/thead/tr/th[1]//*[@name='arrowRight']/ancestor::button"))
                            .shouldNot(exist, ofSeconds(waitTime))
                        element(byXpath("//table/thead/tr/th[1]//*[@name='arrowDown']/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                    }
                    Thread.sleep(300)
                    //если интересующий нас элемент существует, то...
                    if (element(byXpath("//table/tbody/tr//*[contains(text(),'$nameOfCategory')]/ancestor::tr/td[last()]//button")).exists()){
                        //т.к. ищем по значениям хардкодного списка, рассчитываем на уникальность результатов
                        elements(byXpath("//table/tbody/tr//*[contains(text(),'$nameOfCategory')]/ancestor::tr/td[last()]//button"))
                            .shouldHave(CollectionCondition.size(1))
                        //запоминаем имя удаляемого элемента
                        val removedName = element(byXpath("//table/tbody/tr//*[contains(text(),'$nameOfCategory')]")).ownText
                        //флаг, что рассматриваемый элемент является родителем
                        val parentRersist = element(byXpath("//table/tbody/tr//*[text()='$removedName']/ancestor::tr/td[1]//*[contains(@name,'arrow')]")).exists()
                        //открываем трехточечное меню интересующей строки таблицы
                        element(byXpath("//table/tbody/tr//*[text()='$removedName']/ancestor::tr/td[last()]//button"))
                            .click()
                        Thread.sleep(300)
                        element(byXpath("//div[@role='presentation']//*[text()='Удалить']/text()/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        //ждем диалоговое окно
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']//h2[text()='Удалить запись?']"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                        //подтверждаем удаление в диалоговом окне
                        Thread.sleep(300)
                        element(byXpath("//div[@role='presentation']//div[@role='dialog']//*[text()='Удалить']/text()/ancestor::button"))
                            .should(exist, ofSeconds(waitTime))
                            .shouldBe(visible, ofSeconds(waitTime))
                            .click()
                        //если пробуем удалить родителя
                        if (parentRersist){
                            //проверяем ошибку
                            checkAlert(AlertsEnum.snackbarError, "Ошибка удаления записи", true, waitTime)
                            element(byXpath("//table/tbody/tr//*[text()='$removedName']"))
                                .should(exist, ofSeconds(waitTime))
                                .shouldBe(visible, ofSeconds(waitTime))
                        } else {
                            //проверяем подтверждение
                            checkAlert(AlertsEnum.snackbarSuccess, "Запись удалена", true, waitTime)
                            //проверяем исчезновение из таблицы
                            element(byXpath("//table/tbody/tr//*[text()='$removedName']"))
                                .shouldNot(exist, ofSeconds(waitTime))
                            element(byXpath("//table/tbody/tr//*[contains(text(),'$nameOfCategory')]"))
                                .shouldNot(exist, ofSeconds(waitTime))
                            again +=1
                            removalWasCategory = true
                        }
                        //если интересующий нас элемент не существует, то...
                    } else {
                        again +=1
                    }
                }
            }
        }
    }


    @Test(retryAnalyzer = Retry::class, groups = ["ALL", "KB"])
    fun `KB 0020 Проверка фильтров статей БЗ`() {
        //путь из папок к каждой конкретной папке
        val categoryRouteMap: MutableMap<String, List<String>> = mutableMapOf()
        //атрибуты статьи
        val mapOfAttArt: MutableMap<String, MutableMap<String, MutableList<String>?>> = mutableMapOf()
        //хардкодный список атрибутов статьи, преимущественно для сокращения кода
        val listOfAttName: List<String> = listOf("Муниципальные образования", "Метки", "Типы происшествий")
        //для работы с фильтрами понадобится соответствие кода и наименования типа происшествия,
        // т.к. в статьях хранится только код, а в фильтре для однозначной навигации понадобится и наименование
        val incidentTypesMap: MutableMap<String, String> = mutableMapOf()
        logonTool()
        menuNavigation(MyMenu.Dictionaries.IncidentTypes, waitTime)
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
        //заполняем карту кодов и наименований
        tableColumnCheckbox("Код;Тип происшествия", true, waitTime)
        val codeColumn = tableNumberOfColumn("Код", waitTime)
        val typeColumn = tableNumberOfColumn("Тип происшествия", waitTime)
        for (tblStr in 1..elements(byXpath("//table/tbody/tr")).size){
            incidentTypesMap.put(element(byXpath("//table/tbody/tr[$tblStr]/td[$codeColumn]//text()/..")).ownText,
                element(byXpath("//table/tbody/tr[$tblStr]/td[$typeColumn]//text()/..")).ownText)
        }
        menuNavigation(MyMenu.KB.Categories, waitTime)
        //ждем и убеждаемся что мы там где надо
        element(byXpath("//div[@id='dict-title']//h2[text()='Разделы базы знаний']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//table/tbody"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //проверяем хлебные крошки
        element(byXpath("//nav/ol/li//*[text()='База знаний']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        element(byXpath("//nav/ol/li[last()]//*[text()='Разделы']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //раскрываем иерархию
        element(byXpath("//table/thead/tr/th[1]//*[contains(@name,'arrow')]/ancestor::button"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        while (element(byXpath("//table/tbody/tr/td[1]//*[@name='arrowRight']")).exists()){
            element(byXpath("//table/thead/tr/th[1]//*[contains(@name,'arrow')]/ancestor::button"))
                .click()
            Thread.sleep(500)
        }
        element(byXpath("//table/tbody/tr/td[1]//*[@name='arrowRight']"))
            .shouldNot(exist, ofSeconds(waitTime))
        //выставляем нужные столбцы
        tableColumnCheckbox("Раздел;Кол-во статей;Кол-во разделов", true, waitTime)
        val colOfNameCat = tableNumberOfColumn("Раздел", waitTime)
        val colOfAmountArt = tableNumberOfColumn("Кол-во статей", waitTime)
        //для всей таблицы построчно выполняем
        for (tblStrNum in 1..elements(byXpath("//table/tbody/tr")).size){
            //запомним уровень вложенности
            val nestingLevel = element(byXpath("//table/tbody/tr[$tblStrNum]/td[1]//text()/..")).ownText.toInt()
            //если вложенность есть и в разделе не 0 статей, то...
            if ((nestingLevel != 1)
                && (element(byXpath("//table/tbody/tr[$tblStrNum]/td[$colOfAmountArt]//text()/..")).ownText != "0")){
                val routeList: MutableList<String> = mutableListOf()
                for (nesting in 1 until nestingLevel){
                    routeList.add(
                        element(byXpath("(//table/tbody/tr[$tblStrNum]/preceding-sibling::tr/td[1]//*[text()='$nesting'])[last()]/ancestor::tr/td[$colOfNameCat]//text()/..")).ownText
                    )
                }
                categoryRouteMap.put(element(byXpath("//table/tbody/tr[$tblStrNum]/td[$colOfNameCat]//text()/..")).ownText, routeList.toList())
            }
        }
        menuNavigation(MyMenu.KB.Explorer, waitTime)
        tableStringsOnPage(50, waitTime)
        //соберем атрибуты тех статей что есть на экране
        cleanFilterByEnum(listOf(), waitTime)
        element(byXpath("//div[@id='kb-card']"))
            .should(exist, ofSeconds(waitTime))
            .shouldBe(visible, ofSeconds(waitTime))
        //для каждой карточки статьи
        for (artNum in 1..elements(byXpath("//div[@id='kb-card']")).size){
            val oneArtAttMap: MutableMap<String, MutableList<String>> = mutableMapOf()
            //имя статьи
            val artName = element(byXpath("//div[@id='kb-card'][$artNum]//h6[2]")).ownText
            //имя раздела, но в списке
            val catNameList = listOf<String>(element(byXpath("//div[@id='kb-card'][$artNum]//h6[1]")).ownText)
            oneArtAttMap.put("Раздел", catNameList.toMutableList())
            //окрываем подробности каждой карточки
            element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .scrollIntoView("{block: \"center\"}")
                .click()
            //ждем
            element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@id='kb-card'][$artNum]//*[text() and not(text()=' ПОДРОБНОСТИ')]"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='Муниципальные образования']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //зачитываем каждый аттрибут и кдалем его в список, а его в карту
            listOfAttName.forEach { attName ->
                val oneAttList: MutableList<String> = mutableListOf()
                if (element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='$attName']")).exists()){
                    for (att in 1..elements(byXpath("//div[@id='kb-card'][$artNum]//*[text()='$attName']/following-sibling::*//*[text()]")).size){
                        oneAttList.add(element(byXpath("(//div[@id='kb-card'][$artNum]//*[text()='$attName']/following-sibling::*//*[text()])[$att]")).ownText)
                    }
                }
                oneArtAttMap.put(attName, oneAttList)
            }
            //статью в карту кладем под уникальным именем
            mapOfAttArt.put("$artName&|&$artNum", oneArtAttMap as MutableMap<String, MutableList<String>?>)
            //закрываем подробности
            element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .scrollIntoView("{block: \"center\"}")
                .click()
            element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                .shouldNot(exist, ofSeconds(waitTime))
            element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
        }
        //выбираем значение каждого аттрибута для применения в фильтре
        listOfAttName.forEach { attName ->
            val rndAttValue = mapOfAttArt.keys.map { mapOfAttArt[it]!!.get(attName) }.flatMap { it!! }.distinct().random()
            val filterEnum = FilterEnum.values().filter { it.filterAlias.fullName == attName}[0]
            //значение атрибута и значение фильтра не всегда одно и тоже, поэтому выкручиваемся
            var filterRndAttValue = ""
            when (attName) {
                "Типы происшествий" -> filterRndAttValue = "$rndAttValue ${incidentTypesMap[rndAttValue]}"
                //&||& символ костыль, что бы фильтр отключил обратно авто-включаемых потомков в фильтре
                "Муниципальные образования" -> filterRndAttValue = "$rndAttValue&||&"
                "Метки" -> filterRndAttValue = rndAttValue
            }
            //устанавливаем фильтр
            setFilterByEnum(filterEnum, filterRndAttValue, waitTime)
            Thread.sleep(1000)
            //убеждаемся что все статьи из ранее зафиксированных, которые соответствуют фильтрам, присутствуют
            mapOfAttArt.keys.filter { mapOfAttArt[it]!![attName]?.contains(rndAttValue) == true }.forEach { artName ->
                element(byXpath("//div[@id='kb-card']//h6[2 and text()='${artName.substringBefore("&|&")}']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //убеждаемся что все статьи из отображенных соответствуют фильтрам
            for (artNum in 1..elements(byXpath("//div[@id='kb-card']")).size){
                //открываем подробности каждой карточки и проверяем наличие атрибута из фильтра и закрываем подробности
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .scrollIntoView("{block: \"center\"}")
                    .click()
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text() and not(text()=' ПОДРОБНОСТИ')]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='Муниципальные образования']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='$attName']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='$attName']/following-sibling::*//*[text()='${rndAttValue.substringBefore("&||&").trim()}']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .scrollIntoView("{block: \"center\"}")
                    .click()
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //выбираем случайную статью из тех что подходят по фильтру, из ранее созданного списка
            val rndArt = mapOfAttArt.keys.filter { mapOfAttArt[it]!![attName]?.contains(rndAttValue) == true }.random()
            //запоминаем раздел статьи
            val rndCatName = mapOfAttArt[rndArt]!!["Раздел"]!![0]
            //если раздел интересуемой статьи лежит в не корне, то... перемещаемся по папкам к нему
            if (!element(byXpath("//div[@id='category']//*[text()='$rndCatName']")).exists()){
                categoryRouteMap[rndCatName]!!.forEach { cat ->
                    element(byXpath("//div[@id='category']//*[text()='$cat']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                }
            }
            //переходим в раздел статьи
            element(byXpath("//div[@id='category']//*[text()='$rndCatName']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
                .scrollIntoView("{block: \"center\"}")
                .click()
            //проверяем наличие статьи
            element(byXpath("//div[@id='kb-card']//h6[2 and text()='${rndArt.substringBefore("&|&")}']"))
                .should(exist, ofSeconds(waitTime))
                .shouldBe(visible, ofSeconds(waitTime))
            //проверяем что все статьи папки соответствуют фильтрам
            for (artNum in 1..elements(byXpath("//div[@id='kb-card']")).size){
                //для каждой карточки статьи открываем подробности, проверяем, закрываем подробности
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .scrollIntoView("{block: \"center\"}")
                    .click()
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text() and not(text()=' ПОДРОБНОСТИ')]"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='Муниципальные образования']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='$attName']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='$attName']/following-sibling::*//*[text()='${rndAttValue.substringBefore("&||&").trim()}']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .scrollIntoView("{block: \"center\"}")
                    .click()
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='СКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .shouldNot(exist, ofSeconds(waitTime))
                element(byXpath("//div[@id='kb-card'][$artNum]//*[text()='ОТКРЫТЬ' and text()=' ПОДРОБНОСТИ']/.."))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
            }
            //возвращаемся в корневую папку одним из двух способов, поднимаясь в папку ".." используя развернутый маршрут к папке
            if (Random.nextBoolean() && categoryRouteMap.keys.contains(rndCatName)){
                //проверяем хлебные крошки
                element(byXpath("//nav//li[last()]//*[text()='$rndCatName']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                categoryRouteMap[rndCatName]!!.reversed().forEach { cat ->
                    element(byXpath("//div[@id='category-up']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                        .click()
                    //проверяем хлебные крошки
                    element(byXpath("//nav//li[last()]//*[text()='$cat']"))
                        .should(exist, ofSeconds(waitTime))
                        .shouldBe(visible, ofSeconds(waitTime))
                }
                element(byXpath("//div[@id='category-up']"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            cleanFilterByEnum(listOf(filterEnum), waitTime)
            } else {
                //или по хлебным крошкам
                element(byXpath("//nav//*[text()='База знаний']/ancestor::li"))
                    .should(exist, ofSeconds(waitTime))
                    .shouldBe(visible, ofSeconds(waitTime))
                    .click()
            }
            //проверяем хлебные крошки
            //TODO раскоментить проверку хлебных крошек (баг 1862)
//            element(byXpath("//nav//li[last()]//*[text()='База знаний']"))
//                .should(exist, ofSeconds(waitTime))
//                .shouldBe(visible, ofSeconds(waitTime))
            //Проверяем отсутствие примененных фильтров
            element(byXpath("//form[@novalidate]//button//button"))
                .shouldNot(exist, ofSeconds(waitTime))
        }
    }
}