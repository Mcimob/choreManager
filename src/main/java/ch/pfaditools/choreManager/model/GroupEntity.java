package ch.pfaditools.choreManager.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class GroupEntity extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String groupCode;

    @ManyToMany(mappedBy = "groups")
    private Set<UserEntity> users = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    private Set<GroupChoreRegistrationEntity> groupChoreRegistrations = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    public void addUser(UserEntity user) {
        users.add(user);
    }


    public Set<GroupChoreRegistrationEntity> getGroupChoreRegistrations() {
        return groupChoreRegistrations;
    }

    public void setGroupChoreRegistrations(Set<GroupChoreRegistrationEntity> groupChoreRegistrations) {
        this.groupChoreRegistrations = groupChoreRegistrations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupEntity that = (GroupEntity) o;
        return Objects.equals(name, that.name) && Objects.equals(groupCode, that.groupCode) && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, groupCode, getId());
    }
}
