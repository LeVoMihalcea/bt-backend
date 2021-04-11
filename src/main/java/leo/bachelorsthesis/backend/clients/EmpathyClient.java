package leo.bachelorsthesis.backend.clients;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import leo.bachelorsthesis.backend.domain.dtos.empathy.EmpathyResponseDTO;
import org.springframework.messaging.handler.annotation.Header;

public interface EmpathyClient {
    @RequestLine("POST /")
    @Headers("Content-Type: application/json")
    EmpathyResponseDTO sendImageToEmpathy(String imageUrl);
}
