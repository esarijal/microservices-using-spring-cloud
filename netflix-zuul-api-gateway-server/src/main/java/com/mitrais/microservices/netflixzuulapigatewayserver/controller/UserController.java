package com.mitrais.microservices.netflixzuulapigatewayserver.controller;

import com.mitrais.microservices.netflixzuulapigatewayserver.controller.payload.UserPayload;
import com.mitrais.microservices.netflixzuulapigatewayserver.exception.DuplicateUniqueFieldException;
import com.mitrais.microservices.netflixzuulapigatewayserver.exception.NotFoundException;
import com.mitrais.microservices.netflixzuulapigatewayserver.model.User;
import com.mitrais.microservices.netflixzuulapigatewayserver.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("api/v1/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UserController extends AbstractCrudController<UserPayload, Long> {

    static final String ROLE_ADMIN = "ROLE_ADMIN";
    static final String ERROR = "error";

    @NonNull UserRepository userRepository;

    @Secured(ROLE_ADMIN)
    @GetMapping
    @Override
    ResponseEntity<?> findAll(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("{id}")
    @Override
    Resource<User> findById(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("ID " + id + " is not Found"));

        // HATEOAS
        // "all-users", SERVER_PATH + "/users"
        Resource<User> resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).findAll());
        resource.add(linkTo.withRel("all-users"));
        return resource;

    }

    @Secured(ROLE_ADMIN)
    @PostMapping
    @Override
    ResponseEntity<?> create(@Valid @RequestBody UserPayload userPayload){
        try{
            User user = userRepository.save(new User(userPayload));
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (DataIntegrityViolationException de){
            throw new DuplicateUniqueFieldException("Constraint violation");
        } catch (DataAccessException e){
            throw e;
        }
    }

    @Secured(ROLE_ADMIN)
    @PutMapping("{id}")
    @Override
    ResponseEntity<?> update(@RequestBody UserPayload userPayload, @PathVariable @NotNull Long id){
        try{
            User user = new User(userPayload);
            user.setId(id);
            user = userRepository.save(user);
            return ResponseEntity.ok(user);
        } catch (DataIntegrityViolationException de){
            throw new DuplicateUniqueFieldException("Constraint violation");
        } catch (DataAccessException e){
            throw e;
        }
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("{id}")
    @Override
    ResponseEntity<?> delete(@PathVariable Long id){
        try{
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataAccessException e){
            throw e;
        }
    }

    @GetMapping("current")
    @ResponseStatus(HttpStatus.OK)
    User getCurrent(@AuthenticationPrincipal final User user){
        return userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


}
