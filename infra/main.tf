terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

locals {
  env           = terraform.workspace
  instance_type = "t2.micro"
  log_group     = "/inspline/patient-api/${local.env}"

  common_tags = {
    Project     = "inspline-patient-api"
    Environment = local.env
    ManagedBy   = "terraform"
  }
}

# AMI: SSM Parameter로 최신 AL2023 동적 참조
data "aws_ssm_parameter" "al2023_ami" {
  name = "/aws/service/ami-amazon-linux-latest/al2023-ami-kernel-default-x86_64"
}

# Security Group
resource "aws_security_group" "patient_api" {
  name        = "patient-api-sg-${local.env}"
  description = "patient-api Security Group (${local.env})"

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Spring Boot API"
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.my_ip]
    description = "SSH - my IP only"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = merge(local.common_tags, {
    Name = "patient-api-sg-${local.env}"
  })
}

# IAM Role
resource "aws_iam_role" "patient_api" {
  name = "patient-api-role-${local.env}"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect    = "Allow"
      Principal = { Service = "ec2.amazonaws.com" }
      Action    = "sts:AssumeRole"
    }]
  })

  tags = merge(local.common_tags, {
    Name = "patient-api-role-${local.env}"
  })
}

# IAM Policy
resource "aws_iam_role_policy" "patient_api" {
  name = "patient-api-policy-${local.env}"
  role = aws_iam_role.patient_api.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Sid    = "DynamoDBAccess"
        Effect = "Allow"
        Action = [
          "dynamodb:GetItem",
          "dynamodb:PutItem",
          "dynamodb:Scan",
          "dynamodb:Query",
          "dynamodb:DeleteItem",
          "dynamodb:UpdateItem",
          "dynamodb:DescribeTable"
        ]
        Resource = [
          aws_dynamodb_table.patients.arn,
          aws_dynamodb_table.users.arn
        ]
      },
      {
        Sid    = "CloudWatchLogsAccess"
        Effect = "Allow"
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "logs:DescribeLogStreams"
        ]
        Resource = "arn:aws:logs:${var.aws_region}:*:log-group:${local.log_group}:*"
      }
    ]
  })
}

# IAM Instance Profile
resource "aws_iam_instance_profile" "patient_api" {
  name = "patient-api-profile-${local.env}"
  role = aws_iam_role.patient_api.name
}

# EC2
resource "aws_instance" "patient_api" {
  ami                    = data.aws_ssm_parameter.al2023_ami.value
  instance_type          = local.instance_type
  vpc_security_group_ids = [aws_security_group.patient_api.id]
  iam_instance_profile   = aws_iam_instance_profile.patient_api.name

  key_name = "patient-api-key"

  user_data = <<-EOF
    #!/bin/bash
    yum install docker -y
    systemctl start docker
    systemctl enable docker
    usermod -aG docker ec2-user
  EOF

  tags = merge(local.common_tags, {
    Name = "patient-api-${local.env}"
  })
}

# DynamoDB: patients
resource "aws_dynamodb_table" "patients" {
  name         = "patients"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "id"

  attribute {
    name = "id"
    type = "S"
  }

  tags = merge(local.common_tags, {
    Name = "patients"
  })
}

# DynamoDB: users
resource "aws_dynamodb_table" "users" {
  name         = "users"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "username"

  attribute {
    name = "username"
    type = "S"
  }

  tags = merge(local.common_tags, {
    Name = "users"
  })
}