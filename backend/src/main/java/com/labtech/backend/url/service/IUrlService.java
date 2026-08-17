package com.labtech.backend.url.service;

import com.labtech.backend.dto.CreateUrlRequestDto;
import com.labtech.backend.dto.UpdateUrlDto;
import com.labtech.backend.dto.UrlDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Optional;

public interface IUrlService {

    boolean createLink (CreateUrlRequestDto createUrlRequestDto, HttpServletRequest request);

    List<UrlDto> getAllUrlForUser ();

    String accessLink (String shortCode);

    void deleteUrl(String shortCode);

    boolean updateUrl (String shortCode, UpdateUrlDto updateUrlDto);
}
