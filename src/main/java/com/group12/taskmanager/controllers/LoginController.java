package com.group12.taskmanager.controllers;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.User;
import com.group12.taskmanager.services.GroupService;
import com.group12.taskmanager.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Transactional
    @PostConstruct
    public void initData() {
        if (userService.findUserByEmail("admin@admin.com") == null) {
            // Crear usuarios
            User u1 = new User("admin", "admin@admin.com", "eoHYeHEXe76Jn");
            User u2 = new User("test", "test@test.com", "eoHYeHEXe5g54");
            User u3 = new User("Roi", "roi@roi.com", "eoHYeHEXe5g54");
            User u4 = new User("Roberto", "rob@rob.com", "eoHYeHEXe5g54");

            // Guardar usuarios
            userService.addUser(u1);
            userService.addUser(u2);
            userService.addUser(u3);
            userService.addUser(u4);

            // Crear grupos personales automáticamente y asociarlos
            Group group1 = groupService.createGroup("USER_" + u1.getName(), u1);
            group1.getUsers().add(u1);
            u1.getGroups().add(group1);
            Group group2 = groupService.createGroup("USER_" + u2.getName(), u2);
            group2.getUsers().add(u2);
            u2.getGroups().add(group2);
            Group group3 = groupService.createGroup("USER_" + u3.getName(), u3);
            group3.getUsers().add(u3);
            u3.getGroups().add(group3);
            Group group4 = groupService.createGroup("USER_" + u4.getName(), u4);
            group4.getUsers().add(u4);
            u4.getGroups().add(group4);

            // Crear grupo de prueba
            Group g = groupService.createGroup("PRUEBA", u1);
            g.getUsers().add(u2); // Asociar u2 al grupo de prueba
            u2.getGroups().add(g); // Asociar el grupo de prueba a u2

            // Persistir grupo de prueba y sus relaciones
            groupService.addGroup(g);

            // Asegurarse de que los usuarios tengan sus grupos
            userService.addUser(u2);  // Guardar usuario u2 después de asociarlo a un grupo
            userService.addUser(u1);
            userService.addUser(u3);
            userService.addUser(u4);
        }
    }


    @GetMapping("/")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/projects";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.findUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/projects";
        }
        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "redirect:/";
    }

    @PostMapping("/register")
    public String register(@RequestParam String new_username,
                           @RequestParam String email,
                           @RequestParam String new_password,
                           @RequestParam String confirm_password,
                           Model model) {

        if (userService.findUserByUsername(new_username) != null) {
            model.addAttribute("error", "El usuario ya existe");
            return "redirect:/";
        }
        if (userService.findUserByEmail(email) != null) {
            model.addAttribute("error", "El email ya está registrado");
            return "redirect:/";
        }
        if (!new_password.equals(confirm_password)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/";
        }

        // Crear nuevo usuario
        User newUser = new User(new_username, email, new_password);
        userService.addUser(newUser);  // Guardamos el usuario en la tabla 'user'

        // Crear grupo personal para el nuevo usuario
        Group newGroup = groupService.createGroup("USER_" + newUser.getName(), newUser);  // Este grupo ya tiene el usuario asignado

        // Persistir el grupo
        groupService.addGroup(newGroup);  // Guardamos el grupo en la tabla 'group'

        // Ahora, las relaciones en la tabla 'user_group' se gestionan automáticamente
        newUser.getGroups().add(newGroup); // Añadir el grupo al usuario
        newGroup.getUsers().add(newUser);  // Añadir el usuario al grupo

        // Guardar los cambios en el usuario, lo que actualizará la relación
        userService.addUser(newUser); // Esta llamada actualizará la relación en 'user_group'

        model.addAttribute("success_message", "Registro exitoso, inicia sesión");
        return "redirect:/";
    }



}
