package leo.bachelorsthesis.backend.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import leo.bachelorsthesis.backend.constants.ImageConstants;
import leo.bachelorsthesis.backend.domain.dtos.empathy.EmpathyResponseDTO;
import leo.bachelorsthesis.backend.error.exceptions.ApiJsonError;

import java.util.HashMap;
import java.util.Map;

public class Mapper {
    public static String mapEmpathyResponseToJson(EmpathyResponseDTO empathyResponse){
        Map<String, String> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        map.put("confusion", String.valueOf(empathyResponse.getConfusion()));
        map.put("happiness", String.valueOf(empathyResponse.getHappiness()));
        map.put("sadness", String.valueOf(empathyResponse.getSadness()));

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new ApiJsonError(ImageConstants.INVALID_RESPONSE);
        }
    }
}
