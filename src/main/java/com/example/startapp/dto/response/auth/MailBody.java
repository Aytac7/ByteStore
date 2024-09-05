package com.example.startapp.dto.response.auth;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
