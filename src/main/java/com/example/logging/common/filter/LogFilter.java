package com.example.logging.common.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogFilter extends OncePerRequestFilter {

    private static final String TXID = "Txid";
    private static final String METHOD = "Method";
    private static final String URI = "Uri";

    private void putMDC(HttpServletRequest request) {
        if (MDC.get(TXID) == null || MDC.get(TXID).isEmpty()) {
            MDC.put(TXID, String.valueOf(UUID.randomUUID()).substring(0, 8));
        }

        MDC.put(METHOD, request.getMethod());
        MDC.put(URI, request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        putMDC(request);
        requestWrapper.setAttribute(TXID, MDC.get(TXID));
        responseWrapper.setHeader(TXID, MDC.get(TXID));
        filterChain.doFilter(requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();
    }
}
