package com.vn.fruitcart.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vn.fruitcart.domain.dto.response.UploadImgRes;
import com.vn.fruitcart.service.FileService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<UploadImgRes> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "") String folder) throws URISyntaxException, IOException {
            String filename = fileService.store(file, folder);
            UploadImgRes res = new UploadImgRes(filename, Instant.now());
            return ResponseEntity.ok().body(res);
       
    }
}
