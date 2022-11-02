package com.robotics.robotmanagement.service;

import com.robotics.robotmanagement.dao.RobotRepository;
import com.robotics.robotmanagement.dao.entity.Robot;
import com.robotics.robotmanagement.exception.RobotException;
import com.robotics.robotmanagement.util.CommonUtil;
import com.robotics.robotmanagement.web.constants.LifeCyclePhase;
import com.robotics.robotmanagement.web.dto.PhaseCountDto;
import com.robotics.robotmanagement.web.dto.RobotWebDto;
import com.robotics.robotmanagement.web.service.RobotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RobotServiceTest {

    @MockBean
    RobotRepository robotRepository;

    @Autowired
    RobotService robotService;

    @Test
    void getAllRobots() {
        Robot robot1 = Robot.builder()
                .robotId("id1")
                .name("Robot 1")
                .yearOfManufacture(2022L)
                .mass(120L)
                .color("YELLOW")
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Robot robot2 = Robot.builder()
                .robotId("id2")
                .name("Robot 2")
                .yearOfManufacture(2022L)
                .mass(120L)
                .color("YELLOW")
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        List<Robot> robots = new ArrayList<>();
        robots.add(robot1);
        robots.add(robot2);
        Mockito.when(robotRepository.findAll()).thenReturn(robots);
        List<RobotWebDto> robotWebDtos = robotService.findAllRobots();

        RobotWebDto robotWebDto = robotWebDtos.get(robotWebDtos.size() - 1);

        assertEquals(robot2.getRobotId(), robotWebDto.getRobotId());
        assertEquals(robot2.getName(), robotWebDto.getName());
        assertEquals(robot2.getRobotPhase(), robotWebDto.getRobotPhase());
    }

    @Test
    void getAllActiveRobots() {
        Robot robot1 = Robot.builder()
                .robotId(UUID.randomUUID().toString())
                .name("Robot 1")
                .yearOfManufacture(2022L)
                .mass(120L)
                .color("YELLOW")
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Robot robot2 = Robot.builder()
                .robotId(UUID.randomUUID().toString())
                .name("Robot 1")
                .yearOfManufacture(2022L)
                .mass(120L)
                .color("YELLOW")
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        List<Robot> robots = new ArrayList<>();
        robots.add(robot1);
        robots.add(robot2);
        Mockito.when(robotRepository.findAllActiveRobots()).thenReturn(robots);

        List<RobotWebDto> robotWebDtos = robotService.findAllActiveRobots();
        for(RobotWebDto robotWebDto : robotWebDtos) {
            assertNotEquals(robotWebDto.getRobotPhase(), LifeCyclePhase.DAMAGED_BEYOND_REPAIR);
        }
    }

    @Test
    void getRobotById() throws RobotException {

        Robot robot = Robot.builder()
                .robotId(UUID.randomUUID().toString())
                .name("Robot 1")
                .yearOfManufacture(2022L)
                .mass(120L)
                .color("YELLOW")
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Mockito.when(robotRepository.findByRobotId(robot.getRobotId())).thenReturn(robot);
        RobotWebDto robotWebDtoExpected = CommonUtil.getRobotWebDto(robot);
        RobotWebDto robotWebDtoActual = robotService.getRobotById(robot.getRobotId());

        Assertions.assertEquals(robotWebDtoExpected.getRobotId(),robotWebDtoActual.getRobotId());
        Assertions.assertEquals(robotWebDtoExpected.getName(),robotWebDtoActual.getName());
        Assertions.assertEquals(robotWebDtoExpected.getYearOfManufacture(),robotWebDtoActual.getYearOfManufacture());
        Assertions.assertEquals(robotWebDtoExpected.getColor(),robotWebDtoActual.getColor());
    }

    @Test
    void getCountPerPhase() {

        List<PhaseCountDto> phaseCountDtos = new ArrayList<>();
        PhaseCountDto phaseCountDto1 = PhaseCountDto.builder().lifeCyclePhase(LifeCyclePhase.IN_DESIGN).count(3L).build();
        PhaseCountDto phaseCountDto2 = PhaseCountDto.builder().lifeCyclePhase(LifeCyclePhase.DEPLOYED).count(2L).build();
        PhaseCountDto phaseCountDto3 = PhaseCountDto.builder().lifeCyclePhase(LifeCyclePhase.READY_TO_SHIP).count(8L).build();
        PhaseCountDto phaseCountDto4 = PhaseCountDto.builder().lifeCyclePhase(LifeCyclePhase.DAMAGED_BEYOND_REPAIR).count(1L).build();
        phaseCountDtos.add(phaseCountDto1);
        phaseCountDtos.add(phaseCountDto2);
        phaseCountDtos.add(phaseCountDto3);
        phaseCountDtos.add(phaseCountDto4);
        Mockito.when(robotRepository.countRobotsPerPhase()).thenReturn(phaseCountDtos);
        List<PhaseCountDto> phaseCountDtosActual = robotService.getRobotCountPerPhase();

        assertEquals(phaseCountDtosActual.size(),4);
    }

    @Test
    void createRobot() throws RobotException {
        Robot robot = Robot.builder()
                .robotId("id_dummy")
                .name("Robot 1")
                .yearOfManufacture(2022L)
                .mass(120L)
                .color("YELLOW")
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Mockito.when(robotRepository.existsByRobotId(robot.getRobotId())).thenReturn(Boolean.FALSE);
        Mockito.when(robotRepository.save(robot)).thenReturn(robot);
        RobotWebDto robotWebDtoResponseExpected = CommonUtil.getRobotWebDto(robot);
        RobotWebDto robotWebDtoResponseActual = robotService.createRobot(robotWebDtoResponseExpected);

        Assertions.assertEquals(robotWebDtoResponseActual.getRobotId(),robotWebDtoResponseExpected.getRobotId());
        Assertions.assertEquals(robotWebDtoResponseActual.getName(),robotWebDtoResponseExpected.getName());
        Assertions.assertEquals(robotWebDtoResponseActual.getYearOfManufacture(),robotWebDtoResponseExpected.getYearOfManufacture());
        Assertions.assertEquals(robotWebDtoResponseActual.getColor(),robotWebDtoResponseExpected.getColor());
    }

    @Test
    void updateRobot() throws RobotException {
        Robot robot = Robot.builder()
                .robotId("id_dummy")
                .name("robot 1 updated")
                .mass(120L)
                .yearOfManufacture(2022L)
                .color("YELLOW")
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Mockito.when(robotRepository.existsByRobotId(robot.getRobotId())).thenReturn(Boolean.TRUE);
        Mockito.when(robotRepository.save(robot)).thenReturn(robot);
        RobotWebDto robotWebDtoResponseExpected = CommonUtil.getRobotWebDto(robot);

        RobotWebDto robotWebDto1 = RobotWebDto.builder()
                .robotId("id_dummy")
                .name("robot 1 updated")
                .mass(120L)
                .color("YELLOW")
                .yearOfManufacture(2022L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        RobotWebDto robotWebDtoResponseActual = robotService.updateRobot(robotWebDto1);

        Assertions.assertEquals(robotWebDtoResponseActual.getRobotId(),robotWebDtoResponseExpected.getRobotId());
        Assertions.assertEquals(robotWebDtoResponseActual.getName(),robotWebDtoResponseExpected.getName());
        Assertions.assertEquals(robotWebDtoResponseActual.getYearOfManufacture(),robotWebDtoResponseExpected.getYearOfManufacture());
        Assertions.assertEquals(robotWebDtoResponseActual.getColor(),robotWebDtoResponseExpected.getColor());
    }

    @Test
    void deleteRobot() {

        Robot robot = Robot.builder()
                .robotId(UUID.randomUUID().toString())
                .name("Robot 1")
                .yearOfManufacture(2022L)
                .mass(120L)
                .color("YELLOW")
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Mockito.when(robotRepository.deleteByRobotId(robot.getRobotId())).thenReturn(1);
        boolean status = robotService.deleteRobot(robot.getRobotId());

        assertTrue(status);
    }

}
