package ch.pfaditools.choreManager.backend.service.impl;

import ch.pfaditools.choreManager.backend.repository.MealSuggestionVoteRepository;
import ch.pfaditools.choreManager.backend.service.IMealSuggestionVoteEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.MealSuggestionEntity;
import ch.pfaditools.choreManager.model.MealSuggestionVoteEntity;
import ch.pfaditools.choreManager.model.UserEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MealSuggestionVoteEntityService implements IMealSuggestionVoteEntityService {
    private final MealSuggestionVoteRepository mealSuggestionVoteRepository;

    public MealSuggestionVoteEntityService(MealSuggestionVoteRepository mealSuggestionRepository) {
        this.mealSuggestionVoteRepository = mealSuggestionRepository;
    }
    @Override
    public ServiceResponse<MealSuggestionVoteEntity> getByUserAndMealSuggestion(UserEntity user, MealSuggestionEntity mealSuggestion) {
        ServiceResponse<MealSuggestionVoteEntity> response = new ServiceResponse<>();
        if (user == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionVoteService.error.userNull");
            return response;
        }
        if (mealSuggestion == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionVoteService.error.mealSuggestionNull");
            return response;
        }

        try {
            Optional<MealSuggestionVoteEntity> mealSuggestionVote = mealSuggestionVoteRepository.findByVotedByAndMealSuggestion(user, mealSuggestion);
            if (mealSuggestionVote.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("suggestedFor.error.noSuchMealSuggestionVote");
                return response;
            }
            response.setOperationSuccessful(true);
            response.addBusinessObject(mealSuggestionVote.get());
            response.setInfoMessage("suggestedFor.message.getByPersonAndMealSuggestion");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<MealSuggestionVoteEntity> getById(Long id) {
        ServiceResponse<MealSuggestionVoteEntity> response = new ServiceResponse<>();
        if (id == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionVoteService.error.idNull");
            return response;
        }

        try {
            Optional<MealSuggestionVoteEntity> mealSuggestionVote = mealSuggestionVoteRepository.findById(id);
            if (mealSuggestionVote.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("mealSuggestionVoteService.error.noSuchMealSuggestionVote");
                return response;
            }
            response.setOperationSuccessful(true);
            response.addBusinessObject(mealSuggestionVote.get());
            response.setInfoMessage("mealSuggestionVoteService.message.getById");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<MealSuggestionVoteEntity> getAll() {
        ServiceResponse<MealSuggestionVoteEntity> response = new ServiceResponse<>();
        try {
            List<MealSuggestionVoteEntity> mealSuggestionVotes = mealSuggestionVoteRepository.findAll();
            if (mealSuggestionVotes.isEmpty()) {
                response.setErrorMessage("mealSuggestionVoteService.error.noMealSuggestionVotesFound");
                return response;
            }
            response.setOperationSuccessful(true);
            response.setBusinessObjects(mealSuggestionVotes);
            response.setInfoMessage("mealSuggestionVoteService.message.getAll");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<MealSuggestionVoteEntity> delete(MealSuggestionVoteEntity entity) {
        ServiceResponse<MealSuggestionVoteEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionVoteService.error.delete.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("mealSuggestionVoteService.error.delete.idNull");
            return response;
        }

        try {
            mealSuggestionVoteRepository.delete(entity);
            response.setOperationSuccessful(true);
            response.setInfoMessage("mealSuggestionVoteService.message.delete");
            return response;
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<MealSuggestionVoteEntity> save(MealSuggestionVoteEntity entity) {
        ServiceResponse<MealSuggestionVoteEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setInfoMessage("mealSuggestionVoteService.error.save.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }

        try {
            MealSuggestionVoteEntity savedMealSuggestionVote = mealSuggestionVoteRepository.save(entity);
            response.addBusinessObject(savedMealSuggestionVote);
            response.setOperationSuccessful(true);
            response.setInfoMessage("mealSuggestionVoteService.message.save");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    private void catchException(ServiceResponse<MealSuggestionVoteEntity> response, Exception e) {
        response.setOperationSuccessful(false);
        response.setErrorMessage("mealSuggestionVoteService.error.general");
    }
}
