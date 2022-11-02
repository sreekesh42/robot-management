package com.robotics.robotmanagement.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robotics.robotmanagement.dao.entity.Robot;
import com.robotics.robotmanagement.web.dto.ErrorResponse;
import com.robotics.robotmanagement.web.dto.RobotWebDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sreekesh
 */
public class CommonUtil {

    private static Logger LOGGER = LogManager.getLogger(CommonUtil.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     * @param robots
     * @return
     */
    public static List<RobotWebDto> getRobotWebDtos(List<Robot> robots) {
        LOGGER.info("converting entities to web dtos");
        List<RobotWebDto> robotWebDtos = robots.stream()
                .map(eachRobot -> getRobotWebDto(eachRobot)).collect(Collectors.toList());
        return robotWebDtos;
    }

    /**
     *
     * @param robot
     * @return
     */
    public static RobotWebDto getRobotWebDto(Robot robot) {
        RobotWebDto robotWebDto = RobotWebDto.builder()
                .robotId(robot.getRobotId())
                .name(robot.getName())
                .yearOfManufacture(robot.getYearOfManufacture())
                .mass(robot.getMass())
                .color(robot.getColor())
                .functions(robot.getFunctions())
                .robotPhase(robot.getRobotPhase())
                .build();
        LOGGER.info("converted RobotWebDto is "+robotWebDto);
        return robotWebDto;
    }

    /**
     *
     * @param robotWebDto
     * @return
     */
    public static Robot getRobotEntity(RobotWebDto robotWebDto) {
        LOGGER.info("Converting web dto to entity");
        Robot robot = Robot.builder()
                .robotId(robotWebDto.getRobotId())
                .name(robotWebDto.getName())
                .mass(robotWebDto.getMass())
                .yearOfManufacture(robotWebDto.getYearOfManufacture())
                .color(robotWebDto.getColor())
                .functions(robotWebDto.getFunctions())
                .robotPhase(robotWebDto.getRobotPhase())
                .createTime(robotWebDto.getCreateTime())
                .updateTime(robotWebDto.getUpdateTime())
                .build();
        LOGGER.info("Robot entity after conversion is "+robot);
        return robot;
    }

    /**
     *
     * @param javaObject
     * @return
     * @throws JsonProcessingException
     */
    public static String convertToJson(Object javaObject) throws JsonProcessingException {
        return objectMapper.writeValueAsString(javaObject);
    }

    /**
     *
     * @param jsonFromResponse
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T retrieveResourceFromResponse(String jsonFromResponse , Class<T> clazz)
            throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(jsonFromResponse, clazz);
    }

    /**
     *
     * @param errorCode
     * @param errorMessage
     * @return
     */
    public static ErrorResponse getErrorResponse(String errorCode, String errorMessage) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }

    public static void updateRobotEntityFromWebDto(Robot robot, RobotWebDto robotWebDto) {
        robot.setName(robotWebDto.getName());
        robot.setYearOfManufacture(robotWebDto.getYearOfManufacture());
        robot.setMass(robotWebDto.getMass());
        robot.setColor(robotWebDto.getColor());
        robot.setFunctions(robotWebDto.getFunctions());
        robot.setRobotPhase(robotWebDto.getRobotPhase());
    }
}
