package com.labtech.backend.security;

import jdk.dynalink.linker.LinkerServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathsConfig {

    @Bean(name = "publicPaths")
    public List<String> publicPaths (){
        return List.of(
                "/auth/register/public",
                "/auth/login/public",
                "/csrf-token/public",
                "/url/{shortCode}"

        );

    }
    @Bean(name = "userPaths")
    public List<String> userPaths (){
        return List.of(
                "/**"
        );
    }
}
