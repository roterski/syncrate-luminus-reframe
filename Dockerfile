FROM openjdk:8-alpine

COPY target/uberjar/syncrate-kee-frame.jar /syncrate-kee-frame/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/syncrate-kee-frame/app.jar"]
