package com.labtech.backend.url.service.impl;

import com.labtech.backend.dto.CreateUrlRequestDto;
import com.labtech.backend.dto.UpdateUrlDto;
import com.labtech.backend.dto.UrlDto;
import com.labtech.backend.entity.Url;
import com.labtech.backend.entity.User;
import com.labtech.backend.repository.UrlRepository;
import com.labtech.backend.repository.UserRepository;
import com.labtech.backend.url.service.IUrlService;
import com.labtech.backend.util.ApplicationUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional (readOnly = true)
public class UrlServiceImpl implements IUrlService {

    private final UrlRepository urlRepository;
    private final UserRepository userRepository;
    private final SecureRandom random = new SecureRandom();
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Transactional
    @Override
    public boolean createLink(CreateUrlRequestDto createUrlRequestDto, HttpServletRequest request) {
        Url url = transformUrlDtoToEntity(createUrlRequestDto);
        String email = ApplicationUtility.getLoggedInUser();
        User user = userRepository.findUserByEmail(email).orElseThrow(()-> new RuntimeException("Usuario não encontrado com"+email));
        url.setUser(user);
        url.setCreatorIp(request.getRemoteAddr());
        Url savedUrl = urlRepository.save(url);

        return savedUrl.getId() != null && savedUrl.getId()>0;
    }

    @Override
    public List<UrlDto> getAllUrlForUser() {
        String email = ApplicationUtility.getLoggedInUser();
        List<Url> urlList = urlRepository.findAllByUserEmail(email);
        return urlList.stream().map(this::tranformUrlToDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public String accessLink(String shortCode) {
        List<Url> urls = urlRepository.findByShortCode(shortCode);
        if (urls.isEmpty()){
            throw new RuntimeException("Não foi possivel encontrar esse nome");
        }

        Url url = urls.get(0);

        url.setAccessCount(url.getAccessCount()+ 1);
        url.setLastAccess(Instant.now());

        urlRepository.save(url);

        return url.getLink();
    }
    @Transactional
    @Override
    public void deleteUrl(String shortCode) {
        String email = ApplicationUtility.getLoggedInUser();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(()->new RuntimeException("Usuario não existe"));
        List<Url> urls = urlRepository.findByShortCode(shortCode);
        if (urls.isEmpty()) {
            throw new RuntimeException("URL não existe");
        }
        Url url = urls.get(0);

        if (!url.getUser().getId().equals(loggedUser.getId())){
            throw new RuntimeException("Voce não tem acesso para deletar esse url");
        } else {
            urlRepository.deleteByShortCode(shortCode);
        }

    }

    @Transactional
    @Override
    public boolean updateUrl(String shortCode, UpdateUrlDto updateUrlDto) {

        String email = ApplicationUtility.getLoggedInUser();
        User loggedUser = userRepository.findUserByEmail(email).orElseThrow(()->new RuntimeException("Usuario não existe"));
        List<Url> urls = urlRepository.findByShortCode(shortCode);
        if (urls.isEmpty()) {
            throw new RuntimeException("URL não existe");
        }
        Url url = urls.get(0);
        if (!url.getUser().getId().equals(loggedUser.getId())) {
            throw  new RuntimeException("Não pode dar update nesse url");
        }
        String newShortCode = updateUrlDto.shortCode() != null
                ? updateUrlDto.shortCode()
                : url.getShortCode();

        String newLink = updateUrlDto.link() != null
                ? updateUrlDto.link()
                : url.getLink();

        int updateUrl = urlRepository.urlUpdate(
                shortCode,
                newShortCode,
                newLink
        );
        return updateUrl > 0;
    }

    private Url transformUrlDtoToEntity(CreateUrlRequestDto createUrlRequestDto){
        Url url = new Url();
        if (createUrlRequestDto.shortCode() == null || createUrlRequestDto.shortCode().isBlank()){
            url.setShortCode((generateUniqueShortCode()));
        } else {
            if (!urlRepository.findByShortCode(createUrlRequestDto.shortCode()).isEmpty()) {
                throw new RuntimeException("Esse codigo ja esta sendo usado");
            }else {
                url.setShortCode(createUrlRequestDto.shortCode());
            }
        }
        if (!isValidDomain(createUrlRequestDto.link())){
            throw new RuntimeException("Link invalido");
        }
        url.setLink(createUrlRequestDto.link());
        url.setAccessCount(0);
        return url;
    }

    private UrlDto tranformUrlToDto (Url url){
        return new UrlDto(url.getId(),url.getShortCode(),url.getLink(),url.getAccessCount(),url.getCreatedAt(),url.getLastAccess(),url.getCreatorIp());
    }

    private String generateShortCode(){
        StringBuilder code = new StringBuilder(6);
        for (int i = 0; i < 6 ; i++){
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }


    private String generateUniqueShortCode() {

        String shortCode;
        List<Url> urls;
        do {
            shortCode = generateShortCode();
            urls = urlRepository.findByShortCode(shortCode);

        } while (!urls.isEmpty());

        return shortCode;
    }

    private boolean isValidDomain(String link) {
        try {
            URI uri = URI.create(link);

            String host = uri.getHost();

            if (host == null) {
                return false;
            }

            String[] parts = host.split("\\.");

            // Must have at least: example.com
            if (parts.length < 2) {
                return false;
            }

            // Domain cannot end with a dot
            if (host.endsWith(".")) {
                return false;
            }

            // Every part must contain something
            for (String part : parts) {
                if (part.isBlank()) {
                    return false;
                }
            }

            // Last part should look like a TLD
            String tld = parts[parts.length - 1];

            if (tld.length() < 2) {
                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
