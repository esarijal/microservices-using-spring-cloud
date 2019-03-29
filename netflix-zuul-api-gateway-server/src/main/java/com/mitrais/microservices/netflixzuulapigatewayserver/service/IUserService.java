package com.mitrais.microservices.netflixzuulapigatewayserver.service;


import com.mitrais.microservices.netflixzuulapigatewayserver.model.Privilege;
import com.mitrais.microservices.netflixzuulapigatewayserver.model.Role;
import com.mitrais.microservices.netflixzuulapigatewayserver.model.User;

public interface IUserService {
    void addPrivilegeToRole(Privilege privilege, Role role);
    void removePrivilegeFromRole(Privilege privilege, Role role);

    void addRoleToUser(Role role, User user);
    void removeRoleFromUser(Role role, User user);
}
