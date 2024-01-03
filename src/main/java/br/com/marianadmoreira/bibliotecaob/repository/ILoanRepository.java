package br.com.marianadmoreira.bibliotecaob.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.marianadmoreira.bibliotecaob.model.Loan;
import br.com.marianadmoreira.bibliotecaob.model.LoanStatus;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;

public interface ILoanRepository extends JpaRepository<Loan,Long>{
    List<Loan> findByUserAndStatus(UserModel user, LoanStatus status);
    
}
