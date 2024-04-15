package ch.pfaditools.choreManager.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class MealSuggestionEntity extends AbstractEntity {

    private String name;
    private LocalDate suggestedDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity suggestedBy;
    @ManyToOne
    private GroupEntity suggestedFor;
    @OneToMany(mappedBy = "mealSuggestion", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<MealSuggestionVoteEntity> mealSuggestionVotes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getSuggestedDate() {
        return suggestedDate;
    }

    public void setSuggestedDate(LocalDate suggestedDate) {
        this.suggestedDate = suggestedDate;
    }

    public UserEntity getSuggestedBy() {
        return suggestedBy;
    }

    public void setSuggestedBy(UserEntity suggestedBy) {
        this.suggestedBy = suggestedBy;
    }

    public GroupEntity getSuggestedFor() {
        return suggestedFor;
    }

    public void setSuggestedFor(GroupEntity suggestedFor) {
        this.suggestedFor = suggestedFor;
    }

    public List<MealSuggestionVoteEntity> getMealSuggestionVotes() {
        return mealSuggestionVotes;
    }

    public void setMealSuggestionVotes(List<MealSuggestionVoteEntity> mealSuggestionVotes) {
        this.mealSuggestionVotes = mealSuggestionVotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealSuggestionEntity that = (MealSuggestionEntity) o;
        return Objects.equals(getName(), that.getName())
                && Objects.equals(getSuggestedDate(), that.getSuggestedDate())
                && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSuggestedDate(), getId());
    }

    @Override
    public String toString() {
        return "MealSuggestionEntity{" +
                "id='" + getId() + '\'' +
                "name='" + name + '\'' +
                ", suggestedDate=" + suggestedDate +
                '}';
    }
}
