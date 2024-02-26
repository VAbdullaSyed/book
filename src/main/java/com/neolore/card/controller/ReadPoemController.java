package com.neolore.card.controller;

import com.neolore.card.s3.S3PoemClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReadPoemController {
    private final S3PoemClient s3PoemClient;
    @GetMapping(path = "/poem")
    public String poem() {
        String poem = "Poem2: ";
        try {
            poem += s3PoemClient.readFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return poem;
    }
}
