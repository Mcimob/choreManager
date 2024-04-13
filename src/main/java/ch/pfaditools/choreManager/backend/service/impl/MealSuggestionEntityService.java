package ch.pfaditools.choreManager.backend.service.impl;

import ch.pfaditools.choreManager.backend.repository.MealSuggestionRepository;
import ch.pfaditools.choreManager.backend.service.IMealSuggestionsEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.GroupEntity;
import ch.pfaditools.choreManager.model.MealSuggestionEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MealSuggestionEntityService implements IMealSuggestionsEntityService {

    private final MealSuggestionRepository mealSuggestionRepository;

    public MealSuggestionEntityService(MealSuggestionRepository mealSuggestionRepository) {
        this.mealSuggestionRepository = mealSuggestionRepository;
    }
    @Override
    public ServiceResponse<MealSuggestionEntity> getByGroupAndDate(GroupEntity group, LocalDate date) {
        ServiceResponse<MealSuggestionEntity> response = new ServiceResponse<>();
        if (group == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionEntityService.error.groupNull");
            return response;
        }
        if (date == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionEntityService.error.dateNull");
            return response;
        }

        try {
            List<MealSuggestionEntity> mealSuggestions = mealSuggestionRepository.findBySuggestedForAndSuggestedDate(group, date);
            if (mealSuggestions.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("mealSuggestionEntityService.error.noMealSuggestionsFound");
                return response;
            }
            response.setOperationSuccessful(true);
            response.setBusinessObjects(mealSuggestions);
            response.setInfoMessage("mealSuggestionEntityService.message.getByGroupAndDate");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<MealSuggestionEntity> getById(Long id) {
        ServiceResponse<MealSuggestionEntity> response = new ServiceResponse<>();
        if (id == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionEntityService.error.idNull");
            return response;
        }

        try {
            Optional<MealSuggestionEntity> mealSuggestion = mealSuggestionRepository.findById(id);
            if (mealSuggestion.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("mealSuggestionEntityService.error.noSuchMealSuggestion");
                return response;
            }
            response.setOperationSuccessful(true);
            response.addBusinessObject(mealSuggestion.get());
            response.setInfoMessage("mealSuggestionEntityService.message.getById");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<MealSuggestionEntity> getAll() {
        ServiceResponse<MealSuggestionEntity> response = new ServiceResponse<>();
        try {
            List<MealSuggestionEntity> mealSuggestions = mealSuggestionRepository.findAll();
            if (mealSuggestions.isEmpty()) {
                response.setErrorMessage("mealSuggestionEntityService.error.noMealSuggestionsFound");
                return response;
            }
            response.setOperationSuccessful(true);
            response.setBusinessObjects(mealSuggestions);
            response.setInfoMessage("mealSuggestionEntityService.message.getAll");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<MealSuggestionEntity> delete(MealSuggestionEntity entity) {
        ServiceResponse<MealSuggestionEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionEntityService.error.delete.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionEntityService.error.delete.idNull");
            return response;
        }

        try {
            mealSuggestionRepository.delete(entity);
            response.setOperationSuccessful(true);
            response.setInfoMessage("mealSuggestionEntityService.message.delete");
            return response;
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<MealSuggestionEntity> save(MealSuggestionEntity entity) {
        ServiceResponse<MealSuggestionEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setInfoMessage("mealSuggestionEntityService.error.save.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }

        try {
            MealSuggestionEntity savedMealSuggestion = mealSuggestionRepository.save(entity);
            response.addBusinessObject(savedMealSuggestion);
            response.setOperationSuccessful(true);
            response.setInfoMessage("mealSuggestionEntityService.message.save");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    private void catchException(ServiceResponse<MealSuggestionEntity> response, Exception e) {
        response.setOperationSuccessful(false);
        response.setErrorMessage("mealSuggestionEntityService.error.general");
    }
}
