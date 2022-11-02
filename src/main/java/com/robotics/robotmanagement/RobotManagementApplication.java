package com.robotics.robotmanagement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RobotManagementApplication {

	private static final Logger LOGGER = LogManager.getLogger(RobotManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RobotManagementApplication.class, args);
		LOGGER.info("Robot management application started!!");
	}

}
