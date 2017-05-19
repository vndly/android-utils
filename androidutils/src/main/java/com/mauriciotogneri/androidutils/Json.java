package com.mauriciotogneri.androidutils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Json
{
    private static final Gson gson = new Gson();

    public static <T> T fromJson(String string, Class<T> type)
    {
        return gson.fromJson(string, type);
    }

    public static <T> T fromJson(JsonElement json, Class<T> type)
    {
        return gson.fromJson(json, type);
    }

    public static <T> T fromJson(Map<?, ?> map, Class<T> type)
    {
        String json = gson.toJson(map);

        return gson.fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type)
    {
        return gson.fromJson(json, type);
    }

    public static String toJson(Object object)
    {
        return gson.toJson(object);
    }

    public static <A, B extends JsonToObject<A>> List<A> list(B[] list)
    {
        List<A> result = new ArrayList<>();

        if (list != null)
        {
            for (B json : list)
            {
                result.add(json.object());
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <A, B extends JsonToObject<A>> A[] array(B[] list, Class<A> clazz)
    {
        List<A> newList = list(list);

        A[] result = (A[]) Array.newInstance(clazz, list.length);
        newList.toArray(result);

        return result;
    }

    public interface JsonToObject<T>
    {
        T object();
    }
}