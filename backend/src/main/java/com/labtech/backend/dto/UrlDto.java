package com.labtech.backend.dto;

import java.time.Instant;

public record UrlDto(Long id, String shortCode, String link, Integer access_count, Instant createAt, Instant lastAccess, String creatorIp) {
}
