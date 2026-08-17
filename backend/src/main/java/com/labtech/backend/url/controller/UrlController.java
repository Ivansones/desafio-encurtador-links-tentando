package com.labtech.backend.url.controller;

import com.labtech.backend.dto.CreateUrlRequestDto;
import com.labtech.backend.dto.UpdateUrlDto;
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
            return ResponseEntity.status(HttpStatus.CREATED).body("Criado corretamente");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Criação falhou");
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
    @DeleteMapping (path = "/delete/{shortCode}")
    public ResponseEntity<String> deleteUrl (@PathVariable String shortCode){
        urlService.deleteUrl(shortCode);
        return ResponseEntity.status(HttpStatus.OK).body("Url deletado corrematamente");
    }

    @PatchMapping (path = "/update/{shortCode}")
    public  ResponseEntity<String> updateUrl (@PathVariable String shortCode, @RequestBody @Valid UpdateUrlDto updateUrlDto){
        boolean isUpdate = urlService.updateUrl(shortCode, updateUrlDto);
        if (isUpdate){
            return ResponseEntity.status(HttpStatus.OK).body("Url foi mudado com sucesso");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falhou ao mudar o url");
        }
    }
}
