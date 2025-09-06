FROM openjdk:17-jdk

WORKDIR /app

COPY ./build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]

CMD ["-jar","./app.jar", "--spring.profiles.active=prod", "-Duser.timezone=Asia/Seoul"]