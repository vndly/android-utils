package com.mauriciotogneri.androidutils.mock;

public enum HttpResponseCode
{
    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CONFLICT(409, "Conflict"),
    PRECONDITION_FAILED(412, "Precondition Failed"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    public final int code;
    public final String message;

    HttpResponseCode(int code, String message)
    {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString()
    {
        return String.format("%s %s", code, message);
    }
}