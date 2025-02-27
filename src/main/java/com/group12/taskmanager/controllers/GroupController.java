package com.group12.taskmanager.controllers;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.User;
import com.group12.taskmanager.services.GroupService;
import com.group12.taskmanager.services.GroupUserService;
import com.group12.taskmanager.services.UserService;
import jakarta.servlet.http.HttpSession;
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
    private final GroupUserService GROUP_USER_SERVICE;
    private final GroupService GROUP_SERVICE;

    public GroupController() {
        this.GROUP_USER_SERVICE = GroupUserService.getInstance();
        this.GROUP_SERVICE = GroupService.getInstance();
    }

    @GetMapping("/user_groups")
    public String getUserGroups(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/login"; // Redirigir si no hay sesión activa
        }

        List<Group> groups = GROUP_USER_SERVICE.getUserGroups(currentUser.getId());
        // Añadir propiedad isOwner a cada grupo
        for (Group group : groups) {
            group.setIsOwner(group.isOwner(currentUser.getId()));
            group.setIsPersonal(group.getName().equals("USER_" + currentUser.getName()));
        }

        model.addAttribute("groups", groups);
        model.addAttribute("user", currentUser);

        return "groups";
    }

    @PostMapping("/save_group")
    public String createGroup(@RequestParam String name, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return "redirect:/login";
        }

        GROUP_USER_SERVICE.createGroup(name, currentUser.getId());

        return "redirect:/user_groups";
    }

    @PostMapping("/leave_group/{groupId}")
    public ResponseEntity<?> leaveGroup(@PathVariable int groupId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"No autenticado\"}");
        }

        boolean success = GROUP_USER_SERVICE.leaveGroup(currentUser.getId(), groupId);

        if (success) {
            return ResponseEntity.ok("{\"success\": true}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Error al salir del grupo\"}");
        }
    }

    @PostMapping("/edit_group/{groupId}")
    public ResponseEntity<?> editGroup(@PathVariable int groupId, @RequestParam String name, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"No autenticado\"}");
        }

        boolean success = GROUP_USER_SERVICE.updateGroupName(groupId, name);

        if (success) {
            return ResponseEntity.ok("{\"success\": true}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Error al actualizar el grupo\"}");
        }
    }

    @PostMapping("/delete_group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable int groupId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");


        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"No autenticado\"}");
        }

        boolean success = GROUP_USER_SERVICE.deleteGroup(groupId, currentUser);

        if (success) {
            return ResponseEntity.ok("{\"success\": true}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Error al eliminar el grupo\"}");
        }
    }



    @GetMapping("/manage_members/{groupId}")
    public String getManageMembers(@PathVariable int groupId, Model model, HttpSession session) {
        Group currentGroup = GROUP_SERVICE.findGroupById(groupId);
        User currentUser = (User) session.getAttribute("user");
        List<User> groupUsers = GROUP_USER_SERVICE.getGroupUsers(groupId);

        model.addAttribute("users", groupUsers);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentGroup", currentGroup);

        return "users";
    }

    @DeleteMapping("/delete_member/{userId}")
    public ResponseEntity<?> deleteMember(@PathVariable int userId, @RequestParam int groupId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"No autenticado\"}");
        }

        boolean success = GROUP_USER_SERVICE.removeUserFromGroup(userId, groupId, currentUser);

        if (success) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Miembro eliminado correctamente"));
        } else {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Tarea no encontrada"));
        }
    }

    @GetMapping("/search_users")
    @ResponseBody
    public List<User> searchUsers(@RequestParam String q) {
        return UserService.getInstance().searchUsersByName(q);
    }

    @PostMapping("/add_members")
    public ResponseEntity<?> addMembersToGroup(@RequestBody Map<String, Object> payload) {
        try {
            // Obtener el userId del frontend en lugar de la sesión
            int currentUserId = Integer.parseInt(payload.get("currentUserId").toString());
            User currentUser = UserService.getInstance().findUserById(currentUserId);

            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Usuario no encontrado"));
            }

            int groupId = Integer.parseInt(payload.get("groupId").toString());

            List<Integer> userIds = ((List<?>) payload.get("userIds")).stream()
                    .map(id -> Integer.parseInt(id.toString()))
                    .toList();

            boolean success = GROUP_USER_SERVICE.addUsersToGroup(groupId, userIds, currentUser);

            if (success) {
                return ResponseEntity.ok(Collections.singletonMap("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Error al agregar usuarios al grupo"));
            }
        } catch (NumberFormatException | ClassCastException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Formato inválido en los datos enviados"));
        }
    }

}
