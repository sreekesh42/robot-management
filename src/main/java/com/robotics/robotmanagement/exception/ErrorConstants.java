package com.robotics.robotmanagement.exception;

import lombok.Getter;

public class ErrorConstants {

    public enum ERROR {
        INPUT_VAIDATION("ERR01","Mandatory fields cannot be blank"),
        INTERNAL_SERVER_ERROR("ERR02","Unexpected error occurred"),
        ROBOT_ALREADY_EXISTS("ERR03","Robot already exists"),
        ROBOT_NOT_FOUND("ERR04","Robot not found");

        @Getter
        String errorCode;
        @Getter
        String errorMessage;

        ERROR(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }


    }
}
