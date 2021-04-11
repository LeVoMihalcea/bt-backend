package leo.bachelorsthesis.backend.domain.dtos.empathy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class EmpathyResponseDTO {
    private int confusion;
    private int happiness;
    private int sadness;
}
