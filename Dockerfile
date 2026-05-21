FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY DataMigrator.java /app/
COPY orders.csv /app/

RUN javac DataMigrator.java

CMD ["java", "DataMigrator"]