packer {
  required_plugins {
    amazon = {
      version = ">= 0.0.1"
      source  = "github.com/hashicorp/amazon"
    }
  }
}

variable "aws_region"{
    type = string
    default = "us-east-1" 
}

variable "source_ami"{
    type = string
    default = "ami-06db4d78cb1d3bbf9"
}

variable "ssh_username"{
    type = string
    default = "admin"
}

variable "subnet_id"{
    type = string
    default = "subnet-092bbfc0bd41e12f2"
}

variable "dev_id"{
  type = string
  default = "603808807036"
}

variable "demo_id"{
  type = string
  default = "099917940770"
}

source "amazon-ebs" "my-ami" {
  ami_name      = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  region        = "${var.aws_region}"
  ami_description = "AMI for CSYE 6225"
  // most_recent = true

  ami_regions = [
    "us-east-1",]
       
  ami_users = [
    "${var.dev_id}",
    "${var.demo_id}", ]

   aws_polling {
    delay_seconds = 100
    max_attempts = 30
   }
   
   instance_type = "t2.micro"
   source_ami = "${var.source_ami}"
   ssh_username = "${var.ssh_username}"
   subnet_id = "${var.subnet_id}"

   launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/sda2"
    volume_size           = 10
    volume_type           = "gp2"
   }

   }


build {
  sources = [
    "source.amazon-ebs.my-ami",
  ]

  provisioner "file" {
    source = "./artifact"
    destination = "/home/admin"
  }
  provisioner "file" {
    source = "./opt"
    destination = "/home/admin"
  }

  provisioner "shell" {
    inline = [
      "sudo groupadd csye6225",
      "sudo useradd -s /bin/false -g csye6225 -d /opt/csye6225 -m csye6225",
    ]
  }

  provisioner "shell" {
    inline = ["sudo mv /home/admin/opt/users.csv /opt",
      "sudo mv /home/admin/opt/amazon-cloudwatch-config.json /opt",
      "sudo mv /home/admin/opt/app.service /etc/systemd/system",
      "sudo chown -R csye6225:csye6225 /opt/csye6225",
      "sudo mv /home/admin/artifact /opt/csye6225/",
      "sudo mkdir /opt/csye6225/logs",
    ]
}
  provisioner "shell" {
    scripts = ["./csye6225_packer/pre-install.sh",
      "./csye6225_packer/service.sh",]
  }

  provisioner "shell" {
    scripts = [
      "./csye6225_packer/cloudwatch.sh",
    ]
  }


}

