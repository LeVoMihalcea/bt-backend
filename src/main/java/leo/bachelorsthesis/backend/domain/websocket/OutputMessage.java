package leo.bachelorsthesis.backend.domain.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessage extends Message {
    private String time;

    public OutputMessage(String imageUri, String time) {
        super(imageUri);
        this.time = time;
    }
}
