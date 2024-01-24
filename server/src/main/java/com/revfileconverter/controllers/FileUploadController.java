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



@RestController
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class FileUploadController {
    private final FileUploadService fileUploadService;
    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    //Used object instead of a specific entity since we can potentially return different types of entities
    @PostMapping("/readFile/{fileLayout}")
    public Object parseFile(@RequestParam("flatFile") MultipartFile file, @RequestParam(value = "specJSON", required = false) MultipartFile specifications, @PathVariable FileLayout fileLayout) throws IOException{
        if(specifications != null && fileLayout.name().equals("CUSTOM"))
        {
            return fileUploadService.parseFile(file, fileLayout, specifications);
        }
        return fileUploadService.parseFile(file, fileLayout);
    }
    @ExceptionHandler(IncorrectLineLengthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> internalErrorHandler(IncorrectLineLengthException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ClassCastException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> internalErrorHandler(ClassCastException e) {
        return new ResponseEntity<>("Delimiter can only be used to denote a delimiter for a fixed file", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> internalErrorHandler(NumberFormatException e) {
        return new ResponseEntity<>("Bad input file", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> internalErrorHandler(IllegalArgumentException e) {
        return new ResponseEntity<>("Datatype not valid", HttpStatus.BAD_REQUEST);
    }
}