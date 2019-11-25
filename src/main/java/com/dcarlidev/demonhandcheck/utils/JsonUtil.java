/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dcarlidev.demonhandcheck.utils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 *
 * @author carlos
 */
public class JsonUtil {

    private final JSONParser parser = new JSONParser();

    public String createDataJson(Map<String, String> values) throws ParseException, InterruptedException {
        JSONObject obj = new JSONObject();
        String actualDate = LocalDateTime.now().toString();
        obj.put("lastOpenDate", actualDate);
        if (values != null && !values.isEmpty()) {
            values.forEach((key, value) -> {
                obj.put(key, value);
            });
        }
        return obj.toJSONString();
    }

    public Map<String, String> getPropertiesFromJsonObject(String json) throws ParseException {
        JSONObject obj = (JSONObject) parser.parse(json);
        Map<String, String> properties = new HashMap();
        if (!obj.isEmpty()) {
            obj.keySet().forEach(key -> {
                properties.put(key.toString(), obj.get(key.toString()).toString());
            });
            return properties;
        } else {
            return null;
        }
    }
}
