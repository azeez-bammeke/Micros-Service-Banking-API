FROM openjdk:21-slim-bullseye
#Information around who maintains the image
MAINTAINER webnet.com

RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

COPY target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "loans-0.0.1-SNAPSHOT.jar"]

#Docker build command
#docker build . -t abammeke/accounts:0-0-1-snapshot