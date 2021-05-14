package leo.bachelorsthesis.backend.domain.dtos.empathy;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmpathyResponseDTO {
    private int anger;
    private int contempt;
    private int disgust;
    private int fear;
    private int happiness;
    private int neutral;
    private int sadness;
    private int surprise;
}
