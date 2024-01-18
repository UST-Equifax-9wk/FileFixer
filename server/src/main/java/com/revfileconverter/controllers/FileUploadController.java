package com.revfileconverter.controllers;
import com.revfileconverter.enums.FileLayout;
import com.revfileconverter.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
    @PostMapping("/readFile/{fileLayout}")
    public Object parseFile(@RequestParam("flatFile") MultipartFile file, @PathVariable FileLayout fileLayout) throws IOException{
        return fileUploadService.parseFile(file, fileLayout);
    }

}