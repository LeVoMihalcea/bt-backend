package leo.bachelorsthesis.backend.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import leo.bachelorsthesis.backend.constants.ImageConstants;
import leo.bachelorsthesis.backend.domain.dtos.empathy.EmpathyResponseDTO;
import leo.bachelorsthesis.backend.error.exceptions.ApiJsonError;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mapper {
    public static String mapEmpathyResponseToJson(EmpathyResponseDTO empathyResponse) {
        Map<String, String> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        map.put("anger", String.valueOf(empathyResponse.getAnger()));
        map.put("contempt", String.valueOf(empathyResponse.getContempt()));
        map.put("disgust", String.valueOf(empathyResponse.getDisgust()));
        map.put("fear", String.valueOf(empathyResponse.getFear()));
        map.put("happiness", String.valueOf(empathyResponse.getHappiness()));
        map.put("neutral", String.valueOf(empathyResponse.getNeutral()));
        map.put("sadness", String.valueOf(empathyResponse.getSadness()));
        map.put("surprise", String.valueOf(empathyResponse.getSurprise()));
        
//        map.put("message", getDominantTrait(empathyResponse));
//        map.put("maxValue", String.valueOf(Collections.max(List
//                .of(empathyResponse.getConfusion(), empathyResponse.getHappiness(), empathyResponse.getSadness()))));

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new ApiJsonError(ImageConstants.INVALID_RESPONSE);
        }
    }

//    private static String getDominantTrait(EmpathyResponseDTO empathyResponse) {
//        Integer max = Collections.max(
//                List.of(empathyResponse.getConfusion(), empathyResponse.getHappiness(), empathyResponse.getSadness()));
//        if(empathyResponse.getConfusion() == max)
//            return ImageConstants.CONFUSION_IS_DOMINANT;
//        if(empathyResponse.getHappiness() == max)
//            return ImageConstants.HAPPINESS_IS_DOMINANT;
//        return ImageConstants.SADNESS_IS_DOMINANT;
//    }
}
