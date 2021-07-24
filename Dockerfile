FROM openjdk:11.0.8
COPY build/libs/sismed_backend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
#CMD ["java", "-jar", "sismed_backend-0.0.1-SNAPSHOT.jar"]