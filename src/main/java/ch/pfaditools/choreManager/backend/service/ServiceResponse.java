package ch.pfaditools.choreManager.backend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServiceResponse<T> {

    private String infoMessage;
    private String errorMessage;
    private boolean operationSuccessful;
    private List<T> businessObjects = new ArrayList<>();

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setInfoMessage(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isOperationSuccessful() {
        return operationSuccessful;
    }

    public void setOperationSuccessful(boolean operationSuccessful) {
        this.operationSuccessful = operationSuccessful;
    }

    public List<T> getBusinessObjects() {
        return businessObjects;
    }

    public void setBusinessObjects(List<T> businessObjects) {
        this.businessObjects = businessObjects;
    }

    public void addBusinessObject(T entity) {
        businessObjects.add(entity);
    }

    public void addBusinessObjects(Collection<T> entites) {
        businessObjects.addAll(entites);
    }
}
