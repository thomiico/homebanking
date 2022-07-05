package com.mindhub.homebanking.services;

import org.dom4j.DocumentException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public interface PdfGenerator {
    void export(HttpServletResponse response, Authentication authentication, String numberAccount, LocalDateTime since, LocalDateTime until) throws IOException, DocumentException, com.lowagie.text.DocumentException;
}