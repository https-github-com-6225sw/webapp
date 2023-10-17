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



source "amazon-ebs" "my-ami" {
  ami_name      = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  region        = "${var.aws_region}"
  ami_description = "AMI for CSYE 6225"
  // most_recent = true

  ami_regions = [
    "us-east-1",]
       
  ami_users = [
    "603808807036",
       ]

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

  provisioner "shell" {
    script = "./csye6225_packer/pre-install.sh"
  }
  

  provisioner "file" {
    source = "./artifact"
    destination = "/home/admin"
}
provisioner "file" {
    source = "./users.csv"
    destination = "/home/admin/opt/users.csv"
}
}

