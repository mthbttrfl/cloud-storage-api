# Cloud Storage API
Проект реализован в рамках [Java Роадмап Сергея Жукова](https://zhukovsd.github.io/java-backend-learning-course/).
Бэкенд для облачного файлового менеджера. 
Предоставляет REST API для авторизации, навигации по папкам, загрузки, скачивания и удаления файлов. 
Проект является серверной частью приложения, фронтенд доступен в отдельном [репозитории](https://github.com/MrShoffen/roadmap-cloud-storage-frontend).

## Деплой
Доступен по [адресу](http://82.146.32.21), сервер прекратит работу 03.04.26

## Особенности проекта

* Регистрация и аутентификация пользователей (HTTP сессии).
* Просмотр содержимого директорий, навигация по папкам.
* Загрузка файлов и папок (с сохранением структуры).
* Скачивание файлов и папок (архивация на лету).
* Создание новых папок.
* Удаление файлов и папок.
* Подготовлен для работы с готовым фронтендом.

## Совместимость с фронтендом

Бэкенд спроектирован для работы с [фронтендом](https://github.com/MrShoffen/roadmap-cloud-storage-frontend). Для корректной работы API должно соответствовать определенным требованиям.



### Важные правила:
* Корневая папка обозначается пустой строкой "" или "/".

* Имена папок должны оканчиваться слэшем / (например, "name": "photos/").

* Путь (path), если это папка, также должен оканчиваться слэшем(например, "path": "folder1/subfolder/").

### Эндпоинты API и документация:

* При локальном запуске, документация доступна по [адресу](http://localhost:8080/swagger-ui/index.html).
* Если прод сервер работает то, документация доступна по [адресу](http://82.146.32.21:8080/swagger-ui/index.html)

#### Auth
* POST /api/auth/sign-in — вход в систему.
* POST /api/auth/sign-up — регистрация.
* POST /api/auth/sign-out — выход из системы.

#### Directory
* POST /api/directory?path={path} — создать новую папку.
* GET /api/directory?path={path} — получить содержимое папки (не рекурсивно).

#### Resource
* GET /api/resource?path={path} — получить информацию о конкретном файле или папке.
* DELETE /api/resource?path={path} — удалить файл или папку.
* GET /api/resource/download?path={path} — скачать файл или папку (папка отдается как ZIP-архив).
* GET /resource/move?from={from}&to={to} — переименовывать папку или файл.
* * При переименовании меняется только имя файла.
* * При перемещении меняется только путь к файлу.
* GET /resource/search?query={query} — поиск ресурса по имени.
* POST /api/resource?path={path} — загрузка файлов (multipart/form-data).

## Быстрый старт с Docker

### Создание image фронта
1. Клонируйте репозиторий фронтенда:
```bash
    git clone https://github.com/MrShoffen/roadmap-cloud-storage-frontend.git
```
2. Согласно инструкции в репозитории фронта, отредактируйте файлы.
3. Создайте image:
```bash
    docker build --no-cache -t frontend .
```

### Запуск бэкенда
1. Клонируйте репозиторий:
```bash
    git clone https://github.com/mthbttrfl/cloud-storage-api.git
```
2. Создайте файл .env на основе примера или скопируйте переменные из файла ниже.
```env
#Публичный env файл для локального запуска
DB_USER=userdb
DB_PASSWORD=userdb
DB_PORT=5433
DB_URL=jdbc:postgresql://localhost:5433/cloud_storage
DB_DRIVER=org.postgresql.Driver

PGADMIN_EMAIL=userdb@gmail.com
PGADMIN_PASSWORD=userdb
PGADMIN_PORT=5050

MINIO_ROOT_USER=userminio
MINIO_ROOT_PASSWORD=userminio
MINIO_WEB_CONSOLE_PORT=9001
MINIO_URL=http://localhost:9000

REDIS_PASSWORD=userredis
REDIS_PORT=6379
REDIS_HOST=localhost
```
3. Запустите сборку и контейнеры:
```bash
    docker compose -f compose-localhost.yaml up --build
```