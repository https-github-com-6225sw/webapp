package com.csye6225.cloudwebapp;

import com.csye6225.cloudwebapp.dao.UserDao;
import com.csye6225.cloudwebapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication

public class CloudwebappApplication {

	@Autowired
	DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(CloudwebappApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserDao userDao){
		return runner ->{
			createUser(userDao);
		};
	}

	public void createUser(UserDao userDao){
        try {
            String csvFilePath = "/opt/users.csv";
            //String csvFilePath = "/usr/local/opt/users.csv";
            Connection conn = dataSource.getConnection();
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText = null;
            int count = 0;
            lineReader.readLine();
            while ((lineText = lineReader.readLine()) != null){
                String[] data = lineText.split(",");
                String email = data[2];
                if(userDao.findByEmail(email).isEmpty()) {
                    User newuser = new User();
                    newuser.setFirstName(data[0]);
                    newuser.setLastName(data[1]);
                    newuser.setEmail(data[2]);
                    newuser.setRole("user");
                    newuser.setEnabled(true);
                    //password
                    String hashPassword = new BCryptPasswordEncoder().encode(data[3]);
                    newuser.setPassword("{bcrypt}" + hashPassword);
                    userDao.save(newuser);
                    System.out.println("Saved user. Generated id: " + newuser.getId());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	}

