FROM openjdk:8-jdk-alpine
MAINTAINER kyozhou@sina.com
EXPOSE 9019
COPY "target/jbooter-1.0.jar" /jbooter.jar
ENTRYPOINT ["java","-jar","/jbooter.jar", "--spring.profiles.active=docker"]