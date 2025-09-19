# 환경변수

민감한 환경변수는 다음과 같은 파일에 들어있어야합니다. 

`/resources/.application-env.properties` 

위의 파일은 `.application.properties` 를 불러올 때 같이 불러와집니다.

해당 파일 내 변수들은 다음과 같이 들어있습니다.

---

### server

`server.port` : 서버를 실행할 포트를 지정합니다.

---

### datasource

서버에 연결할 DB를 설정힙니다.

`spring.datasource.url` : DB 내 주소를 지정합니다.

`spring.datasource.username` : DB 내 사용자를 지정합니다. (ex - root)

`spring.datasource.password` : 서버를 실행할 포트를 지정합니다.

---

### redis

서버에 연결할 redis를 설정힙니다.

`spring.redis.host` : 실행한 redis 서버를 불러올 주소를 입력합니다.

`spring.redis.port` : 실행한 redis 서버를 불러올 port 번호를 입력합니다.

