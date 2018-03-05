FROM openjdk:8-jre-alpine
MAINTAINER Jay

RUN apk --no-cache add curl

ADD build/libs/micro.jar /deployments/

CMD ["java", "-Dspring.profiles.active=docker" ,"-jar", "/deployments/micro.jar"]
