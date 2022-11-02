package com.robotics.robotmanagement.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robotics.robotmanagement.util.CommonUtil;
import com.robotics.robotmanagement.web.constants.LifeCyclePhase;
import com.robotics.robotmanagement.web.dto.FunctionsDto;
import com.robotics.robotmanagement.web.dto.PhaseCountDto;
import com.robotics.robotmanagement.web.dto.RobotWebDto;
import com.robotics.robotmanagement.web.service.RobotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
@TestPropertySource("classpath:application.properties")
public class RobotWebControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private RobotService robotService;

    @Test
    void getAllRobots() throws Exception {
        List<RobotWebDto> robotWebDtoList = new ArrayList<>();
        RobotWebDto robotWebDto1 = RobotWebDto.builder()
                .robotId(UUID.randomUUID().toString())
                .name("robot 1")
                .mass(90L)
                .color("GREEN")
                .yearOfManufacture(2020L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        RobotWebDto robotWebDto2 = RobotWebDto.builder()
                .robotId(UUID.randomUUID().toString())
                .name("robot 2")
                .mass(90L)
                .color("GREEN")
                .yearOfManufacture(2022L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.READY_TO_SHIP)
                .build();
        RobotWebDto robotWebDto3 = RobotWebDto.builder()
                .robotId(UUID.randomUUID().toString())
                .name("robot 3")
                .mass(50L)
                .color("BLUE")
                .yearOfManufacture(2021L)
                .functions(List.of("temperature sensing","sound sensing","light sensing","pressure sensing","mobility degrees-of-freedom").toString())
                .robotPhase(LifeCyclePhase.DEPLOYED)
                .build();
        robotWebDtoList.add(robotWebDto1);
        robotWebDtoList.add(robotWebDto2);
        robotWebDtoList.add(robotWebDto3);
        Mockito.when(robotService.findAllRobots()).thenReturn(robotWebDtoList);
        mockMvc.perform(MockMvcRequestBuilders.get("/robot")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$", hasSize(3))).andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getRobotById() throws Exception {

        RobotWebDto robotWebDto1 = RobotWebDto.builder()
                .robotId(UUID.randomUUID().toString())
                .name("robot 1")
                .mass(90L)
                .color("GREEN")
                .yearOfManufacture(2020L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Mockito.when(robotService.getRobotById(robotWebDto1.getRobotId())).thenReturn(robotWebDto1);
        mockMvc.perform(MockMvcRequestBuilders.get("/robot/"+robotWebDto1.getRobotId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(robotWebDto1.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getRobotsByPhase() throws Exception {
        List<PhaseCountDto> phaseCountDtos = new ArrayList<>();
        PhaseCountDto phaseCountDto1 = PhaseCountDto.builder().lifeCyclePhase(LifeCyclePhase.IN_DESIGN).count(3L).build();
        PhaseCountDto phaseCountDto2 = PhaseCountDto.builder().lifeCyclePhase(LifeCyclePhase.DEPLOYED).count(2L).build();
        PhaseCountDto phaseCountDto3 = PhaseCountDto.builder().lifeCyclePhase(LifeCyclePhase.READY_TO_SHIP).count(8L).build();
        PhaseCountDto phaseCountDto4 = PhaseCountDto.builder().lifeCyclePhase(LifeCyclePhase.DAMAGED_BEYOND_REPAIR).count(1L).build();
        phaseCountDtos.add(phaseCountDto1);
        phaseCountDtos.add(phaseCountDto2);
        phaseCountDtos.add(phaseCountDto3);
        phaseCountDtos.add(phaseCountDto4);
        Mockito.when(robotService.getRobotCountPerPhase()).thenReturn(phaseCountDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/robot/count-per-phase")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(4)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createRobot() throws Exception {

        RobotWebDto robotWebDto1 = RobotWebDto.builder()
                .robotId("id_dummy")
                .name("robot 1")
                .mass(90L)
                .color("GREEN")
                .yearOfManufacture(2020L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        String jsonContent = CommonUtil.convertToJson(robotWebDto1);
        RobotWebDto robotWebDtoResponse = RobotWebDto.builder()
                .robotId(UUID.randomUUID().toString())
                .name("robot 1")
                .mass(90L)
                .color("GREEN")
                .yearOfManufacture(2020L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Mockito.when(robotService.createRobot(robotWebDto1)).thenReturn(robotWebDtoResponse);


        mockMvc.perform(MockMvcRequestBuilders.post("/robot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        //.andExpect(jsonPath("$", hasSize(1)))
                        .andExpect(jsonPath("$.name",is(robotWebDto1.getName())))
                        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateRobot() throws Exception {

        RobotWebDto robotWebDto1 = RobotWebDto.builder()
                .robotId("id_dummy")
                .name("robot 1")
                .mass(90L)
                .color("GREEN")
                .yearOfManufacture(2020L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        String jsonContent = CommonUtil.convertToJson(robotWebDto1);

        RobotWebDto robotWebDtoUpdatedName = RobotWebDto.builder()
                .robotId("id_dummy")
                .name("robot name updated")
                .mass(90L)
                .color("GREEN")
                .yearOfManufacture(2020L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();

        Mockito.when(robotService.updateRobot(robotWebDto1)).thenReturn(robotWebDtoUpdatedName);

        mockMvc.perform(MockMvcRequestBuilders.put("/robot")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("robot name updated")))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void deleteRobot() throws Exception {

        RobotWebDto robotWebDto1 = RobotWebDto.builder()
                .robotId(UUID.randomUUID().toString())
                .name("robot 1")
                .mass(90L)
                .color("GREEN")
                .yearOfManufacture(2020L)
                .functions(List.of("temperature sensing","sound sensing").toString())
                .robotPhase(LifeCyclePhase.IN_DESIGN)
                .build();
        Mockito.when(robotService.getRobotById(robotWebDto1.getRobotId())).thenReturn(robotWebDto1);
        mockMvc.perform(MockMvcRequestBuilders.delete("/robot/"+robotWebDto1.getRobotId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Value("${functions}")
    private String functions;

    @Test
    void getRobotFunctions() throws Exception {
        FunctionsDto functionsDto = FunctionsDto.builder().functions(functions).build();
        String functionsJson = new ObjectMapper().writeValueAsString(functionsDto);
        System.out.println("Functions JSON "+functionsJson);
        mockMvc.perform(MockMvcRequestBuilders.get("/robot/functions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(functionsJson, false))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getLifeCyclePhases() throws Exception {
        List<LifeCyclePhase> phases = new ArrayList<>(Arrays.asList(LifeCyclePhase.values()));
        String phasesJson = new ObjectMapper().writeValueAsString(phases);
        System.out.println("Lifecycle phases JSON "+phasesJson);
        mockMvc.perform(MockMvcRequestBuilders.get("/robot/phases")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(phasesJson, false))
                .andDo(MockMvcResultHandlers.print());
    }
}
