package ch.pfaditools.choreManager.backend.service;

import ch.pfaditools.choreManager.model.GroupChoreRegistrationEntity;
import ch.pfaditools.choreManager.model.GroupEntity;

import java.time.LocalDate;

public interface IGroupChoreRegistrationEntityService extends IService<GroupChoreRegistrationEntity> {

    ServiceResponse<GroupChoreRegistrationEntity> getByDateRangeAndGroup(LocalDate start, LocalDate end, GroupEntity group);
}
