package ch.pfaditools.choreManager.views.admin.groupChoreRegistration;

import ch.pfaditools.choreManager.backend.service.IChoreEntityService;
import ch.pfaditools.choreManager.backend.service.IGroupChoreRegistrationEntityService;
import ch.pfaditools.choreManager.backend.service.IGroupEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.model.ChoreEntity;
import ch.pfaditools.choreManager.model.GroupChoreRegistrationEntity;
import ch.pfaditools.choreManager.model.GroupEntity;
import ch.pfaditools.choreManager.util.Notifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class GroupRegistrationForm extends FormLayout {

    private final IGroupChoreRegistrationEntityService groupRegistrationService;
    private final IGroupEntityService groupService;
    private final IChoreEntityService choreService;
    private final GroupChoreRegistrationView parent;
    private final Binder<GroupChoreRegistrationEntity> binder = new Binder<>();
    private final Select<GroupEntity> groupSelect = new Select<>();
    private final Select<ChoreEntity> choreSelect = new Select<>();
    private final DatePicker startDatePicker = new DatePicker(getTranslation("groupChoreRegistrationView.groupRegistrationForm.startDatePicker"));
    private final DatePicker endDatePicker = new DatePicker(getTranslation("groupChoreRegistrationView.groupRegistrationForm.endatePicker"));
    private final TextField createdAtField = new TextField(getTranslation("groupChoreRegistrationView.groupRegistrationForm.createdAtField"));
    private final Button saveButton = new Button(getTranslation("groupChoreRegistrationView.groupRegistrationForm.saveButton"));
    private final Button deleteButton = new Button(getTranslation("groupChoreRegistrationView.groupRegistrationForm.deleteButton"));

    private GroupChoreRegistrationEntity registration = new GroupChoreRegistrationEntity();

    public GroupRegistrationForm(
            GroupChoreRegistrationView parent,
            IGroupChoreRegistrationEntityService groupRegistrationService,
            IGroupEntityService groupService,
            IChoreEntityService choreService) {
        this.parent = parent;
        this.groupRegistrationService = groupRegistrationService;
        this.groupService = groupService;
        this.choreService = choreService;
        setupLayout();
        setupBinder();
        setupButtons();
        setupSelects();
    }

    private void setupLayout() {
        add(groupSelect, choreSelect, startDatePicker, endDatePicker, createdAtField, saveButton, deleteButton);
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2)
        );
        setColspan(createdAtField, 2);
        this.setMaxWidth("1000px");
    }

    private void setupSelects() {
        ServiceResponse<GroupEntity> groupResponse = groupService.getAll();
        if (!groupResponse.isOperationSuccessful()) {
            Notifier.showErrorNotification(getTranslation(groupResponse.getErrorMessage()));
            return;
        }
        groupSelect.setItems(groupResponse.getBusinessObjects());

        ServiceResponse<ChoreEntity> choreResponse = choreService.getAll();
        if (!choreResponse.isOperationSuccessful()) {
            Notifier.showErrorNotification(getTranslation(choreResponse.getErrorMessage()));
            return;
        }
        choreSelect.setItems(choreResponse.getBusinessObjects());

        groupSelect.setItemLabelGenerator(GroupEntity::getName);
        choreSelect.setItemLabelGenerator(ChoreEntity::getName);

        groupSelect.setLabel(getTranslation("groupChoreRegistrationView.groupRegistrationForm.groupSelect"));
        choreSelect.setLabel(getTranslation("groupChoreRegistrationView.groupRegistrationForm.choreSelect"));
    }

    private void setupButtons() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        saveButton.addClickListener(click -> {
            try {
                binder.writeBean(registration);
                ServiceResponse<GroupChoreRegistrationEntity> response = groupRegistrationService.save(registration);
                if (!response.isOperationSuccessful()) {
                    Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                    return;
                }
                Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
                parent.setupGridItems();
                setItem();
            } catch(ValidationException e) {
                Notifier.showErrorNotification(getTranslation("groupChoreRegistrationView.groupRegistrationForm.validate"));
            }
        });

        deleteButton.addClickListener(click -> {
            ServiceResponse<GroupChoreRegistrationEntity> response = groupRegistrationService.delete(registration);
            if (!response.isOperationSuccessful()) {
                Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                return;
            }
            Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
            parent.setupGridItems();
        });
    }

    private void setupBinder() {
        binder.forField(groupSelect).asRequired().bind(GroupChoreRegistrationEntity::getGroup, GroupChoreRegistrationEntity::setGroup);
        binder.forField(choreSelect).asRequired().bind(GroupChoreRegistrationEntity::getChore, GroupChoreRegistrationEntity::setChore);
        binder.forField(startDatePicker).asRequired().bind(GroupChoreRegistrationEntity::getStartDate, GroupChoreRegistrationEntity::setStartDate);
        binder.forField(endDatePicker).asRequired().bind(GroupChoreRegistrationEntity::getEndDate, GroupChoreRegistrationEntity::setEndDate);
        binder.forField(createdAtField).bind(GroupChoreRegistrationEntity::getCreatedAtString, null);
    }

    private void clearForm() {

    }

    public void setItem(GroupChoreRegistrationEntity registration) {
        this.registration = registration;
        binder.readBean(this.registration);
    }

    public void setItem() {
        setItem(new GroupChoreRegistrationEntity());
    }

}

