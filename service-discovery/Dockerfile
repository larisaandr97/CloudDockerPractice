FROM maven:3.8.3-amazoncorretto-17

WORKDIR ./service-discovery
COPY . .
RUN mvn clean install -e

CMD mvn spring-boot:run
