package com.labtech.backend.dto;

public record LoginResponseDto( String message, UserDto user , String JwtToken) {
}
