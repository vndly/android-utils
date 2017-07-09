package com.mauriciotogneri.androidutils.mock;

public abstract class EndPoint
{
    private final String method;
    private final String pattern;

    protected static final String GET = "GET";
    protected static final String PUT = "PUT";
    protected static final String POST = "POST";
    protected static final String PATCH = "PATCH";
    protected static final String DELETE = "DELETE";

    protected EndPoint(String method, String pattern)
    {
        this.method = method;
        this.pattern = pattern;
    }

    protected String pattern()
    {
        return pattern;
    }

    public boolean matches(HttpRequest httpRequest)
    {
        return httpRequest.matches(method, pattern);
    }

    public abstract HttpResponse process(HttpRequest httpRequest);
}