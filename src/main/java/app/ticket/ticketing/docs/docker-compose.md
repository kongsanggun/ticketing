# Docker Compose

프로젝트 내 다양한 서비스를 한번에 빌드 및 실행하기 위하여 Docker Compose를 작성하게 되었습니다. 

개발 기준 다음과 같은 터미널 명령어로 실행 가능합니다.

`docker-compose up --build` : docker-compose 실행

`docker-compose down -v` : docker-compose 종료

---

### 구성요소

DB 컨테이너 - container_name: mariadb

redis 컨테이너 - container_name: redis

server 컨테이너 -  container_name: server

---

### 환경변수

docker compose에서 실행한 컨테이너가 무사히 실행하기 위해서 지정된 환경변수를 설정해주어야합니다.

`./.env` : 환경변수 파일 위치

해당 파일 내에서는 다음과 같은 변수가 있어야합니다. (`/resources/.application-env.properties` 에 들어있는 DB 환경변수와 맞게 설정하는 것을 권장함)

`MARIADB_USER` : DB 컨테이너 빌드 시 추가 할 User 설정 

`MARIADB_ROOT_PASSWORD` : DB 컨테이너 빌드 시 설정할 비밀번호 설정

`MARIADB_DATABASE` : DB 컨테이너 빌드 시 설정할 데이터베이스 설정