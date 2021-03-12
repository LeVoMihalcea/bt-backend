package leo.bachelorsthesis.backend.domain.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TokenResponse implements Serializable {
    private int uid;
    private String token;
}
