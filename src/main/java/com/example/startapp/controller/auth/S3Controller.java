package com.example.startapp.controller.auth;

import com.example.startapp.service.auth.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
//
//@RestController
//@RequestMapping("/s3")
//@RequiredArgsConstructor
//public class S3Controller {
//
//    private final S3Service s3Service;
//
//    @GetMapping
//    public String health() {
//        return "UP";
//    }
//
//    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        s3Service.uploadFile(file.getOriginalFilename(), file);
//        return "File uploaded";
//    }
//
//    @GetMapping("/download/{fileName}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(new InputStreamResource(s3Service.getFile(fileName).getObjectContent()));
//    }
//
//    @GetMapping("/view/{fileName}")
//    public ResponseEntity<InputStreamResource> viewFile(@PathVariable String fileName) {
//        var s3Object = s3Service.getFile(fileName);
//        var content = s3Object.getObjectContent();
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_PNG)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\""+fileName+"\"")
//                .body(new InputStreamResource(content));
//    }
//}