package ch.pfaditools.choreManager.views.meal;

import ch.pfaditools.choreManager.backend.service.IMealSuggestionVoteEntityService;
import ch.pfaditools.choreManager.backend.service.IMealSuggestionsEntityService;
import ch.pfaditools.choreManager.backend.service.ServiceResponse;
import ch.pfaditools.choreManager.layout.MainLayout;
import ch.pfaditools.choreManager.model.GroupEntity;
import ch.pfaditools.choreManager.model.MealSuggestionEntity;
import ch.pfaditools.choreManager.model.MealSuggestionVoteEntity;
import ch.pfaditools.choreManager.model.UserEntity;
import ch.pfaditools.choreManager.security.SecurityService;
import ch.pfaditools.choreManager.util.DateUtils;
import ch.pfaditools.choreManager.util.Notifier;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Route(value = "mealView", layout = MainLayout.class)
@PermitAll
public class MealView extends VerticalLayout {
    private final SecurityService securityService;
    private final IMealSuggestionsEntityService mealService;
    private final IMealSuggestionVoteEntityService mealVoteService;

    private Select<GroupEntity> groupSelect = new Select<>();
    private GroupEntity selectedGroup;
    private LocalDate currentDate = LocalDate.now();
    private final Text dateText = new Text("");
    private final HorizontalLayout datePickerLayout = new HorizontalLayout();
    private final VerticalLayout suggestionLayout = new VerticalLayout();
    private final Div suggestionPickerLayout = new Div();
    public MealView(SecurityService securityService, IMealSuggestionsEntityService mealService, IMealSuggestionVoteEntityService mealVoteService) {
        this.securityService = securityService;
        this.mealService = mealService;
        this.mealVoteService = mealVoteService;
        setupLayout();
        setupGroupSelect();
        setupDatePicker();
        setupSuggestionAdder();
    }

    private void setupLayout() {
        datePickerLayout.addClassName("mealView__sticky");

        suggestionLayout.getStyle().setPaddingLeft("2rem");
        suggestionLayout.getStyle().setPaddingRight("2rem");

        H1 title = new H1(getTranslation("mealView.title"));
        title.getStyle().setTextAlign(Style.TextAlign.CENTER);
        title.getStyle().setPaddingRight("2rem");
        title.getStyle().setPaddingLeft("2rem");
        title.getStyle().setPaddingTop("1rem");

        add(title,
                datePickerLayout,
                suggestionLayout,
                suggestionPickerLayout);

        this.getStyle().setAlignItems(Style.AlignItems.CENTER);
        this.setMaxWidth("800px");
        this.setWidthFull();
        this.setPadding(false);
    }

    private void setupGroupSelect() {
        groupSelect.setLabel(getTranslation("mealView.groupSelect"));
        groupSelect.addValueChangeListener(change -> {
            selectedGroup = change.getValue();
            updateMealSuggestions();
        });
        UserEntity user = securityService.getAuthenticatedUser().get();
        if (user.getGroups().size() == 1) {
            selectedGroup = user.getGroups().stream().toList().getFirst();
            updateMealSuggestions();
        }
    }

