FROM gradle:7.6-jdk17-alpine as build
ADD . /build
WORKDIR /build
RUN gradle -i build 

FROM openjdk:17-alpine
COPY --from=build /build/build/libs/*.jar /app/
WORKDIR /app
CMD ["java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=prod", "-jar", "timebank-0.0.1.jar"]
