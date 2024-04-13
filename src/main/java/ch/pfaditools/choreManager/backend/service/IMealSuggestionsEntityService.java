package ch.pfaditools.choreManager.backend.service;

import ch.pfaditools.choreManager.model.GroupEntity;
import ch.pfaditools.choreManager.model.MealSuggestionEntity;

import java.time.LocalDate;

public interface IMealSuggestionsEntityService extends IService<MealSuggestionEntity> {

    ServiceResponse<MealSuggestionEntity> getByGroupAndDate(GroupEntity group, LocalDate date);
}
