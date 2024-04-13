package ch.pfaditools.choreManager.views.main;

import ch.pfaditools.choreManager.backend.service.IChoreRegistrationEntityService;
import ch.pfaditools.choreManager.backend.service.IGroupChoreRegistrationEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.layout.MainLayout;
import ch.pfaditools.choreManager.model.ChoreRegistrationEntity;
import ch.pfaditools.choreManager.model.GroupChoreRegistrationEntity;
import ch.pfaditools.choreManager.model.GroupEntity;
import ch.pfaditools.choreManager.model.UserEntity;
import ch.pfaditools.choreManager.security.SecurityService;
import ch.pfaditools.choreManager.util.DateUtils;
import ch.pfaditools.choreManager.util.Notifier;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class MainView extends VerticalLayout {

    private final static String[] weekdays = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
    private final IChoreRegistrationEntityService choreRegistrationService;
    private final IGroupChoreRegistrationEntityService groupChoreRegistrationService;
    private final SecurityService securityService;

    private LocalDate currentMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    private final Select<GroupEntity> groupSelect = new Select<>();
    private final Div choreGrid = new Div();
    private final Button previousButton = new Button(VaadinIcon.ANGLE_LEFT.create());
    private final Button nextButton = new Button(VaadinIcon.ANGLE_RIGHT.create());
    private GroupEntity selectedGroup;

    MainView(IChoreRegistrationEntityService choreRegistrationService,
             IGroupChoreRegistrationEntityService groupChoreRegistrationService,
             SecurityService securityService) {
        this.choreRegistrationService = choreRegistrationService;
        this. groupChoreRegistrationService = groupChoreRegistrationService;
        this.securityService = securityService;
        setupLayout();
        setupGroupSelect();
        setupChoreGrid();
        setupSelectorButtons();
    }


    private void setupSelectorButtons() {
        previousButton.addClickListener(click -> {
            currentMonday = currentMonday.minusDays(7);
            setupChoreGrid();
        });
        nextButton.addClickListener(click -> {
            currentMonday = currentMonday.plusDays(7);
            setupChoreGrid();
        });
    }

    private void setupGroupSelect() {
        Optional<UserEntity> currentUser = securityService.getAuthenticatedUser();
        if (currentUser.isEmpty()) {
            Notifier.showErrorNotification(getTranslation("mainView.noAuthenticatedUser"));
            return;
        }
        Set<GroupEntity> groups = currentUser.get().getGroups();
        if (groups.size() == 1) {
            selectedGroup = groups.stream().toList().getFirst();
            return;
        }
        groupSelect.setItems(groups);
        groupSelect.setLabel(getTranslation("mainView.groupSelect"));
        add(groupSelect);

        groupSelect.addValueChangeListener(this::setSelectedGroup);

    }

    private void setupLayout() {
        H1 title = new H1(getTranslation("mainView.title"));
        Div controlContainer = new Div(previousButton, choreGrid, nextButton);
        controlContainer.addClassName("control_container");
        add(title, controlContainer);
    }

    private void setupChoreGrid() {
        choreGrid.removeAll();
        choreGrid.addClassName("chore_grid");

        ServiceResponse<GroupChoreRegistrationEntity> response =
                groupChoreRegistrationService.getByDateRangeAndGroup
                        (currentMonday, currentMonday.plusDays(6), selectedGroup);
        if (!response.isOperationSuccessful()) {
            Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
            return;
        }


        choreGrid.add(new Div());
        for (int i = 0; i < weekdays.length; i++) {
            Div dateContainer = new Div(
                    new Div(weekdays[i]),
                    new Div(DateUtils.formatDateShort(currentMonday.plusDays(i))));
            dateContainer.addClassName("date_container");
            choreGrid.add(dateContainer);
        }

        if (response.getBusinessObjects().isEmpty()) {
            choreGrid.add(getTranslation(response.getErrorMessage()));
            return;
        }

        Div choresContainer = new Div();
        choresContainer.addClassName("chores_container");
        choresContainer.getStyle().set("grid-area", "2 / 2 / %s / 9".formatted(response.getBusinessObjects().size() + 2));
        if (response.getBusinessObjects().isEmpty()) {
            choresContainer.addClassName("empty");
        }
        choreGrid.add(choresContainer);

        for (GroupChoreRegistrationEntity groupRegistration : response.getBusinessObjects()) {
            Div choreTitleContainer = new Div(groupRegistration.getChore().getName());
            choreTitleContainer.addClassName("chore_title");
            choreTitleContainer.addClassName("chore_box");
            choreGrid.add(choreTitleContainer);
            for (LocalDate date = currentMonday; date.isBefore(currentMonday.plusDays(7)); date = date.plusDays(1)) {
                if (!groupRegistration.getStartDate().isAfter(date) && !groupRegistration.getEndDate().isBefore(date)) {
                    choresContainer.add(new ChoreBox(groupRegistration, date));
                } else {
                    Div emptyDiv = new Div();
                    emptyDiv.addClassNames("chore_box", "empty");
                    choresContainer.add(emptyDiv);
                }
            }
        }


    }

    private void setSelectedGroup(AbstractField.ComponentValueChangeEvent<Select<GroupEntity>, GroupEntity> change) {
        this.selectedGroup = change.getValue();
        setupChoreGrid();
    }

    private class ChoreBox extends Div {

        private final GroupChoreRegistrationEntity groupRegistration;
        private final LocalDate date;
        private final Icon infoIcon = VaadinIcon.USER.create();
        private final Checkbox check = new Checkbox();
        private final Div countDiv = new Div();
        private int registrationCount;
        private Optional<ChoreRegistrationEntity> personalRegistration = Optional.empty();
        private List<ChoreRegistrationEntity> specificRegistrations;

        public ChoreBox(GroupChoreRegistrationEntity groupRegistration, LocalDate date) {
            this.groupRegistration = groupRegistration;
            this.date = date;
            setupSpecificRegistrations();
            setupPersonalRegistration();
            setupCheckBox();
            setupIcon();
            setupLayout();
        }

        private void setupLayout() {
            this.addClassName("chore_box");
            add(infoIcon, check, countDiv);
        }

        private void setupPersonalRegistration() {
            personalRegistration = specificRegistrations.stream()
                    .filter(reg -> reg.getUser().equals(securityService.getAuthenticatedUser().get()))
                    .findAny();
        }

        private void setupCheckBox() {
            check.setValue(personalRegistration.isPresent());
            check.addValueChangeListener(change -> {
               if (change.getValue()) {
                   ChoreRegistrationEntity newReg = new ChoreRegistrationEntity();
                   newReg.setUser(securityService.getAuthenticatedUser().get());
                   newReg.setDate(date);
                   newReg.setGroupChoreRegistration(groupRegistration);

                   ServiceResponse<ChoreRegistrationEntity> response = choreRegistrationService.save(newReg);
                   if (!response.isOperationSuccessful()) {
                       Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                       return;
                   }
                   Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
                   personalRegistration = Optional.of(response.getBusinessObjects().get(0));
               } else {
                   if (personalRegistration.isEmpty()) {
                       Notifier.showErrorNotification(getTranslation("mainView.error.noRegistrationToDelete"));
                       return;
                   }
                   ServiceResponse<ChoreRegistrationEntity> response =
                           choreRegistrationService.delete(personalRegistration.get());
                   if (!response.isOperationSuccessful()) {
                       Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                       return;
                   }
                   Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
                    personalRegistration = Optional.empty();
               }
               updateRegistrationCount(change.getValue());
            });
        }

        private void setupIcon() {
            if (registrationCount == 0)
                infoIcon.addClassName("disabled");
            infoIcon.addClickListener(click -> {
                if (registrationCount == 0)
                    return;
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle(getTranslation("mainView.dialog.title"));

                Button closeDialogButton = new Button(getTranslation("mainView.dialog.closeButton"));
                closeDialogButton.addClickListener(c -> dialog.close());
                dialog.getFooter().add(closeDialogButton);

                dialog.add(getTranslation("mainView.dialog.content", groupRegistration.getChore().getName()));
                UnorderedList ul = new UnorderedList();
                specificRegistrations.stream().
                        map(ChoreRegistrationEntity::getUser)
                        .forEach(user -> ul.add(new ListItem(user.getUsername())));
                dialog.add(ul);

                dialog.open();
            });
        }

        private void updateRegistrationCount(boolean value) {
            registrationCount += value ? 1 : -1;
            if (registrationCount == 0) {
                infoIcon.removeClassName("disabled");
            } else {
                infoIcon.addClassName("disabled");
            }
            updateShownRegistrationCount();
        }

        private void updateShownRegistrationCount() {
            countDiv.removeAll();
            countDiv.add(String.valueOf(registrationCount));
        }

        private void setupSpecificRegistrations() {
            this.specificRegistrations = groupRegistration.getChoreRegistrations()
                    .stream().filter(reg -> reg.getDate().isEqual(date)).toList();
            registrationCount = specificRegistrations.size();
            updateShownRegistrationCount();
        }

    }
}
