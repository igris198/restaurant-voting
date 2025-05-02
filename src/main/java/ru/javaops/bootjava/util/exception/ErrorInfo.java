package ru.javaops.bootjava.util.exception;

public record ErrorInfo(
    String url,
    ErrorType type,
    String typeMessage,
    String[] details
) {
    public ErrorInfo(StringBuffer url, ErrorType type, String typeMessage, String[] details) {
        this(url.toString(), type, typeMessage, details);
    }
}
