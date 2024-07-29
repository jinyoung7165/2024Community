FROM openjdk:11-jre
COPY build/libs/*.jar ./app.jar
COPY src/main/resources/application.yml application.yml
COPY src/main/resources/application-local.yml application-local.yml
COPY src/main/resources/application-oauth.yml application-oauth.yml

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=local"]