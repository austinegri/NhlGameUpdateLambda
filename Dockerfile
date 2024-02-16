# https://docs.aws.amazon.com/lambda/latest/dg/java-image.html
FROM public.ecr.aws/amazoncorretto/amazoncorretto:21 as base

COPY . .
RUN ./gradlew clean build --stacktrace

FROM public.ecr.aws/lambda/java:21

# Copy function code and runtime dependencies from Gradle layout
# https://hub.docker.com/r/aleph0io/aws-lambda-java
COPY app/build/classes/java/main ${LAMBDA_TASK_ROOT}
COPY app/build/dependency/* ${LAMBDA_TASK_ROOT}/lib/

# Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
CMD ["nhlgameupdatelambda.NhlGameUpdateLambda::handleRequest"]