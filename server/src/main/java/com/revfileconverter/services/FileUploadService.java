package com.revfileconverter.services;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.revfileconverter.dtos.JSONRange;
import com.revfileconverter.enums.DataTypes;
import com.revfileconverter.enums.FileLayout;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class FileUploadService {
    private final Charset encoding;
    private final FixedLengthTokenizer[] fixedLengthTokenizer;
    private final ResourceLoader resourceLoader;
    private final ArrayList<ArrayList<JSONRange>> rangesarray;
    private String newline = "";//\r?\n|\r
    final String[] paths = {"classpath:person.json","classpath:person2.json", "classpath:car.json","classpath:car2.json"};
    @Autowired
    public FileUploadService(ResourceLoader resourceLoader) throws IOException {
        this.encoding = StandardCharsets.UTF_8;
        this.resourceLoader = resourceLoader;
        fixedLengthTokenizer = new FixedLengthTokenizer[paths.length + 1];//need this + 1 for the custom tokenizer
        rangesarray = new ArrayList<>(paths.length + 1);
        for(int i = 0; i < paths.length; i++) {
            ArrayList<JSONRange> objectranges = readjson(paths[i]);
            rangesarray.add(objectranges);
        }

    }
    private void setTokenizer(String path, Integer j) throws IOException {
        ArrayList<JSONRange> objectranges = readjson(path);
        if(rangesarray.get(j).isEmpty())
        {
            rangesarray.add(objectranges);
        }
        fixedLengthTokenizer[j] = generateTokenizer(objectranges);
    }
    private ArrayList<JSONRange> readjson(String location) throws IOException{
        Resource resource = resourceLoader.getResource(location);
        File file = resource.getFile();
        String content = new String(Files.readAllBytes(file.toPath()));
        return setTokenizerRanges(content);
    }

    private ArrayList<JSONRange> setTokenizerRanges(String content) {
        JsonObject userData = new Gson().fromJson(content, new TypeToken<JsonObject>() {
        }.getType());
        Set<String> keys = userData.keySet();
        ArrayList<JSONRange> ranges = new ArrayList<>();
        newline = "";
        for(String name: keys)
        {
            if(name.equals("delimiter"))
            {
                JsonPrimitive object = userData.getAsJsonPrimitive(name);
                newline = object.getAsString();
            }
            else {
                JsonObject object = userData.getAsJsonObject(name);
                JSONRange temprange = new JSONRange();
                temprange.setStartpos((object.get("startpos").getAsInt()));
                temprange.setEndpos((object.get("endpos").getAsInt()));
                if (object.get("datatype") != null) {
                    temprange.setDataTypes(DataTypes.valueOf(object.get("datatype").getAsString()));
                } else {
                    temprange.setDataTypes(DataTypes.STRING);//default value is string
                }
                temprange.setName(name);
                ranges.add(temprange);
            }
        }
        return ranges;
    }
    private int findmax(ArrayList<JSONRange> ranges)
    {
        int max = ranges.get(0).getEndpos();
        for(int i = 1 ; i < ranges.size();i++)
        {
            int temp = ranges.get(i).getEndpos();
            if(max < temp)
            {
                max = temp;
            }
        }
        return max;
    }
    private FixedLengthTokenizer generateTokenizer(ArrayList<JSONRange> ranges) {
        Range[] fieldrange = new Range[ranges.size()];
        String[] names = new String[ranges.size()];
        for(int i = 0; i < ranges.size();i++)
        {
            fieldrange[i] = new Range(ranges.get(i).getStartpos(),ranges.get(i).getEndpos());
            names[i] = ranges.get(i).getName();
        }
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames(names);
        tokenizer.setColumns(fieldrange);
        return tokenizer;
    }
    private Map<String,Object> setjsonObject(FieldSet fieldSet, Map<String, DataTypes> dataMap)
    {
        Map<String, Object> jsonObject = new LinkedHashMap<>();
        for (String value : fieldSet.getNames()) {
            switch (dataMap.get(value)) {
                case INTEGER:
                    jsonObject.put(value, Long.parseLong(fieldSet.readString(value)));
                    break;
                case NUMBER:
                    jsonObject.put(value, Double.parseDouble(fieldSet.readString(value)));
                    break;
                case BOOLEAN:
                    jsonObject.put(value, fieldSet.readString(value).equalsIgnoreCase("T") ||
                            fieldSet.readString(value).equalsIgnoreCase("Y") ||
                            fieldSet.readString(value).equals("1"));
                    break;
                case STRING:
                    jsonObject.put(value, fieldSet.readString(value));
                    break;
                default:
                    break;
            }
        }
        return jsonObject;
    }
    public List<Map<String,Object>> parseFixedFile(MultipartFile file, FileLayout fileLayout, Map<String, DataTypes> dataMap,int endline) throws IOException {
        List<Map<String,Object>> outputlist = new ArrayList<>();
        if(!newline.isEmpty()) {
            String[] filedata = (new String(file.getBytes(), encoding)).split(newline);
            for (String line : filedata) {
                FieldSet fieldSet = fixedLengthTokenizer[fileLayout.getValue()].tokenize(line);
                Map<String, Object> jsonObject = setjsonObject(fieldSet, dataMap);
                outputlist.add(jsonObject);
            }
        }
        else{
            int i = 0;
            String filedata = new String(file.getBytes(), encoding);
            while(i < filedata.length())
            {
                FieldSet fieldSet = fixedLengthTokenizer[fileLayout.getValue()].tokenize(filedata.substring(i,i+endline));
                Map<String, Object> jsonObject = setjsonObject(fieldSet,dataMap);
                outputlist.add(jsonObject);
                i = i + endline;
            }
        }
        return outputlist;
    }
    //parse file for one of our preset JSON specifications
    public Object parseFile(MultipartFile file, FileLayout fileLayout) throws IOException {
        setTokenizer(paths[fileLayout.getValue()], fileLayout.getValue());
        ArrayList<JSONRange> ranges = rangesarray.get(fileLayout.getValue());
        int max = findmax(ranges);
        LinkedHashMap<String, DataTypes> dataMap = ranges.stream().collect(Collectors.toMap(JSONRange::getName, JSONRange::getDataTypes, (x, y) -> y, LinkedHashMap::new));
        List<Map<String,Object>> result = parseFixedFile(file, fileLayout,dataMap,max);
        return result.size() == 1 ? result.get(0) : result;
    }
    //overloaded parse file to be used with a custom preset
    public Object parseFile(MultipartFile file, FileLayout fileLayout, MultipartFile specifications) throws IOException {
        String content = new String(specifications.getBytes());
        ArrayList<JSONRange> ranges = setTokenizerRanges(content);
        int max = findmax(ranges);
        LinkedHashMap<String, DataTypes> dataMap = ranges.stream().collect(Collectors.toMap(JSONRange::getName, JSONRange::getDataTypes, (x, y) -> y, LinkedHashMap::new));
        fixedLengthTokenizer[fileLayout.getValue()] = generateTokenizer(ranges);
        List<Map<String,Object>> result = parseFixedFile(file, fileLayout,dataMap,max);
        return result.size() == 1 ? result.get(0) : result;
    }


}
