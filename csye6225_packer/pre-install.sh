sudo apt update
echo "install JDK"
sudo apt -y install default-jdk

echo "install mariadb"
sudo apt -y install mariadb-server 
echo "secure_installation"
sudo mysql_secure_installation <<EOF

y
Wsj13191867918
Wsj13191867918
y
y
y
y
EOF

echo "install maven"
sudo apt -y install maven

echo "------------------------------"
echo "All the dependencies installed"
