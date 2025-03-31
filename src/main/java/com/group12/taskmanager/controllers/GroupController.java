package com.group12.taskmanager.controllers;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.User;
import com.group12.taskmanager.services.GroupService;
import com.group12.taskmanager.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    @GetMapping("/user_groups")
    public String getUserGroups(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        List<Group> groups = currentUser.getGroups();
        for (Group group : groups) {
            group.setIsOwner(group.getOwner().getId().equals(currentUser.getId()));
            group.setIsPersonal(group.getName().equals("USER_" + currentUser.getName()));
        }

        model.addAttribute("groups", groups);
        model.addAttribute("user", currentUser);
        return "groups";
    }

    @PostMapping("/save_group")
    public String createGroup(@RequestParam String name, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        groupService.createGroup(name, currentUser);
        return "redirect:/user_groups";
    }

    @PostMapping("/leave_group/{groupId}")
    public ResponseEntity<?> leaveGroup(@PathVariable int groupId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"No autenticado\"}");

        Group group = groupService.findGroupById(groupId);
        if (group == null || group.getOwner().getId().equals(currentUser.getId()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"No se puede salir del grupo\"}");

        groupService.removeUserFromGroup(group, currentUser);

        // Actualizar lista de grupos del usuario en sesión
        currentUser.getGroups().removeIf(g -> g.getId().equals(groupId));
        session.setAttribute("user", currentUser);

        return ResponseEntity.ok("{\"success\": true}");
    }

    @PostMapping("/edit_group/{groupId}")
    public ResponseEntity<?> editGroup(@PathVariable int groupId, @RequestParam String name, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"No autenticado\"}");

        boolean success = groupService.updateGroupName(groupId, name);
        // Actualizar nombre del grupo en la sesión del usuario
        currentUser.getGroups().stream()
                .filter(g -> g.getId().equals(groupId))
                .findFirst()
                .ifPresent(g -> g.setName(name));

        session.setAttribute("user", currentUser);

        return success ? ResponseEntity.ok("{\"success\": true}")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Error al actualizar el grupo\"}");
    }

    @PostMapping("/delete_group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable int groupId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"No autenticado\"}");

        boolean success = groupService.deleteGroup(groupId, currentUser);
        // Actualizar lista de grupos del usuario en sesión
        currentUser.getGroups().removeIf(g -> g.getId().equals(groupId));
        session.setAttribute("user", currentUser);

        return success ? ResponseEntity.ok("{\"success\": true}")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Error al eliminar el grupo\"}");
    }

    @GetMapping("/manage_members/{groupId}")
    public String getManageMembers(@PathVariable int groupId, Model model, HttpSession session) {
        Group group = groupService.findGroupById(groupId);
        User currentUser = (User) session.getAttribute("user");

        model.addAttribute("users", group.getUsers());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentGroup", group);
        return "group_members";
    }

    @DeleteMapping("/delete_member/{userId}")
    public ResponseEntity<?> deleteMember(@PathVariable int userId, @RequestParam int groupId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "No autenticado"));
        }

        Group group = groupService.findGroupById(groupId);
        User user = userService.findUserById(userId);

        if (group == null || user == null || !group.getOwner().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "No autorizado o grupo/usuario no encontrado"));
        }

        group.getUsers().remove(user);
        groupService.addGroup(group); // solo guarda el propietario

        return ResponseEntity.ok(Collections.singletonMap("message", "Miembro eliminado correctamente"));
    }

    @GetMapping("/search_users")
    @ResponseBody
    public List<User> searchUsers(@RequestParam String q, @RequestParam int groupId) {
        Group group = groupService.findGroupById(groupId);
        return userService.searchUsersByNameExcludingGroup(q, group);
    }

    @PostMapping("/add_members")
    public ResponseEntity<?> addMembersToGroup(@RequestBody Map<String, Object> payload) {
        try {
            int currentUserId = Integer.parseInt(payload.get("currentUserId").toString());
            User currentUser = userService.findUserById(currentUserId);
            int groupId = Integer.parseInt(payload.get("groupId").toString());
            Group group = groupService.findGroupById(groupId);

            if (currentUser == null || group == null || !group.getOwner().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "No autorizado"));
            }

            List<Integer> userIds = ((List<?>) payload.get("userIds")).stream()
                    .map(id -> Integer.parseInt(id.toString()))
                    .toList();

            for (Integer userId : userIds) {
                User user = userService.findUserById(userId);
                if (user != null && !group.getUsers().contains(user)) {
                    group.getUsers().add(user);
                    user.getGroups().add(group);
                }
            }
            groupService.addGroup(group);

            return ResponseEntity.ok(Collections.singletonMap("success", true));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Error procesando la solicitud"));
        }
    }



    
    @GetMapping("/edit_user")
    public String showEditUserPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        model.addAttribute("user", currentUser);
        return "edit_user";
    }

    @PostMapping("/delete_user/{userId}")
    public String deleteUser(@PathVariable int userId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/error";

        boolean deleted = userService.deleteUser(userId, currentUser);
        if (deleted && currentUser.getId().equals(userId)) session.invalidate();
        return deleted ? "redirect:/" : "redirect:/error";
    }

    @PostMapping("/update_user")
    public String updateUser(@RequestParam String name, @RequestParam String email, @RequestParam String password, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            currentUser.setName(name);
            currentUser.setEmail(email);
            if (!password.isBlank()) {
                currentUser.setPassword(password); // En producción, encriptar aquí
            }

            userService.updateUser(currentUser);
            session.setAttribute("user", currentUser);
        }
        return "redirect:/";
    }
}
