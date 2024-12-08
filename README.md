# plantguide

## Описание
Учебный проект по созданию web-приложения с помощью технологии Spring boots.
Приложение представляет собой интерфейс для работы с базой данных.

## Работа с проектом
- сборка
```
docker-compose -p plant build
```
- запуск:
```
docker-compose -p plant up -d
```
docker compose поднимает 3 технологии: postgres на 5432 порте, pgadmin для работы с базой на 8000 порте, и само spring приложение на 8080 порте.
- после запуска в pgAdmin создать сервер host: postgres_db; database: plantguide; user / password: postgres;
- запустить sql скрипты для создания и заполнения;

- опустить:
```
docker-compose -p plant down
```
