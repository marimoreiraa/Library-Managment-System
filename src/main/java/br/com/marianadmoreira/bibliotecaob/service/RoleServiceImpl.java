package br.com.marianadmoreira.bibliotecaob.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.marianadmoreira.bibliotecaob.model.Role;
import br.com.marianadmoreira.bibliotecaob.repository.IRoleRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RoleServiceImpl implements RoleService{
    
    @Autowired
    private IRoleRepository roleRepository;

    public Role findRoleById(Long id){
        return roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
    }
}
