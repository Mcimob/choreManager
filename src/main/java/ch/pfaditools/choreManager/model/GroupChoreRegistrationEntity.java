package ch.pfaditools.choreManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class GroupChoreRegistrationEntity extends AbstractEntity {

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private ChoreEntity chore;
    @ManyToOne
    private GroupEntity group;

    @OneToMany(mappedBy = "groupChoreRegistration", fetch = FetchType.EAGER)
    private Set<ChoreRegistrationEntity> choreRegistrations = new HashSet<>();

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ChoreEntity getChore() {
        return chore;
    }

    public void setChore(ChoreEntity chore) {
        this.chore = chore;
    }

    public GroupEntity getGroup() {
        return group;
    }

    public void setGroup(GroupEntity group) {
        this.group = group;
    }

    public Set<ChoreRegistrationEntity> getChoreRegistrations() {
        return choreRegistrations;
    }

    public void setChoreRegistrations(Set<ChoreRegistrationEntity> choreRegistrations) {
        this.choreRegistrations = choreRegistrations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupChoreRegistrationEntity that = (GroupChoreRegistrationEntity) o;
        return Objects.equals(getStartDate(), that.getStartDate())
                && Objects.equals(getEndDate(), that.getEndDate())
                && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartDate(), getEndDate(), getId());
    }
}
