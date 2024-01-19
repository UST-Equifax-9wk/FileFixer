package com.revfileconverter.controllers;
import com.revfileconverter.enums.FileLayout;
import com.revfileconverter.services.FileUploadService;
import org.springframework.batch.item.file.transform.IncorrectLineLengthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.NoSuchElementException;

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
    @ExceptionHandler(IncorrectLineLengthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> internalErrorHandler(IncorrectLineLengthException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}