# Base image 선택 - JDK 21을 포함한 경량 Alpine Linux
FROM eclipse-temurin:21-jdk-alpine

# 빌드 시 전달받는 JAR 파일 경로 (GitHub Actions에서 넘겨받음)
ARG JAR_FILE=build/libs/hwarrk-0.0.1-SNAPSHOT.jar

# 해당 JAR 파일을 Docker 컨테이너 내로 복사
COPY ${JAR_FILE} app.jar

# 포트 설정 (Spring Boot는 기본적으로 8080 포트를 사용)
EXPOSE 8080

# 애플리케이션을 JAR로 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
