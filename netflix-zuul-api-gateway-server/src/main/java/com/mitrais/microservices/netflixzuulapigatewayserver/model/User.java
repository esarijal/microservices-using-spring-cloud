package com.mitrais.microservices.netflixzuulapigatewayserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mitrais.microservices.netflixzuulapigatewayserver.controller.payload.UserPayload;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@JsonIgnoreProperties({"accountNonExpired","accountNonLocked","credentialsNonExpired","authorities"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbstractModel<String, Long> implements UserDetails {
    @NonNull
    @Column(unique = true)
    String username;
    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;
    @Column(unique = true)
    @Email
    String email;
    String token;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    }
    , fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
//    @JsonManagedReference
    private Set<Role> roles;

    @Transactional
    public void addRole(Role role){
        if(getRoles() == null){
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    @Transactional
    public void removeRole(Role role){
        if(getRoles() == null){
            return;
        }
        this.roles.remove(role);
    }

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role: roles){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
            role.getPrivileges().stream()
                    .map(p -> new SimpleGrantedAuthority(p.getName()))
                    .forEach(authorities::add);

        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(UserPayload payload) {
        username = payload.getUsername();
        password = BCrypt.hashpw(payload.getPassword(), BCrypt.gensalt());
        email = payload.getEmail();
    }
}
