package br.com.marianadmoreira.bibliotecaob.service;

import java.util.List;


import br.com.marianadmoreira.bibliotecaob.model.UserModel;

public interface UserService {
    
    List<UserModel> listUsers();

    UserModel addUser(UserModel user);

    void updateUser(UserModel user);
    
    void deleteUser(UserModel user);

    UserModel searchUser(String username);

    UserModel searchUserById(Long idUser);

    List<UserModel> searchUsers(String name);
    
    boolean checkIfUserExists(String username);
    
}
