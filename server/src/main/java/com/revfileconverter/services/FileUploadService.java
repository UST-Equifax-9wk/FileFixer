package com.revfileconverter.services;


import com.revfileconverter.entities.Person;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class FileUploadService {
    private FixedLengthTokenizer fixedLengthTokenizer = fixedLengthTokenizer();

   public Person parseFile(MultipartFile file) throws IOException {
       FieldSet fieldSet = fixedLengthTokenizer.tokenize(new String(file.getBytes(), StandardCharsets.UTF_8));
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

    private FixedLengthTokenizer fixedLengthTokenizer(){
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("firstname","lastname","dob","email","phone","isUsCitizen");
        tokenizer.setColumns(new Range(1,15),//15 characters first name
                new Range(16,30),//15 characters last name
                new Range(31,38),//8 characters dob
                new Range(39,68),//30 characters email
                new Range(69,78),//10 characters phone
                new Range(79,79)//1 character isUsCitizen
        );
        return tokenizer;
    }
}
