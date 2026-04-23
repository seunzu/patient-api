# 배포

## IaC: Terraform

```bash
cd infra/

# 초기화 (최초 1회)
terraform init

# workspace 생성 및 선택
terraform workspace new prod
terraform workspace select prod

# 인프라 생성
terraform apply 

# EC2 IP 확인
terraform output ec2_public_ip

# 인프라 삭제 (과제 완료 후 반드시 실행)
terraform destroy

```

생성되는 리소스: EC2 t2.micro, DynamoDB (patients, users), CloudWatch Log Group, Security Group, IAM Role

## CI/CD: GitHub Actions

main 브랜치에 push 시 자동으로 EC2에 Blue-Green 배포됩니다.

**GitHub Secrets 등록 필요**

| Secret | 값 |
|---|---|
| `DOCKER_USERNAME` | DockerHub 아이디 |
| `DOCKER_PASSWORD` | DockerHub 비밀번호 |
| `DOCKER_REPO` | DockerHub Repository |
| `EC2_HOST` | `terraform output ec2_public_ip` 결과값 |
| `EC2_SSH_KEY` | EC2 SSH 프라이빗 키 전체 내용 |
| `JWT_SECRET` | Base64 인코딩된 JWT 시크릿 |
| `AWS_REGION` | ap-northeast-2 |

**배포 파이프라인**

```
git push (main)
    → Gradle 빌드
    → Docker 이미지 빌드 (linux/amd64) + DockerHub 푸시
    → EC2 SSH 접속
    → Blue-Green 배포 (8081 헬스체크 → 8080 교체)
    → 헬스체크 실패 시 자동 롤백
```

## CloudWatch 로그 확인

AWS 콘솔 → CloudWatch → 로그 그룹 → `/inspline/patient-api`