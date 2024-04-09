package ch.pfaditools.choreManager.backend.repository;

import ch.pfaditools.choreManager.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
