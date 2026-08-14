package com.labtech.backend.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;

public record CreateUrlRequestDto(
        String shortCode,
        @NotBlank (message = "There must be a link")
        @URL (message = "Link must be valid")
        String link) {
}
