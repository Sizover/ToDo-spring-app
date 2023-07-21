# Сквозные автотесты

Репозиторий для автоматизированного E2E тестирование КИАП

## Примененные продукты

Тесты написаны на языке kotlin с использованием драйвера управления браузером "selenium webdriver" и расширяющей его надстройкой Selenide.  
Также в проекте используется Apache Commons IO.  
Управление запуском тестов и формирование отчетов реализовано с помощью TestNG.

## Тесты

Расположение непосредственно тестов: testing-e2e/src/test/kotlin

1. Dicts INC 0010 Проверка наличия и работоспособности языка обращения в карточке обращения
2. Dicts INC 0020 Проверка подсказки КП
3. Dicts CP 0010 Создание, изменение, перемещение и удаление пунктов реагирования
4. Labels 0010 Проверка создания, прикрепления к КП и удаления метки
5. Search 0010 Проверка создания, поиска и удаления справочных сущностей некоторых справочников
6. Event INC 5020 Nicholas (проверка возникновения критической ошибки после перехода Аудит -> редактирование МО)
7. Event INC 5030 Предупреждение о ложном вызове актуально месяц
8. Status 0010 Проверка сумарного статуса родительской КП
9. MT_001 Проверка отображения значов на карте при применении различных фильтров карты
10. MT_002 Проверка перехода по координатам места происшествия
11. MT_002 Проверка PM1363~B - Общая ошибка приложения после обновления страницы карты
12. PMI 0010 Проверка загрузки таблиц справочников
13. PMI 0020 Проверка загрузки и иерархии справочника "Типы происшествий"
14. PMI 0030 Проверка наличия Организаций с метками ОМПЛ, ПОО и СЗО в справочнике «Организации»
15. PMI 0080 Проверка наличия КП в различных статусах
16. PMI 0090 Проверка загрузки таблицы «Архив происшествий»
17. PMI 0100 Проверка процедуры авторизации в системе
18. PMI 0110 Регистрация вызова (формирование карточки происшествия)(С прикреплением файлов)
19. PMI 0120 Проверка наличия опросника абонента (заявителя)
20. PMI 0130 Регистрация вызова (формирование карточки происшествия) с проверкой выполнения плана реагирования
21. PMI 0140 Проверка фильтрации карточек происшествия
22. PMI 0150 Назначение карточки происшествия на службу ДДС-ЕДДС с последующей проверкой изменения статуса родительской карточки
23. PMI 0160 Назначение сил и средств в КП
24. PMI 0112 Назначение КП из 112 в КИАП
25. PMI 0180 Просмотр паспортов опасных объектов
26. PMI 0220 Проверка открытия карточек справочных сущностей
27. PMI 0230 Проверка поиска справочных данных
28. PMI 0241 Проверка наличия справочников с иерархической системой классификации
29. PMI 0250 Проверка присвоения и удаления меток в карточке организации
30. PMI 0260 Проверка использования ассоциативных связей-ссылок между объектами справочников
31. PMI 0270 Проверка возможности создания, удаления записей, просмотра детальной истории в справочнике должностных лиц
32. Reports 0010 Проверка формирования отчетов по обращениям
33. Reports 0020 Проверка формирования отчетов по деятельности сотрудников
34. Reports 0030 проверка отчетов по происшествиям с использованием фильтров по пострадавшим, адресу и типу происшествия
35. Reports 0070 Расширенная проверка формирования отчетов
36. KB 0010 Создание и редактирование раздела, добавление и редактирование статьи
37. KB 0020 Проверка фильтров статей БЗ
38. CF 0010 Проверка скачивания и корректности табличного CSV файла

# DevOPS

## Запуск в контейнере
Запуск браузеров для каждой сессии автотестирования осуществляется через [selenoid](https://aerokube.com/selenoid/latest/)
Стенд доступен по адресу [selenoid.kiap.local](http://selenoid.kiap.local/)

## ENV-переменные

### Основные настройки

На текущий момент реализовано согласно схеме предложенной в [этой статье](https://itnext.io/how-to-run-automation-scripts-in-multiple-environments-abc39d11aa20). Т.е. конфигурация/параметры/переменные запуска изложены в testng xml файле, в котором в свою очередь параметры запуска ссылаются на параметры переданные в командной строке виртуальной машины Java, используя системные свойства (-D).

Пример запуска:  
```gradle clean test -Psuite=$SUITE```

Список значений используемых на ТС test  
```ADMIN_LOGIN=autotest_admin;ADMIN_PASSWORD=autotest_admin;ATTACH_FOLDER=./attachFolder;DISABLE_GPU=false;HEADLESS=false;MAIN_LOGIN=a.sizov;MAIN_PASSWORD=a.sizov;NO_SANDBOX=false;URL=https://test.kiap.local/```


* "URL" - ссылка на главную страницу КИАП.  
* "MAIN_LOGIN" - основная УЗ под которой осуществляется вход в КИАП и осуществляются все/основные тестовые манипуляции.  
* "MAIN_PASSWORD" - пароль основной УЗ.  
* "ADMIN_LOGIN" - УЗ администратора, предназначенная для "наведения порядка" в среде запуска тестов.  
* "ADMIN_PASSWORD" - пароль УЗ администратора.  
* "ATTACH_FOLDER" - директория в которую АТ проверяющие загрузку файлов, скачивают файлы.  
* "HEADLESS" - булева настройка включения или выключения безголового режима браузера.  
* "DISABLE_GPU" - булева настройка применения [опции](https://peter.sh/experiments/chromium-command-line-switches/#disable-gpu) запуска браузера "--disable-gpu".  
* "NO_SANDBOX" - булева настройка применения [опции](https://peter.sh/experiments/chromium-command-line-switches/#disable-gpu) запуска браузера "--no-sandbox".
## Архитектура?

Для управления составом запускаемых тестов используется аннотация @org.testng.annotations.Test с передачей параметра groups.  
Параметр groups указывается в файле *testng.xml, запуск которого и запускает настроенную пачку тестов

Одинаковые тесты запускаемые для разных условий реализованы одним тестом в связке с параметром аннотации "dataProvider", указывающим на функцию с аннотацией @DataProvider, передающую в тест пачку параметров.  
Т.о. падение теста на любом из пакетов параметров не остановит проверку на последующих параметрах.

Повторный перезапуск упавших тестов реализован параметром аннотации retryAnalyzer, указывающим на класс содержащий функцию счетчика запусков (Retry.kt).  
Дефолтное значение - 3
