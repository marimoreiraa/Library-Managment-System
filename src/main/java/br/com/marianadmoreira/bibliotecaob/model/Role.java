package br.com.marianadmoreira.bibliotecaob.model;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "permissoes")
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long idRole;

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(mappedBy = "role",cascade = CascadeType.REMOVE)
    private List<UserModel> users;

    @Override
    public String toString() {
        return "Role{idRole=" + idRole + ", name='" + roleName + "'}";
    }
}
