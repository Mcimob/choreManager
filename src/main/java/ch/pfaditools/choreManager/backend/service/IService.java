package ch.pfaditools.choreManager.backend.service;

import ch.pfaditools.choreManager.model.AbstractEntity;

public interface IService<T extends AbstractEntity> {

    ServiceResponse<T> getById(Long id);

    ServiceResponse<T> getAll();

    ServiceResponse<T> delete(T entity);

    ServiceResponse<T> save(T entity);

}
