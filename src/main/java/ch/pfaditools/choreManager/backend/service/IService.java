package ch.pfaditools.choreManager.backend.service;

import ch.pfaditools.choreManager.model.AbstractEntity;
import ch.pfaditools.choreManager.util.HasLogger;
import com.helger.commons.traits.IGetterDirectTrait;

public interface IService<T extends AbstractEntity> extends HasLogger {

    ServiceResponse<T> getById(Long id);

    ServiceResponse<T> getAll();

    ServiceResponse<T> delete(T entity);

    ServiceResponse<T> save(T entity);

    default void catchException(ServiceResponse<T> response, Exception e) {
        getLogger().error("Serviceclass threw exception: {}", e);
        response.setOperationSuccessful(false);
        response.setErrorMessage("entityService.error.general");
    }

}
