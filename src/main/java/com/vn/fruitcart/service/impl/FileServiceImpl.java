package com.vn.fruitcart.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.vn.fruitcart.service.FileService;

@Service
public class FileServiceImpl implements FileService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = new HashSet<>(
            Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"));

    @Value("${upload.file.base.path}")
    private String baseURI;

    @Override
    public Path getBasePath() throws IOException {
        try {
            Path path = Paths.get(new URI(baseURI));
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            return path;
        } catch (URISyntaxException e) {
            throw new IOException("Invalid base URI configuration", e);
        }
    }

    @Override
    public void createDirectory(String folder) throws URISyntaxException, IOException {
        Path basePath = getBasePath();
        Path folderPath = basePath.resolve(folder);
        
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
                System.out.printf(">>> Created directory: %s%n", folderPath);
            } catch (IOException e) {
                System.err.printf(">>> Failed to create directory: %s, error: %s%n", 
                                folderPath, e.getMessage());
                throw e;
            }
        }
    }

    @Override
    public String store(MultipartFile file, String folder) throws URISyntaxException, IOException {
        if (file.isEmpty()) {
            throw new IOException("Uploaded file is empty");
        }
        
        if (!isImage(file)) {
            throw new IOException("Only image files are allowed. Supported types: " + 
                               ALLOWED_IMAGE_TYPES);
        }

        // Prepare directory
        createDirectory(folder);
        
        // Generate filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String finalName = generateUniqueFilename(originalFilename, fileExtension);
        
        // Prepare target path
        Path targetPath = getBasePath().resolve(folder).resolve(finalName);
        
        // Đọc file vào byte array để có thể sử dụng nhiều lần
        byte[] fileBytes = file.getBytes();
        
        // Validate image content
        try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("Invalid image content");
            }
            
            // Log image info
            logImageInfo(finalName, file.getSize(), image);
        }
        
        // Write file to disk
        try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        return finalName;
    }

    @Override
    public boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "jpg"; // Default extension
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    @Override
    public String generateUniqueFilename(String originalFilename, String extension) {
        String nameWithoutExt = originalFilename != null ? 
                originalFilename.replace("." + extension, "") : "image";
        return System.currentTimeMillis() + "-" + nameWithoutExt + "." + extension;
    }

    @Override
    public void logImageInfo(String filename, long size, BufferedImage image) {
        System.out.printf("""
            >>> Image uploaded successfully:
               - Filename: %s
               - Size: %d bytes (%.2f MB)
               - Dimensions: %dx%d pixels
            """, 
            filename, 
            size, 
            (double)size / (1024 * 1024),
            image.getWidth(), 
            image.getHeight()
        );
    }
}
