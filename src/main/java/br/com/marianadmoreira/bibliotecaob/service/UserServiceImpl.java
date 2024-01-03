package br.com.marianadmoreira.bibliotecaob.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;
import br.com.marianadmoreira.bibliotecaob.repository.IUserRepository;


@Service
public class UserServiceImpl implements UserService{

    @Autowired
    IUserRepository userRepository;

    
    private BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<UserModel> listUsers() {
        return this.userRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public UserModel searchUser(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserModel> searchUsers(String name) {
        var allUsers = this.listUsers();
        List<UserModel> users = new ArrayList<>();

        if(name.isBlank()){
            return allUsers;
        }
        
        for(UserModel user: allUsers){
            if(user.getName().toUpperCase().contains(name.toUpperCase())){
                users.add(user);
            }
        }

        return users;
    }

    @Override
    @Transactional
    public UserModel addUser(UserModel user) {

        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);

    }


    @Override
    @Transactional
    public void updateUser(UserModel user) {
        UserModel userModel = userRepository.findById(user.getIdUser()).get();
        userModel.setName(user.getName());
        userModel.setEmail(user.getEmail());
        userModel.setPassword(passwordEncoder().encode(user.getPassword()));
        userModel.setRole(user.getRole());

        userRepository.save(userModel);
    }


    @Override
    @Transactional
    public void deleteUser(UserModel user) {
        userRepository.delete(user);
    }


    @Override
    public UserModel searchUserById(Long idUser) {
        UserModel user = userRepository.findById(idUser).orElse(null);

        return user;
    }


    @Override
    public boolean checkIfUserExists(String username) {
        UserModel userExists = userRepository.findByUsername(username);
        if(userExists == null){
            return false;
        }
        else{
            return true;
        }
    }


}
