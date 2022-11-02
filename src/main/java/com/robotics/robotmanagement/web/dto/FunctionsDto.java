package com.robotics.robotmanagement.web.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class FunctionsDto {

    @JsonProperty("functions")
    String functions;
}
