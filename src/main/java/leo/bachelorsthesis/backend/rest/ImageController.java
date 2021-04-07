package leo.bachelorsthesis.backend.rest;

import leo.bachelorsthesis.backend.service.ImageService.ImageService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private ImageService imageService;

    @PostMapping()
    public List<String> analysePicture(@RequestBody String imageUri) {
        List<String> response = new ArrayList<>();
        logger.info("received analyse picture request: {}", imageUri);

        imageService.analysePicture(imageUri);
        response.add("random message " + UUID.randomUUID());

        logger.info("analyse picture done: {}", response);
        return response;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) {
        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
