package com.mitrais.microservices.netflixzuulapigatewayserver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Role extends AbstractModel<String, Long>{
    @NonNull
    private String roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
//    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<User> users;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    }
    , fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;

    @Transactional
    public void addPrivilege(Privilege privilege){
        if(getPrivileges() == null){
            this.privileges = new HashSet<>();
        }
        this.privileges.add(privilege);
    }

    @Transactional
    public void removePrivilege(Privilege privilege){
        if(getPrivileges() == null){
            return;
        }
        this.privileges.remove(privilege);
    }
}
