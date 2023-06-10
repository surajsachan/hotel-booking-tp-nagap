FROM openjdk:11
RUN mkdir /app
WORKDIR /app
COPY target/hotel-booking-0.0.1-SNAPSHOT.jar /app
EXPOSE 8084
CMD ["--spring.profiles.active=gcp"]
ENTRYPOINT ["java", "-jar", "hotel-booking-0.0.1-SNAPSHOT.jar"]