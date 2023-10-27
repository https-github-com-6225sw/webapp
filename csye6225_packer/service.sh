sudo groupadd csye6225
sudo useradd -s /bin/false -g csye6225 -d /opt/csye6225 -m csye6225

sudo systemctl daemon-reload
sudo systemctl enable app.service
sudo systemctl start app.service