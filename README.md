# inSpline-patient-api

> InSpline Take-Home Challenge BE-2  
> Stateless API + Graceful Lifecycle + IaC 배포

---

## 빠른 시작

```bash
git clone https://github.com/seunzu/inspline-patient-api.git
cd inspline-patient-api
cp .env.example .env        # JWT_SECRET 입력
docker-compose up --build
\```

로컬 실행 후 → http://localhost:8080/swagger-ui.html

---

## 기술 스택

| 분류 | 선택 |
|---|---|
| Language / Framework | Java 21 / Spring Boot 4.0.5 |
| Database | AWS DynamoDB (Always Free) |
| Cloud | AWS (ap-northeast-2) |
| IaC | Terraform |
| Container | Docker (Multi-stage) |
| CI/CD | GitHub Actions |

---

## 12-Factor App 적용 원칙

| 원칙 | 구현 |
|---|---|
| II. Dependencies | `build.gradle` 완전 명시, `./gradlew`로 글로벌 설치 불필요 |
| V. Build-Release-Run | Multi-stage Dockerfile |
| VI. Processes | JWT Stateless 인증, 서버 사이드 세션 없음 |
| IX. Disposability | Graceful Shutdown (SIGTERM → 15초 대기 → 종료) |

---

## 문서

| 문서 | 내용 |
|---|---|
| [아키텍처](docs/architecture.md) | 시스템 구조, 설계 근거, 패키지 구성 이유 |
| [API 명세](docs/api.md) | 엔드포인트 목록, 요청/응답 형식 |
| [로컬 실행](docs/getting-started.md) | 환경 설정, 실행 방법, 더미 계정 |
| [배포](docs/deployment.md) | IaC, AWS 배포, CI/CD 파이프라인 |
| [트러블슈팅](docs/troubleshooting.md) | 배포 과정에서 만난 문제와 해결 과정 |
| [AI 활용](docs/ai-usage.md) | AI 활용 방법, 무료 크레딧 내역 |

---

## 환경변수

| 변수명 | 설명 |
|---|---|---|
| `JWT_SECRET` | Base64 인코딩된 JWT 서명 키 (`openssl rand -base64 32`) |
| `JWT_EXPIRATION_MS` | JWT 만료 시간 ms (기본값: 3600000) |
| `SPRING_PROFILES_ACTIVE` | `local` 또는 `prod` |