package com.ysj.usb.usbtest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * 简单的 Gson 工具类,便于打印信息
 * <p>
 *
 * @author Ysj
 * Create time: 2018/11/29 21:38
 */
public class GsonUtil
{

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Double.class, (JsonSerializer<Double>) (src, typeOfSrc, context) ->
            {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            })
            .setPrettyPrinting()
            .create();

    public static Gson getGson()
    {
        return gson;
    }

    public static String toJson(Object o)
    {
        if (o == null)
            return "";
        return gson.toJson(o);
    }

    public static <T> T toObj(String jsonData, Object object)
    {
        if (object instanceof Type)
        {
            return gson.fromJson(jsonData, (Type) object);
        } else
        {
            return gson.fromJson(jsonData, (Class<T>) object);
        }
    }

}
