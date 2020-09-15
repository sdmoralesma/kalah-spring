package com.sdmorales.kalah;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KalahController {

    @GetMapping
    public ResponseEntity lotto() {
        return ResponseEntity.ok("hello");
    }
}
