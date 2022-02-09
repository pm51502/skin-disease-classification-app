package backend.dao;

import backend.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RecordRepository extends JpaRepository<Record, Long> {
    Set<Record> findByUsername(String username);

    Record findByImageId(Long id);
}
