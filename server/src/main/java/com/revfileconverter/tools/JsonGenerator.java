package com.revfileconverter.tools;
import com.google.gson.JsonObject;
import com.revfileconverter.enums.DataTypes;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
public class JsonGenerator {

    private static String[] keys = {"flavor","calories","price","cheese","isVegetarian"};
    private static int[] lengths = {30,5,6,15,1};
    private static DataTypes[] dataTypes = {DataTypes.STRING,DataTypes.INTEGER,DataTypes.NUMBER,DataTypes.STRING,DataTypes.BOOLEAN};
    private static final String FILENAME = "pizza.json";
    private static final String delimiter = "";

    public static void main(String[] args) throws IOException {
        int pos = 1;
        JsonObject output = new JsonObject();
        for (int i = 0; i < keys.length; i++) {
            JsonObject inner = new JsonObject();
            inner.addProperty("startpos", pos);
            inner.addProperty("endpos", pos + lengths[i] - 1);
            inner.addProperty("datatype", dataTypes[i].toString());
            pos += lengths[i];
            output.add(keys[i], inner);
        }
        output.addProperty("delimiter", delimiter);

        try(FileWriter newFileWriter = new FileWriter("src/main/resources/" + FILENAME)) {
            newFileWriter.write(output.toString());
            newFileWriter.flush();
        }
    }

}
