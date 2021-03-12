package leo.bachelorsthesis.backend.clients;

import feign.RequestLine;
import leo.bachelorsthesis.backend.domain.requests.TokenRequest;
import leo.bachelorsthesis.backend.domain.responses.TokenResponse;

public interface TokenClient {
    @RequestLine("GET")
    TokenResponse findByIsbn(TokenRequest tokenRequest);
}
