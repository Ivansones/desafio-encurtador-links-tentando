package com.labtech.backend.url.service;

import com.labtech.backend.dto.CreateUrlRequestDto;
import com.labtech.backend.dto.UrlDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface IUrlService {

    boolean createLink (CreateUrlRequestDto createUrlRequestDto, HttpServletRequest request);

    List<UrlDto> getAllUrlForUser ();

    String accessLink (String shortCode);
}
