package leo.bachelorsthesis.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomCalendarEntry {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @NotNull(message = "First Date Time cannot be null!")
    private LocalDateTime firstDateTime;

    @Column(name = "hours", nullable = false)
    private int hours;

    @Column(name = "repeat_every", nullable = false)
    private int repeatEvery;

    @Column(name = "time_unit", nullable = false)
    private TimeUnit timeUnit;

    @OneToOne(mappedBy = "roomCalendarEntry")
    @JsonIgnore
    private Room room;
}
