FROM openjdk:11-jre-slim

RUN	apt-get update && apt-get install -y \
	curl \
 && rm -rf /var/lib/apt/lists/*

COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV PORT=8080
ENV DATABASE_HOST=localhost
ENV DATABASE_PORT=3306
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE ${PORT}
CMD ["java","-Dserver.port=${PORT}","-jar","/app.jar"]
