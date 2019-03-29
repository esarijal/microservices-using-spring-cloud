package com.mitrais.microservices.netflixzuulapigatewayserver.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Privilege extends AbstractModel<String, Long> {
    @NonNull
    private String name;

    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Role> roles;

}
