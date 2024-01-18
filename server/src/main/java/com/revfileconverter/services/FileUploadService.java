package com.revfileconverter.services;


import com.revfileconverter.dtos.JSONRange;
import com.revfileconverter.entities.Car;
import com.revfileconverter.entities.Person;
import org.json.JSONObject;
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
    private Charset encoding;
    private FixedLengthTokenizer[] fixedLengthTokenizer;
    private final ResourceLoader resourceLoader;
    private final PersonRepository personRepository;

    @Autowired//get rid of those exceptions
    public FileUploadService(ResourceLoader resourceLoader, PersonRepository personRepository) throws IOException {
        this.encoding = StandardCharsets.UTF_8;
        this.resourceLoader = resourceLoader;
        this.personRepository = personRepository;
        fixedLengthTokenizer = new FixedLengthTokenizer[2];
        ArrayList<JSONRange> objectranges = readjson("classpath:person.json");
        Range[] ranges = new Range[objectranges.size()];
        String[] names = new String[objectranges.size()];
        for(int i = 0; i < objectranges.size();i++)
        {
            ranges[i] = new Range(objectranges.get(i).getStartpos(),objectranges.get(i).getEndpos());
            names[i] = objectranges.get(i).getName();
        }
        fixedLengthTokenizer[0] = generateTokenizer(ranges, names);
        objectranges = readjson("classpath:car.json");
        ranges = new Range[objectranges.size()];
        names = new String[objectranges.size()];
        for(int i = 0; i < objectranges.size();i++)
        {
            ranges[i] = new Range(objectranges.get(i).getStartpos(),objectranges.get(i).getEndpos());
            names[i] = objectranges.get(i).getName();
        }
        fixedLengthTokenizer[1] = generateTokenizer(ranges, names);

    }

    public Person parsePersonFile(MultipartFile file) throws IOException {
        FieldSet fieldSet = fixedLengthTokenizer[0].tokenize(new String(file.getBytes(), encoding));
        Person person = new Person();
        //clean up by using some type of loop to store the data
        person.setFirstname(fieldSet.readString("firstname"));
        person.setLastname(fieldSet.readString("lastname"));
        person.setEmail(fieldSet.readString("email"));
        person.setPhone(fieldSet.readString("phone"));
        person.setDob(fieldSet.readString("dob"));
        person.setIsUsCitizen(fieldSet.readString("isUsCitizen").equals("T"));
        return person;
    }
    public Car parseCarFile(MultipartFile file) throws IOException {
        FieldSet fieldSet = fixedLengthTokenizer[1].tokenize(new String(file.getBytes(), encoding));
        Car car = new Car();
        //clean up by using some type of loop to store the data
        car.setManufacturer(fieldSet.readString("manufacturer"));
        car.setModel(fieldSet.readString("model"));
        car.setColor(fieldSet.readString("color"));
        car.setState(fieldSet.readString("state"));
        car.setLicenseplate(fieldSet.readString("licenseplate"));
        car.setYear(fieldSet.readString("year"));
        return car;
    }
    //didn't just use endpos+1 in case we wanted some characters ignored
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
}
