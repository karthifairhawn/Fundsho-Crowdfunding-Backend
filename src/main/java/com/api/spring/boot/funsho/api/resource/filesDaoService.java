package com.api.spring.boot.funsho.api.resource;


import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.spring.boot.funsho.api.entity.Files;
import com.api.spring.boot.funsho.api.repository.filesRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class ResponseMessage {
    private String url;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ResponseFile {
    private String name;
    private String url;
    private String type;
    private long size;    
}



@Service
class FileStorageService {
    @Autowired
    private filesRepository fileDBRepository;

    public Files store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Files FileDB = new Files(fileName, file.getContentType(), file.getBytes());
        return fileDBRepository.save(FileDB);
    }
    public Files getFile(String id) { return fileDBRepository.findById(id).get(); }

    public Stream<Files> getAllFiles() { return fileDBRepository.findAll().stream(); }
}


@RestController
@CrossOrigin
public class filesDaoService {

    @Autowired
    private FileStorageService storageService;
    

    @PostMapping("/images")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
      String url = "";
      try {
        Files uploadedFile = storageService.store(file);
        String fileDownloadUri = ServletUriComponentsBuilder
        .fromCurrentContextPath()
        .path("/files/")
        .path(uploadedFile.getId())
        .toUriString();

        url = fileDownloadUri;
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(url));
      } catch (Exception e) {
        url = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(url));
      }
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
      Files fileDB = storageService.getFile(id);
      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
          .body(fileDB.getData());
    }

    // @GetMapping("/files")
    // public ResponseEntity<List<ResponseFile>> getListFiles() {
    //   List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
    //     String fileDownloadUri = ServletUriComponentsBuilder
    //         .fromCurrentContextPath()
    //         .path("/files/")
    //         .path(dbFile.getId())
    //         .toUriString();
    //     return new ResponseFile(dbFile.getName(),fileDownloadUri,dbFile.getType(),dbFile.getData().length);
    //   }).collect(Collectors.toList());
    //   return ResponseEntity.status(HttpStatus.OK).body(files);
    // }


}
