package com.webcrudsecurityboot.webcrudsecurityboot.controller;

import com.webcrudsecurityboot.webcrudsecurityboot.model.Role;
import com.webcrudsecurityboot.webcrudsecurityboot.model.User;
import com.webcrudsecurityboot.webcrudsecurityboot.service.RoleService;
import com.webcrudsecurityboot.webcrudsecurityboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;

@Controller
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin")
    public String welcome() {
        return "redirect:/admin/all";
    }

    @GetMapping(value = "admin/all")
    public String allUsers(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "index";
    }

    @GetMapping("admin/{id}")
    public String show(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.show(user.getId()));
        model.addAttribute("role", roleService.show(user.getId()));
        return "show";
    }

    @GetMapping(value = "admin/add")
    public String addUser(Model model,
                          @ModelAttribute("user") User user,
                          @ModelAttribute("role") Role role) {
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "new";
    }

    @PostMapping(value = "admin/add")
    public String postAddUser(@ModelAttribute("user") User user,
                              @RequestParam("rolesSelected") Long[] rolesId,
                              BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "new";
        }
        HashSet<Role> roles = new HashSet<>();
        for(Long roleId : rolesId) {
            roles.add(roleService.show(roleId));
        }

        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }


    @GetMapping(value = "admin/{id}/edit")
    public String editUser(ModelMap model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.show(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "edit";
    }

    @PatchMapping("admin/{id}")
    public String update(@ModelAttribute("user") @Valid User user, @PathVariable("id") Long id,
                         @RequestParam("rolesSelected") Long[] rolesId,
                         BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "edit";
        }
        HashSet<Role> roles = new HashSet();
        for(Long roleId : rolesId) {
            roles.add(roleService.show(roleId));
        }
        user.setRoles(roles);
        user.getId();
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("admin/{id}")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
