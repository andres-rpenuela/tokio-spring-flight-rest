package org.tokio.spring.resources.core.exception;

public class ResourceException extends RuntimeException {
    public ResourceException(String message) {
        super(message);
    }
    public ResourceException(String message, Throwable cause) {}
}
