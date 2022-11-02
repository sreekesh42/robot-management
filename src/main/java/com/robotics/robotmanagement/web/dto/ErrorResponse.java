package com.robotics.robotmanagement.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    @JsonProperty("code")
    String errorCode;

    @JsonProperty("message")
    String errorMessage;
}
