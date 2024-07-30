package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller

public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String allUsers(Model model, Authentication auth) {
        List<User> users = userService.getAllUsers();
        User curUser = (User) auth.getPrincipal();
        model.addAttribute("users", users);
        model.addAttribute("curUser", curUser);
        return "admin";
    }

    @GetMapping("/admin/new-user")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());
        return "newUser";
    }

    @PostMapping("/admin/add-user")
    public String addUser(@ModelAttribute("user") User user, @RequestParam List<Long> roleId) {
        userService.addUser(user, roleId);
        return "redirect:/admin";
    }

    @GetMapping("admin/edit/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        return "editUser";
    }

    @PostMapping("admin/edit/{id}")
    public String editUser(@PathVariable("id") Long id, @ModelAttribute("user") User user, @RequestParam List<Long> roleId) {
        user.setId(id);
        userService.updateUser(user, roleId);
        return "redirect:/admin";
    }

    @GetMapping("/admin/view")
    public String showViewUserForm(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoles());
        return "viewUser";
    }

    @DeleteMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
