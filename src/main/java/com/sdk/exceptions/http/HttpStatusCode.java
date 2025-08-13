package com.sdk.exceptions.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Enumeration of HTTP status codes with descriptions and categories.
 */
public enum HttpStatusCode {
    
    // 1xx Informational
    CONTINUE(100, "Continue", "Informational"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols", "Informational"),
    PROCESSING(102, "Processing", "Informational"),
    
    // 2xx Success
    OK(200, "OK", "Success"),
    CREATED(201, "Created", "Success"),
    ACCEPTED(202, "Accepted", "Success"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information", "Success"),
    NO_CONTENT(204, "No Content", "Success"),
    RESET_CONTENT(205, "Reset Content", "Success"),
    PARTIAL_CONTENT(206, "Partial Content", "Success"),
    
    // 3xx Redirection
    MULTIPLE_CHOICES(300, "Multiple Choices", "Redirection"),
    MOVED_PERMANENTLY(301, "Moved Permanently", "Redirection"),
    FOUND(302, "Found", "Redirection"),
    SEE_OTHER(303, "See Other", "Redirection"),
    NOT_MODIFIED(304, "Not Modified", "Redirection"),
    USE_PROXY(305, "Use Proxy", "Redirection"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect", "Redirection"),
    PERMANENT_REDIRECT(308, "Permanent Redirect", "Redirection"),
    
    // 4xx Client Error
    BAD_REQUEST(400, "Bad Request", "Client Error"),
    UNAUTHORIZED(401, "Unauthorized", "Client Error"),
    PAYMENT_REQUIRED(402, "Payment Required", "Client Error"),
    FORBIDDEN(403, "Forbidden", "Client Error"),
    NOT_FOUND(404, "Not Found", "Client Error"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "Client Error"),
    NOT_ACCEPTABLE(406, "Not Acceptable", "Client Error"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required", "Client Error"),
    REQUEST_TIMEOUT(408, "Request Timeout", "Client Error"),
    CONFLICT(409, "Conflict", "Client Error"),
    GONE(410, "Gone", "Client Error"),
    LENGTH_REQUIRED(411, "Length Required", "Client Error"),
    PRECONDITION_FAILED(412, "Precondition Failed", "Client Error"),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large", "Client Error"),
    URI_TOO_LONG(414, "URI Too Long", "Client Error"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type", "Client Error"),
    RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable", "Client Error"),
    EXPECTATION_FAILED(417, "Expectation Failed", "Client Error"),
    IM_A_TEAPOT(418, "I'm a teapot", "Client Error"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity", "Client Error"),
    LOCKED(423, "Locked", "Client Error"),
    FAILED_DEPENDENCY(424, "Failed Dependency", "Client Error"),
    TOO_EARLY(425, "Too Early", "Client Error"),
    UPGRADE_REQUIRED(426, "Upgrade Required", "Client Error"),
    PRECONDITION_REQUIRED(428, "Precondition Required", "Client Error"),
    TOO_MANY_REQUESTS(429, "Too Many Requests", "Client Error"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large", "Client Error"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons", "Client Error"),
    
    // 5xx Server Error
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented", "Server Error"),
    BAD_GATEWAY(502, "Bad Gateway", "Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable", "Server Error"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout", "Server Error"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported", "Server Error"),
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates", "Server Error"),
    INSUFFICIENT_STORAGE(507, "Insufficient Storage", "Server Error"),
    LOOP_DETECTED(508, "Loop Detected", "Server Error"),
    NOT_EXTENDED(510, "Not Extended", "Server Error"),
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required", "Server Error");
    
    private final int code;
    private final String reasonPhrase;
    private final String category;
    
    private static final Map<Integer, HttpStatusCode> CODE_MAP = new HashMap<>();
    
    static {
        for (HttpStatusCode status : values()) {
            CODE_MAP.put(status.code, status);
        }
    }
    
    HttpStatusCode(int code, String reasonPhrase, String category) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
        this.category = category;
    }
    
    /**
     * Gets the HTTP status code.
     *
     * @return the status code
     */
    public int getCode() {
        return code;
    }
    
    /**
     * Gets the reason phrase.
     *
     * @return the reason phrase
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }
    
    /**
     * Gets the category of the status code.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Checks if this is an informational status code (1xx).
     *
     * @return true if informational, false otherwise
     */
    public boolean isInformational() {
        return code >= 100 && code < 200;
    }
    
    /**
     * Checks if this is a success status code (2xx).
     *
     * @return true if success, false otherwise
     */
    public boolean isSuccess() {
        return code >= 200 && code < 300;
    }
    
    /**
     * Checks if this is a redirection status code (3xx).
     *
     * @return true if redirection, false otherwise
     */
    public boolean isRedirection() {
        return code >= 300 && code < 400;
    }
    
    /**
     * Checks if this is a client error status code (4xx).
     *
     * @return true if client error, false otherwise
     */
    public boolean isClientError() {
        return code >= 400 && code < 500;
    }
    
    /**
     * Checks if this is a server error status code (5xx).
     *
     * @return true if server error, false otherwise
     */
    public boolean isServerError() {
        return code >= 500 && code < 600;
    }
    
    /**
     * Checks if this represents an error status code (4xx or 5xx).
     *
     * @return true if error, false otherwise
     */
    public boolean isError() {
        return isClientError() || isServerError();
    }
    
    /**
     * Gets the HttpStatusCode for the given code.
     *
     * @param code the HTTP status code
     * @return the HttpStatusCode, or empty if not found
     */
    public static Optional<HttpStatusCode> valueOf(int code) {
        return Optional.ofNullable(CODE_MAP.get(code));
    }
    
    /**
     * Gets the HttpStatusCode for the given code, or throws an exception if not found.
     *
     * @param code the HTTP status code
     * @return the HttpStatusCode
     * @throws IllegalArgumentException if the code is not recognized
     */
    public static HttpStatusCode valueOfOrThrow(int code) {
        return valueOf(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown HTTP status code: " + code));
    }
    
    @Override
    public String toString() {
        return String.format("%d %s", code, reasonPhrase);
    }
}
