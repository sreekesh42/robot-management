package com.robotics.robotmanagement.exception;

import lombok.Getter;

public class RobotException extends Exception {

    @Getter
    String code;
    @Getter
    String message;

    public RobotException(String id, String message) {
        super();
        this.code = id;
        this.message = message;
    }


}
