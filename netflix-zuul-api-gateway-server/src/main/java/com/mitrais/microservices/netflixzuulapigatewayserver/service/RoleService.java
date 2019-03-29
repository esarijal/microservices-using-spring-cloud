package com.mitrais.microservices.netflixzuulapigatewayserver.service;

import com.mitrais.microservices.netflixzuulapigatewayserver.model.Role;
import com.mitrais.microservices.netflixzuulapigatewayserver.model.User;
import com.mitrais.microservices.netflixzuulapigatewayserver.repository.RoleRepository;
import com.mitrais.microservices.netflixzuulapigatewayserver.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RoleService implements IRoleService {
    @NonNull RoleRepository roleRepository;
    @NonNull UserRepository userRepository;

    @Override
    @Transactional
    public boolean addUserToRole(Long userId, Long roleId) {
        Optional<User> optUser = userRepository.findById(userId);
        Optional<Role> optRole = roleRepository.findById(roleId);
        if(!optUser.isPresent() || !optRole.isPresent()){
            return false;
        }

        User user = optUser.get();
        user.addRole(optRole.get());
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean addUsersToRole(List<Long> userIds, Long roleId) {
        Optional<Role> optRole = roleRepository.findById(roleId);
        List<User> users = new ArrayList<>();

        List<Boolean> collect = userIds.stream().map(userId -> {
            Optional<User> optUser = userRepository.findById(userId);
            if (!optUser.isPresent() || !optRole.isPresent()) {
                return false;
            }

            User user = optUser.get();
            user.addRole(optRole.get());
            users.add(user);
            return true;
        }).collect(Collectors.toList());

        if(collect.contains(false)){
            return false;
        }

        userRepository.saveAll(users);
        return true;
    }

    @Override
    @Transactional
    public boolean removeUserFromRole(Long userId, Long roleId) {
        Optional<User> optUser = userRepository.findById(userId);
        Optional<Role> optRole = roleRepository.findById(roleId);
        if(!optUser.isPresent() || !optRole.isPresent()){
            return false;
        }

        User user = optUser.get();
        user.removeRole(optRole.get());
        userRepository.save(user);
        return true;
    }
}
