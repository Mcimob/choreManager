package ch.pfaditools.choreManager.backend.service.impl;

import ch.pfaditools.choreManager.backend.repository.GroupRepository;
import ch.pfaditools.choreManager.backend.service.IGroupEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.GroupEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupEntityService implements IGroupEntityService {

    private final GroupRepository groupRepository;

    public GroupEntityService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public ServiceResponse<GroupEntity> getByCode(String groupCode) {
        ServiceResponse<GroupEntity> response = new ServiceResponse<>();
        if (groupCode == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupEntityService.error.nameNull");
            return response;
        }

        try {
            Optional<GroupEntity> group = groupRepository.findByGroupCode(groupCode);
            if (group.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("groupEntityService.error.noSuchGroup");
                return response;
            }
            response.setOperationSuccessful(true);
            response.addBusinessObject(group.get());
            response.setInfoMessage("groupEntityService.message.getByCode");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<GroupEntity> getById(Long id) {
        ServiceResponse<GroupEntity> response = new ServiceResponse<>();
        if (id == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupEntityService.error.idNull");
            return response;
        }

        try {
            Optional<GroupEntity> group = groupRepository.findById(id);
            if (group.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("groupEntityService.error.noSuchGroup");
                return response;
            }
            response.setOperationSuccessful(true);
            response.addBusinessObject(group.get());
            response.setInfoMessage("groupEntityService.message.getById");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<GroupEntity> getAll() {
        ServiceResponse<GroupEntity> response = new ServiceResponse<>();
        try {
            List<GroupEntity> group = groupRepository.findAll();
            if (group.isEmpty()) {
                response.setOperationSuccessful(false);
                response.setErrorMessage("groupEntityService.error.noGroupsFound");
                return response;
            }
            response.setOperationSuccessful(true);
            response.setBusinessObjects(group);
            response.setInfoMessage("groupEntityService.message.getAll");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<GroupEntity> delete(GroupEntity entity) {
        ServiceResponse<GroupEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupEntityService.error.delete.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupEntityService.error.delete.idNull");
            return response;
        }

        try {
            groupRepository.delete(entity);
            response.setOperationSuccessful(true);
            response.setInfoMessage("groupEntityService.message.delete");
            return response;
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    @Override
    public ServiceResponse<GroupEntity> save(GroupEntity entity) {
        ServiceResponse<GroupEntity> response = new ServiceResponse<>();
        if (entity == null) {
            response.setOperationSuccessful(false);
            response.setErrorMessage("groupEntityService.error.save.entityNull");
            return response;
        }
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
            entity.setGroupCode(RandomStringUtils.random(32, true, true));
        }
        try {
            GroupEntity savedGroup = groupRepository.save(entity);
            response.addBusinessObject(savedGroup);
            response.setOperationSuccessful(true);
            response.setInfoMessage("groupEntityService.message.save");
        } catch (Exception e) {
            catchException(response, e);
        }
        return response;
    }

    private void catchException(ServiceResponse<GroupEntity> response, Exception e) {
        response.setOperationSuccessful(false);
        response.setErrorMessage("groupEntityService.error.general");
    }
}
