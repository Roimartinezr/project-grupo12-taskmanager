package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.User;
import com.group12.taskmanager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void addUser(User user) {
        userRepository.save(user); // 👈 primero se guarda el usuario
        for (Group group : user.getGroups()) {
            group.getUsers().add(user); // asegúrate de que esta relación sea bidireccional
        }
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findUserByUsername(String userName) {
        return userRepository.findByName(userName);
    }

    // Actualizar este método para que use la consulta personalizada que recupera los grupos
    @Transactional
    public User findUserByEmail(String email) {
        return userRepository.findByEmailWithGroups(email); // Usamos el método de repositorio que evita el LazyInitializationException
    }

    public List<User> searchUsersByNameExcludingGroup(String q, Group group) {
        if (q == null || q.trim().isEmpty()) {
            return List.of(); // evita devolver todos los usuarios si no hay búsqueda
        }

        return userRepository.findByNameStartingWithExcludingGroup(q.trim(), group.getUsers());
    }


    public boolean deleteUser(int userId, User currentUser) {
        if (currentUser.getId() != userId) {
            System.out.println("Not authorized to delete this account.");
            return false;
        }

        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            System.out.println("Account successfully deleted: " + userId);
            return true;
        }

        System.out.println("User with ID not found: " + userId);
        return false;
    }
}
