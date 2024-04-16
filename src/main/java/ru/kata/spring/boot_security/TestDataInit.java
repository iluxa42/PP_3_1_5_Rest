package ru.kata.spring.boot_security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.entities.Role;
import ru.kata.spring.boot_security.entities.User;
import ru.kata.spring.boot_security.services.RoleService;
import ru.kata.spring.boot_security.services.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class TestDataInit {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public TestDataInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        final Role ROLE_USER = new Role("ROLE_USER");
        final Role ROLE_ADMIN = new Role("ROLE_ADMIN");

        roleService.save(ROLE_USER);
        roleService.save(ROLE_ADMIN);

        // password = username
        userService.saveUser(new User(1L, "Ivan", "Ivanov", 35, 1200, "ivanovich81@gmail.com",
                "ivanov777",       // username
                "$2a$12$lLMeBQD24bw/ZUhtW47LyeToYtGq4imvFqiHus9SdfyS7rZZ.9NqG", // ivanov777
                new HashSet<>(Set.of(ROLE_USER))));
        userService.saveUser(new User(2L, "Victor", "Petrov", 22, 950, "petrovich3000@mail.ru",
                "victor555",       // username
                "$2a$12$dQNjZBbO.Rk99NdZzSOIsOdoXDwy1o2zZLAr/KVORGhnwAKDzVim.", // victor555
                new HashSet<>(Set.of(ROLE_ADMIN))));
        userService.saveUser(new User(3L, "Софья", "Риановна", 43, 1030, "sophico11@yahoo.com",
                "sophico11",       // username
                "$2a$12$N4kmorTZLSAZaWQwXspsr./lo3ypGZol6Tak5xXn16kfOu6KkWpAm", // sophico11
                new HashSet<>(Set.of(ROLE_USER, ROLE_ADMIN))));
        userService.saveUser(new User(4L, "Sidr", "Sidorov", 31, 870, "sidr.sidr@list.ru",
                "sidr111",       // username
                "$2a$12$v0yvhJCDIjc8ZqwQqvAwbeh4IIBg3qyU1ddAMlJPi3x/YJxKSwfji", // sidr111
                new HashSet<>(Set.of(ROLE_USER))));
        userService.saveUser(new User(5L, "Петр", "Лебедев", 39, 720, "lebed.white@yahoo.com",
                "lebed777",       // username
                "$2a$12$6wBmvtW42bJmp2lVJkTM.uGNV2I1PHbFQgiZ.lmEvsZevrN.oOT82", // lebed777
                new HashSet<>(Set.of(ROLE_USER, ROLE_ADMIN))));
    }
}