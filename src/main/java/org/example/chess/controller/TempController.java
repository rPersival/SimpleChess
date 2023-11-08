package org.example.chess.controller;

import lombok.RequiredArgsConstructor;
import org.example.chess.service.TempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("temp")
@RequestMapping("/hello")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TempController {

    private final TempService service;

    @GetMapping()
    public ResponseEntity<Object> helloWorld() {
        return ResponseEntity.ok("Hello World!");
    }
}
