package ch.pfaditools.choreManager.backend.repository;

import ch.pfaditools.choreManager.model.ChoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoreRepository extends JpaRepository<ChoreEntity, Long> {
}
