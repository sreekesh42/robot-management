package com.robotics.robotmanagement.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.robotics.robotmanagement.web.constants.LifeCyclePhase;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RobotWebDto {

    @JsonProperty("robot_id")
    @NotBlank(message = "Robot id cannot be blank")
    String robotId;

    @JsonProperty("name")
    @NotBlank(message = "Name cannot be blank")
    String name;

    @JsonProperty("year")
    @NotNull(message = "Year of manufacture cannot be blank")
    Long yearOfManufacture;

    @JsonProperty("mass")
    @NotNull(message = "Mass cannot be blank")
    Long mass;

    @JsonProperty("color")
    @NotBlank(message = "Color cannot be blank")
    String color;

    @JsonProperty("functions")
    @NotBlank(message = "Functions cannot be blank")
    String functions;

    //by default phase is IN_DESIGN
    @JsonProperty("phase")
    LifeCyclePhase robotPhase = LifeCyclePhase.IN_DESIGN;

    @JsonIgnore
    LocalDateTime createTime;
    @JsonIgnore
    LocalDateTime updateTime;
}
