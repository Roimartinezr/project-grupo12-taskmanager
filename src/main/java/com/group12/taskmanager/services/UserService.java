package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.User;
import com.group12.taskmanager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupService groupService;

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

    public Group findPersonalGroup(int userId) {
        User user = findUserById(userId);
        if (user == null) return null;

        String personalGroupName = "USER_" + user.getName();

        return user.getGroups().stream()
                .filter(group -> group.getName().equals(personalGroupName))
                .findFirst()
                .orElse(null);
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

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user); // Hibernate detecta si es nuevo o existente
    }

    public boolean deleteUser(int userId, User currentUser) {
        if (currentUser.getId() != userId) {
            System.out.println("Not authorized to delete this account.");
            return false;
        }

        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) return false;

            // Clonamos la lista de grupos para evitar problemas de concurrencia
            List<Group> userGroups = new ArrayList<>(user.getGroups());

            for (Group group : userGroups) {
                if (group.getOwner().getId().equals(user.getId())) {
                    // Si el usuario es propietario: eliminar el grupo completo
                    groupService.deleteGroup(group.getId(), user); // 👈 usa tu método existente
                } else {
                    // Si NO es propietario: eliminar solo su relación con el grupo
                    group.getUsers().remove(user);
                    user.getGroups().remove(group);
                    groupService.saveGroup(group); // guardar el grupo actualizado
                }
            }

            // Finalmente eliminar al usuario
            userRepository.delete(user);
            System.out.println("Usuario y sus grupos eliminados correctamente.");
            return true;

        } catch (Exception e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
