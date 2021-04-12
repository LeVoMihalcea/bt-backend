package leo.bachelorsthesis.backend.clients;

import feign.Headers;
import feign.RequestLine;
import leo.bachelorsthesis.backend.domain.dtos.empathy.EmpathyResponseDTO;

public interface EmpathyClient {
    @RequestLine("POST /")
    @Headers("Content-Type: application/json")
    EmpathyResponseDTO sendImageToEmpathy(String imageUrl);
}
