# API 명세

Base URL: `http://{EC2_IP}:8080`  
로컬: `http://localhost:8080`  
Swagger UI: `/swagger-ui.html`

---

## 인증

### POST /auth/login
JWT 토큰 발급

**Request**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response 200**
```json
{
  "message": "성공",
  "data": {
    "token": "eyJhbGci..."
  }
}
```

**Response 401** - 아이디/비밀번호 불일치
```json
{
  "message": "아이디 또는 비밀번호가 올바르지 않습니다.",
  "data": null
}
```

---

## 환자 (인증 필요)

> Header: `Authorization: Bearer {token}`

### GET /patients
환자 목록 조회

**Response 200**
```json
{
  "message": "성공",
  "data": [
    {
      "id": "patient-001",
      "name": "홍길동",
      "dateOfBirth": "1990-01-15"
    }
  ]
}
```

### GET /patients/{id}
환자 상세 조회

**Response 200**
```json
{
  "message": "성공",
  "data": {
    "id": "patient-001",
    "name": "홍길동",
    "dateOfBirth": "1990-01-15",
    "phone": "010-1234-5678",
    "insuranceNumber": "INS-001"
  }
}
```

**Response 404**
```json
{
  "message": "환자를 찾을 수 없습니다.",
  "data": null
}
```

### POST /patients
환자 생성

**Request**
```json
{
  "name": "홍길동",
  "dateOfBirth": "1990-01-15",
  "phone": "010-1234-5678",
  "insuranceNumber": "INS-001"
}
```

**Response 201**
```json
{
  "message": "성공",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "홍길동",
    "dateOfBirth": "1990-01-15",
    "phone": "010-1234-5678",
    "insuranceNumber": "INS-001"
  }
}
```

---

## 공통 응답

### GET /health
서버 상태 확인 (인증 불필요)

**Response 200** - 정상
```json
{
  "status": "healthy",
  "db": "connected",
  "uptime": 123
}
```

**Response 503** - Graceful Shutdown 진입
```json
{
  "status": "shutting_down",
  "db": "disconnected",
  "uptime": 145
}
```

### GET /slow?delay={n}
Graceful Shutdown 시연용 (인증 불필요)

**Response 200**
```json
{
  "message": "성공",
  "data": "완료. delay=10s"
}
```

---

## 인증 오류 공통 응답

| 상황 | 상태 코드 | 메시지 |
|---|---|---|
| 토큰 없음 | 401 | 인증이 필요합니다. |
| 토큰 만료 | 401 | 토큰이 만료되었습니다. |
| 토큰 변조 | 403 | 토큰이 유효하지 않습니다. |