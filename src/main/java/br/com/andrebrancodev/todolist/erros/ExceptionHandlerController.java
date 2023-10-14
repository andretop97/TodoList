package br.com.andrebrancodev.todolist.erros;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        System.out.println("IllegalArgumentException: " + e.getMessage());
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
