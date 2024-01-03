package br.com.marianadmoreira.bibliotecaob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.marianadmoreira.bibliotecaob.model.Role;


@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}

    

