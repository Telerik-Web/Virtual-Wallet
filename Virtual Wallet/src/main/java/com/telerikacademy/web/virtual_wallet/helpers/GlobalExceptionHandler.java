//package com.telerikacademy.web.virtual_wallet.helpers;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public String handleGlobalException(Exception ex, Model model) {
//        model.addAttribute("error", ex.getMessage());
//        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        return "UnexpectedException";
//    }
//}
