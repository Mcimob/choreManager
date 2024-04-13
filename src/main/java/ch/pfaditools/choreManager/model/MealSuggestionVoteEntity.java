package ch.pfaditools.choreManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.Objects;

@Entity
public class MealSuggestionVoteEntity extends AbstractEntity {

    @ManyToOne
    private UserEntity votedBy;

    @ManyToOne
    private MealSuggestionEntity mealSuggestion;

    public UserEntity getVotedBy() {
        return votedBy;
    }

    public void setVotedBy(UserEntity votedBy) {
        this.votedBy = votedBy;
    }

    public MealSuggestionEntity getMealSuggestion() {
        return mealSuggestion;
    }

    public void setMealSuggestion(MealSuggestionEntity mealSuggestion) {
        this.mealSuggestion = mealSuggestion;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealSuggestionVoteEntity that = (MealSuggestionVoteEntity) o;
        return Objects.equals(getId(), that.getId());
    }
}
