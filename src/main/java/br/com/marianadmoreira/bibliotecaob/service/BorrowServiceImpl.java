package br.com.marianadmoreira.bibliotecaob.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.marianadmoreira.bibliotecaob.model.Book;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;
import br.com.marianadmoreira.bibliotecaob.model.Borrow;
import br.com.marianadmoreira.bibliotecaob.model.BorrowStatus;
import br.com.marianadmoreira.bibliotecaob.repository.IBorrowRepository;

public class BorrowServiceImpl implements BorrowService{

    @Autowired
    IBorrowRepository borrowRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Borrow> listBorrows() {
        return this.borrowRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)   
    public List<Borrow> borrowsByUser(Long idUser) {
        var allBorrows = this.listBorrows();
        List<Borrow> borrows = new ArrayList<>();

        for( Borrow borrow: allBorrows){
            if(borrow.getUser().getIdUser().equals(idUser)){
                borrows.add(borrow);
            }
        }
        return borrows;
       }

    @Override
    @Transactional(readOnly = true)
    public List<Borrow> activeBorrows() {
        var allBorrows = this.listBorrows();
        List<Borrow> borrows = new ArrayList<>();

        for(Borrow borrow: allBorrows){
            if(borrow.getReturnDate() == null){
                borrows.add(borrow);
            }
        }
        return borrows;
    }

    @Override
    @Transactional(readOnly = false)
    public List<Borrow> overdueBorrows() {
        var allBorrows = this.listBorrows();
        List<Borrow> borrows = new ArrayList<>();

        for(Borrow borrow: allBorrows){
            if(borrow.getStatus().equals(BorrowStatus.ATRASADO)){
                borrows.add(borrow);
            }
        }
        return borrows;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Borrow> returnedBorrows() {
        var allBorrows = this.listBorrows();
        List<Borrow> borrows = new ArrayList<>();
        for(Borrow borrow: allBorrows){
            if(borrow.getStatus().equals(BorrowStatus.DEVOLVIDO_COM_MULTA) || borrow.getStatus().equals(BorrowStatus.DEVOLVIDO_SEM_MULTA)){
                borrows.add(borrow);
            }
        }
        return borrows;
    }

    @Override
    @Transactional(readOnly = true)
    public Borrow searchBorrow(Borrow borrow) {
        return this.borrowRepository.findById(borrow.getIdBorrow()).orElse(null);
    }

    @Override
    @Transactional
    public void addBorrow(Book book, UserModel user) {
        Borrow borrow = new Borrow();
        borrow.setUser(user);
        borrow.setBook(book);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setStatus(BorrowStatus.REGULAR);
        borrow.setReturnLimitDate(borrow.getBorrowDate().plusDays(10));

        this.borrowRepository.save(borrow);
    }

    @Override
    @Transactional
    public void returnBook(Borrow borrow) {
        borrow.setReturnDate(LocalDate.now());
        if(borrow.getFine() == null){
            borrow.setStatus(BorrowStatus.DEVOLVIDO_SEM_MULTA);
        }
        else{
            borrow.setStatus(BorrowStatus.DEVOLVIDO_COM_MULTA);
        }

    }

    @Override
    @Transactional
    public void calculeFine(Borrow borrow) {
        LocalDate today = LocalDate.now();
        int daysLate = (int) ChronoUnit.DAYS.between(today, borrow.getReturnLimitDate());

        double fine = 0;

        switch (daysLate) {
            case 0:
                fine = 0;
                break;
            default:
                fine = daysLate * 1.0; // Convertido para double para manter a precisão
                break;
        }

        borrow.setFine(fine);
    }

    @Override
    @Transactional
    public void setBorrowStatus() {
        var allBorrows = this.listBorrows();
        LocalDate today = LocalDate.now();
        for(Borrow borrow: allBorrows){
            int daysBorrowed = (int) ChronoUnit.DAYS.between(today, borrow.getBorrowDate());
            if(daysBorrowed > 10){
                borrow.setStatus(BorrowStatus.ATRASADO);
            }
            else{
                borrow.setStatus(BorrowStatus.REGULAR);
            }
        }

    }


    

}
