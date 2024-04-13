package ch.pfaditools.choreManager.backend.repository;

import ch.pfaditools.choreManager.model.GroupEntity;
import ch.pfaditools.choreManager.model.MealSuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealSuggestionRepository extends JpaRepository<MealSuggestionEntity, Long> {

    List<MealSuggestionEntity> findBySuggestedForAndSuggestedDate(GroupEntity group, LocalDate date);
}
