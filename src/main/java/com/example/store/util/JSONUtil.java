package com.example.store.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.util.StringUtils;

public class JSONUtil {

    public static boolean isNull(String json) {
        return !StringUtils.hasText(json);
    }

    public static boolean isFieldNull(String json, String field) {
        if (isNull(json)) {
            return true;
        }
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.get(field) == null || jsonObject.get(field).isJsonNull();
    }

    public static boolean isIdNull(String json) {
        return isFieldNull(json, "id");
    }
}
