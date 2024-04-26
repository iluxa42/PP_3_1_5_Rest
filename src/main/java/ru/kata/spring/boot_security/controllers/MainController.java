package ru.kata.spring.boot_security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.entities.User;
import ru.kata.spring.boot_security.services.RoleService;
import ru.kata.spring.boot_security.services.UserService;

import java.security.Principal;

@Controller
public class MainController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model, Principal principal) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("authUser", userService.findByUsername(principal.getName()));
        return "index";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Principal principal) {
        User authUser = userService.findByUsername(principal.getName());
        userService.saveUser(user);
        return (authUser.getId().equals(user.getId())
                && !user.getRolesNames().contains("ADMIN"))
                    ? "redirect:/logout" // админ снял с себя роль
                    : "redirect:/";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id, Principal principal) {
        User authUser = userService.findByUsername(principal.getName());
        userService.deleteUser(id);
        return (authUser.getId().equals(id))
                ? "redirect:/logout" // админ удалил себя
                : "redirect:/";
    }
}
