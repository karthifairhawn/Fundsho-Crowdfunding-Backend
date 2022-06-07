package com.api.spring.boot.funsho.api.resource;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class Miscellaneous {

    public static final String folderPath =  "incoming-files//";
    public static final Path filePath = Paths.get(folderPath);    
    

    // Save File and return url Starts 
    @PostMapping("/uploadFiles")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            createDirIfNotExist();
            byte[] bytes = new byte[0];
            bytes = file.getBytes();
            String newName = new Date().hashCode()+file.getOriginalFilename();
            Files.write(Paths.get(folderPath + newName), bytes);
            return newName;            
        } catch (Exception e) {            
            return "failed";
        }
    }

    private void createDirIfNotExist() {
        //create directory to save the files
        File directory = new File(folderPath);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    @GetMapping("/files")
    public ResponseEntity<String[]> getListFiles() {
        return ResponseEntity.status(HttpStatus.OK).body(new java.io.File(folderPath).list());
    }

    // Save File Ends...
    
}
