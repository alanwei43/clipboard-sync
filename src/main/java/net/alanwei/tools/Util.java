package net.alanwei.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

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
        System.out.printf("[%s] %s\n", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), txt);
    }

    public static String calculateHash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] bytes = md.digest(data);
            return DatatypeConverter.printHexBinary(bytes).toUpperCase();
        } catch (Throwable ex) {
        }
        return null;
    }

    public static String toBase64(byte[] data) {
        if (data == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] fromBase64(String base64String) {
        if (base64String == null) {
            return null;
        }
        return Base64.getDecoder().decode(base64String);
    }
}
