package ch.pfaditools.choreManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class ChoreRegistrationEntity extends AbstractEntity {

    private LocalDate date;
    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private GroupChoreRegistrationEntity groupChoreRegistration;


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public GroupChoreRegistrationEntity getGroupChoreRegistration() {
        return groupChoreRegistration;
    }

    public void setGroupChoreRegistration(GroupChoreRegistrationEntity groupChoreRegistration) {
        this.groupChoreRegistration = groupChoreRegistration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChoreRegistrationEntity that = (ChoreRegistrationEntity) o;
        return Objects.equals(getDate(), that.getDate()) && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getId());
    }
}
