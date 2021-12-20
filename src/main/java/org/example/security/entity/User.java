package org.example.security.entity;

import org.example.security.enums.Role;
import org.example.security.enums.UserStatus;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "seq_users", allocationSize = 1)
    private Long idUsers;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    public User() {
    }

    public User(Long idUsers, String email, String firstName, String lastName, String password, Role role, UserStatus status) {
        this.idUsers = idUsers;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Long getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(Long idUsers) {
        this.idUsers = idUsers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
