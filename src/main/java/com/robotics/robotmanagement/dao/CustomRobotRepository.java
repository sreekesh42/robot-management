package com.robotics.robotmanagement.dao;

import com.robotics.robotmanagement.dao.entity.Robot;

import java.util.List;

public interface CustomRobotRepository {

    List<Robot> findAllActiveRobots();
}
