package com.revfileconverter.controllers;
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
    //Used object instead of a specific entity since we can potentially return different types of entities
    @PostMapping("/readFile")
    public Object parseFile(@RequestParam("flatFile") MultipartFile file) throws IOException{
        return fileUploadService.parseCarFile(file);
    }

}