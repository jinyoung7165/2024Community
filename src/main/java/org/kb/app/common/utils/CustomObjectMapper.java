package org.kb.app.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CustomObjectMapper<T> {

    public static ObjectMapper objectMapper = new ObjectMapper();

    public CustomObjectMapper() {
        objectMapper
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static<F,T> T to(F from, Class<T> to){
        if(from == null) return null;
        return objectMapper.convertValue(from,to);
    }


    // List를 변환해준다.
    public static<F,T> List<T> toList(List<F> froms, Class<T> to) {
        List<T> ts = new ArrayList<>();
        if (froms.size() == 0) return ts;
        froms.stream().forEach(f -> ts.add(objectMapper.convertValue(f,to)));
        return ts;
    }

    //json을 받아 hashmap으로 변환하는 메소드
    public static Map<String, Object> jsonToMap(JSONObject json) {
        Map<String, Object> retMap = new HashMap<>();

        if(json != null) {
            retMap = toMap(json);
        }
        return retMap;
    }

    //json객체 안에 또다른 json 객체가 있을 경우
    public static Map<String, Object> toMap(JSONObject object) {
        Map<String, Object> map = new HashMap<>();

        @SuppressWarnings("rawtypes")
        Set keys = object.keySet();
        @SuppressWarnings("unchecked")
        Iterator<String> keysItr = keys.iterator();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    //json객체 안에 json 배열이 있을경우
    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
}