package ch.pfaditools.choreManager.backend.repository;

import ch.pfaditools.choreManager.model.ChoreRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoreRegistrationRepository extends JpaRepository<ChoreRegistrationEntity, Long> {
}
