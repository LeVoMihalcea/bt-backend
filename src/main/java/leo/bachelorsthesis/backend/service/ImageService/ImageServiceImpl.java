package leo.bachelorsthesis.backend.service.ImageService;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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

    @Value("${staticFilePath}")
    private String staticFilePath;

    @Override
    public Set<String> analysePicture(String imageUri) {
        Set<String> messages = new HashSet<>();

        String decodedImageName = decodeImage(imageUri);
        messages.add(decodedImageName);

        return messages;
    }

    @Override
    public byte[] getImage(String imageUri) {
        try {
            return FileUtils.readFileToByteArray(new File(imageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String decodeImage(String imageUri) {
        byte[] imagedata = DatatypeConverter.parseBase64Binary(imageUri.substring(imageUri.indexOf(",") + 1));
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
            String imageName = UUID.randomUUID() + ".png";
            String imageNameWithPath = staticFilePath + imageName;
            ImageIO.write(bufferedImage, "png", new File(imageNameWithPath));
            return imageNameWithPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
