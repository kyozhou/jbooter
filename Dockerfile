FROM openjdk:8-jdk-alpine
MAINTAINER zhoubin@mirahome.me
EXPOSE 9019
COPY target/ms_report.jar /ms_report.jar
ENTRYPOINT ["java","-jar","/ms_report.jar", "--spring.profiles.active=docker"]