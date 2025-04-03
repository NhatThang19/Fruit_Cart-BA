package com.vn.fruitcart.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Path getBasePath() throws IOException;

    void createDirectory(String folder) throws URISyntaxException, IOException;

    String store(MultipartFile file, String folder) throws URISyntaxException, IOException;

    boolean isImage(MultipartFile file);

    String generateUniqueFilename(String originalFilename, String extension);

    void logImageInfo(String filename, long size, BufferedImage image);
}
