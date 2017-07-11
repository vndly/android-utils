package com.mauriciotogneri.androidutils.mock;

import android.text.TextUtils;

import com.mauriciotogneri.androidutils.Encoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest
{
    private final String method;
    private final String route;
    private final List<String> cookies;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(String method, String route, List<String> cookies, Map<String, String> headers, String body)
    {
        this.method = method;
        this.route = route;
        this.cookies = cookies;
        this.headers = headers;
        this.body = body;
    }

    public boolean matches(String method, String pattern)
    {
        return this.method.equals(method) && route.matches(pattern);
    }

    public List<String> cookies()
    {
        return cookies;
    }

    public Map<String, String> headers()
    {
        return headers;
    }

    public List<String> pathParameters(String regex)
    {
        List<String> result = new ArrayList<>();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(route);

        while (matcher.find())
        {
            result.add(matcher.group(1));
        }

        return result;
    }

    public Map<String, String> queryParameters()
    {
        Map<String, String> result = new HashMap<>();

        try
        {
            String[] parts = route.split("\\?")[1].split("&");

            for (String part : parts)
            {
                String[] param = part.split("=");

                result.put(param[0], Encoding.urlDecode(param[1]));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return result;
    }

    public Map<String, String> formParameters()
    {
        Map<String, String> result = new HashMap<>();

        try
        {
            String[] parts = body.split("&");

            for (String part : parts)
            {
                String[] param = part.split("=");

                result.put(param[0], Encoding.urlDecode(param[1]));
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static HttpRequest fromInputStream(InputStream inputStream) throws IOException
    {
        String method = null;
        String route = null;
        List<String> cookies = new ArrayList<>();
        Map<String, String> headers = new HashMap<>();
        String body = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        int contentLength = 0;

        while (!TextUtils.isEmpty(line = reader.readLine()))
        {
            if ((method == null) || (route == null))
            {
                String[] parts = line.split(" ");
                method = parts[0];
                route = parts[1];
            }
            else
            {
                if (line.startsWith("Content-Length:"))
                {
                    contentLength = Integer.parseInt(line.replace("Content-Length:", "").trim());
                }

                String[] parts = line.split(":");
                String name = parts[0];
                String value = parts[1];

                if (name.equals("Cookie"))
                {
                    cookies.add(value);
                }
                else
                {
                    headers.put(name, value);
                }
            }
        }

        if (contentLength != 0)
        {
            char[] buffer = new char[contentLength];
            reader.read(buffer);
            body = new String(buffer);
        }

        return new HttpRequest(method, route, cookies, headers, body);
    }
}