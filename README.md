# 🍏 Calorie Tracker API

**REST API сервис для отслеживания дневной нормы калорий и учета питания**

---

## 📋 Описание проекта

Сервис позволяет:
- Управлять профилями пользователей с автоматическим расчетом дневной нормы калорий
- Вести базу блюд с их пищевой ценностью
- Записывать приемы пищи
- Получать отчеты о питании

---

## 🚀 Функционал

### 👥 Пользователи
- ✅ Создание/обновление/удаление пользователей
- ⚡ Автоматический расчет дневной нормы калорий (формула Харриса-Бенедикта)
- 🔍 Валидация данных (вес, рост, возраст)

### 🍽️ Блюда
- ➕ Добавление/редактирование/удаление блюд
- 📊 Учет калорийности и БЖУ на порцию

### 🍴 Приемы пищи
- 📅 Запись приемов пищи с указанием блюд
- 🧮 Автоматический расчет суммарной пищевой ценности

### 📊 Отчеты
- 📈 Дневной отчет по калориям и БЖУ
- ✅ Проверка соблюдения дневной нормы
- 📅 История питания по дням

---

## 💻 Технологии

| Компонент       | Технология         |
|----------------|-------------------|
| Backend        | Java 17 + Spring Boot 3 |
| База данных    | PostgreSQL 16+     |
| ORM            | Spring Data JPA    |
| Тестирование   | JUnit 5 + Mockito  |

---

## 🛠️ Установка и запуск

### Требования
- JDK 17+
- PostgreSQL 16+
- Gradle 8+


### Шаги установки

1. Создать БД:
```sql
CREATE DATABASE calorie_tracker;
```

2. Настроить подключение в application.properties:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/calorie_tracker
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

3. Запустить приложение:
```
   ./gradlew build
```
### 📊 Структура базы данных

Миграции Flyway находятся в:
```
src/main/resources/db/migration/
```

Основные таблицы:

users - информация о пользователях

dishes - база блюд

meals - приёмы пищи

### Swagger UI
При запуске проекта возможно взаимодействия через Swagger доступный по адресу : [Тык!](http://localhost:8080/swagger-ui.html)

### 📁 Postman коллекция
В проекте доступны 3 коллекции:

DishCollection.postman_collection.json - запросы к блюдам

MealCollection.postman_collection.json - запросы к перекусам

UserCaloriesCollection.postman_collection.json - запросы для пользователей

Метод NormaCcal - эндпоинт показывающий превысил ли лимит дневной нормы пользователь.

HistoryOfDays - для создания отчета по дням.


Импортировать в Postman через:
```
File → Import → Выбрать файл коллекции
```

### 🧪 Тестирование

Запуск всех тестов:
```
./gradlew test
```

### 📈 Миграции Flyway
Скрипты миграций расположены в:
```
src/main/resources/db/migration/
```
Формат именования:
```
V{версия}__{описание}.sql
```

### Контактные данные
Почта: roma.esenkov@yandex.ru
Telegram: @romanesenk
