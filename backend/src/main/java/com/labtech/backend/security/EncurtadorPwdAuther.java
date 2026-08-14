package com.labtech.backend.security;

import com.labtech.backend.entity.User;
import com.labtech.backend.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncurtadorPwdAuther implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public  @Nullable Authentication authenticate (Authentication authentication)throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User detaisl not found with"+email));
        if (passwordEncoder.matches(pwd,user.getPasswordHash())){
            return new UsernamePasswordAuthenticationToken(user,null);
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
