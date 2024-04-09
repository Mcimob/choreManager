package ch.pfaditools.choreManager.backend.service;

import ch.pfaditools.choreManager.model.GroupEntity;

public interface IGroupEntityService extends IService<GroupEntity> {

    ServiceResponse<GroupEntity> getByCode(String name);
}
