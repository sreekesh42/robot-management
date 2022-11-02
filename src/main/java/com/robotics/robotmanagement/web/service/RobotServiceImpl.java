package com.robotics.robotmanagement.web.service;

import com.robotics.robotmanagement.dao.CustomRobotRepository;
import com.robotics.robotmanagement.dao.RobotRepository;
import com.robotics.robotmanagement.dao.entity.Robot;
import com.robotics.robotmanagement.exception.ErrorConstants;
import com.robotics.robotmanagement.exception.RobotException;
import com.robotics.robotmanagement.util.CommonUtil;
import com.robotics.robotmanagement.web.constants.LifeCyclePhase;
import com.robotics.robotmanagement.web.dto.PhaseCountDto;
import com.robotics.robotmanagement.web.dto.RobotWebDto;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.robotics.robotmanagement.util.CommonUtil.*;

@Service
@Transactional
public class RobotServiceImpl implements RobotService {

    private static Logger LOGGER = LogManager.getLogger(RobotServiceImpl.class);

    @Autowired
    RobotRepository robotRepository;

    public List<RobotWebDto> findAllRobots() {
        List<Robot> robots = robotRepository.findAll();
        LOGGER.info("robots data from database is"+robots);
        List<RobotWebDto> robotWebDtos = getRobotWebDtos(robots);
        return robotWebDtos;
    }

    @Override
    public List<RobotWebDto> findAllActiveRobots() {
        return robotRepository.findAllActiveRobots()
                .stream()
                .map(eachRobot -> getRobotWebDto(eachRobot))
                .collect(Collectors.toList());
    }

    @Override
    public RobotWebDto getRobotById(String robotId) throws RobotException {
        Robot robot = robotRepository.findByRobotId(robotId);
        if(ObjectUtils.isEmpty(robot)) {
            LOGGER.error("Exception occurred while fetching robot by robotId");
            throw new RobotException(ErrorConstants.ERROR.ROBOT_NOT_FOUND.getErrorCode(), ErrorConstants.ERROR.ROBOT_NOT_FOUND.getErrorMessage());
        }
        RobotWebDto robotWebDto = getRobotWebDto(robot);
        return robotWebDto;
    }

    @Override
    public RobotWebDto createRobot(RobotWebDto robotWebDto) throws RobotException {
        LOGGER.info("Creating robot with details "+robotWebDto);
        if(robotRepository.existsByRobotId(robotWebDto.getRobotId())) {
            throw new RobotException(ErrorConstants.ERROR.ROBOT_ALREADY_EXISTS.getErrorCode(), ErrorConstants.ERROR.ROBOT_ALREADY_EXISTS.getErrorMessage());
        } else {
            LOGGER.info("Robot not present in database. Creating new robot...");
        }
        Robot robot = robotRepository.save(getRobotEntity(robotWebDto));
        LOGGER.info("Robot entity after save is "+robot);
        RobotWebDto robotWebDtoResponse = getRobotWebDto(robot);
        return robotWebDtoResponse;
    }

    @Override
    public RobotWebDto updateRobot(RobotWebDto robotWebDto) throws RobotException {
        Robot robot = robotRepository.findByRobotId(robotWebDto.getRobotId());
        if(ObjectUtils.isEmpty(robot)) {
            LOGGER.error("Robot with the provided robot id do not exist");
            throw new RobotException(ErrorConstants.ERROR.ROBOT_NOT_FOUND.getErrorCode(), ErrorConstants.ERROR.ROBOT_NOT_FOUND.getErrorMessage());
        }
        LOGGER.info("Robot with the provided robot id exists");
        //update robot entity with data from web service layer
        CommonUtil.updateRobotEntityFromWebDto(robot,robotWebDto);
        Robot updatedRobot = robotRepository.save(robot);
        LOGGER.info("Updated robot is : "+updatedRobot);
        RobotWebDto updatedRobotWebDto = getRobotWebDto(updatedRobot);
        return updatedRobotWebDto;
    }

    @Override
    public boolean deleteRobot(String robotId) {
        if(robotRepository.deleteByRobotId(robotId) == 1) {
            LOGGER.info("robot successfully marked "+ LifeCyclePhase.DAMAGED_BEYOND_REPAIR);
            return true;
        } else {
            LOGGER.error("unexpected error occurred while marking robot for "+LifeCyclePhase.DAMAGED_BEYOND_REPAIR);
            return false;
        }
    }

    @Override
    public List<PhaseCountDto> getRobotCountPerPhase() {
        return robotRepository.countRobotsPerPhase();
    }
}
