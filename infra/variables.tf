variable "aws_region" {
  description = "AWS 리전"
  type        = string
  default     = "ap-northeast-2"
}

variable "my_ip" {
  description = "SSH 접속 허용 IP (내 IP/32 형식)"
  type        = string
  # terraform apply -var="my_ip=$(curl -s ifconfig.me)/32"
}