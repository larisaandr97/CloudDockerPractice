FROM maven:3.8.3-amazoncorretto-17

WORKDIR ./product
COPY . .
RUN mvn clean install -e

CMD mvn spring-boot:run
