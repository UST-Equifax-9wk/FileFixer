package com.revfileconverter.tools;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class JsonGenerator {

    private static String[] keys = {"height", "width", "length"};
    private static int[] lengths = {3, 4 ,5};
    private static final String FILENAME = "Entity.json";

    public static void main(String[] args) throws IOException {
        int pos = 1;
        JSONObject output = new JSONObject();
        for (int i = 0; i < keys.length; i++) {
            JSONObject inner = new JSONObject();
            inner.put("startpos", pos);
            inner.put("endpos", pos + lengths[i] - 1);
            pos += lengths[i];
            output.put(keys[i], inner);
        }
        ObjectMapper om = new ObjectMapper();
        File newFile = new File("classpath:" + FILENAME);
        System.out.println(newFile.getPath());
        om.writeValue(newFile, output);
    }

}
