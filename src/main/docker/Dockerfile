FROM java:8

VOLUME /tmp

ADD blockchain-java-1.0-SNAPSHOT.jar app.jar

RUN sh -c 'touch /app.jar'

ENV JAVA_OPTS=""

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom --server.port=", "-Dapp.port=3001", "-jar","/app.jar"]

LABEL maintainer "Patrick Buhagiar"