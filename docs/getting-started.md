# 로컬 실행

## 사전 요구사항

- Java 21
- Docker Desktop

## 실행 방법

```bash
# 1. 클론
git clone https://github.com/seunzu/inspline-patient-api.git
cd inspline-patient-api

# 2. JWT_SECRET 생성 및 설정
openssl rand -base64 32   # 출력값을 복사

cp .env.example .env
# .env 파일에 JWT_SECRET=복사한값 입력

# 3. 빌드 및 실행
docker-compose up --build
```

## 더미 계정

| username | password |
|---|---|
| admin | password123 |
| user1 | password123 |

## 동작 확인

```bash
# 헬스체크
curl http://localhost:8080/health

# 로그인 → 토큰 발급
curl -X POST http://localhost:8080/auth/login \
-H "Content-Type: application/json" \
-d '{"username":"admin","password":"password123"}'

# 환자 목록 조회 (토큰 필요)
curl http://localhost:8080/patients \
-H "Authorization: Bearer {발급받은_토큰}"

# Swagger UI
open http://localhost:8080/swagger-ui.html
```

## Graceful Shutdown 시연

```bash
# 터미널 1: 10초짜리 긴 요청 시작
curl "http://localhost:8080/slow?delay=10"

# 터미널 2: 컨테이너 종료 (SIGTERM 전송)
docker ps
docker stop {container_id}

# 기대 로그 순서
# [Shutdown] Shutdown initiated.
# [Shutdown] Draining connections... (max 15s)
# [Slow] Request completed. delay=10s
# [Shutdown] Closed.
```


/health 엔드포인트가 503을 반환하기 시작하면 Graceful Shutdown 진입 상태입니다.