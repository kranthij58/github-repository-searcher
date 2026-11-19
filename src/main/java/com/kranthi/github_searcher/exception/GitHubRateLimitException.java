
package com.kranthi.github_searcher.exception;

public class GitHubRateLimitException extends RuntimeException {

    private final String resetTime;

    public GitHubRateLimitException(String message, String resetTime) {
        super(message);
        this.resetTime = resetTime;
    }

    public String getResetTime() {
        return resetTime;
    }
}
