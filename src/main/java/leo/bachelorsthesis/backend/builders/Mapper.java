package leo.bachelorsthesis.backend.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import leo.bachelorsthesis.backend.constants.ImageConstants;
import leo.bachelorsthesis.backend.domain.dtos.empathy.EmpathyResponseDTO;
import leo.bachelorsthesis.backend.error.exceptions.ApiJsonError;

import java.util.*;

public class Mapper {
    public static String mapEmpathyResponseToJson(EmpathyResponseDTO empathyResponse) {
        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        map.put("anger", empathyResponse.getAnger());
        map.put("contempt", empathyResponse.getContempt());
        map.put("disgust", empathyResponse.getDisgust());
        map.put("fear", empathyResponse.getFear());
        map.put("happiness", empathyResponse.getHappiness());
        map.put("neutral", empathyResponse.getNeutral());
        map.put("sadness", empathyResponse.getSadness());
        map.put("surprise", empathyResponse.getSurprise());

        float max = -1;
        String maxKey = "neutral";
        for(Map.Entry<String, Object> entry: map.entrySet()){
            if((Float)entry.getValue() > max){
                maxKey = entry.getKey();
                max = (Float) entry.getValue();
            }
        }
        
        map.put("message", getDominantTrait(maxKey));
        map.put("dominantEmotion", maxKey);
        map.put("maxValue", map.get(maxKey));

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new ApiJsonError(ImageConstants.INVALID_RESPONSE);
        }
    }

    private static String getDominantTrait(String maxKey) {
        return getEmotionMap().get(maxKey);
    }

    private static Map<String, String> getEmotionMap() {
        Map<String, String> map = new HashMap<>();

        map.put("anger", ImageConstants.ANGER_IS_DOMINANT);
        map.put("contempt", ImageConstants.CONTEMPT_IS_DOMINANT);
        map.put("disgust", ImageConstants.DISGUST_IS_DOMINANT);
        map.put("fear", ImageConstants.FEAR_IS_DOMINANT);
        map.put("happiness", ImageConstants.HAPPINESS_IS_DOMINANT);
        map.put("neutral", ImageConstants.NEUTRAL_IS_DOMINANT);
        map.put("sadness", ImageConstants.SADNESS_IS_DOMINANT);
        map.put("surprise", ImageConstants.SURPRISE_IS_DOMINANT);

        return map;
    }
}
