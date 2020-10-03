package com.sdmorales.kalah.control;

import com.sdmorales.kalah.domain.GameException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(GameException.class)
    public ResponseEntity<MessageWrapper> handleGameException(GameException gameException) {
        return ResponseEntity.badRequest()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new MessageWrapper(gameException.getMessage()));
    }

    static class MessageWrapper {

        private final String message;

        public MessageWrapper(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
