package leo.bachelorsthesis.backend.repository;

import leo.bachelorsthesis.backend.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
}
