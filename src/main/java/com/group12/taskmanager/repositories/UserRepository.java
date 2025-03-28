package com.group12.taskmanager.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.group12.taskmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByName(String name);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.groups WHERE u.email = :email")
    User findByEmailWithGroups(@Param("email") String email);
}
