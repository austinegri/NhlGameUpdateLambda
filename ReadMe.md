# NhlGameUpdateLambda
Aws lambda function to setup nhl game update function from the NHL API.

### Testing
1. Build with `./gradlew build`
1. `docker build --platform linux/amd64 -t docker-image:test .`
2. `docker run --platform linux/amd64 -p 9000:8080 docker-image:test`
3. `curl "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{"gameId": "2023020672"}'`