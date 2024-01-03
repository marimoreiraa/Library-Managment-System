package br.com.marianadmoreira.bibliotecaob.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.marianadmoreira.bibliotecaob.model.Borrow;
import br.com.marianadmoreira.bibliotecaob.model.BorrowStatus;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;

public interface IBorrowRepository extends JpaRepository<Borrow,Long>{
    List<Borrow> findByUserAndStatus(UserModel user, BorrowStatus status);
    
}
