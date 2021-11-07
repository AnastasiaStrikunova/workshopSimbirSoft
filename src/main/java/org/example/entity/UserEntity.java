package org.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "userr")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_gen")
    @SequenceGenerator(name = "user_id_gen", sequenceName = "seq_user", allocationSize = 1)
    private Long idUser;
    private String name;
    private String priority;

    public UserEntity() {
    }

    public UserEntity(Long idUser, String name, String priority) {
        this.idUser = idUser;
        this.name = name;
        this.priority = priority;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
