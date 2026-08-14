package com.labtech.backend.auth;

import com.labtech.backend.dto.LoginRequestDto;
import com.labtech.backend.dto.LoginResponseDto;
import com.labtech.backend.dto.RegisterRequestDto;
import com.labtech.backend.dto.UserDto;
import com.labtech.backend.entity.User;
import com.labtech.backend.repository.UserRepository;
import com.labtech.backend.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping  (path = "/register/public")
    public ResponseEntity<?> registerUser (@RequestBody RegisterRequestDto registerRequestDto){
        User user = new User();
        BeanUtils.copyProperties(registerRequestDto,user);
        user.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");

    }

    @PostMapping (path = "/login/public")
    public ResponseEntity<LoginResponseDto> logInUser (@RequestBody LoginRequestDto loginRequestDto){
        try{
            var resulAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.email()
            ,loginRequestDto.password()));
            String jwtToken = jwtUtil.generateToken(resulAuthentication);
            var userDto = new UserDto();
            var loggedInUser = (User) resulAuthentication.getPrincipal();
            BeanUtils.copyProperties(loggedInUser,userDto);
            userDto.setUserId(loggedInUser.getId());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(),userDto,jwtToken));

        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }
    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status,
                                                                String message) {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDto(message, null, null));
    }
}
