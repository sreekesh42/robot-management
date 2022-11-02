package com.robotics.robotmanagement.web.controller;

import com.robotics.robotmanagement.exception.RobotException;
import com.robotics.robotmanagement.util.CommonUtil;
import com.robotics.robotmanagement.web.constants.LifeCyclePhase;
import com.robotics.robotmanagement.web.dto.FunctionsDto;
import com.robotics.robotmanagement.web.dto.PhaseCountDto;
import com.robotics.robotmanagement.web.dto.RobotWebDto;
import com.robotics.robotmanagement.web.service.RobotService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.robotics.robotmanagement.exception.ErrorConstants.ERROR.INTERNAL_SERVER_ERROR;

@RestController
public class RobotWebController {

    public static final String ALL = "ALL";
    private static Logger LOGGER = LogManager.getLogger(RobotWebController.class);

    @Autowired
    RobotService robotService;

    @GetMapping("/robot")
    public ResponseEntity<?> getAllRobots(@RequestParam(required = false,name = "criteria") String criteria) {
        try {
            List<RobotWebDto> robotWebDtos = null;
            if(StringUtils.isBlank(criteria)) {
                robotWebDtos = robotService.findAllActiveRobots();
            } else if(criteria.equalsIgnoreCase(ALL)) {
                robotWebDtos = robotService.findAllRobots();
            }
            if(CollectionUtils.isEmpty(robotWebDtos)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(robotWebDtos, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(),INTERNAL_SERVER_ERROR.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/robot/{robotId}")
    public ResponseEntity<?> getRobotById(@PathVariable String robotId) {
        try {
            RobotWebDto robotWebDto = robotService.getRobotById(robotId);
            if(ObjectUtils.isEmpty(robotWebDto)) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(robotWebDto, HttpStatus.OK);
        } catch (RobotException e) {
            LOGGER.error("Business exception occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(e.getCode(),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("Error occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(),INTERNAL_SERVER_ERROR.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/robot/count-per-phase")
    public ResponseEntity<?> getRobotsPerPhase() {
        try {
            List<PhaseCountDto> phaseCountDto = robotService.getRobotCountPerPhase();
            if(CollectionUtils.isEmpty(phaseCountDto)) {
                LOGGER.error("data returned from database is empty");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(phaseCountDto, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(),INTERNAL_SERVER_ERROR.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/robot")
    public ResponseEntity<?> createRobot(@RequestBody @Valid RobotWebDto robotWebDto) {
        try {
            RobotWebDto robotWebDtoResponse = robotService.createRobot(robotWebDto);
            if(ObjectUtils.isEmpty(robotWebDtoResponse)) {
                return new ResponseEntity<>(CommonUtil.getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(),INTERNAL_SERVER_ERROR.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            ResponseEntity responseEntity = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(robotWebDtoResponse);
            return responseEntity;
        } catch (RobotException e) {
            LOGGER.error("Business exception occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(e.getCode(),e.getMessage()), HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.error("Error occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(),INTERNAL_SERVER_ERROR.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/robot")
    public ResponseEntity<?> updateRobot(@RequestBody @Valid RobotWebDto robotWebDto) {
        try {
            RobotWebDto robotWebDtoResponse = robotService.updateRobot(robotWebDto);
            if(ObjectUtils.isEmpty(robotWebDtoResponse)) {
                LOGGER.error("Unable to fund robot with requested robot id");
                return new ResponseEntity<>(CommonUtil.getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(),INTERNAL_SERVER_ERROR.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            LOGGER.info("Robot with robot id "+robotWebDto.getRobotId()+" updated successfully!!");
            ResponseEntity responseEntity = ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(robotWebDtoResponse);
            return responseEntity;
        } catch (RobotException e) {
            LOGGER.error("Business exception occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(e.getCode(),e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error("Error occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(),INTERNAL_SERVER_ERROR.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/robot/{robotId}")
    public ResponseEntity<?> deleteRobot(@PathVariable String robotId) {
        try {
            RobotWebDto robotWebDto = robotService.getRobotById(robotId);
            if(ObjectUtils.isEmpty(robotWebDto)) {
                LOGGER.error("unable to find the robot to be deleted in database");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            robotService.deleteRobot(robotId);
            LOGGER.info("Robot id "+robotId+" deleted successfully!!");
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (RobotException e) {
            LOGGER.error("Business exception occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(e.getCode(),e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("Error occurred",e);
            return new ResponseEntity<>(CommonUtil.getErrorResponse(INTERNAL_SERVER_ERROR.getErrorCode(),INTERNAL_SERVER_ERROR.getErrorMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Value("${functions}")
    private String functions;

    @GetMapping("/robot/functions")
    public ResponseEntity<FunctionsDto> getFunctions() {
        FunctionsDto functionsDto = FunctionsDto.builder().functions(functions).build();
        LOGGER.info("Robot functions returned are : "+functionsDto);
        return new ResponseEntity<>(functionsDto, HttpStatus.OK);
    }

    @GetMapping("/robot/phases")
    public ResponseEntity<List<LifeCyclePhase>> getPhases() {
        List<LifeCyclePhase> phases = new ArrayList<>(Arrays.asList(LifeCyclePhase.values()));
        LOGGER.info("Life cycle phases returned are : "+phases);
        return new ResponseEntity<>(phases, HttpStatus.OK);
    }
}
