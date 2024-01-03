package br.com.marianadmoreira.bibliotecaob.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import br.com.marianadmoreira.bibliotecaob.model.Book;


public interface IBookRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);
    
}
