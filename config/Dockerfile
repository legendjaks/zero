FROM openjdk:8-jre-alpine
MAINTAINER Jay

RUN apk --no-cache add curl

ADD build/libs/config.jar /deployments/

CMD ["java", "-Dspring.profiles.active=native" ,"-jar", "/deployments/config.jar"]
