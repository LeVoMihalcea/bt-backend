package leo.bachelorsthesis.backend.domain.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class TokenRequest {
    private int uid;
    private String channel;
    private boolean isPublisher;
}
