package com.group12.taskmanager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/projects"; // Redirige a la lista de proyectos si ya está autenticado
        }
        return "login"; // Renderiza login.mustache
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if ("admin".equals(username) && "1234".equals(password)) {
            session.setAttribute("user", username);
            return "redirect:/projects"; // Redirige a proyectos
        }
        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login"; // Muestra el login con el mensaje de error
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Cierra sesión
        return "redirect:/";
    }
}
