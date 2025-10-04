# 1단계: 빌드
FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# 소스 복사
COPY ../../.. .

# bootJar 빌드
RUN gradle clean bootJar --no-daemon

# 2단계: 실행
FROM openjdk:17-jdk-slim
WORKDIR /app

# 빌드된 JAR 복사sudo systemctl status docker
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트
EXPOSE 3000

# 실행 명령
ENTRYPOINT ["java", "-jar", "app.jar"]