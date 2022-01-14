FROM openjdk:8-jdk-alpine
ADD *.jar cacheLibrary.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","cacheLibrary.jar"]