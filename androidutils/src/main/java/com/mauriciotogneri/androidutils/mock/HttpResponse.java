package com.mauriciotogneri.androidutils.mock;

import com.mauriciotogneri.javautils.Json;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse
{
    private final HttpResponseCode code;
    private final List<String> headers;
    private final byte[] body;

    public HttpResponse(HttpResponseCode code, List<String> headers, byte[] body)
    {
        this.code = code;
        this.headers = headers;
        this.body = body;
    }

    public void respond(OutputStream outputStream) throws IOException
    {
        try (PrintStream output = new PrintStream(outputStream))
        {
            output.println(String.format("HTTP/1.0 %s", code));

            for (String header : headers)
            {
                output.println(header);
            }

            if (body != null)
            {
                output.println("Content-Length: " + body.length);
                output.println();
                output.write(body);
            }
            else
            {
                output.println();
            }

            output.flush();
        }
    }

    public static class Builder
    {
        private final HttpResponseCode code;
        private final List<String> headers;
        private byte[] body;

        public Builder(HttpResponseCode code)
        {
            this.code = code;
            this.headers = new ArrayList<>();
        }

        public Builder header(String name, String value)
        {
            headers.add(String.format("%s: %s", name, value));

            return this;
        }

        public Builder string(String body)
        {
            this.body = body.getBytes();

            return this;
        }

        public Builder bytes(byte[] body)
        {
            this.body = body;

            return this;
        }

        public Builder json(Object object)
        {
            this.body = Json.json(object).getBytes();

            return this;
        }

        public Builder array(Object... object)
        {
            this.body = Json.json(object).getBytes();

            return this;
        }

        public Builder list(List<?> object)
        {
            this.body = Json.json(object).getBytes();

            return this;
        }

        public HttpResponse build()
        {
            return new HttpResponse(code, headers, body);
        }
    }
}