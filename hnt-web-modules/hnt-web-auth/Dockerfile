FROM openjdk:8
VOLUME /tmp
CMD /app
WORKDIR /app
ADD target/auth.jar app.jar
EXPOSE 6902
ENTRYPOINT ["java","-jar","app.jar"]