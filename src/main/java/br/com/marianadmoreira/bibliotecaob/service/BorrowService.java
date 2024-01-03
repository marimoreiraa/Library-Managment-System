package br.com.marianadmoreira.bibliotecaob.service;

import java.util.List;

import br.com.marianadmoreira.bibliotecaob.model.Book;
import br.com.marianadmoreira.bibliotecaob.model.Borrow;
import br.com.marianadmoreira.bibliotecaob.model.UserModel;

public interface BorrowService {
    
    List<Borrow> listBorrows();

    List<Borrow> borrowsByUser(Long idUser);

    List<Borrow> activeBorrows();

    void setBorrowStatus();

    List<Borrow> overdueBorrows();

    List<Borrow> returnedBorrows();

    Borrow searchBorrow(Borrow borrow);

    void addBorrow(Book book, UserModel user);

    void returnBook(Borrow borrow);

    void calculeFine(Borrow borrow);


    
}
