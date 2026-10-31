package com.bookworms.controller;

import com.bookworms.dto.UpdateBanRequest;
import com.bookworms.dto.UpdateProfileRequest;
import com.bookworms.dto.UpdateRoleRequest;
import com.bookworms.model.User;
import com.bookworms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** Admin: list every user. */
    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getOne(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PatchMapping("/{id}/profile")
    public User updateProfile(@PathVariable Long id, @RequestBody UpdateProfileRequest req) {
        return userService.updateProfile(id, req.name(), req.bio());
    }

    /** Admin: suspend or unsuspend an account. */
    @PatchMapping("/{id}/ban")
    public User setBanned(@PathVariable Long id, @Valid @RequestBody UpdateBanRequest req) {
        return userService.setBanned(id, req.banned());
    }

    /** Admin: promote to admin or demote to reader. */
    @PatchMapping("/{id}/role")
    public User setRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest req) {
        return userService.setRole(id, req.role());
    }
}
