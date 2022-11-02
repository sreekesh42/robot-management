package com.robotics.robotmanagement.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robotics.robotmanagement.web.constants.LifeCyclePhase;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PhaseCountDto {

    @JsonProperty("phase")
    LifeCyclePhase lifeCyclePhase;

    @JsonProperty("count")
    Long count;
}
