package br.com.marianadmoreira.bibliotecaob.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.marianadmoreira.bibliotecaob.model.Role;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;
import br.com.marianadmoreira.bibliotecaob.service.RoleService;
import br.com.marianadmoreira.bibliotecaob.service.UserService;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        var users = userService.listUsers();
        var totalUsers = users.size();

        model.addAttribute("users", users);
        model.addAttribute("totalUsers", totalUsers);

        return "users/users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        UserModel newUser = new UserModel();
        model.addAttribute("user", newUser);
        return "users/add";
    }

    @PostMapping("/users/add")
    public String saveUser(@ModelAttribute("user") UserModel user, Errors errors) {
        if (errors.hasErrors()) {
            return "users/add";
        }

        if(userService.checkIfUserExists(user.getUsername())){
            return "error/userExists";
        }
        else{
            Long roleId = user.getRole().getIdRole();
            Role role = roleService.findRoleById(roleId);

            if (role == null) {
                return "users/add";
            }

            user.setRole(role);
            userService.addUser(user);
            return "redirect:/users";  
        }
        
    }

    @GetMapping("/users/edit/{idUser}")
    public String editUser(UserModel user, Model model){
        user = this.userService.searchUserById(user.getIdUser());
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/users/edit")
    public String afterEdit(@Valid UserModel user, Errors errors){
        if (errors.hasErrors()) {
            return "edit";
        }

        this.userService.updateUser(user);
        return "redirect:/users";

    }
    @GetMapping("/users/delete/{idUser}")
    public String removeUser(UserModel user){
        this.userService.deleteUser(user);

        return "redirect:/users";
    }

    @GetMapping("/users/search")
    public String searchByTitle(Model model,@RequestParam String name) {
        var users = this.userService.searchUsers(name);
        var totalUsers = users.size();

        model.addAttribute("users", users);
        model.addAttribute("totalUsers", totalUsers);

        return "users/search";
    }
}
