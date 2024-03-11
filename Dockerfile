FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --chown=185 target/*.jar app.jar
EXPOSE 8080
USER 185
ENV JAVA_OPTS="-XX:+UseContainerSupport \
                -XX:InitialRAMPercentage=30.0 \
                -XX:MaxRAMPercentage=75.0 \
                -XX:MinRAMPercentage=50.0 \
                -XX:MaxRAM=512m \
                -XX:+HeapDumpOnOutOfMemoryError \
                -XX:+ExitOnOutOfMemoryError"
#ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
CMD java $JAVA_OPTS -jar app.jar

