    package ru.kata.spring.boot_security.demo.controllers;


    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;
    import ru.kata.spring.boot_security.demo.model.User;
    import ru.kata.spring.boot_security.demo.service.RoleService;
    import ru.kata.spring.boot_security.demo.service.UserService;

    import java.util.List;

    @RestController
    @RequestMapping("/api/admin")
    public class AdminController {
        private final UserService userService;
        private final RoleService roleService;
        private final PasswordEncoder passwordEncoder;

        @Autowired
        public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
            this.userService = userService;
            this.roleService = roleService;
            this.passwordEncoder = passwordEncoder;
        }


        @GetMapping("/users")
        public ResponseEntity<List<User>> getAllUsers(Authentication auth) {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }

        @GetMapping("/users/{id}")
        public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
            User user = userService.getUserById(id);
            return user != null ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        @PostMapping("/users")
        public ResponseEntity<User> addUser(@RequestBody User user, @RequestParam List<Long> roleId) {
            userService.addUser(user, roleId);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }

        @PutMapping("/users/{id}")
        public ResponseEntity<HttpStatus> updateUser(@PathVariable("id") Long id, @RequestBody User updatedUser,
                @RequestParam(value = "newPassword", required = false) String newPassword,
                @RequestParam List<Long> roleId) {

            User existingUser = userService.getUserById(id);
            if (existingUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if (newPassword != null && !newPassword.isEmpty()) {
                String hashedPassword = passwordEncoder.encode(newPassword);
                existingUser.setPassword(hashedPassword);
            }

            existingUser.setName(updatedUser.getName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setRoles(updatedUser.getRoles());

            userService.updateUser(existingUser, roleId);
            return new ResponseEntity<>(HttpStatus.OK);
        }


        @DeleteMapping("/users/{id}")
        public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }