package com.example.takehome.services.impl;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.takehome.codes.UserAuthType;
import com.example.takehome.config.RateLimitConfig;
import com.example.takehome.services.BucketResolverService;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class BucketResolverServiceImpl implements BucketResolverService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Autowired
    private RateLimitConfig rateLimitConfig;

    @Override
    public Bucket resolveBucket(Authentication auth) {
        if (auth instanceof AnonymousAuthenticationToken) {
            return cache.computeIfAbsent(fetchClientIpAddr(),
                    authName -> this.newBucket(UserAuthType.ANONYMOUS));
        } else {
            return cache.computeIfAbsent(auth.getName(),
                    authName -> this.newBucket(UserAuthType.AUTHENTICATED));
        }
    }

    /**
     * Create new Bucket if it does not exist in cache
     * 
     * @param authType {@link UserAuthType}
     * @return {@link Bucket}
     */
    private Bucket newBucket(UserAuthType authType) {
        Long limit = null;
        if (authType.equals(UserAuthType.ANONYMOUS)) {
            limit = rateLimitConfig.getAnonymous();
        } else {
            limit = rateLimitConfig.getAuthenticated();
        }

        return Bucket.builder().addLimit(Bandwidth.classic(limit,
                Refill.intervally(limit,
                        Duration.ofSeconds(rateLimitConfig.getDuration()))))
                .build();
    }

    /**
     * <pre>
     * Fetch Client IP Address if
     * sender is an anonymous user
     * </pre>
     * 
     * @return String remote address of client
     */
    private String fetchClientIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String ipAddr = Optional
                .ofNullable(request.getHeader("X-FORWARDED-FOR"))
                .orElse(request.getRemoteAddr());
        if (ipAddr.equals("0:0:0:0:0:0:0:1"))
            ipAddr = "127.0.0.1";

        return ipAddr;
    }

}
