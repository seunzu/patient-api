output "ec2_public_ip" {
  description = "EC2 Public IP"
  value       = aws_instance.patient_api.public_ip
}

output "ec2_public_dns" {
  description = "EC2 Public DNS"
  value       = aws_instance.patient_api.public_dns
}

output "patients_table_name" {
  description = "DynamoDB patients 테이블명"
  value       = aws_dynamodb_table.patients.name
}

output "users_table_name" {
  description = "DynamoDB users 테이블명"
  value       = aws_dynamodb_table.users.name
}

output "environment" {
  description = "현재 배포 환경"
  value       = terraform.workspace
}