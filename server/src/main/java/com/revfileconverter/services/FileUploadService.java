package com.revfileconverter.services;


import com.revfileconverter.dtos.JSONRange;
import com.revfileconverter.entities.Car;
import com.revfileconverter.entities.Person;
import org.json.JSONObject;
import com.revfileconverter.enums.FileLayout;
import com.revfileconverter.repositories.CarRepository;
import com.revfileconverter.repositories.PersonRepository;
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
import java.util.ArrayList;
import java.util.Set;


@Service
public class FileUploadService {
    private final Charset encoding;
    private final FixedLengthTokenizer[] fixedLengthTokenizer;
    private final ResourceLoader resourceLoader;
    private final PersonRepository personRepository;
    private final CarRepository carRepository;
    final String[] paths = {"classpath:person.json", "classpath:car.json","classpath:person2.json"};
    @Autowired
    public FileUploadService(ResourceLoader resourceLoader, PersonRepository personRepository, CarRepository carRepository) throws IOException {
        this.encoding = StandardCharsets.UTF_8;
        this.resourceLoader = resourceLoader;
        this.personRepository = personRepository;
        this.carRepository = carRepository;
        fixedLengthTokenizer = new FixedLengthTokenizer[paths.length];
        for(int i = 0; i < paths.length; i++) {
           setTokenizer(paths[i], i);
        }

    }
    private void setTokenizer(String path, Integer j) throws IOException {
        ArrayList<JSONRange> objectranges = readjson(path);
        Range[] ranges = new Range[objectranges.size()];
        String[] names = new String[objectranges.size()];
        for(int i = 0; i < objectranges.size();i++)
        {
            ranges[i] = new Range(objectranges.get(i).getStartpos(),objectranges.get(i).getEndpos());
            names[i] = objectranges.get(i).getName();
        }
        fixedLengthTokenizer[j] = generateTokenizer(ranges, names);
    }
    private FixedLengthTokenizer generateTokenizer(Range[] ranges, String[] names) {
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames(names);
        tokenizer.setColumns(ranges);
        return tokenizer;
    }
    private ArrayList<JSONRange> readjson(String location) throws IOException{
        Resource resource = resourceLoader.getResource(location);
        File file = resource.getFile();
        String content = new String(Files.readAllBytes(file.toPath()));
        JSONObject jsonObject = new JSONObject(content);
        Set<String> keys = jsonObject.keySet();
        ArrayList<JSONRange> ranges = new ArrayList<>();
        for(String name: keys)
        {
            JSONObject positions = jsonObject.getJSONObject(name);
            JSONRange temprange = new JSONRange();
            temprange.setStartpos(positions.getInt("startpos"));
            temprange.setEndpos(positions.getInt("endpos"));
            temprange.setName(name);
            ranges.add(temprange);
        }
        return ranges;
    }

    public Person parsePersonFile(MultipartFile file,FileLayout fileLayout) throws IOException {
        FieldSet fieldSet = fixedLengthTokenizer[fileLayout.getValue()].tokenize(new String(file.getBytes(), encoding));
        Person person = new Person();
        person.setFirstname(fieldSet.readString("firstname"));
        person.setLastname(fieldSet.readString("lastname"));
        person.setEmail(fieldSet.readString("email"));
        person.setPhone(fieldSet.readString("phone"));
        person.setDob(fieldSet.readString("dob"));
        person.setIsUsCitizen(fieldSet.readString("isUsCitizen").equalsIgnoreCase("T")
        || fieldSet.readString("isUsCitizen").equalsIgnoreCase("Y"));
        return personRepository.save(person);
    }
    public Car parseCarFile(MultipartFile file,FileLayout fileLayout) throws IOException {
        FieldSet fieldSet = fixedLengthTokenizer[fileLayout.getValue()].tokenize(new String(file.getBytes(), encoding));
        Car car = new Car();
        car.setManufacturer(fieldSet.readString("manufacturer"));
        car.setModel(fieldSet.readString("model"));
        car.setColor(fieldSet.readString("color"));
        car.setState(fieldSet.readString("state"));
        car.setLicenseplate(fieldSet.readString("licenseplate"));
        car.setYear(fieldSet.readString("year"));
        return carRepository.save(car);
    }
    public Object parseFile(MultipartFile file, FileLayout fileLayout) throws IOException {
        switch(fileLayout){
            case PERSON,PERSON2:
                return parsePersonFile(file,fileLayout);
            case CAR:
                return parseCarFile(file,fileLayout);
            default:
                break;
       }
       return null;
    }
}
