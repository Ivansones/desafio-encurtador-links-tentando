package com.labtech.backend.url.controller;

import com.labtech.backend.dto.CreateUrlRequestDto;
import com.labtech.backend.dto.UrlDto;
import com.labtech.backend.url.service.IUrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final IUrlService urlService;

    @PostMapping(path = "/user")
    public ResponseEntity<String> createLink (@RequestBody @Valid CreateUrlRequestDto requestDto, HttpServletRequest request) {
        boolean isCreated = urlService.createLink(requestDto,request);
        if (isCreated){
            return ResponseEntity.status(HttpStatus.CREATED).body("Created successfully");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Creating failed");
        }
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<UrlDto>> getAllUrl (){
        List<UrlDto> urlList = urlService.getAllUrlForUser();
        return ResponseEntity.ok().body(urlList);
    }

    @GetMapping(path = "/{shortCode}")
    public ResponseEntity<Void> accessLink (@PathVariable String shortCode){
        String link = urlService.accessLink(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(link)).build();
    }
}
