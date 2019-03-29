package com.mitrais.microservices.netflixzuulapigatewayserver.service;

import java.util.List;

public interface IRoleService {
    boolean addUserToRole(Long userId, Long roleId);
    boolean addUsersToRole(List<Long> userIds, Long roleId);

    boolean removeUserFromRole(Long userId, Long roleId);


}
