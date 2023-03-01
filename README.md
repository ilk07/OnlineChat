# Сетевой чат


![Сетевой чат](https://www.lorica.net/wp-content/uploads/2020/06/live-chat-customer-service-header.jpg "Сетевой чат")

### Задача
Разработать два приложения для обмена текстовыми сообщениями по сети с помощью консоли (терминала) между двумя и более пользователями.

Первое приложение - сервер чата, должно ожидать подключения пользователей.

Второе приложение - клиент чата, подключается к серверу чата, осуществляет доставку и получение новых сообщений.

Все сообщения должны записываться в file.log как на сервере, так и на клиентах. File.log должен дополняться при каждом запуске, а также при отправленном или полученном сообщении. Выход из чата должен быть осуществлен по команде exit.

### Требования к серверу
- Установка порта для подключения клиентов через файл настроек (например, settings.txt);
- Возможность подключиться к серверу в любой момент и присоединиться к чату;
- Отправка новых сообщений клиентам;
- Запись всех отправленных через сервер сообщений с указанием имени пользователя и времени отправки

### Требования к клиенту
- Выбор имени для участия в чате;
- Прочитать настройки приложения из файла настроек - например, номер порта сервера;
- Подключение к указанному в настройках серверу;
- Для выхода из чата нужно набрать команду выхода - “/exit”;
- Каждое сообщение участников должно записываться в текстовый файл - файл логирования. При каждом запуске приложения файл должен дополняться.

### Требования в реализации
- Сервер должен уметь одновременно ожидать новых пользователей и обрабатывать поступающие сообщения от пользователей;
- Использован сборщик пакетов gradle/maven;
- Код размещен на github;
- Код покрыт unit-тестами.

## Реализация
### Приложение "Сервер чата" ServerApp
Сразу после запуска, сервер ожидает подключения киента и, как только подключение происходит, сооздает отдельный поток (Thread) для взаимодействия клиент-сервер. 

После передачи клиента в поток, сервер запрашивает имя (никнейм) для общения в чате и, после проверки никнейма на уникальность, открывает доступ к чату для пользователя. 

Все сообщения пользователей в чат отправляются на сервер, где помещаются в очередь.
Для отправки сообщений из очереди используется отдельный поток (Thread), в котором и происходит разбор очереди и, соответственно, отправка сообщений.

При поступлении на сервер команды выхода из чата, пользователю больше не отправляются сообщения, потоки и соединение закрываются.

При такой реализации приложения сервера, в работе принимают участие следующие потоки:
- поток сервера, который ожидает подключения пользователей
- поток отправки сообщений пользователям чата
- поток соединения клиент-сервер (отдельный для каждого клиента)

### Приложение "Клиент чата" ClientApp
После старта приложения происходит попытка соединения с сервером. В случае успешного подключения, создается отдельный поток для приема сообщений сервера, в основном потоке приложения идет получение сообщений пользователя (из терминала). 

При такой реализации клиента, в работе принимают участие два потока:
- поток получения сообщений сервера
- поток чтения сообщений пользователя из терминала и отправки сообщений на сервер

## Особенности реализации
Так как у приложений много общего и  работают они совместно, общие кдассы и функционал вынесены в отдельный пакет, которое поставляется как зависимость "service" и отвечает за настройки, установку соединения клиент-сервер, логирование и обмен сообщениями.

### Сервисный модуль "service"
Включает в себя:
- class Tuner - настройки чата и соединений (порт, хост, команду выхода из чата)
- class Message + enum MessageType - объект Сообщения и типы сообщений
- class Logger - логер действий с созранением в отдельный файл
- class Communicator - управляет сокетами, передает сообщения между клиентом и сервером

#### Tuner
Классы ClientApp, ServerApp расширяют **абстрактный класс Tuner**, что позволяет управлять настройками, не перегружая код ClientApp и ServerApp.
**Настройки** Tuner получает из файла <i>settings.txt</i>, который находится в папке <i>resources</i> приложения <i>service</i> (настройки можно менять непосредственно в файле).

#### Communicator
- имплементирует интерфейс clooseable, что позволяет использовать его в блоке try с ресурсами и отдельно не отслеживать закрытие при завершении работы клиента/выходе пользваотеля из чата и т.д.

#### Logger
- реализован с применением паттерна Singleton, исходный объект каласса создается с прменением synchronized блока, что гарантирует работу с единственным экемпляром логера всех приложений пакета.

#### Message и MessageType
- создание сообщений и их типизация. Указание типа сообщения - обязательно, с помощью типов сообщений серверу маршрутизирует обработку. Например, при типе сообщения, указывающего на выход клиента из чата сервер закрывает соединение и прекращает работу с клиентом.  

**Типы сообщений:**
- REQUEST_USER_NAME - запрос имени (никнейма) пользователя
- ACCEPT_USER_NAME - принятие сервером имени (никнейма) пользователя
- USER_NAME - имя пользователя (при отправке от клиента указывает на то, что сообщение никнейм) 
- NAME_USED - имя пользвателя уже используется 
- EXIT_CHAT - выход из чата
- USER_TEXT_MESSAGE - сообщение пользователя (стандартное, отправляется в чат)
- SYSTEM_MESSAGE - системеное сообщение (вывод отправителя не обязателен)