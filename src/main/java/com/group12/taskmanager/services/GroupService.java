package com.group12.taskmanager.services;

import com.group12.taskmanager.models.Group;
import com.group12.taskmanager.models.User;
import com.group12.taskmanager.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    public Group findGroupById(int id) {
        return groupRepository.findById(id).orElse(null);
    }

    public boolean removeGroup(int groupId, String userName) {
        Group group = findGroupById(groupId);
        if (group != null && !group.getName().equals("USER_" + userName)) {
            groupRepository.delete(group);
            return true;
        }
        return false;
    }

    public Group createGroup(String name, User owner) {
        Group newGroup = new Group(name, owner);
        newGroup.getUsers().add(owner);
        return groupRepository.save(newGroup);
    }

    public boolean updateGroupName(int groupId, String newName) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            group.setName(newName);
            groupRepository.save(group);
            return true;
        }
        return false;
    }

    public boolean deleteGroup(int groupId, User currentUser) {
        Group group = findGroupById(groupId);
        if (group != null && group.getOwner().getId().equals(currentUser.getId())) {
            groupRepository.delete(group);
            return true;
        }
        return false;
    }
}
