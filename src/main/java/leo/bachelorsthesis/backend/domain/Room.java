package leo.bachelorsthesis.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @NotNull(message = "The Room name cannot be empty!")
    private String name;

    @NotNull(message = "The Room type can't be empty!")
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull(message = "The Room description can't be empty!")
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User host;

    @ManyToMany
    @JoinTable(
            name = "rooms_joined",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))

    @OrderBy("email ASC")
    private Set<User> users;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_calendar_entry_id", referencedColumnName = "id")
    private RoomCalendarEntry roomCalendarEntry;

    public Room(@NotNull(message = "The Room name cannot be empty!") String name,
                @NotNull(message = "The Room type can't be empty!") String type,
                @NotNull(message = "The Room description can't be empty!") String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public void addUser(User user) {
        this.users.add(user);
    }
}
