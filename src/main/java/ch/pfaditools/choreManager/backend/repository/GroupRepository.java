package ch.pfaditools.choreManager.backend.repository;

import ch.pfaditools.choreManager.model.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<GroupEntity, Long> {

    public Optional<GroupEntity> findByGroupCode(String name);
}
