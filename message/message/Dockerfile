FROM openjdk:21-slim-bullseye
#Information around who maintains the image
MAINTAINER webnet.com

RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# Add the application's jar to the image
COPY target/message-0.0.1-SNAPSHOT.jar message-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "message-0.0.1-SNAPSHOT.jar"]

#Docker build command
#docker build . -t abammeke/message:0-0-1-snapshot