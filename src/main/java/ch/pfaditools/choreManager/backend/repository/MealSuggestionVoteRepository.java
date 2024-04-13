package ch.pfaditools.choreManager.backend.repository;

import ch.pfaditools.choreManager.model.MealSuggestionEntity;
import ch.pfaditools.choreManager.model.MealSuggestionVoteEntity;
import ch.pfaditools.choreManager.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealSuggestionVoteRepository extends JpaRepository<MealSuggestionVoteEntity, Long> {

    Optional<MealSuggestionVoteEntity> findByVotedByAndMealSuggestion(UserEntity user, MealSuggestionEntity mealSuggestion);
}
