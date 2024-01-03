package br.com.marianadmoreira.bibliotecaob.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.marianadmoreira.bibliotecaob.model.Book;
import br.com.marianadmoreira.bibliotecaob.model.BookStatus;
import br.com.marianadmoreira.bibliotecaob.model.Loan;
import br.com.marianadmoreira.bibliotecaob.model.LoanStatus;
import br.com.marianadmoreira.bibliotecaob.repository.ILoanRepository;

@Service
public class LoanServiceImpl implements LoanService{

    @Autowired
    ILoanRepository loanRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Loan> listLoans() {
        return this.loanRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)   
    public int qtdLoansByUser(Long idUser) {
        var allLoans = this.listLoans();
        List<Loan> loans = new ArrayList<>();

        for( Loan loan: allLoans){
            if(loan.getUser().getIdUser().equals(idUser)){
                loans.add(loan);
            }
        }
        return loans.size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> loansByUser(Long idUser){
        var allLoans = this.listLoans();
        List<Loan> loans = new ArrayList<>();

        for( Loan loan: allLoans){
            if(loan.getUser().getIdUser().equals(idUser)){
                loans.add(loan);
            }
        }
        return loans;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> activeLoansByUser(Long idUser) {
        var userLoans = this.loansByUser(idUser);
        List<Loan> loans = new ArrayList<>();

        for(Loan loan: userLoans){
            if(loan.getReturnDate() == null){
                loans.add(loan);
            }
        }
        return loans;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> activeLoans() {
        var allLoans = this.listLoans();
        List<Loan> loans = new ArrayList<>();

        for(Loan loan: allLoans){
            if(loan.getReturnDate() == null){
                loans.add(loan);
            }
        }
        return loans;
    }

    @Override
    @Transactional(readOnly = false)
    public List<Loan> overdueLoans() {
        var allLoans = this.listLoans();
        List<Loan> loans = new ArrayList<>();

        for(Loan loan: allLoans){
            if(loan.getStatus().equals(LoanStatus.ATRASADO)){
                loans.add(loan);
            }
        }
        return loans;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> returnedLoans() {
        var allLoans = this.listLoans();
        List<Loan> loans = new ArrayList<>();
        for(Loan loan: allLoans){
            if(loan.getStatus().equals(LoanStatus.DEVOLVIDO_COM_MULTA) || loan.getStatus().equals(LoanStatus.DEVOLVIDO_SEM_MULTA)){
                loans.add(loan);
            }
        }
        return loans;
    }

    @Override
    @Transactional(readOnly = true)
    public Loan searchLoan(Loan loan) {
        return this.loanRepository.findById(loan.getIdLoan()).orElse(null);
    }

    @Override
    @Transactional
    public void addLoan(Loan loan) {

        this.loanRepository.save(loan);
    }

    @Override
    @Transactional
    public void returnBook(Optional<Loan> loanOptional) {
        loanOptional.ifPresent(loan -> {
            Book book = loan.getBook();
            
            if (book != null) {
                book.setStatus(BookStatus.DISPONIVEL);
            }
    
            loan.setReturnDate(LocalDate.now());
            if (loan.getFine() == 0) {
                loan.setStatus(LoanStatus.DEVOLVIDO_SEM_MULTA);
            } else {
                loan.setStatus(LoanStatus.DEVOLVIDO_COM_MULTA);
            }
        });
    }
    

    @Override
    @Transactional
    public void calculeFine(Loan loan) {
        LocalDate today = LocalDate.now();
        int daysLate = (int) ChronoUnit.DAYS.between(today, loan.getReturnLimitDate());

        double fine = 0;

        switch (daysLate) {
            case 0:
                fine = 0;
                break;
            default:
                fine = daysLate * 1.0; // Convertido para double para manter a precisão
                break;
        }

        loan.setFine(fine);
    }

    @Override
    @Transactional
    public void setLoanStatus() {
        var allLoans = this.listLoans();
        LocalDate today = LocalDate.now();
        for(Loan loan: allLoans){
            int daysLoaned = (int) ChronoUnit.DAYS.between(today, loan.getLoanDate());
            if(daysLoaned > 10){
                loan.setStatus(LoanStatus.ATRASADO);
            }
            else{
                loan.setStatus(LoanStatus.REGULAR);
            }
        }

    }

    @Override
    @Transactional
    public void calculeFineForAllLoans() {
        var allLoans = this.listLoans();
        for (Loan loan : allLoans) {
            calculeFine(loan);
        }
    }

    @Override
    @Transactional
    public void updateLoanStatusForAllLoans() {
        var allLoans = this.listLoans();
        LocalDate today = LocalDate.now();
        for (Loan loan : allLoans) {
            int daysLoaned = (int) ChronoUnit.DAYS.between(loan.getLoanDate(), today);
            if (daysLoaned > 10) {
                loan.setStatus(LoanStatus.ATRASADO);
            } else {
                loan.setStatus(LoanStatus.REGULAR);
            }
        }
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // Executa todo dia à meia-noite
    public void scheduleCalculeFine() {
        calculeFineForAllLoans();
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * *") // Executa todo dia à meia-noite
    public void scheduleUpdateLoanStatus() {
        updateLoanStatusForAllLoans();
    }
    @Override
    public Optional<Loan> searchLoanById(Long idLoan) {
       return this.loanRepository.findById(idLoan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> overdueReturned() {
        var allLoansReturned = this.returnedLoans();
        List<Loan> overdueLoans = new ArrayList<>();
        for(Loan loan: allLoansReturned){
            if(loan.getStatus().equals(LoanStatus.DEVOLVIDO_COM_MULTA)){
                overdueLoans.add(loan);
            }
        }
        return overdueLoans;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Loan> onTimeReturned() {
        var allLoansReturned = this.returnedLoans();
        List<Loan> onTimeLoans = new ArrayList<>();
        for(Loan loan: allLoansReturned){
            if(loan.getStatus().equals(LoanStatus.DEVOLVIDO_SEM_MULTA)){
                onTimeLoans.add(loan);
            }
        }
        return onTimeLoans;
    }

}
