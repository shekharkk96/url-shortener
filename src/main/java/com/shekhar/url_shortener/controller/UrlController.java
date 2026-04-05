package com.shekhar.url_shortener.controller;

import com.shekhar.url_shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @GetMapping("/{code}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String code) {
        String originalUrl = urlService.getOriginalUrl(code);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @PostMapping("/shorten")
    public String createShortUrl(@RequestBody Map<String, String> request){
        return urlService.shortenUrl(request.get("url"));
    }
}
