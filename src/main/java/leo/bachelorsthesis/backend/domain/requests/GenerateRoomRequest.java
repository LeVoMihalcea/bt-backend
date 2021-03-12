package leo.bachelorsthesis.backend.domain.requests;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GenerateRoomRequest extends GeneralRequest {
    private String name;
    private String description;
}
