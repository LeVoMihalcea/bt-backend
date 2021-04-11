package leo.bachelorsthesis.backend.service.ImageService;

import java.util.Set;

public interface ImageService {
    String analysePicture(String imageUri);

    byte[] getImage(String imageUri);
}