    private void setupDatePicker() {
        Button previousButton = new Button(VaadinIcon.ANGLE_LEFT.create(), click -> {
            currentDate = currentDate.minusDays(1);
            updateDatePicker();
        });
        Button nextButton = new Button(VaadinIcon.ANGLE_RIGHT.create(), click -> {
            currentDate = currentDate.plusDays(1);
            updateDatePicker();
        });
        updateDatePicker();

        datePickerLayout.add(previousButton, dateText, nextButton);
        datePickerLayout.setAlignItems(Alignment.CENTER);
        datePickerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void updateDatePicker() {
        dateText.setText(DateUtils.formatDateShort(currentDate));
        updateMealSuggestions();
    }

    private void updateMealSuggestions() {
        suggestionLayout.removeAll();
        ServiceResponse<MealSuggestionEntity> response = mealService.getByGroupAndDate(selectedGroup, currentDate);
        if (!response.isOperationSuccessful()) {
            suggestionLayout.add(new Text(getTranslation(response.getErrorMessage())));
            return;
        }

        List<MealSuggestionEntity> mealSuggestions = response.getBusinessObjects();
        mealSuggestions.sort(Comparator.comparingInt(a -> -a.getMealSuggestionVotes().size()));
        for (MealSuggestionEntity mealSuggestion : mealSuggestions) {
            suggestionLayout.add(new SuggestionBox(mealSuggestion));
        }

    }

    private void setupSuggestionAdder() {
        TextField suggestionField = new TextField(getTranslation("mealView.suggestionField"));
        Button addButton = new Button(VaadinIcon.CHECK.create(), click -> {
            if (suggestionField.isEmpty()) {
                Notifier.showErrorNotification(getTranslation("mealView.error.suggestionField"));
                return;
            }
            UserEntity user = securityService.getAuthenticatedUser().get();
            MealSuggestionEntity newMealSuggestion = new MealSuggestionEntity();
            newMealSuggestion.setSuggestedDate(currentDate);
            newMealSuggestion.setSuggestedBy(user);
            newMealSuggestion.setSuggestedFor(selectedGroup);
            newMealSuggestion.setName(suggestionField.getValue());

            ServiceResponse<MealSuggestionEntity> response = mealService.save(newMealSuggestion);
            if (!response.isOperationSuccessful()) {
                Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                return;
            }
            Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
            suggestionField.clear();
            updateMealSuggestions();
        });

        suggestionPickerLayout.add(suggestionField, addButton);
    }

    private class SuggestionBox extends Div {

        private final MealSuggestionEntity mealSuggestion;
        private final Checkbox checkBox = new Checkbox();

        private Optional<MealSuggestionVoteEntity> suggestionVote = Optional.empty();

        public SuggestionBox(MealSuggestionEntity mealSuggestion) {
            this.mealSuggestion = mealSuggestion;
            setupLayout();
            setupCheckBox();
        }

        private void setupLayout() {
            this.addClassName("suggestion_box");

            Div nameContainer = new Div(mealSuggestion.getSuggestedBy().getUsername());
            nameContainer.addClassName("name_container");

            VerticalLayout infoContainer = new VerticalLayout(
                    new Div(String.valueOf(mealSuggestion.getMealSuggestionVotes().size())),
                    nameContainer);
            infoContainer.setPadding(false);

            infoContainer.getStyle().setAlignItems(Style.AlignItems.CENTER);

            HorizontalLayout rightLayout = new HorizontalLayout(checkBox, infoContainer);
            rightLayout.setAlignItems(Alignment.CENTER);

            add(new Div(mealSuggestion.getName()));
            add(rightLayout);
        }

        private void setupCheckBox() {
            UserEntity user = securityService.getAuthenticatedUser().get();
            ServiceResponse<MealSuggestionVoteEntity> voteResponse = mealVoteService.getByUserAndMealSuggestion(user, mealSuggestion);
            if (voteResponse.isOperationSuccessful()) {
                suggestionVote = Optional.of(voteResponse.getBusinessObjects().getFirst());
            }
            checkBox.setValue(voteResponse.isOperationSuccessful());

            checkBox.addValueChangeListener(change -> {
               if (change.getValue()) {
                   MealSuggestionVoteEntity newSuggestionVote = new MealSuggestionVoteEntity();
                   newSuggestionVote.setMealSuggestion(mealSuggestion);
                   newSuggestionVote.setVotedBy(user);

                   ServiceResponse<MealSuggestionVoteEntity> response = mealVoteService.save(newSuggestionVote);
                   if (!response.isOperationSuccessful()) {
                       Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                       checkBox.setValue(change.getOldValue());
                       return;
                   }
                   suggestionVote = Optional.of(response.getBusinessObjects().getFirst());
                   Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));

               } else {
                   if (suggestionVote.isEmpty()) {
                       Notifier.showErrorNotification(getTranslation("mealView.error.noSuggestionVote"));
                   }
                   ServiceResponse<MealSuggestionVoteEntity> response = mealVoteService.delete(suggestionVote.get());
                   if (!response.isOperationSuccessful()) {
                       Notifier.showErrorNotification(getTranslation(response.getErrorMessage()));
                       checkBox.setValue(change.getOldValue());
                       return;
                   }
                   suggestionVote = Optional.empty();
                   Notifier.showSuccessNotification(getTranslation(response.getInfoMessage()));
               }
               updateMealSuggestions();
            });

        }

    }
}
