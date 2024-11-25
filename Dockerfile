# Используем официальный Maven-образ для сборки проекта
FROM maven:3.9.5-eclipse-temurin-17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем исходники и pom.xml
COPY pom.xml ./
COPY src ./src

# Собираем проект
RUN mvn clean package -DskipTests

# Используем JDK для запуска приложения
FROM eclipse-temurin:17-jdk-jammy

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный jar из предыдущего этапа
COPY --from=build /app/target/*.jar app.jar

# Указываем порт, который будет использовать приложение
EXPOSE 8080

# Команда для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
