package br.com.marianadmoreira.bibliotecaob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;



public interface IUserRepository extends  JpaRepository<UserModel,Long>{
    UserModel findByUsername(String username);
}
