FROM openjdk:21
LABEL maintainer="sachin"
WORKDIR /app
RUN ls
RUN pwd
COPY . .
RUN ./mvnw clean package
RUN ls
RUN pwd
RUN cp target/*.jar app.jar
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev", "app.jar"]