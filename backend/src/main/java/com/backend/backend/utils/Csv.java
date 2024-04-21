package com.backend.backend.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;

public class Csv {
    public static List<HashMap<String, Object>> csvToJson(String content) {
        List<HashMap<String, Object>> json = new ArrayList<>();

        String[] lines = content.split("\n");
        String[] cab = getCells(lines[0]);
        lines = Arrays.copyOfRange(lines, 1, lines.length);

        for (String line : lines) {
            String[] fields = getCells(line);
            HashMap<String, Object> map = new HashMap<>();
            for (String propName : cab) {
                String value = fields[Arrays.asList(cab).indexOf(propName)].replace("\"", "");
                map.put(toCamelCase(propName), value);
            }
            json.add(map);
        }

        return json;
    }

    static String toCamelCase(String s) {
        String[] words = s.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static List<HashMap<String, Object>> csvToJson(byte[] bytes){
        String content = new String(bytes);
        List<HashMap<String, Object>> json = new ArrayList<>();

        String[] lines = content.split("\n");
        String[] cab = getCells(lines[0]);
        lines = Arrays.copyOfRange(lines, 1, lines.length);

        for (String line : lines) {
            String[] fields = getCells(line);
            HashMap<String, Object> map = new HashMap<>();
            for (String propName : cab) {
                String value = fields[Arrays.asList(cab).indexOf(propName)].replace("\"", "");
                map.put(toCamelCase(propName), value);
            }
            json.add(map);
        }

        return json;

    }

    public static String[] getCells(String line) {
        return line.contains(";") ? line.split(";(?=([^\"]*\"[^\"]*\")*[^\"]*$)") : line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
