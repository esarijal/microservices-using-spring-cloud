package com.mitrais.microservices.netflixzuulapigatewayserver.controller;

import com.mitrais.microservices.netflixzuulapigatewayserver.exception.NotFoundException;
import com.mitrais.microservices.netflixzuulapigatewayserver.model.Role;
import com.mitrais.microservices.netflixzuulapigatewayserver.repository.RoleRepository;
import com.mitrais.microservices.netflixzuulapigatewayserver.service.IRoleService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/v1/roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RoleController extends AbstractCrudController<Role, Long>{

    @NonNull RoleRepository roleRepository;
    @NonNull IRoleService roleService;

    @Secured(ROLE_ADMIN)
    @GetMapping
    @Override
    ResponseEntity<?> findAll(){
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("{id}")
    @Override
    Resource<?> findById(@PathVariable Long id){
        Role role = roleRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("ID " + id + " is not Found"));

        // HATEOAS
        // "all-users", SERVER_PATH + "/users"
        Resource<Role> resource = new Resource<>(role);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAll());
        resource.add(linkTo.withRel("all-roles"));
        return resource;
    }

    @Secured(ROLE_ADMIN)
    @PostMapping
    @Override
    ResponseEntity<?> create(@RequestBody Role role){
        return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).build();
    }

    @Secured(ROLE_ADMIN)
    @PutMapping("{id}")
    @Override
    ResponseEntity<?> update(@RequestBody Role role, @PathVariable @NotNull Long id){
        return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).build();
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("{id}")
    @Override
    ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).build();
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("{id}/users")
    ResponseEntity<?> findAllUsersByRole(@PathVariable Long id){
        Optional<Role> optRole = roleRepository.findById(id);
        if(!optRole.isPresent()){
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap(ERROR, "role not found"));
        }

        Role role = optRole.get();
        return ResponseEntity.ok().body(role.getUsers());
    }

    @Secured(ROLE_ADMIN)
    @PostMapping("{id}/users/{userId}")
    @Transactional
    ResponseEntity<?> linkUserToRole(@PathVariable Long id, @PathVariable Long userId){
        if(roleService.addUserToRole(userId, id)){
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userId)
                    .toUri();
            return ResponseEntity.created(uri).build();
        }

        throw new NotFoundException("User or role not found");

    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("{id}/users/{userId}")
    @Transactional
    ResponseEntity<?> delinkUserToRole(@PathVariable Long id, @PathVariable Long userId){
        if(roleService.removeUserFromRole(userId, id)){
            return ResponseEntity.noContent().build();
        }
        throw new NotFoundException("User or role not found");
    }
}
