package com.mauriciotogneri.androidutils.mock;

import com.mauriciotogneri.javautils.Json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpResponse
{
    private final HttpResponseCode code;
    private final Map<String, String> headers;
    private final String body;

    public HttpResponse(HttpResponseCode code, Map<String, String> headers, String body)
    {
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public void respond(OutputStream outputStream) throws IOException
    {
        PrintStream output = null;

        try
        {
            output = new PrintStream(outputStream);
            output.println(String.format("HTTP/1.0 %s", code));

            for (Entry<String, String> entry : headers.entrySet())
            {
                output.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
            }

            if (body != null)
            {
                byte[] bytes = body.getBytes();
                output.println("Content-Length: " + bytes.length);
                output.println();
                output.write(bytes);
            }
            else
            {
                output.println();
            }

            output.flush();
        }
        finally
        {
            if (null != output)
            {
                output.close();
            }
        }
    }

    public static class Builder
    {
        private final HttpResponseCode code;
        private Map<String, String> headers;
        private String body;

        public Builder(HttpResponseCode code)
        {
            this.code = code;
            this.headers = new HashMap<>();
        }

        public Builder header(String name, String value)
        {
            headers.put(name, value);

            return this;
        }

        public Builder string(String body)
        {
            this.body = body;

            return this;
        }

        public Builder json(Object object)
        {
            this.body = Json.json(object);

            return this;
        }

        public Builder array(Object... object)
        {
            this.body = Json.json(object);

            return this;
        }

        public HttpResponse build()
        {
            return new HttpResponse(code, headers, body);
        }
    }
}