package leo.bachelorsthesis.backend.service.ImageService;

import leo.bachelorsthesis.backend.builders.Mapper;
import leo.bachelorsthesis.backend.domain.dtos.empathy.EmpathyResponseDTO;
import leo.bachelorsthesis.backend.clients.EmpathyClient;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${backendServerUrl}")
    private String backendServerUrl;

    private final EmpathyClient empathyClient;

    public ImageServiceImpl(EmpathyClient empathyClient) {
        this.empathyClient = empathyClient;
    }

    @Override
    public String analysePicture(String imageUri) {
        String message = "";

        String decodedImageName = decodeImage(imageUri);
        EmpathyResponseDTO empathyResponseDTO =
                empathyClient.sendImageToEmpathy(backendServerUrl + "/" + decodedImageName);

        message = Mapper.mapEmpathyResponseToJson(empathyResponseDTO);

        return message;
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
