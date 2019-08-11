package net.alanwei.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Util {
    private final static Gson gson = new GsonBuilder().create();

    private Util() {
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T parseJson(String json, Class<T> t) {
        return gson.fromJson(json, t);
    }

    public static void log(String txt) {
        System.out.println(txt);
    }
}
