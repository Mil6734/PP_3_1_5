package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }


    @Transactional
    @Override
    public void addUser(User user, List<Long> roleId) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleId));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User user, List<Long> roleId) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleId));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}














//package ru.kata.spring.boot_security.demo.service;
//
//
//import com.pp_3_1_2_first.springboot.dao.UserDao;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import ru.kata.spring.boot_security.demo.model.User;
//
//import java.util.List;
//
//@Service
//public class UserServiceImp implements UserService {
//
//    private UserDao userDao;
//
//    @Autowired
//    public UserServiceImp(UserDao userDao) {
//        this.userDao = userDao;
//    }
//
//    @Transactional
//    @Override
//    public void addUser(User user) {
//        userDao.addUser(user);
//    }
//
//    @Transactional
//    @Override
//    public void updateUser(User user) {
//        userDao.updateUser(user);
//    }
//
//    @Transactional
//    @Override
//    public void deleteUser(Long id) {
//        userDao.deleteUser(id);
//    }
//
//    @Transactional
//    @Override
//    public List<User> getAllUsers() {
//        return userDao.getAllUsers();
//    }
//
//    @Transactional
//    @Override
//    public User getUserById(Long id) {
//        return userDao.getUserById(id);
//    }
//}
