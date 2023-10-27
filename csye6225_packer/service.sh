sudo groupadd csye6225
sudo useradd -s /bin/false -g csye6225 -d /opt/csye6225 -m csye6225
sudo chown csye6225:csye6225 /opt/artifact
sudo chown csye6225:csye6225 /opt/artifact/cloudwebapp-0.0.1-SNAPSHOT.jar
sudo chown csye6225:csye6225 /etc/systemd/system/service.env
sudo chown csye6225:csye6225 /etc/systemd/system/app.service
sudo chown csye6225:csye6225 /opt/users.csv
sudo chmod 744 /opt/users.csv
sudo chmod 744 /etc/systemd/system/app.service
sudo chmod 744 /etc/systemd/system/service.env
sudo chmod 744 /opt/artifact/cloudwebapp-0.0.1-SNAPSHOT.jar

sudo systemctl daemon-reload
sudo systemctl enable app.service
sudo systemctl start app.service