package com.engilyin.usefularticles.dao.entities.users;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {

    @Id
    String username;

    String password;
    
    String name;
    
    String role;
    
    LocalDate created;
    
    boolean locked;
}
