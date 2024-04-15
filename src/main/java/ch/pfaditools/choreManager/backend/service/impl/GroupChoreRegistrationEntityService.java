package ch.pfaditools.choreManager.backend.service.impl;

import ch.pfaditools.choreManager.backend.repository.GroupChoreRegistrationRepository;
import ch.pfaditools.choreManager.backend.service.IGroupChoreRegistrationEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.GroupChoreRegistrationEntity;
import ch.pfaditools.choreManager.model.GroupEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupChoreRegistrationEntityService implements IGroupChoreRegistrationEntityService {
    private final GroupChoreRegistrationRepository repository;

    public GroupChoreRegistrationEntityService(GroupChoreRegistrationRepository repository) {
        this.repository = repository;
    }


    @Override
    public ServiceResponse<GroupChoreRegistrationEntity> getByDateRangeAndGroup(LocalDate start, LocalDate end, GroupEntity group) {
        ServiceResponse<GroupChoreRegistrationEntity> response = new ServiceResponse<>();
        if (start == null || end == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupChoreRegistrationEntityService.error.dateNull");
            return response;
        }


        if (group == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupChoreRegistrationEntityService.error.groupNull");
            return response;
        }

        try {
            List<GroupChoreRegistrationEntity> groupChoreRegistrations = repository.findByDateRangeAndGroup(start, end, group);
            if (groupChoreRegistrations.isEmpty()) {
                response.setErrorMessage("groupChoreRegistrationEntityService.error.noGroupChoreRegistrationsFound");
            }
            response.setOperationSuccessful(true);
            response.setBusinessObjects(groupChoreRegistrations);
            response.setInfoMessage("groupChoreRegistrationEntityService.message.getByDateRangeAndGroup");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<GroupChoreRegistrationEntity> getById(Long id) {
        ServiceResponse<GroupChoreRegistrationEntity> response = new ServiceResponse<>();
        if (id == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupChoreRegistrationEntityService.error.idNull");
            return response;
        }

        try {
            Optional<GroupChoreRegistrationEntity> groupChoreRegistration = repository.findById(id);
            if (groupChoreRegistration.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("groupChoreRegistrationEntityService.error.noSuchGroupChoreRegistration");
                return response;
            }
            response.setOperationSuccessful(true);
            response.addBusinessObject(groupChoreRegistration.get());
            response.setInfoMessage("groupChoreRegistrationEntityService.message.getById");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<GroupChoreRegistrationEntity> getAll() {
        ServiceResponse<GroupChoreRegistrationEntity> response = new ServiceResponse<>();
        try {
            List<GroupChoreRegistrationEntity> groupChoreRegistration = repository.findAll();
            if (groupChoreRegistration.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("groupChoreRegistrationEntityService.error.noGroupChoreRegistrationsFound");
                return response;
            }
            response.setOperationSuccessful(true);
            response.setBusinessObjects(groupChoreRegistration);
            response.setInfoMessage("groupChoreRegistrationEntityService.message.getAll");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<GroupChoreRegistrationEntity> delete(GroupChoreRegistrationEntity entity) {
        ServiceResponse<GroupChoreRegistrationEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupChoreRegistrationEntityService.error.delete.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupChoreRegistrationEntityService.error.delete.idNull");
            return response;
        }

        try {
            repository.delete(entity);
            response.setOperationSuccessful(true);
            response.setInfoMessage("groupChoreRegistrationEntityService.message.delete");
            return response;
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<GroupChoreRegistrationEntity> save(GroupChoreRegistrationEntity entity) {
        ServiceResponse<GroupChoreRegistrationEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupChoreRegistrationEntityService.error.save.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        try {
            GroupChoreRegistrationEntity savedGroupChoreRegistration = repository.save(entity);
            response.addBusinessObject(savedGroupChoreRegistration);
            response.setOperationSuccessful(true);
            response.setInfoMessage("groupChoreRegistrationEntityService.message.save");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }
}
