# 🎫 Ticketing System

Spring Boot 기반의 티켓 예매 및 관리 시스템입니다.

## 📋 프로젝트 개요

이 프로젝트는 공연이나 이벤트의 티켓을 예매하고 관리할 수 있는 RESTful API 서버입니다. 사용자는 티켓을 예매하고, 취소하며, 예매 상태를 확인할 수 있습니다.

## 🏗️ 기술 스택

- **Backend Framework**: Spring Boot 3.5.4
- **Language**: Java 17
- **Database**: MariaDB
- **ORM**: Spring Data JPA (Hibernate)
- **Build Tool**: Gradle
- **Lombok**: 코드 간소화
- **Testing**: JUnit 5

## 📁 프로젝트 구조

```
ticketing/
├── build.gradle                 # Gradle 빌드 설정
├── gradle/                      # Gradle Wrapper
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew                      # Gradle Wrapper 실행 스크립트 (Linux/Mac)
├── gradlew.bat                  # Gradle Wrapper 실행 스크립트 (Windows)
├── settings.gradle              # Gradle 프로젝트 설정
└── src/
    ├── main/
    │   ├── java/
    │   │   └── ticket/test/
    │   │       ├── TestApplication.java          # Spring Boot 메인 클래스
    │   │       └── ticketing/
    │   │           ├── TicketingController.java  # REST API 컨트롤러
    │   │           ├── TicketingService.java     # 비즈니스 로직 서비스
    │   │           ├── TicketingRequestDto.java  # 요청 DTO
    │   │           ├── TicketingResponseDto.java # 응답 DTO
    │   │           ├── TicketingServer.java      # 서버 설정
    │   │           └── db/
    │   │               ├── Ticket.java           # 티켓 엔티티
    │   │               └── TicketRepository.java # 데이터 접근 계층
    │   └── resources/
    │       └── application.properties            # 애플리케이션 설정
    └── test/
        └── java/
            └── ticket/test/
                └── TestApplicationTests.java     # 테스트 코드
```

## 🚀 주요 기능

### 1. 티켓 예매 (`POST /ticket`)

- 사용자가 원하는 공연의 티켓을 예매할 수 있습니다.
- 요청 시 사용자 ID, 공연 ID, 좌석 정보가 필요합니다.

### 2. 티켓 취소 (`POST /cancel`)

- 예매한 티켓을 취소할 수 있습니다.
- 요청 시 티켓 ID와 사용자 정보가 필요합니다.

### 3. 티켓 조회 (`GET /check/{id}`)

- 티켓 ID를 통해 예매 정보를 조회할 수 있습니다.

## 🗄️ 데이터베이스 스키마

### Ticket 테이블

- `ticketId`: 티켓 고유 식별자 (Primary Key)
- `userId`: 사용자 ID (Primary Key)
- `showId`: 공연 ID (Primary Key)
- `seat`: 좌석 정보
- `bookTime`: 예매 시간

## ⚙️ 설정

### 데이터베이스 설정

- **Host**: localhost:3306
- **Database**: test
- **Username**: root
- **Password**: 0000
- **Driver**: MariaDB JDBC Driver

### 서버 설정

- **Port**: 3000
- **Application Name**: test

## 🛠️ 설치 및 실행

### 사전 요구사항

- Java 17 이상
- MariaDB 10.x 이상
- Gradle (또는 Gradle Wrapper 사용)

### 1. 프로젝트 클론

```bash
git clone <repository-url>
cd ticketing
```

### 2. 데이터베이스 설정

MariaDB에서 `test` 데이터베이스를 생성하고, `application.properties`의 데이터베이스 연결 정보를 확인하세요.

### 3. 애플리케이션 실행

```bash
# Gradle Wrapper 사용
./gradlew bootRun

# 또는 빌드 후 실행
./gradlew build
java -jar build/libs/ticketing-0.0.1-SNAPSHOT.jar
```

### 4. 테스트 실행

```bash
./gradlew test
```

## 📡 API 엔드포인트

| Method | Endpoint      | Description |
| ------ | ------------- | ----------- |
| POST   | `/ticket`     | 티켓 예매   |
| POST   | `/cancel`     | 티켓 취소   |
| GET    | `/check/{id}` | 티켓 조회   |

## 🔧 개발 환경 설정

### IDE 설정

- IntelliJ IDEA 또는 Eclipse 사용 권장
- Lombok 플러그인 설치 필요

### 빌드 및 실행

```bash
# 개발 모드 실행
./gradlew bootRun

# 테스트 실행
./gradlew test

# 프로덕션 빌드
./gradlew build
```

## 📝 라이센스

이 프로젝트는 MIT 라이센스 하에 배포됩니다.

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해 주세요.
