package com.example.takehome.services;

import org.springframework.security.core.Authentication;

import io.github.bucket4j.Bucket;

public interface BucketResolverService {

    /**
     * <pre>
     * Resolve bucket based on whether sender is authenticated or not.
     * Apply different rate limits based on the same condition.
     * </pre>
     * 
     * @param auth {@link Authentication}
     * @return {@link Bucket} object that defines the rate limit
     */
    public Bucket resolveBucket(Authentication auth);
}
