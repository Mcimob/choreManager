package ch.pfaditools.choreManager.backend.service;

import ch.pfaditools.choreManager.model.MealSuggestionEntity;
import ch.pfaditools.choreManager.model.MealSuggestionVoteEntity;
import ch.pfaditools.choreManager.model.UserEntity;

public interface IMealSuggestionVoteEntityService extends IService<MealSuggestionVoteEntity> {

    ServiceResponse<MealSuggestionVoteEntity> getByUserAndMealSuggestion(UserEntity user, MealSuggestionEntity mealSuggestion);
}
