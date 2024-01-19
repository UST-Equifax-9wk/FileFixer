package com.revfileconverter.tools;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
public class JsonGenerator {

    private static String[] keys = {"manufacturer", "model", "color","state","licenseplate","year"};
    private static int[] lengths = {20,25,15,2,8,4};
    private static final String FILENAME = "car2.json";

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
        try(FileWriter newFileWriter = new FileWriter("src/main/resources/" + FILENAME)) {
            newFileWriter.write(output.toString());
            newFileWriter.flush();
        }
    }

}
