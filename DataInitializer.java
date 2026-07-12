package com.lifetrace.backend.config;

import com.lifetrace.backend.model.Role;
import com.lifetrace.backend.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {

        List<String> roles = List.of(
                "ROLE_DONOR",
                "ROLE_HOSPITAL",
                "ROLE_ADMIN"
        );

        for (String roleName : roles) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(
                            new Role(null, roleName)
                    ));
        }
    }
}
