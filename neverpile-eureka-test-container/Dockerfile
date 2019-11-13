FROM openjdk:8-jre-alpine

# Configure timezone
RUN apk add -U tzdata
RUN ln -sf /usr/share/zoneinfo/Europe/Berlin /etc/localtime

ADD ./target/dependency/neverpile-eureka-application.war /app.war
ADD application.yml /

#ARG show.Download = true
#ARG show.Lizenz = true

RUN sh -c 'touch /app.war'
EXPOSE 8080
CMD ["java", "-jar", "/app.war", "-Xmx1G"]