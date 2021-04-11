package leo.bachelorsthesis.backend.controllers;

import leo.bachelorsthesis.backend.service.ImageService.ImageService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Value("${staticFilePath}")
    private String staticFilePath;

    @Autowired
    private ImageService imageService;

    @MessageMapping("/analyse")
    @SendTo("/topic/AI-responses")
    public String analysePicture(@RequestBody String imageUri) {
        String response = "";
        logger.info("received analyse picture request");

        response = imageService.analysePicture(imageUri);

        logger.info("analyse picture done: {}", response);
        return response;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) {
        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File(staticFilePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
