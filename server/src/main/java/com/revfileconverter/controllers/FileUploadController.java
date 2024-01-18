package com.revfileconverter.controllers;
import com.revfileconverter.entities.Person;
import com.revfileconverter.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/readFile")
    public Person parseFile(@RequestParam("flatFile") MultipartFile file) throws IOException{
        return fileUploadService.parsePersonFile(file);
    }

}