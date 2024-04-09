package ch.pfaditools.choreManager.backend.repository;

import ch.pfaditools.choreManager.model.GroupChoreRegistrationEntity;
import ch.pfaditools.choreManager.model.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface GroupChoreRegistrationRepository extends JpaRepository<GroupChoreRegistrationEntity, Long> {

    @Query("SELECT g FROM GroupChoreRegistrationEntity g WHERE g.startDate <= ?2 AND g.endDate >= ?1 AND g.group = ?3")
    List<GroupChoreRegistrationEntity> findByDateRangeAndGroup(LocalDate start, LocalDate end, GroupEntity group);

}
