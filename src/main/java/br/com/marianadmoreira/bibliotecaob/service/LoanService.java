package br.com.marianadmoreira.bibliotecaob.service;

import java.util.List;
import java.util.Optional;

import br.com.marianadmoreira.bibliotecaob.model.Loan;

public interface LoanService {
    
    List<Loan> listLoans();

    int qtdLoansByUser(Long idUser);

    List<Loan> loansByUser(Long idUser);

    List<Loan> activeLoansByUser(Long idUser);

    List<Loan> activeLoans();

    void setLoanStatus();

    List<Loan> overdueLoans();

    List<Loan> returnedLoans();

    List<Loan> overdueReturned();

    List<Loan> onTimeReturned();

    Loan searchLoan(Loan Loan);

    Optional<Loan> searchLoanById(Long idLoan);

    void addLoan(Loan loan);

    void returnBook(Optional<Loan> loan);

    void calculeFine(Loan Loan);

    void calculeFineForAllLoans();

    void updateLoanStatusForAllLoans();

    void scheduleCalculeFine();

    void scheduleUpdateLoanStatus();   
}
