package org.example.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(IOException.class)
    public void handler(){}
}
