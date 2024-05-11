package ru.kata.spring.boot_security.services;

import ru.kata.spring.boot_security.entities.Role;
import java.util.Collection;

public interface RoleService {

    void save(Role role);

    Collection<Role> getAllRoles();
}
