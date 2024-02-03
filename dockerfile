# https://docs.aws.amazon.com/lambda/latest/dg/java-image.html
FROM public.ecr.aws/lambda/java:17

# Copy function code and runtime dependencies from Gradle layout
COPY app/build/classes/java/main ${LAMBDA_TASK_ROOT}

# Set the CMD to your handler (could also be done as a parameter override outside of the Dockerfile)
CMD ["nhlgameupdatelambda.NhlGameUpdateLambda::handleRequest"]