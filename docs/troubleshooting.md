# 배포 트러블슈팅

> AWS 배포 진행 후 작성 예정
> 예상 항목: CORS 에러, IAM 권한 부족, Security Group 방화벽, 환경변수 미설정

---


## 1. SSH 연결 타임아웃

### 문제
GitHub Actions CD 실행 시 `dial tcp ***:22: i/o timeout` 오류 발생

### 원인
Terraform Security Group의 SSH 인바운드 규칙이 로컬 IP만 허용하도록 설정됨.
GitHub Actions는 GitHub 서버에서 실행되므로 IP가 달라 차단됨.

### 해결
Security Group SSH 규칙을 `0.0.0.0/0`으로 변경 후 `terraform apply` 재실행.
SSH 키 인증 방식이므로 포트를 열어도 키 파일 없이는 접근 불가능.

## 2. IAM 권한 부족 (terraform apply)

### 문제
`terraform apply` 실행 시 `AccessDeniedException` 반복 발생.

### 원인
`terraform-deployer` IAM 사용자에 최소 권한만 부여했으나
Terraform이 내부적으로 호출하는 AWS API 권한이 누락됨.

### 해결
`terraform apply` 실행 후 터미널에 출력되는 에러 메시지에서 누락된 권한을 확인하고
IAM 정책에 추가하는 방식으로 반복 적용.

### 최종 terraform-deployer IAM 정책
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "EC2Access",
      "Effect": "Allow",
      "Action": [
        "ec2:RunInstances",
        "ec2:TerminateInstances",
        "ec2:DescribeInstances",
        "ec2:DescribeSecurityGroups",
        "ec2:CreateSecurityGroup",
        "ec2:DeleteSecurityGroup",
        "ec2:AuthorizeSecurityGroupIngress",
        "ec2:RevokeSecurityGroupIngress",
        "ec2:DescribeVpcs",
        "ec2:DescribeSubnets",
        "ec2:DescribeKeyPairs",
        "ec2:DescribeImages",
        "ec2:CreateTags",
        "ec2:RevokeSecurityGroupEgress",
        "ec2:DescribeNetworkInterfaces",
        "ec2:AuthorizeSecurityGroupEgress",
        "ec2:DescribeInstanceTypes",
        "ec2:DescribeTags",
        "ec2:DescribeInstanceAttribute",
        "ec2:DescribeVolumes",
        "ec2:DescribeInstanceCreditSpecifications"
      ],
      "Resource": "*"
    },
    {
      "Sid": "DynamoDBAccess",
      "Effect": "Allow",
      "Action": [
        "dynamodb:CreateTable",
        "dynamodb:DeleteTable",
        "dynamodb:DescribeTable",
        "dynamodb:DescribeContinuousBackups",
        "dynamodb:DescribeTimeToLive",
        "dynamodb:UpdateTable",
        "dynamodb:TagResource",
        "dynamodb:UntagResource",
        "dynamodb:ListTagsOfResource"
      ],
      "Resource": "*"
    },
    {
      "Sid": "IAMAccess",
      "Effect": "Allow",
      "Action": [
        "iam:CreateRole",
        "iam:DeleteRole",
        "iam:GetRole",
        "iam:PassRole",
        "iam:AttachRolePolicy",
        "iam:DetachRolePolicy",
        "iam:PutRolePolicy",
        "iam:DeleteRolePolicy",
        "iam:GetRolePolicy",
        "iam:CreateInstanceProfile",
        "iam:DeleteInstanceProfile",
        "iam:GetInstanceProfile",
        "iam:AddRoleToInstanceProfile",
        "iam:RemoveRoleFromInstanceProfile",
        "iam:ListInstanceProfiles",
        "iam:ListInstanceProfilesForRole",
        "iam:ListAttachedRolePolicies",
        "iam:ListRolePolicies",
        "iam:TagRole",
        "iam:UntagRole"
      ],
      "Resource": "*"
    },
    {
      "Sid": "SSMReadForAMI",
      "Effect": "Allow",
      "Action": [
        "ssm:GetParameter"
      ],
      "Resource": "arn:aws:ssm:*:*:parameter/aws/service/ami-amazon-linux-latest/*"
    },
    {
      "Sid": "CloudWatchLogsAccess",
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:DeleteLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents",
        "logs:DescribeLogStreams"
      ],
      "Resource": "*"
    }
  ]
}
```

각 권한 부여 근거:

| Sid | 목적 |
|---|---|
| EC2Access | Terraform이 EC2 인스턴스, Security Group 생성/삭제 |
| DynamoDBAccess | Terraform이 DynamoDB 테이블 생성/삭제 |
| IAMAccess | Terraform이 EC2용 IAM Role, Instance Profile 생성/삭제 |
| SSMReadForAMI | Terraform이 최신 AL2023 AMI ID를 SSM에서 동적 조회 |
| CloudWatchLogsAccess | Terraform이 CloudWatch 로그 그룹 생성/삭제 |