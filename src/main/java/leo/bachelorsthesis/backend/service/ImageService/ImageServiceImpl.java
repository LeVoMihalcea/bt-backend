package leo.bachelorsthesis.backend.service.ImageService;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public Set<String> analysePicture(String imageUri) {
        Set<String> messages = new HashSet<>();

        String decodedImageName = decodeImage(imageUri);

        return messages;
    }

    private String decodeImage(String imageUri) {
        byte[] imagedata = DatatypeConverter.parseBase64Binary(imageUri.substring(imageUri.indexOf(",") + 1));
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
            String imageName = UUID.randomUUID() + ".png";
            String imageNameWithPath = "src/main/resources/static/" + imageName;
            ImageIO.write(bufferedImage, "png", new File(imageNameWithPath));
            return imageNameWithPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
