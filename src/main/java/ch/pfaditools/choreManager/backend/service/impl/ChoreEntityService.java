package ch.pfaditools.choreManager.backend.service.impl;

import ch.pfaditools.choreManager.backend.repository.ChoreRepository;
import ch.pfaditools.choreManager.backend.service.IChoreEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.ChoreEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChoreEntityService implements IChoreEntityService {
    private final ChoreRepository choreRepository;

    public ChoreEntityService(ChoreRepository choreRepository) {
        this.choreRepository = choreRepository;
    }

    @Override
    public ServiceResponse<ChoreEntity> getById(Long id) {
        ServiceResponse<ChoreEntity> response = new ServiceResponse<>();
        if (id == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("choreEntityService.error.idNull");
            return response;
        }

        try {
            Optional<ChoreEntity> chore = choreRepository.findById(id);
            if (chore.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("choreEntityService.error.noSuchChore");
                return response;
            }
            response.setOperationSuccessful(true);
            response.addBusinessObject(chore.get());
            response.setInfoMessage("choreEntityService.message.getById");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<ChoreEntity> getAll() {
        ServiceResponse<ChoreEntity> response = new ServiceResponse<>();
        try {
            List<ChoreEntity> chores = choreRepository.findAll();
            if (chores.isEmpty()) {
                response.setErrorMessage("choreEntityService.error.noChoresFound");
                return response;
            }
            response.setOperationSuccessful(true);
            response.setBusinessObjects(chores);
            response.setInfoMessage("choreEntityService.message.getAll");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<ChoreEntity> delete(ChoreEntity entity) {
        ServiceResponse<ChoreEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("choreEntityService.error.delete.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("choreEntityService.error.delete.idNull");
            return response;
        }

        try {
            choreRepository.delete(entity);
            response.setOperationSuccessful(true);
            response.setErrorMessage("choreEntityService.message.delete");
            return response;
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<ChoreEntity> save(ChoreEntity entity) {
        ServiceResponse<ChoreEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setInfoMessage("choreEntityService.error.save.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }

        try {
            ChoreEntity savedChore = choreRepository.save(entity);
            response.addBusinessObject(savedChore);
            response.setOperationSuccessful(true);
            response.setInfoMessage("choreEntityService.message.save");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }
}
