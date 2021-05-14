package leo.bachelorsthesis.backend.domain.dtos.empathy;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmpathyResponseDTO {
    private float anger;
    private float contempt;
    private float disgust;
    private float fear;
    private float happiness;
    private float neutral;
    private float sadness;
    private float surprise;
}
