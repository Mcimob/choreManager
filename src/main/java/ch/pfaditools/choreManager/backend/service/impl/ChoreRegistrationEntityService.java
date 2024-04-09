package ch.pfaditools.choreManager.backend.service.impl;

import ch.pfaditools.choreManager.backend.repository.ChoreRegistrationRepository;
import ch.pfaditools.choreManager.backend.service.IChoreRegistrationEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.ChoreRegistrationEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChoreRegistrationEntityService implements IChoreRegistrationEntityService {

    private final ChoreRegistrationRepository choreRegistrationRepository;

    public ChoreRegistrationEntityService(ChoreRegistrationRepository choreRegistrationRepository) {
        this.choreRegistrationRepository = choreRegistrationRepository;
    }

    @Override
    public ServiceResponse<ChoreRegistrationEntity> getById(Long id) {
        ServiceResponse<ChoreRegistrationEntity> response = new ServiceResponse<>();
        if (id == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("choreRegistrationEntityService.error.idNull");
            return response;
        }

        try {
            Optional<ChoreRegistrationEntity> choreRegistration = choreRegistrationRepository.findById(id);
            if (choreRegistration.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("choreRegistrationEntityService.error.noSuchChoreRegistration");
                return response;
            }
            response.setOperationSuccessful(true);
            response.addBusinessObject(choreRegistration.get());
            response.setInfoMessage("choreRegistrationEntityService.message.getById");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<ChoreRegistrationEntity> getAll() {
        ServiceResponse<ChoreRegistrationEntity> response = new ServiceResponse<>();
        try {
            List<ChoreRegistrationEntity> choreRegistrations = choreRegistrationRepository.findAll();
            if (choreRegistrations.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("choreRegistrationEntityService.error.noChoreRegistrationsFound");
                return response;
            }
            response.setOperationSuccessful(true);
            response.setBusinessObjects(choreRegistrations);
            response.setInfoMessage("choreRegistrationEntityService.message.getAll");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<ChoreRegistrationEntity> delete(ChoreRegistrationEntity entity) {
        ServiceResponse<ChoreRegistrationEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("choreRegistrationEntityService.error.delete.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("choreRegistrationEntityService.error.delete.idNull");
            return response;
        }

        try {
            choreRegistrationRepository.delete(entity);
            response.setOperationSuccessful(true);
            response.setInfoMessage("choreRegistrationEntityService.message.delete");
            return response;
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<ChoreRegistrationEntity> save(ChoreRegistrationEntity entity) {
        ServiceResponse<ChoreRegistrationEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("choreRegistrationEntityService.error.save.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        try {
            ChoreRegistrationEntity savedChoreRegistration = choreRegistrationRepository.save(entity);
            response.addBusinessObject(savedChoreRegistration);
            response.setOperationSuccessful(true);
            response.setInfoMessage("choreRegistrationEntityService.message.save");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    private void catchException(ServiceResponse<ChoreRegistrationEntity> response, Exception e) {
        response.setOperationSuccessful(false);
        response.setErrorMessage("choreRegistrationEntityService.error.general");
    }
}
