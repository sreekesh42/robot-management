package com.robotics.robotmanagement.web.service;

import com.robotics.robotmanagement.exception.RobotException;
import com.robotics.robotmanagement.web.dto.PhaseCountDto;
import com.robotics.robotmanagement.web.dto.RobotWebDto;

import java.util.List;

public interface RobotService {

    List<RobotWebDto> findAllRobots();

    List<RobotWebDto> findAllActiveRobots();

    RobotWebDto getRobotById(String robotId) throws RobotException;

    RobotWebDto createRobot(RobotWebDto robotWebDto) throws RobotException;

    RobotWebDto updateRobot(RobotWebDto robotWebDto) throws RobotException;

    boolean deleteRobot(String robotId);

    List<PhaseCountDto> getRobotCountPerPhase();

}
