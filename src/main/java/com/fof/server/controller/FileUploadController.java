package com.fof.server.controller;

import com.fof.server.model.normal.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/entrepreneur")
public class FileUploadController {

    @PostMapping("/upload")
    public ResponseEntity <ResponseDTO> uploadFile(
            @RequestParam("image") List<MultipartFile> images,
            @RequestParam("video") MultipartFile video,
            @RequestParam("thumbnail") MultipartFile thumbnail
    ) {
        // Project root directory
        String rootDirectory = System.getProperty("user.dir");

        // Example paths for saving images and video
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images";
        String videoUploadPath = rootDirectory + "/src/main/resources/static/uploads/videos";

        //Path to save the thumbnail
        String thumbnailUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/thumbnails";


        try {
            //If there exists images in the request
            // Save images
            for (MultipartFile image : images) {
                String fileName = image.getOriginalFilename();
                Path filePath = Paths.get(imageUploadPath, fileName);
                Files.write(filePath, image.getBytes());
            }

            //Upload the thumbnail
            String thumbnailFileName = thumbnail.getOriginalFilename();
            Path thumbnailFilePath = Paths.get(thumbnailUploadPath, thumbnailFileName);
            Files.write(thumbnailFilePath, thumbnail.getBytes());

            // Save video
            String videoFileName = video.getOriginalFilename();
            Path videoFilePath = Paths.get(videoUploadPath, videoFileName);
            Files.write(videoFilePath, video.getBytes());

            return ResponseEntity.ok(new ResponseDTO("Success", "Files uploaded successfully."));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO("Failure", "Error uploading files."));
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        String uniqueFileName = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return uniqueFileName + fileExtension;
    }
}